package ba.unsa.etf.rpr;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Drzava {



    private SimpleStringProperty naziv = new SimpleStringProperty("");
    private SimpleStringProperty glavni_grad = new SimpleStringProperty("");

    public Drzava() {}

    public Drzava(String a, String n) {
        naziv = new SimpleStringProperty(a);
        glavni_grad = new SimpleStringProperty(n);

    }



    public String getNaziv() {
        return naziv.get();
    }

    public SimpleStringProperty nazivProperty() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv.set(naziv);
    }

    public String getGlavni_grad() {
        return glavni_grad.get();
    }

    public SimpleStringProperty glavni_gradProperty() {
        return glavni_grad;
    }

    public void setGlavni_grad(String glavni_grad) {
        this.glavni_grad.set(glavni_grad);
    }

    public void setGlavniGrad(Grad sarajevo) {
    }
}
