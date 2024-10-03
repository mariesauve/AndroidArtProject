package com.mariesauve.myartapp.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mariesauve.myartapp.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Back button functionality
        findViewById<Button>(R.id.backBtn).setOnClickListener {
            finish()  // This will close MainActivity and return to PostActivity
        }

        // Send email button functionality
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
    }

    private fun sendEmail(recipient: String, subject: String, message: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"  // This ensures that only email apps will handle the intent
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))  // Recipient's email address
            putExtra(Intent.EXTRA_SUBJECT, subject)          // Email subject
            putExtra(Intent.EXTRA_TEXT, message)             // Email body
        }

        // Check if an email app is available to handle the intent
        try {
            startActivity(Intent.createChooser(intent, "Choose an email client"))
        } catch (e: Exception) {
            Toast.makeText(this, "No email app found.", Toast.LENGTH_SHORT).show()
        }
    }

}
