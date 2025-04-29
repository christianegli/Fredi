
# KidAIVoicePrototype

Offline, guard‑railed AI voice assistant for 4‑year‑olds.

* Whisper.cpp STT
* llama.cpp / Gemma‑2B LLM
* Coqui XTTS TTS
* Spotify control via Spotify App Remote SDK

## Quick start

```bash
git clone <this‑repo>
./gradlew installDebug            # builds native + apk
adb push models/ /sdcard/ai/models
adb shell am start -n "com.example.voiceloop/.MainActivity"
```

See `docs/BUILD.md` for details.
