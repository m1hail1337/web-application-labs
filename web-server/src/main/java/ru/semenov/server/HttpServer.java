package ru.semenov.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HttpServer {
    private static final String HOST = "localhost";
    private static final int PORT = 8080;
    private static final String PATH_TO_HTML_PAGES = "web-server\\src\\main\\resources\\html";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен. Подключиться: http://" + HOST + ":" + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Клиент присоединился");

                try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter output = new PrintWriter(socket.getOutputStream())) {
                    List<String> htmlPage = new ArrayList<>();
                    while (!input.ready()) ;
                    System.out.println();
                    while (input.ready()) {
                        String data = input.readLine();
                        if (data.startsWith("GET")) {
                            String filename = data.split(" ")[1];
                            Path pathToHtmlFile = Path.of(PATH_TO_HTML_PAGES + filename);
                            if (filename.length() > 1 && Files.exists(pathToHtmlFile)) {
                                htmlPage = Files.readAllLines(pathToHtmlFile);
                            }
                        }
                    }

                    output.println("HTTP/1.1 200 OK");
                    output.println("Content-Type: text/html; charset=utf-8");
                    for (String line : htmlPage) {
                        output.println(line);
                    }

                    output.flush();
                    System.out.println("Клиент отключился");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}