package com.shsxt.server;

import java.io.*;
import java.net.Socket;
import java.util.Date;

//封装响应，只在服务器的运行当中暴露内容
public class Response {
    private BufferedWriter bw;
    //头(状态行，请求头，回车)
    private StringBuilder head;
    //正文
    private StringBuilder content;
    //正文的长度
    private int len;
    private final String BLACK = " ";
    private final String CRLF = "\r\n";

    private Response() {
        content = new StringBuilder();
        head = new StringBuilder();
        len = 0;
    }

    public Response(Socket client) {
        this();
        try {
            bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Response(OutputStream os) {
        this();
        bw = new BufferedWriter(new OutputStreamWriter(os));
    }

    //创建头
    public void createHead(int code) {
        //1.状态行
        //1、状态行:HTTP/1.1 200 OK
        head.append("HTTP/1.1").append(BLACK);
        head.append(code).append(BLACK);
        switch (code) {
           case 200 -> head.append("OK").append(CRLF);
           case 404 -> head.append("NOT FOUND").append(CRLF);
           case 500 -> head.append("SERVER ERROR").append(CRLF);
           default -> System.out.println("输入的状态码错误");
        }
        /**2、请求头:
         Date:Mon,31Dec209904:25:57GMT
         Server:shsxt Server/0.0.1;charset=GBK
         Content-type:text/html
         Content-length:39725426（与正文之间有空行）*/
        head.append("Date:").append(new Date()).append(CRLF);
        head.append("Server:shsxt Server/0.0.1;charset=GBK").append(CRLF);
        head.append("Content-type:text/html").append(CRLF);
        head.append("Content-length:").append(len).append(CRLF);
        head.append(CRLF);
    }

    //动态创建请求正文
    public Response createContent(String info) {
        content.append(info);
        len += info.getBytes().length;
        return this;
    }

    //输出到客户端
    public void push(int code) {
        createHead(code);
        try {
            bw.append(head);
            bw.append(content);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //动态添加内容
    public Response print(String info) {
        content.append(info);
        len += info.getBytes().length;
        return this;
    }
}
