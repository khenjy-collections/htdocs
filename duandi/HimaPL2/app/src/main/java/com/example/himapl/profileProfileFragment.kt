package com.example.himapl

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.himapl.databinding.FragmentProfileProfileBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class profileProfileFragment : Fragment() {

    private var _binding: FragmentProfileProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var fragmentContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context // Simpan konteks fragment saat fragment dilampirkan
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clearData()
        val sharedPref = activity?.let {
            it.getSharedPreferences("userSession", Context.MODE_PRIVATE)
        }
        val userNim = sharedPref?.getString("userNim", null)
        Log.e("ProfileFragment", "User nim = $userNim")
        readData(userNim)
    }

    private fun clearData() {
        binding.usernameEditText.text = ""
        binding.emailEditText.text = ""
        binding.roleEditText.text = ""
        binding.nimEditText.text = ""
        binding.profileImage.setImageDrawable(null)
    }

    private fun readData(userNim: String?) {
        if (userNim != null) {
            // Ambil data pengguna dari Firebase
            database = Firebase.database.reference
            database.child("mahasiswa").orderByChild("nim").equalTo(userNim).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach { userSnapshot ->
                            val userName = userSnapshot.child("nama").getValue(String::class.java)
                            val userEmail = userSnapshot.child("email").getValue(String::class.java)
                            val userRole = userSnapshot.child("role").getValue(String::class.java)
                            val jabatan = userSnapshot.child("jabatan").getValue(String::class.java)
                            val userNim = userSnapshot.child("nim").getValue(String::class.java)
                            val userProfileImageUrl = userSnapshot.child("imageUrl").getValue(String::class.java)
                            Log.e("ProfileFragment", "$userName, $userEmail")

                            binding.usernameEditText.text = userName
                            binding.emailEditText.text = userEmail
                            binding.roleEditText.text = jabatan
                            binding.nimEditText.text = userNim

                            if (userProfileImageUrl != null) {
                                Glide.with(fragmentContext)
                                    .load(userProfileImageUrl)
                                    .into(binding.profileImage)
                            } else {
                                binding.profileImage.setImageResource(R.drawable.logohima)
                            }
                        }
                    } else {
                        Log.e("ProfileFragment", "User data not found")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("ProfileFragment", "Error fetching data", error.toException())
                }
            })

        } else {
            Log.e("ProfileFragment", "NIM not found in SharedPreferences")
        }
    }

}
