package com.mahmoud.bezierdrawingtest.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.mahmoud.bezierdrawingtest.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var signatureView: SignatureView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signatureView = findViewById(R.id.signature_view)

        val saveButton: Button = findViewById(R.id.saveBtn)
        saveButton.setOnClickListener {
            val bitmap = signatureView.getSignatureBitmap()
            bitmap?.let {
                saveSignatureAsImage(bitmap)
            }
        }

        val clearButton: Button = findViewById(R.id.clearBtn)
        clearButton.setOnClickListener {
            signatureView.clear()
        }
    }

    private fun saveSignatureAsImage(bitmap: Bitmap) {
        // Convert Bitmap to Base64
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val base64Signature = Base64.encodeToString(byteArray, Base64.DEFAULT)

        // Save Base64 string locally or handle it as needed
        saveToFile(base64Signature)
    }

    private fun saveToFile(base64: String) {
        signatureView.setDrawingColor()
        // Decode Base64 string to byte array
        val decodedBytes = Base64.decode(base64, Base64.DEFAULT)

        // Create a Bitmap from the byte array
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

        // Get the internal storage directory
        val fileOutputStream: FileOutputStream
        try {
            val file = File(filesDir, "signature.png")
            fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()

            // Display the saved image
            displayImage(file.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun displayImage(filePath: String) {
        val imageView: ImageView = findViewById(R.id.image_view)
        val bitmap = BitmapFactory.decodeFile(filePath)
        imageView.setImageBitmap(bitmap)
    }

}