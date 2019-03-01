package org.academiadecodigo.org;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by codecadet on 28/02/2019.
 */
public class WebServer {

    private int portNumber;
    private Socket client;
    private BufferedReader in;
    private DataOutput dataOut;
    private double byteSize;
    private File file;
    private String str;
    private String getRequest;
    private String extension;
    private String path = "/";
    private String type;
    private double size;


    public WebServer(int portNumber) throws IOException {
        this.portNumber = portNumber;
        start();
    }


    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(portNumber);

        while (true) {
            client = serverSocket.accept();
            System.out.println("Connected");
            streams();
            client.close();

        }

    }

    public void streams() throws IOException {

        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        getRequest = in.readLine();

        System.out.println(getRequest);

        dataOut = new DataOutputStream(client.getOutputStream());

        dataWrite();


    }


    public String header() {

        str = "HTTP/1.0 200 Document Follows\r\n" +
                "Content-Type: " + type + "; charset=UTF-8\r\n" +
                "Content-Length: " + byteSize + "\r\n" +
                "\r\n";
        return str;

    }

    public String notFound() {

        String str = "HTTP/1.0 404 Not Found\r\n" +
                "Content-Type: text/html; charset=UTF-8\r\n" +
                "Content-Length: " + size + " \r\n" +
                "\r\n";

        return str;
    }


    public void dataWrite() throws IOException {
        if (pathRequest().equals("/")) {
            path = "/Index.html";
        }

        file = new File("www" + path);

        if (file.canRead()) {

            this.byteSize = file.length();
            dataOut.writeBytes(header());

            FileInputStream fileInput = new FileInputStream(file);

            byte[] fileContent = new byte[1024];
            while (fileInput.read(fileContent) != -1) {
                dataOut.write(fileContent);
            }


        } else {

            file = new File("www/404Error.html");
            size = file.length();
            System.out.println("is there a path:" + file.canRead());

            dataOut.writeBytes(notFound());

            System.out.println(notFound());

            FileInputStream fileInput = new FileInputStream(file);
            byte[] fileContent = new byte[1024];
            while (fileInput.read(fileContent)!= -1){
                dataOut.write(fileContent);
            }
           
        }


    }

    public String pathRequest() {
        String[] arr;
        arr = getRequest.split(" ");
        System.out.println(arr[1]);
        path = arr[1];
        return arr[1];
    }

    public String extension() {
        extension = path.substring(path.lastIndexOf("."));
        System.out.println("extensao " + extension);
        fileType();
        return extension;

    }

    public void fileType() {
        switch (extension) {
            case ".html":
                type = "text/html";
                break;
            case ".jpg":
                type = "image/jpg";
                break;
            case ".jpeg":
                type = "image/jpeg";
                break;
            case ".css":
                type = "image/css";
                break;
            case ".gif":
                type = "image/gif";
                break;
            case ".png":
                type = "image/png";
                break;
            case ".wave":
                type = "audio/wave";
                break;
            case ".ogg":
                type = "video/jpeg";
                break;

        }

    }


    public static void main(String[] args) throws IOException {
        new WebServer(8080);

    }

}
