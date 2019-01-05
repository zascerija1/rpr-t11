package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

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
    ResourceBundle bundle = ResourceBundle.getBundle("Translation");


    public void unesiDrzavu(ActionEvent actionEvent) {
        try {
            Stage scena = new Stage();
            Parent root = null;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("prozorDrzava.fxml"),bundle);
            //loader.setController(prozor);
            root = loader.load();
            root.setVisible(true);
            scena.setTitle(bundle.getString("unos_drzave"));
            scena.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            scena.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void unesiGrad(ActionEvent actionEvent)  {

        Grad g=null;


        FXMLLoader loader = new FXMLLoader(getClass().getResource("prozor.fxml"),bundle);
        loader.setController(new ProzorController(g));
        Parent root = null;
        try {
            root = loader.load();
            Stage stage=new Stage();
            stage.setTitle(bundle.getString("unos_grada"));
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void izmijeniGrad(ActionEvent actionEvent) {
        Grad g=nadjiGradDialog();
        if(g !=null) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("prozor.fxml"),bundle);
            loader.setController(new ProzorController(g));
            Parent root = null;
            try {
                root = loader.load();
                Stage stage = new Stage();
                stage.setTitle(bundle.getString("izmjena_grada"));
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
            alert.setTitle(bundle.getString("drzava_pronadjena"));

            alert.setHeaderText(bundle.getString("rezultat_pretrage"));
            alert.setContentText(d.toString());
            alert.showAndWait();
        }

    }

    public void izbrisiDrzavu(ActionEvent actionEvent) {
        Drzava d=nadjiDrzavuDialog();
        if(d!=null){
            geo.obrisiDrzavu(d.getNaziv());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString("naslov_brisanja"));
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("obrisana_drzava"));

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
    alert.setTitle(bundle.getString("grad_pronadjen"));
    alert.setHeaderText(bundle.getString("rezultat_grad"));
    alert.setContentText(g.toString());
    alert.showAndWait();

        }



     public Grad nadjiGradDialog(){

         TextInputDialog dialog = new TextInputDialog(bundle.getString("naziv_grada"));
         dialog.setTitle(bundle.getString("naslov_za_grad"));
         dialog.setHeaderText(bundle.getString("pretraga_grada"));
         dialog.setContentText(bundle.getString("unos_naziva_grada"));


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

        TextInputDialog dialog = new TextInputDialog(bundle.getString("naziv"));
        dialog.setTitle(bundle.getString("naslov_za_drzavu"));
        dialog.setHeaderText(bundle.getString("pretraga_drzave"));
        dialog.setContentText(bundle.getString("unos_naziva_drzave"));

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

        Parent root = FXMLLoader.load(getClass().getResource("spisakGradova.fxml"),bundle);
        stage.setTitle(bundle.getString("spisak_gradova"));
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.show();


    }

    public void nadjiDrzavuPoGlGradu(ActionEvent actionEvent) {

        TextInputDialog dialog = new TextInputDialog(bundle.getString("naziv"));
        dialog.setTitle(bundle.getString("naslov_za_drzavu"));
        dialog.setHeaderText(bundle.getString("pretraga_gl_grada"));
        dialog.setContentText(bundle.getString("ime_drzave_gl_grad"));


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
        alert.setTitle(bundle.getString("greska"));
        alert.setHeaderText(bundle.getString("upozorenje"));
        alert.setContentText(bundle.getString("pokusaj"));

        alert.showAndWait();
    }

    void promijeniJezik(Locale locale){
        Locale.setDefault(locale);
        ResourceBundle bundle2 = ResourceBundle.getBundle("Translation");
        bundle=bundle2;
        Stage stage = (Stage) unosDrzava.getScene().getWindow();


        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("glavniProzor.fxml"),bundle2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle(bundle.getString("appname"));
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
    }
    public void bos(ActionEvent actionEvent) {
        promijeniJezik(new Locale("bs","BA"));

    }

    public void engl(ActionEvent actionEvent) {
        promijeniJezik(new Locale("en","US"));

    }

    public void fran(ActionEvent actionEvent) {
        promijeniJezik(new Locale("fr","FR"));
    }

    public void germ(ActionEvent actionEvent) {
        promijeniJezik(new Locale("de","DE"));
    }

    public void Prikazi(ActionEvent actionEvent) {

        try {
            new GradoviReport().showReport(GeografijaDAO.getConn());
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
