package com.lich.platform.service;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-24
 * Time: 下午2:29
 * <p/>
 * TODO
 */
public class JavaFunc {

    private ArrayList<JavaMem> mems = new ArrayList<JavaMem>();

    private int flag;

    private String name;

    private String data;

    private int lineStart;

    private int lineSum;

    public ArrayList<JavaMem> getMems() {
        return mems;
    }

    public void setMems(ArrayList<JavaMem> mems) {
        this.mems = mems;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getLineStart() {
        return lineStart;
    }

    public void setLineStart(int lineStart) {
        this.lineStart = lineStart;
    }

    public int getLineSum() {
        return lineSum;
    }

    public void setLineSum(int lineSum) {
        this.lineSum = lineSum;
    }
}
