package ba.unsa.etf.rpr;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

public class GeografijaDAO {
    private static GeografijaDAO ourInstance = null;
    private ObservableList<Grad> gradovi= FXCollections.observableArrayList();
    private ObservableList<Drzava> drzave=FXCollections.observableArrayList();
    private Connection conn;
    private PreparedStatement stmt;
    private PreparedStatement stmt1;
    private PreparedStatement stmt2;

    public static GeografijaDAO getInstance() {
        if(ourInstance==null) initialize();
            return ourInstance;
    }
    private static void initialize(){
        ourInstance=new GeografijaDAO();
    }


    private GeografijaDAO() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:D:/repozitoriji/rpr-t9/src/baza.db​");
            stmt = conn.prepareStatement("SELECT g.naziv, g.broj_stanovnika, d.naziv,d.glavni_grad FROM grad g,drzava d" +
                    "                          where g.drzava=d.id ");
            stmt1 = conn.prepareStatement("SELECT d.naziv, g.naziv,g.broj_stanovnika FROM drzava d, grad g" +
                    " where d.glavni_grad=g.id");
            stmt2= conn.prepareStatement("UPDATE grad set naziv=? where broj_stanovnika=?");

            ResultSet rs = stmt.executeQuery();
            ResultSet rs2=stmt1.executeQuery();
            while (rs.next()) {

                Grad k = new Grad(rs.getString(1),new Drzava(rs.getString(3),rs.getString(4) ),rs.getInt(2));
                gradovi.add(k);

                //if (trenutnaKnjiga == null) trenutnaKnjiga = new SimpleObjectProperty<Knjiga>(k);
            }

            while (rs2.next()) {

                Drzava k = new Drzava(rs2.getString(1),rs2.getString(2));
                drzave.add(k);

                //if (trenutnaKnjiga == null) trenutnaKnjiga = new SimpleObjectProperty<Knjiga>(k);
            }


        } catch(SQLException e) {
            System.out.println("Neuspješno čitanje iz baze2: " + e.getMessage());
            drzave.addAll(new Drzava("Francuska","Pariz"),new Drzava("Velika Britanija","London")
                    ,new Drzava("Austrija","Beč"));

            gradovi.addAll(new Grad("Pariz", drzave.get(0),2206488),
                    new Grad("London",drzave.get(1), 8825000),
                    new Grad("Manchester",drzave.get(1), 545500),
                    new Grad("Graz",drzave.get(2), 280200),
                    new Grad("Beč",drzave.get(2), 1899055));

        }

        //if (trenutnaKnjiga == null) trenutnaKnjiga = new SimpleObjectProperty<>();
    }

    public static void removeInstance() {
        ourInstance=null;
    }

    public Grad glavniGrad(String naziv) {


    for(int i=0;i<gradovi.size();i++){
        if(gradovi.get(i).getDrzava().getNaziv().equals(naziv)) return gradovi.get(i);
    }
       return null;
    }

    public void izmijeniGrad(Grad grad) {
     for(int i=0;i<gradovi.size();i++){
         if(gradovi.get(i).getBrojStanovnika()==grad.getBrojStanovnika() &&
      gradovi.get(i).getDrzava().getNaziv().equals(grad.getDrzava().getNaziv())){
             gradovi.get(i).setNaziv(grad.getNaziv());

         }
    }
        try {
            stmt2.setString(1,grad.getNaziv());
            stmt2.setInt(2,grad.getBrojStanovnika());
            stmt2.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public ArrayList<Grad> gradovi() {
        ArrayList<Grad> a=new ArrayList<>();
        a.addAll(gradovi);
        a.sort((Grad b, Grad c)->{return ((Integer)c.getBrojStanovnika()).compareTo( (Integer)b.getBrojStanovnika()); });
        return a;
    }

    public void obrisiDrzavu(String drzava) {
        drzave.remove(nadjiDrzavu(drzava));
        for(int i=0;i<gradovi.size();i++){
            if(gradovi.get(i).getDrzava().getNaziv().equals(drzava)) {gradovi.remove(i); i--;}
        }

    }

    public Drzava nadjiDrzavu(String drzava) {
        for(int i=0;i<drzave.size();i++){
            if(drzave.get(i).getNaziv().equals(drzava)) return drzave.get(i);
        }
        return null;
    }

    public void dodajGrad(Grad grad) {
        gradovi.add(grad);
    }

    public void dodajDrzavu(Drzava drzava) {
        drzave.add(drzava);
    }
}
