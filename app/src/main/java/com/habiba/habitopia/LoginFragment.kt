//package com.habiba.habitopia
//
//import android.os.Bundle
//import android.util.Patterns
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import com.google.android.material.snackbar.Snackbar
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.habiba.habitopia.databinding.FragmentLoginBinding
//
//class LoginFragment : Fragment() {
//
//    private var _binding: FragmentLoginBinding? = null
//    private val binding get() = _binding!!
//    private lateinit var mAuth: FirebaseAuth
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentLoginBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        mAuth = FirebaseAuth.getInstance()
//
////        binding.tvForgotPassword.setOnClickListener {
////            (activity as? MainActivity)?.navigateToForgotPassword()
////        }
//
//        binding.btnLogin.setOnClickListener {
//            val email = binding.etEmail.text.toString().trim()
//            val password = binding.etPassword.text.toString().trim()
//
//            if (validate(email, password)) {
//                loginUser(email, password)
//            }
//        }
//    }
//
//    private fun validate(email: String, password: String): Boolean {
//        if (email.isEmpty()) {
//            binding.etEmail.error = "Email required"
//            return false
//        }
//        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            binding.etEmail.error = "Invalid email"
//            return false
//        }
//        if (password.length < 6) {
//            binding.etPassword.error = "Min 6 characters"
//            return false
//        }
//        return true
//    }
//
//    private fun loginUser(email: String, password: String) {
//        binding.btnLogin.isEnabled = false
//
//        mAuth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                binding.btnLogin.isEnabled = true
//
//                if (task.isSuccessful) {
//                    val user = mAuth.currentUser
//                    user?.reload()?.addOnCompleteListener { reloadTask ->
//                        if (reloadTask.isSuccessful) {
//                            if (user.isEmailVerified) {
//                                Snackbar.make(binding.root, "Login successful. Welcome!", Snackbar.LENGTH_LONG).show()
//                                // TODO: Navigate to HomeFragment or Activity
//                            } else {
//                                sendVerificationEmail(user)
//                                Snackbar.make(
//                                    binding.root,
//                                    "Email not verified. Verification email sent. Please check your inbox.",
//                                    Snackbar.LENGTH_LONG
//                                ).show()
//                            }
//                        } else {
//                            Snackbar.make(binding.root, "Could not refresh user data.", Snackbar.LENGTH_LONG).show()
//                        }
//                    }
//                } else {
//                    Snackbar.make(
//                        binding.root,
//                        "Login failed: ${task.exception?.message}",
//                        Snackbar.LENGTH_LONG
//                    ).show()
//                }
//            }
//    }
//
//    private fun sendVerificationEmail(user: FirebaseUser) {
//        user.sendEmailVerification()
//            .addOnCompleteListener { task ->
//                if (!task.isSuccessful) {
//                    Snackbar.make(
//                        binding.root,
//                        "Failed to send verification email: ${task.exception?.message}",
//                        Snackbar.LENGTH_LONG
//                    ).show()
//                }
//            }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding=null }
//}