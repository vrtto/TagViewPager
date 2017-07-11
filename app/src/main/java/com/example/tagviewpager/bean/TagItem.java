package com.example.tagviewpager.bean;

/**
 * Description：标签item
 * Author：lxl
 * Date： 2017/7/3 17:09
 */
public class TagItem {
    private String            name;//标签名称
    private int            x   ;//x轴坐标
    private int           y    ;//y轴坐标
    private int           position   ;//界面索引
    private int           dx;//如果发生moveView操作保存x轴移动偏差
    private int           dy;//如果发生moveView操作保存y轴移动偏差


    public TagItem(String name, int x, int y, int position) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }
}
