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

    public static final String LABEL_TIME = "com.service.time";
    public static final String LABEL_WEATHER = "com.service.weather";
    public static final String LABEL_WORK_REPORT = "com.service.work_report";

    private HashMap<String, BaseService> serviceHashMap = new HashMap<String, BaseService>();
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
    }

    public BaseService getService(String label) {
        BaseService service;

        if (serviceHashMap.containsKey(label)) {
            service = serviceHashMap.get(label);
        } else {
            // no cached, init.
            service = registerService(label);
        }
        return service;
    }

    private BaseService registerService(String label) {
        BaseService service = null;
        String className = serviceNameHashMap.get(label);
        try {
            Class cls = Class.forName(className);
            Object xyz = cls.newInstance();
            if (xyz instanceof BaseService) {
                service = (BaseService) xyz;
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        serviceHashMap.put(label, service);
        return service;
    }
}
