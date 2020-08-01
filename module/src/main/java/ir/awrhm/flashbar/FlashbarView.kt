package ir.awrhm.flashbar


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.M
import android.text.Spanned
import android.text.TextUtils
import android.util.TypedValue
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import ir.awrhm.flashbar.anim.FlashAnimIconBuilder
import ir.awrhm.flashbar.util.convertDpToPx
import ir.awrhm.flashbar.view.ShadowView
import ir.awrhm.flashbar.Flashbar

import kotlinx.android.synthetic.main.flash_bar_view.view.*

/**
 * The actual Flashbar withView representation that can consist of the title, message, button, icon, etc.
 * Its size is adaptive and depends solely on the amount of content present in it. It always matches
 * the width of the screen.
 *
 * It can either be present at the top or at the bottom of the screen. It will always consume touch
 * events and respond as necessary.
 */
internal class FlashbarView(context: Context) : ConstraintLayout(context) {

    private val TOP_COMPENSATION_MARGIN = resources.getDimension(R.dimen.fb_top_compensation_margin).toInt()
    private val BOTTOM_COMPENSATION_MARGIN = resources.getDimension(R.dimen.fb_bottom_compensation_margin).toInt()

    private lateinit var parentFlashbarContainer: FlashbarContainerView
    private lateinit var gravity: Flashbar.Gravity

