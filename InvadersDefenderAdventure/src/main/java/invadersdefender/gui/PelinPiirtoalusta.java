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
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Luokka tarjoaa mahdollisuuden piirtää kuvina kostruktiossa annetun pelin
 *
 * @author emivo
 */
public class PelinPiirtoalusta extends JPanel {

    private Peli peli;
    private Pelikentta pelikentta;
    private int palojenKoko;
    private Map<String, BufferedImage> kuvat;
    private JFrame ikkuna;

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

        piirraTausta(graphics);

        if (peli.getTilanne() == Pelitilanne.KAYNNISSA) {
            piirraPeli(graphics);
        } else if (peli.getTilanne() == Pelitilanne.PAUSE) {
            piirraPause(graphics);
        } else if (peli.getTilanne() == Pelitilanne.TULOKSET) {
            piirraHuipputulokset(graphics);
        } else if (peli.getTilanne() == Pelitilanne.ALKURUUTU) {
            piirraPelinAlkuruutu(graphics);
        } else {
            piirraPeliLoppu(graphics);
        }

    }

    public void setIkkuna(JFrame ikkuna) {
        this.ikkuna = ikkuna;
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

    private BufferedImage lueKuva(URL osoite) {
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
            virhe();
        }
    }

    private void piirraOmaAlus(Graphics graphics) {
        piirraLiikkuva(graphics, pelikentta.getOmaAlus(), kuvat.get("omaalus"));
    }

    /**
     * Metodi päivittää piirroksen ajan tasalle
     */
    public void paivitaPiirto() {
        super.repaint();
    }

    private void piirraPeliLoppu(Graphics graphics) {

        graphics.setColor(Color.red);
        int paikkaX = (pelikentta.getPelikentanLeveys() + palojenKoko) / 2;
        int paikkaY = (pelikentta.getPelikentanKorkeus() + palojenKoko) / 2;
        graphics.drawString("Game Over", paikkaX, paikkaY);
        String pisteet = "Your score: " + peli.getPisteet();
        graphics.drawString(pisteet, paikkaX, paikkaY + palojenKoko + 3);
        graphics.drawString("Press enter to start again", paikkaX, paikkaY + (palojenKoko + 3) * 2);
    }

    private void piirraPause(Graphics graphics) {
        piirraPeli(graphics);

        graphics.setColor(Color.red);
        int paikkaX = (pelikentta.getPelikentanLeveys() + palojenKoko) / 2;
        int paikkaY = (pelikentta.getPelikentanKorkeus() + palojenKoko) / 2;
        graphics.drawString("Pause. Press enter to continue", paikkaX, paikkaY);
    }

    private void piirraTausta(Graphics graphics) {
        try {
            BufferedImage tausta = kuvat.get("tausta");
            if (peli.getTaustanLeikkauskohta() > tausta.getHeight()) {
                peli.setTaustanLeikkauskohta(0);
            }
            graphics.drawImage(tausta,
                    0, 0,
                    pelikentta.getPelikentanLeveys() * palojenKoko, peli.getTaustanLeikkauskohta(),
                    0, tausta.getHeight() - peli.getTaustanLeikkauskohta(),
                    tausta.getWidth(), tausta.getHeight(),
                    this);
            graphics.drawImage(tausta,
                    0, peli.getTaustanLeikkauskohta(),
                    pelikentta.getPelikentanLeveys() * palojenKoko, pelikentta.getPelikentanKorkeus() * palojenKoko,
                    0, 0,
                    tausta.getWidth(), tausta.getHeight() - peli.getTaustanLeikkauskohta(),
                    this);
        } catch (Exception e) {
            virhe();
        }
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

    private void piirraHuipputulokset(Graphics graphics) {
        graphics.setColor(Color.red);
        int paikkaX = (pelikentta.getPelikentanLeveys() + palojenKoko) / 2;
        int paikkaY = (pelikentta.getPelikentanKorkeus() + palojenKoko) / 2;
        int huipputuloksia = peli.getHuipputulokset().getTulokset().size();

        for (int i = 0; i < 10; i++) {
            Pelaaja pelaaja = null;
            if (i < huipputuloksia) {
                pelaaja = peli.getHuipputulokset().getTulokset().get(i);
            }
            graphics.drawString(luoRivi(i + 1, pelaaja), paikkaX, paikkaY + (i - 1) * 2 * palojenKoko);
        }
    }

    private String luoRivi(int i, Pelaaja pelaaja) {
        
        String pelaaTulos = "xxxx  0";
        if (pelaaja != null) {
            pelaaTulos = pelaaja.toString();
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append(". ");
        sb.append(pelaaTulos);
        return sb.toString();
    }

    private void virhe() {
        JOptionPane.showMessageDialog(ikkuna,
                "Some graphic components are missing.\n"
                + "Try again later.",
                "Fatal error",
                JOptionPane.ERROR_MESSAGE);
        peli.peliLoppuu();
    }

    private void piirraPelinAlkuruutu(Graphics graphics) {

        graphics.setColor(Color.red);
        int paikkaX = (pelikentta.getPelikentanLeveys() + palojenKoko) / 2;
        int paikkaY = (pelikentta.getPelikentanKorkeus() + palojenKoko) / 2;
        graphics.drawString("Press enter to start a game", paikkaX, paikkaY);

        piirraOmaAlus(graphics);

        piirraPistetilanne(graphics);
    }
}
