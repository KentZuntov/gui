import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private Scanner scanner;
    private List<String> onlineKasutajad;

    public Client(String serverAddress) throws Exception {
            this.socket = new Socket("Localhost", 1337);
            this.scanner = new Scanner(System.in);
            this.start();
            this.onlineKasutajad = new ArrayList<>();
        }

    public static void main(String[] args) throws Exception {
        Client client = new Client("Localhost");
    }


    public void start() throws IOException {

        ClientTextOutput clientTextOutput = new ClientTextOutput(scanner, new DataOutputStream(socket.getOutputStream()));
        ClientTextInput clientTextInput = new ClientTextInput(new DataInputStream(socket.getInputStream()), scanner, onlineKasutajad);


        Thread thread1 = new Thread(clientTextOutput);
        thread1.start();
        Thread thread = new Thread(clientTextInput);
        thread.start();
        }

}
