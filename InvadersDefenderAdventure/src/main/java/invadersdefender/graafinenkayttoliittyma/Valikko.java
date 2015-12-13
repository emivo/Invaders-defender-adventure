package invadersdefender.graafinenkayttoliittyma;

import invadersdefender.sovelluslogiikka.Aseistus;
import invadersdefender.sovelluslogiikka.Peli;
import invadersdefender.sovelluslogiikka.Pelitilanne;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Luokka luo peliin liittyvään JFrame ikkunaan valikkopalkin ja siihen valikot.
 * Valikkopalkki sisältää yksinkertaisia toimitoja peliin liittyen.
 *
 * @author emivo
 */
public class Valikko implements ActionListener {

    private final Peli peli;
    private final JFrame ikkuna;

    /**
     * Luo Valikko-luokan olion.
     *
     * @param peli peli, jolle valikosta voidaan antaa käskyjä
     * @param ikkuna ikkuna, johon valikko ollaan lisäämässä
     */
    public Valikko(Peli peli, JFrame ikkuna) {
        this.peli = peli;
        this.ikkuna = ikkuna;
    }

    /**
     * Metodi luo valikkopalkin ja valikot ja niihin liittyvyät toiminnot.
     *
     * @return Palautta metodin luoman valikkopalkin
     */
    public JMenuBar luoValikko() {

        JMenuBar valikkopalkki = new JMenuBar();
        JMenu paavalikko = new JMenu("Game");
        paavalikko.setMnemonic(KeyEvent.VK_G);

        JMenu aluksenPaivitysvalikko = new JMenu("Spaceship");
        aluksenPaivitysvalikko.setMnemonic(KeyEvent.VK_S);

        JMenu alavalikko = new JMenu("Upgrade guns");
        alavalikko.setMnemonic(KeyEvent.VK_U);
        lisaaAlavalikonPainikkeet(alavalikko, aluksenPaivitysvalikko);

        lisaaValikkopainikkeet(aluksenPaivitysvalikko, paavalikko);

        valikkopalkki.add(paavalikko);
        valikkopalkki.add(aluksenPaivitysvalikko);

        return valikkopalkki;
    }

    private void lisaaAlavalikonPainikkeet(JMenu alavalikko, JMenu aluksenPaivitysvalikko) {
        lisaaValikkopainike(alavalikko, "Double Fire     1000 points", KeyEvent.VK_D);
        lisaaValikkopainike(alavalikko, "Triple Fire     1500 points", KeyEvent.VK_T);
        aluksenPaivitysvalikko.add(alavalikko);
    }

    private void lisaaValikkopainikkeet(JMenu aluksenPaivitysvalikko, JMenu paavalikko) {
        lisaaValikkopainike(aluksenPaivitysvalikko, "Repair spaceship       10 points", KeyEvent.VK_R);

        lisaaValikkopainike(paavalikko, "Start new game", KeyEvent.VK_S);

        lisaaValikkopainike(paavalikko, "Pause", KeyEvent.VK_P);

        lisaaValikkopainike(paavalikko, "Highscores", KeyEvent.VK_H);

        lisaaValikkopainike(paavalikko, "Exit", KeyEvent.VK_E);
    }

    private void lisaaValikkopainike(JMenu paavalikko, String nimi, int ke) {
        JMenuItem lisattava = new JMenuItem(nimi, ke);
        lisattava.addActionListener(this);
        paavalikko.add(lisattava);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (((JMenuItem) e.getSource()).getText()) {
            case "Start new game":
                peli.kaynnistaPeliUuudelleen();
                break;
            case "Pause":
                pause();
                break;
            case "Exit":
                exitPainikkeenTapahtuma();
                break;
            case "Highscores":
                peli.asetaHuipputuloistenKatselutilaan();
                break;
            case "Double Fire     1000 points":
                parannaAseistusta(Aseistus.TUPLA);
                break;
            case "Triple Fire     1500 points":
                parannaAseistusta(Aseistus.TRIPLA);
                break;
            case "Repair spaceship       10 points":
                korjaaOmaaAlusta();
                break;
        }
    }

    private void pause() {
        peli.pysaytaTaiJatkaPysahtynytta();
        peli.paivitaPelinpiirto();
    }

    private void korjaaOmaaAlusta() {
        if (peli.getTilanne() == Pelitilanne.KAYNNISSA
                || peli.getTilanne() == Pelitilanne.PYSAYTETTY) {
            peli.korjaaOmaaAlusta();
            peli.paivitaPelinpiirto();
        }
    }

    private void parannaAseistusta(Aseistus aseistus) {
        if (peli.getTilanne() == Pelitilanne.KAYNNISSA
                || peli.getTilanne() == Pelitilanne.PYSAYTETTY) {
            peli.parannaAluksenAseistusta(aseistus);
            peli.paivitaPelinpiirto();
        }
    }

    private void exitPainikkeenTapahtuma() {
        peli.tallennaTulokset();
        ikkuna.setVisible(false);
        ikkuna.dispose();
        peli.stop();
    }

}
