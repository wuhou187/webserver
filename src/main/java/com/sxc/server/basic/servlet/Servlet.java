package com.sxc.server.basic.servlet;

import com.shsxt.server.Request;
import com.shsxt.server.Response;
import com.shsxt.server.Server02;
public interface Servlet {

    void server(Request request, Response response);

    void common_server();
}
