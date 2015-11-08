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
    
    private JFrame frame;
    private Peli peli;
    private final int palojenKoko;
    private final PelinPiirtoalusta piirtoalusta;

    public Kayttoliittyma (Peli peli, int palojenKoko) {
        this.palojenKoko = palojenKoko;
        this.peli = peli;
        this.piirtoalusta = new PelinPiirtoalusta(peli, palojenKoko);
    }
    
    

    @Override
    public void run() {
        frame = new JFrame("Invaders defender adventures");
        int ikkunanKoko = peli.getPelikentanKoko() * palojenKoko + 10;
        
        frame.setPreferredSize(new Dimension(ikkunanKoko, ikkunanKoko + 10));
 
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        rakennaIkkunanKomponentit(frame.getContentPane());
        
        frame.pack();
        frame.setVisible(true);
    
    }

    public PelinPiirtoalusta getPiirtoalusta() {
        return piirtoalusta;
    }
    
    public void rakennaIkkunanKomponentit(Container container) {
        container.add(piirtoalusta);
        
        frame.addKeyListener(new Nappaimistokuuntelija(peli));
    }
}
