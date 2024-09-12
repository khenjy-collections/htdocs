package com.example.himapl

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.himapl.databinding.FragmentAgendaRootBinding
import com.example.himapl.models.agenda
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class AgendaRootFragment : Fragment() {

    private var _binding: FragmentAgendaRootBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAgendaRootBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = Firebase.database.reference

        val sharedPref = activity?.getSharedPreferences("userSession", Context.MODE_PRIVATE)
        val userNim = sharedPref?.getString("userNim", null)
        val userRole = sharedPref?.getString("userRole", null)

        // Memeriksa apakah rolenya adalah admin atau bukan
        if (userRole == "admin") {
            // Jika admin, tampilkan tombol Add
            binding.addAgenda.visibility = View.VISIBLE
        } else {
            // Jika bukan admin, sembunyikan tombol Add
            binding.addAgenda.visibility = View.GONE
        }

        binding.addAgenda.setOnClickListener {
            navigateToFragment(AddAgendaFragment())
        }

        readData()
    }

    private fun readData() {
        Log.d("AgendaFragment", "Baca data agenda")
        database.child("agenda").get().addOnSuccessListener { datasnapshot ->
            val agendas = mutableListOf<agenda>()
            val currentDate = Calendar.getInstance().time
            for(agendaSnapshot in datasnapshot.children){
                val agenda = agendaSnapshot.getValue(agenda::class.java)
                agenda?.let {
                    if(it.agendaId != null && it.nama!=null && it.lokasi!=null && it.tanggal!=null){
                        agendas.add(it)
                    } else{
                        Log.w(
                            "AgendaFragment",
                            "readData: Null value found in mahasiswa data: ${agendaSnapshot.key}"
                        )
                    }
                }?: run {
                    Log.w(
                        "AnggotaFragment",
                        "readData: Failed to parse mahasiswa data: ${agendaSnapshot.key}"
                    )}
            } // Sort by date
            agendas.sortBy { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it.tanggal) }

            // Separate upcoming and past agendas
            val upcomingAgendas = agendas.filter {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it.tanggal)?.after(currentDate) == true
            }
            val pastAgendas = agendas.filter {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it.tanggal)?.before(currentDate) == true
            }

            populateUI(upcomingAgendas, pastAgendas)
        }.addOnFailureListener { e ->
            Log.e("AgendaFragment", "Error reading data", e)
            Toast.makeText(context, "Error reading data: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun populateUI(upcomingAgendas: List<agenda>, pastAgendas: List<agenda>) {
        Log.d("AgendaFragment", "populateUI: Populating UI with agenda data")
        if (_binding != null) {
            val layoutAgenda = binding.layoutAgenda
            val layoutPastAgenda = binding.agendalewat

            // Clear existing views
            layoutAgenda.removeAllViews()
            layoutPastAgenda.removeAllViews()

            // Inflate and add views for each upcoming agenda
            for (agenda in upcomingAgendas) {
                val childLayout = LayoutInflater.from(requireContext())
                    .inflate(R.layout.agenda_item_layout, layoutAgenda, false)

                childLayout.findViewById<TextView>(R.id.namaAgenda).text = agenda.nama
                childLayout.findViewById<TextView>(R.id.tanggal_agenda).text = agenda.tanggal
                childLayout.findViewById<TextView>(R.id.agenda_location).text = agenda.lokasi

                val sharedPref = activity?.getSharedPreferences("userSession", Context.MODE_PRIVATE)
                val userRole = sharedPref?.getString("userRole", null)
                if (userRole == "admin") {
                    val buttonlayout = childLayout.findViewById<LinearLayout>(R.id.actionAgendaButton)
                    buttonlayout.visibility = View.VISIBLE

                    val editbutton = childLayout.findViewById<ImageButton>(R.id.editagendabtn)
                    val deletebutton = childLayout.findViewById<ImageButton>(R.id.deleteagendabtn)

                    editbutton.setOnClickListener {
                        // Navigate to update fragment and pass
                        navigateToEditAgendaFragment(agenda.agendaId!!)
                    }

                    deletebutton.setOnClickListener {
                        // Show delete confirmation dialog
                        showDeleteConfirmationDialog(agenda.agendaId!!)
                    }
                }

                layoutAgenda.addView(childLayout)
            }

            // Inflate and add views for each past agenda
            for (agenda in pastAgendas) {
                val childLayout = LayoutInflater.from(requireContext())
                    .inflate(R.layout.agenda_item_layout, layoutPastAgenda, false)

                childLayout.findViewById<TextView>(R.id.namaAgenda).text = agenda.nama
                childLayout.findViewById<TextView>(R.id.tanggal_agenda).text = agenda.tanggal
                childLayout.findViewById<TextView>(R.id.agenda_location).text = agenda.lokasi
                childLayout.setBackgroundResource(R.drawable.deactiveagenda)

                val sharedPref = activity?.getSharedPreferences("userSession", Context.MODE_PRIVATE)
                val userRole = sharedPref?.getString("userRole", null)
                if (userRole == "admin") {
                    val buttonlayout = childLayout.findViewById<LinearLayout>(R.id.actionAgendaButton)
                    buttonlayout.visibility = View.VISIBLE

                    val editbutton = childLayout.findViewById<ImageButton>(R.id.editagendabtn)
                    val deletebutton = childLayout.findViewById<ImageButton>(R.id.deleteagendabtn)

                    editbutton.setOnClickListener {
                        // Navigate to update fragment and pass
                        navigateToEditAgendaFragment(agenda.agendaId!!)
                    }

                    deletebutton.setOnClickListener {
                        // Show delete confirmation dialog
                        showDeleteConfirmationDialog(agenda.agendaId!!)
                    }
                }

                layoutPastAgenda.addView(childLayout)
            }
        } else {
            Log.e("AgendaFragment", "populateUI: Binding object is null")
        }
    }

    private fun showDeleteConfirmationDialog(agendaId: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Agenda")
            .setMessage("Are you sure you want to delete this agenda?")
            .setPositiveButton("Yes") { dialog, _ ->
                deleteAgenda(agendaId)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun deleteAgenda(agendaId: String) {
        database.child("agenda").child(agendaId).removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Agenda deleted successfully", Toast.LENGTH_SHORT).show()
                readData() // Refresh data after deletion
            }
            .addOnFailureListener { e ->
                Log.e("AgendaFragment", "Error deleting data", e)
                Toast.makeText(context, "Error deleting agenda: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun navigateToFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.agendaContainer, fragment)
            .commit()
    }

    private fun navigateToEditAgendaFragment(agendaId: String) {
        val fragment = EditAgendaFragment().apply {
            arguments = Bundle().apply {
                putString("agendaId", agendaId)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.agendaContainer, fragment)
            .commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}