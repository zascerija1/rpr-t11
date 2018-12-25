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
        dbfile.delete();

       GeografijaDAO dao = GeografijaDAO.getInstance();

        Drzava a=new Drzava("Poljska", null,5);
        Grad d=new Grad("Varsava", a, 939393,7);
        a.setGlavniGrad(d);
        d.setDrzava(a);
        dao.dodajDrzavu(a);
        dao.dodajGrad(d);
        System.out.println("Gradovi su:\n" + ispisiGradove());
        glavniGrad();




    }
}
