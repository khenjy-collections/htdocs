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
import com.example.himapl.databinding.FragmentAddMahasiswaBinding
import com.example.himapl.models.mahasiswa
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.security.MessageDigest

class AddMahasiswaFragment : Fragment() {

    private var _binding: FragmentAddMahasiswaBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var storageRef: StorageReference
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        Log.d("AnggotaFragment", "display add mahasiswa fragment")
        _binding = FragmentAddMahasiswaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = Firebase.database.reference
        storageRef = FirebaseStorage.getInstance().reference
        Log.d("AnggotaFragment", "display add mahasiswa fragment")
        binding.selectImageButton.setOnClickListener {
            selectImage()
        }

        binding.buttonSave.setOnClickListener {
            saveData()
        }

        // Check if imageUri is not null before setting it to ImageView
        if (imageUri != null) {
            binding.imagePreview.setImageURI(imageUri)
        } else {
            // If imageUrl is null, you can set a placeholder image or leave it empty
            // For example, setting a placeholder drawable
            binding.imagePreview.setImageResource(R.drawable.baseline_person_24)
            // If you want to leave the ImageView empty, you can remove the above line
        }
    }

    private fun saveData() {
        val nimText = binding.editNim.text.toString()
        val namaText = binding.editNama.text.toString()
        val emailText = binding.editEmail.text.toString()
        val angkatanText = binding.editAngkatan.text.toString()
        val passwordText = binding.editpass.text.toString()
        val roleText = "user"
        val jabatanText = binding.editJabatan.text.toString()

        if (nimText.isNotEmpty() && namaText.isNotEmpty() && emailText.isNotEmpty() &&
            angkatanText.isNotEmpty() && passwordText.isNotEmpty() && roleText.isNotEmpty() && jabatanText.isNotEmpty()) {
            val nim = nimText
            val angkatan = angkatanText.toInt()

            val hashedPass = hashPassword(passwordText)
            Log.d("AnggotaFragment","imageurl = $imageUri")
            if (imageUri != null) {
                val imageName = "$nim.jpg" // Nama file gambar yang akan diunggah
                val imageRef = storageRef.child("images/$imageName")

                uploadImageToStorage(imageUri!!, imageRef) { imageUrl ->
                    // Save data to Firebase Realtime Database
                    saveToDatabase(nim, namaText, emailText, angkatan, hashedPass, imageUrl.toString(), roleText, jabatanText)
                }
            } else {
                Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }

    }

    private fun uploadImageToStorage(uri: Uri, imageRef: StorageReference, callback: (Uri?) -> Unit) {
        val uploadTask = imageRef.putFile(uri)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                callback(downloadUri) // Pass the download URL to the callback
            } else {
                Log.e("FirebaseStorage", "Error uploading image: ${task.exception?.message}")
                Toast.makeText(requireContext(), "Error uploading image", Toast.LENGTH_SHORT).show()
                callback(null) // Pass null to the callback if there's an error
            }
        }
    }


    private fun saveToDatabase(
        nim: String,
        nama: String,
        email: String,
        angkatan: Int,
        hashedPass: String,
        imageUrl: String,
        role:String,
        jabatan:String
    ) {
        val userId = database.child("mahasiswa").push().key
        val mahas = mahasiswa(
            userId, nim, nama, email, angkatan, hashedPass, role, imageUrl, jabatan
        )
        Log.d("DashboardFragment", "mahas: $mahas")
        userId?.let {
            Log.d("DashboardFragment", "userId: $userId")

            database.child("mahasiswa").child(it).setValue(mahas)
                .addOnSuccessListener {
                    Log.d("AnggotaFragment", "Database saved")
                    Toast.makeText(context, "Data Stored Succesfully", Toast.LENGTH_SHORT).show()
                    navigateBackToFragment(AnggotaRootFragment())
                }
                .addOnFailureListener { e ->
                    Log.d("AnggotaFragment", "Error: ${e.message}")
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

    }

    private fun navigateBackToFragment(fragment:Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()

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


    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            imageUri = data.data!!
            binding.imagePreview.setImageURI(imageUri)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1
    }

}