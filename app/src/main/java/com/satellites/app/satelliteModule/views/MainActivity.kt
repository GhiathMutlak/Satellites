package com.satellites.app.satelliteModule.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.satellites.app.R
import com.satellites.app.utils.FragmentUtils
import com.satellites.app.utils.FragmentUtils.launchFragmentWithDefaultAnimation

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launchFragmentWithDefaultAnimation(this,
            R.id.container_fragment,
            SatellitesListFragment(),
            FragmentUtils.FragmentLaunchMode.REPLACE,
            false
        )
    }
}