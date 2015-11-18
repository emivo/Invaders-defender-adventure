package invadersdefender.gui;

import invadersdefender.sovelluslogiikka.Peli;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
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
        int ikkunanKoko = peli.getPelikentta().getPelikentanKoko() * palojenKoko + 5;
        peli.setIkkuna(ikkuna);
        
        ikkuna.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        rakennaIkkunanKomponentit(ikkuna.getContentPane());
        ikkuna.setPreferredSize(new Dimension(ikkunanKoko, ikkunanKoko + ikkuna.getJMenuBar().getHeight() + 40));

        ikkuna.pack();
        ikkuna.setVisible(true);
        ikkuna.setResizable(false);

    }

    public PelinPiirtoalusta getPiirtoalusta() {
        return piirtoalusta;
    }

    public void rakennaIkkunanKomponentit(Container container) {
        container.add(piirtoalusta);
        ikkuna.setJMenuBar(new Valikko(peli, ikkuna).luoValikko());

        ikkuna.addKeyListener(new Nappaimistokuuntelija(peli));
    }

}
