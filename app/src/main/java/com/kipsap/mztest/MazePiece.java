package com.kipsap.mztest;

public class MazePiece
{
    private int iWidth;
    private int iHeight;
    private int iEntrance;
    private int iExit;
    private String mzString;

    public MazePiece(int width, int height, int entrance, int exit, String mzstr)
    {
        iWidth = width;
        iHeight = height;
        iEntrance = entrance;
        iExit = exit;
        mzString = mzstr;
    }

    public boolean hasWallSegmentAt(int level, int yPos)
    {
        //checks if a certain position has a wall on the right (=outer) side
        int x = iWidth - level - 1;
        int idx = yPos * iWidth + x;
        char c = mzString.charAt(idx);
        return (c == '4' || c == '5' || c == '6' || c == '7' || c == 'c' || c == 'd' || c == 'e' || c == 'f');
    }

    public int getWidth() {return iWidth;}
    public int getHeight() {return iHeight;}
}
