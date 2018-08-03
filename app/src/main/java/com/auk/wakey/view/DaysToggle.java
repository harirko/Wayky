package com.auk.wakey.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DaysToggle extends LinearLayout {
    private final static char[] days = {'M', 'T', 'W', 'T', 'F', 'S', 'S'};
    private final static boolean[] daysToggle = new boolean[7];

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
        textView.setOnClickListener(view -> {
            daysToggle[dayNum] = !daysToggle[dayNum];
            if (daysToggle[dayNum]) {
                textView.setTextColor(Color.GRAY);
            } else {
                textView.setTextColor(Color.WHITE);
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
}
