package com.abhi.inc.tasknote

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhi.inc.tasknote.databinding.ActivityApiTaskBinding

class ApiTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApiTaskBinding
    private val viewModel: ApiViewModel by viewModels()
    private val adapter = ApiTaskAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApiTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topAppBarApi.setNavigationOnClickListener {
            finish()
        }

        binding.rvApiList.layoutManager = LinearLayoutManager(this)
        binding.rvApiList.adapter = adapter

        viewModel.tasks.observe(this) { tasks ->
            adapter.submitList(tasks)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBarApi.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}