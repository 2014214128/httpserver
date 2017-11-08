package com.zg.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * 创建服务器，并启动
 */
public class Server {
    private ServerSocket server;
    private boolean isShutDown = false;


    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
    /**启动方法*/
    public void start() {
        start(8888);
    }
    /**指定端口启动方法*/
    public void start(int port) {
        try {
            server = new ServerSocket(port);
            this.receive();
        } catch (IOException e) {
            stop();
        }
    }

    /**关闭服务*/
    public void close() {
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**停止服务器*/
    public void stop() {
        isShutDown = true;
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**接收客户端*/
    private void receive() {
        try {
            while (!isShutDown) {
                Socket socket = server.accept();
                Dispatcher dispatcher = new Dispatcher(socket);
                new Thread(dispatcher).start();
            }
        } catch (IOException e) {
            stop();
        }
    }
}
