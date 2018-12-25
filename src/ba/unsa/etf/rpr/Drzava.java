package ba.unsa.etf.rpr;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Drzava {


    private SimpleStringProperty naziv = new SimpleStringProperty("");
    private SimpleObjectProperty<Grad> glavniGrad = new SimpleObjectProperty<>();
    private int id;



    public Drzava() {}

    public Drzava(String a, Grad n,int id) {
        naziv = new SimpleStringProperty(a);
        glavniGrad = new SimpleObjectProperty(n);
        this.id=id;

    }

    public int getId() {
        return id;
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

    public Grad getGlavniGrad() {
        return glavniGrad.get();
    }

    public SimpleObjectProperty<Grad> glavni_gradProperty() {
        return glavniGrad;
    }

    public void setGlavniGrad(Grad glavni_grad) {
        this.glavniGrad.set(glavni_grad);
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        String s=glavniGrad.get().getNaziv();
        if(s.length()==0) s="Nepoznato";
        return "Naziv=" + naziv.get() +
                "\n Glavni grad=" +s;

    }
}
