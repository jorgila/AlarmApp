package com.estholon.alarmapp.ui.views

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import com.estholon.alarmapp.domain.broadcast.AlarmReceiver
import com.estholon.alarmapp.data.adapters.StringsListAdapter
import com.estholon.alarmapp.databinding.ActivityNewAlarmBinding
import com.estholon.alarmapp.databinding.LiStringsListBinding
import com.estholon.alarmapp.domain.providers.RepetitionTypesProvider
import com.estholon.alarmapp.ui.viewModels.NewAlarmActivityViewModel
import com.estholon.alarmapp.ui.viewModels.NewAlarmActivityViewModelFactory
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar
import java.util.concurrent.TimeUnit

class NewAlarmActivity : AppCompatActivity() {

    // CONNECTION

    //// ViewModel

    private val viewModel : NewAlarmActivityViewModel by viewModels {
        NewAlarmActivityViewModelFactory(application)
    }

    // BINDING VARIABLE
    private lateinit var binding: ActivityNewAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()

    }

    // FUNCTIONS

    private fun initListeners() {
        binding.tieDate.setOnClickListener{
            calendarPicker(binding.tieDate)
        }

        binding.tieDate.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                calendarPicker(binding.tieDate)
            }
        }

        binding.tieHour.setOnClickListener {
            timePicker(binding.tieHour)
        }

        binding.tieHour.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                timePicker(binding.tieHour)
            }
        }

        binding.tieRepetitionType.setOnClickListener{
            liRepetitionTypes()
        }
        binding.tieRepetitionType.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                liRepetitionTypes()
            }
        }
        binding.btnAdd.setOnClickListener {

            if(isInformationGood()){
                viewModel.newAlarm(
                    binding.tieTitle.text.toString(),
                    binding.tieDate.text.toString(),
                    binding.tieHour.text.toString(),
                    binding.tieRepetitionType.text.toString(),
                    binding.tieRepetition.text.toString().toInt(),
                    binding.tieMessage.text.toString(),
                    true
                )

                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun isInformationGood(): Boolean {
        var result = true
        if(binding.tieTitle.text.isNullOrEmpty()){ result = false }
        if(binding.tieDate.text.isNullOrEmpty()){ result = false }
        if(binding.tieHour.text.isNullOrEmpty()){ result = false }
        if(binding.tieRepetitionType.text.isNullOrEmpty()){ result = false }
        if(binding.tieRepetition.text.isNullOrEmpty()){ result = false }
        if(binding.tieMessage.text.isNullOrEmpty()){ result = false }
        return result
    }

    private fun liRepetitionTypes() {
        // Layout Inflater Variables
        val inflater = LayoutInflater.from(binding.root.context)
        val liBinding = LiStringsListBinding.inflate(inflater)
        val builder = androidx.appcompat.app.AlertDialog.Builder(binding.root.context)
        builder.setView(liBinding.root)
        val alertDialog = builder.create()
        //Layout Inflater Logic
        //// Show Data
        val adapter = StringsListAdapter()
        adapter.submitList(RepetitionTypesProvider.getRepetitionTypes(this))
        liBinding.rvStrings.adapter = adapter
        liBinding.rvStrings.setHasFixedSize(true)
        //// Listener
        adapter.setOnClickListener(object: StringsListAdapter.ClickListener{
            override fun onClickListener(string: String) {
                binding.tieRepetitionType.setText(string)
                alertDialog.dismiss()
            }

        })
        //Layout Inflater Finish
        builder.setCancelable(false)
        alertDialog.window!!.setGravity(Gravity.CENTER)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    private fun initAlarm() {
        val broadcastReceiver = AlarmReceiver()
        broadcastReceiver.setAlarm(this, TimeUnit.MINUTES.toMillis(1))
    }

    fun calendarPicker(view: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
            view.setText(selectedDate)
        }, year, month, dayOfMonth).show()


    }

    fun timePicker(view: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, hour, minute ->
            val selectedTime = String.format("%02d:%02d", hour, minute)
            view.setText(selectedTime)
        }, hour, minute, true).show()

    }





}