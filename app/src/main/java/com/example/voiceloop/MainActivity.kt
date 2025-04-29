
package com.example.voiceloop

import android.content.*
import android.os.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {
    private var serviceBound by mutableStateOf(false)
    private lateinit var service: VoiceLoopService.LocalBinder

    private val conn = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            service = binder as VoiceLoopService.LocalBinder
            serviceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            serviceBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, VoiceLoopService::class.java).also {
            bindService(it, conn, BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (serviceBound) unbindService(conn)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Press & Hold to talk to Sunny")
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { /* held press handled below */ },
                        modifier = Modifier.height(80.dp)
                    ) {
                        Text("🎤", style = MaterialTheme.typography.displayLarge)
                    }.apply {
                        setOnTouchListener { _, event ->
                            when (event.action) {
                                android.view.MotionEvent.ACTION_DOWN -> {
                                    if (serviceBound) service.startListening()
                                }
                                android.view.MotionEvent.ACTION_UP,
                                android.view.MotionEvent.ACTION_CANCEL -> {
                                    if (serviceBound) service.stopListening()
                                }
                            }
                            true
                        }
                    }
                }
            }
        }
    }
}
