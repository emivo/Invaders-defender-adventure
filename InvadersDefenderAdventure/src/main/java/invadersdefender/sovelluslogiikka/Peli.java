package invadersdefender.sovelluslogiikka;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;

/**
 * @author emivo
 */
public class Peli implements ActionListener {
    /* peli jatkuu kunnes oma alus tuhoutuu */
    private Alus omaAlus;
    private List<Vihollisolio> viholliset; // voi olla välillä tyhjä
    private List<Ammus> ammukset; // tyhjä aina välillä
    private int pelikentanKoko; // Sivunpituus
    private Timer kello;

    public Peli(int koko) {
        this.pelikentanKoko = koko;
        // oman aluksen alkusijainti alas keskelle
        this.omaAlus = new Alus(pelikentanKoko/2 - 1, pelikentanKoko - 1);
        this.viholliset = new ArrayList<>();
        this.ammukset = new ArrayList<>();
        this.kello = new Timer();
        
    }
    
    private void osuukoAmmukset() {
        Iterator<Ammus> it = ammukset.iterator();
        while (it.hasNext()) {
            // jos ammus poistuu pelialueelta se poistuu listasta ja katoaa
            // jos ammus osuu alukseen alus tuhoutuu ja ammus tuhoutuu
        }
    }
    
    private void ammuksetLiiku() {
        for (Ammus a : ammukset) {
            a.liiku();
        }
    }
    
    private void vihollisetLiiku() {
        for (Vihollisolio o : viholliset) {
            // vihollisten liikkuminen täytyy vielä keksiä
            // esim vasenvasen alas oikea oikea alas...
            o.liiku();
        }
    }

    
    // älä tee tätä vielä
    // kun näyppäimistössä painetaan näppäintä alus liikkuu
    // kun aikaa on kulunt x millisekunttia ammukset liikkuu
    // kun aikaa on kulunut y millisekunttia viholliset liikkuu
    @Override
    public void actionPerformed(ActionEvent e) {
        if (omaAlus == null) {
            // Peliloppuu
        }
        
    }
}
