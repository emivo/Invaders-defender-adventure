package invadersdefender.gui;

import invadersdefender.sovelluslogiikka.Alus;
import invadersdefender.sovelluslogiikka.Ammus;
import invadersdefender.sovelluslogiikka.Liikkuva;
import invadersdefender.sovelluslogiikka.Peli;
import invadersdefender.sovelluslogiikka.Vihollisolio;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author Emil
 */
public class PelinPiirtoalusta extends JPanel {

    private Peli peli;
    private int palojenKoko;

    public PelinPiirtoalusta(Peli peli, int palojenKoko) {
        this.peli = peli;
        this.palojenKoko = palojenKoko;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        // [Ei vielä] piirrä tausta
        
        // piirrä oma alus
        graphics.setColor(Color.BLACK);
        piirraLiikkuva(graphics, peli.getOmaAlus());
        // piirrä vihollisoliot
        graphics.setColor(Color.GREEN);
        for (Vihollisolio vihu : peli.getViholliset()) {
            piirraLiikkuva(graphics, vihu);
        }
        // piirrä ammukset
        graphics.setColor(Color.red);
        for (Ammus ammus : peli.getAmmukset()) {
            piirraLiikkuva(graphics, ammus);
        }

    }

    private void piirraLiikkuva(Graphics graphics, Liikkuva liikkuva) {
        int koko = 1;
        if (liikkuva.getClass() != Ammus.class) {
            koko = ((Alus) liikkuva).getKoko();
        }
        piirra(graphics, liikkuva, liikkuva.getX()* palojenKoko, liikkuva.getY() * palojenKoko, koko * palojenKoko);
    }
    
    private void piirra(Graphics graphics, Liikkuva liikkuva, int x, int y, int sivunpituus) {
        if (liikkuva.getClass() == Ammus.class) {
            piirraYmpyra(graphics, x, y, sivunpituus);
        } else {
            piirraNelio(graphics, x, y, sivunpituus);
        }
    }
    
    private void piirraNelio(Graphics graphics, int x, int y, int sivunpituus) {
        graphics.fill3DRect(x, y, sivunpituus, sivunpituus, true);
    }
    
    private void piirraYmpyra(Graphics graphics, int x, int y, int sivunpituus) {
        graphics.fillOval(x, y, sivunpituus, sivunpituus);
    }

    public void paivitaPiirto() {
        super.repaint();
    }
}
