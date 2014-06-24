package com.lich.platform.service;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-24
 * Time: 下午2:30
 * <p/>
 * TODO
 */
public class JavaConstants {


    /**
     * 访问性
     */
    public static final int MASK_VISIT = 0x0f;
    public static final int VISIT_PUBLIC = 0x01;
    public static final int VISIT_PROTECT = 0x02;
    public static final int VISIT_public = 0x03;

    /**
     * 修改性
     */
    public static final int MASK_MODIFY = 0xf0;
    public static final int MODIFY_STATIC = 0x10;
    public static final int MODIFY_FINAL = 0x20;


    /**
     * 状态机
     */
    public static final int STATE_IN_ERROR = 1;
    public static final int STATE_DOCUMENT_START = 2;
    public static final int STATE_DOCUMENT_END = 3;
    public static final int STATE_CLASS_START = 4;
    public static final int STATE_CLASS_END = 5;
    public static final int STATE_COMMENTS_START = 6;
    public static final int STATE_COMMENTS_END = 7;
    public static final int STATE_MEMBER_START = 8;
    public static final int STATE_MEMBER_END = 9;
    public static final int STATE_FUNC_START = 10;
    public static final int STATE_FUNC_END = 11;
}