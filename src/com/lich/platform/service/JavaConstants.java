package com.lich.platform.service;

import java.util.HashMap;

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
    public static final int VISIT_PRIVATE = 0x03;

    /**
     * 修改性
     */
    public static final int MASK_MODIFY = 0xf0;
    public static final int MODIFY_STATIC = 0x10;
    public static final int MODIFY_FINAL = 0x20;

    /**
     * Java 关键字
     */
    public static final String KW_PACKAGE = "package";
    public static final String KW_IMPORT = "import";
    public static final String KW_PUBLIC = "public";
    public static final String KW_PRIVATE = "private";
    public static final String KW_PROTECT = "protect";
    public static final String KW_STATIC = "static";
    public static final String KW_FINAL = "final";
    public static final String KW_CLASS = "class";


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
    /**
     * <p>member:</p>
     * <li>package</li>
     * <li>import</li>
     * <li>variable</li>
     */
    public static final int STATE_MEMBER_START = 8;
    public static final int STATE_MEMBER_END = 9;
    public static final int STATE_FUNC_START = 10;
    public static final int STATE_FUNC_END = 11;
    public static final int STATE_PACKAGE_START = 12;
    public static final int STATE_PACKAGE_END = 13;
    public static final int STATE_IMPORT_START = 14;
    public static final int STATE_IMPORT_END = 15;
    public static final int STATE_VARIABLE_START = 16;
    public static final int STATE_VARIABLE_END = 17;
    public static final int STATE_LINE_START = 18;
    public static final int STATE_LINE_END = 19;
    public static final int STATE_ELEMENT_START = 20;
    public static final int STATE_ELEMENT_END = 21;


    public static final int ERROR = -1;

    private static HashMap<Integer, String> stateNameMap = new HashMap<Integer, String>();

    static {
        stateNameMap.put(STATE_IN_ERROR, "STATE_IN_ERROR");
        stateNameMap.put(STATE_DOCUMENT_START, "STATE_DOCUMENT_START");
        stateNameMap.put(STATE_DOCUMENT_END, "STATE_DOCUMENT_END");
        stateNameMap.put(STATE_CLASS_START, "STATE_CLASS_START");
        stateNameMap.put(STATE_CLASS_END, "STATE_CLASS_END");
        stateNameMap.put(STATE_COMMENTS_START, "STATE_COMMENTS_START");
        stateNameMap.put(STATE_COMMENTS_END, "STATE_COMMENTS_END");
        stateNameMap.put(STATE_MEMBER_START, "STATE_MEMBER_START");
        stateNameMap.put(STATE_MEMBER_END, "STATE_MEMBER_END");
        stateNameMap.put(STATE_FUNC_START, "STATE_FUNC_START");
        stateNameMap.put(STATE_FUNC_END, "STATE_FUNC_END");
        stateNameMap.put(STATE_PACKAGE_START, "STATE_PACKAGE_START");
        stateNameMap.put(STATE_PACKAGE_END, "STATE_PACKAGE_END");
        stateNameMap.put(STATE_IMPORT_START, "STATE_IMPORT_START");
        stateNameMap.put(STATE_IMPORT_END, "STATE_IMPORT_END");
        stateNameMap.put(STATE_VARIABLE_START, "STATE_VARIABLE_START");
        stateNameMap.put(STATE_VARIABLE_END, "STATE_VARIABLE_END");
        stateNameMap.put(STATE_LINE_START, "STATE_LINE_START");
        stateNameMap.put(STATE_LINE_END, "STATE_LINE_END");
        stateNameMap.put(STATE_ELEMENT_START, "STATE_ELEMENT_START");
        stateNameMap.put(STATE_ELEMENT_END, "STATE_ELEMENT_END");
    }

    public static String getStateName(int state) {
        return stateNameMap.get(state);
    }
}