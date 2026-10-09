package com.craneradius

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.craneradius.navigation.AppNavigation
import com.craneradius.ui.theme.CraneRadiusTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CraneRadiusTheme {
                AppNavigation()
            }
        }
    }
}
