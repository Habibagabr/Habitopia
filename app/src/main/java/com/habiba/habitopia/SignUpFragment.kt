package com.habiba.habitopia

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.habiba.habitopia.databinding.FragmentSignupBinding

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirm = binding.etConfirmPassword.text.toString().trim()
            val userName = binding.etName.text.toString().trim()

            if (validate(email, password, confirm, userName)) {
                registerUser(email, password, userName)
            }
        }
    }

    private fun validate(email: String, password: String, confirm: String, userName: String): Boolean {
        var isValid = true

        if (userName.isEmpty()) {
            binding.etName.error = "Username required"
            isValid = false
        }
        if (email.isEmpty()) {
            binding.etEmail.error = "Email required"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Invalid email"
            isValid = false
        }
        if (password.length < 6) {
            binding.etPassword.error = "Minimum 6 characters"
            isValid = false
        }
        if (password != confirm) {
            binding.etConfirmPassword.error = "Passwords do not match"
            isValid = false
        }

        return isValid
    }

    private fun registerUser(email: String, password: String, userName: String) {
        binding.btnSignUp.isEnabled = false
        binding.progressBar.visibility = View.VISIBLE

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                binding.btnSignUp.isEnabled = true
                binding.progressBar.visibility = View.GONE

                if (task.isSuccessful) {
                    val userId = mAuth.currentUser?.uid
                    if (userId != null) {
                        val userData = hashMapOf(
                            "UserId" to userId,
                            "UserName" to userName,
                            "CharacterURLString" to "default"
                        )

                        FirebaseFirestore.getInstance()
                            .collection("UserData")
                            .document(userId)
                            .set(userData)
                            .addOnSuccessListener {
                                showSuccessDialog()
                            }
                            .addOnFailureListener { e ->
                                Log.e("Firestore", "Error saving user data: ${e.message}")
                                showSnackbar("Failed to save user data.")
                            }
                    } else {
                        showSnackbar("Failed to get user ID.")
                    }

                } else {
                    showSnackbar(task.exception?.message ?: "Sign Up failed")
                }
            }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showSuccessDialog() {
        val title = SpannableString(" Sign up successfully 🎉")
        title.setSpan(StyleSpan(Typeface.BOLD), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val message = SpannableString("Welcome to the world of discipline and achievements")
        message.setSpan(StyleSpan(Typeface.BOLD), 0, message.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val dialog = android.app.AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialogInterface, _ ->
                findNavController().navigate(R.id.action_authFragment_to_fragmentQ2)
                dialogInterface.dismiss()
            }
            .create()
        dialog.show()
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)?.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_button)


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
