package com.baidu.ryujin.ktc

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.Parcel
import android.os.ParcelFileDescriptor

/**
 * @author ryujin
 * @time 2020/12/13 21:45
 *
 * @version $
 * @updateAuthor $
 * @updateDate $
 */
class TestShareMemoryService : Service() {
    lateinit var fd: ParcelFileDescriptor

    companion object {
        const val TRANS_CODE_GET_FD = 0x0000
        const val TRANS_CODE_SET_FD = 0x0001
    }

    override fun onBind(intent: Intent?): IBinder {
        return TestBinder()
    }

    inner class TestBinder : Binder() {

        override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
            when (code) {
                TRANS_CODE_SET_FD -> {
                    fd = data.readFileDescriptor()
                }
                TRANS_CODE_GET_FD -> {
                    reply?.writeFileDescriptor(fd.fileDescriptor)
                }
            }
            return true
        }
    }
}