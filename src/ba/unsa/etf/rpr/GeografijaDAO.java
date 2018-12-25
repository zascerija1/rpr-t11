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
    static private Connection conn = null;
    private PreparedStatement insertGrad = null;
    private PreparedStatement insertDrzava;
    private int id_grad = 0;
    private int id_drzava = 0;

    public static GeografijaDAO getInstance() {
        if (ourInstance == null) initialize();
        return ourInstance;
    }

    private static void initialize() {
        ourInstance = new GeografijaDAO();
    }


    private GeografijaDAO() {
        try {


            drzave.addAll(new Drzava("Francuska", new Grad("Pariz", null, 0, 1), 1), new Drzava("Velika Britanija", new Grad("London", null, 0, 2), 2)
                    , new Drzava("Austrija", new Grad("Beč", null, 0, 5), 3));

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


                insertGrad = conn.prepareStatement("Insert into grad values(?,?,?,?)");
                insertDrzava = conn.prepareStatement("Insert into drzava values (?,?,?)");


                for (int i = 0; i < drzave.size(); i++) {
                    insertDrzava.setInt(1, drzave.get(i).getId());
                    insertDrzava.setString(2, drzave.get(i).getNaziv());
                    insertDrzava.setInt(3, drzave.get(i).getGlavniGrad().getId());
                    insertDrzava.execute();
                }
                id_drzava = 3;
                for (int i = 0; i < gradovi.size(); i++) {
                    insertGrad.setInt(1, i + 1);
                    insertGrad.setString(2, gradovi.get(i).getNaziv());
                    insertGrad.setInt(3, gradovi.get(i).getDrzava().getId());
                    insertGrad.setInt(4, gradovi.get(i).getBrojStanovnika());
                    insertGrad.execute();
                }
                id_grad = 5;

            }
            if (conn == null) conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
            if (insertGrad == null) {
                insertGrad = conn.prepareStatement("Insert into grad values(?,?,?,?)");
                insertDrzava = conn.prepareStatement("Insert into drzava values (?,?,?)");
            }

        } catch (SQLException e) {
            System.out.println("Neuspješno čitanje iz baze2: " + e.getMessage());


        }

    }

    public static void removeInstance() {
        ourInstance = null;
        try {
            if (conn != null) conn.close();
            conn=null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Grad glavniGrad(String naziv) {
        Grad a = null;

        try {
            PreparedStatement upitGradDrzava = conn.prepareStatement("SELECT k.naziv, k.broj_stanovnika, d.naziv,k.id,d.id " +
                    "FROM drzava d,grad k where k.drzava=d.id and k.id=d.glavni_grad and d.naziv=?");
            upitGradDrzava.setString(1, naziv);
            ResultSet rs = upitGradDrzava.executeQuery();
            if (rs.next()) {
                Drzava m = new Drzava(rs.getString(3), null, rs.getInt(5));
                a = new Grad(rs.getString(1), m, rs.getInt(2), rs.getInt(4));
                m.setGlavniGrad(a);
                a.setDrzava(m);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return a;
    }

    public void izmijeniGrad(Grad grad) {

        try {
            PreparedStatement stmt2 = conn.prepareStatement("UPDATE grad set naziv=?, broj_stanovnika=? where id=?");

            stmt2.setString(1, grad.getNaziv());
            stmt2.setInt(2, grad.getBrojStanovnika());
            stmt2.setInt(3, grad.getId());
            stmt2.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Grad> gradovi() {
        ArrayList<Grad> a = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT g.naziv, g.broj_stanovnika, d.naziv,g.id,d.id FROM grad g  join drzava d on  g.drzava=d.id  order by 2 DESC  ");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Drzava m = new Drzava(rs.getString(3), null, rs.getInt(5));
                Grad k = new Grad(rs.getString(1), m, rs.getInt(2), rs.getInt(4));
                k.setDrzava(m);
                a.add(k);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return a;
    }

    public void obrisiDrzavu(String drzava) {
        int id = 0;
        try {
            PreparedStatement dajId = conn.prepareStatement("select id from drzava where naziv=?");
            dajId.setString(1, drzava);
            ResultSet rs = dajId.executeQuery();
            if (rs.next()) id = rs.getInt(1);


        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            PreparedStatement deleteGrad = conn.prepareStatement("delete from grad where drzava=?");
            deleteGrad.setInt(1, id);
            deleteGrad.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            PreparedStatement deleteDrzava = conn.prepareStatement("delete from drzava where naziv=?");
            deleteDrzava.setString(1, drzava);
            deleteDrzava.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public Drzava nadjiDrzavu(String drzava) {
        Drzava k = null;
        Grad m=null;
       //Zbog potreba guija i strožiji upit
        try {
            PreparedStatement stmtDr = conn.prepareStatement("SELECT  g.id , g.broj_stanovnika, g.drzava,g.naziv FROM grad g,drzava d where d.glavni_grad=g.id and  d.naziv=?");

            stmtDr.setString(1, drzava);
            ResultSet rs2 = stmtDr.executeQuery();
            if (rs2.next()) {
                k = new Drzava(drzava, null, rs2.getInt(3));
                m = new Grad(rs2.getString(4), k, rs2.getInt(2), rs2.getInt(1));
                k.setGlavniGrad(m);
                m.setDrzava(k);

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }



      if(k==null) {

          try {
              PreparedStatement stmt1 = conn.prepareStatement("SELECT  d.id , d.glavni_grad FROM drzava d where  d.naziv=?");
              stmt1.setString(1, drzava);
              ResultSet rs2 = stmt1.executeQuery();
              if (rs2.next()) {
                  k = new Drzava(drzava, null, rs2.getInt(1));
                  m = new Grad("", k, 0, rs2.getInt(2));
                  k.setGlavniGrad(m);


              }

          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
        return k;
    }


    public Grad nadjiGrad(String grad) {
        Drzava k = null;
        Grad m=null;
//Zbog potreba guija 2 vrste upita
        try {
            PreparedStatement stmtDr = conn.prepareStatement("SELECT  g.id , g.broj_stanovnika, g.drzava,d.naziv FROM grad g,drzava d where d.id=g.drzava and  g.naziv=?");

            stmtDr.setString(1, grad);
            ResultSet rs2 = stmtDr.executeQuery();
            if (rs2.next()) {
                k = new Drzava(rs2.getString(4), null, rs2.getInt(3));
                m = new Grad(grad, k, rs2.getInt(2), rs2.getInt(1));
                m.setDrzava(k);

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
if(m==null) {
    try {

        PreparedStatement stmt1 = conn.prepareStatement("SELECT  g.id , g.broj_stanovnika, g.drzava FROM grad g where  g.naziv=?");
        stmt1.setString(1, grad);
        ResultSet rs2 = stmt1.executeQuery();
        if (rs2.next()) {
            k = new Drzava("", null, rs2.getInt(3));
            m = new Grad(grad, k, rs2.getInt(2), rs2.getInt(1));
            k.setGlavniGrad(m);
            m.setDrzava(k);

        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

        return m;
    }

    public void dodajGrad(Grad grad) {
        int ident = id_grad + 1;
        int ident_d = id_drzava + 1;

        Drzava m = nadjiDrzavu(grad.getDrzava().getNaziv());


        if (m != null ) {
            ident_d = m.getId();
            if (grad.getDrzava().getGlavniGrad()!=null && grad.getDrzava().getGlavniGrad().getNaziv().equals(grad.getNaziv())) ident = m.getGlavniGrad().getId();
            else if(ident==m.getGlavniGrad().getId()){
                ident++; id_grad++;
            }
        }

        try {
            insertGrad.setInt(1, ident);
            insertGrad.setString(2, grad.getNaziv());
            insertGrad.setInt(3, ident_d);
            insertGrad.setInt(4, grad.getBrojStanovnika());
            insertGrad.execute();
            if (ident == id_grad + 1) id_grad=id_grad+1;
            if (ident_d == id_drzava + 1) id_drzava=id_drzava+1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dodajDrzavu(Drzava drzava) {
        int ident_g= id_grad + 1;
        int ident_d = id_drzava + 1;
        Grad k=nadjiGrad(drzava.getGlavniGrad().getNaziv());
        if(k!=null){
           ident_d=k.getDrzava().getId();
           if(k.getNaziv().equals(drzava.getGlavniGrad().getNaziv())) ident_g=k.getId();
        }
        try {
            insertDrzava.setInt(1, ident_d);
            insertDrzava.setString(2, drzava.getNaziv());
            insertDrzava.setInt(3, ident_g);
            insertDrzava.execute();
            if (ident_g == id_grad + 1) id_grad=id_grad+1;
            if (ident_d == id_drzava + 1) id_drzava=id_drzava+1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
