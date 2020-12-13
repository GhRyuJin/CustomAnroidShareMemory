//
// Created by hasee on 2020/12/13.
//
#include "android_shm.h"

int create_shared_memory(const char *name, int64_t size, char *&addr, int64_t &fd) {
    fd = open(ASHMEM_NAME_DEF, O_RDWR);
    if (fd < 0) {
        return -1;
    }

    int len = get_shared_memory_size(fd);
    if (len > 0) {
        addr = (char *) mmap(NULL, size, PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);
        return 1;
    } else {
        int ret = ioctl(fd, ASHMEM_SET_NAME, name);
        if (ret < 0) {
            close(fd);
            return -1;
        }
        ret = ioctl(fd, ASHMEM_SET_SIZE, size);
        if (ret < 0) {
            close(fd);
            return -1;
        }
        addr = (char *) mmap(NULL, size, PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);
    }
    return 0;
}

int open_shared_memory(char *&addr, int64_t fd) {
    int size = get_shared_memory_size(fd);
    if (size > 0) {
        addr = (char *) mmap(NULL, size, PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);
    } else {
        return -1;
    }
    return 0;
}

int close_shared_memory(int64_t fd, char *&addr) {
    int size = get_shared_memory_size(fd);
    if (size < 0) {
        return -1;
    }
    int ret = munmap((void *) addr, size);
    if (ret == -1) {
        return -1;
    }
    ret = close(fd);
    if (ret == -1) {
        return -1;
    }
    return 0;
}

int get_shared_memory_size(int64_t fd) {
    return ioctl(fd, ASHMEM_GET_SIZE, NULL);
}
