package invadersdefender.gui;

import invadersdefender.sovelluslogiikka.Alus;
import invadersdefender.sovelluslogiikka.Peli;
import invadersdefender.sovelluslogiikka.Suunta;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author emivo
 */
public class Nappaimistokuuntelija implements KeyListener {

    private Peli peli;

    public Nappaimistokuuntelija(Peli peli) {
        this.peli = peli;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // ei mitÃ¤Ã¤n tÃ¤nne
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
            // kokeilua varten
            case KeyEvent.VK_ENTER:
                if (peli.isPause() ||peli.isRunning()) {
                    pause();
                } else {
                    uusiPeli();
                }
                break;
        }
    }

    private void uusiPeli() {
        peli.kaynnistaPeliUuudelleen();
        peli.paivita();
    }

    private void pause() {
        peli.pause();
        peli.paivita();
    }

    private void ammu() {
        if (peli.isRunning()) {
            peli.alusAmmu(peli.getOmaAlus());
            peli.paivita();
        }
    }

    private void liiku(Suunta suunta) {
        if (peli.isRunning()) {
            peli.omaAlusLiiku(suunta);
            peli.paivita();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // ei mitÃ¤Ã¤n tÃ¤nne
    }

}
