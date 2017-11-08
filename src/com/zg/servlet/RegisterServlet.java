package com.zg.servlet;

import com.zg.server.Request;
import com.zg.server.Response;
import com.zg.webApplication.Servlet;

import java.io.UnsupportedEncodingException;


public class RegisterServlet extends Servlet {

    @Override
    public void doGet(Request request, Response response) throws Exception {
        response.print("<html><head><title>Http响应示例</title></head><body>register ").print(request.getParameter("name"));
        response.println("</body></html>");
    }



    @Override
    public void doPost(Request request, Response response) throws Exception {

    }
}
