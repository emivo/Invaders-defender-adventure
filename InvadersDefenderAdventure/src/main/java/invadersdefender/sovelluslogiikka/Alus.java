package invadersdefender.sovelluslogiikka;

import java.util.ArrayList;
import java.util.List;

/**
 * @author emivo
 */
public class Alus implements Liikkuva {
    
    private int koko; // aluksen sivunpituus
    private Pala pala;
    private Suunta suunta;

    // aluksen x koordinaatti on x + aluksenkoko. samoin y
    // getsijainti antaa listan Paloja

    public Alus(int alkuX, int alkuY) {
        this.koko = 3; //pariton on kiva

        this.suunta = null;
        
        this.pala = new Pala(alkuX, alkuY);
    }

    public int getKoko() {
        return koko;
    }

    public void setSuunta(Suunta suunta) {
        this.suunta = suunta;
    }

    public Suunta getSuunta() {
        return suunta;
    }
    
    public Ammus ammu() {
        // oma alus ampuu aina ylos
        return new Ammus((pala.getX()+koko/2), pala.getY()-1, Suunta.YLOS);
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

    @Override
    public void liiku() {
        if (this.suunta != null) {
            this.pala.liiku(suunta);
            this.suunta = null;
        }
        // alus ei liiku ellei se saa uutta suuntaa
        // siis alus pysyy paikoillaan, ellei sitä liikuta
    }
}
