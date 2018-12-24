package ba.unsa.etf.rpr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class GeografijaDAO {
    private static GeografijaDAO ourInstance = null;
    private ObservableList<Grad> gradovi = FXCollections.observableArrayList();
    private ObservableList<Drzava> drzave = FXCollections.observableArrayList();
    private Connection conn;
    private PreparedStatement stmt;
    private PreparedStatement stmt1;
    private PreparedStatement stmt2;
    private PreparedStatement upit1;
    private PreparedStatement upit2;

    public static GeografijaDAO getInstance() {
        if (ourInstance == null) initialize();
        return ourInstance;
    }

    private static void initialize() {
        ourInstance = new GeografijaDAO();
    }


    private GeografijaDAO() {
        try {


            drzave.addAll(new Drzava("Francuska", "Pariz"), new Drzava("Velika Britanija", "London")
                    , new Drzava("Austrija", "Beč"));

            gradovi.addAll(new Grad("Pariz", drzave.get(0), 2206488),
                    new Grad("London", drzave.get(1), 8825000),
                    new Grad("Manchester", drzave.get(1), 545500),
                    new Grad("Graz", drzave.get(2), 280200),
                    new Grad("Beč", drzave.get(2), 1899055));

            File db=new File("baza.db");
            conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
            PreparedStatement kreirajDrzava=conn.prepareStatement("create table if not exists drzava (id int primary key, naziv text,glavni_grad int)");
            kreirajDrzava.executeUpdate();

            PreparedStatement kreirajGrad=conn.prepareStatement("create table if not exists  grad (id int primary key, drzava int  references drzava,broj_stanovnika int)");
            kreirajGrad.executeUpdate();
            PreparedStatement alterDrzava=conn.prepareStatement("alter table drzava" +
                    " add foreign key glavni_grad references grad ");
           alterDrzava.executeUpdate();

            PreparedStatement insertGrad=conn.prepareStatement("Insert into grad values( ?,?,?,?)");
            PreparedStatement insertDrzava=conn.prepareStatement("Insert into drzava values (?,?,?)");

            for(int i=0;i<gradovi.size();i++){
                insertGrad.setInt(1,i);
                insertGrad.setString(2,gradovi.get(i).getNaziv());
                insertGrad.setString(3,gradovi.get(i).getDrzava().getNaziv());
                insertGrad.setInt(4,gradovi.get(i).getBrojStanovnika());
                insertGrad.execute();
            }

            for(int i=0;i<drzave.size();i++){
                insertDrzava.setInt(1,i);
                insertDrzava.setString(2,drzave.get(i).getNaziv());
                insertDrzava.setString(3,drzave.get(i).getGlavni_grad());
                insertDrzava.execute();
            }


            //conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
            stmt = conn.prepareStatement("SELECT g.naziv, g.broj_stanovnika, d.naziv,d.glavni_grad FROM grad g,drzava d" +
                    "                          where g.drzava=d.id ");
            stmt1 = conn.prepareStatement("SELECT d.naziv, g.naziv FROM drzava d, grad g" +
                    " where d.glavni_grad=g.id");
            stmt2 = conn.prepareStatement("UPDATE grad set naziv=? where broj_stanovnika=?");

            ResultSet rs2 = stmt1.executeQuery();


            while (rs2.next()) {

                Drzava k = new Drzava(rs2.getString(1), rs2.getString(2));
                drzave.add(k);

            }

        } catch (SQLException e) {
            System.out.println("Neuspješno čitanje iz baze2: " + e.getMessage());


        }

    }

    public static void removeInstance() {
        ourInstance = null;
    }

    public Grad glavniGrad(String naziv) {


        for (int i = 0; i < gradovi.size(); i++) {
            if (gradovi.get(i).getDrzava().getNaziv().equals(naziv)) return gradovi.get(i);
        }
        return null;
    }

    public void izmijeniGrad(Grad grad) {
        for (int i = 0; i < gradovi.size(); i++) {
            if (gradovi.get(i).getBrojStanovnika() == grad.getBrojStanovnika() &&
                    gradovi.get(i).getDrzava().getNaziv().equals(grad.getDrzava().getNaziv())) {
                gradovi.get(i).setNaziv(grad.getNaziv());

            }
        }
        try {
            stmt2.setString(1, grad.getNaziv());
            stmt2.setInt(2, grad.getBrojStanovnika());
            stmt2.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Grad> gradovi() {
        ArrayList<Grad> a = new ArrayList<>();
        try {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Grad k = new Grad(rs.getString(1), new Drzava(rs.getString(3), rs.getString(4)), rs.getInt(2));
                a.add(k);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        a.sort((Grad b, Grad c) -> {
            return ((Integer) c.getBrojStanovnika()).compareTo((Integer) b.getBrojStanovnika());
        });
        return a;
    }

    public void obrisiDrzavu(String drzava) {
        drzave.remove(nadjiDrzavu(drzava));
        for (int i = 0; i < gradovi.size(); i++) {
            if (gradovi.get(i).getDrzava().getNaziv().equals(drzava)) {
                gradovi.remove(i);
                i--;
            }
        }

    }

    public Drzava nadjiDrzavu(String drzava) {
        for (int i = 0; i < drzave.size(); i++) {
            if (drzave.get(i).getNaziv().equals(drzava)) return drzave.get(i);
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
