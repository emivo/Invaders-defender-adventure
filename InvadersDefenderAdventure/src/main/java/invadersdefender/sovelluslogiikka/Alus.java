package invadersdefender.sovelluslogiikka;

import java.util.ArrayList;
import java.util.List;

/**
 * @author emivo
 */
public class Alus implements Liikkuva {
    
    private int koko; // aluksen sivunpituus
    private Pala sijainti;
    private Suunta suunta;

    
    public Alus(int alkuX, int alkuY, int koko) {
        this.koko = koko;

        this.suunta = null;
        
        this.sijainti = new Pala(alkuX, alkuY);
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
        return new Ammus(getX() + koko / 2, getY() - 1, Suunta.YLOS);
    }
    /**
     * metodi antaa aluksen vasemman reunan x koordinaatin
     * @return 
     */
    @Override
    public int getX() {
        return sijainti.getX();
    }
    
    /**
     * metodi antaa luksen yläreunan koordinaatin
     * @return 
     */
    @Override
    public int getY() {
        return sijainti.getY();
    }
    
    
    /**
     * metodilla saadaan aluksen vasemman yläreunan palan
     * muiden palojen paikat voidaan laskea tämän avullla
     * @return 
     */
    public Pala sijainti() {
        return sijainti;
    }

    @Override
    public void liiku() {
        if (this.suunta != null) {
            this.sijainti.liiku(suunta);
            this.suunta = null;
        }
    }
}
