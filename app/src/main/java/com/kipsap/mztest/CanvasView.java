package com.kipsap.mztest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CanvasView extends View
{
    private MazeActivity parent;

    public int m_iScreenWidth;
    public int m_iScreenHeight;
    public float m_fXmiddle;
    public float m_fYmiddle;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    Context context;
    private Paint   mGreenMazeLinePaint,
                    mBlackPathPaint;
    private float mX, mY;
    private static final float TOLERANCE = 5;
    private static float STEP_SIZE = 100f;

    private float fZoomPercentage = 100.f;
    private boolean bForwardInTime = true;

    private int iMazeSegments = 6; // = height
    private int iMazeDepth = 4; // = width
    private MazePiece theMazePiece;

    public void nextStep()
    {
        String str;
        if (bForwardInTime)
        {
            fZoomPercentage += STEP_SIZE;
            STEP_SIZE *= 2;
        }
        else if (STEP_SIZE > 50)
        {
            STEP_SIZE /= 2;
            fZoomPercentage -= STEP_SIZE;
        }
        String f = String.format("%2.2f", fZoomPercentage);
        str = "zoom: " + f;
        parent.updateZoomLevel(str);
        theMazePiece.updateAllCircleProps(fZoomPercentage / 100.f);
        invalidate();
    }

    public void setTimeForward(boolean forward)
    {
        bForwardInTime = forward;
    }

    public CanvasView(Context c, AttributeSet attrs)
    {
        super(c, attrs);
        context = c;
        if (!isInEditMode())
            parent = (MazeActivity) getContext();

        // we set a new Path
        mPath = new Path();

        // and we set a new Paint with the desired attributes
        mGreenMazeLinePaint = new Paint();
        mGreenMazeLinePaint.setAntiAlias(true);
        mGreenMazeLinePaint.setColor(Color.GREEN);
        mGreenMazeLinePaint.setStyle(Paint.Style.STROKE);
        mGreenMazeLinePaint.setStrokeJoin(Paint.Join.ROUND);

        mBlackPathPaint = new Paint();
        mBlackPathPaint.setAntiAlias(true);
        mBlackPathPaint.setColor(Color.BLACK);
        mBlackPathPaint.setStyle(Paint.Style.STROKE);
        mBlackPathPaint.setStrokeJoin(Paint.Join.ROUND);
        mBlackPathPaint.setStrokeWidth(10f);

        initializeMazePiece();

    }

    private void initializeMazePiece()
    {
        theMazePiece = new MazePiece(iMazeDepth, iMazeSegments, 7, 0, "6bac9aa259cd5516361c9a67"); // goed is: 6bac9aa259cd5516361c9a67

    }

    // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        m_iScreenWidth = w;
        m_iScreenHeight = h;
        m_fXmiddle = (float) m_iScreenWidth / 2.f;
        m_fYmiddle = (float) m_iScreenHeight / 2.f;
        // your Canvas will draw onto the defined Bitmap
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        for (int i = 0; i < theMazePiece.getCircleVector().size(); i++)
            if (theMazePiece.getCircleVector().elementAt(i).isCenteredMazeCircle()) // maze circle
                theMazePiece.getCircleVector().elementAt(i).setCenter(m_fXmiddle, m_fYmiddle, m_fXmiddle, m_fYmiddle, (fZoomPercentage / 100.f));
    }

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        for (int i = 0; i < theMazePiece.getCircleVector().size(); i++)
        {
            Circle c = theMazePiece.getCircleVector().elementAt(i);
            mGreenMazeLinePaint.setStrokeWidth(c.getStrokeWidth());
            for (int segment = 0; segment < iMazeSegments; segment++)
            {
                boolean bHasOuterWallSegment = theMazePiece.hasOuterWallSegmentAt(i, segment);
                boolean bHasLowerWallSegment = theMazePiece.hasLowerWallSegmentAt(i, segment);
                if (bHasOuterWallSegment)
                    canvas.drawArc(c.getRect(), segment * (360.f / (float) iMazeSegments), (360.f / (float) iMazeSegments), false, mGreenMazeLinePaint);
                if (bHasLowerWallSegment)
                {
                    float fsegmentlength = 1.f / (float) iMazeSegments;
                    float fraction = (float)segment * fsegmentlength;
                    double angle = (2.f * Math.PI) - (((fraction * 2.f * Math.PI)) - 0.5f * Math.PI) - (fsegmentlength * 2.f * Math.PI);
                    double delta_x_start = 0.5 * c.getRadius() * Math.sin(angle);
                    float fx_start = c.getX() + (float)delta_x_start;
                    double delta_y_start = 0.5 * c.getRadius() * Math.cos(angle);
                    float fy_start = c.getY() + (float)delta_y_start;
                    double delta_x_end = c.getRadius() * Math.sin(angle);
                    float fx_end = c.getX() + (float)delta_x_end;
                    double delta_y_end = c.getRadius() * Math.cos(angle);
                    float fy_end = c.getY() + (float)delta_y_end;
                    canvas.drawLine(fx_start, fy_start, fx_end, fy_end, mGreenMazeLinePaint);
                }
            }
        }

        // draw the mPath with the mPaint on the canvas when onDraw
        canvas.drawPath(mPath, mBlackPathPaint);
    }

    // when ACTION_DOWN start touch according to the x,y values
    private void startTouch(float x, float y)
    {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    // when ACTION_MOVE move touch according to the x,y values
    private void moveTouch(float x, float y)
    {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    public void clearCanvas()
    {
        mPath.reset();
        invalidate();
    }

    private void upTouch(float x, float y)
    {
        //Circle newCircle = new Circle(x, y, 2, 2, (fZoomPercentage / 100.f));
        //newCircle.setCenter(x, y, m_fXmiddle, m_fYmiddle, (fZoomPercentage / 100.f));
        //newCircle.updateCircleProps(fZoomPercentage / 100.f);

        //vCircles.add(newCircle);
        //String str = "x: " + (int)newCircle.getX() + " y: " + (int)newCircle.getY();
        //parent.updateCircleText(str);
    }

    //override the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //startTouch(x, y);
                //invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                //moveTouch(x, y);
                //invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upTouch(x, y);
                invalidate();
                break;
        }
        return true;
    }
}