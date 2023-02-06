package com.vincentmet.mkm.utils;

public class XYWidthHeight{
    private final int x, y, width, height;

    public XYWidthHeight(int _x, int _y, int _width, int _height){
        x = _x;
        y = _y;
        width = _width;
        height = _height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}