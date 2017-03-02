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
import java.util.Vector;

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
    private static final float STEP_SIZE = 50f;

    private float fZoomPercentage = 100.f;
    private boolean bForwardInTime = true;

    private Vector<Circle> vCircles = new Vector<>();

    private int iMazeSegments = 6; // = height
    private int iMazeDepth = 4; // = width
    private MazePiece theMazePiece = new MazePiece(iMazeDepth, iMazeSegments, 7, 0, "6bac9aa259cd5516361c9a67"); // goed is: 6bac9aa259cd5516361c9a67

    public void nextStep()
    {
        String str;
        if (bForwardInTime)
            fZoomPercentage += STEP_SIZE;
        else
            fZoomPercentage -= STEP_SIZE;
        String f = String.format("%2.2f", fZoomPercentage);
        str = "zoom: " + f;
        parent.updateZoomLevel(str);
        for (int i = 0; i < vCircles.size(); i++)
        {
            Circle c = vCircles.elementAt(i);
            c.updateCircleProps(fZoomPercentage / 100.f);
            if (c.isUserAddedCircle())
            {
                str = "x: " + (int)c.getX() + " y: " + (int)c.getY();
                parent.updateCircleText(str);
            }
        }

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

        Circle circle0 = new Circle(0.0, 1);
        Circle circle1 = new Circle(1.0, 1);
        Circle circle2 = new Circle(2.0, 1);
        Circle circle3 = new Circle(3.0, 1);
        //Circle circle4 = new Circle(4.0, 1);
        //Circle circle5 = new Circle(5.0, 1);
        //Circle circle6 = new Circle(6.0, 1);
        //Circle circle7 = new Circle(7.0, 1);
        //Circle circle8 = new Circle(8.0, 1);
        //Circle circle9 = new Circle(9.0, 1);

        vCircles.add(circle0);
        vCircles.add(circle1);
        vCircles.add(circle2);
        vCircles.add(circle3);
        //vCircles.add(circle4);
        //vCircles.add(circle5);
        //vCircles.add(circle6);
        //vCircles.add(circle7);
        //vCircles.add(circle8);
        //vCircles.add(circle9);
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

        for (int i = 0; i < vCircles.size(); i++)
            if (vCircles.elementAt(i).isCenteredMazeCircle()) // maze circle
                vCircles.elementAt(i).setCenter(m_fXmiddle, m_fYmiddle, m_fXmiddle, m_fYmiddle, (fZoomPercentage / 100.f));
    }

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        for (int i = 0; i < vCircles.size(); i++)
        {
            Circle c = vCircles.elementAt(i);
            mGreenMazeLinePaint.setStrokeWidth(c.getStrokeWidth());
            for (int segment = 0; segment < iMazeSegments; segment++)
            {
                boolean bHasWallSegment = theMazePiece.hasWallSegmentAt(i, segment);
                if (bHasWallSegment)
                    canvas.drawArc(c.getRect(), segment * 60.f, 60.f, false, mGreenMazeLinePaint);
            }
        }

        //canvas.drawCircle(m_iScreenWidth / 2, m_iScreenHeight / 2, fRadius / 4, mGreenMazeLinePaint);
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
        Circle newCircle = new Circle(x, y, 2, 2, (fZoomPercentage / 100.f));
        newCircle.setCenter(x, y, m_fXmiddle, m_fYmiddle, (fZoomPercentage / 100.f));
        newCircle.updateCircleProps(fZoomPercentage / 100.f);

        vCircles.add(newCircle);
        String str = "x: " + (int)newCircle.getX() + " y: " + (int)newCircle.getY();
        parent.updateCircleText(str);
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