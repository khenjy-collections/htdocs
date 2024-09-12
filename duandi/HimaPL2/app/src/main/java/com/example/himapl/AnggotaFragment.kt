package com.example.himapl

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.himapl.databinding.FragmentAnggotaBinding
import com.example.himapl.models.mahasiswa
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AnggotaFragment : Fragment() {
    private var headerListener: HeaderVisibilityListener? = null

    private var _binding: FragmentAnggotaBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnggotaBinding.inflate(inflater, container, false)
        Log.d("AnggotaFragment", "onCreateView: View created")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Logging for debugging
        Log.d("AnggotaFragment", "onViewCreated: View created, initializing components")

        headerListener?.hideHeaderFooter()
        headerListener?.showFooter()
        headerListener?.showHeader()

        val childFragment1 = AnggotaRootFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, childFragment1)
            .commit()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("AnggotaFragment", "onAttach: Fragment attached to context")
        if (context is HeaderVisibilityListener) {
            headerListener = context
        } else {
            throw RuntimeException("$context must implement HeaderVisibilityListener")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("AnggotaFragment", "onDestroyView: Cleaning up view binding")
        _binding = null
    }
}
