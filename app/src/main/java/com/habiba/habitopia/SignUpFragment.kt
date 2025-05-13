package com.habiba.habitopia


import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.habiba.habitopia.databinding.FragmentSignupBinding

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirm = binding.etConfirmPassword.text.toString().trim()

            if (validate(email, password, confirm)) {
                registerUser(email, password)
            }
        }
    }

    private fun validate(email: String, password: String, confirm: String): Boolean {
        var isValid = true

        when {
            email.isEmpty() -> {
                binding.etEmail.error = "Email required"
                isValid = false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.etEmail.error = "Invalid email"
                isValid = false
            }
            password.length < 6 -> {
                binding.etPassword.error = "Min 6 characters"
                isValid = false
            }
            password != confirm -> {
                binding.etConfirmPassword.error = "Passwords do not match"
                isValid = false
            }
        }

        return isValid
    }

    private fun registerUser(email: String, password: String) {
        binding.btnSignUp.isEnabled = false
        binding.progressBar.visibility = View.VISIBLE

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                binding.btnSignUp.isEnabled = true
                binding.progressBar.visibility = View.GONE

                if (task.isSuccessful) {
                    showSnackbar("Registered successfully")
                    // Navigate to next screen or clear fields
                    clearForm()
                } else {
                    showSnackbar(task.exception?.message ?: "Sign Up failed")
                }
            }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun clearForm() {
        binding.etEmail.text?.clear()
        binding.etPassword.text?.clear()
        binding.etConfirmPassword.text?.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
        }
}