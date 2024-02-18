package com.estholon.alarmapp

import android.content.BroadcastReceiver
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import com.estholon.alarmapp.adapters.StringsListAdapter
import com.estholon.alarmapp.databinding.ActivityNewAlarmBinding
import com.estholon.alarmapp.databinding.LiStringsListBinding
import com.estholon.alarmapp.providers.RepetitionTypesProvider
import java.util.concurrent.TimeUnit

class NewAlarmActivity : AppCompatActivity() {

    // BINDING VARIABLE
    private lateinit var binding: ActivityNewAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
        initAlarm()

    }

    // FUNCTIONS

    private fun initListeners() {
        binding.tieRepetitionType.setOnClickListener{
            liRepetitionTypes()
        }
        binding.tieRepetitionType.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                liRepetitionTypes()
            }
        }
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


}