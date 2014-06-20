package com.lich.platform.service;

import com.jni.GIT;
import com.util.FileUtils;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-17
 * Time: 下午3:31
 * <p/>
 * TODO
 */
public class GitService implements IGit {
    @Override
    public String log(String path, String author, String since) {
        GIT.log(path, author, since);
        return FileUtils.readFile("git.log");
    }
}
