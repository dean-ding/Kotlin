package cn.com.dean.server;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer
{
    private String PATH = "";
    private int mPort = 80;

    public MyServer(String requestPath, int port)
    {
        this.PATH = requestPath;
        this.mPort = port;
    }

    public void service() throws Exception
    {
        ServerSocket serverSocket = new ServerSocket(this.mPort);
        System.out.println("server is ok.");
        //开启serverSocket等待用户请求到来，然后根据请求的类别作处理
        //在这里我只针对GET和POST作了处理
        //其中POST具有解析单个附件的能力
        while (true)
        {
            Socket socket = serverSocket.accept();
            System.out.println("new request coming.");
            DataInputStream reader = new DataInputStream((socket.getInputStream()));
            String line = reader.readLine();
            if (line == null)
            {
                Thread.sleep(100);
                continue;
            }
            String method = line.substring(0, 4).trim();
            OutputStream out = socket.getOutputStream();
            String requestPath = line.split(" ")[1];
            System.out.println(requestPath);
            String response = "";
            String result = "";
            if ("GET".equalsIgnoreCase(method))
            {
                result = ParseData.parseData(requestPath);
                if (result.equals(""))
                {
                    result = "HelloWorld";
                }
                result += "\r\n";
            }
            response += ("HTTP/1.0 200 OK");// 返回应答消息,并结束应答
            response += ("Content-Type: text/json");
            response += ("Content-Length:" + result.length());// 返回内容字节数
            response += ("Charsert:" + "GB2312");
            out.write(response.getBytes("GB2312"));
            out.write(result.getBytes("GB2312"));
            out.flush();
            out.close();
            reader.close();
            socket.close();
            System.out.println("socket closed.");
        }
    }

    //处理GET请求
    private void doGet(DataInputStream reader, OutputStream out) throws Exception
    {
        String fileName = "./httpserver/upload.html";
        if (new File(fileName).exists())
        {
            //从服务器根目录下找到用户请求的文件并发送回浏览器
            InputStream fileIn = new FileInputStream(fileName);
            byte[] buf = new byte[fileIn.available()];
            fileIn.read(buf);

            String response = ("HTTP/1.0 200 OK");// 返回应答消息,并结束应答
            response += ("Content-Type: text/html");
            response += ("Content-Length:" + fileIn.available());// 返回内容字节数
            response += ("Charsert:" + "UTF-8" + "\r\n");
            out.write(response.getBytes("GB2312"));
            out.write(buf);
            System.out.println("last " + buf[buf.length - 2] + buf[buf.length - 1]);
            out.flush();
            out.close();
            fileIn.close();
            reader.close();
            System.out.println("request complete.");
        }
    }

    public static void main(String args[])
    {
        try
        {
            MyServer server = new MyServer("", 8080);
            server.service();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}