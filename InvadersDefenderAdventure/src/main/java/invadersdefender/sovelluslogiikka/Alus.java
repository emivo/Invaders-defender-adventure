package invadersdefender.sovelluslogiikka;

import java.util.ArrayList;
import java.util.List;

/**
 * @author emivo
 */
public class Alus {
    
    private int koko;
    private List<Pala> aluksenPalat;
    private int alkuX;
    private int alkuY;
    // tästä laajennetaan varmaan sitten vihollinen ja oma alus?

    public Alus(int alkuX, int alkuY) {
        koko = 9; // täytyy olla neliö
        /*
        Olisi kenties helpompi aluksi pitää kaikki liikkuvat asiat yhden kokoisina
        
        Toisaalta saatan haluta hieman tarkempaa ohjausta sekä osumista
        
        */
        aluksenPalat = new ArrayList<>();
        for (int i = 0; i < koko; i++) {
            // Luodaan palat alku pisteestä oikealle ja alas
            aluksenPalat.add(new Pala(alkuX + i, alkuY + (i % (int) Math.sqrt(koko))));
        }
    }
    
    public void liiku(Suunta suunta) {
        for (int i = 0; i < koko; i++) {
            aluksenPalat.get(i).liiku(suunta);
        }
    }
    
    public Ammus ammu() {
        return new Ammus(aluksenPalat.get((int)Math.sqrt(koko)/2).getX(), aluksenPalat.get(0).getY() + 1);
    }
    
}
