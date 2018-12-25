package ba.unsa.etf.rpr;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class ProzorController {


    public TextField naziv;
    public TextField drzava;
    public TextField brojStanovnika;
    private GeografijaDAO geo=GeografijaDAO.getInstance();
    Grad grad;

    HashMap<Control, Boolean> greske = new HashMap<>();


    boolean greskaO = false;


    public ProzorController(Grad g){

        grad=g;

    }
    @FXML
    public void initialize(){
        if(grad!=null){
            naziv.setText(grad.getNaziv());
            if(grad.getDrzava()!=null) drzava.setText(grad.getDrzava().getNaziv());
            else drzava.setText("Nepoznato");
            brojStanovnika.setText(grad.getBrojStanovnika()+"");
            greske.put(naziv, false);
            greske.put(drzava,false);
            greske.put(brojStanovnika,false);

        }

    }


    public void submit(ActionEvent actionEvent) {
        if (greske.containsValue(true) || (!greske.containsKey(naziv)) || !greske.containsKey(drzava)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("POGREŠAN UNOS");
            if (greske.containsValue(true)) alert.setHeaderText("Neispravni podaci");
            else alert.setHeaderText("Nepotpuni podaci");
            alert.setContentText("Unesite ponovo!");

            alert.showAndWait();
        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Insert ");
            alert.setHeaderText("Grad zapisan");
            alert.setContentText("Uspješan unos!");

            Grad a=new Grad(naziv.getText(),null,Integer.parseInt(brojStanovnika.getText()),0);
            Drzava m=new Drzava(drzava.getText(),a,1);
            a.setDrzava(m);

            if(grad==null) Platform.runLater(()->{geo.dodajGrad(a);});
            else {  a.setId(grad.getId());     Platform.runLater(()->{geo.izmijeniGrad(a);});    }

            alert.showAndWait();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();

        }
    }



    private boolean oboji(TextField ime) {

                       int n = ime.getText().length();
        for (int i = 0; i < n; i++) {
            if (!(Character.isLetter(ime.getText().charAt(i)) || Character.isWhitespace(ime.getText().charAt(i))))
                greskaO = true;
            else greskaO=false;
        }
        if (ime.getText().length() < 1 || ime.getText().length() > 20) greskaO = true;
        if (greskaO) ime.setStyle("-fx-background-color: rgba(255,45,86,0.74)");

        else {
            ime.setStyle("-fx-background-color: rgba(72,128,85,0.4)");
            greskaO = false;
        }

        return greskaO;
    }

    public void unosNaziv(KeyEvent keyEvent) {
        if (!greske.containsKey(drzava)) greske.put(naziv, false);
        greske.replace(naziv, oboji(naziv));
    }

    public void unosDrzava(KeyEvent keyEvent) {
        if (!greske.containsKey(drzava)) greske.put(drzava, false);
        greske.replace(drzava, oboji(drzava));
    }


    public void unosBrStanovnika(KeyEvent keyEvent) {
        boolean greskaO = false;

        int n = brojStanovnika.getText().length();
        for (int i = 0; i < n; i++) {
            if (!(Character.isDigit(brojStanovnika.getText().charAt(i)) || Character.isWhitespace(brojStanovnika.getText().charAt(i))))
                greskaO = true;
            else greskaO=false;
        }
        if (brojStanovnika.getText().length() < 1 || brojStanovnika.getText().length() > 20) greskaO = true;
        if (greskaO) brojStanovnika.setStyle("-fx-background-color: rgba(255,45,86,0.74)");

        else {
            brojStanovnika.setStyle("-fx-background-color: rgba(72,128,85,0.4)");
            greskaO = false;
        }

        if (!greske.containsKey(brojStanovnika)) greske.put(brojStanovnika, false);
        greske.replace(drzava, greskaO);

    }


}
