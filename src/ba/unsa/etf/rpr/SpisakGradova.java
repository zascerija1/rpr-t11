package ba.unsa.etf.rpr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class SpisakGradova {
    public ListView spisak;
    GeografijaDAO geo=GeografijaDAO.getInstance();

    @FXML
    public void initialize(){
        ObservableList<Grad> a= FXCollections.observableArrayList(geo.gradovi());
        spisak.setItems(a);

    }

}
