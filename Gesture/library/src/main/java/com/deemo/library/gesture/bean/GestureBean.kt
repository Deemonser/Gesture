package com.deemo.library.gesture.bean

/**
 * author： deemo
 * date:    2020-01-11
 * desc:
 */
data class GestureBean(var type: GestureType, val positions: ArrayList<Pair<Float, Float>>)


enum class GestureType {
    TAP,
    SWIP
}