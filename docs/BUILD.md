
### Build steps

1. Install **Android Studio Iguana** with NDK 27.
2. Clone submodules:

```bash
git submodule update --init --recursive
```

3. Download models into `app/src/main/assets/models/` or push at runtime to `/sdcard/ai/models`.

4. Build:

```bash
./gradlew assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk
```
