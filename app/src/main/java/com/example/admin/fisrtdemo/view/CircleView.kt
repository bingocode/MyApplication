package com.example.admin.fisrtdemo.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.admin.fisrtdemo.R
import java.lang.reflect.Array
import android.content.res.TypedArray
import android.view.MotionEvent
import android.view.ViewGroup


class CircleView @JvmOverloads constructor(context: Context, attrs: AttributeSet ?= null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var mPaint: Paint? = null

    /** 圆半径  */
    private var mRadius = 50f
    private var mColor = Color.BLUE
    var lastX = 0
    var lastY = 0

    init {
        //val customAttrs = intArrayOf(R.attr.custom_color)
        val a = context.obtainStyledAttributes(attrs, R.styleable.CustomAttribute)
        mColor = a.getColor(R.styleable.CustomAttribute_custom_color, mColor)
        mRadius = a.getDimension(R.styleable.CustomAttribute_custom_radius,mRadius)
        a.recycle()
        setOnClickListener {  }
        init()
    }

    private fun init() {
        // 初始化画笔
        mPaint = Paint()
        mPaint!!.setColor(mColor)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        when(widthMode){
            MeasureSpec.AT_MOST ->{  //处理wrap_content
                widthSize = (mRadius * 2 + paddingLeft + paddingRight).toInt()
            }
            MeasureSpec.UNSPECIFIED  ->{}
            MeasureSpec.EXACTLY ->{}
        }
        when(heightMode){
            MeasureSpec.AT_MOST ->{
                heightSize = (mRadius * 2 + paddingTop + paddingBottom).toInt()
            }
            MeasureSpec.UNSPECIFIED  ->{}
            MeasureSpec.EXACTLY ->{}
        }
        setMeasuredDimension(widthSize,heightSize)
    }


     override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val mwidth = width - paddingLeft - paddingRight  //padding要自己处理，但margin则是父控件负责
        val mheight = height - paddingTop - paddingBottom
        canvas.drawCircle(mwidth / 2f + paddingLeft, mheight / 2f +paddingTop, mRadius, mPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean { //实现随着手指触摸移动
        var x = event.x.toInt()
        var y = event.y.toInt()
        when(event.action){
            MotionEvent.ACTION_DOWN ->{
                lastX = x
                lastY = y
            }
            MotionEvent.ACTION_MOVE ->{
                val offsetX = x - lastX
                val offsetY = y - lastY
                //layout(left + offsetX, top + offsetY, right + offsetX, bottom + offsetY) 这个方法松开手后又回到原地
               val lp : ViewGroup.MarginLayoutParams = layoutParams as ViewGroup.MarginLayoutParams

               lp .apply { //通过布局参数实现移动可以实现实际的交互功能
                    leftMargin = left + offsetX
                    topMargin = top + offsetY
                }

                layoutParams = lp
            }

        }
        return super.onTouchEvent(event) //一定要交给super否则单击事件失效
    }


}