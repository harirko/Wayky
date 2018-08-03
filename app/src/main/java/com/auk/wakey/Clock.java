package com.auk.wakey;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Clock extends View {
    boolean changed = false;
    int c = Color.parseColor("#450045");
    Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    int m = 12;
    int h = 9;

    void init(){
        initPaint();
    }

    public Clock(Context context) {
        super(context);
        init();
    }

    public Clock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Clock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public Clock(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }

    private float getMinuteX(int l) {
        int c;
        if (m < 15) {
            c = m + 45;
        } else {
            c = m - 15;
        }
        double angle = Math.toRadians(c * 6);
        return (float) (0.6 * l * Math.cos(angle));
    }

    private float getHourX(int l) {
        double angle = Math.toRadians(((h * 60) + m) / 2 - 90);
        return (float) (0.4 * l * Math.cos(angle));
    }

    private float getHourY(int l) {
        double angle = Math.toRadians(((h * 60) + m) / 2 - 90);
        return (float) (0.4 * l * Math.sin(angle));
    }

    private float getMinuteY(int l) {
        int c;
        if (m < 15) {
            c = m + 45;
        } else {
            c = m - 15;
        }
        double angle = Math.toRadians(c * 6);
        return (float) (0.6 * l * Math.sin(angle));
    }

    Paint handPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint axlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint facePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint faceOuterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public static final int DOT_ENABLED = 0;
    public static final int DOT_DISABLED = 1;
    public static final int DOT_NEXT = 3;

    //indicatorColor
    //dotColor[3]
    //handsColor
    //ringColor
    //dotContainerColor


    int color = Color.parseColor("#c0c0c0c0");

    void initPaint() {
        handPaint.setStyle(Paint.Style.STROKE);
        handPaint.setStrokeWidth(12);
        handPaint.setColor(c);

        axlePaint.setStyle(Paint.Style.FILL);
        axlePaint.setColor(c);

        faceOuterPaint.setStyle(Paint.Style.STROKE);
        //faceOuterPaint.setStrokeWidth(12);
        faceOuterPaint.setColor(c);

        facePaint.setStyle(Paint.Style.STROKE);
        facePaint.setStrokeWidth(50);
        facePaint.setColor(Color.parseColor("#c0c0c0c0"));
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (changed) {
            invalidate(); //will trigger the onDraw
        }
        int center_w = (getMeasuredWidth() / 2);
        int center_h = (getMeasuredHeight() / 2);

        int len = (getMeasuredWidth() / 2) - 100;

        canvas.drawCircle(center_w, center_h, len, facePaint);
        canvas.drawCircle(center_w, center_h, len + 70, faceOuterPaint);

        canvas.drawLine(center_w, center_h, center_w + getMinuteX(len), center_h + getMinuteY(len), handPaint);
        canvas.drawLine(center_w, center_h, center_w + getHourX(len), center_h + getHourY(len), handPaint);

        canvas.drawCircle(center_w, center_h, 10, axlePaint);
    }
}
