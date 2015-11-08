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
 * @author emivo
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

        if (peli.isRunning()) {
            piirraPeli(graphics);
        } else if (peli.isPause()) {
            piirraPause(graphics);
        } else {
            piirraPeliLoppu(graphics);
        }

    }

    private void piirraPeli(Graphics graphics) {
        piirraOmaAlus(graphics);
        piirraViholliset(graphics);
        piirraAmmukset(graphics);

        piirraPistetilanne(graphics);

    }

    private void piirraPistetilanne(Graphics graphics) {
        // pisteet oikeaan ylänurkkaan
        String pisteet = "" + peli.getPisteet();
        graphics.drawString(pisteet, palojenKoko, palojenKoko);
    }

    private void piirraAmmukset(Graphics graphics) {
        // piirrä ammukset
        graphics.setColor(Color.red);
        for (Ammus ammus : peli.getAmmukset()) {
            piirraLiikkuva(graphics, ammus);
        }
    }

    private void piirraViholliset(Graphics graphics) {
        // piirrä vihollisoliot
        graphics.setColor(Color.GREEN);
        for (Vihollisolio vihu : peli.getViholliset()) {
            piirraLiikkuva(graphics, vihu);
        }
    }

    private void piirraOmaAlus(Graphics graphics) {

        // piirrä oma alus [TODO] Kunnolliset kuviot
        graphics.setColor(Color.BLACK);
        piirraLiikkuva(graphics, peli.getOmaAlus());
    }

    private void piirraLiikkuva(Graphics graphics, Liikkuva liikkuva) {
        int koko = 1;
        if (liikkuva.getClass() != Ammus.class) {
            koko = ((Alus) liikkuva).getKoko();
        }
        piirra(graphics, liikkuva, liikkuva.getX() * palojenKoko, liikkuva.getY() * palojenKoko, koko * palojenKoko);
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

    private void piirraPeliLoppu(Graphics graphics) {
        graphics.setColor(Color.red);
        int paikka = (peli.getPelikentanKoko() + palojenKoko) / 2;
        graphics.drawString("Game Over", paikka, paikka);
        String pisteet = "Your score: " + peli.getPisteet();
        graphics.drawString(pisteet, paikka, paikka + palojenKoko + 3);
        graphics.drawString("Press enter to start again", paikka, paikka + (palojenKoko + 3) * 2);
        
    }

    private void piirraPause(Graphics graphics) {
        graphics.setColor(Color.red);
        int paikka = (peli.getPelikentanKoko() + palojenKoko) / 2;
        graphics.drawString("Pause. Press enter to continue", paikka, paikka);
    }
}
