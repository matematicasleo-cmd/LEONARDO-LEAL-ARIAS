package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.audio.LocalSpaceSoundManager
import com.example.audio.SpaceSoundManager
import com.example.data.AstroRepository
import com.example.data.local.AstroDatabase
import com.example.ui.AstroMateApp
import com.example.ui.AstroViewModel
import com.example.ui.theme.AstroMateTheme

class MainActivity : ComponentActivity() {
    private lateinit var soundManager: SpaceSoundManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )

        soundManager = SpaceSoundManager()
        val database = AstroDatabase.getInstance(applicationContext)
        val repository = AstroRepository(database.astroDao())

        setContent {
            AstroMateTheme {
                CompositionLocalProvider(LocalSpaceSoundManager provides soundManager) {
                    val viewModel: AstroViewModel = viewModel(
                        factory = AstroViewModel.Factory(repository)
                    )
                    AstroMateApp(viewModel = viewModel)
                }
            }
        }
    }

    override fun onDestroy() {
        if (::soundManager.isInitialized) {
            soundManager.release()
        }
        super.onDestroy()
    }
}
