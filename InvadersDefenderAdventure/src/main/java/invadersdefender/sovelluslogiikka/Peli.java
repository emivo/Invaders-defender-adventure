package invadersdefender.sovelluslogiikka;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
//import java.util.Timer;

/**
 * @author emivo
 */
public class Peli implements ActionListener {
    /* peli jatkuu kunnes oma alus tuhoutuu */

    private Alus omaAlus;
    private final List<Vihollisolio> viholliset; // voi olla välillä tyhjä
    private final List<Ammus> ammukset; // tyhjä aina välillä
    private final int pelikentanKoko; // Sivunpituus
//    private Timer kello; // TODO mietin vielä miten käytän

    public Peli(int koko, Alus omaAlus) {
        this.pelikentanKoko = koko;

        this.omaAlus = omaAlus;
        this.viholliset = new ArrayList<>();
        this.ammukset = new ArrayList<>();
//        this.kello = new Timer();

    }

    public List<Ammus> getAmmukset() {
        return ammukset;
    }

    public List<Vihollisolio> getViholliset() {
        return viholliset;
    }

    public int getPelikentanKoko() {
        return pelikentanKoko;
    }

    public Alus getOmaAlus() {
        return omaAlus;
    }

    public void alusAmmu(Alus alus) {
        ammukset.add(alus.ammu());
    }


    public boolean osuukoAmmus(Ammus ammus) {
        for (Pala omanaluksenPala : omaAlus.sijainti()) {
            // jos osuu nii peli myös loppuu WOW, sillä one shot one kill ainakin vielä
            if (omanaluksenPala.equals(ammus.getSijainti())) {
                omaAlus = null;
                // lopeta peli
                return true;
            }
        }
        
        Iterator<Vihollisolio> it = viholliset.iterator();
        while (it.hasNext()) {
            Vihollisolio vihu = it.next();
            // tätä kenties täytyy tehostaa myöhemmin, sillä O(n*m*l)
            for (Pala vihunPala : vihu.sijainti()) {
                if (vihunPala.equals(ammus.getSijainti())) {
                    it.remove();
                    return true;
                }
            }
        }
        return false;
    }

    public void vihollisetTulevatEsille() {
        // kuinka monta vihollista tulee
        // KESKEN
        int maara = 3;
        int alustenKoko = omaAlus.getKoko();
        for (int i = 0; i < maara; i++) {

            viholliset.add(new Vihollisolio(1 + (i * (alustenKoko + 1)), 1));
        }
    }

    public void ammuksetLiiku() {
        Iterator<Ammus> it = ammukset.iterator();
        while (it.hasNext()) {
            Ammus a = it.next();
            a.liiku();
            // leveyssuuntaa ei tarvitse tarkistaa sillä ammuksien ei tulisi poistua kentän reunalta
            if (a.getSijainti().getY() < 0 || a.getSijainti().getY() > pelikentanKoko) {
                it.remove();
            } else if (osuukoAmmus(a)) {
                it.remove();
            }
        }
    }

    public void omaAlusLiiku() {
        Suunta suuntaJohonAlusOnLiikkumassa = omaAlus.getSuunta();
        omaAlus.liiku();
        if (!tarkistaVoikoLiikkua(omaAlus)) {
            peruLiikumminen(omaAlus, suuntaJohonAlusOnLiikkumassa);
        }
    }

    private void peruLiikumminen(Alus alus, Suunta suunta) {
        switch (suunta) {
            case ALAS:
                alus.setSuunta(Suunta.YLOS);
                break;
            case YLOS:
                alus.setSuunta(Suunta.ALAS);
                break;
            case OIKEA:
                alus.setSuunta(Suunta.VASEN);
                break;
            case VASEN:
                alus.setSuunta(Suunta.OIKEA);
                break;
        }
        alus.liiku();
    }

    private boolean tarkistaVoikoLiikkua(Alus alus) {
        for (Pala pala : alus.sijainti()) {
            if (pala.getX() < 0 || pala.getX() > pelikentanKoko
                    || pala.getY() < 0 || pala.getY() > pelikentanKoko) {
                return false;
            }
        }
        return true;
    }

    public void vihollisetLiiku() {
        // kaikilla vihollisilla sama suunta
        if (!viholliset.isEmpty()) {
            Suunta suuntaJohonOliotAikovatLiikkua = viholliset.get(0).getSuunta();
            boolean voikoLiikkua = true;
            for (Vihollisolio o : viholliset) {
                o.liiku();
                voikoLiikkua = voikoLiikkua && tarkistaVoikoLiikkua(o);
            }
            if (!voikoLiikkua) {
                for (Vihollisolio o : viholliset) {
                    peruLiikumminen(o, suuntaJohonOliotAikovatLiikkua);
                }
            }
        }
    }

    public void jokuVihollinenAmpuu() {
        if (!viholliset.isEmpty()) {
            alusAmmu(viholliset.get(new Random().nextInt(viholliset.size())));
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
