package com.example.di_to_do_list.model

data class Task(
    val title: String,
    val hour: String,
    val date: String,
    val id: Int = 0
) {
    // Tem haver com a opção de atualizar a tarefa
    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(javaClass != other?.javaClass) return false

        other as Task

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}
