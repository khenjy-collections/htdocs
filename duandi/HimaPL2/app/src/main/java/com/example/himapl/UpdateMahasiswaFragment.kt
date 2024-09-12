package com.example.himapl

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.himapl.databinding.FragmentUpdateMahasiswaBinding
import com.example.himapl.models.mahasiswa
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.ktx.Firebase

class UpdateMahasiswaFragment : Fragment() {

    private var _binding: FragmentUpdateMahasiswaBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var mahasiswa: mahasiswa
    private lateinit var storageRef: StorageReference
    private var imageUri: Uri? = null
    private var oldImageUrl: String? = null

    companion object {
        private const val ARG_MAHASISWA = "mahasiswa"
        private const val REQUEST_IMAGE_PICK = 1

        fun newInstance(mahasiswa: mahasiswa): UpdateMahasiswaFragment {
            val fragment = UpdateMahasiswaFragment()
            val args = Bundle()
            args.putSerializable(ARG_MAHASISWA, mahasiswa)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mahasiswa = (it.getSerializable(ARG_MAHASISWA) as mahasiswa?)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateMahasiswaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Database
        database = Firebase.database.reference
        storageRef = FirebaseStorage.getInstance().reference

        // Populate UI with mahasiswa data
        binding.namaEditText.setText(mahasiswa.nama)
        binding.nimEditText.setText(mahasiswa.nim)
        binding.emailEditText.setText(mahasiswa.email)
        binding.angkatanEditText.setText(mahasiswa.angkatan?.toString())
        binding.jabatanEditText.setText(mahasiswa.jabatan)
        oldImageUrl = mahasiswa.imageUrl // Save old image URL

        var imageView = binding.imagePreview
        Glide.with(requireContext())
            .load(mahasiswa.imageUrl)
            .placeholder(R.drawable.logohima) // Optional placeholder image
            .error(R.drawable.baseline_person_24) // Optional error image
            .into(imageView)

        binding.selectImageButton.setOnClickListener {
            selectImage()
        }

        // Update button click listener
        binding.updateBtn.setOnClickListener {
            updateMahasiswa()
        }
    }

    private fun updateMahasiswa() {
        // Retrieve updated data from UI
        val updatedMahasiswa = mahasiswa.copy(
            nama = binding.namaEditText.text.toString(),
            nim = binding.nimEditText.text.toString(),
            email = binding.emailEditText.text.toString(),
            angkatan = binding.angkatanEditText.text.toString().toIntOrNull(),
            role = "user",
            jabatan = binding.jabatanEditText.text.toString()
        )

        // Image URI from the selection of new image
        val newImageUri = imageUri

        // If there is no old image or no new image selected, simply save the updated data
        if (oldImageUrl == null || newImageUri == null) {
            saveUpdatedMahasiswa(updatedMahasiswa)
            return
        }

        // Delete old image and upload new image
        deleteOldImageAndUploadNewImage(oldImageUrl, newImageUri)
    }

    private fun saveUpdatedMahasiswa(updatedMahasiswa: mahasiswa) {
        mahasiswa.userId?.let {
            database.child("mahasiswa").child(it).setValue(updatedMahasiswa)
                .addOnSuccessListener {
                    Toast.makeText(context, "Mahasiswa updated successfully", Toast.LENGTH_SHORT).show()
                    navigateBackToFragment(AnggotaRootFragment())
                }
                .addOnFailureListener { e ->
                    Log.e("UpdateMahasiswaFragment", "Error updating mahasiswa", e)
                    Toast.makeText(context, "Error updating mahasiswa: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun uploadImageToStorage(uri: Uri, imageRef: StorageReference, callback: (Uri?) -> Unit) {
        Log.d("UpdateMahasiswaFragment", "Uploading image to storage: $uri")
        val uploadTask = imageRef.putFile(uri)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                Log.d("UpdateMahasiswaFragment", "Image uploaded successfully: $downloadUri")
                callback(downloadUri)
            } else {
                Log.e("UpdateMahasiswaFragment", "Error uploading image: ${task.exception?.message}")
                callback(null)
            }
        }
    }

    private fun deleteOldImageAndUploadNewImage(imageUrl: String?, newImageUri: Uri) {
        imageUrl?.let {
            val oldImageRef = FirebaseStorage.getInstance().getReferenceFromUrl(it)
            oldImageRef.delete().addOnSuccessListener {
                Log.d("UpdateMahasiswaFragment", "Old image successfully deleted.")
                // After deletion of old image completes, upload new image
                val imageName = mahasiswa.nim?.let { generateImageName(it) }
                val imageRef = storageRef.child("images/$imageName")
                uploadImageToStorage(newImageUri, imageRef) { imageUrl ->
                    if (imageUrl != null) {
                        mahasiswa.imageUrl = imageUrl.toString()
                        saveUpdatedMahasiswa(mahasiswa)
                    } else {
                        Toast.makeText(context, "Error uploading image", Toast.LENGTH_SHORT).show()
                    }
                }
            }.addOnFailureListener { exception ->
                Log.e("UpdateMahasiswaFragment", "Error deleting old image: ${exception.message}")
            }
        } ?: run {
            // If there is no old image, upload the new image directly
            val imageName = mahasiswa.nim?.let { generateImageName(it) }
            val imageRef = storageRef.child("images/$imageName")
            uploadImageToStorage(newImageUri, imageRef) { imageUrl ->
                if (imageUrl != null) {
                    mahasiswa.imageUrl = imageUrl.toString()
                    saveUpdatedMahasiswa(mahasiswa)
                } else {
                    Toast.makeText(context, "Error uploading image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            imageUri = data.data
            binding.imagePreview.setImageURI(imageUri)
            Log.d("UpdateMahasiswaFragment", "Image selected: $imageUri")
        }
    }

        private fun generateImageName(nim: String): String {
        return "$nim.jpg"
    }

    private fun navigateBackToFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
