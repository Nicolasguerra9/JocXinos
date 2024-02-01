import ClientData.Client;

import javax.lang.model.type.ArrayType;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final int PORT = 5000;

    public static void main(String[] args) {

        int totalOfCoins = 0;
        int clients = 0;
        boolean firstClient = true;

        try {

            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Servidor obert esperant conexions ...");

            ArrayList<Socket> socketClients = new ArrayList<Socket>();
            ArrayList<Client> clientsArray = new ArrayList<Client>();

            for (int index = 0; index < 2; index++) {

                if (firstClient) {

                    firstClient = false;

                    Socket first = server.accept();

                    sendClientsMessage(first);

                    clients = getClients(first);

                } else {

                    for (int i = 0; i < clients; i++) {
                        Socket socketClient = server.accept();
                        System.out.println(socketClient.getInetAddress().getHostAddress());
                        socketClients.add(socketClient);

                        Client client;

                        client = getClientInformation(socketClient);

                        clientsArray.add(client);

                    }

                    totalOfCoins = getTotalOfCoins(clientsArray);

                    sendFinalMessage(socketClients, clientsArray, totalOfCoins);

                    for (int i = 0; i < socketClients.size(); i++) {

                        socketClients.get(i).close();

                    }

                }


            }

            server.close();

        } catch (Exception e) {

            System.out.println(e.toString());
        }

    }


    private static void sendClientsMessage(Socket first) {

        String msgClient = "Introdueix el número de clients";

        try {

            OutputStream os = first.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(msgClient);

        } catch (Exception e) {

            System.out.println(e);

        }


    }

    private static int getClients(Socket first) {

        int numClients = 1;

        try {

            InputStream is = first.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            numClients = ois.readInt();

        } catch (Exception e) {

            System.out.println(e);

        }
        return numClients;

    }

    private static Client getClientInformation(Socket socketClient) {

        Client sendClient = null;

        try {

            InputStream is = socketClient.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            sendClient = (Client) ois.readObject();

        } catch (Exception e) {

            System.out.println(e);

        }

        return sendClient;

    }

    private static int getTotalOfCoins(ArrayList<Client> clientsArray) {

        int coinsToReturn = 0;

        for (int i = 0; i < clientsArray.size(); i++) {

            coinsToReturn = coinsToReturn + clientsArray.get(i).getCoins();

        }

        return coinsToReturn;
    }

    private static void sendFinalMessage(ArrayList<Socket> socketClients, ArrayList<Client> clientsArray, int totalOfCoins) {

        ArrayList<String> winners = new ArrayList<String>();

        for (int i = 0; i < clientsArray.size(); i++) {

            if (clientsArray.get(i).getTotalCoins() == totalOfCoins) {

                winners.add(clientsArray.get(i).getName());

            }

        }

        sendResult(socketClients, clientsArray, winners);

        try {

            for (int i = 0; i < socketClients.size(); i++) {
                OutputStream os = socketClients.get(i).getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeObject(winners);
            }

        } catch (Exception e) {

            System.out.println(e);

        }

    }

    private static void sendResult(ArrayList<Socket> socketClients, ArrayList<Client> clientsArray, ArrayList<String> winners) {

        String msg = null;

        for (int i = 0; i < clientsArray.size(); i++) {

            if (winners.get(i).equals(clientsArray.get(i).getName())) {

                msg = "Has ganado";

            } else {

                msg = "Has perdido";

            }

        }

        try {

            for (int i = 0; i < socketClients.size(); i++) {
                OutputStream os = socketClients.get(i).getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeObject(msg);
            }

        } catch (Exception e) {

            System.out.println(e);

        }

    }
}
