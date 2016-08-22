#include<string.h>
#include<jni.h>

jstring Java_com_lyric_android_app_activity_WebActivity_sayHello(JNIEnv *env, jobject thiz) {
	return (*env)->NewStringUTF(env, "Hello form JNI!");
}

