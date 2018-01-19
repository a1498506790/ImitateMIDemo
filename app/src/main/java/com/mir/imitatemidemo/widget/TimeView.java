package com.mir.imitatemidemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author by lx
 * @github https://github.com/a1498506790
 * @data 2018/1/19
 * @desc 仿小米时钟UI
 */

public class TimeView extends View {

    private int mWidth;
    private int mCenterPoint;
    private int mRadius;
    private int mNormalColor = Color.parseColor("#88FFFFFF");

    private int mDistance;
    private Paint mTextPaint;
   /* 连接时间的外部圆弧*/
    private Paint mOutSidePaint;
    /*画圆环*/
    private Paint mCirclePaint;
    /*内部圆的距离*/
    private int mInnerDistance;

    /* 时针角度 */
    private float mHourDegree;
    /* 分针角度 */
    private float mMinuteDegree;
    /* 秒针角度 */
    private float mSecondDegree;

    /* 渐变矩阵，作用在SweepGradient */
    private Matrix mGradientMatrix;
    /* 梯度扫描渐变 */
    private SweepGradient mSweepGradient;

    private int mWeithColor = Color.parseColor("#ffffff");
    private int mGrayColor = Color.parseColor("#88ffffff");

    /* 时针路径 */
    private Path mHourHandPath;
    /* 分针路径 */
    private Path mMinuteHandPath;
    /* 秒针路径 */
    private Path mSecondHandPath;

    //秒针笔
    private Paint mSPaint;
    /* 分针笔*/
    private Paint mMpaint;
    private RectF mCircleRectF;

    /* 时针笔*/
    private Paint mHpaint;
    private float mHour;
    private float mMinute;
    private float mSecond;
    private Calendar mCalendar;

    public TimeView(Context context) {
        this(context, null);
    }

    public TimeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(30);
        mTextPaint.setColor(Color.parseColor("#FFFFFF"));

        mOutSidePaint = new Paint();
        mOutSidePaint.setAntiAlias(true);
        mOutSidePaint.setStyle(Paint.Style.STROKE);
        mOutSidePaint.setStrokeWidth(1f);
        mOutSidePaint.setColor(mNormalColor);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mNormalColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        DashPathEffect dashPathEffect = new DashPathEffect(new float[]{5, 5}, 0);
        mCirclePaint.setPathEffect(dashPathEffect);
        mCirclePaint.setStrokeWidth(30);

        mSPaint = new Paint();
        mSPaint.setAntiAlias(true);
        mSPaint.setColor(Color.parseColor("#FFFFFF"));
        mSPaint.setStyle(Paint.Style.FILL);

        mMpaint = new Paint();
        mMpaint.setColor(Color.parseColor("#ffffff"));
        mMpaint.setStyle(Paint.Style.FILL);
        mMpaint.setAntiAlias(true);

        mHpaint = new Paint();
        mHpaint.setColor(Color.parseColor("#88ffffff"));
        mHpaint.setStyle(Paint.Style.FILL);
        mHpaint.setAntiAlias(true);

        mSecondHandPath = new Path();
        mMinuteHandPath = new Path();
        mHourHandPath = new Path();
        mCircleRectF = new RectF();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        mWidth = Math.min(widthSize, heightSize);

        if (widthMode == MeasureSpec.UNSPECIFIED) { //未指定宽高
            mWidth = heightSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            mWidth = widthSize;
        }
        //圆心点的坐标
        mCenterPoint = mWidth / 2;
        //圆的半径
        mRadius = widthSize / 3;

        //圆环距离屏幕边缘的距离
        mDistance = mWidth / 6;

        mSweepGradient = new SweepGradient(mCenterPoint, mCenterPoint,
                new int[]{mGrayColor, mWeithColor}, new float[]{0.75f, 1});

        setMeasuredDimension(mWidth, mWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        getTime();
        drawText(canvas);
        drawOutSideArc(canvas);
        drawCircle(canvas);
        drawSPointer(canvas);
        drawHPointer(canvas);
        drawMPointer(canvas);
        invalidate();
    }

    private void drawHPointer(Canvas canvas) {
        canvas.save();
        canvas.rotate(mHourDegree, getWidth() / 2, getHeight() / 2);
        mHourHandPath.reset();
        float offset = mDistance;
        mHourHandPath.moveTo(getWidth() / 2 - 0.018f * mRadius, getHeight() / 2 - 0.03f * mRadius);
        mHourHandPath.lineTo(getWidth() / 2 - 0.009f * mRadius, offset + 0.48f * mRadius);
        mHourHandPath.quadTo(getWidth() / 2, offset + 0.46f * mRadius,
                getWidth() / 2 + 0.009f * mRadius, offset + 0.48f * mRadius);
        mHourHandPath.lineTo(getWidth() / 2 + 0.018f * mRadius, getHeight() / 2 - 0.03f * mRadius);
        mHourHandPath.close();
        mHpaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mHourHandPath, mHpaint);

