package com.example.himapl

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.himapl.databinding.FragmentEditAgendaBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import android.app.DatePickerDialog
import android.widget.Toast
import com.example.himapl.models.agenda

class EditAgendaFragment : Fragment() {

    private var _binding: FragmentEditAgendaBinding? = null
    private val binding get() = _binding!!
    private var agendaId: String? = null

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditAgendaBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = Firebase.database.reference

        // Get agenda ID from arguments
        agendaId = arguments?.getString("agendaId")

        // Load agenda data
        loadAgendaData()

        // Set up date picker
        binding.editTanggalAgenda.setOnClickListener {
            showDatePicker()
        }

        // Set up save button
        binding.btnSave.setOnClickListener {
            saveAgenda()
        }
    }

    private fun loadAgendaData() {
        agendaId?.let {
            database.child("agenda").child(it).get().addOnSuccessListener { dataSnapshot ->
                val agenda = dataSnapshot.getValue(agenda::class.java)
                agenda?.let {
                    binding.editNamaAgenda.setText(it.nama)
                    binding.editTanggalAgenda.setText(it.tanggal)
                    binding.editLokasiAgenda.setText(it.lokasi)
                }
            }.addOnFailureListener { e ->
                Toast.makeText(context, "Error loading data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                binding.editTanggalAgenda.setText(dateFormat.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun saveAgenda() {
        val nama = binding.editNamaAgenda.text.toString().trim()
        val tanggal = binding.editTanggalAgenda.text.toString().trim()
        val lokasi = binding.editLokasiAgenda.text.toString().trim()

        if (nama.isEmpty() || tanggal.isEmpty() || lokasi.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedAgenda = agenda(agendaId, nama, tanggal, lokasi)

        agendaId?.let {
            database.child("agenda").child(it).setValue(updatedAgenda).addOnSuccessListener {
                Toast.makeText(context, "Agenda updated successfully", Toast.LENGTH_SHORT).show()
                navigateBackToFragment(AgendaRootFragment())
            }.addOnFailureListener { e ->
                Toast.makeText(context, "Error updating agenda: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }

        }

    }

    private fun navigateBackToFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.agendaContainer, fragment)
            .commit()
    }

}