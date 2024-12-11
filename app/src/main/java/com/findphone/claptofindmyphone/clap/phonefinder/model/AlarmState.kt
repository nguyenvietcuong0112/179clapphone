package com.findphone.claptofindmyphone.clap.phonefinder.model

import androidx.lifecycle.MutableLiveData

object AlarmState {
    val shouldStopFullChargeAlarm = MutableLiveData(true)
    val shouldStopLowBatteryAlarm = MutableLiveData(true)
}
