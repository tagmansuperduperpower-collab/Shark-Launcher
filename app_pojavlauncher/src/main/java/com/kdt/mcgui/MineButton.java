package com.kdt.mcgui;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.view.animation.*;
import androidx.core.content.res.ResourcesCompat;
import net.kdt.pojavlaunch.R;

public class MineButton extends androidx.appcompat.widget.AppCompatButton {

    public MineButton(Context ctx) {
        this(ctx, null);
    }

    public MineButton(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        init();
    }

    public void init() {
        setTypeface(ResourcesCompat.getFont(
            getContext(), R.font.noto_sans_bold));
        setBackground(ResourcesCompat.getDrawable(
            getResources(), 
            R.drawable.mine_button_background, null));
        setTextSize(TypedValue.COMPLEX_UNIT_PX, 
            getResources().getDimensionPixelSize(R.dimen._13ssp));
        setTextColor(Color.WHITE);
        setLetterSpacing(0.1f);

        setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Animation pressAnim = AnimationUtils.loadAnimation(
                        getContext(), R.anim.button_press);
                    startAnimation(pressAnim);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    Animation releaseAnim = new ScaleAnimation(
                        0.95f, 1.0f, 0.95f, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                    releaseAnim.setDuration(100);
                    startAnimation(releaseAnim);
                    break;
            }
            return false;
        });
    }
}
