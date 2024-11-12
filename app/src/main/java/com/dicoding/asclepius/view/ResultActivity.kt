package com.dicoding.asclepius.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the Uri of the image and the classification result passed from MainActivity
        val imageUriString = intent.getStringExtra("imageUri")
        val classificationResults = intent.getStringExtra("classificationResults")

        // Set the image in the ImageView
        imageUriString?.let {
            val imageUri = Uri.parse(it)
            binding.resultImage.setImageURI(imageUri)
        }

        // Set the classification result in the TextView
        binding.resultText.text = classificationResults ?: "No result available"
    }
}