
package com.example.maddtutorial05sensors

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityData : ViewModel() {

    // Accelerometer sensor data
    val accelerometerData: MutableLiveData<String> =
        MutableLiveData(
            "Accelerometer\nWaiting for readings..."
        )

    // Proximity sensor data
    val proximityData: MutableLiveData<String> =
        MutableLiveData(
            "Proximity Sensor\nWaiting for readings..."
        )

    // Light sensor data
    val lightData: MutableLiveData<String> =
        MutableLiveData(
            "Light Sensor\nWaiting for readings..."
        )

    // Update accelerometer readings
    fun updateAccelerometerData(data: String) {
        accelerometerData.value = data
    }

    // Update proximity readings
    fun updateProximityData(data: String) {
        proximityData.value = data
    }

    // Update light readings
    fun updateLightData(data: String) {
        lightData.value = data
    }
}
