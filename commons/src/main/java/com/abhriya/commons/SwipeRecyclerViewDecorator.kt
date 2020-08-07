package com.abhriya.commons

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color.DKGRAY
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.text.TextPaint
import android.util.Log
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewSwipeDecorator private constructor() {

    private lateinit var canvas: Canvas
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewHolder: RecyclerView.ViewHolder
    private var dX: Float = 0f
    private var dY: Float = 0f
    private var actionState: Int = 0
    private var isCurrentlyActive: Boolean = false

    private var swipeLeftBackgroundColor: Int = 0
    private var swipeLeftActionIconId: Int = 0
    private var swipeLeftActionIconTint: Int? = null

    private var swipeRightBackgroundColor: Int = 0
    private var swipeRightActionIconId: Int = 0
    private var swipeRightActionIconTint: Int? = null

    private var iconHorizontalMargin: Int = 0

    private var mSwipeLeftText: String? = null
    private var mSwipeLeftTextSize = 14f
    private var mSwipeLeftTextUnit = TypedValue.COMPLEX_UNIT_SP
    private var mSwipeLeftTextColor = DKGRAY
    private var mSwipeLeftTypeface = Typeface.SANS_SERIF

    private var mSwipeRightText: String? = null
    private var mSwipeRightTextSize = 14f
    private var mSwipeRightTextUnit = TypedValue.COMPLEX_UNIT_SP
    private var mSwipeRightTextColor = DKGRAY
    private var mSwipeRightTypeface = Typeface.SANS_SERIF

    init {
        swipeLeftBackgroundColor = 0
        swipeRightBackgroundColor = 0
        swipeLeftActionIconId = 0
        swipeRightActionIconId = 0
        swipeLeftActionIconTint = null
        swipeRightActionIconTint = null
    }

    /**
     * Create a @RecyclerViewSwipeDecorator
     * @param context A valid Context object for the RecyclerView
     * @param canvas The canvas which RecyclerView is drawing its children
     * @param recyclerView The RecyclerView to which ItemTouchHelper is attached to
     * @param viewHolder The ViewHolder which is being interacted by the User or it was interacted and simply animating to its original position
     * @param dX The amount of horizontal displacement caused by user's action
     * @param dY The amount of vertical displacement caused by user's action
     * @param actionState The type of interaction on the View. Is either ACTION_STATE_DRAG or ACTION_STATE_SWIPE.
     * @param isCurrentlyActive True if this view is currently being controlled by the user or false it is simply animating back to its original state
     *
     */
    @Deprecated("in RecyclerViewSwipeDecorator 1.2.2")
    constructor(
        context: Context,
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) : this(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive) {
    }

    /**
     * Create a @RecyclerViewSwipeDecorator
     * @param canvas The canvas which RecyclerView is drawing its children
     * @param recyclerView The RecyclerView to which ItemTouchHelper is attached to
     * @param viewHolder The ViewHolder which is being interacted by the User or it was interacted and simply animating to its original position
     * @param dX The amount of horizontal displacement caused by user's action
     * @param dY The amount of vertical displacement caused by user's action
     * @param actionState The type of interaction on the View. Is either ACTION_STATE_DRAG or ACTION_STATE_SWIPE.
     * @param isCurrentlyActive True if this view is currently being controlled by the user or false it is simply animating back to its original state
     */
    constructor(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) : this() {
        this.canvas = canvas
        this.recyclerView = recyclerView
        this.viewHolder = viewHolder
        this.dX = dX
        this.dY = dY
        this.actionState = actionState
        this.isCurrentlyActive = isCurrentlyActive
        this.iconHorizontalMargin = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            16f,
            recyclerView.context.resources.displayMetrics
        ).toInt()
    }

    /**
     * Set the background color for either (left/right) swipe directions
     * @param backgroundColor The resource id of the background color to be set
     */
    fun setBackgroundColor(backgroundColor: Int) {
        this.swipeLeftBackgroundColor = backgroundColor
        this.swipeRightBackgroundColor = backgroundColor
    }

    /**
     * Set the action icon for either (left/right) swipe directions
     * @param actionIconId The resource id of the icon to be set
     */
    fun setActionIconId(actionIconId: Int) {
        this.swipeLeftActionIconId = actionIconId
        this.swipeRightActionIconId = actionIconId
    }

    /**
     * Set the tint color for either (left/right) action icons
     * @param color a color in ARGB format (e.g. 0xFF0000FF for blue)
     */
    fun setActionIconTint(color: Int) {
        this.setSwipeLeftActionIconTint(color)
        this.setSwipeRightActionIconTint(color)
    }

    /**
     * Set the background color for left swipe direction
     * @param swipeLeftBackgroundColor The resource id of the background color to be set
     */
    fun setSwipeLeftBackgroundColor(swipeLeftBackgroundColor: Int) {
        this.swipeLeftBackgroundColor = swipeLeftBackgroundColor
    }

    /**
     * Set the action icon for left swipe direction
     * @param swipeLeftActionIconId The resource id of the icon to be set
     */
    fun setSwipeLeftActionIconId(swipeLeftActionIconId: Int) {
        this.swipeLeftActionIconId = swipeLeftActionIconId
    }

    /**
     * Set the tint color for action icon drawn while swiping left
     * @param color a color in ARGB format (e.g. 0xFF0000FF for blue)
     */
    fun setSwipeLeftActionIconTint(color: Int) {
        swipeLeftActionIconTint = color
    }

    /**
     * Set the background color for right swipe direction
     * @param swipeRightBackgroundColor The resource id of the background color to be set
     */
    fun setSwipeRightBackgroundColor(swipeRightBackgroundColor: Int) {
        this.swipeRightBackgroundColor = swipeRightBackgroundColor
    }

    /**
     * Set the action icon for right swipe direction
     * @param swipeRightActionIconId The resource id of the icon to be set
     */
    fun setSwipeRightActionIconId(swipeRightActionIconId: Int) {
        this.swipeRightActionIconId = swipeRightActionIconId
    }

    /**
     * Set the tint color for action icon drawn while swiping right
     * @param color a color in ARGB format (e.g. 0xFF0000FF for blue)
     */
    fun setSwipeRightActionIconTint(color: Int) {
        swipeRightActionIconTint = color
    }

    /**
     * Set the label shown when swiping right
     * @param label a String
     */
    fun setSwipeRightLabel(label: String) {
        mSwipeRightText = label
    }

    /**
     * Set the size of the text shown when swiping right
     * @param unit TypedValue (default is COMPLEX_UNIT_SP)
     * @param size the size value
     */
    fun setSwipeRightTextSize(unit: Int, size: Float) {
        mSwipeRightTextUnit = unit
        mSwipeRightTextSize = size
    }

    /**
     * Set the color of the text shown when swiping right
     * @param color the color to be set
     */
    fun setSwipeRightTextColor(color: Int) {
        mSwipeRightTextColor = color
    }

    /**
     * Set the Typeface of the text shown when swiping right
     * @param typeface the Typeface to be set
     */
    fun setSwipeRightTypeface(typeface: Typeface) {
        mSwipeRightTypeface = typeface
    }

    /**
     * Set the horizontal margin of the icon in DPs (default is 16dp)
     * @param iconHorizontalMargin the margin in pixels
     *
     */
    @Deprecated("in RecyclerViewSwipeDecorator 1.2, use {@link #setIconHorizontalMargin(int, int)} instead.")
    fun setIconHorizontalMargin(iconHorizontalMargin: Int) {
        setIconHorizontalMargin(TypedValue.COMPLEX_UNIT_DIP, iconHorizontalMargin)
    }

    /**
     * Set the horizontal margin of the icon in the given unit (default is 16dp)
     * @param unit TypedValue
     * @param iconHorizontalMargin the margin in the given unit
     */
    fun setIconHorizontalMargin(unit: Int, iconHorizontalMargin: Int) {
        this.iconHorizontalMargin = TypedValue.applyDimension(
            unit,
            iconHorizontalMargin.toFloat(),
            recyclerView.context.resources.displayMetrics
        ).toInt()
    }

    /**
     * Set the label shown when swiping left
     * @param label a String
     */
    fun setSwipeLeftLabel(label: String) {
        mSwipeLeftText = label
    }

    /**
     * Set the size of the text shown when swiping left
     * @param unit TypedValue (default is COMPLEX_UNIT_SP)
     * @param size the size value
     */
    fun setSwipeLeftTextSize(unit: Int, size: Float) {
        mSwipeLeftTextUnit = unit
        mSwipeLeftTextSize = size
    }

    /**
     * Set the color of the text shown when swiping left
     * @param color the color to be set
     */
    fun setSwipeLeftTextColor(color: Int) {
        mSwipeLeftTextColor = color
    }

    /**
     * Set the Typeface of the text shown when swiping left
     * @param typeface the Typeface to be set
     */
    fun setSwipeLeftTypeface(typeface: Typeface) {
        mSwipeLeftTypeface = typeface
    }

    /**
     * Decorate the RecyclerView item with the chosen backgrounds and icons
     */
    fun decorate() {
        try {
            if (actionState != ItemTouchHelper.ACTION_STATE_SWIPE) return

            if (dX > 0) {
                // Swiping Right
                if (swipeRightBackgroundColor != 0) {
                    val background = ColorDrawable(swipeRightBackgroundColor)
                    background.setBounds(
                        viewHolder.itemView.left,
                        viewHolder.itemView.top,
                        viewHolder.itemView.left + dX.toInt(),
                        viewHolder.itemView.bottom
                    )
                    background.draw(canvas)
                }

                var iconSize = 0
                if (swipeRightActionIconId != 0 && dX > iconHorizontalMargin) {
                    val icon =
                        ContextCompat.getDrawable(recyclerView.context, swipeRightActionIconId)
                    if (icon != null) {
                        iconSize = icon.getIntrinsicHeight()
                        val halfIcon = iconSize / 2
                        val top =
                            viewHolder.itemView.top + ((viewHolder.itemView.bottom - viewHolder.itemView.top) / 2 - halfIcon)
                        icon.setBounds(
                            viewHolder.itemView.left + iconHorizontalMargin,
                            top,
                            viewHolder.itemView.left + iconHorizontalMargin + icon.intrinsicWidth,
                            top + icon.getIntrinsicHeight()
                        )
                        if (swipeRightActionIconTint != null)
                            icon.setColorFilter(swipeRightActionIconTint!!, PorterDuff.Mode.SRC_IN)
                        icon.draw(canvas)
                    }
                }

                if (mSwipeRightText != null && mSwipeRightText!!.isNotEmpty() && dX > iconHorizontalMargin + iconSize) {
                    val textPaint = TextPaint()
                    textPaint.isAntiAlias = true
                    textPaint.textSize =
                        TypedValue.applyDimension(
                            mSwipeRightTextUnit,
                            mSwipeRightTextSize,
                            recyclerView.context.resources.displayMetrics
                        )
                    textPaint.color = mSwipeRightTextColor
                    textPaint.typeface = mSwipeRightTypeface

                    val textTop =
                        (viewHolder.itemView.top.toDouble() + (viewHolder.itemView.bottom - viewHolder.itemView.top) / 2.0 + (textPaint.textSize / 2).toDouble()).toInt()
                    canvas.drawText(
                        mSwipeRightText!!,
                        (viewHolder.itemView.left + iconHorizontalMargin + iconSize + if (iconSize > 0) iconHorizontalMargin / 2 else 0).toFloat(),
                        textTop.toFloat(),
                        textPaint
                    )
                }

            } else if (dX < 0) {
                // Swiping Left
                if (swipeLeftBackgroundColor != 0) {
                    val background = ColorDrawable(swipeLeftBackgroundColor)
                    background.setBounds(
                        viewHolder.itemView.right + dX.toInt(),
                        viewHolder.itemView.top,
                        viewHolder.itemView.right,
                        viewHolder.itemView.bottom
                    )
                    background.draw(canvas)
                }

                var iconSize = 0
                var imgLeft = viewHolder.itemView.right
                if (swipeLeftActionIconId != 0 && dX < -iconHorizontalMargin) {
                    val icon =
                        ContextCompat.getDrawable(recyclerView.context, swipeLeftActionIconId)
                    if (icon != null) {
                        iconSize = icon!!.getIntrinsicHeight()
                        val halfIcon = iconSize / 2
                        val top =
                            viewHolder.itemView.top + ((viewHolder.itemView.bottom - viewHolder.itemView.top) / 2 - halfIcon)
                        imgLeft = viewHolder.itemView.right - iconHorizontalMargin - halfIcon * 2
                        icon!!.setBounds(
                            imgLeft,
                            top,
                            viewHolder.itemView.right - iconHorizontalMargin,
                            top + icon!!.getIntrinsicHeight()
                        )
                        if (swipeLeftActionIconTint != null)
                            icon!!.setColorFilter(swipeLeftActionIconTint!!, PorterDuff.Mode.SRC_IN)
                        icon!!.draw(canvas)
                    }
                }

                if (mSwipeLeftText != null && mSwipeLeftText!!.isNotEmpty() && dX < -iconHorizontalMargin - iconSize) {
                    val textPaint = TextPaint()
                    textPaint.isAntiAlias = true
                    textPaint.textSize =
                        TypedValue.applyDimension(
                            mSwipeLeftTextUnit,
                            mSwipeLeftTextSize,
                            recyclerView.context.resources.displayMetrics
                        )
                    textPaint.color = mSwipeLeftTextColor
                    textPaint.typeface = mSwipeLeftTypeface

                    val width = textPaint.measureText(mSwipeLeftText)
                    val textTop =
                        (viewHolder.itemView.top.toDouble() + (viewHolder.itemView.bottom - viewHolder.itemView.top) / 2.0 + (textPaint.textSize / 2).toDouble()).toInt()
                    canvas.drawText(
                        mSwipeLeftText!!,
                        imgLeft.toFloat() - width - (if (imgLeft == viewHolder.itemView.right) iconHorizontalMargin else iconHorizontalMargin / 2).toFloat(),
                        textTop.toFloat(),
                        textPaint
                    )
                }
            }
        } catch (e: Exception) {
//            Log.e(this.javaClass.name, e.message)
        }

    }

    /**
     * A Builder for the RecyclerViewSwipeDecorator class
     */
    class Builder
    /**
     * Create a builder for a RecyclerViewsSwipeDecorator
     * @param canvas The canvas which RecyclerView is drawing its children
     * @param recyclerView The RecyclerView to which ItemTouchHelper is attached to
     * @param viewHolder The ViewHolder which is being interacted by the User or it was interacted and simply animating to its original position
     * @param dX The amount of horizontal displacement caused by user's action
     * @param dY The amount of vertical displacement caused by user's action
     * @param actionState The type of interaction on the View. Is either ACTION_STATE_DRAG or ACTION_STATE_SWIPE.
     * @param isCurrentlyActive True if this view is currently being controlled by the user or false it is simply animating back to its original state
     */
        (
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        private val mDecorator: RecyclerViewSwipeDecorator

        /**
         * Create a builder for a RecyclerViewsSwipeDecorator
         * @param context A valid Context object for the RecyclerView
         * @param canvas The canvas which RecyclerView is drawing its children
         * @param recyclerView The RecyclerView to which ItemTouchHelper is attached to
         * @param viewHolder The ViewHolder which is being interacted by the User or it was interacted and simply animating to its original position
         * @param dX The amount of horizontal displacement caused by user's action
         * @param dY The amount of vertical displacement caused by user's action
         * @param actionState The type of interaction on the View. Is either ACTION_STATE_DRAG or ACTION_STATE_SWIPE.
         * @param isCurrentlyActive True if this view is currently being controlled by the user or false it is simply animating back to its original state
         *
         */
        @Deprecated("in RecyclerViewSwipeDecorator 1.2.2")
        constructor(
            context: Context,
            canvas: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) : this(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive) {
        }

        init {
            mDecorator = RecyclerViewSwipeDecorator(
                canvas,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
        }

        /**
         * Add a background color to both swiping directions
         * @param color A single color value in the form 0xAARRGGBB
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun addBackgroundColor(color: Int): Builder {
            mDecorator.setBackgroundColor(color)
            return this
        }

        /**
         * Add an action icon to both swiping directions
         * @param drawableId The resource id of the icon to be set
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun addActionIcon(drawableId: Int): Builder {
            mDecorator.setActionIconId(drawableId)
            return this
        }

        /**
         * Set the tint color for either (left/right) action icons
         * @param color a color in ARGB format (e.g. 0xFF0000FF for blue)
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun setActionIconTint(color: Int): Builder {
            mDecorator.setActionIconTint(color)
            return this
        }

        /**
         * Add a background color while swiping right
         * @param color A single color value in the form 0xAARRGGBB
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun addSwipeRightBackgroundColor(color: Int): Builder {
            mDecorator.setSwipeRightBackgroundColor(color)
            return this
        }

        /**
         * Add an action icon while swiping right
         * @param drawableId The resource id of the icon to be set
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun addSwipeRightActionIcon(drawableId: Int): Builder {
            mDecorator.setSwipeRightActionIconId(drawableId)
            return this
        }

        /**
         * Set the tint color for action icon shown while swiping right
         * @param color a color in ARGB format (e.g. 0xFF0000FF for blue)
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun setSwipeRightActionIconTint(color: Int): Builder {
            mDecorator.setSwipeRightActionIconTint(color)
            return this
        }

        /**
         * Add a label to be shown while swiping right
         * @param label The string to be shown as label
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun addSwipeRightLabel(label: String): Builder {
            mDecorator.setSwipeRightLabel(label)
            return this
        }

        /**
         * Set the color of the label to be shown while swiping right
         * @param color the color to be set
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun setSwipeRightLabelColor(color: Int): Builder {
            mDecorator.setSwipeRightTextColor(color)
            return this
        }

        /**
         * Set the size of the label to be shown while swiping right
         * @param unit the unit to convert from
         * @param size the size to be set
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun setSwipeRightLabelTextSize(unit: Int, size: Float): Builder {
            mDecorator.setSwipeRightTextSize(unit, size)
            return this
        }

        /**
         * Set the Typeface of the label to be shown while swiping right
         * @param typeface the Typeface to be set
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun setSwipeRightLabelTypeface(typeface: Typeface): Builder {
            mDecorator.setSwipeRightTypeface(typeface)
            return this
        }

        /**
         * Adds a background color while swiping left
         * @param color A single color value in the form 0xAARRGGBB
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun addSwipeLeftBackgroundColor(color: Int): Builder {
            mDecorator.setSwipeLeftBackgroundColor(color)
            return this
        }

        /**
         * Add an action icon while swiping left
         * @param drawableId The resource id of the icon to be set
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun addSwipeLeftActionIcon(drawableId: Int): Builder {
            mDecorator.setSwipeLeftActionIconId(drawableId)
            return this
        }

        /**
         * Set the tint color for action icon shown while swiping left
         * @param color a color in ARGB format (e.g. 0xFF0000FF for blue)
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun setSwipeLeftActionIconTint(color: Int): Builder {
            mDecorator.setSwipeLeftActionIconTint(color)
            return this
        }

        /**
         * Add a label to be shown while swiping left
         * @param label The string to be shown as label
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun addSwipeLeftLabel(label: String): Builder {
            mDecorator.setSwipeLeftLabel(label)
            return this
        }

        /**
         * Set the color of the label to be shown while swiping left
         * @param color the color to be set
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun setSwipeLeftLabelColor(color: Int): Builder {
            mDecorator.setSwipeLeftTextColor(color)
            return this
        }

        /**
         * Set the size of the label to be shown while swiping left
         * @param unit the unit to convert from
         * @param size the size to be set
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun setSwipeLeftLabelTextSize(unit: Int, size: Float): Builder {
            mDecorator.setSwipeLeftTextSize(unit, size)
            return this
        }

        /**
         * Set the Typeface of the label to be shown while swiping left
         * @param typeface the Typeface to be set
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun setSwipeLeftLabelTypeface(typeface: Typeface): Builder {
            mDecorator.setSwipeLeftTypeface(typeface)
            return this
        }

        /**
         * Set the horizontal margin of the icon in DPs (default is 16dp)
         * @param pixels margin in pixels
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         *
         */
        @Deprecated("in RecyclerViewSwipeDecorator 1.2, use {@link #setIconHorizontalMargin(int, int)} instead.")
        fun setIconHorizontalMargin(pixels: Int): Builder {
            mDecorator.setIconHorizontalMargin(pixels)
            return this
        }

        /**
         * Set the horizontal margin of the icon in the given unit (default is 16dp)
         * @param unit TypedValue
         * @param iconHorizontalMargin the margin in the given unit
         *
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun setIconHorizontalMargin(unit: Int, iconHorizontalMargin: Int): Builder {
            mDecorator.setIconHorizontalMargin(unit, iconHorizontalMargin)
            return this
        }

        /**
         * Create a RecyclerViewSwipeDecorator
         * @return The created @RecyclerViewSwipeDecorator
         */
        fun create(): RecyclerViewSwipeDecorator {
            return mDecorator
        }
    }
}