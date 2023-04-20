package com.example.androidassignment2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceView;

import java.util.Calendar;

public class CustomClockView extends SurfaceView implements Runnable {
    private float mHourAngle;
    private float mMinuteAngle;
    private float mSecondAngle;
    private Paint mPaint = new Paint();

    // Variables for the clock dimensions
    private int mWidth;
    private int mHeight;
    private int mRadius;
    private int mCenterX;
    private int mCenterY;

    // Constants for the clock update interval and clock markers
    private static final int INTERVAL = 1000;

    public CustomClockView(Context context, AttributeSet attrs) {
        super(context, attrs);


        mWidth = getWidth();
        mHeight = getHeight();
        mRadius = Math.min(mWidth, mHeight) / 2 - 20;
        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;
        setZOrderOnTop(true); //to be able to change the background of the view
    }

    @Override
    public void run() {
        while (true) {
            // Update the rotation angles for each clock hand
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            mHourAngle = (hour * 30) + (minute / 2);
            mMinuteAngle = minute * 6;
            mSecondAngle = second * 6;

            // Redraw the clock
            postInvalidate();

            // Wait for one second
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
        drawClockMarkers(canvas);
        drawHands(canvas);
        postInvalidateDelayed(INTERVAL);
    }

    void drawClockMarkers(Canvas canvas) {
        // Draw the hour marks
        for (int i = 0; i < 12; i++) {
            int markLength = mRadius / 10;
            int markWidth = mRadius / 30;
            int markRadius = mRadius - markLength;

            float angle = i * 30;
            float startX = mCenterX + markRadius * (float) Math.sin(Math.toRadians(angle));
            float startY = mCenterY - markRadius * (float) Math.cos(Math.toRadians(angle));
            float endX = mCenterX + (markRadius + markLength) * (float) Math.sin(Math.toRadians(angle));
            float endY = mCenterY - (markRadius + markLength) * (float) Math.cos(Math.toRadians(angle));

            mPaint.setColor(Color.BLACK);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(markWidth);
            canvas.drawLine(startX, startY, endX, endY, mPaint);

            float numberX = mCenterX + (mRadius - mRadius / 4) * (float) Math.sin(Math.toRadians(angle));
            float numberY = mCenterY - (mRadius - mRadius / 4) * (float) Math.cos(Math.toRadians(angle));

            // Draw number using Paint object
            Paint textPaint = new Paint();
            textPaint.setColor(Color.BLACK);
            textPaint.setTextSize(mRadius / 8);
            textPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(String.valueOf(i), numberX, numberY + mRadius / 15, textPaint);
        }

        // Draw the minute marks
        for (int i = 0; i < 60; i++) {
            if (i % 5 != 0) { // Skip the hour marks
                int markLength = mRadius / 20;
                int markWidth = mRadius / 60;
                int markRadius = mRadius - markLength;

                float angle = i * 6;
                float startX = mCenterX + markRadius * (float) Math.sin(Math.toRadians(angle));
                float startY = mCenterY - markRadius * (float) Math.cos(Math.toRadians(angle));
                float endX = mCenterX + (markRadius + markLength) * (float) Math.sin(Math.toRadians(angle));
                float endY = mCenterY - (markRadius + markLength) * (float) Math.cos(Math.toRadians(angle));

                mPaint.setColor(Color.BLACK);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(markWidth);
                canvas.drawLine(startX, startY, endX, endY, mPaint);
            }
        }
    }



    void drawHands(Canvas canvas) {// Draw the hour hand
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mRadius / 20);
        canvas.save();
        canvas.rotate(mHourAngle, getWidth() / 2, getHeight() / 2);
        canvas.drawLine(mWidth / 2, mHeight / 2, mWidth / 2, mHeight / 2 - mRadius / 2, mPaint);
        canvas.restore();

        // Draw the minute hand
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mRadius / 30);
        canvas.save();
        canvas.rotate(mMinuteAngle, getWidth() / 2, getHeight() / 2);
        canvas.drawLine(mWidth / 2, mHeight / 2, mWidth / 2, mHeight / 2 - (int) (mRadius * 0.7), mPaint);
        canvas.restore();

        // Draw the second hand
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mRadius / 50);
        canvas.save();
        canvas.rotate(mSecondAngle, getWidth() / 2, getHeight() / 2);
        canvas.drawLine(mWidth / 2, mHeight / 2, mWidth / 2, mHeight / 2 - mRadius + (int) (mRadius * 0.15), mPaint);
        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
        mRadius = Math.min(mWidth, mHeight) / 2 - 20;
        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;

        postInvalidate();
    }

}

