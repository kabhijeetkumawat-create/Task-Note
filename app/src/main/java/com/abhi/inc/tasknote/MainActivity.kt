package com.abhi.inc.tasknote

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhi.inc.tasknote.databinding.ActivityMainBinding
import com.abhi.inc.tasknote.ui.TaskAdapter
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: TaskAdapter
    private var taskList = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        setupRecyclerView()
        setupFab()
        listenToTasks()
    }

    private fun setupRecyclerView() {
        adapter = TaskAdapter { task ->
            deleteTask(task)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun listenToTasks() {
        val userId = auth.currentUser?.uid ?: return


        db.collection("tasks")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Toast.makeText(this, "Error loading tasks: ${error.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    taskList.clear()
                    for (document in snapshots) {
                        val task = document.toObject(Task::class.java)
                        task.id = document.id
                        taskList.add(task)
                    }
                    adapter.updateList(taskList)
                }
            }
    }

    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            showAddTaskDialog()
        }
    }

    private fun showAddTaskDialog() {

         val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null) // Create this simple XML below
        val etTitle = dialogView.findViewById<EditText>(R.id.etTitleInput)
        val etDesc = dialogView.findViewById<EditText>(R.id.etDescInput)

        AlertDialog.Builder(this)
            .setTitle("New Task")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val title = etTitle.text.toString().trim()
                val description = etDesc.text.toString().trim()
                if (title.isNotEmpty()) {
                    saveTaskToFirestore(title, description)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun saveTaskToFirestore(title: String, description: String) {
        val userId = auth.currentUser?.uid ?: return

        val newTask = Task(
            title = title,
            description = description,
            userId = userId,
            timestamp = Timestamp.now()
        )

        db.collection("tasks").add(newTask)
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add task", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteTask(task: Task) {
        db.collection("tasks").document(task.id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error deleting task", Toast.LENGTH_SHORT).show()
            }
    }
}