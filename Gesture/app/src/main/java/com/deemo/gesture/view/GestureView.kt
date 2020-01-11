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

    val pathList: ArrayList<Path> = arrayListOf()
    val dotList: ArrayList<Pair<Float, Float>> = arrayListOf()

    private val paint by lazy { Paint() }

    private var actionListener: ((ArrayList<Pair<Float, Float>>, ArrayList<Path>) -> Unit)? = null

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

    private var path: Path? = null

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                dotList.add(Pair(event.x, event.y))
            }
            MotionEvent.ACTION_UP -> {
                path?.let {
                    pathList.add(it)
                }
                path = null
            }
            MotionEvent.ACTION_MOVE -> {
                if (path == null) {
                    path = Path().apply {
                        val last = dotList.last()
                        this.moveTo(last.first, last.second)
                    }
                }

                path?.let {
                    it.lineTo(event.x, event.y)
                }
            }
        }

        invalidate()

        if (event.action == MotionEvent.ACTION_UP) {
            actionListener?.let {
                it(dotList, pathList)
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)

        paint.style = Paint.Style.FILL
        dotList.forEach {
            canvas.drawCircle(it.first, it.second, paint.strokeWidth / 2, paint)
        }

        paint.style = Paint.Style.STROKE
        pathList.forEach {
            canvas.drawPath(it, paint)
        }


        path?.let {
            canvas.drawPath(it, paint)
        }

    }


    fun reset() {
        path = null
        pathList.clear()
        dotList.clear()

        invalidate()
    }

    fun setActionListener(listener: (dots:ArrayList<Pair<Float, Float>>, paths:ArrayList<Path>) -> Unit) {
        actionListener = listener
    }
}