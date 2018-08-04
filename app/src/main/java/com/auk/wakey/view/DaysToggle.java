package com.auk.wakey.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DaysToggle extends LinearLayout {
    private final static char[] days = {'M', 'T', 'W', 'T', 'F', 'S', 'S'};
    private final boolean[] daysToggle = new boolean[7];
    OnDaysChangedListener onDaysChangedListener;

    public DaysToggle(Context context) {
        super(context);
        init();
    }

    public DaysToggle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DaysToggle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DaysToggle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setOnDaysChangedListener(OnDaysChangedListener onDaysChangedListener) {
        this.onDaysChangedListener = onDaysChangedListener;
    }

    private void checker(TextView textView, boolean b) {

        if (b) {
            textView.setTextColor(Color.WHITE);
            textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else {
            textView.setTextColor(Color.GRAY);
            textView.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
        }
    }

    public boolean[] getRepeats() {
        return daysToggle;
    }

    private TextView createTextView(final int dayNum) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.leftMargin = layoutParams.rightMargin = 12;
        TextView textView = new TextView(getContext());
        //textView.getLayoutParams().width =
        textView.setText(String.valueOf(days[dayNum]));
        textView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        //add disabled color here
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setLayoutParams(layoutParams);
        checker(textView, daysToggle[dayNum]);
        textView.setOnClickListener(view -> {
            daysToggle[dayNum] = !daysToggle[dayNum];
            checker(textView, daysToggle[dayNum]);
            if (onDaysChangedListener != null) {
                onDaysChangedListener.onDaysChanged(daysToggle);
            }
        });
        return textView;
    }

    private void init() {
        setOrientation(HORIZONTAL);
        //create TextView and to Layout
        for (int i = 0; i < 7; i++) {
            TextView textView = createTextView(i);
            addView(textView);
        }
    }

    public interface OnDaysChangedListener {
        void onDaysChanged(boolean[] days);
    }
}
