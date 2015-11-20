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

    public Huipputulokset() {
        tulokset = new ArrayList<>();
    }

    public boolean lataaTulokset() {
        try {
            try (FileInputStream fis = new FileInputStream("huipputulokset.data"); ObjectInputStream ois = new ObjectInputStream(fis)) {
                tulokset = (ArrayList<Pelaaja>) ois.readObject();
                return true;
            }
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }

    public Pelaaja getViimeinen() {
        if (tulokset.isEmpty()) {
            return null;
        } else {
            return tulokset.get(tulokset.size() - 1);
        }
    }

    public void lisaaTulos(String nimi, int tulos) {
        Pelaaja pelaaja = new Pelaaja(tulos, nimi);
        tulokset.add(pelaaja);
        if (tulokset.size() > 10) {
            sort(10);
        } else {
            sort(tulokset.size());
        }
    }

    private void sort(int koko) {
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

    public void tallennaTulokset() {
        try {
            try (FileOutputStream fos = new FileOutputStream("huipputulokset.data"); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(tulokset);
                oos.close();
                fos.close();
            }
        } catch (Exception e) {
            // tänne ei pitäisi voida päätyä
        }
    }

}
