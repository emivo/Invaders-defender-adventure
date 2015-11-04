package invadersdefender.sovelluslogiikka;

import java.util.ArrayList;
import java.util.List;

/**
 * @author emivo
 */
public class Alus {
    
    private int koko; // aluksen sivunpituus
    private Pala pala;

    // aluksen x koordinaatti on x + aluksenkoko. samoin y
    // getsijainti antaa listan x,y pareja

    public Alus(int alkuX, int alkuY) {
        koko = 3; //pariton on kiva
        
        
        pala = new Pala(alkuX, alkuY);
    }
    
    public void liiku(Suunta suunta) {
        pala.liiku(suunta);
    }
    
    public Ammus ammu() {
        return new Ammus((pala.getX()+koko/2),pala.getY()+1);
    }
    
    public List<Pala> sijainti() {
        List<Pala> sijainnit = new ArrayList<>();
        int x = pala.getX();
        int y = pala.getY();
        for (int i = 0; i < koko; i++) {
            for (int j = 0; j < koko; j++) {
                sijainnit.add(new Pala(x + i, y + j));
            }
        }
        return sijainnit;
    }
}
