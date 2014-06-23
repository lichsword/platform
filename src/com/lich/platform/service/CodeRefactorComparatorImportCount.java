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
public class CodeRefactorComparatorImportCount implements Comparator<CodeRefactorResult> {

    private int flag = Constants.fInc;

    public CodeRefactorComparatorImportCount(int flag) {
        this.flag = flag;
    }

    @Override
    public int compare(CodeRefactorResult object, CodeRefactorResult another) {
        if (flag == Constants.fInc) {
            return object.getImportCount() - another.getImportCount();
        } else {
            return another.getImportCount() - object.getImportCount();
        }
    }

}
