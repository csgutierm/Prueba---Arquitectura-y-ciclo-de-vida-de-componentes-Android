package com.desafiolatam.coroutines.view.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.desafiolatam.coroutines.R
import com.desafiolatam.coroutines.data.TaskEntity
import com.desafiolatam.coroutines.repository.TaskRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewTaskActivity : AppCompatActivity() {

    @Inject
    lateinit var taskRepository: TaskRepository

    //private lateinit var binding: NewTaskActivityBinding
    private lateinit var editTitulo: EditText;
    private lateinit var editDescripcion: EditText;
    private lateinit var button: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)

        editDescripcion = findViewById(R.id.etDescription);
        editTitulo = findViewById(R.id.etTitle);
        button = findViewById(R.id.buttonEnviar);

        button.setOnClickListener  {

            val task = TaskEntity(
                id = 0,  // 0 para generar automaticamente
                title = editTitulo.text.toString(),
                description = editDescripcion.text.toString(),
                completada = false
            )

            lifecycleScope.launch {
                taskRepository.addTask(task)

                Toast.makeText(this@NewTaskActivity, "Tarea agregada con éxito", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@NewTaskActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }

    }

}