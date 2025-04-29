
package com.example.voiceloop

import android.app.*
import android.content.Intent
import android.media.*
import android.os.*
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*
import java.nio.ByteBuffer

class VoiceLoopService : Service() {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var recorder: AudioRecord? = null

    private external fun nativeInit(modelDir: String)
    private external fun nativeProcessAudio(buf: ByteBuffer, len: Int): String
    private external fun nativeGenerateReply(prompt: String): String
    private external fun nativeSpeak(text: String)

    override fun onBind(intent: Intent?): IBinder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun startListening() = this@VoiceLoopService.startListening()
        fun stopListening() = this@VoiceLoopService.stopListening()
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(1, buildNotif("Idle"))
        nativeInit(filesDir.absolutePath + "/models")
    }

    private fun buildNotif(text: String): Notification {
        return NotificationCompat.Builder(this, "voiceloop")
            .setContentTitle("Sunny is running")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .build()
    }

    fun startListening() {
        if (recorder != null) return
        val minBuf = AudioRecord.getMinBufferSize(
            16000,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        recorder = AudioRecord(
            MediaRecorder.AudioSource.VOICE_RECOGNITION,
            16000,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            minBuf
        ).apply { startRecording() }

        scope.launch {
            val buf = ByteArray(2048)
            val bb = ByteBuffer.allocateDirect(buf.size)
            while (isActive && recorder != null) {
                val read = recorder!!.read(buf, 0, buf.size)
                bb.clear(); bb.put(buf, 0, read)
                val partial = nativeProcessAudio(bb, read)
                if (partial.endsWith("\n")) {
                    val reply = nativeGenerateReply(partial)
                    nativeSpeak(reply)
                }
            }
        }
        startForeground(1, buildNotif("Listening..."))
    }

    fun stopListening() {
        recorder?.run {
            stop()
            release()
        }
        recorder = null
        stopForeground(STOP_FOREGROUND_DETACH)
    }

    override fun onDestroy() {
        scope.cancel()
        stopListening()
        super.onDestroy()
    }
}
