package com.baidu.ryujin.ktc

import android.os.ParcelFileDescriptor
import java.io.FileDescriptor

/**
 * @author ryujin
 * @time 2020/12/13 19:58
 *
 * @version $
 * @updateAuthor $
 * @updateDate $
 */
class MyShareMemory(fd: Int) {
    private val mFd: Int = fd
    private val mSize: Int

    init {
        mSize = nGetSize(mFd)
        require(mSize > 0) { "FileDescriptor is not a valid ashmem fd" }
    }


    fun getSize(): Int {
        return mSize
    }

    fun close() {
        nClose(mFd)
    }

    fun write(data: ByteArray) {
        nWrite(mFd, data.size, data)
    }

    fun read(size: Int, data: ByteArray) {
        nRead(mFd, size, data)
    }

    companion object {
        init {
            System.loadLibrary("mysharememory-lib")
        }

        fun create(name: String, size: Int): MyShareMemory {
            require(size > 0) { "Size must be greater than zero" }
            return MyShareMemory(nCreate(name, size))
        }


        @JvmStatic
        private external fun nCreate(name: String, size: Int): Int

        @JvmStatic
        private external fun nGetSize(fd: Int): Int

        @JvmStatic
        private external fun nClose(fd: Int)

        @JvmStatic
        private external fun nWrite(fd: Int, size: Int, data: ByteArray): Int

        @JvmStatic
        private external fun nRead(fd: Int, size: Int, data: ByteArray): Int

    }
}