import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private int clientId;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private Server server;
    private String username;


    public ClientHandler(ServerSocket serverSocket, int clientId, Server server) throws IOException {
        this.clientSocket = serverSocket.accept();
        this.clientId = clientId;
        this.server = server;
        this.username = null;
    }


    @Override
    public void run() {
        try {
            this.dataInputStream = new DataInputStream(clientSocket.getInputStream());
            this.dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());


            while (true) {
                String[] cmdMsg = dataInputStream.readUTF().split(" ", 2);

                switch (cmdMsg[0]){
                    case "login":
                        this.username = cmdMsg[1];
                        dataOutputStream.writeUTF("Tere tulemast " + this.username + "! Alusta suhtlemist");
                        StringBuilder stringBuilder = new StringBuilder();
                        for (ClientHandler connectedClients : server.getConnectedClients()) {
                            stringBuilder.append(connectedClients.getUsername()).append(" ");
                        }
                        for (ClientHandler connectedClient : server.getConnectedClients()) {
                            if (!connectedClient.equals(this)){
                                connectedClient.dataOutputStream.writeUTF("ListOfPeople " + stringBuilder.toString());
                            }
                        }
                        break;

                    case "broadcast":
                        for (ClientHandler connectedClient : server.getConnectedClients()) {
                            if (!connectedClient.equals(this)){
                                connectedClient.dataOutputStream.writeUTF(username + ": " + cmdMsg[1]);
                            }
                        }
                        break;
                    case "private":
                        String[] vastuvõtjaMsg = cmdMsg[1].split(" ", 2);
                        for (ClientHandler connectedClient : server.getConnectedClients()) {
                            if (connectedClient.getUsername().equals(vastuvõtjaMsg[0])){
                                connectedClient.dataOutputStream.writeUTF(username + ": " + vastuvõtjaMsg[1]);
                            }
                        }
                        break;

                    case "people":
                        StringBuilder sb = new StringBuilder();
                        for (ClientHandler connectedClient : server.getConnectedClients()) {
                            sb.append(connectedClient.getUsername()).append(" ");
                        }
                        dataOutputStream.writeUTF("ListOfPeople " + sb.toString());
                        break;
                    case "logoff":
                        server.removeFromConnectedClients(this);
                        //TODO streamid?
                        StringBuilder sb1 = new StringBuilder();
                        for (ClientHandler connectedClient : server.getConnectedClients()) {
                            sb1.append(connectedClient.getUsername()).append(" ");
                        }
                        for (ClientHandler connectedClient : server.getConnectedClients()) {
                            connectedClient.dataOutputStream.writeUTF("ListOfPeople " + sb1.toString());
                        }
                    default:
                        dataOutputStream.writeUTF("tundmatu käsk!");
                        break;
                }
            }



        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                dataOutputStream.close();
                dataInputStream.close();
            } catch (IOException e) {
                System.err.print("Clienti streame ei õnnestunud korralikult sulgeda!");
            }

        }

    }

    public String getUsername() {
        return username;
    }

    private String kasutajanimeKontroll(String soovitudNimi) {
        for (ClientHandler connectedClient : server.getConnectedClients()) {
            if (connectedClient.getUsername() != null && connectedClient.getUsername().equals(soovitudNimi)) {
                return null;
            }
        }
        return soovitudNimi;
    }
}
