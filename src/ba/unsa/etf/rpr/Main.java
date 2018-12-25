package ba.unsa.etf.rpr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static javafx.application.Application.launch;
import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class Main extends Application {
    public static GeografijaDAO gegrafija=GeografijaDAO.getInstance();

    @Override
    public void start(Stage primaryStage) throws IOException {


        Parent root = FXMLLoader.load(getClass().getResource("glavniProzor.fxml"));
        primaryStage.setTitle("Baza podataka");
        primaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        primaryStage.show();

    }

   public  static String ispisiGradove(){
        ArrayList<Grad> a=gegrafija.gradovi();
        String str="";
        for(int i=0;i<a.size();i++) str=str+a.get(i).toString()+'\n';
        return str;
    }

    private static void glavniGrad(){
        String a=null;
        Scanner ulaz=new Scanner(System.in);
        a=ulaz.next();

        Grad grad= gegrafija.glavniGrad(a);
        if(grad!=null) System.out.println("Glavni grad države "+ a+ " je "+grad.getNaziv());
        else System.out.println("Nepostojeća država");




    }

    public static void main(String[] args) {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();
        launch(args);
        /*GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();

       GeografijaDAO dao = GeografijaDAO.getInstance();

        Drzava a=new Drzava("Poljska", null,5);
        Grad d=new Grad("Varsava", a, 939393,7);
        a.setGlavniGrad(d);
        d.setDrzava(a);
        dao.dodajDrzavu(a);
        dao.dodajGrad(d);*/
        System.out.println("Gradovi su:\n" + ispisiGradove());
        Grad g=gegrafija.nadjiGrad("Pariz");
        System.out.println(g.toString());
        //glavniGrad();




    }
}
