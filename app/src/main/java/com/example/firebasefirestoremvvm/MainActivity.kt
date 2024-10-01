package com.example.firebasefirestoremvvm

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasefirestoremvvm.Adapter.DataAdapter
import com.example.firebasefirestoremvvm.Data.Data
import com.example.firebasefirestoremvvm.ViewModel.DataViewModel
import com.example.firebasefirestoremvvm.databinding.ActivityMainBinding
import com.google.firebase.Timestamp

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val dataViewModel: DataViewModel by viewModels()
    private lateinit var adapter: DataAdapter
    private fun clearInputField() {
        binding.idEtxt.text?.clear()
        binding.nameEtxt.text?.clear()
        binding.emailEtxt.text?.clear()
        binding.subjectEtxt.text?.clear()
        binding.birthdateEtxt.text?.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter = DataAdapter(listOf(), this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        dataViewModel.dataList.observe(this, Observer {
            adapter.updateData(it)
        })

        binding.addBtn.setOnClickListener {
            val stuid = binding.idEtxt.text.toString()
            val name = binding.nameEtxt.text.toString()
            val email = binding.emailEtxt.text.toString()
            val subject = binding.subjectEtxt.text.toString()
            val birthday = binding.birthdateEtxt.text.toString()

            if (stuid.isNotEmpty() && name.isNotEmpty() && email.isNotEmpty() && subject.isNotEmpty() && birthday.isNotEmpty()) {
                val data = Data(null, stuid, name, email, subject, birthday, Timestamp.now())
                dataViewModel.addData(data, onSuccess = {
                    clearInputField()
                    Toast.makeText(this@MainActivity, "Data saved successfully", Toast.LENGTH_SHORT)
                        .show()
                }, onFailure = {
                    Toast.makeText(this@MainActivity, "failed to add data", Toast.LENGTH_SHORT)
                        .show()


                })

            }


        }


    }

    fun onEditItemClick(data: Data) {

        binding.idEtxt.setText(data.stuid)
        binding.nameEtxt.setText(data.name)
        binding.emailEtxt.setText(data.email)
        binding.subjectEtxt.setText(data.subject)
        binding.birthdateEtxt.setText(data.birthday)

        binding.addBtn.setOnClickListener {
            val updateData = Data(
                data.id, binding.idEtxt.text.toString(),
                binding.nameEtxt.text.toString(), binding.emailEtxt.text.toString(),
                binding.subjectEtxt.text.toString(), binding.birthdateEtxt.text.toString()
            )
            dataViewModel.updateData(updateData)
            clearInputField()
            Toast.makeText(this@MainActivity, "Data updated successfully", Toast.LENGTH_SHORT)
                .show()

        }


    }

    fun onDeleteItemClick(data: Data) {

        AlertDialog.Builder(this).apply {
            setTitle("Delete Data")
            setMessage("Are you sure you to deldete this data?")
            setPositiveButton("Yes") { _, _ ->
                dataViewModel.deleteData(data,
                    onSuccess = {
                        Toast.makeText(
                            this@MainActivity,
                            "Data deleted successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onFailure = {
                        Toast.makeText(
                            this@MainActivity,
                            "Failed to delete data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )

            }
            setNegativeButton("No", null)
        }.show()


    }
}
