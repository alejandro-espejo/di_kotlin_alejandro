package com.example.di_to_do_list.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.di_to_do_list.databinding.ActivityMainBinding
import com.example.di_to_do_list.datasource.TaskDataSource

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTasks.adapter = adapter
        updateList()

        insertListeners()

        // ATIVIDADE EXTRA:
        // PESQUISAR SOBRE: DATA STORE E ROOM
    }

    // Ao clicar o botão adicionar, ele vai para a tela de criar tarefa
    private fun insertListeners() {
        binding.fab.setOnClickListener {
            // StartActivityForResult - Tem haver com o retorno das informações na tela de Criar Tarefa para o RecyclerView no MainActivity
            startActivityForResult(Intent(this, AddTaskActivity::class.java), CREATE_NEW_TASK)
        }

        // Botão editar
        adapter.listenerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)
        }

        // Botão deletar
        adapter.listenerDelete = {
            TaskDataSource.deleteTask(it)
            updateList()
        }
    }

    // Verificar como este metodo é chamado
    // Tem haver com o retorno das informações na tela de Criar Tarefa para o RecyclerView no MainActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if( requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK) {
            //binding.rvTasks.adapter = adapter
            //adapter.submitList(TaskDataSource.getList())
            updateList()
        }
    }

    private fun updateList() {
        val list = TaskDataSource.getList()

        if (list.isEmpty()) {
            binding.includeEmpty.emptyState.visibility = View.VISIBLE
            //binding.rvTasks.visibility = View.GONE
        } else {
            binding.includeEmpty.emptyState.visibility = View.GONE
            //binding.rvTasks.visibility = View.VISIBLE
        }
        //binding.includeEmpty.emptyState.visibility = View.GONE
        adapter.submitList(list)
    }

    companion object {
        private const val CREATE_NEW_TASK = 1000
    }
}