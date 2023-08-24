package com.example.wastemanagement

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class UserSettingsFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val logoutButton = view.findViewById<Button>(R.id.buttonLogout)
        val deleteAccountButton = view.findViewById<Button>(R.id.buttonDeleteAccount)
        val passwordEditText = view.findViewById<EditText>(R.id.editTextPassword)
        val changePasswordButton = view.findViewById<Button>(R.id.button)

        logoutButton.setOnClickListener {
            // Sign out the current user
            auth.signOut()

            // Navigate to the SignInActivity
            val intent = Intent(activity, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        deleteAccountButton.setOnClickListener {
            val currentUser = auth.currentUser
            val enteredPassword = passwordEditText.text.toString()
            passwordEditText.visibility = View.VISIBLE

            if (enteredPassword.isNotEmpty()) {
                // Check if user is authenticated
                currentUser?.let { user ->
                    val credentials = EmailAuthProvider.getCredential(user.email!!, enteredPassword)

                    // Re-authenticate the user with entered password
                    user.reauthenticate(credentials)
                        .addOnCompleteListener { authTask ->
                            if (authTask.isSuccessful) {
                                // Re-authentication successful, now delete account
                                user.delete()
                                    .addOnCompleteListener { deleteTask ->
                                        if (deleteTask.isSuccessful) {
                                            // Account deleted successfully
                                            Toast.makeText(
                                                requireContext(),
                                                "Account deleted successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            // Navigate to the SignUpActivity
                                            val intent = Intent(activity, MainActivity::class.java)
                                            intent.flags =
                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            startActivity(intent)
                                            requireActivity().finishAffinity() // Close all previous activities
                                        } else {
                                            // Failed to delete account
                                            Toast.makeText(
                                                requireContext(),
                                                "Failed to delete account: ${deleteTask.exception?.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            } else {
                                // Re-authentication failed
                                Toast.makeText(
                                    requireContext(),
                                    "Re-authentication failed: ${authTask.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            } else {
                // Password not entered
                Toast.makeText(
                    requireContext(),
                    "Please enter your password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        changePasswordButton.setOnClickListener {
            //Open the change password page
            val intent = Intent(activity, ChangePassword::class.java)
            startActivity(intent)
        }
    }
}







