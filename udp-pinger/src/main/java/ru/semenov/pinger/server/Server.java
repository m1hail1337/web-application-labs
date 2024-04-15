package ru.semenov.pinger.server;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.*;

public class Server extends Thread {

    private DatagramSocket socket;
    private boolean running = true;
    private byte[] buf = new byte[1024];
    private final Random random = new Random();

    public Server() throws SocketException {
        socket = new DatagramSocket(4445);
    }

    public void run() {
        running = true;
        try {
            while (running) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println("[Сервер] Сервер получил сообщение: " + received);
                if (random.nextInt(10) < 3) {
                    continue;
                }
                byte[] response = ("Resp " + received.split("")[received.length() - 1]).getBytes(Charset.defaultCharset());
                for (int i = 0; i < response.length; i++) {
                    buf[i] = response[i];
                }
                System.arraycopy(response, 0, buf, 0, response.length);
                packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
            }
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        running = false;
    }
}
