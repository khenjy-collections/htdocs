package com.example.himapl

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.himapl.databinding.FragmentAnggotaRootBinding
import com.example.himapl.models.mahasiswa
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AnggotaRootFragment : Fragment() {

    private var _binding: FragmentAnggotaRootBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var storageRef: StorageReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnggotaRootBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Database
        database = Firebase.database.reference
        storageRef = FirebaseStorage.getInstance().reference

        val sharedPref = activity?.getSharedPreferences("userSession", Context.MODE_PRIVATE)
        val userNim = sharedPref?.getString("userNim", null)
        val userRole = sharedPref?.getString("userRole", null)

        // Memeriksa apakah rolenya adalah admin atau bukan
        if (userRole == "admin") {
            // Jika admin, tampilkan tombol Add
            binding.addBtn.visibility = View.VISIBLE
        } else {
            // Jika bukan admin, sembunyikan tombol Add
            binding.addBtn.visibility = View.GONE
        }


        binding.addBtn.setOnClickListener {
            navigateToFragment(AddMahasiswaFragment())
        }

        readData()
    }

    private fun navigateToFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun readData() {
        Log.d("AnggotaFragment", "readData: Reading data from Firebase")
        database.child("mahasiswa").get().addOnSuccessListener { dataSnapshot ->
            val mahasiswas = mutableListOf<mahasiswa>()
            for (mahasiswaSnapshot in dataSnapshot.children) {
                val mahasiswa = mahasiswaSnapshot.getValue(mahasiswa::class.java)
                mahasiswa?.let {
                    // Validate that none of the values are null
                    if (it.userId != null && it.nim != null && it.nama != null && it.email != null && it.angkatan != null && it.password != null && it.role != null && it.imageUrl != null && it.jabatan != null) {
                        mahasiswas.add(it)
                    } else {
                        Log.w(
                            "AnggotaFragment",
                            "readData: Null value found in mahasiswa data: ${mahasiswaSnapshot.key}"
                        )
                    }
                } ?: run {
                    Log.w(
                        "AnggotaFragment",
                        "readData: Failed to parse mahasiswa data: ${mahasiswaSnapshot.key}"
                    )
                }
            }
            // Populate UI with retrieved data
            populateUI(mahasiswas)
        }.addOnFailureListener { e ->
            Log.e("AnggotaFragment", "Error reading data", e)
            Toast.makeText(context, "Error reading data: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun populateUI(mahasiswas: List<mahasiswa>) {
        Log.d("AnggotaFragment", "populateUI: Populating UI with mahasiswa data")

        // Check if the binding object is not null
        if (_binding != null) {
            val gridLayout = binding.gridLayout

            // Clear existing views
            gridLayout.removeAllViews()

            // Inflate and add views for each mahasiswa
            for (mahasiswa in mahasiswas) {
                val childLayout = LayoutInflater.from(requireContext())
                    .inflate(R.layout.mahasiswa_item_layout, gridLayout, false)

                if (mahasiswa.role == "admin") {
                    continue
                }
                // Populate views with mahasiswa data
                val imageUrl = mahasiswa.imageUrl.toString()
                val imageView = childLayout.findViewById<ImageView>(R.id.imageUser)

// Load image from URL using Glide or Picasso library
                Glide.with(requireContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.logohima) // Optional placeholder image
                    .error(R.drawable.baseline_person_24) // Optional error image
                    .into(imageView)
                childLayout.findViewById<TextView>(R.id.namaTextView).text = mahasiswa.nama
                childLayout.findViewById<TextView>(R.id.nimTextView).text = mahasiswa.nim.toString()
                childLayout.findViewById<TextView>(R.id.roleTextView).text = mahasiswa.jabatan

                val sharedPref = activity?.getSharedPreferences("userSession", Context.MODE_PRIVATE)
                val userRole = sharedPref?.getString("userRole", null)
                if (userRole == "admin") {
                    val buttonlayout =
                        childLayout.findViewById<LinearLayout>(R.id.adminButtonsLayout)
                    buttonlayout.visibility = View.VISIBLE

                    val editbutton = childLayout.findViewById<ImageButton>(R.id.editBtn)
                    val deletebutton = childLayout.findViewById<ImageButton>(R.id.deleteBtn)

                    editbutton.setOnClickListener {
                        // Navigate to update fragment and pass mahasiswa data
                        navigateToFragment(UpdateMahasiswaFragment.newInstance(mahasiswa))
                    }

                    deletebutton.setOnClickListener {
                        // Show delete confirmation dialog
                        showDeleteConfirmationDialog(mahasiswa)
                    }

                }

                gridLayout.addView(childLayout)
            }
        } else {
            Log.e("AnggotaFragment", "populateUI: Binding object is null")
        }
    }

    private fun showDeleteConfirmationDialog(mahasiswa: mahasiswa) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage("Are you sure you want to delete this student?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                // Delete the mahasiswa
                deleteMahasiswa(mahasiswa)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = dialogBuilder.create()
        alert.show()

    }

    private fun deleteMahasiswa(mahasiswa: mahasiswa) {
        // Hapus gambar di Firebase Storage jika ada
        mahasiswa.imageUrl?.let { imageUrl ->
            val imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
            imageRef.delete().addOnSuccessListener {
                Log.d("AnggotaFragment", "Image deleted successfully from storage")
                // Setelah gambar dihapus, hapus mahasiswa dari database
                deleteMahasiswaFromDatabase(mahasiswa)
            }.addOnFailureListener { exception ->
                Log.e("AnggotaFragment", "Error deleting image from storage: ${exception.message}")
                // Jika gagal menghapus gambar, tetap hapus data mahasiswa dari database
                deleteMahasiswaFromDatabase(mahasiswa)
            }
        } ?: run {
            // Jika tidak ada gambar, langsung hapus mahasiswa dari database
            deleteMahasiswaFromDatabase(mahasiswa)
        }
    }

    private fun deleteMahasiswaFromDatabase(mahasiswa: mahasiswa) {
        database.child("mahasiswa").child(mahasiswa.userId!!).removeValue().addOnSuccessListener {
            Toast.makeText(context, "Mahasiswa deleted successfully", Toast.LENGTH_SHORT).show()
            // Refresh data
            readData()
        }.addOnFailureListener { e ->
            Log.e("AnggotaFragment", "Error deleting mahasiswa", e)
            Toast.makeText(context, "Error deleting mahasiswa: ${e.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
