package com.example.admin.fisrtdemo.view

import android.content.Context
import android.util.AttributeSet
import android.widget.Scroller
import android.widget.LinearLayout



/**
 * Created by zengbin on 2017/8/14.
 */
class ScrollerLinearLayout(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val mScroller: Scroller

    init {
        mScroller = Scroller(context)
    }

    fun startScroll() {
        mScroller.startScroll(scrollX, scrollY, -50, -50)
        invalidate()
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY)
            invalidate()
        }
    }
}