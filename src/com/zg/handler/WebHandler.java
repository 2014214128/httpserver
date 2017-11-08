package com.zg.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.*;

public class WebHandler extends DefaultHandler{
    private List<Entity> entityList;
    private List<Mapping> mappingList;
    private Entity entity;
    private Mapping mapping;
    private String beginTag;
    private boolean isMap;

    @Override
    public void startDocument() throws SAXException {
        /*文档解析开始*/
        entityList = new ArrayList<>();
        mappingList = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        /*元素解析开始*/
        if (qName != null) {
            beginTag = qName;
            if (qName.equals("servlet")) {
                isMap = false;
                entity = new Entity();
            } else if (qName.equals("servlet-mapping")) {
                isMap = true;
                mapping = new Mapping();
            }
        }

    }
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        /*处理内容*/
        if (null != beginTag) {
            String str = new String(ch, start, length);
            if(isMap) {
                if (beginTag.equals("servlet-name")) {
                    mapping.setName(str);
                } else if (beginTag.equals("url-pattern")) {
                    mapping.getUrlPattern().add(str);
                }
            } else {
                if (beginTag.equals("servlet-name")) {
                    entity.setName(str);
                } else if (beginTag.equals("servlet-class")) {
                    entity.setClz(str);
                }
            }
        }
    }
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        /*元素解析结束*/
        if (qName != null) {
            if (qName.equals("servlet")) {
                entityList.add(entity);
            } else if (qName.equals("servlet-mapping")) {
                mappingList.add(mapping);
            }
        }
    }
    @Override
    public void endDocument() throws SAXException {
        /*文档解析结束*/
        super.endDocument();
    }

    public List<Entity> getEntityList() {
        return entityList;
    }

    public List<Mapping> getMappingList() {
        return mappingList;
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        WebHandler handler = new WebHandler();
        parser.parse(Thread.currentThread().getContextClassLoader().getResourceAsStream("web.xml"),handler);
        for (Entity entity : handler.getEntityList()) {
            System.out.println(entity.getName());
        }
        for (Mapping mapping : handler.getMappingList()) {
            System.out.println(mapping.getName());
        }
    }
}