    private var isMarginCompensationApplied: Boolean = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (!isMarginCompensationApplied) {
            isMarginCompensationApplied = true

            val params = layoutParams as MarginLayoutParams
            when (gravity) {
                Flashbar.Gravity.TOP -> params.topMargin = -TOP_COMPENSATION_MARGIN
                Flashbar.Gravity.BOTTOM -> params.bottomMargin = -BOTTOM_COMPENSATION_MARGIN
            }
            requestLayout()
        }
    }

    internal fun init(
        gravity: Flashbar.Gravity,
        castShadow: Boolean,
        shadowStrength: Int) {
        this.gravity = gravity

        // If the bar appears with the bottom, then the shadow needs to added to the top of it,
        // Thus, before the inflation of the bar
        if (castShadow && gravity == Flashbar.Gravity.BOTTOM) {
            castShadow(ShadowView.ShadowType.TOP, shadowStrength)
        }

        inflate(context, R.layout.flash_bar_view, this)

        // If the bar appears with the top, then the shadow needs to added to the bottom of it,
        // Thus, after the inflation of the bar
        if (castShadow && gravity == Flashbar.Gravity.TOP) {
            castShadow(ShadowView.ShadowType.BOTTOM, shadowStrength)
        }
    }

    /*internal fun adjustWitPositionAndOrientation(activity: Activity,
                                                 gravity: Gravity) {
        val flashbarViewLp = RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        val statusBarHeight = activity.getStatusBarHeightInPx()

        val flashbarViewContentLp = fbContent.layoutParams as LayoutParams

        when (gravity) {
            TOP -> {
                flashbarViewContentLp.topMargin = statusBarHeight.plus(TOP_COMPENSATION_MARGIN / 2)
                flashbarViewLp.addRule(ALIGN_PARENT_TOP)
            }
            BOTTOM -> {
                flashbarViewContentLp.bottomMargin = BOTTOM_COMPENSATION_MARGIN
                flashbarViewLp.addRule(ALIGN_PARENT_BOTTOM)
            }
        }
        fbContent.layoutParams = flashbarViewContentLp
        layoutParams = flashbarViewLp
    }*/

    internal fun addParent(flashbarContainerView: FlashbarContainerView) {
        this.parentFlashbarContainer = flashbarContainerView
    }

    internal fun setBarBackgroundDrawable(drawable: Drawable?) {
        drawable?.let {
            this.fbRoot.setBackgroundDrawable(drawable)
        }
    }

    fun setBarBackgroundColor(@ColorInt color: Int?) {
        if (color == null) return
        this.fbRoot.setBackgroundColor(color)
    }

    internal fun setBarTapListener(listener: Flashbar.OnTapListener?) {
        if (listener == null) return

        fbRoot.setOnClickListener {
            listener.onTap(parentFlashbarContainer.parentFlashbar)
        }
    }

    internal fun setTitle(title: String?) {
        if (TextUtils.isEmpty(title)) return

        this.fbTitle.text = title
        this.fbTitle.visibility = VISIBLE
    }

    internal fun setTitleSpanned(title: Spanned?) {
        if (title == null) return

        this.fbTitle.text = title
        this.fbTitle.visibility = VISIBLE
    }

    internal fun setTitleTypeface(typeface: Typeface?) {
        if (typeface == null) return
        fbTitle.typeface = typeface
    }

    internal fun setTitleSizeInPx(size: Float?) {
        if (size == null) return
        fbTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    internal fun setTitleSizeInSp(size: Float?) {
        if (size == null) return
        fbTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }

    internal fun setTitleColor(color: Int?) {
        if (color == null) return
        fbTitle.setTextColor(color)
    }

    internal fun setTitleAppearance(titleAppearance: Int?) {
        if (titleAppearance == null) return

        if (SDK_INT >= M) {
            this.fbTitle.setTextAppearance(titleAppearance)
        } else {
            this.fbTitle.setTextAppearance(fbTitle.context, titleAppearance)
        }
    }

    internal fun setMessage(message: String?) {
        if (TextUtils.isEmpty(message)) return

        this.fbMessage.text = message
        this.fbMessage.visibility = VISIBLE
    }

    internal fun setMessageSpanned(message: Spanned?) {
        if (message == null) return

        this.fbMessage.text = message
        this.fbMessage.visibility = VISIBLE
    }

    internal fun setMessageTypeface(typeface: Typeface?) {
        if (typeface == null) return
        this.fbMessage.typeface = typeface
    }

    internal fun setMessageSizeInPx(size: Float?) {
        if (size == null) return
        this.fbMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    internal fun setMessageSizeInSp(size: Float?) {
        if (size == null) return
        this.fbMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }

    internal fun setMessageColor(color: Int?) {
        if (color == null) return
        this.fbMessage.setTextColor(color)
    }

    internal fun setMessageAppearance(messageAppearance: Int?) {
        if (messageAppearance == null) return

        if (SDK_INT >= M) {
            this.fbMessage.setTextAppearance(messageAppearance)
        } else {
            this.fbMessage.setTextAppearance(fbMessage.context, messageAppearance)
        }
    }

    internal fun setPrimaryActionText(text: String?) {
        if (TextUtils.isEmpty(text)) return

        this.fbPrimaryAction.text = text
        this.fbPrimaryAction.visibility = VISIBLE
    }

    internal fun setPrimaryActionTextSpanned(text: Spanned?) {
        if (text == null) return

        this.fbPrimaryAction.text = text
        this.fbPrimaryAction.visibility = VISIBLE
    }

    internal fun setPrimaryActionTextTypeface(typeface: Typeface?) {
        if (typeface == null) return
        this.fbPrimaryAction.typeface = typeface
    }

    internal fun setPrimaryActionTextSizeInPx(size: Float?) {
        if (size == null) return
        this.fbPrimaryAction.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    internal fun setPrimaryActionTextSizeInSp(size: Float?) {
        if (size == null) return
        this.fbPrimaryAction.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }

    internal fun setPrimaryActionTextColor(color: Int?) {
        if (color == null) return
        this.fbPrimaryAction.setTextColor(color)
    }

    internal fun setPrimaryActionTextAppearance(messageAppearance: Int?) {
        if (messageAppearance == null) return

        if (SDK_INT >= M) {
            this.fbPrimaryAction.setTextAppearance(messageAppearance)
        } else {
            this.fbPrimaryAction.setTextAppearance(fbPrimaryAction.context, messageAppearance)
        }
    }

    internal fun setPrimaryActionTapListener(listener: Flashbar.OnActionTapListener?) {
        if (listener == null) return

        this.fbPrimaryAction.setOnClickListener {
            listener.onActionTapped(parentFlashbarContainer.parentFlashbar)
        }
    }

    internal fun setPositiveActionText(text: String?) {
        if (TextUtils.isEmpty(text)) return

        this.fbSecondaryActionContainer.visibility = VISIBLE
        this.fbPositiveAction.text = text
        this.fbPositiveAction.visibility = VISIBLE
    }

    internal fun setPositiveActionTextSpanned(text: Spanned?) {
        if (text == null) return

        this.fbSecondaryActionContainer.visibility = VISIBLE
        this.fbPositiveAction.text = text
        this.fbPositiveAction.visibility = VISIBLE
    }

    internal fun setPositiveActionTextTypeface(typeface: Typeface?) {
        if (typeface == null) return
        this.fbPositiveAction.typeface = typeface
    }

    internal fun setPositiveActionTextSizeInPx(size: Float?) {
        if (size == null) return
        this.fbPositiveAction.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    internal fun setPositiveActionTextSizeInSp(size: Float?) {
        if (size == null) return
        this.fbPositiveAction.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }

    internal fun setPositiveActionTextColor(color: Int?) {
        if (color == null) return
        this.fbPositiveAction.setTextColor(color)
    }

    internal fun setPositiveActionTextAppearance(messageAppearance: Int?) {
        if (messageAppearance == null) return

        if (SDK_INT >= M) {
            this.fbPositiveAction.setTextAppearance(messageAppearance)
        } else {
            this.fbPositiveAction.setTextAppearance(fbPrimaryAction.context, messageAppearance)
        }
    }

    internal fun setPositiveActionTapListener(listener: Flashbar.OnActionTapListener?) {
        if (listener == null) return

        this.fbPositiveAction.setOnClickListener {
            listener.onActionTapped(parentFlashbarContainer.parentFlashbar)
        }
    }

    internal fun setNegativeActionText(text: String?) {
        if (TextUtils.isEmpty(text)) return

        this.fbSecondaryActionContainer.visibility = VISIBLE
        this.fbNegativeAction.text = text
        this.fbNegativeAction.visibility = VISIBLE
    }

    internal fun setNegativeActionTextSpanned(text: Spanned?) {
        if (text == null) return

        this.fbSecondaryActionContainer.visibility = VISIBLE
        this.fbNegativeAction.text = text
        this.fbNegativeAction.visibility = VISIBLE
    }

    internal fun setNegativeActionTextTypeface(typeface: Typeface?) {
        if (typeface == null) return
        this.fbNegativeAction.typeface = typeface
    }

    internal fun setNegativeActionTextSizeInPx(size: Float?) {
        if (size == null) return
        this.fbNegativeAction.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    internal fun setNegativeActionTextSizeInSp(size: Float?) {
        if (size == null) return
        this.fbNegativeAction.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }

    internal fun setNegativeActionTextColor(color: Int?) {
        if (color == null) return
        this.fbNegativeAction.setTextColor(color)
    }

    internal fun setNegativeActionTextAppearance(messageAppearance: Int?) {
        if (messageAppearance == null) return

        if (SDK_INT >= M) {
            this.fbNegativeAction.setTextAppearance(messageAppearance)
        } else {
            this.fbNegativeAction.setTextAppearance(fbPrimaryAction.context, messageAppearance)
        }
    }

    internal fun setNegativeActionTapListener(listener: Flashbar.OnActionTapListener?) {
        if (listener == null) return

        this.fbNegativeAction.setOnClickListener {
            listener.onActionTapped(parentFlashbarContainer.parentFlashbar)
        }
    }

    internal fun showIcon(showIcon: Boolean) {
        this.fbIcon.visibility = if (showIcon) VISIBLE else GONE
    }

    internal fun showIconScale(scale: Float) {
        this.fbIcon.scaleX = scale
        this.fbIcon.scaleY = scale
//        this.fbIcon.scaleType = scaleType
    }

    internal fun setIconDrawable(icon: Drawable?) {
        icon?.let {
            this.fbIcon.setBackgroundDrawable(icon)
        }
    }

    internal fun setIconBitmap(bitmap: Bitmap?) {
        if (bitmap == null) return
        this.fbIcon.setImageBitmap(bitmap)
    }

/*    internal fun setIconColorFilter(colorFilter: Int?, filterMode: PorterDuff.Mode?) {
        if (colorFilter == null) return
        if (filterMode == null) {
            this.fbIcon.setColorFilter(colorFilter)
        } else {
            this.fbIcon.setColorFilter(colorFilter, filterMode)
        }
    }*/

    internal fun startIconAnimation(animator: FlashAnimIconBuilder?) {
        animator?.withView(fbIcon)?.build()?.start()
    }

    internal fun stopIconAnimation() {
        fbIcon.clearAnimation()
    }

    internal fun enableSwipeToDismiss(enable: Boolean, callbacks: SwipeDismissTouchListener.DismissCallbacks) {
        if (enable) {
            fbRoot.setOnTouchListener(SwipeDismissTouchListener(this, callbacks))
        }
    }

    private fun castShadow(shadowType: ShadowView.ShadowType, strength: Int) {
        val params = RelativeLayout.LayoutParams(MATCH_PARENT, context.convertDpToPx(strength))
        val shadow = ShadowView(context)
        shadow.applyShadow(shadowType)
        addView(shadow, params)
    }
}