import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class ClientTextInput implements Runnable{
    private final Client client;
    private DataInputStream dataInputStream;
    private Scanner scanner;
    private List<String> onlineKasutajad;



    public ClientTextInput(DataInputStream dataInputStream, Scanner scanner, List<String> onlineKasutajad, Client client) {
        this.dataInputStream = dataInputStream;
        this.scanner = scanner;
        this.onlineKasutajad = onlineKasutajad;
        this.client = client;
    }

    @Override
    public void run() {
        while (true){
            try {
                String message = dataInputStream.readUTF();
                if (message.startsWith("ListOfPeople")){
                    String list = message.replace("ListOfPeople ", "");
                    onlineKasutajad = Arrays.asList(list.split(" "));
                    client.updateUserList(onlineKasutajad);
                    //System.out.println("Hetkel on online: " + onlineKasutajad);

                } else {
                    //laused.put(message);
                    if (!message.equals(""))
                        client.receiveMessage(message);

                    //System.out.println(message);

                }
            }
            catch (IOException e) {
                throw new RuntimeException("inputStream on suletud" + e);
            }
        }
    }
}
