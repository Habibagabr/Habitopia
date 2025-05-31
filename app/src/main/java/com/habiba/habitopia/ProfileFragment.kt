package com.habiba.habitopia


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.habiba.habitopia.ViewModel.HomeViewModel
import com.habiba.habitopia.databinding.FragmentCharacterBinding
import com.habiba.habitopia.databinding.FragmentProfileBinding
import com.habiba.habitopia.utils.renderSvgToBitmapWithDynamicWebView

class ProfileFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private var _viewBinding: FragmentProfileBinding? = null
    private val viewBinding get() = _viewBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentProfileBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val avatarImage: ShapeableImageView = view.findViewById(R.id.avatarImage)

        val homeViewModel: HomeViewModel
        homeViewModel = HomeViewModel()

        // Character Avatar Rendering
        val sharedPrefImage =
            requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPrefImage.getString("character", null)?.let { characterImage ->
            val updatedCharacterImage =
                homeViewModel.setAvater(characterImage, "Happy", "Default")
            renderSvgToBitmapWithDynamicWebView(
                context = avatarImage.context,
                svgUrl = updatedCharacterImage,
                width = 750,
                height = 750
            ) { bitmap -> avatarImage.setImageBitmap(bitmap) }
        }


        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        val userEmail = auth.currentUser?.email

        // ✅ Set email directly to the UI
        viewBinding.emailEditText.text = userEmail

        // ✅ Get username from Firestore
        if (userId != null) {
            val store = FirebaseFirestore.getInstance()
            store.collection("UserData")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val username = document.getString("UserName")
                        viewBinding.nameEditText.text =username
                    } else {
                        viewBinding.nameEditText.text = "Good Morning, User!"
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to load user data", Toast.LENGTH_SHORT).show()
                }
        }



        viewBinding.logOutButton.setOnClickListener{
            auth.signOut()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)


        }

    }
}
