package com.nikola.jakshic.dagger.ui.matchstats.comparison

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.nikola.jakshic.dagger.R
import java.lang.Math.*
import java.util.*

class SpiderChart @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val dataPath = Path()
    private val fillPath = Path()
    private val strokePath = Path()
    private val webPath = Path()

    private val dataPaint = Paint()
    private val fillPaint = Paint()
    private val strokePaint = Paint()
    private val webPaint = Paint()
    private val labelPaint = Paint()

    private val listOfData = mutableListOf<SpiderData>()

    private var maxTextSize = 0F

    private val textBounds = Rect()

    private val labels = mutableListOf<String>()

    private var totalSides = 0

    init {
        if (isInEditMode) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SpiderChart)
            try {
                // Setup Web Paint
                webPaint.color = typedArray.getColor(R.styleable.SpiderChart_sc_preview_webColor, Color.LTGRAY)
                webPaint.style = Paint.Style.STROKE
                webPaint.strokeWidth =
                        typedArray.getDimension(R.styleable.SpiderChart_sc_preview_webStrokeSize, dpToPixels(1F))
                webPaint.isAntiAlias = true
                // Setup Stroke Paint
                strokePaint.color =
                        typedArray.getColor(R.styleable.SpiderChart_sc_preview_strokeColor, Color.DKGRAY)
                strokePaint.style = Paint.Style.STROKE
                strokePaint.strokeWidth =
                        typedArray.getDimension(R.styleable.SpiderChart_sc_preview_strokeSize, dpToPixels(2F))
                strokePaint.isAntiAlias = true
                // Setup Fill Paint
                fillPaint.color =
                        typedArray.getColor(R.styleable.SpiderChart_sc_preview_fillColor, Color.TRANSPARENT)
                fillPaint.style = Paint.Style.FILL
                fillPaint.isAntiAlias = true
                // Setup Label Paint
                labelPaint.color = typedArray.getColor(R.styleable.SpiderChart_sc_preview_labelColor, Color.DKGRAY)
                labelPaint.textSize =
                        typedArray.getDimension(R.styleable.SpiderChart_sc_preview_labelSize, spToPixels(11F))
                labelPaint.textAlign = Paint.Align.CENTER
                labelPaint.isAntiAlias = true
                // Setup Data Paint
                dataPaint.style = Paint.Style.FILL
                dataPaint.isAntiAlias = true

                // Setup Fake Data for layout editor
                totalSides = typedArray.getInt(R.styleable.SpiderChart_sc_preview_totalSides, 5)
                repeat(totalSides) {
                    labels.add("ART$it")
                }
                maxTextSize = calculateMaxTextSize()
                val totalEntries = typedArray.getInt(R.styleable.SpiderChart_sc_preview_totalEntries, 2)

                val data = mutableListOf<SpiderData>()
                repeat(totalEntries) {
                    val values = mutableListOf<Float>()
                    repeat(totalSides) {
                        values.add((Math.random() * 100).toFloat())
                    }
                    data.add(
                            SpiderData(
                                    values,
                                    Color.argb(125, Random().nextInt(256), Random().nextInt(256), Random().nextInt(256))
                            )
                    )
                }
                setData(data)
            } finally {
                typedArray.recycle()
            }
        } else {
            // Setup Web Paint
            webPaint.color = Color.LTGRAY
            webPaint.style = Paint.Style.STROKE
            webPaint.strokeWidth = dpToPixels(0.5F)
            webPaint.isAntiAlias = true
            // Setup Stroke Paint
            strokePaint.color = ContextCompat.getColor(context, R.color.colorAccent)
            strokePaint.style = Paint.Style.STROKE
            strokePaint.strokeWidth = dpToPixels(1F)
            strokePaint.isAntiAlias = true
            // Setup Fill Paint
            fillPaint.color = Color.TRANSPARENT
            fillPaint.style = Paint.Style.FILL
            fillPaint.isAntiAlias = true
            // Setup Label Paint
            labelPaint.color = Color.DKGRAY
            labelPaint.textSize = spToPixels(10F)
            labelPaint.textAlign = Paint.Align.CENTER
            labelPaint.isAntiAlias = true
            // Setup Data Paint
            dataPaint.style = Paint.Style.FILL
            dataPaint.isAntiAlias = true
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2F
        val centerY = height / 2F

        val lineLength = min(centerX, centerY) - maxTextSize

        drawPolygon(canvas, strokePath, strokePaint, lineLength)
        drawPolygon(canvas, fillPath, fillPaint, lineLength)
        drawWebs(canvas, webPath, webPaint, lineLength)
        drawData(canvas, dataPath, dataPaint, lineLength)
        drawLabels(canvas, labelPaint, lineLength)
    }

    private fun drawData(canvas: Canvas, path: Path, paint: Paint, lineLength: Float) {
        val centerX = width / 2F
        val centerY = height / 2F

        for (data in listOfData) {
            val values = data.values
            val color = data.color

            paint.color = color
            path.reset()

            for (i in 0 until values.size) {
                val angle = i * (360.0 / totalSides) - 90

                var value = values[i]
                if (value < 0) value = 0F
                if (value > 100) value = 100F

                val x = centerX + (lineLength * (value / 100)) * (cos(toRadians(angle))).toFloat()
                val y = centerY + (lineLength * (value / 100)) * (sin(toRadians(angle))).toFloat()

                if (i == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }

            canvas.drawPath(path, paint)
        }
    }

    private fun drawWebs(canvas: Canvas, path: Path, paint: Paint, lineLength: Float) {
        fun drawWeb(canvas: Canvas, paint: Paint, lineLength: Float) {
            val centerX = width / 2F
            val centerY = height / 2F

            for (i in 0..totalSides) {

                val angle = i * (360.0 / totalSides) - 90

                val x = centerX + (lineLength) * (cos(toRadians(angle))).toFloat()
                val y = centerY + (lineLength) * (sin(toRadians(angle))).toFloat()

                if (i == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                    path.moveTo(x, y)
                }
            }
            canvas.drawPath(path, paint)
        }

        fun drawStraightLines(canvas: Canvas, paint: Paint, lineLength: Float) {
            val centerX = width / 2F
            val centerY = height / 2F

            for (i in 0..totalSides) {

                val angle = i * (360.0 / totalSides) - 90

                val x = centerX + (lineLength) * (cos(toRadians(angle))).toFloat()
                val y = centerY + (lineLength) * (sin(toRadians(angle))).toFloat()

                if (i == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(centerX, centerY)
                    path.moveTo(x, y)
                }
            }
            canvas.drawPath(path, paint)
        }

        for (i in 0 until totalSides) {
            if (i == 0) {
                drawStraightLines(canvas, paint, lineLength - i * (lineLength / totalSides))
            } else {
                drawWeb(canvas, paint, lineLength - i * (lineLength / totalSides))
            }
        }
    }

    private fun drawPolygon(canvas: Canvas, path: Path, paint: Paint, lineLength: Float) {
        val centerX = width / 2F
        val centerY = height / 2F

        for (i in 0..totalSides) {

            val angle = i * (360.0 / totalSides) - 90

            val x = centerX + (lineLength) * (cos(toRadians(angle))).toFloat()
            val y = centerY + (lineLength) * (sin(toRadians(angle))).toFloat()

            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        canvas.drawPath(path, paint)
    }

    private fun drawLabels(canvas: Canvas, paint: Paint, lineLength: Float) {
        val centerX = width / 2F
        val centerY = height / 2F

        for (i in 0 until labels.size) {

            val angle = i * (360.0 / totalSides) - 90

            val x = centerX + (lineLength) * (cos(toRadians(angle))).toFloat()
            val y = centerY + (lineLength) * (sin(toRadians(angle))).toFloat()

            var textX = x
            var textY = y

            val textWidth = paint.measureTextWidth(labels[i])
            val textHeight = paint.measureTextHeight(labels[i])

            if (y > centerY) {
                textY = y + textHeight + textHeight / 2
            }
            if (y < centerY) {
                textY = y - textHeight / 2
            }
            if (x < centerX) {
                textX = x - textWidth / 2
            }
            if (x > centerX) {
                textX = x + textWidth / 2
            }
            if (y == centerY) {
                if (x < centerX) {
                    textX = x - maxTextSize / 2
                }
                if (x > centerX) {
                    textX = x + maxTextSize / 2
                }
            }

            canvas.drawText(labels[i], textX, textY, paint)
        }
    }

    fun setData(data: List<SpiderData>) {
        listOfData.clear()
        listOfData.addAll(data)

        if (data.isNotEmpty()) {
            totalSides = data[0].values.size
        }

        invalidate()
    }

    override fun invalidate() {
        super.invalidate()
        dataPath.reset()
        fillPath.reset()
        strokePath.reset()
        webPath.reset()
    }

    fun setLabels(labels: List<String>?) {
        this.labels.clear()
        if (labels != null)
            this.labels.addAll(labels)

        maxTextSize = calculateMaxTextSize()

        invalidate()
    }

    private fun Paint.measureTextWidth(text: String?) = measureText(text)

    private fun Paint.measureTextHeight(text: String): Float {
        getTextBounds(text, 0, text.length, textBounds)
        return textBounds.height().toFloat()
    }

    private fun calculateMaxTextSize(): Float {
        var maxTextWidth = 0F
        var maxTextHeight = 0F

        for (label in labels) {
            if (maxTextHeight < labelPaint.measureTextHeight(label)) {
                maxTextHeight = labelPaint.measureTextHeight(label)
            }

            if (maxTextWidth < labelPaint.measureTextWidth(label)) {
                maxTextWidth = labelPaint.measureTextWidth(label)
            }
        }
        return maxTextHeight + maxTextWidth
    }

    private fun dpToPixels(dp: Float): Float {
        return dp * resources.displayMetrics.density
    }

    private fun spToPixels(sp: Float): Float {
        return sp * resources.displayMetrics.scaledDensity
    }
}