package com.mariesauve.myartapp.Activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mariesauve.myartapp.R

class MainActivity : AppCompatActivity() {

    private lateinit var phone: EditText
    private lateinit var call: ImageButton
    private val REQUEST_CALL_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Back button functionality
        findViewById<Button>(R.id.backBtn).setOnClickListener {
            finish()  // This will close MainActivity and return to PostActivity
        }

        // Send email functionality
        val sendEmailButton = findViewById<Button>(R.id.sendEmailLinkBtn)
        val emailInput = findViewById<EditText>(R.id.emailInput)

        sendEmailButton.setOnClickListener {
            val emailContent = emailInput.text.toString().trim()
            if (emailContent.isNotEmpty()) {
                sendEmail("sweetnessjoker@gmail.com", "User Inquiry", emailContent)
            } else {
                Toast.makeText(this, "Please enter your message.", Toast.LENGTH_SHORT).show()
            }
        }

        // Phone call functionality
        phone = findViewById(R.id.number)
        call = findViewById(R.id.call)
        call.setOnClickListener {
            makePhoneCall()
        }
    }

    private fun sendEmail(recipient: String, subject: String, message: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"  // Ensures only email apps will handle the intent
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
        }
        try {
            startActivity(Intent.createChooser(intent, "Choose an email client"))
        } catch (e: Exception) {
            Toast.makeText(this, "No email app found.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun makePhoneCall() {
        val phoneNumber = phone.text.toString()
        if (phoneNumber.trim().isNotEmpty()) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL_PERMISSION)
            } else {
                startCall(phoneNumber)
            }
        } else {
            Toast.makeText(this, "Please enter a phone number", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phoneNumber")
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "Call permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall()
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
