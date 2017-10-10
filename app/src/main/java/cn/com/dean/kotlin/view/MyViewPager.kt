package cn.com.dean.kotlin.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup

/**
 * Created: tvt on 17/9/18 15:10
 */
class MyViewPager : ViewGroup {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)

    private var mChildWidth: Int = 0
    private var mChildHeight: Int = 0
    private var mLastX: Float = 0F
    private var mLastY: Float = 0F

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)

        var height = heightSpecSize
        if (heightSpecMode != MeasureSpec.EXACTLY) {
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if (child.measuredHeight > height) {
                    height = child.measuredHeight
                }
            }
        }
        mChildWidth = widthSpecSize
        setMeasuredDimension(widthSpecSize, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val size = childCount
        var left = 0
        for (i in 0 until size) {
            val child = getChildAt(i)
            child.layout(left + paddingLeft, paddingTop, left + paddingLeft + child.measuredWidth, child.measuredHeight - paddingTop - paddingBottom)
            left += mChildWidth
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val x = ev.x
        val y = ev.y
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - mLastX
                val deltaY = y - mLastY
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    parent.requestDisallowInterceptTouchEvent(false)
                }
            }
        }
        mLastX = x
        mLastY = y
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            return false
        }
        return true
    }
}