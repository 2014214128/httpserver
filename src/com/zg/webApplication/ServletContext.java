package com.zg.webApplication;

import java.util.HashMap;
import java.util.Map;

public class ServletContext {
    /*为每个servlet取一个别名*/
    private Map<String, String> servlet;
    private Map<String, String> mapping;

    ServletContext() {
        servlet = new HashMap<>();
        mapping = new HashMap<>();
    }

    public Map<String, String> getMapping() {
        return mapping;
    }

    public Map<String, String> getServlet() {
        return servlet;
    }

    public void setMapping(Map<String, String> mapping) {
        this.mapping = mapping;
    }

    public void setServlet(Map<String, String> servlet) {
        this.servlet = servlet;
    }
}
