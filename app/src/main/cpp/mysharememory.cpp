#include <jni.h>
#include <string>
#include "android_shm.h"
#include "Log.h"

extern "C"
JNIEXPORT jint JNICALL
Java_com_baidu_ryujin_ktc_MyShareMemory_nCreate(JNIEnv *env, jclass clazz, jstring name,
                                                jint size) {
    char *addr;
    int64_t ufd = 0;
    const char *_name = env->GetStringUTFChars(name, 0);
    int ret = create_shared_memory(_name, size, addr, ufd);
    RYUJINLOG("nCreate ret = %d, addr = %p", ret, &addr);
    return ufd;
}extern "C"
JNIEXPORT jint JNICALL
Java_com_baidu_ryujin_ktc_MyShareMemory_nGetSize(JNIEnv *env, jclass clazz,
                                                 jint fd) {
    return get_shared_memory_size(fd);
}extern "C"
JNIEXPORT void JNICALL
Java_com_baidu_ryujin_ktc_MyShareMemory_nClose(JNIEnv *env, jclass clazz, jint fd) {
    char *addr;
    open_shared_memory(addr, fd);
    RYUJINLOG("nClose addr = %p", &addr);
    close_shared_memory(fd, addr);
}extern "C"
JNIEXPORT jint JNICALL
Java_com_baidu_ryujin_ktc_MyShareMemory_nWrite(JNIEnv *env, jclass clazz, jint fd,
                                               jint size, jint offset, jbyteArray data_) {
    char *addr;
    int space = get_shared_memory_size(fd) - offset;
    RYUJINLOG("nWrite space = %d, size = %d", space, size);
    if (size - space > 0) {
        return -1;
    }
    open_shared_memory(addr, fd);
    RYUJINLOG("nWrite addr = %p", &addr);
    jbyte *data = env->GetByteArrayElements(data_, 0);
    memcpy(addr + offset, data, size);
    env->ReleaseByteArrayElements(data_, data, 0);
    return 0;

}extern "C"
JNIEXPORT jint JNICALL
Java_com_baidu_ryujin_ktc_MyShareMemory_nRead(JNIEnv *env, jclass clazz, jint fd, jint size,
                                              jint offset, jbyteArray data_) {

    int space = get_shared_memory_size(fd) - offset;
    RYUJINLOG("nRead space = %d, size = %d", space, size);
    if (size - space > 0) {
        return -1;
    }
    char *addr;
    open_shared_memory(addr, fd);
    RYUJINLOG("nRead addr = %p", &addr);
    jbyte *data = env->GetByteArrayElements(data_, 0);
    memcpy(data, addr + offset, size);
    env->ReleaseByteArrayElements(data_, data, 0);
    return 0;
}