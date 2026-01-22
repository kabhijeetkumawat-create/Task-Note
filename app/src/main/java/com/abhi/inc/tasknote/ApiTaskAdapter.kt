package com.abhi.inc.tasknote

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ApiTaskAdapter : RecyclerView.Adapter<ApiTaskAdapter.ViewHolder>() {

    private var tasks = listOf<ApiTask>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvTaskTitle)
        val checkBox: CheckBox = view.findViewById(R.id.cbCompleted)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_api_task, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]
        holder.title.text = task.title
        holder.checkBox.isChecked = task.completed
        holder.checkBox.isEnabled = false // Make it read-only
    }

    override fun getItemCount() = tasks.size

    fun submitList(newTasks: List<ApiTask>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}