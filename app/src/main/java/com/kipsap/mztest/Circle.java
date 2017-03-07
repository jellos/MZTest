package com.kipsap.mztest;


import android.graphics.RectF;

public class Circle
{
    private float m_fOriginalX;
    private float m_fOriginalY;

    private float m_fGlobalCenterX;
    private float m_fGlobalCenterY;
    private float m_fDistanceToCenterX;
    private float m_fDistanceToCenterY;
    private float m_fOriginalDistanceToCenterX;
    private float m_fOriginalDistanceToCenterY;

    private float m_fX;
    private float m_fY;
    private float m_fRadius;
    private float m_fOriginalRadius;
    private double m_dLevel;
    private int m_iType; //1 = maze, 2 = user touched

    public Circle(double level, float zoomlevel)
    {
        m_dLevel = level;
        m_fOriginalRadius = m_fRadius = (720.f / (float) (Math.pow(2.0, level))) / zoomlevel;
        m_iType = 1;
    }

    public Circle(float x, float y, double level, int type, float zoomlevel)
    {
        m_fOriginalX = m_fX = x;
        m_fOriginalY = m_fY = y;
        m_dLevel = level;
        m_fOriginalRadius = m_fRadius = (720.f / (float) (Math.pow(2.0, level))) / zoomlevel;
        m_iType = type;
    }

    public void setCenter(float x, float y, float globalCenterX, float globalCenterY, float zoomlevel)
    {
        m_fX = x;
        m_fY = y;
        m_fGlobalCenterX = globalCenterX;
        m_fGlobalCenterY = globalCenterY;
        m_fDistanceToCenterX = m_fOriginalDistanceToCenterX = (x - globalCenterX) / zoomlevel;
        m_fDistanceToCenterY = m_fOriginalDistanceToCenterY = (y - globalCenterY) / zoomlevel;
    }

    public void updateCircleProps(float fzoom)
    {
        m_fRadius = m_fOriginalRadius * fzoom;
        if (m_iType == 2)
        {
            m_fDistanceToCenterX = m_fOriginalDistanceToCenterX * fzoom;
            m_fDistanceToCenterY = m_fOriginalDistanceToCenterY * fzoom;
            m_fX = m_fGlobalCenterX + m_fDistanceToCenterX;
            m_fY = m_fGlobalCenterY + m_fDistanceToCenterY;
        }
    }

    public boolean isCenteredMazeCircle()
    {
        return m_iType == 1;
    }

    public boolean isUserAddedCircle()
    {
        return m_iType == 2;
    }

    public float getX() {return m_fX;}
    public float getY() {return m_fY;}
    public float getRadius() {return m_fRadius;}
    public float getStrokeWidth()
    {
        return m_fRadius / 50.f;
    }

    public RectF getOuterRect()
    {
        return new RectF(m_fX - m_fRadius, m_fY - m_fRadius, m_fX + m_fRadius, m_fY + m_fRadius);
    }

    public RectF getInnerRect()
    {
        return new RectF(m_fX - (m_fRadius / 2.f), m_fY - (m_fRadius / 2.f), m_fX + (m_fRadius / 2.f), m_fY + (m_fRadius / 2.f));
    }


}
