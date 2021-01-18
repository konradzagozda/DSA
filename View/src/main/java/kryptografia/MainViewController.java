package kryptografia;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    public Button generateParamsBtn;
    public TextArea PText;
    public TextArea QText;
    public TextArea GText;
    public TextArea YText;
    public TextArea messageCopiedText;
    public Button verifyBtn;
    public Button verifyFileBtn;
    public TextArea rCopiedText;
    public TextArea sCopiedText;
    public TextArea MessageText;
    public Button signBtn;
    public Button signFileBtn;
    public TextArea rCreatedText;
    public TextArea sCreatedText;

    Alert a = new Alert(Alert.AlertType.NONE);

    private Alice alice;
    private Bob bob;
    private List<byte[]> encrypted;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void generateParams(ActionEvent actionEvent) {
        alice = new Alice();
        bob = new Bob(alice.getP(), alice.getQ(), alice.getG(), alice.getY());

        PText.setText(alice.getP().toString());
        GText.setText(alice.getG().toString());
        QText.setText(alice.getQ().toString());
        YText.setText(alice.getY().toString());

        signBtn.setDisable(false);
        signFileBtn.setDisable(false);
    }

    public void signMessage(ActionEvent actionEvent) {
        var signature = alice.sign(MessageText.getText().getBytes());
        sCreatedText.setText(signature[1].toString());
        sCopiedText.setText(signature[1].toString());
        rCreatedText.setText(signature[0].toString());
        rCopiedText.setText(signature[0].toString());
    }

    public void signFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        try {
            byte[] fileBytes = FileUtils.readFileToByteArray(selectedFile);
            var signature = alice.sign(fileBytes);
            sCreatedText.setText(signature[1].toString());
            sCopiedText.setText(signature[1].toString());
            rCreatedText.setText(signature[0].toString());
            rCopiedText.setText(signature[0].toString());
        } catch (IOException e) {
            e.printStackTrace();
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText("Couldn't sign file.");
            a.show();
        }
    }

    public void verifyFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        BigInteger r = new BigInteger(rCopiedText.getText());
        BigInteger s = new BigInteger(sCopiedText.getText());
        byte[] message = new byte[0];
        try {
            message = FileUtils.readFileToByteArray(selectedFile);
            if(bob.verifySignature(r, s, message)){
                a.setAlertType(Alert.AlertType.INFORMATION);
                a.setContentText("Successfully verfied!");
                a.show();
            }else {
                a.setAlertType(Alert.AlertType.WARNING);
                a.setContentText("Signature doesn't match file!");
                a.show();
            };
        } catch (IOException e) {
            e.printStackTrace();
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText("Couldn't verify file.");
            a.show();
        }
    }

    public void verifyMessage(ActionEvent actionEvent) {
        BigInteger r = new BigInteger(rCopiedText.getText());
        BigInteger s = new BigInteger(sCopiedText.getText());
        byte[] message = messageCopiedText.getText().getBytes();

        if(bob.verifySignature(r, s, message)){
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setContentText("Successfully verfied!");
            a.show();
        }else {
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText("Signature doesn't match message!");
            a.show();
        };
    }
}
