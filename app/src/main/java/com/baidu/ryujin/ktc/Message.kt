package com.baidu.ryujin.ktc

import com.baidu.ryujin.ktc.Packet.byteArrayToIntLittle
import com.baidu.ryujin.ktc.Packet.intToByteArrayLittle
import java.io.UnsupportedEncodingException

/**
 * @author ryujin
 * @version $
 * @time 2020/12/13 22:21
 * @updateAuthor $
 * @updateDate $
 */
class Message(msg: ByteArray) {
    var length: Int
    var msg: String? = null

    companion object {
        private const val MAX_LENGTH = 50 * 1024
        fun createMsg(msg: String): ByteArray {
            val length = msg.length
            val data = ByteArray(4 + length)
            try {
                System.arraycopy(intToByteArrayLittle(length), 0, data, 0, 4)
                System.arraycopy(msg.toByteArray(charset("utf-8")), 0, data, 4, length)
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
            return data
        }
    }

    init {
        require(msg.size >= 4) { "msg size must be greater than 4" }
        length = byteArrayToIntLittle(msg)
        require(!(length < 0 || length > MAX_LENGTH)) { "illegal msg size:$length" }
        require(length + 4 <= msg.size) { "decode msg failure!" }
        val data = ByteArray(length)
        System.arraycopy(msg, 4, data, 0, length)
        try {
            this.msg = String(data, charset("utf-8"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }
}