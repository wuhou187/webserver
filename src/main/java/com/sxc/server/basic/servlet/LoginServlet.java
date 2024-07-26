package com.sxc.server.basic.servlet;

import com.shsxt.server.Request;
import com.shsxt.server.Response;

import java.util.Arrays;

public class LoginServlet implements Servlet{

    @Override
    public void common_server() {
        System.out.println("login");
    }
    @Override
    public void server(Request request, Response response) {
        response.createContent("<html>");
        response.createContent("<head>");
        response.createContent("<title>");
        response.createContent("服务器响应成功");
        response.createContent("</title>");
        response.createContent("</head>");
        response.createContent("<body>");
        response.createContent("欢迎回来" + Arrays.toString(request.getValues("uname")));
        response.createContent("</body>");
        response.createContent("</html>");
    }
}
