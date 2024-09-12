package com.example.himapl

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.himapl.databinding.FragmentProfileRootBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileRootFragment : Fragment() {

    private var _binding: FragmentProfileRootBinding? = null
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
        _binding = FragmentProfileRootBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileaboutusbutton.setOnClickListener {
            navigateToFragment(AboutUsFragment())
        }

        binding.profiledetailbutton.setOnClickListener {
            navigateToFragment(profileProfileFragment())
        }

        binding.logoutbutton.setOnClickListener {
            // Membuat AlertDialog
            AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Apakah Anda yakin ingin keluar?")
                .setPositiveButton("Ya") { dialog, which ->
                    // Jika pengguna menekan tombol "Ya", lakukan logout
                    val sharedPref = activity?.getSharedPreferences("userSession", Context.MODE_PRIVATE)
                    sharedPref?.edit()?.remove("userNim")?.apply()
                    findNavController().popBackStack()
                }
                .setNegativeButton("Tidak") { dialog, which ->
                    // Jika pengguna menekan tombol "Tidak", batalkan logout
                    dialog.dismiss() // Menutup dialog
                }
                .show()
        }


        val sharedPref = activity?.let {
            it.getSharedPreferences("userSession", Context.MODE_PRIVATE)
        }
        val userNim = sharedPref?.getString("userNim", null)
        Log.e("ProfileFragment", "User nim = $userNim")
        readdata(userNim)

    }


    private fun readdata(userNim: String?) {
        if (userNim != null) {
            // Ambil data pengguna dari Firebase
            database = Firebase.database.reference
            database.child("mahasiswa").orderByChild("nim").equalTo(userNim).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach { userSnapshot ->
                            val userName = userSnapshot.child("nama").getValue(String::class.java)
                            val userEmail = userSnapshot.child("email").getValue(String::class.java)
                            val userRole = userSnapshot.child("role").getValue(String::class.java)
                            val userNim = userSnapshot.child("nim").getValue(String::class.java)
                            val userProfileImageUrl = userSnapshot.child("imageUrl").getValue(String::class.java)
                            Log.e("ProfileFragment", "$userName, $userEmail")

                            binding.profilename.text = userName
                            binding.profilenim.text = userNim

                            if (userProfileImageUrl != null) {
                                Glide.with(fragmentContext)
                                    .load(userProfileImageUrl)
                                    .into(binding.profileimage)
                            } else {
                                binding.profileimage.setImageResource(R.drawable.logohima)
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


    private fun navigateToFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.child_fragment_container, fragment)
            .commit()
    }

}