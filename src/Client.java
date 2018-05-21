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
    private ClientGui clientGui;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;


    public Client(String serverAddress, ClientGui clientGui) throws Exception {
        this.socket = new Socket(serverAddress, 1337);
        this.scanner = new Scanner(System.in);
        this.startClient();
        this.onlineKasutajad = new ArrayList<>();
        this.clientGui = clientGui;
    }


    public void startClient() throws IOException {

        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        //ClientTextOutput clientTextOutput = new ClientTextOutput(scanner, dataOutputStream);
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        ClientTextInput clientTextInput = new ClientTextInput(dataInputStream, scanner, onlineKasutajad, this);


        //Thread thread1 = new Thread(clientTextOutput);
        //thread1.start();
        Thread thread = new Thread(clientTextInput);
        thread.start();
    }

    public void sendMessage(String message) throws IOException {
        dataOutputStream.writeUTF(message);
    }

    public void receiveMessage(String message){
        clientGui.updateTextArea(message);
    }

    public void updateUserList(List<String> asi){
        clientGui.updateKasutajateListView(asi.toString());
    }

}
