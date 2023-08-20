package com.example.wastemanagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.wastemanagement.databinding.FragmentFragRequestUiBinding

class RequestUIFragment : Fragment() {

    private lateinit var binding: FragmentFragRequestUiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFragRequestUiBinding.inflate(inflater,container,false)
        binding.btNewRequest.setOnClickListener{
            it.findNavController().navigate(R.id.action_frag_request_ui_to_mapsFragment)
        }
        return binding.root
    }
}