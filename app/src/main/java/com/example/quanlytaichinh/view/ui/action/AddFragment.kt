package com.example.quanlytaichinh.view.ui.action


import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.navigation.fragment.findNavController

import com.example.quanlytaichinh.R
import com.example.quanlytaichinh.model.ManagerDatabase
import com.example.quanlytaichinh.model.Spend
import com.example.quanlytaichinh.model.Time
import com.example.quanlytaichinh.util.SharedPreference
import com.example.quanlytaichinh.util.hideSoftKeyboard
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.coroutines.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class AddFragment : Fragment() {

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private val sharedPreference: SharedPreference by lazy {
        SharedPreference(activity!!)
    }

    private var time = ""
    private var month = 0
    private var year = 0

    private val database: ManagerDatabase by lazy {
        ManagerDatabase.getInstance(context!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClick()

        initSpinner()

    }

    private fun initSpinner() {
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
                tv_time.setText(time)
            }, year, month, day)

            dpd.show()
        }

        btn_add.setOnClickListener {
            val category = sp_category.selectedItemPosition
            val section = sp_section.selectedItem as String
            val money = edt_cost.text.toString()

            if(!money.isNullOrEmpty()) {

                if(category == 0) {
                    addRevenue()
                } else {
                    addExpense(section)
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
            insertSpendExpense(section)
            updateSectionExpense(section)
            updateTimeExpense()
            hideSoftKeyboard(activity!!)
            cancelAction()
        }
    }

    private suspend fun updateTimeExpense() {
        withContext(Dispatchers.IO) {
            val time = database.timeDao.getTime(month, year)
            if(time == null) {
                database.timeDao.insert(Time(month, year, 0, edt_cost.text.toString().toLong()))
            } else {
                database.timeDao.updatePaid(month, year, edt_cost.text.toString().toLong())
            }
        }
    }

    private suspend fun updateSectionExpense(section: String) {
        withContext(Dispatchers.IO) {
            database.sectionDao.updatePaid(section, edt_cost.text.toString().toLong())
        }
    }

    private suspend fun insertSpendExpense(section: String) {
        withContext(Dispatchers.IO) {
            database.spendDao.insert(Spend(time, 1, edt_content.text.toString(), edt_cost.text.toString().toLong(), section))
        }
    }

    private fun addRevenue() {
        uiScope.launch {
            insertSpendRevenue()
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
                database.timeDao.updateTotal(month, year, edt_cost.text.toString().toLong())
            }
        }
    }

    private suspend fun updateSection() {
        withContext(Dispatchers.IO) {
            database.sectionDao.update("TT", edt_cost.text.toString().toLong())
            database.sectionDao.updateTotal("TT", edt_cost.text.toString().toLong())
        }
    }

    private suspend fun insertSpendRevenue() {
        withContext(Dispatchers.IO) {
            database.spendDao.insert(Spend(time, 0, edt_content.text.toString(), edt_cost.text.toString().toLong(), ""))
        }
    }

}
