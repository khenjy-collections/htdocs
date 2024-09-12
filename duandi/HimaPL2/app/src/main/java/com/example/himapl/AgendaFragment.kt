package com.example.himapl

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.replace
import com.example.himapl.databinding.FragmentAgendaBinding
import com.example.himapl.databinding.FragmentHomeBinding

class AgendaFragment : Fragment() {
    private var headerListener: HeaderVisibilityListener? = null
    private var _binding: FragmentAgendaBinding? = null
    private val binding get() =_binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =FragmentAgendaBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        headerListener?.hideHeaderFooter()
        headerListener?.showFooter()
        headerListener?.showHeader()

        val childFragment1 = AgendaRootFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.agendaContainer, childFragment1)
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