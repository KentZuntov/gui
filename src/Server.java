import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private boolean serverOn = false;
    private int clientsServed = 0;
    private List<ClientHandler> connectedClients;

    public Server() throws IOException {
        this.connectedClients = new ArrayList<>();
        this.startServer();
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
        } catch (IOException e) {
            throw new RuntimeException("Serverit ei õnnestunud käivitada: " + e);
        }
    }

    private void startServer() throws IOException {
        this.serverOn = true;
        ServerSocket serverSocket = new ServerSocket(1337);
        System.out.println("Server alustas tööd!");

        try {
            while(serverOn) {
                ClientHandler clientHandler = new ClientHandler(serverSocket, ++clientsServed, this);
                //Thread clientThread = new Thread(clientHandler);
                //clientThread.start();
                clientHandler.start();
                connectedClients.add(clientHandler);
                System.out.println("Client id-ga: " + clientsServed + " liitus serveriga!");
            }
        } finally {
            serverSocket.close();
        }
    }

    public List<ClientHandler> getConnectedClients() {
        return connectedClients;
    }


    public void removeFromConnectedClients(ClientHandler clientHandler){
        connectedClients.remove(clientHandler);
    }


}
