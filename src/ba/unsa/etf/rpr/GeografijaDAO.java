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
    private Connection conn=null;
    private PreparedStatement stmt;
    private PreparedStatement stmt1;
    private PreparedStatement stmt2;
    private PreparedStatement upitGradDrzava;
    private PreparedStatement deleteDrzava;
    private PreparedStatement deleteGrad=null;
    private PreparedStatement insertGrad=null;
    private PreparedStatement insertDrzava;
    private PreparedStatement dajId;
    private int id_grad=5;
    private int id_drzava=3;

    public static GeografijaDAO getInstance() {
        if (ourInstance == null) initialize();
        return ourInstance;
    }

    private static void initialize() {
        ourInstance = new GeografijaDAO();
    }


    private GeografijaDAO() {
        try {


            drzave.addAll(new Drzava("Francuska", new Grad("Pariz",null,0,1), 1), new Drzava("Velika Britanija", new Grad("London",null,0,2), 2)
                    , new Drzava("Austrija", new Grad("Beč",null,0,5), 3));

            gradovi.addAll(new Grad("Pariz", drzave.get(0), 2206488, 1),
                    new Grad("London", drzave.get(1), 8825000, 2),
                    new Grad("Manchester", drzave.get(1), 545500, 3),
                    new Grad("Graz", drzave.get(2), 280200, 4),
                    new Grad("Beč", drzave.get(2), 1899055, 5));

            File db = new File("baza.db");

            if (!db.exists()) {
                conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
                PreparedStatement kreirajDrzava = conn.prepareStatement("create table if not exists drzava (id int primary key, naziv text,glavni_grad int references grad)");
                kreirajDrzava.executeUpdate();

                PreparedStatement kreirajGrad = conn.prepareStatement("create table if not exists  grad (id int primary key, naziv text, drzava int  references drzava,broj_stanovnika int)");
                kreirajGrad.executeUpdate();
            /*PreparedStatement alterDrzava=conn.prepareStatement("alter table drzava" +
                    " add foreign key glavni_grad references grad ");
           alterDrzava.executeUpdate();*/

                 insertGrad = conn.prepareStatement("Insert into grad values(?,?,?,?)");
                insertDrzava = conn.prepareStatement("Insert into drzava values (?,?,?)");


                for (int i = 0; i < drzave.size(); i++) {
                    insertDrzava.setInt(1, drzave.get(i).getId());
                    insertDrzava.setString(2, drzave.get(i).getNaziv());
                    insertDrzava.setInt(3, drzave.get(i).getGlavniGrad().getId());
                    insertDrzava.execute();
                }

                for (int i = 0; i < gradovi.size(); i++) {
                    insertGrad.setInt(1, i + 1);
                    insertGrad.setString(2, gradovi.get(i).getNaziv());
                    insertGrad.setInt(3, gradovi.get(i).getDrzava().getId());
                    insertGrad.setInt(4, gradovi.get(i).getBrojStanovnika());
                    insertGrad.execute();
                }

            }
            if(conn==null) conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
            if(insertGrad==null){
                insertGrad = conn.prepareStatement("Insert into grad values(?,?,?,?)");
                insertDrzava = conn.prepareStatement("Insert into drzava values (?,?,?)");
            }

            stmt = conn.prepareStatement("SELECT g.naziv, g.broj_stanovnika, d.naziv,k.naziv,g.id,d.id FROM grad g,drzava d,grad k where g.drzava=d.id and k.id=d.glavni_grad");
            stmt1 = conn.prepareStatement("SELECT  g.naziv,d.id,g.id,g.broj_stanovnika FROM drzava d, grad g where d.glavni_grad=g.id and d.naziv=?");
           stmt2 = conn.prepareStatement("UPDATE grad set naziv=?, broj_stanovnika=? where id=?");
           upitGradDrzava=conn.prepareStatement("SELECT k.naziv, k.broj_stanovnika, d.naziv,k.id,d.id " +
                   "FROM drzava d,grad k where k.drzava=d.id and k.id=d.glavni_grad and d.naziv=?");
           deleteGrad=conn.prepareStatement("delete from grad where drzava=?");
           dajId=conn.prepareStatement("select id from drzava where naziv=?");
           deleteDrzava=conn.prepareStatement("delete from drzava where naziv=?");




        } catch (SQLException e) {
            System.out.println("Neuspješno čitanje iz baze2: " + e.getMessage());


        }

    }

    public static void removeInstance() {
        ourInstance = null;
    }

    public Grad glavniGrad(String naziv) {
        Grad a=null;
        try {
            upitGradDrzava.setString(1,naziv);
            ResultSet rs=upitGradDrzava.executeQuery();
            if(rs.next()) {
                a = new Grad(rs.getString(1), new Drzava(rs.getString(3), new Grad(rs.getString(1),null,0,rs.getInt(4)), rs.getInt(5)), rs.getInt(2), rs.getInt(4));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return a;
    }

    public void izmijeniGrad(Grad grad) {
      /*  for (int i = 0; i < gradovi.size(); i++) {
            if (gradovi.get(i).getBrojStanovnika() == grad.getBrojStanovnika() &&
                    gradovi.get(i).getDrzava().getNaziv().equals(grad.getDrzava().getNaziv())) {
                gradovi.get(i).setNaziv(grad.getNaziv());

            }
        }*/
        try {
            stmt2.setString(1, grad.getNaziv());
            stmt2.setInt(2, grad.getBrojStanovnika());
            stmt2.setInt(3,grad.getId());
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

                Grad k = new Grad(rs.getString(1), new Drzava(rs.getString(3), new Grad(rs.getString(4),null,0,0), rs.getInt(6)), rs.getInt(2), rs.getInt(5));
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
        try {
            dajId.setString(1,drzava);
            ResultSet rs= dajId.executeQuery();
            int id=rs.getInt(1);
            deleteGrad.setInt(1,id);
            deleteGrad.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            deleteDrzava.setString(1,drzava);
            deleteDrzava.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public Drzava nadjiDrzavu(String drzava) {
       Drzava k=null;
        try {
            stmt1.setString(1,drzava);
            ResultSet rs2 = stmt1.executeQuery();
            if(rs2.next()) {
               k = new Drzava(drzava,new Grad(rs2.getString(1),new Drzava(drzava,null,rs2.getInt(2)),rs2.getInt(4),rs2.getInt(3)), rs2.getInt(2));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

       return k;
    }

    public void dodajGrad(Grad grad) {
        try {
            insertGrad.setInt(1,id_grad+1);
            insertGrad.setString(2,grad.getNaziv());
            insertGrad.setInt(3,grad.getDrzava().getId());
            insertGrad.setInt(4,grad.getBrojStanovnika());
            insertGrad.executeUpdate();
            id_grad++;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dodajDrzavu(Drzava drzava) {
        try {
            insertDrzava.setInt(1,id_drzava+1);
            insertDrzava.setString(2,drzava.getNaziv());
            insertDrzava.setInt(3,drzava.getGlavniGrad().getId());
            insertDrzava.executeUpdate();
            id_drzava++;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
