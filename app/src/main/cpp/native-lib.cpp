#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring

JNICALL
Java_cash_indonesia_optima_prima_cashflow_splashscreen_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
