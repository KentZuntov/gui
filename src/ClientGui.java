import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

import static javafx.collections.FXCollections.observableArrayList;


public class ClientGui extends Application {
    private TextArea textArea = new TextArea();
    private ListView kasutajateListView = new ListView();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Client client = new Client("Localhost", this);
        primaryStage.setTitle("Facebook Xtra-Lite chat");

        //1st window

        Stage teineStage = new Stage();
        teineStage.setTitle("Facebook Xtra-Lite LogIn");
        BorderPane teineBorderpane = new BorderPane();
        Scene teineTseen = new Scene(teineBorderpane, 300, 300, Color.SNOW);
        Text tereTulemastTekst = new Text("Facebook Xtra-Lite");
        tereTulemastTekst.setTextAlignment(TextAlignment.CENTER);
        HBox hBox1 = new HBox();
        TextField kasutajanimeTextField = new TextField("Enter your username");
        Button logiSisseButton = new Button("Log in");
        logiSisseButton.setAlignment(Pos.CENTER);

        teineBorderpane.setTop(tereTulemastTekst);
        teineBorderpane.setCenter(kasutajanimeTextField);
        teineBorderpane.setBottom(logiSisseButton);
        teineBorderpane.setPadding(new Insets(40));

        teineStage.resizableProperty().setValue(false);
        teineStage.setScene(teineTseen);
        teineStage.show();


        //2nd window

        BorderPane borderPane = new BorderPane();

        HBox hBox = new HBox();
        TextField textField = new TextField("sisesta tekst siia");
        textField.setPrefWidth(450);
        Button button = new Button("Send");
        button.setPrefWidth(70);
        hBox.getChildren().addAll(textField, button);
        hBox.setAlignment(Pos.CENTER);

        primaryStage.setMinWidth(520);
        primaryStage.setMinHeight(500);

        textArea.setPrefHeight(450);
        textArea.setPadding(new Insets(10));

        kasutajateListView.setPrefWidth(100);
        kasutajateListView.setPrefHeight(70);


        borderPane.setCenter(textArea);
        borderPane.setBottom(hBox);
        borderPane.setLeft(kasutajateListView);

        Scene tseen = new Scene(borderPane, 420, 500, Color.SNOW);
        primaryStage.setScene(tseen);
        //primaryStage.show();

        //events

        logiSisseButton.setOnAction(event -> {
            String usernameOfChoice = kasutajanimeTextField.textProperty().getValue();
            if (!usernameOfChoice.equals("")){
                try {
                    client.sendMessage("login " + usernameOfChoice);
                    client.sendMessage("people ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                primaryStage.show();
                teineStage.close();
            }
        });

        button.setOnAction(event -> {
            String message = textField.textProperty().getValue();
            forwardMsg(message, client, textField);
        });


        textField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)){
                String message = textField.textProperty().getValue();
                forwardMsg(message, client, textField);
            }
        });

        teineStage.setOnCloseRequest(event -> clientClose(client));


        primaryStage.setOnCloseRequest(event -> {
            clientClose(client);
        });

    }

    private void clientClose(Client client) {
        try {
            client.sendMessage("logoff");
            Platform.exit();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void forwardMsg(String message, Client client, TextField textField) {
        if (!message.equals("")) {
            try {
                client.sendMessage("broadcast " + message);
            } catch (IOException e) {
                e.printStackTrace();
            }
            textArea.appendText("\n" + "You: " + message);
            textField.textProperty().setValue("");
        }
    }


    public void updateTextArea(String message){
        this.textArea.appendText("\n" + message);
    }

    public void updateKasutajateListView(String listiMoodiAsi){
        ObservableList<String> items = observableArrayList(listiMoodiAsi.substring(1,listiMoodiAsi.length()-1).split(", "));
        this.kasutajateListView.setItems(items);
    }


}
