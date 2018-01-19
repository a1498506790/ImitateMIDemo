package com.mir.imitatemidemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author by lx
 * @github https://github.com/a1498506790
 * @data 2018/1/19
 * @desc 模仿小米指南针UI
 */

public class ChaosCompassView extends View {

    private int mWidth;
    private int mTextHeight;
    private int mCenterX;
    private int mCenterY;
    private int mOutSideRadius;
    private int mCircumRadius;
    private float mVal;

    private Paint mTextPaint;

    public ChaosCompassView(Context context) {
        this(context, null);
    }

    public ChaosCompassView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChaosCompassView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initPaint();
    }

    private void initPaint() {

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(25f);
        mTextPaint.setColor(Color.RED);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        mWidth = Math.min(widthSize, heightSize);

        if (widthMode == MeasureSpec.UNSPECIFIED) {
            mWidth = heightSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            mWidth = widthSize;
        }

        //为指南针上面的文字预留空间，定义为1/3边张
        mTextHeight = mWidth / 3;

        //设置圆心点坐标
        mCenterX = mWidth / 2;
        mCenterY = mWidth / 2 + mTextHeight;

        //外部圆的外径
        mOutSideRadius = mWidth * 3 / 8;
        //外接圆的半径
        mCircumRadius = mOutSideRadius * 4 / 5;

        setMeasuredDimension(mWidth, mWidth + mWidth / 3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        String text = null;
        if (mVal<=15||mVal>=345){
            text = "北";
        }else if (mVal>15&&mVal<=75){
            text= "东北";
        }else if (mVal>75&&mVal<=105){
            text= "东";
        }else if (mVal>105&&mVal<=165){
            text="东南";
        }else if (mVal>165&&mVal<=195){
            text = "南";
        }else if (mVal>195&&mVal<=255){
            text = "西南";
        }else if (mVal>255&&mVal<=285){
            text = "西";
        }else if (mVal>285&&mVal<345){
            text="西北";
        }
        Rect rect = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), rect);

        int mTextWidth = rect.width();
        //让文字水平居中显示
        canvas.drawText(text, mWidth / 2 - mTextWidth / 2, mTextHeight / 2 , mTextPaint);
    }

    public void setVal(float val) {
        mVal = val;
        invalidate();
    }
}
