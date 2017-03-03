package com.kipsap.mztest;

import java.util.Vector;

public class MazePiece
{
    private int iWidth;
    private int iHeight;
    private int iEntrance;
    private int iExit;
    private String mzString;
    private Vector<Circle> vCircles;

    public MazePiece(int width, int height, int entrance, int exit, String mzstr)
    {
        iWidth = width;
        iHeight = height;
        iEntrance = entrance;
        iExit = exit;
        mzString = mzstr;

        vCircles = new Vector<>();
        for (double circle = 0.0; circle < iWidth * 3; circle++)
        {
            Circle c = new Circle(circle, 1);
            vCircles.addElement(c);
        }
    }

    public boolean hasWallSegmentAt(int level, int yPos)
    {
        //checks if a certain position has a wall on the right (=outer) side
        int x = iWidth - (level % iWidth) - 1; //this should be "iWidth - level - 1;" when not drawing segments doubly
        int idx = yPos * iWidth + x;
        char c = mzString.charAt(idx);
        return (c == '4' || c == '5' || c == '6' || c == '7' || c == 'c' || c == 'd' || c == 'e' || c == 'f');
    }

    public void updateAllCircleProps(float fzoom)
    {
        for (int i = 0; i < vCircles.size(); i++)
        {
            Circle c = vCircles.elementAt(i);
            c.updateCircleProps(fzoom);
            //if (c.isUserAddedCircle())
            //{
             //   str = "x: " + (int)c.getX() + " y: " + (int)c.getY();
              //  parent.updateCircleText(str);
            //}
        }
    }

    public Vector<Circle> getCircleVector()
    {
        return vCircles;
    }

    public int getWidth() {return iWidth;}
    public int getHeight() {return iHeight;}
}
