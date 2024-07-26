package com.shsxt.server;
import com.sxc.server.basic.servlet.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Dispatcher implements  Runnable{
    private Request request;
    private  Response response;
    private Socket client;

    private Servlet servlet;

    public Dispatcher(Socket client) {
        try {
            this.client = client;
            request = new Request(client);
            response = new Response(client);
        } catch (IOException e) {
            e.printStackTrace();
            release();
        }
    }

    @Override
    public void run() {
        try {
            //解析请求信息
            request.parseRequest();
            if(request.getUrl() == null || request.getUrl().equals("")) {
                try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("index.html")) {
                    if (is != null) {
                        response.print(new String(is.readAllBytes()));
                        response.push(200);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            servlet = WebApp.getServer(request.getUrl());
            if(servlet != null) {
                servlet.server(request, response);
                response.push(200);
            }
            else {
                try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("error.html")) {
                    if (is != null) {
                        response.print(new String(is.readAllBytes()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                response.push(404);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.print("你好");
            System.out.println("请检查客户端所输入的url");
            response.push(500);
        }
        release();
    }

    //释放资源
    public void release() {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
