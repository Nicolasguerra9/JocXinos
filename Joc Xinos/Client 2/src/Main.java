import ClientData.Client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static final int PORT = 5000;
    public static final int USERTIMES = 2;

    public static void main(String[] args) {

        boolean first = true;
        Client client;
        String ipClient = null;
        String nameClient = null;
        int coins = (int) (Math.random() * (3 + 1) + 0);
        int totalcoins = 0;

        try {

            for (int i = 0; i < USERTIMES; i++) {

                System.out.print("IP del servidor: ");
                String ip = "localhost";
                ipClient = ip;

                Socket socket = new Socket(ip, PORT);
                System.out.println("Conexió amb el servidor establerta");

                if (first) {

                    first = false;

                    showMessageClients(socket);

                    sendNumClients(socket);

                } else {

                    nameClient = getNameClient();
                    totalcoins = getTotalCoins();
                    client = new Client(ipClient, nameClient, coins, totalcoins);

                    sendClientInformation(socket, client);

                    getResults(socket);

                    getListOfWinners(socket);

                }

                closeConection(socket);

            }
        } catch (Exception e) {

            System.out.println(e.toString());
        }



    }

    private static void showMessageClients(Socket socket) {

        String msg = null;

        try {
            InputStream is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            msg = (String) ois.readObject();
        } catch (Exception e) {

            System.out.println(e);

        }

        System.out.println(msg);

    }



    public static void sendNumClients(Socket socket) {

        try {

            int num = new Scanner(System.in).nextInt();
            num = num - 1;

            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(num);

        } catch (Exception e) {

            System.out.println(e.toString());
        }

    }

    private static String getNameClient() {

        System.out.println("Introduce tu nombre");
        String name = new Scanner(System.in).nextLine();

        return name;

    }

    private static int getTotalCoins () {

        System.out.println("Di quantas monedas crees que hay");
        int totalcoins = new Scanner(System.in).nextInt();

        return totalcoins;

    }

    private static void sendClientInformation(Socket socket, Client client) {

        try {

            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(client);

        } catch (Exception e) {

            System.out.println(e);

        }

    }

    private static void getResults (Socket socket) {

        String msg = null;

        try {

            InputStream is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            msg = (String) ois.readObject();

        } catch (Exception e) {

            System.out.println(e);

        }

    }

    private static void getListOfWinners (Socket socket) {

        String winnersText = "Los ganadores son: \n";
        ArrayList<String> winners = new ArrayList<String>();

        try {

            InputStream is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            winners = (ArrayList<String>) ois.readObject();

        } catch (Exception e) {

            System.out.println(e);

        }

        if (winners.size() >= 1) {

            System.out.println(winnersText);

            for (int i = 0; i < winners.size(); i++) {

                System.out.println((i+1) + ": " + winners.get(i));

            }
        } else {

            System.out.println("Nadie ha ganado");

        }

    }

    private static void closeConection(Socket socket) {
        try {

            socket.close();

        } catch (Exception e) {

            System.out.println(e.toString());

        }
    }
}
