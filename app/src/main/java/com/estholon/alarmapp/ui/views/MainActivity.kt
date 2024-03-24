package com.estholon.alarmapp.ui.views

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.estholon.alarmapp.data.adapters.AlarmsAdapter
import com.estholon.alarmapp.data.localDB.AlarmDao
import com.estholon.alarmapp.data.localDB.AlarmDataSourceLocal
import com.estholon.alarmapp.databinding.ActivityMainBinding
import com.estholon.alarmapp.domain.broadcast.AlarmReceiver
import com.estholon.alarmapp.domain.model.Alarm
import com.estholon.alarmapp.domain.permissions.PermissionUtil
import com.estholon.alarmapp.ui.viewModels.MainActivityViewModel
import com.estholon.alarmapp.ui.viewModels.MainActivityViewModelFactory

class MainActivity : AppCompatActivity() {

    // CONNECTION

    private val viewModel by viewModels<MainActivityViewModel> {
        MainActivityViewModelFactory(application)
    }

    // BINDING VARIABLE
    private lateinit var binding: ActivityMainBinding

    // OTHER VARIABLES

    val adapter = AlarmsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        initListeners()
        initObservers()
    }

    private fun initUI() {
        rv()
    }

    private fun initObservers() {
        viewModel.listOfAlarms.observe(this){
            adapter.submitList(it)
        }
    }

    private fun initListeners() {
        binding.btnAdd.setOnClickListener{
            if(PermissionUtil.checkNotificationPermission(this@MainActivity)) {
                val intent = Intent(this, NewAlarmActivity::class.java)
                startActivity(intent)
            }else{
                PermissionUtil.requestNotificationPermission(this@MainActivity)
            }
        }
    }
    private fun rv(){
        viewModel.refresh()
        adapter.submitList(viewModel.listOfAlarms.value)
        binding.rvAlarms.adapter = adapter
        binding.rvAlarms.setHasFixedSize(true)

        adapter.setOnClickListener(object:AlarmsAdapter.ClickListener{
            override fun onClickListener(string: String, id: Int, status: Boolean) {

                when(string) {
                    "delete" -> {
                        viewModel.deleteAlarm(id)
                    }
                    "status" -> {
                        viewModel.changeStatus(id,status,this@MainActivity)
                    }
                }
            }

        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PermissionUtil.NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(this, NewAlarmActivity::class.java)
                startActivity(intent)
            }
        }
    }

}