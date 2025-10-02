package com.cevichepicante.composescrollshadow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.cevichepicante.ReadMeSampleSwipingPhoto
import com.cevichepicante.composescrollshadow.ui.theme.ComposeScrollShadowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeScrollShadowTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ReadMeSampleSwipingPhoto(Modifier.padding(innerPadding))
                }
            }
        }
    }
}