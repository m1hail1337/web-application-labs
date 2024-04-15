package ru.semenov.smtp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SmtpClient {

    private static final String GOOGLE_MAIL_IP_ADDRESS = "104.237.130.88";
    private static final int PORT = 25;

    public static void main(String[] args) {
        testSmtpClient("<mihail@gmail.com>", "<alice@mail.ru>");
    }

    private static void testSmtpClient(String sender, String recipient) {
        try (Socket socket = new Socket(GOOGLE_MAIL_IP_ADDRESS, PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String response = in.readLine();
            if (!response.startsWith("220")) {
                System.out.println("код 220 от сервера не получен");
            }
            System.out.println(response);

            send(out, in, "HELO Alice\r\n", 250);
            send(out, in, "MAIL FROM: " + sender + "\r\n", 250);
            send(out, in, "RCPT TO: " + recipient + "\r\n", 250);
            send(out, in, "DATA\r\nFrom: " + sender + "\r\nTo: " + recipient + "\r\nSubject: messages \r\n", 354);

            String dataMessage = "\r\n Do you do a homework?";
            out.printf(dataMessage);

            send(out, in, "\r\n.\r\n", 250);
            send(out, in, "QUIT\r\n", 221);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void send(PrintWriter out, BufferedReader in, String message, int expectedCode) throws Exception {
        out.printf(message);
        String response = in.readLine();
        if (!response.startsWith("" + expectedCode)) {
            System.out.println("код "+ expectedCode + " от сервера не получен");
        }
        System.out.println(response);
    }
}
