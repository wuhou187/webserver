package com.sxc.server.basic.servlet;

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

public class WebApp {
    private static WebContext context;
    private static WebHandler handler;
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

   static {
       try {
           // 1、获取解析工厂
           SAXParserFactory factory = SAXParserFactory.newInstance();
           // 2、从解析工厂获取解析器
           SAXParser parse = factory.newSAXParser();

           // 4、加载文档 Document 注册处理器
           handler = new WebHandler();

           // 检查文件是否存在
           InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("web.xml");
           if (inputStream == null) {
               System.out.println("File not found!");
           } else {
               //解析
               parse.parse(inputStream, handler);
           }
       } catch (ParserConfigurationException | SAXException | IOException e) {
           e.printStackTrace();
           System.out.println("解析失败");
       }
   }

   public static Servlet getServer(String url){
      try {
          //假设输入了login
          context = new WebContext(handler.getListEntity(), handler.getListMapping());
          String className = context.getClz("/" + url);//根据url动态地构建对象，也就是相应的服务
          Class<?> clz = Class.forName(className);
          return (Servlet) clz.getConstructor().newInstance();

      } catch (Exception e) {
          e.printStackTrace();
          System.out.println("配置文件当中没有这个类名");
          return null;
      }
   }
}
