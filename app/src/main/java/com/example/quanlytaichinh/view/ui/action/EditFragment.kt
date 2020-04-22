package com.example.quanlytaichinh.view.ui.action


import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.navigation.fragment.findNavController

import com.example.quanlytaichinh.R
import com.example.quanlytaichinh.databinding.FragmentEditBinding
import com.example.quanlytaichinh.model.ManagerDatabase
import com.example.quanlytaichinh.model.Spend
import com.example.quanlytaichinh.model.Time
import com.example.quanlytaichinh.util.hideSoftKeyboard
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_edit.*
import kotlinx.android.synthetic.main.fragment_edit.btn_add
import kotlinx.android.synthetic.main.fragment_edit.btn_cancel
import kotlinx.android.synthetic.main.fragment_edit.edt_content
import kotlinx.android.synthetic.main.fragment_edit.edt_cost
import kotlinx.android.synthetic.main.fragment_edit.sp_category
import kotlinx.android.synthetic.main.fragment_edit.sp_section
import kotlinx.android.synthetic.main.fragment_edit.tv_time
import kotlinx.coroutines.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class EditFragment : Fragment() {

    private lateinit var spend: Spend

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private var time = ""
    private var month = 0
    private var year = 0

    private var oldMoney = 0L

    private val database: ManagerDatabase by lazy {
        ManagerDatabase.getInstance(context!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        spend = EditFragmentArgs.fromBundle(arguments!!).spend

        time = spend.time
        oldMoney = spend.money

        val binding = FragmentEditBinding.inflate(inflater, container, false)
        binding.viewModel = spend

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        val menuArray = getResources().getStringArray(R.array.spend);

        tv_code.text = "#${spend.spendId}"

        if(spend.type == 0) {
            sp_category.setSelection(0)
        } else {
            sp_category.setSelection(1)
            sp_section.setSelection(menuArray.indexOf(spend.sectionName))
        }

        initClick()

    }

    private fun initClick() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        tv_time.setOnClickListener {

            val dpd = DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                this.month = month + 1
                this.year = year
                time = "$dayOfMonth/" + (month + 1) + "/$year"
                tv_time.text = time
            }, year, month, day)

            dpd.show()
        }

        btn_add.setOnClickListener {
            val category = sp_category.selectedItemPosition
            val section = sp_section.selectedItem as String
            Log.d("TAG", "$category - $section")

            val money = edt_cost.text.toString()

            if(!money.isNullOrEmpty()) {

                spend.time = time
                spend.type = category
                spend.content = edt_content.text.toString()
                spend.money = edt_cost.text.toString().toLong()

                if(category == 0) {
                    addRevenue()
                    spend.sectionName = ""
                } else {
                    addExpense(section)
                    spend.sectionName = section
                }
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

    private fun addExpense(section: String) {
        uiScope.launch {
            updateSpend()
            updateSectionExpense(section)
            updateTimeExpense()
            hideSoftKeyboard(activity!!)
            cancelAction()
        }
    }

    private suspend fun updateSpend() {
        withContext(Dispatchers.IO) {
            database.spendDao.update(spend)
        }
    }

    private suspend fun updateTimeExpense() {
        withContext(Dispatchers.IO) {
            val time = database.timeDao.getTime(month, year)
            if(time == null) {
                database.timeDao.insert(Time(month, year, 0, edt_cost.text.toString().toLong()))
            } else {
                database.timeDao.updatePaid(month, year, oldMoney, edt_cost.text.toString().toLong())
            }
        }
    }

    private suspend fun updateSectionExpense(section: String) {
        withContext(Dispatchers.IO) {
            database.sectionDao.updatePaid(section, oldMoney, edt_cost.text.toString().toLong())
        }
    }

    private fun addRevenue() {
        uiScope.launch {
            updateSpend()
            updateSection()
            updateTimeTotal()
            hideSoftKeyboard(activity!!)
            cancelAction()
        }
    }

    private suspend fun updateTimeTotal() {
        withContext(Dispatchers.IO) {
            val time = database.timeDao.getTime(month, year)
            if(time == null) {
                database.timeDao.insert(Time(month, year, edt_cost.text.toString().toLong(), 0))
            } else {
                database.timeDao.updateTotal(month, year, oldMoney, edt_cost.text.toString().toLong())
            }
        }
    }

    private suspend fun updateSection() {
        withContext(Dispatchers.IO) {
            database.sectionDao.update("TT", oldMoney, edt_cost.text.toString().toLong())
            database.sectionDao.updateTotal("TT", oldMoney, edt_cost.text.toString().toLong())
        }
    }
}
