package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener {
            startGallery()
        }

        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let {
                // Classify image and pass results to ResultActivity
                ImageClassifierHelper(
                    context = this,
                    classifierListener = object : ImageClassifierHelper.ClassifierListener {
                        override fun onError(error: String) {
                            showToast(error)
                        }

                        override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                            val highestScoreCategory = results?.flatMap { it.categories }
                                ?.maxByOrNull { it.score }
                            val highestScoreResult = highestScoreCategory?.let {
                                "${it.label}: ${String.format("%.2f", it.score * 100)}%"
                            } ?: "No result available"

                            val intent = Intent(this@MainActivity, ResultActivity::class.java)
                            intent.putExtra("imageUri", currentImageUri.toString())
                            intent.putExtra("classificationResults", highestScoreResult)
                            startActivity(intent)
                        }
                    }
                ).classifyStaticImage(it)
            } ?: showToast("No image selected")
        }
    }

    private fun startGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        startActivityForResult(intent, REQUEST_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_GALLERY -> {
                if (resultCode == RESULT_OK) {
                    data?.data?.let { uri ->
                        currentImageUri = uri
                        startCrop(uri)
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    showToast("Image selection canceled")
                }
            }
            UCrop.REQUEST_CROP -> {
                when (resultCode) {
                    RESULT_OK -> {
                        currentImageUri = UCrop.getOutput(data!!)
                        showImage()
                    }
                    RESULT_CANCELED -> {
                        showImage() // Display the raw image
                        showToast("Using raw image without cropping")
                    }
                    UCrop.RESULT_ERROR -> {
                        showToast("Error cropping image")
                    }
                }
            }
        }
    }

    private fun startCrop(uri: Uri) {
        val destinationUri = Uri.fromFile(File(cacheDir, "croppedImage_${System.currentTimeMillis()}.jpg"))
        UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(1080, 1080)
            .start(this)
    }

    private fun showImage() {
        binding.previewImageView.setImageURI(null)
        currentImageUri?.let { uri ->
            binding.previewImageView.setImageURI(uri)
        } ?: showToast("No image selected")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Save the current image URI when the activity is recreated (e.g., during a rotation)
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        currentImageUri?.let {
            outState.putString("currentImageUri", it.toString())
        }
    }

    // Restore the image URI and update the display after a configuration change
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getString("currentImageUri")?.let {
            currentImageUri = Uri.parse(it)
            showImage() // Display the saved image URI
        }
    }

    companion object {
        private const val REQUEST_GALLERY = 1
    }
}