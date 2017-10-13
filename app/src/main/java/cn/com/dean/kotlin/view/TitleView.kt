package cn.com.dean.kotlin.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue.COMPLEX_UNIT_SP
import android.view.*
import android.widget.Scroller
import android.widget.TextView
import cn.com.dean.kotlin.R

/**
 * Created: tvt on 17/9/13 14:13
 */
class TitleView : ViewGroup {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributes: AttributeSet?) : super(context, attributes)

    private var mChildWidth: Int = 0
    private var mChildSize: Int = 0
    private var mContext: Context = context.applicationContext
    private var mLastInterceptX: Float = 0F
    private var mLastInterceptY: Float = 0F
    private var mLastX: Float = 0F
    private var mLastY: Float = 0F
    private var mScroller: Scroller
    private var mVelocityTracker: VelocityTracker
    private var mSelectedIndex: Int = 0

    init {
        mScroller = Scroller(mContext)
        mVelocityTracker = VelocityTracker.obtain()
    }

    fun OnClick(v: View?) {
        if (v == null) {
            return
        }
        for (i in 0 until childCount) {
            val child = getChildAt(i) as TextView
            if (child == v) {
                child.isSelected = true
                child.setTextSize(COMPLEX_UNIT_SP, 15F)
                mSelectedIndex = i
            } else {
                child.isSelected = false
                child.setTextSize(COMPLEX_UNIT_SP, 14F)
            }
        }
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val childCount = childCount
        var width = 0
        var height = 0
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            for (i in 0 until childCount) {
                val child: View = getChildAt(i)
                width += child.measuredWidth
                if (height < child.measuredHeight) {
                    height = child.measuredHeight
                }
            }
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                width += child.measuredWidth
            }
            height = heightSpecSize
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if (height < child.measuredHeight) {
                    height = child.measuredHeight
                }
            }
            width = widthSpecSize
        } else {
            width = widthSpecSize
            height = heightSpecSize
        }
        setMeasuredDimension(width, height)
        mChildWidth = 0
        for (i in 0 until childCount) {
            mChildWidth += getChildAt(i).measuredWidth
        }
        mChildSize = mChildWidth
        if (mChildSize < measuredWidth) {
            mChildSize = measuredWidth
        }
        mChildWidth /= childCount
    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        var childLeft = 0
        for (i in 0 until childCount) {
            val child: View = getChildAt(i)
            val childWidth = child.measuredWidth
            child.layout(childLeft, 0, childLeft + childWidth, child.measuredHeight)
            childLeft += childWidth
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val child = getChildAt(mSelectedIndex)
        val width = child.measuredWidth / 3
        val height = measuredHeight / 10
        var left = (child.measuredWidth - width) / 2
        for (i in 0 until mSelectedIndex) {
            left += getChildAt(i).measuredWidth
        }
        val top = measuredHeight * 4 / 5

        val paint = Paint()
        paint.color = ContextCompat.getColor(mContext, R.color.tab_text_color_current)
        canvas.drawRect(left.toFloat(), top.toFloat(), (left + width).toFloat(), (top + height).toFloat(), paint)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (ev == null) {
            return false
        }
        var intercepted = false
        val x: Float = ev.x
        val y: Float = ev.y

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                intercepted = false
                if (!mScroller.isFinished) {
                    mScroller.abortAnimation()
                    intercepted = true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (mLastInterceptX > 0 && mLastInterceptY > 0) {
                    val deltaX = x - mLastInterceptX
                    val deltaY = y - mLastInterceptY
                    if (Math.abs(deltaX) < ViewConfiguration.get(mContext).scaledTouchSlop) {
                        return false
                    }
                    intercepted = Math.abs(deltaX) >= Math.abs(deltaY)
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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return false
        }
        mVelocityTracker.addMovement(event)
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!mScroller.isFinished) {
                    mScroller.abortAnimation()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (mLastX > 0 && mLastY > 0) {
                    val deltaX = x - mLastX
                    if (deltaX > 0 && (scrollX - Math.abs(deltaX)) >= 0) {
                        scrollBy(-deltaX.toInt(), 0)
                    } else if (deltaX < 0 && (Math.abs(deltaX) + scrollX) <= (mChildSize - measuredWidth)) {
                        scrollBy(-deltaX.toInt(), 0)
                    }
                }
                mLastX = x
                mLastY = y
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mVelocityTracker.computeCurrentVelocity(1000)
                val xVelocity = mVelocityTracker.xVelocity
                if (Math.abs(xVelocity) < 1000) {
                    mVelocityTracker.clear()
                    return true
                }
                var childIndex = scrollX / mChildWidth + if (xVelocity > 0) -1 else 1
                childIndex = if (xVelocity > 0) childIndex - 1 else childIndex + 1
                childIndex = Math.max(0, Math.min(childIndex, (mChildWidth * childCount + mChildWidth / 2 - measuredWidth) / mChildWidth))
                val dx = childIndex * mChildWidth - scrollX
                if (scrollX < mChildSize - measuredWidth) {
                    smoothScrollBy(dx, 0)
                }
                mVelocityTracker.clear()
                mLastX = 0F
                mLastY = 0F
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

    fun GetCount(): Int = childCount

}