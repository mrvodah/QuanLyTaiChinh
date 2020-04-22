package com.example.quanlytaichinh.view.ui.action


import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.example.quanlytaichinh.OnClickListener

import com.example.quanlytaichinh.R
import com.example.quanlytaichinh.databinding.FragmentFindBinding
import com.example.quanlytaichinh.model.ManagerDatabase
import com.example.quanlytaichinh.model.Spend
import com.example.quanlytaichinh.util.hideSoftKeyboard
import com.example.quanlytaichinh.view.adapter.SpendAdapter
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_edit.*
import kotlinx.android.synthetic.main.fragment_find.*
import kotlinx.android.synthetic.main.fragment_find.btn_add
import kotlinx.android.synthetic.main.fragment_find.btn_cancel
import kotlinx.android.synthetic.main.fragment_find.sp_category
import kotlinx.android.synthetic.main.fragment_find.sp_section
import kotlinx.android.synthetic.main.fragment_find.tv_time
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class FindFragment : Fragment() {

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private var time = ""
    private var month = 0
    private var year = 0

    private val database: ManagerDatabase by lazy {
        ManagerDatabase.getInstance(context!!)
    }

    private lateinit var adapter: SpendAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentFindBinding.inflate(inflater, container, false)

        adapter = SpendAdapter(object : OnClickListener<Spend> {
            override fun onClick(value: Spend) {
            }
        })
        binding.rcvSpend.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClick()

        sp_category.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position == 0) {
                    sp_section.visibility = View.GONE
                } else {
                    sp_section.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initClick() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        this.month = month + 1
        this.year = year
        time = "$day/" + (month + 1) + "/$year"
        tv_time.text = time

        tv_time.setOnClickListener {

            val dpd = DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                this.month = monthOfYear + 1
                this.year = year
                time = "$dayOfMonth/" + (month + 1) + "/$year"
                tv_time.text = time
            }, year, month, day)

            dpd.show()
        }
        btn_add.setOnClickListener {
            val category = sp_category.selectedItemPosition
            val section = sp_section.selectedItem as String

            if(category == 0) {
                findSpends(category, "")
            } else {
                findSpends(category, section)
            }
        }

        btn_cancel.setOnClickListener {
            hideSoftKeyboard(activity!!)
            cancelAction()
        }
    }

    private fun cancelAction() {
        findNavController().popBackStack()
    }

    private fun findSpends(category: Int, section: String) {
        try {
            uiScope.launch {
                val spends = database.spendDao.getSpends(time, category, section)
                spends.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    Log.d("TAG", "${it.size}")
                    if(it.isNullOrEmpty()) {
                        adapter.submitList(listOf())
                    } else {
                        adapter.submitList(it)
                    }
                    hideSoftKeyboard(activity!!)
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}
