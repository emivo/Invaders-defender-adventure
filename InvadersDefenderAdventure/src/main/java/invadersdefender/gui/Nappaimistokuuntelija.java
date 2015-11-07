package invadersdefender.gui;

import invadersdefender.sovelluslogiikka.Alus;
import invadersdefender.sovelluslogiikka.Peli;
import invadersdefender.sovelluslogiikka.Suunta;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Emil
 */
public class Nappaimistokuuntelija implements KeyListener {
    
    private Alus omaAlus;
    private Peli peli;

    public Nappaimistokuuntelija(Peli peli) {
        this.omaAlus = peli.getOmaAlus();
        this.peli = peli;
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
                peli.alusAmmu(omaAlus);
                peli.paivita();
                break;
        }
    }

    private void liiku(Suunta suunta) {
        omaAlus.setSuunta(suunta);
        peli.omaAlusLiiku();
        peli.paivita();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // ei mitään tänne
    }

}
