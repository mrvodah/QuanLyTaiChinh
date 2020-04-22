package com.example.quanlytaichinh.view.ui.analysts


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.quanlytaichinh.R
import com.example.quanlytaichinh.databinding.FragmentAnalystsBinding
import com.example.quanlytaichinh.model.ManagerDatabase
import com.example.quanlytaichinh.model.Time
import com.example.quanlytaichinh.view.adapter.TimeAdapter
import kotlinx.android.synthetic.main.fragment_analysts.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class AnalystsFragment : Fragment() {

    private lateinit var adapter: TimeAdapter

    private var times = arrayListOf<Time>()

    private var month = 0
    private var year = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentAnalystsBinding.inflate(inflater, container, false)

        val database = ManagerDatabase.getInstance(context!!).timeDao

        val viewModelFactory = AnalystsViewModelFactory(database)

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(AnalystsViewModel::class.java)

        adapter = TimeAdapter()
        binding.rcvTime.adapter = adapter

        val c = Calendar.getInstance()
        year = c.get(Calendar.YEAR)
        month = c.get(Calendar.MONTH)

        initTimes()

        viewModel.times.observe(viewLifecycleOwner, Observer {
            Log.d("TAG", "${it.size}")
            it.forEach {
                Log.d("TAG", "${it.month} - ${it.year} - ${it.total} - ${it.paid}")
            }

            if(it.isNullOrEmpty()) {
                adapter.submitList(times)
            } else {
                initTimes()
                setupList(it)
            }
        })

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    private fun setupList(it: List<Time>) {
        times.forEach { parent ->
            it.forEach { child ->
                if(child.month == parent.month) {
                    parent.total = child.total
                    parent.paid = child.paid
                }
            }
        }

        var total = 0L
        var paid = 0L
        it.forEach {
            total += it.total
            paid += it.paid
        }

        times.add(Time(13, year, total, paid))
        adapter.submitList(times)
    }

    private fun initTimes() {
        times.clear()
        for(i in 1..12) {
            times.add(Time(i, year, 0, 0))
        }
    }


}
