package com.zg.webApplication;


import com.zg.handler.Entity;
import com.zg.handler.Mapping;
import com.zg.handler.WebHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.util.Map;

public class WebApp {
    private static ServletContext context;

    static {
        context = new ServletContext();
        Map<String, String> mappingMap = context.getMapping();
        Map<String, String> servletMap = context.getServlet();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();;
            WebHandler handler = new WebHandler();
            parser.parse(Thread.currentThread().getContextClassLoader().getResourceAsStream("web.xml"),handler);
            /*存放servletMap*/
            for (Entity entity : handler.getEntityList()) {
                servletMap.put(entity.getName(),entity.getClz());
            }
            /*存放mappingMap*/
            for (Mapping mapping : handler.getMappingList()) {
                for (String value : mapping.getUrlPattern()) {
                    mappingMap.put(value, mapping.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Servlet getServlet(String url) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (null == url || url.equals("")) {
            return null;
        }
        return (Servlet) Class.forName(context.getServlet().get(context.getMapping().get(url))).newInstance();
    }

}
