package com.example.himapl

import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.himapl.databinding.FragmentHomeBinding
import com.example.himapl.models.agenda
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {

    private var headerListener: HeaderVisibilityListener? = null

    private var _binding: FragmentHomeBinding? = null
    private val binding get() =_binding!!

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =FragmentHomeBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        

        headerListener?.showFooter()
        headerListener?.showHeader()



        database = Firebase.database.reference
        val sharedPref = activity?.let {
            it.getSharedPreferences("userSession", Context.MODE_PRIVATE)
        }
        val userNim = sharedPref?.getString("userNim", null)

        database.child("mahasiswa").orderByChild("nim").equalTo(userNim).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach { userSnapshot ->
                        val userName = userSnapshot.child("nama").getValue(String::class.java)
                        val userRole = userSnapshot.child("jabatan").getValue(String::class.java)
                        val userNim = userSnapshot.child("nim").getValue(String::class.java)
                        val userProfileImageUrl = userSnapshot.child("imageUrl").getValue(String::class.java)

                        binding.roleHome.text = userRole
                        binding.nimHome.text = userNim

                        if (userProfileImageUrl != null) {
                            Glide.with(requireContext())
                                .load(userProfileImageUrl)
                                .into(binding.profilePictHome)
                        } else {
                            binding.profilePictHome.setImageResource(R.drawable.logohima)

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

        // Fetch and display nearest agenda
        fetchNearestAgenda()
    }

    private fun fetchNearestAgenda() {
        database.child("agenda").get().addOnSuccessListener { dataSnapshot ->
            val agendas = mutableListOf<agenda>()
            val currentDate = Calendar.getInstance().time

            for (agendaSnapshot in dataSnapshot.children) {
                val agenda = agendaSnapshot.getValue(agenda::class.java)
                agenda?.let {
                    if (it.agendaId != null && it.nama != null && it.lokasi != null && it.tanggal != null) {
                        agendas.add(it)
                    } else {
                        Log.w("HomeFragment", "Null value found in agenda data: ${agendaSnapshot.key}")
                    }
                } ?: run {
                    Log.w("HomeFragment", "Failed to parse agenda data: ${agendaSnapshot.key}")
                }
            }

            // Find the nearest agenda
            val nearestAgenda = agendas
                .filter { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it.tanggal)?.after(currentDate) == true }
                .minByOrNull { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it.tanggal)!! }

            nearestAgenda?.let {
                binding.agendaTerdekatName.text = it.nama
                binding.AgendaTerdekatTanggal.text = it.tanggal
                binding.AgendaTerdekatLokasi.text = it.lokasi
            }
        }.addOnFailureListener { e ->
            Log.e("HomeFragment", "Error reading data", e)
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




}