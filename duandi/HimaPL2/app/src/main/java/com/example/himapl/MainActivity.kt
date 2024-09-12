package com.example.himapl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.himapl.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity(), HeaderVisibilityListener {

    private lateinit var binding : ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.footer.setupWithNavController(navController)

        val toolbar = binding.toolbar

        // Mendengarkan perubahan dalam navigasi
        navController.addOnDestinationChangedListener { _, destination, _ ->
            toolbar.title = destination.label
            supportActionBar?.setDisplayHomeAsUpEnabled(destination.id != navController.graph.startDestinationId)
            if (destination.id == R.id.homeFragment) {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
        }

    }




    override fun hideHeaderFooter() {
        val header: View? = findViewById(R.id.headerview)
        header?.visibility = View.GONE

        val footer: View? = findViewById(R.id.footer)
        footer?.visibility = View.GONE
    }

    override fun showFooter() {

        val footer: View? = findViewById(R.id.footer)
        footer?.visibility = View.VISIBLE
    }

    override fun showHeader(){
        val header: View? = findViewById(R.id.headerview)
        header?.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Apakah Anda ingin keluar dari aplikasi?")
            .setCancelable(false)
            .setPositiveButton("Ya") { dialog, id ->
                // Keluar dari aplikasi
                super.onBackPressed()
            }
            .setNegativeButton("Tidak") { dialog, id ->
                // Batal keluar dari aplikasi
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }



}