        mCircleRectF.set(getWidth() / 2 - 0.03f * mRadius, getHeight() / 2 - 0.03f * mRadius,
                getWidth() / 2 + 0.03f * mRadius, getHeight() / 2 + 0.03f * mRadius);
        mHpaint.setStyle(Paint.Style.STROKE);
        mHpaint.setStrokeWidth(0.01f * mRadius);
        canvas.drawArc(mCircleRectF, 0, 360, false, mHpaint);
        canvas.restore();
    }

    private void drawMPointer(Canvas canvas) {
        canvas.save();
        canvas.rotate(mMinuteDegree, getWidth() / 2, getHeight() / 2);
        mMinuteHandPath.reset();
        float offset = mDistance - 30;
        mMinuteHandPath.moveTo(getWidth() / 2 - 0.01f * mRadius, getHeight() / 2 - 0.03f * mRadius);
        mMinuteHandPath.lineTo(getWidth() / 2 - 0.008f * mRadius, offset + 0.365f * mRadius);
        mMinuteHandPath.quadTo(getWidth() / 2, offset + 0.345f * mRadius,
                getWidth() / 2 + 0.008f * mRadius, offset + 0.365f * mRadius);
        mMinuteHandPath.lineTo(getWidth() / 2 + 0.01f * mRadius, getHeight() / 2 - 0.03f * mRadius);
        mMinuteHandPath.close();
        mMpaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mMinuteHandPath, mMpaint);

        mCircleRectF.set(getWidth() / 2 - 0.03f * mRadius, getHeight() / 2 - 0.03f * mRadius,
                getWidth() / 2 + 0.03f * mRadius, getHeight() / 2 + 0.03f * mRadius);
        mMpaint.setStyle(Paint.Style.STROKE);
        mMpaint.setStrokeWidth(0.02f * mRadius);
        canvas.drawArc(mCircleRectF, 0, 360, false, mMpaint);
        canvas.restore();
    }

    private void drawSPointer(Canvas canvas) {
        canvas.save();
        canvas.rotate(mSecondDegree, mCenterPoint, mCenterPoint);
        mSecondHandPath.reset();
        mSecondHandPath.moveTo(mCenterPoint, mDistance - 20 + 0.26f * mRadius);
        mSecondHandPath.lineTo(mCenterPoint - 0.05f * mRadius, mDistance- 20 + 0.34f * mRadius);
        mSecondHandPath.lineTo(mCenterPoint + 0.05f * mRadius, mDistance - 20 + 0.34f * mRadius);
        mSecondHandPath.close();
        //画三角形
//        canvas.drawPath(mSecondHandPath, mSPaint);
        //画圆形
        canvas.drawCircle(mCenterPoint, mDistance + 0.26f * mRadius , 10, mSPaint);
        canvas.restore();
    }

    private void drawCircle(Canvas canvas) {
        mGradientMatrix = new Matrix();
        mGradientMatrix.setRotate(mSecondDegree - 90 ,mCenterPoint,mCenterPoint);
        mSweepGradient.setLocalMatrix(mGradientMatrix);
        mCirclePaint.setShader(mSweepGradient);
        mInnerDistance = mRadius / 8;
        canvas.drawCircle(mCenterPoint, mCenterPoint, mRadius - mInnerDistance, mCirclePaint);
    }

    /**
     * 画 连接时间文字的 圆弧
     * @param canvas
     */
    private void drawOutSideArc(Canvas canvas) {
        canvas.drawArc(mDistance, mDistance,mDistance + mRadius * 2,mDistance + mRadius * 2, -85, 80, false, mOutSidePaint);
        canvas.drawArc(mDistance, mDistance,mDistance + mRadius * 2,mDistance + mRadius * 2, 5, 80, false, mOutSidePaint);
        canvas.drawArc(mDistance, mDistance,mDistance + mRadius * 2,mDistance + mRadius * 2, 95, 80, false, mOutSidePaint);
        canvas.drawArc(mDistance, mDistance,mDistance + mRadius * 2,mDistance + mRadius * 2, 185, 80, false, mOutSidePaint);
    }

    /**
     * 画 时间文字 12 3 6 9
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        canvas.drawText("12", mWidth / 2 - 20, mDistance + 10, mTextPaint);
        canvas.drawText("3", mWidth - mDistance - 10, mDistance + 10 + mRadius, mTextPaint);
        canvas.drawText("6", mWidth / 2 - 10, mWidth - mDistance + 10, mTextPaint);
        canvas.drawText("9", mDistance - 10, mDistance + mRadius + 10, mTextPaint);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String time = "当前时间 : " + format.format(new Date());
        Rect rect = new Rect();
        mTextPaint.getTextBounds(time, 0, time.length(), rect);
        int mTextWidth = rect.width();
        canvas.drawText(time, mWidth / 2 - mTextWidth / 2, 10 + rect.height(), mTextPaint);
    }

    public void getTime() {
        mCalendar = Calendar.getInstance();
        //毫秒
        float milliSecond = mCalendar.get(Calendar.MILLISECOND);
        //秒
        mSecond = mCalendar.get(Calendar.SECOND) + milliSecond / 1000;
        //分
        mMinute = mCalendar.get(Calendar.MINUTE) + mSecond / 60;
        //时
        mHour = mCalendar.get(Calendar.HOUR) + mMinute / 60;

        //求出三个指针的角度
        mSecondDegree = mSecond * 6;
        mMinuteDegree = 6 * mMinute;
        mHourDegree = 30 * mHour;
    }
}
