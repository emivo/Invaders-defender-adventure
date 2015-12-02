package invadersdefender.gui;

import invadersdefender.sovelluslogiikka.Aseistus;
import invadersdefender.sovelluslogiikka.Peli;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Luokka luo peliin liittyvään JFrame ikkunaan valikkopalkin ja siihen valikot.
 * Valikkopalkki sisältää yksinkertaisia toimitoja peliin liittyen
 *
 * @author emivo
 */
public class Valikko implements ActionListener {
    
    private Peli peli;
    private JFrame ikkuna;
    
    public Valikko(Peli peli, JFrame ikkuna) {
        this.peli = peli;
        this.ikkuna = ikkuna;
    }

    /**
     * Metodi luo valikkopalkin ja valikot ja niihin liittyvyät toiminnot
     *
     * @return
     */
    public JMenuBar luoValikko() {
        
        JMenuBar valikkopalkki = new JMenuBar();
        JMenu paavalikko = new JMenu("Game");
        paavalikko.setMnemonic(KeyEvent.VK_G);
        
        JMenu aluksenPaivitysvalikko = new JMenu("Spaceship");
        aluksenPaivitysvalikko.setMnemonic(KeyEvent.VK_S);
        
        JMenu alavalikko = new JMenu("Upgrade guns");
        alavalikko.setMnemonic(KeyEvent.VK_U);
        lisaaValikkopainike(alavalikko, "Double Fire     1000 points", KeyEvent.VK_D);
        lisaaValikkopainike(alavalikko, "Triple Fire     1500 points", KeyEvent.VK_T);
        aluksenPaivitysvalikko.add(alavalikko);
        
        lisaaValikkopainike(aluksenPaivitysvalikko, "Repair spaceship       10 points", KeyEvent.VK_R);
        
        
        lisaaValikkopainike(paavalikko, "Start new game", KeyEvent.VK_S);
        
        lisaaValikkopainike(paavalikko, "Pause", KeyEvent.VK_P);
        
        lisaaValikkopainike(paavalikko, "Highscores", KeyEvent.VK_H);
        
        lisaaValikkopainike(paavalikko, "Exit", KeyEvent.VK_E);
        
        valikkopalkki.add(paavalikko);
        valikkopalkki.add(aluksenPaivitysvalikko);
        
        return valikkopalkki;
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
                peli.pause();
                peli.paivitaPelinpiirto();
                break;
            case "Exit":
                peli.tallennaTulokset();
                ikkuna.setVisible(false);
                ikkuna.dispose();
                peli.stop();
                break;
            case "Highscores":
                peli.asetaHuipputuloistenKatselutilaan();
                break;
            case "Double Fire     1000 points":
                peli.parannaAluksenAseistusta(Aseistus.TUPLA);
                peli.paivitaPelinpiirto();
                break;
            case "Triple Fire     1500 points":
                peli.parannaAluksenAseistusta(Aseistus.TRIPLA);
                peli.paivitaPelinpiirto();
                break;
            case "Repair spaceship       10 points":
                peli.korjaaOmaaAlusta();
                peli.paivitaPelinpiirto();
                break;
        }
    }
    
}
