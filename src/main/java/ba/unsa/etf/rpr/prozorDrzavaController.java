package ba.unsa.etf.rpr;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.ResourceBundle;

public class prozorDrzavaController {
    public TextField naziv;
    public TextField glavniGrad;
    public Button Posalji;
    private GeografijaDAO geo=GeografijaDAO.getInstance();

    HashMap<Control, Boolean> greske = new HashMap<>();


    boolean greskaO = false;


    public void submit(ActionEvent actionEvent) {
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        if (greske.containsValue(true) || (!greske.containsKey(naziv) || (!greske.containsKey(glavniGrad)))) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("pogr_unos"));
            if (greske.containsValue(true)) alert.setHeaderText(bundle.getString("neispr_podaci"));
            else alert.setHeaderText(bundle.getString("nepot_podaci"));
            alert.setContentText(bundle.getString("unos_ponovo"));

            alert.showAndWait();


        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(bundle.getString("dod"));
            alert.setHeaderText(bundle.getString("zapis_uspj"));
            alert.setContentText(bundle.getString("uspj"));

            Drzava a=new Drzava(naziv.getText(),null,1);
            Grad b=new Grad(glavniGrad.getText(),a,1,1);
            a.setGlavniGrad(b);
            b.setDrzava(a);
            Platform.runLater(()->geo.dodajDrzavu(a));
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
        if (greskaO) ime.setStyle("-fx-background-color: rgba(243,31,54,0.78)");

        else {
            ime.setStyle("-fx-background-color: rgba(92,168,108,0.91)");
            greskaO = false;
        }

        return greskaO;
    }

    public void unosNaziv(KeyEvent keyEvent) {
        if (!greske.containsKey(naziv)) greske.put(naziv, false);
        greske.replace(naziv, oboji(naziv));
    }

    public void unosGlavniGrad(KeyEvent keyEvent) {
        if (!greske.containsKey(glavniGrad)) greske.put(glavniGrad, false);
        greske.replace(glavniGrad, oboji(glavniGrad));
    }

}
