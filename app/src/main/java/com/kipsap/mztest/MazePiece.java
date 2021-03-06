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
        for (double circle = 0.0; circle < iWidth; circle++)
        {
            Circle c = new Circle(circle, 1);
            vCircles.addElement(c);
        }
    }

    public boolean hasOuterWallSegmentAt(int level, int yPos)
    {
        //checks if a certain position has a wall on the right (=outer) side of the cell
        int x = iWidth - (level % iWidth) - 1; //this should be "iWidth - level - 1;" when not drawing segments doubly
        int idx = yPos * iWidth + x;
        char c = mzString.charAt(idx);
        return (c == '4' || c == '5' || c == '6' || c == '7' || c == 'c' || c == 'd' || c == 'e' || c == 'f');
    }

    public boolean hasLowerWallSegmentAt(int level, int yPos)
    {
        //checks if a certain position has a wall on the lower side of the cell
        int x = iWidth - (level % iWidth) - 1; //this should be "iWidth - level - 1;" when not drawing segments doubly
        int idx = yPos * iWidth + x;
        char c = mzString.charAt(idx);
        return (c == '2' || c == '3' || c == '6' || c == '7' || c == 'a' || c == 'b' || c == 'e' || c == 'f');
    }

    public boolean hasInnerWallSegmentAt(int level, int yPos)
    {
        //checks if a certain position has a wall on the left (=inner) side of the cell
        int x = iWidth - (level % iWidth) - 1; //this should be "iWidth - level - 1;" when not drawing segments doubly
        int idx = yPos * iWidth + x;
        char c = mzString.charAt(idx);
        return (c == '1' || c == '3' || c == '5' || c == '7' || c == '9' || c == 'b' || c == 'd' || c == 'f');
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
