package com.example.quanlytaichinh.view.ui.spend


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.quanlytaichinh.OnClickListener

import com.example.quanlytaichinh.R
import com.example.quanlytaichinh.databinding.FragmentSpendBinding
import com.example.quanlytaichinh.model.ManagerDatabase
import com.example.quanlytaichinh.model.Spend
import com.example.quanlytaichinh.view.adapter.SpendAdapter
import com.example.quanlytaichinh.view.ui.action.EditFragment
import kotlinx.android.synthetic.main.fragment_spend.*

/**
 * A simple [Fragment] subclass.
 */
class SpendFragment : Fragment() {

    private var currentSpend: Spend? = null
    var position = 0
    var max = 0

    private lateinit var spends: List<Spend>

    private lateinit var adapter: SpendAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentSpendBinding.inflate(inflater, container, false)

        val database = ManagerDatabase.getInstance(context!!).spendDao

        val viewModelFactory = SpendViewModelFactory(database)

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(SpendViewModel::class.java)

        adapter = SpendAdapter(object : OnClickListener<Spend> {
            override fun onClick(value: Spend) {
                currentSpend = value
            }
        })
        binding.rcvSpend.adapter = adapter

        viewModel.spends.observe(viewLifecycleOwner, Observer {
            val size = it.size
            if(size % 3 == 0) {
                max = size / 3
            } else {
                max = size / 3 + 1
            }
            spends = it
            Log.d("TAG", "${spends.size} - $max")
            if(spends.isNotEmpty())
                loadData()
            else {
                tv_paging.text = "0"
                adapter.submitList(spends)
            }
        })

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClick()
    }

    private fun initClick() {
        tv_find.setOnClickListener {
            findNavController().navigate(R.id.action_spendFragment_to_findFragment)
        }
        btn_add.setOnClickListener {
            findNavController().navigate(R.id.action_spendFragment_to_addFragment)
        }

        btn_edit.setOnClickListener {
            if(currentSpend != null)
            findNavController().navigate(SpendFragmentDirections.actionSpendFragmentToEditFragment(currentSpend!!))
        }

        btn_remove.setOnClickListener {
            if(currentSpend != null)
            findNavController().navigate(SpendFragmentDirections.actionSpendFragmentToRemoveFragment(currentSpend!!))
        }

        iv_left.setOnClickListener {
            if(position > 0) {
                position--
                loadData()
            }
        }

        iv_right.setOnClickListener {
            if(position < max - 1) {
                position++
                loadData()
            }
        }
    }

    private fun loadData() {
        tv_paging.text = "" + (position + 1) + "/$max"
        if((position + 1) * 3 > spends.size) {
            adapter.submitList(spends.subList(position * 3, spends.size))
        } else {
            adapter.submitList(spends.subList(position * 3, (position + 1) * 3))
        }
    }


}
