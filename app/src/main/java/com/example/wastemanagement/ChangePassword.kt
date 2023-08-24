package com.example.wastemanagement

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ChangePassword : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        auth = FirebaseAuth.getInstance()

        val oldPasswordEditText = findViewById<EditText>(R.id.editTextOldPassword)
        val newPasswordEditText = findViewById<EditText>(R.id.editTextNewPassword)
        val confirmPasswordEditText = findViewById<EditText>(R.id.editTextConfirmPassword)
        val changePasswordButton = findViewById<Button>(R.id.buttonChangePassword)

        changePasswordButton.setOnClickListener {
            val oldPassword = oldPasswordEditText.text.toString()
            val newPassword = newPasswordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (newPassword == confirmPassword) {
                // Check if newPassword meets required criteria
                if (isPasswordValid(newPassword)) {
                    val currentUser = auth.currentUser

                    // Re-authenticate the user with the entered old password
                    val credentials = EmailAuthProvider.getCredential(currentUser?.email!!, oldPassword)
                    currentUser.reauthenticate(credentials)
                        .addOnCompleteListener { authTask ->
                            if (authTask.isSuccessful) {
                                // Re-authentication successful, now update the password
                                currentUser.updatePassword(newPassword)
                                    .addOnCompleteListener { updateTask ->
                                        if (updateTask.isSuccessful) {
                                            // Password updated successfully
                                            Toast.makeText(
                                                this,
                                                "Password updated successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            finish() // Close the activity
                                        } else {
                                            // Failed to update password
                                            Toast.makeText(
                                                this,
                                                "Failed to update password: ${updateTask.exception?.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            } else {
                                // Re-authentication failed
                                Toast.makeText(
                                    this,
                                    "Re-authentication failed: ${authTask.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Minimum length of new password is 8", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "New password and confirm password don't match.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 8
    }
}
