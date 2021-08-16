package com.example.di_to_do_list.ui

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.di_to_do_list.databinding.ActivityAddTaskBinding
import com.example.di_to_do_list.datasource.TaskDataSource
import com.example.di_to_do_list.extensions.format
import com.example.di_to_do_list.extensions.text
import com.example.di_to_do_list.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    lateinit var preferenciasTarefa: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Criando a preferencia
        preferenciasTarefa = getSharedPreferences("tarefaPreferences", Context.MODE_PRIVATE)

        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.tilTitle.text = it.title
                binding.tilDate.text = it.date
                binding.tilHour.text = it.hour
            }
        }

        insertLinsteners()
    }

    private fun insertLinsteners() {

        // Abrir o calendário
        binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            // Irá retornar a informação do dia escolhido
            datePicker.addOnPositiveButtonClickListener {
                // Irá selecionar a date corretamente
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1

                binding.tilDate.text = Date(it + offset).format()
            }
            // exibe o calendário
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        // Iniciando o TimerPicker - Para abrir o Horário
        binding.tilHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.addOnPositiveButtonClickListener {
                // Retorna no campo de hora o horário escolhido
                val minute = if(timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute

                val hour =  if(timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour

                binding.tilHour.text = "$hour:$minute"
            }
            timePicker.show(supportFragmentManager, null)
        }

        // Quando clicar no botão Cancelar, irá voltar a tela anterior
        binding.btnCancel.setOnClickListener {
            binding.tilTitle.text = ""
            binding.tilDate.text = ""
            binding.tilHour.text = ""
            // Apagar a preferência gravada anteriormente
            val editor = preferenciasTarefa.edit()
            editor.remove("PREFERENCIAS_TITULO")
            editor.remove("PREFERENCIAS_DATA")
            editor.remove("PREFERENCIAS_HORA")
            editor.apply()
            finish()
        }

        //Botão de adicionar
        binding.btnNewTask.setOnClickListener {
            val task = Task(
                title = binding.tilTitle.text,
                date = binding.tilDate.text,
                hour =binding.tilHour.text,
                id = intent.getIntExtra(TASK_ID, 0)
            )
            TaskDataSource.insertTask(task)

            binding.tilTitle.text = ""
            binding.tilDate.text = ""
            binding.tilHour.text = ""
            // Apagar a preferência gravada anteriormente
            val editor = preferenciasTarefa.edit()
            editor.remove("PREFERENCIAS_TITULO")
            editor.remove("PREFERENCIAS_DATA")
            editor.remove("PREFERENCIAS_HORA")
            editor.apply()

            // Irá retornar para o ReyclerView na MainActivity a tarefa que foi criada
            setResult(Activity.RESULT_OK)

            finish()
        }
    }

    companion object {
        const val TASK_ID = "task_id"
    }

    // Adicionando no Shared Preferences
    override fun onPause() {
        super.onPause()

        if(binding.tilTitle.text.isNotEmpty()) {
            val editor = preferenciasTarefa.edit()
            editor.putString("PREFERENCIAS_TITULO", binding.tilTitle.text)
            editor.apply()
        }

        if(binding.tilDate.text.isNotEmpty()) {
            val editor = preferenciasTarefa.edit()
            editor.putString("PREFERENCIAS_DATA", binding.tilDate.text)
            editor.apply()
        }

        if(binding.tilHour.text.isNotEmpty()) {
            val editor = preferenciasTarefa.edit()
            editor.putString("PREFERENCIAS_HORA", binding.tilHour.text)
            editor.apply()
        }
    }

    override fun onResume() {
        super.onResume()

        binding.tilTitle.editText?.setText(preferenciasTarefa.getString("PREFERENCIAS_TITULO", null))
        binding.tilDate.editText?.setText(preferenciasTarefa.getString("PREFERENCIAS_DATA", null))
        binding.tilHour.editText?.setText(preferenciasTarefa.getString("PREFERENCIAS_HORA", null))
    }
}