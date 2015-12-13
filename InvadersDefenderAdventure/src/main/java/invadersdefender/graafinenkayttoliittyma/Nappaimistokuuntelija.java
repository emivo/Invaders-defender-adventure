package invadersdefender.graafinenkayttoliittyma;

import invadersdefender.sovelluslogiikka.Peli;
import invadersdefender.sovelluslogiikka.Pelikentta;
import invadersdefender.sovelluslogiikka.Pelitilanne;
import invadersdefender.sovelluslogiikka.Suunta;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Luokka luodaan kuuntelemaan näppäimistötapahtumia ja antamaan sen perusteella
 * pelille käskyjä, joita pelaaja haluaa suorittaa pelille.
 *
 * @author emivo
 */
public class Nappaimistokuuntelija implements KeyListener {

    private final Peli peli;
    private final Pelikentta pelikentta;

    /**
     * Luo {@code Nappaimistokuuntelija}-luokan olion, jolle asetetaan peli,
     * jolle se antaa käskyt.
     *
     * @param peli pelattava peli, jolle käskyt annetaan
     */
    public Nappaimistokuuntelija(Peli peli) {
        this.peli = peli;
        this.pelikentta = peli.getPelikentta();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // ei mitään tänne
    }

    /**
     * Metodi antaa pelille käskyjä, kun pelille asetettuja kontrolleja
     * käytetään.
     *
     * @param e näppäintapahtuma
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                liiku(Suunta.YLOS);
                break;
            case KeyEvent.VK_DOWN:
                liiku(Suunta.ALAS);
                break;
            case KeyEvent.VK_LEFT:
                liiku(Suunta.VASEN);
                break;
            case KeyEvent.VK_RIGHT:
                liiku(Suunta.OIKEA);
                break;
            case KeyEvent.VK_SPACE:
                ammu();
                break;
            case KeyEvent.VK_P:
                pysaytaPeliTaiJatkaPysahtynytta();
                break;
            case KeyEvent.VK_ENTER:
                pelaajaPainaaEnter();
                break;
            case KeyEvent.VK_X:
                tyhjennaHuipputulokset();
        }
    }

    private void pelaajaPainaaEnter() {
        if (peli.getTilanne() == Pelitilanne.LOPPU) {
            uusiPeli();
        } else {
            pysaytaPeliTaiJatkaPysahtynytta();
        }
    }

    private void uusiPeli() {
        peli.kaynnistaPeliUuudelleen();
        peli.paivitaPelinpiirto();
    }

    private void pysaytaPeliTaiJatkaPysahtynytta() {
        peli.pysaytaTaiJatkaPysahtynytta();
        peli.paivitaPelinpiirto();
    }

    private void ammu() {
        if (peli.isRunning()) {
            pelikentta.alusAmmu(pelikentta.getOmaAlus());
            peli.paivitaPelinpiirto();
        }
    }

    private void liiku(Suunta suunta) {
        if (peli.isRunning()) {
            pelikentta.omaAlusLiiku(suunta);
            peli.paivitaPelinpiirto();
        }
    }

    private void tyhjennaHuipputulokset() {
        if (peli.getTilanne() == Pelitilanne.TULOKSET) {
            peli.tyhjennaHuipputulokset();
            peli.paivitaPelinpiirto();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // ei tee mitään
    }

}
