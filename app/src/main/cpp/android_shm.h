//
// Created by hasee on 2020/12/13.
//

#ifndef KTC_ANDROID_SHM_H
#define KTC_ANDROID_SHM_H

#include <fcntl.h>
#include <stdio.h>
#include <linux/ashmem.h>
#include <sys/mman.h>
#include <sys/ioctl.h>
#include <unistd.h>
#include <stdlib.h>

__BEGIN_DECLS


int create_shared_memory(const char *name, int64_t size, char *&addr, int64_t &fd);

int open_shared_memory(char *&addr, int64_t fd);

int close_shared_memory(int64_t fd, char *&addr);

int get_shared_memory_size(int64_t fd);
__END_DECLS

#endif //KTC_ANDROID_SHM_H
