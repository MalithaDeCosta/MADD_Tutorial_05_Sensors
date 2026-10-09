
package com.example.maddtutorial05sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider

import java.util.Locale

class MainActivity : AppCompatActivity() {

    // Sensor Manager
    private lateinit var sensorManager: SensorManager

    // Sensors
    private var accelerometer: Sensor? = null
    private var proximitySensor: Sensor? = null
    private var lightSensor: Sensor? = null

    // Sensor Event Listener
    private lateinit var sensorListener: SensorEventListener

    // ViewModel
    private lateinit var viewModel: MainActivityData

    // --------------------------------------------
    // onCreate()
    // --------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Handle system bars
        val rootView: View = findViewById(R.id.main)

        val originalLeft = rootView.paddingLeft
        val originalTop = rootView.paddingTop
        val originalRight = rootView.paddingRight
        val originalBottom = rootView.paddingBottom

        ViewCompat.setOnApplyWindowInsetsListener(
            rootView
        ) { view, insets ->

            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            view.setPadding(
                originalLeft + bars.left,
                originalTop + bars.top,
                originalRight + bars.right,
                originalBottom + bars.bottom
            )

            insets
        }

        // Find TextViews from XML
        val tvAccelaration: TextView =
            findViewById(R.id.tvAccelaration)

        val tvProximity: TextView =
            findViewById(R.id.tvProximity)

        val tvLight: TextView =
            findViewById(R.id.tvLight)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[
            MainActivityData::class.java
        ]

        // Observe accelerometer data
        viewModel.accelerometerData.observe(this) { data ->

            tvAccelaration.text = data
        }

        // Observe proximity data
        viewModel.proximityData.observe(this) { data ->

            tvProximity.text = data
        }

        // Observe light data
        viewModel.lightData.observe(this) { data ->

            tvLight.text = data
        }

        // Initialize SensorManager
        sensorManager = getSystemService(
            Context.SENSOR_SERVICE
        ) as SensorManager

        // Get default accelerometer
        accelerometer = sensorManager.getDefaultSensor(
            Sensor.TYPE_ACCELEROMETER
        )

        // Get default proximity sensor
        proximitySensor = sensorManager.getDefaultSensor(
            Sensor.TYPE_PROXIMITY
        )

        // Get default light sensor
        lightSensor = sensorManager.getDefaultSensor(
            Sensor.TYPE_LIGHT
        )

        // Check unavailable sensors
        if (accelerometer == null) {

            viewModel.updateAccelerometerData(
                "Accelerometer\nNot available on this device"
            )
        }

        if (proximitySensor == null) {

            viewModel.updateProximityData(
                "Proximity Sensor\nNot available on this device"
            )
        }

        if (lightSensor == null) {

            viewModel.updateLightData(
                "Light Sensor\nNot available on this device"
            )
        }

        // Create SensorEventListener
        sensorListener = initSensorListener()
    }

    // --------------------------------------------
    // initSensorListener()
    // --------------------------------------------

    private fun initSensorListener(): SensorEventListener {

        return object : SensorEventListener {

            override fun onAccuracyChanged(
                sensor: Sensor?,
                accuracy: Int
            ) {
                // No action needed for this tutorial
            }

            override fun onSensorChanged(event: SensorEvent) {

                when (event.sensor.type) {

                    // ACCELEROMETER
                    Sensor.TYPE_ACCELEROMETER -> {

                        if (event.values.size >= 3) {

                            val x = event.values[0]
                            val y = event.values[1]
                            val z = event.values[2]

                            val data = String.format(
                                Locale.US,
                                "Accelerometer\nX: %.2f m/s²\nY: %.2f m/s²\nZ: %.2f m/s²",
                                x,
                                y,
                                z
                            )

                            viewModel.updateAccelerometerData(
                                data
                            )
                        }
                    }

                    // PROXIMITY
                    Sensor.TYPE_PROXIMITY -> {

                        if (event.values.isNotEmpty()) {

                            val distance = event.values[0]

                            val data = String.format(
                                Locale.US,
                                "Proximity Sensor\nDistance: %.2f cm",
                                distance
                            )

                            viewModel.updateProximityData(
                                data
                            )
                        }
                    }

                    // LIGHT SENSOR
                    Sensor.TYPE_LIGHT -> {

                        if (event.values.isNotEmpty()) {

                            val lux = event.values[0]

                            val data = String.format(
                                Locale.US,
                                "Light Sensor\nIlluminance: %.2f lux",
                                lux
                            )

                            viewModel.updateLightData(
                                data
                            )
                        }
                    }
                }
            }
        }
    }

    // --------------------------------------------
    // onResume()
    // Register sensors
    // --------------------------------------------

    override fun onResume() {

        super.onResume()

        accelerometer?.let { sensor ->

            sensorManager.registerListener(
                sensorListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        proximitySensor?.let { sensor ->

            sensorManager.registerListener(
                sensorListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        lightSensor?.let { sensor ->

            sensorManager.registerListener(
                sensorListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    // --------------------------------------------
    // onPause()
    // Unregister sensors
    // --------------------------------------------

    override fun onPause() {

        sensorManager.unregisterListener(sensorListener)

        super.onPause()
    }
}
