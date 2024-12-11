package com.findphone.claptofindmyphone.clap.phonefinder.ui.battery

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.findphone.claptofindmyphone.clap.phonefinder.broadcast.BatteryLevelReceiver
import com.findphone.claptofindmyphone.clap.phonefinder.databinding.ActivityBatteryNotificationBinding
import com.findphone.claptofindmyphone.clap.phonefinder.model.AlarmType
import com.findphone.claptofindmyphone.clap.phonefinder.model.BatteryAlarmSettings
import com.findphone.claptofindmyphone.clap.phonefinder.ui.battery.adapter.BatteryAlarmAdapter

class BatteryStatusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBatteryNotificationBinding
    private lateinit var batteryAlarms: MutableList<BatteryAlarmSettings>
    private lateinit var batteryAlarmAdapter: BatteryAlarmAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBatteryNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        batteryAlarms = mutableListOf(
            BatteryAlarmSettings(alarmType = AlarmType.BATTERY_FULL),
            BatteryAlarmSettings(
                alarmType = AlarmType.BATTERY_LOW,
                batteryPercentage = 15
            )
        )
        binding.icBackbutton.setOnClickListener {
            onBackPressed()
        }

        val layoutManager = GridLayoutManager(this, 2)
        batteryAlarmAdapter = BatteryAlarmAdapter(batteryAlarms)

        with(binding.rvBatteryAlarms) {
            this.layoutManager = layoutManager
            this.adapter = batteryAlarmAdapter
        }

        registerBatteryLevelReceiver()
    }

    private fun registerBatteryLevelReceiver() {
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val receiver = BatteryLevelReceiver()
        applicationContext.registerReceiver(receiver, intentFilter)
    }


}