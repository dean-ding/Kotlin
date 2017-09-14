package cn.com.dean.kotlin.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * Created: tvt on 17/9/14 11:17
 */
open class TabView : ViewGroup, View.OnClickListener {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)

    private lateinit var mContext: Context

    init {
        mContext = context
        setOnClickListener(this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        val count = childCount
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)

        var width = 0
        var height = 0
        if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
            width = widthSpecSize
            height = heightSpecSize
        } else if (widthSpecMode == MeasureSpec.EXACTLY) {
            width = widthSpecSize
            for (i in 0 until count) {
                val child: View = getChildAt(i)
                height += child.measuredHeight
            }
        } else if (heightSpecMode == MeasureSpec.EXACTLY) {
            for (i in 0 until count) {
                val child: View = getChildAt(i)
                if (child.measuredWidth > width) {
                    width = child.measuredWidth
                }
            }
            height = heightSpecSize
        } else {
            for (i in 0 until count) {
                val child: View = getChildAt(i)
                if (child.measuredWidth > width) {
                    width = child.measuredWidth
                }
                height += child.measuredHeight
            }
        }
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var childTop = 0
        for (i in 0 until childCount) {
            val child: View = getChildAt(i)
            val childLeft = (measuredWidth - child.measuredWidth) / 2
            child.layout(childLeft, childTop + child.paddingTop, childLeft + child.measuredWidth, childTop + child.paddingTop + child.measuredHeight)
            childTop += child.measuredHeight + child.paddingTop
        }
    }

    private var mCallback: TabCallback? = null
    fun setCallback(callback: TabCallback) {
        this.mCallback = callback
    }

    interface TabCallback {
        fun onTabClick(v: View?)
    }

    override fun onClick(v: View?) {
        if (mCallback != null) {
            mCallback!!.onTabClick(v)
        }
        for (i in 0 until childCount) {
            val child: View = getChildAt(i)
            child.isSelected = true
        }
    }


    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        for (i in 0 until childCount) {
            val child: View = getChildAt(i)
            child.isSelected = selected
        }
    }

}