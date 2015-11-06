package invadersdefender.sovelluslogiikka;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
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

    public Peli(int koko, Alus omaAlus) {
        this.pelikentanKoko = koko;
        
        this.omaAlus = omaAlus;
        this.viholliset = new ArrayList<>();
        this.ammukset = new ArrayList<>();
        this.kello = new Timer();
        
    }
    
    
    public void alusAmpuu(Alus alus) {
        ammukset.add(alus.ammu());
    }
    
    public void osuukoAmmukset() {
        Iterator<Ammus> it = ammukset.iterator();
        while (it.hasNext()) {
            // jos ammus poistuu pelialueelta se poistuu listasta ja katoaa
            // jos ammus osuu alukseen alus tuhoutuu ja ammus tuhoutuu
        }
    }
    
    public void vihollisetTulevatEsille() {
        // kuinka monta vihollista tulee
        int maara = 3;
        int alustenKoko = omaAlus.getKoko();
        for (int i = 0; i < maara; i++) {
            
            viholliset.add(new Vihollisolio(1 + (i * alustenKoko) , 1));
        }
    }
    
    public void ammuksetLiiku() {
        for (Ammus a : ammukset) {
            a.liiku();
        }
    }
    
    public void vihollisetLiiku() {
        for (Vihollisolio o : viholliset) {
            o.liiku();
        }
    }
    
    public void jokuVihollinenAmpuu() {
        viholliset.get(new Random().nextInt(viholliset.size())).ammu(); // ompas ruma koodinpätkä
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
