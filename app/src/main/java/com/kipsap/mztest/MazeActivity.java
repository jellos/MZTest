package com.kipsap.mztest;

import android.os.Handler;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView; //blaap

public class MazeActivity extends Activity
{
    private CanvasView customCanvas;
    private long lTimeSinceStartPressed = 0L;
    private long lTimeStartPressed = 0L;

    private TextView m_circleProps;
    private TextView m_zoomLevel;

    Handler mainTimeHandler = new Handler();
    Runnable mainTimeRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            lTimeSinceStartPressed = System.currentTimeMillis() - lTimeStartPressed;
            int seconds = (int) (lTimeSinceStartPressed / 1000);
            int hundredths = (int) (lTimeSinceStartPressed % 1000) / 10;
            int minutes = seconds / 60;
            seconds = seconds % 60;

            //m_textTimer.setText(String.format("%d:%02d:%02d", minutes, seconds, hundredths));

            if (lTimeSinceStartPressed > 2000)
            {
                mainTimeHandler.removeCallbacks(mainTimeRunnable);
                lTimeSinceStartPressed = 0L;
                System.out.println("2 seconds have passed");
            }
            else
            {
                customCanvas.nextStep();
                mainTimeHandler.postDelayed(this, 50);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze);

        customCanvas = (CanvasView) findViewById(R.id.maze_canvas);
        m_circleProps = (TextView) findViewById(R.id.text1);
        m_zoomLevel = (TextView) findViewById(R.id.text2);
        m_circleProps.setText("no coords");
        m_zoomLevel.setText("zoom: 100.0");
    }

    public void goBack(View v)
    {
        customCanvas.setTimeForward(false);
        lTimeStartPressed = System.currentTimeMillis();

        //mainTimeHandler.postDelayed(mainTimeRunnable, 0);

        customCanvas.nextStep();
    }

    public void goForward(View v)
    {
        customCanvas.setTimeForward(true);
        lTimeStartPressed = System.currentTimeMillis();

        //mainTimeHandler.postDelayed(mainTimeRunnable, 0);

        customCanvas.nextStep();
    }

    public void updateCircleText(String str)
    {
        m_circleProps.setText(str);
    }

    public void updateZoomLevel(String str)
    {
        m_zoomLevel.setText(str);
    }
}