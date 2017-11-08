package com.zg.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * 封装响应信息
 */
public class Response {
    private static final String CRLF = "\r\n";
    private static final String BLANK = " ";

    private PrintWriter writer;
    //正文
    private StringBuilder content;
    //头信息
    private StringBuilder headInfo;
    //长度
    private int len = 0;

    private Response() {
        headInfo = new StringBuilder();
        content = new StringBuilder();
        len = 0;
    }

    Response(Socket client) {
        this();
        try {
            writer = new PrintWriter(client.getOutputStream());
        } catch (IOException e) {
            headInfo = null;
            e.printStackTrace();
        }
    }

    public Response(OutputStream os) {
        this();
        writer = new PrintWriter(os);
    }

    /**
     * 构建正文
     */
    public Response print(String info) {
        content.append(info);
        len += info.getBytes().length;
        return this;
    }
    /**
     * 构建正文+回车
     */
    public Response println(String info) {
        content.append(info).append(CRLF);
        len += (info + CRLF).getBytes().length;
        return this;
    }

    /**
     * 构建响应头信息
     */
    private void createHead(int code) {
        //1) HTTP协议 状态码 描述
        headInfo.append("HTTP/1.1").append(BLANK).append(code).append(BLANK);
        switch (code) {
            case 200:
                headInfo.append("ok");
                break;
            case 404:
                headInfo.append("NOT FOUND");
                break;
            case 500:
                headInfo.append("Server Error");
                break;
        }
        headInfo.append(CRLF);
        //2) 响应头(Response Head)
        headInfo.append("Server:bjsxt Server/0.0.1").append(CRLF);
        headInfo.append("Date:").append(new Date()).append(CRLF);
        headInfo.append("Content-type:text/html;charset=GBK").append(CRLF);
        //正文长度：字节长度
        headInfo.append("Content-Length:").append(len).append(CRLF);
        //3) 正文之前
        headInfo.append(CRLF);
    }

    /**
     * 推送到客户端
     */
    void pushToClient(int code) {
        if (null == headInfo) {
            code = 500;
        }
        createHead(code);
        writer.append(headInfo.toString());
        writer.append(content.toString());
        writer.flush();
    }
    /**
     * 关闭流
     */
    void close() {
        writer.close();
    }
}
