package com.habiba.habitopia

import com.habiba.habitopia.databinding.FragmentAuthBinding


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class AuthFragment : Fragment() {

    private lateinit var binding: FragmentAuthBinding
    private var isLoginSelected = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        updateToggle()
        loadLoginFragment()


        binding.tvLogin.setOnClickListener {
            if (!isLoginSelected) {
                isLoginSelected = true
                updateToggle()
                loadLoginFragment()
            }
        }


        binding.tvAuthToggle.setOnClickListener {
            if (isLoginSelected) {
                isLoginSelected = false
                updateToggle()
                loadSignUpFragment()
            }
        }
        childFragmentManager.setFragmentResultListener("showForgotPassword", viewLifecycleOwner) { _, _ ->

            binding.header.visibility = View.GONE

            childFragmentManager.beginTransaction()
                .replace(R.id.auth_container, ForgotPasswordFragment())
                .addToBackStack(null)
                .commit()
            findNavController().navigate(R.id.action_authFragment_to_forgotPasswordFragment)
        }

    }

    private fun updateToggle() {
        if (isLoginSelected) {
            // Login selected
            binding.tvLogin.setBackgroundResource(R.drawable.toggle_left_selected)
            binding.tvLogin.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))

            binding.tvAuthToggle.setBackgroundResource(R.drawable.toggle_right_unselected)
            binding.tvAuthToggle.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
        } else {
            // Sign Up selected
            binding.tvLogin.setBackgroundResource(R.drawable.toggle_left_unselected)
            binding.tvLogin.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))

            binding.tvAuthToggle.setBackgroundResource(R.drawable.toggle_right_selected)
            binding.tvAuthToggle.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
        }
    }

    private fun loadLoginFragment() {
        childFragmentManager.beginTransaction()
            .replace(R.id.auth_container, LoginFragment())
            .commit()
    }

    private fun loadSignUpFragment() {
        childFragmentManager.beginTransaction()
            .replace(R.id.auth_container, SignUpFragment())
            .commit()
    }

    override fun onResume() {
        super.onResume()

        val current = childFragmentManager.findFragmentById(R.id.auth_container)
        if (current !is ForgotPasswordFragment) {
            binding.header.visibility = View.VISIBLE
            }
    }

}