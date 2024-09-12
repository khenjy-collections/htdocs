package com.example.himapl

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.himapl.databinding.FragmentLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import java.security.MessageDigest


class LoginFragment : Fragment() {

    private var headerListener: HeaderVisibilityListener? = null

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editNim.text = null
        binding.editTextPassword.text = null


        headerListener?.hideHeaderFooter()
        database = Firebase.database.reference
        binding.loginButton.setOnClickListener {
            val nim = binding.editNim.text.toString()
            val password = binding.editTextPassword.text.toString()
            val hashedspassword = hashPassword(password)
            Log.e("LoginFragment", "nim : $nim\n password : $hashedspassword")
            verifyUser(nim, hashedspassword)
        }

    }

    private fun hashPassword(password: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update(password.toByteArray())
        val hashedBytes = messageDigest.digest()
        val hashedString = StringBuilder()
        for (byte in hashedBytes) {
            hashedString.append("%02x".format(byte.toInt() and 0xff))
        }
        return hashedString.toString()
    }

    private fun verifyUser(nim: String, hashedPassword: String) {
        database.child("mahasiswa").orderByChild("nim").equalTo(nim).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                snapshot.children.forEach { userSnapshot ->
                    val dbPassword = userSnapshot.child("password").getValue(String::class.java)
                    val role = userSnapshot.child("role").getValue(String::class.java)

                    Log.e("LoginFragment", "DB Password: $dbPassword")
                    if (dbPassword == hashedPassword) {
                        // Simpan NIM di SharedPreferences
                        val sharedPref = activity?.getSharedPreferences("userSession", Context.MODE_PRIVATE) ?: return@addOnSuccessListener
                        with(sharedPref.edit()) {
                            putString("userNim", nim)
                            putString("userRole", role)
                            apply()
                        }
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    } else {
                        Toast.makeText(context, "Invalid NIM or password", Toast.LENGTH_SHORT).show()
                        Log.e("LoginFragment", "Invalid NIM or password")
                    }
                }
            } else {
                Toast.makeText(context, "Nim tidak ditemukan", Toast.LENGTH_SHORT).show()
                Log.e("LoginFragment", "NIM not found")
            }
        }.addOnFailureListener {
            Log.e("LoginFragment", "Error fetching data", it)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HeaderVisibilityListener) {
            headerListener = context
        } else {
            throw RuntimeException("$context must implement HeaderVisibilityListener")
        }
    }

    override fun onPause() {
        super.onPause()
        binding.editNim.text = null
        binding.editTextPassword.text = null
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}