package ru.semenov.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class ProxyServer {

    private static final int HTTP_PORT = 80;
    private static final int PROXY_PORT = 8888;

    public static void main(String[] args) {
        startProxyServer();
    }

    private static void startProxyServer() {
        boolean connected = false;
        String hostName = "localhost";
        while (true) {
            try (ServerSocket serverSocket = new ServerSocket(PROXY_PORT);
                Socket socket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String requestFromClient = in.readLine();
                System.out.println("From client: \n" + requestFromClient + "\n");
                String[] elementsOfRequest = requestFromClient.split(" ");

                String reqFile = elementsOfRequest[1];
                if (reqFile.charAt(0) == '/' && reqFile.length() > 1) {
                    reqFile = reqFile.substring(1);
                }

                String requestToServer;
                String response;

                if (connected) {
                    requestToServer = "GET " + reqFile + " HTTP/1.1\r\nHost: " + hostName + "\r\n\r\n";
                } else {
                    connected = true;
                    hostName = reqFile;
                    requestToServer = "GET / HTTP/1.1\r\nHost: " + reqFile + "\r\n\r\n";
                }

                response = sendRequest(hostName, requestToServer);
                System.out.println(response + "\n");

                out.println(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String getMessageContent(BufferedReader in) throws IOException {
        StringBuilder message = new StringBuilder();
        String line;
        while (!Objects.equals(line = in.readLine(), null)) {
            message.append(line).append("\r\n");
            System.out.println(line);
            if (line.equals("0")) {
                break;
            }
        }
        return message.toString();
    }

    private static String sendRequest(String host, String request) {
        try (Socket socket = new Socket(host, ProxyServer.HTTP_PORT)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.printf(request);
            return getMessageContent(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
