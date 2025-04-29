
#include <jni.h>
#include <string>
#include <android/log.h>

#define TAG "VoiceLoopJNI"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)

extern "C"
JNIEXPORT void JNICALL
Java_com_example_voiceloop_VoiceLoopService_nativeInit(JNIEnv *env, jobject thiz, jstring model_dir_) {
    const char *model_dir = env->GetStringUTFChars(model_dir_, 0);
    LOGI("Init with models at %s", model_dir);
    // TODO: init whisper.cpp & llama.cpp contexts
    env->ReleaseStringUTFChars(model_dir_, model_dir);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_voiceloop_VoiceLoopService_nativeProcessAudio(JNIEnv *env, jobject thiz,
                                                               jobject buf, jint len) {
    // TODO: feed PCM to whisper live transcriber
    std::string partial = "";
    return env->NewStringUTF(partial.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_voiceloop_VoiceLoopService_nativeGenerateReply(JNIEnv *env, jobject thiz,
                                                                jstring prompt_) {
    const char *prompt = env->GetStringUTFChars(prompt_, 0);
    // TODO: call llama.cpp generate
    std::string reply = "Hi there!";
    env->ReleaseStringUTFChars(prompt_, prompt);
    return env->NewStringUTF(reply.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_voiceloop_VoiceLoopService_nativeSpeak(JNIEnv *env, jobject thiz,
                                                        jstring text_) {
    const char *text = env->GetStringUTFChars(text_, 0);
    // TODO: run XTTS and stream to AudioTrack
    LOGI("Would speak: %s", text);
    env->ReleaseStringUTFChars(text_, text);
}
