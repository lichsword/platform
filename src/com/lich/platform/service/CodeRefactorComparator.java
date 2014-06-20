package com.lich.platform.service;

import com.lich.platform.Constants;

import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-20
 * Time: 上午11:49
 * <p/>
 * TODO
 */
public class CodeRefactorComparator implements Comparator<CodeRefactorResult> {

    private int flag = Constants.fInc;

    public CodeRefactorComparator(int flag) {
        this.flag = flag;
    }

    @Override
    public int compare(CodeRefactorResult object, CodeRefactorResult another) {
        if (flag == Constants.fInc) {
            return object.getLines() - another.getLines();
        } else {
            return another.getLines() - object.getLines();
        }
    }

}
