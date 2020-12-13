package com.baidu.ryujin.ktc

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var mBinder: Binder? = null
    var myShareMemory: MyShareMemory? = null
    var count: Int = 1
    private var mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            mBinder = service as Binder
        }

        override fun onServiceDisconnected(className: ComponentName) {
            mBinder = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val it = Intent(this@MainActivity, TestShareMemoryService::class.java)
        startService(it)
        bindService(it, mConnection, Context.BIND_AUTO_CREATE)
    }


    fun gotoMain2(view: View) {
        startActivity(Intent(this@MainActivity, MainActivity2::class.java))
    }

    fun createShareMemory(view: View) {
        myShareMemory = MyShareMemory.create("myShareMemory1", 1024)
    }

    fun write(view: View) {
        myShareMemory?.apply {
            write(Message.createMsg("count(${count++})"))
            btn_write.text = "write($count)"
        }
    }

    override fun onDestroy() {
        unbindService(mConnection)
        super.onDestroy()
    }

    fun read(view: View) {
        myShareMemory?.apply {
            val sizeArray = ByteArray(4)
            read(4, sizeArray)
            val size = Packet.byteArrayToIntLittle(sizeArray)
            val data = ByteArray(size)
            read(size,data)
            tv_msg.text = String(data, charset("utf-8"))
        }
    }

}
