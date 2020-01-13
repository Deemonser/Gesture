package com.deemo.gesture.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * author： deemo
 * date:    2020-01-10
 * desc:
 */
class GestureView : View {

    val swipeList: ArrayList<Swipe> = arrayListOf()

    val pointList: ArrayList<Point> = arrayListOf()

    private val paint by lazy { Paint() }

    private var actionListener: ((ArrayList<Point>, ArrayList<Swipe>) -> Unit)? =
        null

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        init()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }


    private fun init() {

        paint.setColor(Color.BLACK)
        paint.strokeWidth = 8f


    }

    private var pathList: ArrayList<Point>? = null
    private var lastDownTime = 0L

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                pointList.add(Point(event.x, event.y))
                lastDownTime = System.currentTimeMillis()
            }
            MotionEvent.ACTION_UP -> {
                pathList?.let {
                    swipeList.add(Swipe(it, System.currentTimeMillis() - lastDownTime))
                }
                pathList = null
            }

            MotionEvent.ACTION_MOVE -> {
                if (pathList == null) {
                    val last = pointList.last()
                    pathList = arrayListOf(last)
                }

                pathList?.let {
                    it.add(Point(event.x, event.y))
                }
            }
        }

        invalidate()

        if (event.action == MotionEvent.ACTION_UP) {
            actionListener?.let {
                it(pointList, swipeList)
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)

        paint.style = Paint.Style.FILL
        pointList.forEach {
            canvas.drawCircle(it.x, it.y, paint.strokeWidth / 2, paint)
        }

        paint.style = Paint.Style.STROKE
        swipeList.forEach {
            canvas.drawPath(toPath(it.paths), paint)
        }


        pathList?.let {
            canvas.drawPath(toPath(it), paint)
        }

    }

    private fun toPath(list: ArrayList<Point>): Path {
        val path = Path()
        list.forEachIndexed { index, pair ->
            if (index == 0) {
                path.moveTo(pair.x, pair.y)
            } else {
                path.lineTo(pair.x, pair.y)
            }
        }
        return path
    }

    fun reset() {
        pathList = null
        swipeList.clear()
        pointList.clear()

        invalidate()
    }

    fun setActionListener(listener: (points: ArrayList<Point>, paths: ArrayList<Swipe>) -> Unit) {
        actionListener = listener
    }


    data class Point(val x: Float, val y: Float)
    data class Swipe(val paths: ArrayList<Point>, val duration: Long)
}