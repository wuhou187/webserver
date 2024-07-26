package com.sxc.server.basic.servlet;

import com.shsxt.server.Request;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class TestXml02 {
    // 3、编写处理器
    public static class WebHandler extends DefaultHandler {
        private List<Entity> listEntity;
        private List<Mapping> listMapping;
        private Entity entity;
        private Mapping mapping;
        private String tag;
        private boolean isMapping = false;
        @Override
        public void startDocument() throws SAXException {
            System.out.println("开始解析文档");
            listEntity = new ArrayList<>();
            listMapping = new ArrayList<>();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            System.out.println(qName + "->解析开始");
            tag = qName;
            if(tag != null) {
                if(tag.equals("servlet")) {
                    entity = new Entity();
                    isMapping = false;
                }
                else if(tag.equals("servlet-mapping")) {
                    mapping = new Mapping();
                    isMapping = true;
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            System.out.println(qName + "->解析结束");
            if(qName != null) {
                if(qName.equals("servlet")) {
                    listEntity.add(entity);
                }
                else if(qName.equals("servlet-mapping")) {
                    listMapping.add(mapping);
                }
            }
            tag = null;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String contents = new String(ch, start, length).trim();
            if(contents.length() > 0) {
                if(!(isMapping)) {//处理servlet
                    if(tag.equals("servlet-name")) {
                        entity.setName(contents);
                    }
                    else if(tag.equals("servlet-class")) {
                        entity.setClz(contents);
                    }
                }
                else {//处理servlet-mapping
                    if(tag.equals("servlet-name")) {
                        mapping.setName(contents);
                    }
                    else if(tag.equals("url-pattern")) {
                        mapping.addPatten(contents);
                    }
                }
            }

        }

        @Override
        public void endDocument() throws SAXException {
            System.out.println("文档解析结束");
        }

        public List<Entity> getListEntity() {
            return listEntity;
        }

        public List<Mapping> getListMapping() {
            return listMapping;
        }
    }

    public static void main(String[] args) throws Exception {
        // 1、获取解析工厂
        SAXParserFactory factory = SAXParserFactory.newInstance();
        // 2、从解析工厂获取解析器
        SAXParser parse = factory.newSAXParser();

        // 4、加载文档 Document 注册处理器
        WebHandler handler = new WebHandler();

        // 检查文件是否存在
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("web.xml");
        if (inputStream == null) {
            System.out.println("File not found!");
        } else {
            //解析
            parse.parse(inputStream, handler);
        }

        //获取数据
        WebContext context = new WebContext(handler.getListEntity(), handler.getListMapping());

        //假设输入了login
        String className = context.getClz("/reg");
        Class<?> clz = Class.forName(className);
        Servlet servlet = (Servlet) clz.getConstructor().newInstance();
        System.out.println(servlet);
        servlet.common_server();
    }
}