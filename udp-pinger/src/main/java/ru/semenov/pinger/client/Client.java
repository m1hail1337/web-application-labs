package ru.semenov.pinger.client;

import java.io.*;
import java.net.*;

public class Client {

    private static final String HOST = "localhost";
    private static final int PORT = 4445;

    private final DatagramSocket socket;
    private final InetAddress address;

    public Client() {
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(1000);  // 1 second
            address = InetAddress.getByName(HOST);
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String msg) throws SocketException {
        byte[] buf = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, PORT);
        try {
            long start = System.currentTimeMillis();
            System.out.println("[Клиент] Сообщение \" " + msg + " \" отправлено");
            socket.send(packet);
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            long duration = System.currentTimeMillis() - start;
            System.out.println("[Клиент] Получено сообщение от сервера [" + duration  + "ms] : \"" + new String(packet.getData(), 0, packet.getLength()) + "\"");
        } catch (SocketTimeoutException e) {
            System.out.println("[Клиент] Request timed out");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
