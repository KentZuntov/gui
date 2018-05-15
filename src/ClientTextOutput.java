import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class ClientTextOutput implements Runnable {
    private Scanner scanner;
    private DataOutputStream dataOutputStream;

    public ClientTextOutput(Scanner scanner, DataOutputStream dataOutputStream) {
        this.scanner = scanner;
        this.dataOutputStream = dataOutputStream;
    }

    @Override
    public void run() {
        System.out.println("Sisesta kasutajanimi: ");
        try {
            dataOutputStream.writeUTF("login " + scanner.nextLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            dataOutputStream.writeUTF("people ");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        while (true) {
            String tokens = scanner.nextLine();
            try {
                dataOutputStream.writeUTF(tokens);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
