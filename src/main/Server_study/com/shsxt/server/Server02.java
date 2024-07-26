package com.shsxt.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import com.sxc.server.basic.servlet.*;

//返回相应协议
public class Server02 {
    ServerSocket serverSocket;
    private boolean isRunning;
    public void start() {
        try {
            isRunning = true;
            serverSocket = new ServerSocket(8888);
            receive();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务器启动失败");
        }
    }

    public void receive() {
        try {
            while(isRunning) {
                Socket client = serverSocket.accept();
                System.out.println("一个客户端正在连接....");
                new Thread(new Dispatcher(client)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Server02 server02 = new Server02();
        server02.start();
    }
}
