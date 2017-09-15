package cn.com.dean.kotlin.News

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.*
import android.widget.Scroller
import cn.com.dean.kotlin.R

class Recommend : ViewGroup {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)

    private var mChildCount: Int = 0
    private var mSelected: Int = 0
    private var mLastInterceptX: Float = 0F
    private var mLastInterceptY: Float = 0F
    private var mLastTouchX: Float = 0F
    private var mLastTouchY: Float = 0F
    private var mScroller: Scroller = Scroller(context)
    private var mVelocityTracker: VelocityTracker = VelocityTracker.obtain()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        mChildCount = childCount
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)

        var width = widthSpecSize
        var height = 0
        if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
            width = widthSpecSize
            height = heightSpecSize
        } else if (widthSpecMode == MeasureSpec.EXACTLY) {
            width = widthSpecSize
            (0 until mChildCount)
                    .asSequence()
                    .map { getChildAt(it) }
                    .filter { it.measuredHeight > height }
                    .forEach { height = it.measuredHeight }
        } else if (heightSpecMode == MeasureSpec.EXACTLY) {
            height = heightSpecSize
            (0 until mChildCount)
                    .asSequence()
                    .map { getChildAt(it) }
                    .filter { it.measuredWidth > width }
                    .forEach { width = it.measuredWidth }
        } else {
            for (i in 0 until mChildCount) {
                val child: View = getChildAt(i)
                if (child.measuredWidth > width) {
                    width = measuredWidth
                }
                if (child.measuredHeight > height) {
                    height = measuredHeight
                }
            }
        }
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = 0
        for (i in 0 until mChildCount) {
            val child: View = getChildAt(i)
            val top = paddingTop
            val right = paddingRight
            val bottom = paddingBottom
            child.layout(left + paddingLeft, top, left + paddingLeft + child.measuredWidth - right, top + child.measuredHeight - bottom)
            left += measuredWidth
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) {
            return
        }

        val paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.tab_text_color_current)

        val width = 10
        var left = (measuredWidth - childCount * width - (childCount - 1) * 2 * width) / 2
        val top = measuredHeight * 9 / 10
        for (i in 0 until mChildCount) {
            if (i == mSelected) {
                paint.color = ContextCompat.getColor(context, R.color.tab_text_color_current)
            } else {
                paint.color = ContextCompat.getColor(context, R.color.tab_line_color)
            }
            canvas.drawCircle(left.toFloat(), top.toFloat(), width.toFloat(), paint)
            left += width * 3
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercepted = false
        val x: Float = ev.x
        val y: Float = ev.y
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                intercepted = false
                if (!mScroller.isFinished) {
                    intercepted = true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (mLastInterceptX > 0 && mLastInterceptY > 0) {
                    val deltaX: Float = x - mLastInterceptX
                    val deltaY: Float = y - mLastInterceptY
                    intercepted = Math.abs(deltaX) > Math.abs(deltaY) && scrollX <= measuredWidth * mChildCount && scrollX >= 0
                }
                mLastInterceptX = x
                mLastInterceptY = y
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                intercepted = false
                mLastInterceptX = 0F
                mLastInterceptY = 0F
            }
        }
        return intercepted
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mVelocityTracker.addMovement(event)
        val x = event.x
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!mScroller.isFinished) {
                    mScroller.abortAnimation()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (mLastTouchX > 0F && mLastTouchY > 0F) {
                    val deltaX = x - mLastTouchX
                    if (Math.abs(deltaX) > ViewConfiguration.get(context).scaledTouchSlop) {
                        scrollBy(-deltaX.toInt(), 0)
                    }
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mVelocityTracker.computeCurrentVelocity(1000)
                val velocityX: Float = mVelocityTracker.xVelocity
                if (velocityX < 1000F) {
                    mVelocityTracker.clear()
                    return true
                }
                var scroll = scrollX % measuredWidth
                if (scroll > measuredWidth / 2) {
                    mSelected += 1
                    scroll = measuredWidth - scroll
                } else {
                    mSelected -= 1
                }
                smoothScrollBy(scroll, 0)
                invalidate()
                mVelocityTracker.clear()
                mLastTouchX = 0F
                mLastTouchY = 0F
            }
        }
        return true
    }

    private fun smoothScrollBy(dx: Int, dy: Int) {
        mScroller.startScroll(scrollX, 0, dx, dy, 500)
        invalidate()
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY)
            postInvalidate()
        }
    }

    override fun onDetachedFromWindow() {
        mVelocityTracker.clear()
        super.onDetachedFromWindow()
    }
}