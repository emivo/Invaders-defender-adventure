package invadersdefender.gui;

import invadersdefender.sovelluslogiikka.Peli;
import invadersdefender.sovelluslogiikka.Pelikentta;
import invadersdefender.sovelluslogiikka.Pelitilanne;
import invadersdefender.sovelluslogiikka.Suunta;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Luokka kuuntelee näppäimistötapahtumia ja antaa sen perusteella pelille
 * käskyjä, mitä tulee tehdä. Ammu käsky annetaan vasta onrelease
 *
 * @author emivo
 */
public class Nappaimistokuuntelija implements KeyListener {

    private Peli peli;
    private Pelikentta pelikentta;

    public Nappaimistokuuntelija(Peli peli) {
        this.peli = peli;
        this.pelikentta = peli.getPelikentta();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // ei mitään tänne
    }

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
                pause();
                break;
            case KeyEvent.VK_ENTER:
                if (peli.getTilanne() == Pelitilanne.LOPPU) {
                    uusiPeli();
                } else {
                    pause();
                }
                break;
            case KeyEvent.VK_X:
                if (peli.getTilanne() == Pelitilanne.TULOKSET) {
                    tyhjennaHuipputulokset();
                }
        }
    }

    private void uusiPeli() {
        peli.kaynnistaPeliUuudelleen();
        peli.paivitaPelinpiirto();
    }

    private void pause() {
        peli.pause();
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
        peli.tyhjennaHuipputulokset();
        peli.paivitaPelinpiirto();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // ei tee mitään
    }

}
