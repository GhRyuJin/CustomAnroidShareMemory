//
// Created by hasee on 2020/12/13.
//

#ifndef KTC_LOG_H
#define KTC_LOG_H
//#define ENABLE_RYUJIN_LOG
#ifdef ENABLE_RYUJIN_LOG
#include <android/log.h>
#define RYUJIN_LOG_TAG  "RYUJIN"
#define RYUJINLOG(format, ...)  __android_log_print(ANDROID_LOG_ERROR, RYUJIN_LOG_TAG, format, ##__VA_ARGS__)
#else

#    define RYUJINLOG(format, ...)                                                                                     \
        {}
#endif
#endif //KTC_LOG_H
