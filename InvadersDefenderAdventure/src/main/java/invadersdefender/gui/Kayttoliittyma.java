package invadersdefender.gui;

import invadersdefender.sovelluslogiikka.Peli;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Luokka luo pelille JFrame ikkunan ja lisää Nappaimistokuuntelijan ikkunalle
 * @author emivo
 */
public class Kayttoliittyma implements Runnable {

    private JFrame ikkuna;
    private final Peli peli;
    private final int palojenKoko;
    private final PelinPiirtoalusta piirtoalusta;

    public Kayttoliittyma(Peli peli, int palojenKoko) {
        this.palojenKoko = palojenKoko;
        this.peli = peli;
        this.piirtoalusta = new PelinPiirtoalusta(peli, palojenKoko);
    }

    @Override
    public void run() {
        ikkuna = new JFrame("Invaders defender adventures");
        int ikkunanLeveys = peli.getPelikentta().getPelikentanLeveys()* palojenKoko + 5;
        int ikkunanKorkeus = peli.getPelikentta().getPelikentanKorkeus() * palojenKoko + 40;
        peli.setIkkuna(ikkuna);
        piirtoalusta.setIkkuna(ikkuna);
        
        ikkuna.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        rakennaIkkunanKomponentit();
        ikkuna.setPreferredSize(new Dimension(ikkunanLeveys, ikkunanKorkeus + ikkuna.getJMenuBar().getHeight() ));

        ikkuna.pack();
        ikkuna.setVisible(true);
        ikkuna.setResizable(false);

    }

    public PelinPiirtoalusta getPiirtoalusta() {
        return piirtoalusta;
    }

    /**
     * Metodi lisää pelinpiirtoalustan ikkunaan sekä valikon ja näppäimistökuuntelijan
     */
    public void rakennaIkkunanKomponentit() {
        
        ikkuna.getContentPane().add(piirtoalusta);
        ikkuna.setJMenuBar(new Valikko(peli, ikkuna).luoValikko());

        ikkuna.addKeyListener(new Nappaimistokuuntelija(peli));
    }

}
