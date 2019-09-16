package com.example.mvvm_example.CustomViews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.min

class Bar(context: Context, attrs: AttributeSet?): View(context, attrs) {

    private val TAG = "Bar"

    private var onePercentWidth = 0f
    private val defaultWidth = (300*resources.displayMetrics.density).toInt()
    private val defaultHeight = (100*resources.displayMetrics.density).toInt()
    private var currentWidth = 0
    private var currentHeight = 0
    //prop
    var maxValue: Int = 100
        private set
    var currentValue: Int = 50
        private set
    var color: Int = Color.GREEN
    private lateinit var rectBar: Rect
    private lateinit var rectFrame: Rect
    private lateinit var textCenter: Point
    //paints
    private var paintBar: Paint
    private var paintFrame: Paint
    private var paintText: Paint
    init{
        paintBar = Paint().apply {
            color = this@Bar.color
            style = Paint.Style.FILL
        }
        paintFrame =Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 5 * resources.displayMetrics.density
        }
        paintText = Paint().apply {
            color = Color.BLACK
            textSize = 15 * resources.displayMetrics.scaledDensity
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        currentWidth = recognizeSize(widthMeasureSpec, defaultWidth)
        currentHeight = recognizeSize(heightMeasureSpec, defaultHeight)
        setMeasuredDimension(currentWidth, currentHeight)
        initView()
    }

    private fun recognizeSize(measureSpec: Int, defaultSize: Int): Int{
        Log.d(TAG, "onMeasure")
        when(MeasureSpec.getMode(measureSpec)){
            MeasureSpec.EXACTLY -> {
                Log.d(TAG, "EXACTLY")
                return MeasureSpec.getSize(measureSpec)
            }
            MeasureSpec.AT_MOST -> {
                Log.d(TAG, "AT_MOST")
                return min(defaultSize, MeasureSpec.getSize(measureSpec))
            }
            MeasureSpec.UNSPECIFIED ->{
                Log.d(TAG, "UNSPECIFIED")
                return (50*resources.displayMetrics.density).toInt()
            }
            else -> {
                //throw IllegalArgumentException("bla-bla-bla")
                return 0
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            it.drawRect(rectFrame.start.x, rectFrame.start.y, rectFrame.end.x, rectFrame.end.y, paintFrame)
            it.drawRect(rectBar.start.x, rectBar.start.y, rectBar.end.x, rectBar.end.y, paintBar)
            it.drawText(currentValue.toString(), textCenter.x, textCenter.y, paintText)
        }
    }

    private fun initView(){
        val barSize = calculateBarSize()

        onePercentWidth = (measuredWidth.toFloat() - paintFrame.strokeWidth*2) / maxValue
        val frameEndPoint = calculateFrameEndPoint()
        val barEndPoint = calculateBarEndPoint(barSize)
        rectFrame = Rect(Point(paintFrame.strokeWidth / 2, paintFrame.strokeWidth / 2), frameEndPoint)
        rectBar = Rect(Point(paintFrame.strokeWidth, paintFrame.strokeWidth),barEndPoint)
        textCenter = rectFrame.getCenter()
        textCenter.relocateWithMeasure(paintText.measureText(currentValue.toString()), (paintText.descent() + paintText.ascent()))
    }

    private fun calculateFrameEndPoint() = Point(
        measuredWidth.toFloat() - paintFrame.strokeWidth / 2,
        measuredHeight.toFloat() - paintFrame.strokeWidth / 2
    )
    private fun calculateBarEndPoint(barSize: Int) = Point(
        paintFrame.strokeWidth + onePercentWidth * barSize,
        measuredHeight - paintFrame.strokeWidth
    )

    private fun calculateBarSize(): Int{
        if (currentValue < 0)
            return 0
        return min(currentValue, maxValue)
    }

    fun redrawBar() {
        /*if (!::rectBar.isInitialized){
            requestLayout()
            invalidate()
            return
        }
        val barSize = calculateBarSize()
        Log.e(TAG, "barSize=$barSize")
        val barEndPoint = calculateBarEndPoint(barSize)
        rectBar.end = barEndPoint*/
        requestLayout()
        invalidate()
    }

    fun setCurrentValue(value: Int){
        currentValue = value
    }

    private class Rect(var start: Point, var end: Point){
        fun getCenter() = Point(
            (end.x - start.x)/2,
            (end.y - start.y)/2
        )
    }

    private class Point(var x: Float, var y: Float){
        override fun toString(): String {
            return "Point($x, $y)"
        }
    }

    private fun Point.relocateWithMeasure(measureX: Float, measureY: Float){
        x -= measureX / 2
        y -= measureY / 2
    }
}