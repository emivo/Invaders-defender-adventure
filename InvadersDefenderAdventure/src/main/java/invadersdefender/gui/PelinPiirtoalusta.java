package invadersdefender.gui;

import invadersdefender.sovelluslogiikka.Ammus;
import invadersdefender.sovelluslogiikka.Liikkuva;
import invadersdefender.sovelluslogiikka.Peli;
import invadersdefender.sovelluslogiikka.Suunta;
import invadersdefender.sovelluslogiikka.Vihollisolio;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import javax.imageio.ImageIO;
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
        piirraTausta(graphics);

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
            URL kuvanOsoite;
            if (ammus.getSuunta() == Suunta.YLOS) {
                kuvanOsoite = haeOsoite("/ammus.png");
            } else {
                kuvanOsoite = haeOsoite("/vihollisenammus.png");
            }
            piirraLiikkuva(graphics, ammus, lueKuva(kuvanOsoite));
//            piirraYmpyra(graphics, ammus.getX() * palojenKoko, ammus.getY() * palojenKoko, palojenKoko);
        }
    }

    private void piirraViholliset(Graphics graphics) {
        // piirrä vihollisoliot
        graphics.setColor(Color.GREEN);
        for (Vihollisolio vihu : peli.getViholliset()) {
            URL kuvanOsoite = haeOsoite("/vihollisolio.png");
            piirraLiikkuva(graphics, vihu, lueKuva(kuvanOsoite));
        }
    }

    private URL haeOsoite(String tiedostonNimi) {
        return getClass().getResource(tiedostonNimi);
    }

    private Image lueKuva(URL osoite) {
        try {
            return ImageIO.read(osoite);
        } catch (Exception e) {
            return null;
        }
    }

    private void piirraKuva(Graphics graphics, int x, int y, int koko, Image kuva) {
        if (kuva != null) {
            graphics.drawImage(kuva, x, y, koko, koko, this);
        } else {
            // KÄSITTELE VIRHE
        }
    }

    private void piirraOmaAlus(Graphics graphics) {
        URL kuvanOsoite = haeOsoite("/omaalus.png");
        piirraLiikkuva(graphics, peli.getOmaAlus(), lueKuva(kuvanOsoite));
    }

//    private void piirraYmpyra(Graphics graphics, int x, int y, int sivunpituus) {
//        graphics.fillOval(x, y, sivunpituus, sivunpituus);
//    }
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

    private void piirraTausta(Graphics graphics) {
        URL taustanOsoite = haeOsoite("/tausta.jpg");
        piirraKuva(graphics, 0, 0, peli.getPelikentanKoko() * palojenKoko, lueKuva(taustanOsoite));
    }

    private void piirraLiikkuva(Graphics graphics, Liikkuva liikkuva, Image kuva) {
        piirraKuva(graphics, liikkuva.getX() * palojenKoko, liikkuva.getY() * palojenKoko, liikkuva.getKoko() * palojenKoko, kuva);
    }
}
