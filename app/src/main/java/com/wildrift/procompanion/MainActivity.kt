package com.wildrift.procompanion

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wildrift.procompanion.overlay.OverlayService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFFBB86FC),
                    background = Color(0xFF121212),
                    surface = Color(0xFF1E1E1E)
                )
            ) {
                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF121212)) {
                    MainScreen(
                        onStartOverlay = { startOverlay() },
                        onStopOverlay = { stopOverlay() },
                        onRequestPermission = { requestOverlayPermission() }
                    )
                }
            }
        }
    }

    private fun startOverlay() {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "Please allow Display over other apps", Toast.LENGTH_LONG).show()
            requestOverlayPermission()
            return
        }
        startService(Intent(this, OverlayService::class.java))
        Toast.makeText(this, "Overlay started", Toast.LENGTH_SHORT).show()
    }

    private fun stopOverlay() {
        stopService(Intent(this, OverlayService::class.java))
        Toast.makeText(this, "Overlay stopped", Toast.LENGTH_SHORT).show()
    }

    private fun requestOverlayPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        startActivity(intent)
    }
}

@Composable
fun MainScreen(
    onStartOverlay: () -> Unit,
    onStopOverlay: () -> Unit,
    onRequestPermission: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Wild Rift Pro Companion",
            color = Color.White,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onRequestPermission,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("1. Allow Overlay Permission")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onStartOverlay,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("2. Start Overlay")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onStopOverlay,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
        ) {
            Text("Stop Overlay")
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Open Wild Rift after starting the overlay",
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}
