package com.shsxt.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

//使用ServerSocket建立与浏览器的连接，获取请求协议
public class Server01 {
    ServerSocket serverSocket;
    //启动服务
    public void start() {
        try {
            serverSocket = new ServerSocket(8888);
            receive();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务器启动失败");
        }
    }
    //接收请求协议
    public void receive() {
        try {
            Socket client = serverSocket.accept();
            System.out.println("一个客户端正在连接....");
            InputStream is = client.getInputStream();
            byte[] dates = new byte[1024*104];
            int len = is.read(dates);
            String request = new String(dates, 0, len);
            System.out.println(request);
        } catch (IOException e) {
           e.printStackTrace();
            System.out.println("客户端出错");
        }
    }
    //停止服务
    public void stop() {

    }
    public static void main(String[] args) {
        Server01 server01 = new Server01();
        server01.start();
    }
}
