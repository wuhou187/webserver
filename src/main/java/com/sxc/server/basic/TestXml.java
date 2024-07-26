package com.sxc.server.basic;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TestXml {
    // 3、编写处理器
    public static class PersonHandler extends DefaultHandler {
        private List<Person> list;
        private Person person;
        private String tag;//存储标签
        @Override
        public void startDocument() throws SAXException {
            list = new ArrayList<>();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            System.out.println(qName + "->解析开始");
            tag = qName;
            if(tag != null) {
                if(tag.equals("person")) {
                    person = new Person();
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            //这里要使用qName，因为在每个标签结束之后tag被设置为空，最后无法存储信息到容器里面
            if(qName != null) {
                if(qName.equals("person")) {
                    list.add(person);
                }
            }
            tag = null;//tag设置为空是为了不让空内容覆盖原有的内容
            System.out.println(qName + "->解析结束");
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String contents = new String(ch, start, length).trim();
            if(contents.length() > 0) {
                if(tag.equals("name")) {
                    person.setName(contents);
                }
                else if(tag.equals("age")) {
                    person.setAge(Integer.parseInt(contents));
                }
            }
        }

        @Override
        public void endDocument() throws SAXException {
            System.out.println("文档解析结束");
        }

        public List<Person> getList() {
            return list;
        }
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        // 1、获取解析工厂
        SAXParserFactory factory = SAXParserFactory.newInstance();
        // 2、从解析工厂获取解析器
        SAXParser parse = factory.newSAXParser();

        // 4、加载文档 Document 注册处理器
        PersonHandler handler = new PersonHandler();

        // 检查文件是否存在
        // 解析
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("p.xml");
        if (inputStream == null) {
            System.out.println("File not found!");
        } else {
            //解析
            parse.parse(inputStream, handler);
        }

        //获取数据
        List<Person> persons = handler.getList();
        for(Person p : persons) {
            System.out.println(p);
        }
    }
}