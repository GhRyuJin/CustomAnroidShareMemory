package com.baidu.ryujin.ktc

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.Parcel
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity2 : AppCompatActivity() {
    var mBinder: IBinder? = null
    private var mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            mBinder = service
        }

        override fun onServiceDisconnected(className: ComponentName) {
            mBinder = null
        }
    }

    fun read(view: View) {
        val data = Parcel.obtain()
        val reply = Parcel.obtain()
        mBinder?.apply {

            //从服务端获取MainActivity传递的文件描述符
            transact(TestShareMemoryService.TRANS_CODE_GET_FD, data, reply, 0)
            val readFileDescriptor = reply.readFileDescriptor()
            val myShareMemory = MyShareMemory(readFileDescriptor.fd)
            val sizeArray = ByteArray(4)
            myShareMemory.read(4, sizeArray) //先读取数据长度
            val size = Packet.byteArrayToIntLittle(sizeArray) //由于Message中使用的是小端序，所以这里要按照小端序解析
            val string = ByteArray(size)
            myShareMemory.read(size, 4, string) //读取data
            tv_msg.text = String(string, charset("utf-8"))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val intent = Intent(this, TestShareMemoryService::class.java)
        startService(intent)
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        unbindService(mConnection)
        super.onDestroy()
    }
}