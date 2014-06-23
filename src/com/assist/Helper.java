package com.assist;

import com.util.FileUtils;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-23
 * Time: 下午4:39
 * <p/>
 * TODO
 */
public class Helper implements IHelper {

    @Override
    public void record(String log) {
        String path = "2014-06-23.log";
        boolean append = true;
        String content = String.format("%s\n", log);   // TODO 以后可以加上时间字段以空格分隔
        FileUtils.writeIntoFile(new File(path), content, append);
    }
}