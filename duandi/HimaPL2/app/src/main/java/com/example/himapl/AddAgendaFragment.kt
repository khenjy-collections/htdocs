package com.example.himapl

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material3.DatePickerDialog
import com.example.himapl.databinding.FragmentAddAgendaBinding
import com.example.himapl.models.agenda
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddAgendaFragment : Fragment() {

    private var _binding : FragmentAddAgendaBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddAgendaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = Firebase.database.reference
        binding.editTanggalAgenda.setOnClickListener {
            showDatePickerDialog()
        }
        binding.btnSave.setOnClickListener {
            saveAgenda()
        }
    }

    private fun showDatePickerDialog() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            binding.editTanggalAgenda.setText(dateFormat.format(calendar.time))
        }

        DatePickerDialog(
            requireContext(),
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun saveAgenda() {
        val namaAgenda = binding.editNamaAgenda.text.toString().trim()
        val tanggalAgenda = binding.editTanggalAgenda.text.toString().trim()
        val lokasiAgenda = binding.editLokasiAgenda.text.toString().trim()

        if (namaAgenda.isEmpty() || tanggalAgenda.isEmpty() || lokasiAgenda.isEmpty()) {
            Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        val agendaId = database.child("agenda").push().key
        val agenda = agenda(agendaId, namaAgenda, tanggalAgenda, lokasiAgenda)

        agendaId?.let {
            database.child("agenda").child(it).setValue(agenda)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Agenda saved successfully", Toast.LENGTH_SHORT).show()
                    // Clear input fields
                    binding.editNamaAgenda.text.clear()
                    binding.editTanggalAgenda.text.clear()
                    binding.editLokasiAgenda.text.clear()
                    navigateBackToFragment(AgendaRootFragment())
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to save agenda", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun navigateBackToFragment(fragment:Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.agendaContainer, fragment)
            .addToBackStack(null)
            .commit()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}