package com.example.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter : TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // 1. Remove item from list
                listOfTasks.removeAt(position)

                // 2. Notify adapter that our data set has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }

        // 1. Detect when user clicks on button
        findViewById<Button>(R.id.button).setOnClickListener() {
            Log.i("Caren", "User clicked on a button")
        }

        loadItems()

        // Look up recycler view in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up button and input field so that the user can input a task

        val inputTextField = findViewById<EditText>(R.id.addTaskField)
        inputTextField.setOnClickListener() {
            // 1. Grab text user as inputted into addTaskField
            val userInputtedTask = inputTextField.text.toString()

            // 2. Add string to list of tasks
            listOfTasks.add(userInputtedTask)

            // Notify the data adapter that the data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            // 3. Reset text field
            inputTextField.setText("")

            saveItems()
        }
    }

    // Save the data that the user has inputted
    // Save the data by writing and reading from a file

    // Get the file we need
    fun getDataFile() : File {

        // Every line is going to represent a specific task in our lists of tasks
        return File(filesDir, "data.txt")
    }

    // Load the items by reading every line in the data file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    // Save items by writing them into our data file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}