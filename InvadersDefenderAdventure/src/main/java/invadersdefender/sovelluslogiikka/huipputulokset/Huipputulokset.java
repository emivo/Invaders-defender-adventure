package invadersdefender.sovelluslogiikka.huipputulokset;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 *
 * @author emivo
 */
public class Huipputulokset {

    private PriorityQueue<Pelaaja> tulokset;
    private Pelaaja viimeinen;

    public Huipputulokset() {
        try {
            try (FileInputStream fis = new FileInputStream("huipputulokset.data"); ObjectInputStream ois = new ObjectInputStream(fis)) {
                tulokset = (PriorityQueue<Pelaaja>) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            tulokset = new PriorityQueue<>();
        }
        viimeinen = tulokset.poll();

        PriorityQueue<Pelaaja> uusiTulokset = new PriorityQueue<>();
        boolean jatka = true;
        while (jatka) {
            Pelaaja tmp = tulokset.poll();
            uusiTulokset.add(viimeinen);
            jatka = false;
            if (tmp != null) {
                viimeinen = tmp;
                jatka = true;
            }
        }
        tulokset = uusiTulokset;
    }

    public Pelaaja getViimeinen() {
        return viimeinen;
    }

    public void lisaaTulos(String nimi, int tulos) {
        Pelaaja pelaaja = new Pelaaja(tulos, nimi);
        tulokset.add(pelaaja);
        if (tulokset.size() > 10) {
            // poista huonoint
            PriorityQueue<Pelaaja> uusiTulokset = new PriorityQueue<>();
            for (int i = 0; i < 10; ++i) {
                viimeinen = tulokset.poll();
                uusiTulokset.add(viimeinen);
            }
            tulokset = uusiTulokset;
        } else if (viimeinen.getTulos() > tulos) {
            viimeinen = pelaaja;
        }
    }

    public PriorityQueue<Pelaaja> getTulokset() {
        return tulokset;
    }

    public void setTulokset(PriorityQueue<Pelaaja> tulokset) {
        this.tulokset = tulokset;
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
