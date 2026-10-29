package com.kdt.mcgui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import net.kdt.pojavlaunch.R;

/** A small animated compass used by the Shark Launcher home screen. */
public class SharkCompassView extends View {
    private final Paint ringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint tickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint needlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path needlePath = new Path();
    private ValueAnimator animator;
    private float heading;

    public SharkCompassView(Context context) {
        this(context, null);
    }

    public SharkCompassView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setContentDescription(context.getString(R.string.shark_compass_content_description));
        setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_YES);
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setStrokeWidth(dp(2));
        ringPaint.setColor(Color.parseColor("#47D7FF"));
        tickPaint.setStyle(Paint.Style.STROKE);
        tickPaint.setStrokeCap(Paint.Cap.ROUND);
        tickPaint.setColor(Color.parseColor("#8AE9FF"));
        needlePaint.setStyle(Paint.Style.FILL);
        needlePaint.setColor(Color.parseColor("#FF557A"));
        textPaint.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(dp(10));
        textPaint.setColor(Color.WHITE);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    private float dp(float value) {
        return value * getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;
        float radius = Math.min(getWidth(), getHeight()) / 2f - dp(5);

        ringPaint.setShadowLayer(dp(8), 0, 0, Color.parseColor("#3347D7FF"));
        canvas.drawCircle(cx, cy, radius, ringPaint);
        ringPaint.clearShadowLayer();

        for (int i = 0; i < 24; i++) {
            double radians = Math.toRadians(i * 15f - 90f);
            float outer = radius - dp(3);
            float inner = outer - (i % 6 == 0 ? dp(8) : dp(4));
            tickPaint.setStrokeWidth(i % 6 == 0 ? dp(2) : dp(1));
            canvas.drawLine(
                    cx + (float) Math.cos(radians) * inner,
                    cy + (float) Math.sin(radians) * inner,
                    cx + (float) Math.cos(radians) * outer,
                    cy + (float) Math.sin(radians) * outer,
                    tickPaint);
        }

        canvas.drawText("N", cx, cy - radius + dp(15), textPaint);
        canvas.drawText("E", cx + radius - dp(12), cy + dp(4), textPaint);
        canvas.drawText("S", cx, cy + radius - dp(6), textPaint);
        canvas.drawText("W", cx - radius + dp(12), cy + dp(4), textPaint);

        canvas.save();
        canvas.rotate(heading, cx, cy);
        needlePath.reset();
        needlePath.moveTo(cx, cy - radius + dp(13));
        needlePath.lineTo(cx - dp(5), cy + dp(4));
        needlePath.lineTo(cx, cy + dp(1));
        needlePath.lineTo(cx + dp(5), cy + dp(4));
        needlePath.close();
        canvas.drawPath(needlePath, needlePaint);
        canvas.restore();

        needlePaint.setColor(Color.parseColor("#D8F8FF"));
        canvas.drawCircle(cx, cy, dp(4), needlePaint);
        needlePaint.setColor(Color.parseColor("#FF557A"));
    }

    private void startAnimation() {
        if (animator != null) return;
        animator = ValueAnimator.ofFloat(0f, 360f);
        animator.setDuration(14000L);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(valueAnimator -> {
            heading = (float) valueAnimator.getAnimatedValue();
            postInvalidateOnAnimation();
        });
        animator.start();
    }

    private void stopAnimation() {
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimation();
        super.onDetachedFromWindow();
    }
}
