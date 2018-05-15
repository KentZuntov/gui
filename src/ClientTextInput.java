import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ClientTextInput implements Runnable{
    private DataInputStream dataInputStream;
    private Scanner scanner;
    private List<String> onlineKasutajad;

    public ClientTextInput(DataInputStream dataInputStream, Scanner scanner, List<String> onlineKasutajad) {
        this.dataInputStream = dataInputStream;
        this.scanner = scanner;
        this.onlineKasutajad = onlineKasutajad;
    }

    @Override
    public void run() {
        while (true){
            String message = null;
            try {
                message = dataInputStream.readUTF();
                if (message.startsWith("ListOfPeople")){
                    String list = message.replace("ListOfPeople ", "");
                    onlineKasutajad = Arrays.asList(list.split(" "));

                } else {
                    System.out.println(message);
                }
                System.out.println(onlineKasutajad);
            } catch (IOException e) {
                throw new RuntimeException(message + e);
            }
        }
    }
}
