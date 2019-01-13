package ba.unsa.etf.rpr;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

class Grad {


    private SimpleStringProperty naziv = new SimpleStringProperty("");
    private SimpleObjectProperty<Drzava> drzava = new SimpleObjectProperty("");
    private SimpleIntegerProperty brojStanovnika = new SimpleIntegerProperty(0);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    public Grad() {}

    public Grad(String a, Drzava n, int b,int id) {
        naziv = new SimpleStringProperty(a);
        drzava = new SimpleObjectProperty(n);
        brojStanovnika = new SimpleIntegerProperty(b);
        this.id=id;
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

    public Drzava getDrzava() {
        return (Drzava)drzava.get();
    }

    public SimpleObjectProperty drzavaProperty() {
        return drzava;
    }

    public void setDrzava(Drzava drzava) {
        this.drzava.set(drzava);
    }

    public int getBrojStanovnika() {
        return brojStanovnika.get();
    }

    public SimpleIntegerProperty brojStanovnikaProperty() {
        return brojStanovnika;
    }

    public void setBrojStanovnika(int brojStanovnika) {
        this.brojStanovnika.set(brojStanovnika);
    }

    @Override
    public String toString() {
        return naziv.get() +" ("+
                 drzava.get().getNaziv() + ") - "+
                + brojStanovnika.get();

    }
}
