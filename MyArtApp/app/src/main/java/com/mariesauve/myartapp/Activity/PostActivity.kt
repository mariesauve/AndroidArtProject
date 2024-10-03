package com.mariesauve.myartapp.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mariesauve.myartapp.Model.PostData
import com.mariesauve.myartapp.R

class PostActivity : AppCompatActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        // Initialize Firebase Realtime Database
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("artPosts")

        val editArtText = findViewById<EditText>(R.id.editArtText)
        val loreunTxt = findViewById<EditText>(R.id.loreunTxt)

        val editButton = findViewById<ImageButton>(R.id.editButton)
        editButton.setOnClickListener {
            val artDescription = editArtText.text.toString()
            val additionalText = loreunTxt.text.toString()

            if (artDescription.isNotEmpty() && additionalText.isNotEmpty()) {
                // Save data to Realtime Database
                saveArtPost(artDescription, additionalText)
            } else {
                Toast.makeText(this@PostActivity, "All fields are mandatory", Toast.LENGTH_SHORT).show()
            }
        }

        val buttonMainPage = findViewById<Button>(R.id.button_go_to_main)
        buttonMainPage.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    // Function to save data to Firebase Realtime Database
    private fun saveArtPost(artDescription: String, additionalText: String) {
        val artData = PostData(artDescription, additionalText, System.currentTimeMillis().toString())

        // Push the data into the Realtime Database under a unique ID
        val newPostRef = databaseReference.push()
        newPostRef.setValue(artData)
            .addOnSuccessListener {
                Toast.makeText(this, "Post successfully added", Toast.LENGTH_SHORT).show()
                Log.d("PostActivity", "Data successfully added to Realtime Database")
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error adding post: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.w("PostActivity", "Error adding post to Realtime Database", e)
            }
    }
}
