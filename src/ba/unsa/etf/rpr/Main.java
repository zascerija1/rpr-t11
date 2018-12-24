package ba.unsa.etf.rpr;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static GeografijaDAO gegrafija=GeografijaDAO.getInstance();

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
        if(dbfile.isFile()) System.out.print("OK");
        dbfile.delete();

        /*GeografijaDAO dao = GeografijaDAO.getInstance();
        Grad bech = dao.glavniGrad("Austrija");
        bech.setNaziv("Wien");
        dao.izmijeniGrad(bech);
        gegrafija=dao;
        System.out.println("Gradovi su:\n" + ispisiGradove());
        glavniGrad();*/


    }
}
