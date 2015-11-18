package invadersdefender.gui;

import invadersdefender.sovelluslogiikka.Ammus;
import invadersdefender.sovelluslogiikka.Liikkuva;
import invadersdefender.sovelluslogiikka.Peli;
import invadersdefender.sovelluslogiikka.Pelikentta;
import invadersdefender.sovelluslogiikka.Pelitilanne;
import invadersdefender.sovelluslogiikka.Suunta;
import invadersdefender.sovelluslogiikka.Vihollisolio;
import invadersdefender.sovelluslogiikka.huipputulokset.Pelaaja;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author emivo
 */
public class PelinPiirtoalusta extends JPanel {

    private Peli peli;
    private Pelikentta pelikentta;
    private int palojenKoko;
    private Map<String, Image> kuvat;

    public PelinPiirtoalusta(Peli peli, int palojenKoko) {
        this.peli = peli;
        this.pelikentta = peli.getPelikentta();
        this.palojenKoko = palojenKoko;
        this.kuvat = new HashMap<>();

        lataaKuvat();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (peli.getTilanne() == Pelitilanne.KAYNNISSA) {
            piirraPeli(graphics);
        } else if (peli.getTilanne() == Pelitilanne.PAUSE) {
            piirraPause(graphics);
        } else if (peli.getTilanne() == Pelitilanne.TULOKSET) {
            piirraHuipputulokset(graphics);
        }else {
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
        graphics.drawString(pisteet, palojenKoko, (int) (palojenKoko * 1.5));
    }

    private void piirraAmmukset(Graphics graphics) {
        // piirrä ammukset
        graphics.setColor(Color.red);
        for (Ammus ammus : pelikentta.getAmmukset()) {
            Image kuva;
            if (ammus.getSuunta() == Suunta.YLOS) {
                kuva = kuvat.get("ammus");
            } else {
                kuva = kuvat.get("vihollisenammus");
            }
            piirraLiikkuva(graphics, ammus, kuva);
        }
    }

    private void piirraViholliset(Graphics graphics) {
        // piirrä vihollisoliot
        graphics.setColor(Color.GREEN);
        for (Vihollisolio vihu : pelikentta.getViholliset()) {
            piirraLiikkuva(graphics, vihu, kuvat.get("vihollisolio"));
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
        piirraLiikkuva(graphics, pelikentta.getOmaAlus(), kuvat.get("omaalus"));
    }

    public void paivitaPiirto() {
        super.repaint();
    }

    private void piirraPeliLoppu(Graphics graphics) {
        graphics.setColor(Color.red);
        int paikka = (pelikentta.getPelikentanKoko() + palojenKoko) / 2;
        graphics.drawString("Game Over", paikka, paikka);
        String pisteet = "Your score: " + peli.getPisteet();
        graphics.drawString(pisteet, paikka, paikka + palojenKoko + 3);
        graphics.drawString("Press enter to start again", paikka, paikka + (palojenKoko + 3) * 2);
    }

    private void piirraPause(Graphics graphics) {
        graphics.setColor(Color.red);
        int paikka = (pelikentta.getPelikentanKoko() + palojenKoko) / 2;
        graphics.drawString("Pause. Press enter to continue", paikka, paikka);
    }

    private void piirraTausta(Graphics graphics) {
        piirraKuva(graphics, 0, 0, pelikentta.getPelikentanKoko() * palojenKoko, kuvat.get("tausta"));
    }

    private void piirraLiikkuva(Graphics graphics, Liikkuva liikkuva, Image kuva) {
        piirraKuva(graphics, liikkuva.getX() * palojenKoko, liikkuva.getY() * palojenKoko, liikkuva.getKoko() * palojenKoko, kuva);
    }

    private void lataaKuvat() {
        kuvat.put("ammus", lueKuva(haeOsoite("/ammus.png")));
        kuvat.put("vihollisenammus", lueKuva(haeOsoite("/vihollisenammus.png")));
        kuvat.put("tausta", lueKuva(haeOsoite("/tausta.jpg")));
        kuvat.put("omaalus", lueKuva(haeOsoite("/omaalus.png")));
        kuvat.put("vihollisolio", lueKuva(haeOsoite("/vihollisolio.png")));
    }

    public void piirraHuipputulokset(Graphics graphics) {
        graphics.setColor(Color.red);
        int paikka = (pelikentta.getPelikentanKoko() + palojenKoko) / 2;
        PriorityQueue<Pelaaja> uusi = new PriorityQueue<>();
        Pelaaja pelaaja = peli.getHuipputulokset().getTulokset().poll();
        int i = 1;
        while (pelaaja != null) {
            uusi.add(pelaaja);
            graphics.drawString(luoRivi(i, pelaaja), paikka, paikka + (i-1) * 2*palojenKoko);
            pelaaja = peli.getHuipputulokset().getTulokset().poll();
            i++;
        }
        peli.getHuipputulokset().setTulokset(uusi);

    }

    private String luoRivi(int i, Pelaaja pelaaja) {
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append(" ");
        sb.append(pelaaja.getNimi());
        sb.append(" ");
        sb.append(pelaaja.getTulos());
        return sb.toString();
    }
}
