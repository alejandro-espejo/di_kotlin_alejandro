package com.example.di_to_do_list.datasource

import com.example.di_to_do_list.model.Task

object TaskDataSource {
    private val list = arrayListOf<Task>()

    // obter os objetos
    fun getList() = list.toList()

    // Popular a lista
    fun insertTask(task: Task) {
        // Se for verdadeiro, irá fazer um cadastro na lista, caso não, irá fazer uma atualização
        if(task.id == 0) {
            // copy - defini um atributo mesmo que seja uma variavel do tipo 'val'
            list.add(task.copy(id = list.size + 1))
        } else {
            list.remove(task)
            list.add(task)
        }
    }

    fun findById(taskId: Int) = list.find { it.id == taskId }

    fun deleteTask(task: Task) {
        list.remove(task)
    }

}