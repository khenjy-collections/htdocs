package com.example.himapl

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.himapl.databinding.FragmentHomeBinding
import com.example.himapl.databinding.FragmentProfileBinding

class profileFragment : Fragment() {
    private var headerListener: HeaderVisibilityListener? = null

    private var _binding: FragmentProfileBinding? = null
    private val binding get() =_binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        headerListener?.hideHeaderFooter()
        headerListener?.showFooter()

        // Inisiasi fragment child pertama
        val childFragment1 = ProfileRootFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.child_fragment_container, childFragment1)
            .commit()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HeaderVisibilityListener) {
            headerListener = context
        } else {
            throw RuntimeException("$context must implement HeaderVisibilityListener")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}