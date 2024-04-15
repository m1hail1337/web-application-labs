package ru.semenov.pinger;

import java.net.*;

import ru.semenov.pinger.client.Client;
import ru.semenov.pinger.server.Server;

public class UdpPingerMain {
    public static void main(String[] args) throws SocketException, InterruptedException {

        Server server = new Server();
        server.start();
        Client client = new Client();
        for (int i = 0; i < 10; i++) {
            client.sendMessage("Ping " + i);
            Thread.sleep(500);
        }
        server.close();
        System.out.println("Клиент отправил 10 сообщений");
        System.exit(0);
    }
}
