//package com.habiba.habitopia
//
//
//import android.content.Context
//import android.content.SharedPreferences
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import android.view.LayoutInflater
//import android.view.MotionEvent
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.EditText
//import android.widget.ImageView
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AlertDialog
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.setFragmentResultListener
//import androidx.navigation.fragment.findNavController
//
//class ProfileFragment : Fragment() {
//
//    private lateinit var sharedPreferences: SharedPreferences
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_profile, container, false).apply {
//            sharedPreferences =
//                requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
//            val nameEditText: EditText = findViewById(R.id.nameEditText)
//            val emailEditText: EditText = findViewById(R.id.emailEditText)
//            val resetPasswordText: TextView = findViewById(R.id.resetPasswordText)
//            val saveChangesButton: Button = findViewById(R.id.saveChangesButton)
//            val logOutButton: Button = findViewById(R.id.logOutButton)
//            val avatarImage: ImageView = findViewById(R.id.avatarImage)
//            val editAvatarIcon: ImageView = findViewById(R.id.editAvatarIcon)
//
//            // استرجاع البيانات من Shared Preferences
//            nameEditText.setText(sharedPreferences.getString("user_name", ""))
//            emailEditText.setText(sharedPreferences.getString("user_email", ""))
//            // تمسح "Answer" لما تبدأ تكتب
//            nameEditText.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(
//                    s: CharSequence?,
//                    start: Int,
//                    count: Int,
//                    after: Int
//                ) {
//                }
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                    if (s.toString() == getString(R.string.answer_hint) && count > 0) {
//                        nameEditText.setText("")
//                    }
//                }
//
//                override fun afterTextChanged(s: Editable?) {}
//            })
//            emailEditText.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(
//                    s: CharSequence?,
//                    start: Int,
//                    count: Int,
//                    after: Int
//                ) {
//                }
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                    if (s.toString() == getString(R.string.answer_hint) && count > 0) {
//                        emailEditText.setText("")
//                    }
//                }
//
//                override fun afterTextChanged(s: Editable?) {}
//            })
//
//            // استرجاع الأفاتار
//            setFragmentResultListener("avatarResult") { _, bundle ->
//                val selectedAvatar = bundle.getInt("selectedAvatar")
//                avatarImage.setImageResource(selectedAvatar)
//            }
////
////            // Reset Password Dialog
////            resetPasswordText.setOnClickListener {
////                val builder = AlertDialog.Builder(requireContext())
////                val dialogView = layoutInflater.inflate(R.layout.dialog_reset_password, null)
////                val passwordEditText = dialogView.findViewById<EditText>(R.id.newPasswordEditText)
////                builder.setView(dialogView)
////                    .setPositiveButton("Save") { _, _ ->
////                        val newPassword = passwordEditText.text.toString()
////                        if (newPassword.isNotEmpty()) {
////                            sharedPreferences.edit().putString("user_password", newPassword).apply()
////                            Toast.makeText(
////                                context,
////                                "تم حفظ كلمة المرور الجديدة!",
////                                Toast.LENGTH_SHORT
////                            ).show()
////                        } else {
////                            Toast.makeText(context, "من فضلك أدخل كلمة مرور!", Toast.LENGTH_SHORT)
////                                .show()
////                        }
////                    }
////                    .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
////                    .show()
////            }
//
//
//            // حفظ التغييرات
//            saveChangesButton.setOnClickListener {
//                val newName = nameEditText.text.toString()
//                val newEmail = emailEditText.text.toString()
//                if (newName.isNotEmpty() && newEmail.isNotEmpty()) {
//                    sharedPreferences.edit()
//                        .putString("user_name", newName)
//                        .putString("user_email", newEmail)
//                        .apply()
//                    Toast.makeText(context, "تم حفظ التغييرات!", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(context, "من فضلك أدخل الاسم والإيميل!", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            }
//
//            // Log Out (مؤقت)
//            logOutButton.setOnClickListener {
//                // هنا هيتم الربط لاحقًا مع Sign Up Screen
//                Toast.makeText(context, "Log Out (سيتم الربط لاحقًا)", Toast.LENGTH_SHORT).show()
//                findNavController().popBackStack() // رجوع مؤقت
//            }
//
////            // تعديل الأفاتار (التنقل مؤقتًا لـ MainFragmentScreen)
////            editAvatarIcon.setOnClickListener {
////                findNavController().navigate(R.id.action_profileFragment_to_mainScreenFragment)
////            }
//
//            // تفعيل التعديل لما يضغط على الصورة في نهاية الحقل
//            nameEditText.setOnTouchListener{ _, event ->
//                if (event.action == MotionEvent.ACTION_UP) {
//                    if (event.rawX >= (nameEditText.right - nameEditText.compoundDrawables[2].bounds.width())) {
//                        nameEditText.isFocusable = true
//                        nameEditText.isFocusableInTouchMode = true
//                        nameEditText.requestFocus()
//                        val imm =
//                            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
//                        imm.showSoftInput(nameEditText, 0)
//                        return@setOnTouchListener true
//                    }
//                }
//                false
//            }
//
//            emailEditText.setOnTouchListener { _, event ->
//                if (event.action == MotionEvent.ACTION_UP) {
//                    if (event.rawX >= (emailEditText.right - emailEditText.compoundDrawables[2].bounds.width())) {
//                        emailEditText.isFocusable = true
//                        emailEditText.isFocusableInTouchMode = true
//                        emailEditText.requestFocus()
//                        val imm =
//                            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
//                        imm.showSoftInput(emailEditText, 0)
//                        return@setOnTouchListener true
//                    }
//                }
//                false
//            }
//        }
//    }
//}