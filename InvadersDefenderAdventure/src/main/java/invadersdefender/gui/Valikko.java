package invadersdefender.gui;

import invadersdefender.sovelluslogiikka.Peli;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Luokka luo peliin liittyvään JFrame ikkunaan valikkopalkin ja siihen valikot. Valikkopalkki sisältää yksinkertaisia toimitoja peliin liittyen
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
     * @return 
     */
    public JMenuBar luoValikko() {

        JMenuBar valikkopalkki = new JMenuBar();
        JMenu paavalikko = new JMenu("Game");
        paavalikko.setMnemonic(KeyEvent.VK_G);

        valikkopalkki.add(paavalikko);

        lisaaValikkopainike(paavalikko, "Start new game", KeyEvent.VK_S);

        lisaaValikkopainike(paavalikko, "Pause", KeyEvent.VK_P);
        
        lisaaValikkopainike(paavalikko, "Highscores", KeyEvent.VK_H);

        lisaaValikkopainike(paavalikko, "Exit", KeyEvent.VK_E);

        

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
                peli.paivita();
                break;
            case "Exit":
                peli.tallennaTulokset();
                ikkuna.setVisible(false);
                ikkuna.dispose();
                peli.stop();
                break;
            case "Highscores":
                peli.huipputulokset();
                break;
        }
    }

}
