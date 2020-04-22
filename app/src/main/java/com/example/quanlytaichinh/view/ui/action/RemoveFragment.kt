package com.example.quanlytaichinh.view.ui.action


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.example.quanlytaichinh.databinding.FragmentRemoveBinding
import com.example.quanlytaichinh.model.ManagerDatabase
import com.example.quanlytaichinh.model.Spend
import com.example.quanlytaichinh.model.Time
import com.example.quanlytaichinh.util.hideSoftKeyboard
import kotlinx.android.synthetic.main.fragment_edit.*
import kotlinx.android.synthetic.main.fragment_remove.btn_add
import kotlinx.android.synthetic.main.fragment_remove.btn_cancel
import kotlinx.android.synthetic.main.fragment_remove.tv_code
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class RemoveFragment : Fragment() {

    private lateinit var spend: Spend

    private var month = 0
    private var year = 0

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private val database: ManagerDatabase by lazy {
        ManagerDatabase.getInstance(context!!)
    }

    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        spend = RemoveFragmentArgs.fromBundle(arguments!!).spend

        val date = simpleDateFormat.parse(spend.time)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date.time

        month = calendar.get(Calendar.MONTH) + 1
        year = calendar.get(Calendar.YEAR)

        val binding = FragmentRemoveBinding.inflate(inflater, container, false)

        binding.viewModel = spend

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_code.text = "#${spend.spendId}"

        initClick()
    }

    private fun initClick() {

        btn_add.setOnClickListener {
            removeSpend()
        }

        btn_cancel.setOnClickListener {
            hideSoftKeyboard(activity!!)
            cancelAction()
        }
    }

    private fun cancelAction() {
        findNavController().popBackStack()
    }

    private fun removeSpend() {
        uiScope.launch {
            deleteSpend()
            if(spend.type == 1) {
                updateSectionExpense()
                updateTimeExpense()
                hideSoftKeyboard(activity!!)
                cancelAction()
            } else {
                updateSection()
                updateTimeTotal()
                hideSoftKeyboard(activity!!)
                cancelAction()
            }
        }
    }

    private suspend fun updateTimeExpense() {
        withContext(Dispatchers.IO) {
            val time = database.timeDao.getTime(month, year)
            if(time == null) {
                database.timeDao.insert(Time(month, year, 0, 0))
            } else {
                database.timeDao.updatePaid(month, year, spend.money, 0)
            }
        }
    }

    private suspend fun updateSectionExpense() {
        withContext(Dispatchers.IO) {
            database.sectionDao.updatePaid(spend.sectionName, spend.money, 0)
        }
    }

    private suspend fun deleteSpend() {
        withContext(Dispatchers.IO) {
            database.spendDao.delete(spend)
        }
    }

    private suspend fun updateTimeTotal() {
        withContext(Dispatchers.IO) {
            val time = database.timeDao.getTime(month, year)
            if(time == null) {
                database.timeDao.insert(Time(month, year, spend.money, 0))
            } else {
                database.timeDao.updateTotal(month, year, spend.money, 0)
            }
        }
    }

    private suspend fun updateSection() {
        withContext(Dispatchers.IO) {
            database.sectionDao.update("TT", spend.money, 0)
            database.sectionDao.updateTotal("TT", spend.money, 0)
        }
    }

}
