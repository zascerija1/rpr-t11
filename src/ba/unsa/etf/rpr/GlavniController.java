package ba.unsa.etf.rpr;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class GlavniController {
    public Button unosDrzava;
    public Button unosGrad;
    public Button exit;
    public Button promijeniGrad;
    public Button nadjiDrzavu;
    public Button obrisiDrzavu;
    public Button nadjiGrad;
    public Button spisakGradova;
    public Button nadjiDrzavuGr;
    private GeografijaDAO geo=GeografijaDAO.getInstance();


    public void unesiDrzavu(ActionEvent actionEvent) {
        try {
            Stage scena = new Stage();
            Parent root = null;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("prozorDrzava.fxml"));
            //loader.setController(prozor);
            root = loader.load();
            root.setVisible(true);
            scena.setTitle("unos države");
            scena.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            scena.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void unesiGrad(ActionEvent actionEvent)  {

        Grad g=null;


        FXMLLoader loader = new FXMLLoader(getClass().getResource("prozor.fxml"));
        loader.setController(new ProzorController(g));
        Parent root = null;
        try {
            root = loader.load();
            Stage stage=new Stage();
            stage.setTitle("Dodavanje grada");
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void izmijeniGrad(ActionEvent actionEvent) {
        Grad g=nadjiGradDialog();
        if(g !=null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("prozor.fxml"));
            loader.setController(new ProzorController(g));
            Parent root = null;
            try {
                root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Izmjena grada");
                stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }

    public void nadjiDrzavuPoImenu(ActionEvent actionEvent) {
        Drzava d=nadjiDrzavuDialog();
        if(d!=null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Država pronađena!");
            alert.setHeaderText("Država koji ste tražili je: ");
            alert.setContentText(d.toString());
            alert.showAndWait();
        }

    }

    public void izbrisiDrzavu(ActionEvent actionEvent) {
        Drzava d=nadjiDrzavuDialog();
        if(d!=null){
            geo.obrisiDrzavu(d.getNaziv());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Brisanje države");
            alert.setHeaderText(null);
            alert.setContentText("Država i svi gradovi koji se nalaze u njoj obrisani!");

            alert.showAndWait();

        }
    }

    public void nadjiGrad(ActionEvent actionEvent) {
        Grad g = nadjiGradDialog();

        if (g != null) {
                  alertPronadjen(g);
        }

    }
public void alertPronadjen(Grad g){
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Grad pronađen!");
    alert.setHeaderText("Grad koji ste tražili je: ");
    alert.setContentText(g.toString());
    alert.showAndWait();

        }



     public Grad nadjiGradDialog(){

         TextInputDialog dialog = new TextInputDialog("naziv");
         dialog.setTitle("Definisanje naziva grada");
         dialog.setHeaderText("Pretraga grada");
         dialog.setContentText("Unesite naziv željenog grada:");


         Optional<String> result = dialog.showAndWait();
         Grad g=null;
         if (result.isPresent()) {
             g = geo.nadjiGrad(result.get());

         }

         if(g==null && result.isPresent()) {
             alert();
         }


         return g;

     }

    public Drzava nadjiDrzavuDialog(){

        TextInputDialog dialog = new TextInputDialog("naziv");
        dialog.setTitle("Definisanje naziva države");
        dialog.setHeaderText("Pretraga države");
        dialog.setContentText("Unesite naziv željene države:");


        Optional<String> result = dialog.showAndWait();
        Drzava g=null;
        if (result.isPresent()) {
            g = geo.nadjiDrzavu(result.get());

        }

        if(g==null && result.isPresent()) {
           alert();
        }


        return g;

    }

    public void izlistajGradove(ActionEvent actionEvent) throws IOException {
        Stage stage=new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("spisakGradova.fxml"));
        stage.setTitle("Spisak gradova u bazi");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.show();


    }

    public void nadjiDrzavuPoGlGradu(ActionEvent actionEvent) {

        TextInputDialog dialog = new TextInputDialog("naziv");
        dialog.setTitle("Definisanje naziva države");
        dialog.setHeaderText("Pretraga glavnog grada");
        dialog.setContentText("Unesite naziv  države čiji glavni grad želite saznati: ");


        Optional<String> result = dialog.showAndWait();
        Grad g=null;
        if (result.isPresent()) {
            g = geo.glavniGrad(result.get());
            if(g!=null) alertPronadjen(g);
        }

        if(g==null && result.isPresent()) {
            alert();
        }



    }

    public void izadji(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void alert(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Greška!");
        alert.setHeaderText("Traženi objekat ne postoji!");
        alert.setContentText("Pokušajte ponovo!");

        alert.showAndWait();
    }
}
