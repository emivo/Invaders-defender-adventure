package invadersdefender.gui;

import invadersdefender.sovelluslogiikka.Ammus;
import invadersdefender.sovelluslogiikka.Liikkuva;
import invadersdefender.sovelluslogiikka.Pala;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

    private final Peli peli;
    private final Pelikentta pelikentta;
    private final int palojenKoko;
    private final Map<String, BufferedImage> kuvat;
    private JFrame ikkuna;
    private final List<Pala> rajahdykset;

    public PelinPiirtoalusta(Peli peli, int palojenKoko) {
        this.peli = peli;
        this.pelikentta = peli.getPelikentta();
        this.palojenKoko = palojenKoko;
        this.kuvat = new HashMap<>();
        this.rajahdykset = new ArrayList<>();

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
        piirraPomo(graphics);
        piirraAmmukset(graphics);

        if (!rajahdykset.isEmpty()) {
            piirraRajahdykset(graphics);
        }

        piirraPistetilanne(graphics);
        piirraOmatElamapisteet(graphics);

    }

    private void piirraPistetilanne(Graphics graphics) {
        // pisteet oikeaan ylänurkkaan
        graphics.setColor(Color.red);
        String pisteet = "" + peli.getPisteet();
        graphics.drawString(pisteet, palojenKoko, (int) (palojenKoko * 1.5));
    }

    private void piirraAmmukset(Graphics graphics) {
        // piirrä ammukset
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
            koko *= palojenKoko;
            graphics.drawImage(kuva, x * palojenKoko, y * palojenKoko, koko, koko, this);
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

        int paikkaX = (pelikentta.getPelikentanLeveys() + palojenKoko) / 2;
        int paikkaY = (pelikentta.getPelikentanKorkeus() + palojenKoko) / 2;
        piirraTeksti(graphics, "Game Over", paikkaX, paikkaY);
        String pisteet = "Your score: " + peli.getPisteet();
        piirraTeksti(graphics, pisteet, paikkaX, paikkaY + palojenKoko + 3);
        piirraTeksti(graphics, "Press enter to start again", paikkaX, paikkaY + (palojenKoko + 3) * 2);
    }

    private void piirraPause(Graphics graphics) {
        piirraPeli(graphics);

        int paikkaX = (pelikentta.getPelikentanLeveys() + palojenKoko) / 2;
        int paikkaY = (pelikentta.getPelikentanKorkeus() + palojenKoko) / 2;
        piirraTeksti(graphics, "Pause. Press enter to continue", paikkaX, paikkaY);
    }

    private void piirraTausta(Graphics graphics) {
        try {
            BufferedImage tausta = kuvat.get("tausta");
            if (peli.getTaustanLeikkauskohta() >= tausta.getHeight()) {
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
        piirraKuva(graphics, liikkuva.getX(), liikkuva.getY(), liikkuva.getKoko(), kuva);
    }

    private void lataaKuvat() {
        kuvat.put("ammus", lueKuva(haeOsoite("/ammus.png")));
        kuvat.put("vihollisenammus", lueKuva(haeOsoite("/vihollisenammus.png")));
        kuvat.put("tausta", lueKuva(haeOsoite("/tausta.jpg")));
        kuvat.put("omaalus", lueKuva(haeOsoite("/omaalus.png")));
        kuvat.put("vihollisolio", lueKuva(haeOsoite("/vihollisolio.png")));
        kuvat.put("pomo", lueKuva(haeOsoite("/pomo.png")));
        kuvat.put("rajahdys", lueKuva(haeOsoite("/rajahdys.png")));
    }

    private void piirraHuipputulokset(Graphics graphics) {
        int paikkaX = (pelikentta.getPelikentanLeveys() + palojenKoko) / 2;
        int paikkaY = (pelikentta.getPelikentanKorkeus() + palojenKoko) / 2;
        int huipputuloksia = peli.getHuipputulokset().getTulokset().size();

        for (int i = 0; i < 10; i++) {
            Pelaaja pelaaja = null;
            if (i < huipputuloksia) {
                pelaaja = peli.getHuipputulokset().getTulokset().get(i);
            }
            piirraTeksti(graphics, luoRivi(i + 1, pelaaja), paikkaX, paikkaY + (i - 1) * 2 * palojenKoko);
        }

        piirraTeksti(graphics, "Clear highscores press X", (int) (paikkaX * palojenKoko), paikkaY);
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
        // TÄMÄ EI OLE HYVÄ 
        JOptionPane.showMessageDialog(ikkuna,
                "Some graphic components are missing.\n"
                + "Try again later.",
                "Fatal error",
                JOptionPane.ERROR_MESSAGE);
        peli.peliLoppuu();
    }

    private void piirraPelinAlkuruutu(Graphics graphics) {

        int paikkaX = (pelikentta.getPelikentanLeveys() + palojenKoko) / 2;
        int paikkaY = (pelikentta.getPelikentanKorkeus() + palojenKoko) / 2;
        String s = "Press enter to start a game";
        piirraTeksti(graphics, s, paikkaX, paikkaY);

        piirraOmaAlus(graphics);

        piirraPistetilanne(graphics);
    }

    private void piirraTeksti(Graphics graphics, String merkkijono, int x, int y) {
        graphics.setColor(Color.red);
        graphics.drawString(merkkijono, x, y);
    }

    /**
     * Metodi luo ikkunan pelaajan nimen kysymistä varten
     *
     * @return palauttaa nimen, jonka käyttäjä syöttää ikkunaan. Jos mitään ei
     * syötetä palautetaan {@code XXXXX}
     */
    public String uusiHuipputulos() {
        String nimi = (String) JOptionPane.showInputDialog(
                ikkuna,
                "Enter name:",
                "New Highscore",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "Galatic Warrior");
        if (nimi == null) {
            nimi = "XXXXX";
        }
        return nimi;
    }

    private void piirraPomo(Graphics graphics) {
        if (pelikentta.getPomo() != null) {
            piirraLiikkuva(graphics, pelikentta.getPomo(), kuvat.get("pomo"));
        }
    }

    public void piirraRajahdys(Pala sijainti) {
        rajahdykset.add(sijainti);
    }

    private void piirraOmatElamapisteet(Graphics graphics) {
        int x = pelikentta.getPelikentanLeveys() * palojenKoko * 3 / 4;
        int y = (int) (palojenKoko * 1.5);
        piirraTeksti(graphics, "HP: " + pelikentta.getOmaAlus().getElamapisteet(), x, y);
    }

    private void piirraRajahdykset(Graphics graphics) {
        Iterator<Pala> iteraattori = rajahdykset.iterator();
        while (iteraattori.hasNext()) {
            Pala sijainti = iteraattori.next();
            if (sijainti.getX() >= 0 && sijainti.getY() >= 0) {
                piirraKuva(graphics, sijainti.getX(), sijainti.getY(),sijainti.getKoko(), kuvat.get("rajahdys"));
            } else {
                piirraRajahdysJonkaYTaiXOnKentanUlkopuolella(sijainti, graphics);
            }
            iteraattori.remove();
        }
    }

    private void piirraRajahdysJonkaYTaiXOnKentanUlkopuolella(Pala sijainti, Graphics graphics) {
        if (kuvat.get("rajahdys") != null) {
            int x = sijainti.getX();
            int y = sijainti.getY();
            if (x < 0) {
                graphics.drawImage(kuvat.get("rajahdys"), 0, y * palojenKoko, sijainti.getKoko() * palojenKoko + x * palojenKoko, sijainti.getKoko() * palojenKoko, this);
            } else if (y < 0) {
                graphics.drawImage(kuvat.get("rajahdys"), x * palojenKoko, 0, sijainti.getKoko() * palojenKoko, sijainti.getKoko() * palojenKoko + y * palojenKoko, this);
            }
        } else {
            virhe();
        }
    }

}
