package invadersdefender.sovelluslogiikka.huipputulokset;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Luokka lukee, tallentaa ja kirjaa Pelaajia taulukkoon. Taulukossa
 * maksimimäärä pelaajia on 10.
 *
 * @author emivo
 */
public class Huipputulokset {

    private ArrayList<Pelaaja> tulokset;

    /**
     * Luo {@code Huipputulokset}-luokan olion.
     */
    public Huipputulokset() {
        tulokset = new ArrayList<>();
    }

    /**
     * Metodi hakee tietokoneen paikallisesta muistista, onko edellisten
     * pelikertojen tuloksia on tallennettu.
     *
     * @return {@code true} jos huipputuloslista löytyy
     */
    public boolean lataaTulokset() {
        try {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("huipputulokset.data"))) {
                tulokset = (ArrayList<Pelaaja>) objectInputStream.readObject();
                return true;
            }
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Metodi hakee listan viimeisen pelaajan.
     *
     * @return Pelaaja, joka on huipputuloslistan viimeinen
     */
    public Pelaaja getViimeinen() {
        if (tulokset.isEmpty()) {
            return null;
        } else {
            return tulokset.get(tulokset.size() - 1);
        }
    }

    /**
     * Metodi lisää pelaajan tuloksen listaan.
     *
     * @param nimi Tallennettava nimi
     * @param tulos pistemäärä, jonka pelaaja on saanut
     */
    public void lisaaTulos(String nimi, int tulos) {
        Pelaaja pelaaja = new Pelaaja(tulos, nimi);
        tulokset.add(pelaaja);
        if (tulokset.size() > 10) {
            jarjesta(10);
        } else {
            jarjesta(tulokset.size());
        }
    }

    private void jarjesta(int koko) {
        Pelaaja[] pelaajat = new Pelaaja[koko];
        pelaajat = tulokset.toArray(pelaajat);
        Arrays.sort(pelaajat);
        tulokset.clear();
        for (int i = 0; i < pelaajat.length; ++i) {
            tulokset.add(pelaajat[i]);
        }
    }

    public ArrayList<Pelaaja> getTulokset() {
        return tulokset;
    }

    /**
     * Metodi tallentaa lista olion tietokoneen paikalliseen muistiin.
     */
    public void tallennaTulokset() {
        try {
            try (FileOutputStream fileOutputStream = new FileOutputStream("huipputulokset.data"); 
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
                objectOutputStream.writeObject(tulokset);
                objectOutputStream.close();
                fileOutputStream.close();
            }
        } catch (Exception e) {
            // tänne ei pitäisi voida päätyä
        }
    }

    /**
     * Poistaa kaikki pelaajat listasta, jonka jälkeen tallentaa listan.
     */
    public void tyhjennaHuipputulokset() {
        tulokset.clear();
        tallennaTulokset();
    }

}
