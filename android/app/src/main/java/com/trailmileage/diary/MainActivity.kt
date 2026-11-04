package com.trailmileage.diary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.trailmileage.diary.core.di.ServiceLocator
import com.trailmileage.diary.core.theme.AppTheme
import com.trailmileage.diary.presentation.app.AppRoot

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        ServiceLocator.init(applicationContext)
        setContent {
            AppTheme {
                AppRoot()
            }
        }
    }
}
