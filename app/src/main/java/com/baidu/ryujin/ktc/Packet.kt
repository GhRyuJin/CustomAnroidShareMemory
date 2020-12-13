package com.baidu.ryujin.ktc

/**
 * @author ryujin
 * @time 2020/12/13 22:22
 *
 * @version $
 * @updateAuthor $
 * @updateDate $
 */
object Packet {
    fun byteArrayToIntLittle(byt: ByteArray): Int {
        return when (byt.size) {
            1 -> {
                0xff and byt[0].toInt()
            }
            2 -> {
                0xff and byt[0].toInt() or (0xff and byt[1].toInt() shl 8)
            }
            4 -> {
                0xff and byt[0].toInt() or (0xff and byt[1].toInt() shl 8) or (0xff and byt[2]
                    .toInt() shl 16) or (0xff and byt[3].toInt() shl 24)
            }
            else -> {
                0
            }
        }
    }
    fun intToByteArrayLittle(value: Int): ByteArray {
        return byteArrayOf(
            value.toByte(), (value ushr 8).toByte(), (value ushr 16).toByte(),
            (value ushr 24).toByte()
        )
    }
}