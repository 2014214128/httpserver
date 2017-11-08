package com.zg.server;

import com.zg.webApplication.Servlet;
import com.zg.webApplication.WebApp;

import java.io.IOException;
import java.net.Socket;

/**
 * 一个请求与响应就一个此对象
 */
public class Dispatcher implements Runnable {
    private Request request;
    private Response response;
    private Socket client;
    private int code = 200;

    Dispatcher(Socket client) {
        this.client = client;
        try {
            request = new Request(client.getInputStream());
            response = new Response(client);
        } catch (IOException e) {
            code = 500;
            return;
        }
    }
    @Override
    public void run() {
        try {
            //控制器
            Servlet servlet = WebApp.getServlet(request.getUrl());
            if (null == servlet) {
                this.code = 404;
            } else {
                servlet.service(request, response);
            }
        } catch (Exception e) {
            this.code = 500;
        }
        response.pushToClient(code);
        closeAll();
    }

    /**
     * 关闭资源
     */
    private void closeAll() {
        request.close();
        response.close();
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
