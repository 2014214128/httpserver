package com.zg.servlet;


import com.zg.server.Request;
import com.zg.server.Response;
import com.zg.webApplication.Servlet;

public class LoginServlet extends Servlet {

    @Override
    public void doGet(Request request, Response response) throws Exception {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        if (login(name, password)) {
            response.println("success!");
        } else {
            response.println("failure!");
        }
    }

    /**
     * 验证登陆是否成功
     */
    private boolean login(String name, String password) {
        if (name == null || password == null) {
            return false;
        }
        return name.equals("zhengguo") && password.equals("123456");
    }

    @Override
    public void doPost(Request request, Response response) throws Exception {

    }
}
