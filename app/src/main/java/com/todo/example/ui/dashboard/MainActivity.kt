package com.todo.example.ui.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.todo.example.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * This class is used for fragment container
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
