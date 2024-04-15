package ru.semenov.icmp;

import java.io.IOException;
import java.net.InetAddress;

public class Pinger {

    private static final String HOST_TO_PING = "www.vk.com";
    private static final int N_PACKETS = 4;
    private static final int TIMEOUT = 1000;    // ms

    public static void main(String[] args) {
        try {
            InetAddress address = InetAddress.getByName(HOST_TO_PING);
            int receivedCount = 0;
            long totalRTT = 0;
            long minRTT = Long.MAX_VALUE;
            long maxRTT = Long.MIN_VALUE;

            System.out.println("PING " + HOST_TO_PING + ":");

            for (int i = 0; i < N_PACKETS; i++) {
                long startTime = System.currentTimeMillis();

                try {
                    if (address.isReachable(TIMEOUT)) {
                        long rtt = System.currentTimeMillis() - startTime;
                        receivedCount++;
                        totalRTT += rtt;
                        minRTT = Math.min(minRTT, rtt);
                        maxRTT = Math.max(maxRTT, rtt);
                        System.out.println("Reply from " + address.getHostAddress() + ": time=" + rtt + "ms");
                    } else {
                        System.out.println("Request timed out");
                    }
                } catch (IOException e) {
                    System.out.println("Error: " + e.getMessage());
                }

                try {
                    Thread.sleep(1000); // time to next req
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            int lossPercentage = (int) (1.0 - (double) receivedCount / N_PACKETS) * 100;
            double avgRTT = (double) totalRTT / receivedCount;

            System.out.println("\n--- " + HOST_TO_PING + " ping statistics ---");
            System.out.println(N_PACKETS + " packets transmitted, " + receivedCount + " received, " +
                               String.format("%d%% packet loss", lossPercentage));
            System.out.println("rtt min/avg/max = " + minRTT + "/" + avgRTT + "/" + maxRTT + " ms");

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
