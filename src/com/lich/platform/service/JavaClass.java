package com.lich.platform.service;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-24
 * Time: 下午2:40
 * <p/>
 * TODO
 */
public class JavaClass {

    private ArrayList<JavaFunc> funcs = new ArrayList<JavaFunc>();
    private ArrayList<JavaMem> memses = new ArrayList<JavaMem>();
    private ArrayList<JavaClass> innerClasses = new ArrayList<JavaClass>();

    public ArrayList<JavaFunc> getFuncs() {
        return funcs;
    }

    public void setFuncs(ArrayList<JavaFunc> funcs) {
        this.funcs = funcs;
    }

    public ArrayList<JavaMem> getMemses() {
        return memses;
    }

    public void setMemses(ArrayList<JavaMem> memses) {
        this.memses = memses;
    }

    public ArrayList<JavaClass> getInnerClasses() {
        return innerClasses;
    }

    public void setInnerClasses(ArrayList<JavaClass> innerClasses) {
        this.innerClasses = innerClasses;
    }
}