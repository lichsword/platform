package com.lich.platform.service;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-14
 * Time: 下午10:41
 * <p/>
 * TODO
 */
public class SystemService {

    public static final String LABEL_NEXT_BRAIN = "com.service.next_brain";
    public static final String LABEL_DATABASE = "com.service.database";
    public static final String LABEL_TIME = "com.service.time";
    public static final String LABEL_WEATHER = "com.service.weather";
    public static final String LABEL_WORK_REPORT = "com.service.work_report";
    public static final String LABEL_GIT = "com.service.git";
    public static final String LABEL_CODE_REFACTOR = "com.service.code_refactor";

    private HashMap<String, Object> serviceHashMap = new HashMap<String, Object>();
    private HashMap<String, String> serviceNameHashMap = new HashMap<String, String>();

    private static SystemService sInstance;

    public static SystemService getInstance() {
        if (null == sInstance) {
            sInstance = new SystemService();
        }// end if
        return sInstance;
    }

    public static void main(String[] args) {
        SystemService.getInstance().getService(LABEL_TIME);
    }

    private SystemService() {
        serviceNameHashMap.put(LABEL_WORK_REPORT, WorkReportService.class.getName());
        serviceNameHashMap.put(LABEL_WEATHER, WeatherService.class.getName());
        serviceNameHashMap.put(LABEL_TIME, TimeService.class.getName());
        serviceNameHashMap.put(LABEL_NEXT_BRAIN, NBService.class.getName());
        serviceNameHashMap.put(LABEL_DATABASE, DBService.class.getName());
        serviceNameHashMap.put(LABEL_GIT, GitService.class.getName());
        serviceNameHashMap.put(LABEL_CODE_REFACTOR, CodeRefactorService.class.getName());
    }

    public Object getService(String label) {
        Object service;

        if (serviceHashMap.containsKey(label)) {
            service = serviceHashMap.get(label);
        } else {
            // no cached, init.
            service = registerService(label);
        }
        return service;
    }

    private Object registerService(String label) {
        Object service = null;
        String className = serviceNameHashMap.get(label);
        try {
            Class cls = Class.forName(className);
            service = cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (null != service) {
            serviceHashMap.put(label, service);
        }// end if
        return service;
    }
}
