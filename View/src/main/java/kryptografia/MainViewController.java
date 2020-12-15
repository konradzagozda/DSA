package kryptografia;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    public Button generateParamsBtn;
    public TextArea bobsPublicKeyText;
    public TextArea alfaText;
    public TextArea primeText;
    public Button publishParamsBtn;
    public Button computeKeysBtn;
    public TextArea alicesPublicKeyText;
    public TextArea receivedMessageText;
    public Button decryptBtn;
    public Button decryptFileBtn;
    public TextArea decryptedMessageText;
    public TextArea rawMessageText;
    public Button encryptBtn;
    public Button encryptFileBtn;
    public TextArea encryptedMessageText;
    public Button sendBtn;

    Alert a = new Alert(Alert.AlertType.NONE);

    private Bob bob;
    private Alice alice;
    private List<byte[]> encrypted;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void generateParams(ActionEvent actionEvent) {
        bob = new Bob();
        alice = new Alice(bob.getPrime(), bob.getAlfa(), bob.getBobsPublicKey());

        bobsPublicKeyText.setText(bob.getBobsPublicKey().toString());
        primeText.setText(bob.getPrime().toString());
        alfaText.setText(bob.getAlfa().toString());

        publishParamsBtn.setDisable(false);
    }

    public void publishParams(ActionEvent actionEvent) {
        computeKeysBtn.setDisable(false);
    }

    public void computeAlicesKeys(ActionEvent actionEvent) {
        alicesPublicKeyText.setText(alice.getAlicesPublicKey().toString());

        encryptBtn.setDisable(false);
        encryptFileBtn.setDisable(false);
        generateParamsBtn.setDisable(true);
        publishParamsBtn.setDisable(true);
        decryptFileBtn.setDisable(false);
    }

    public void sendToBob(ActionEvent actionEvent) {
        bob.computeMaskingKey(alice.getAlicesPublicKey());

        StringBuilder str = new StringBuilder();
        for (var part : encrypted) {
            str.append(Utils.bytesToHex(part));
        }
        receivedMessageText.setText(str.toString());

        decryptBtn.setDisable(false);
        decryptFileBtn.setDisable(false);
    }

    public void encrypt(ActionEvent actionEvent) {
        encrypted = alice.encode(rawMessageText.getText().getBytes());

        StringBuilder str = new StringBuilder();
        for (var part : encrypted) {
            str.append(Utils.bytesToHex(part));
        }
        encryptedMessageText.setText(str.toString());
        sendBtn.setDisable(false);
    }

    public void decrypt(ActionEvent actionEvent) {
        var decrypted = bob.decode(encrypted);
        decryptedMessageText.setText(new String(decrypted));
    }

    public void decryptFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        try {
            FileInputStream file = new FileInputStream(selectedFile);
            ObjectInputStream in = new ObjectInputStream(file);

            List<byte[]> cryptogram = (List<byte[]>)in.readObject();
            var decodedBytes = bob.decode(cryptogram);

            File destination = fileChooser.showSaveDialog(new Stage());
            FileUtils.writeByteArrayToFile(destination, decodedBytes);

            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setContentText("File decrypted successfully");
            a.show();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText("File decryption failed");
            a.show();
        }
    }

    public void encryptFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        try {
            byte[] fileBytes = FileUtils.readFileToByteArray(selectedFile);
            List<byte[]> encodedBytes = alice.encode(fileBytes);
            File destination = fileChooser.showSaveDialog(new Stage());

            FileOutputStream file = new FileOutputStream(destination);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(encodedBytes);
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setContentText("File encrypted successfully");
            a.show();

        } catch (IOException e) {
            e.printStackTrace();
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText("File encryption failed");
            a.show();
        }
    }


}
