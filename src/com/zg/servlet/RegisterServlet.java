package com.zg.servlet;

import com.zg.server.Request;
import com.zg.server.Response;
import com.zg.webApplication.Servlet;


public class RegisterServlet extends Servlet {
    @Override
    public void doGet(Request request, Response response) throws Exception {
        response.print("<html><head><title>Http响应示例</title></head><body>login ").print(request.getParameter("name"));
        response.println("</body></html>");
    }



    @Override
    public void doPost(Request request, Response response) throws Exception {

    }
}
