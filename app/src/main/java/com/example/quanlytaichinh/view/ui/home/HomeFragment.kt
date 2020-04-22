package com.example.quanlytaichinh.view.ui.home


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.quanlytaichinh.databinding.FragmentHomeBinding
import com.example.quanlytaichinh.model.ManagerDatabase
import com.example.quanlytaichinh.util.SharedPreference
import com.example.quanlytaichinh.view.adapter.SectionAdapter

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        val dataSource = ManagerDatabase.getInstance(context!!).sectionDao

        val viewModelFactory = HomeViewModelFactory(dataSource)

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)

        val adapter = SectionAdapter()
        binding.rcvSection.adapter = adapter

        viewModel.sections.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "${it.size}")
            it.forEach {

            }
            if(it.isEmpty()) {
                viewModel.addSections()
            } else {
                adapter.submitList(it)
            }
        })

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    companion object {
        const val TAG = "HomeFragment"
    }

}
