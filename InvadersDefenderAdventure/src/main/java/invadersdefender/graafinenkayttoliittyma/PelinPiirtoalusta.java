package invadersdefender.graafinenkayttoliittyma;

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
 * Luokka piirtää kuvina konstruktiossa annetun pelin.
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

    /**
     * Luo PelinPiirtoalusta-luokan olion, jolle asetetaan peli ja palojen koko.
     * Samalla lataa peliin tarvittavat kuvat muistiin.
     *
     * @param peli peli, jota halutaan piirtää.
     * @param palojenKoko pikselileveys paloille, joita piirretään
     */
    public PelinPiirtoalusta(Peli peli, int palojenKoko) {
        this.peli = peli;
        this.pelikentta = peli.getPelikentta();
        this.palojenKoko = palojenKoko;
        this.kuvat = new HashMap<>();
        this.rajahdykset = new ArrayList<>();

        lataaKuvat();
    }

    /**
     * Metodi piirtää pelintilanteen mukaisen kuvan parametrinä annettuun
     * Graphics luokan olioon
     *
     * @param grafiikat alusta, johon peli piirretään
     */
    @Override
    public void paintComponent(Graphics grafiikat) {
        super.paintComponent(grafiikat);

        piirraTausta(grafiikat);

        if (peli.getTilanne() == Pelitilanne.KAYNNISSA) {
            piirraPeli(grafiikat);
        } else if (peli.getTilanne() == Pelitilanne.PYSAYTETTY) {
            piirraPysaytettyPeli(grafiikat);
        } else if (peli.getTilanne() == Pelitilanne.TULOKSET) {
            piirraHuipputulokset(grafiikat);
        } else if (peli.getTilanne() == Pelitilanne.ALKURUUTU) {
            piirraPelinAlkuruutu(grafiikat);
        } else {
            piirraPeliLoppu(grafiikat);
        }

    }

    public void setIkkuna(JFrame ikkuna) {
        this.ikkuna = ikkuna;
    }

    private void piirraPeli(Graphics grafiikat) {

        piirraOmaAlus(grafiikat);
        piirraViholliset(grafiikat);
        piirraPomo(grafiikat);
        piirraAmmukset(grafiikat);

        if (!rajahdykset.isEmpty()) {
            piirraRajahdykset(grafiikat);
        }

        piirraPistetilanne(grafiikat);
        piirraOmatElamapisteet(grafiikat);

    }

    private void piirraPistetilanne(Graphics grafiikat) {
        grafiikat.setColor(Color.red);
        String pisteet = "" + peli.getPisteet();
        grafiikat.drawString(pisteet, palojenKoko, (int) (palojenKoko * 1.5));
    }

    private void piirraAmmukset(Graphics grafiikat) {
        for (Ammus ammus : pelikentta.getAmmukset()) {
            Image kuva;
            if (ammus.getSuunta() == Suunta.YLOS) {
                kuva = kuvat.get("ammus");
            } else {
                kuva = kuvat.get("vihollisenammus");
            }
            piirraLiikkuva(grafiikat, ammus, kuva);
        }
    }

    private void piirraViholliset(Graphics grafiikat) {
        for (Vihollisolio vihu : pelikentta.getViholliset()) {
            piirraLiikkuva(grafiikat, vihu, kuvat.get("vihollisolio"));
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

    private void piirraKuva(Graphics grafiikat, int x, int y, int koko, Image kuva) {
        if (kuva != null) {
            koko *= palojenKoko;
            grafiikat.drawImage(kuva, x * palojenKoko, y * palojenKoko, koko, koko, this);
        } else {
            virhe();
        }
    }

    private void piirraOmaAlus(Graphics grafiikat) {
        piirraLiikkuva(grafiikat, pelikentta.getOmaAlus(), kuvat.get("omaalus"));
    }

    /**
     * Metodi päivittää piirroksen ajan tasalle
     */
    public void paivitaPiirto() {
        super.revalidate();
        super.repaint();
    }

    private void piirraPeliLoppu(Graphics grafiikat) {

        int paikkaX = (pelikentta.getPelikentanLeveys() + palojenKoko) / 2;
        int paikkaY = (pelikentta.getPelikentanKorkeus() + palojenKoko) / 2;
        piirraTeksti(grafiikat, "Game Over", paikkaX, paikkaY);
        String pisteet = "Your score: " + peli.getPisteet();
        piirraTeksti(grafiikat, pisteet, paikkaX, paikkaY + palojenKoko + 3);
        piirraTeksti(grafiikat, "Press enter to start again", paikkaX, paikkaY + (palojenKoko + 3) * 2);
    }

    private void piirraPysaytettyPeli(Graphics grafiikat) {
        piirraPeli(grafiikat);

        int paikkaX = (pelikentta.getPelikentanLeveys() + palojenKoko) / 2;
        int paikkaY = (pelikentta.getPelikentanKorkeus() + palojenKoko) / 2;
        piirraTeksti(grafiikat, "Pause. Press enter to continue", paikkaX, paikkaY);
    }

    private void piirraTausta(Graphics grafiikat) {

        BufferedImage tausta = kuvat.get("tausta");
        if (peli.getTaustanLeikkauskohta() >= ikkuna.getHeight() - palojenKoko / 2 - 1) {
            peli.setTaustanLeikkauskohta(0);
        }

        grafiikat.drawImage(tausta,
                0, 0,
                pelikentta.getPelikentanLeveys() * palojenKoko, peli.getTaustanLeikkauskohta(),
                0, tausta.getHeight() - peli.getTaustanLeikkauskohta(),
                tausta.getWidth(), tausta.getHeight(),
                this);

        grafiikat.drawImage(tausta,
                0, peli.getTaustanLeikkauskohta(),
                pelikentta.getPelikentanLeveys() * palojenKoko, pelikentta.getPelikentanKorkeus() * palojenKoko,
                0, 0,
                tausta.getWidth(), tausta.getHeight() - peli.getTaustanLeikkauskohta(),
                this);
    }

    private void piirraLiikkuva(Graphics grafiikat, Liikkuva liikkuva, Image kuva) {
        piirraKuva(grafiikat, liikkuva.getX(), liikkuva.getY(), liikkuva.getKoko(), kuva);
    }

    private void lataaKuvat() {
        kuvat.put("ammus", lueKuva(haeOsoite("/ammus.png")));
        kuvat.put("vihollisenammus", lueKuva(haeOsoite("/vihollisenammus.png")));
        kuvat.put("tausta", lueKuva(haeOsoite("/tausta.jpg")));
        kuvat.put("omaalus", lueKuva(haeOsoite("/omaalus.png")));
        kuvat.put("vihollisolio", lueKuva(haeOsoite("/vihollisolio.png")));
        kuvat.put("pomo", lueKuva(haeOsoite("/pomo.png")));
        kuvat.put("rajahdys", lueKuva(haeOsoite("/rajahdys.png")));
        kuvat.put("nappaimet", lueKuva(haeOsoite("/nappaimet.png")));
    }

    private void piirraHuipputulokset(Graphics grafiikat) {
        int paikkaX = (pelikentta.getPelikentanLeveys() + palojenKoko) / 2;
        int paikkaY = (pelikentta.getPelikentanKorkeus() + palojenKoko) / 2;
        int huipputuloksia = peli.getHuipputulokset().getTulokset().size();

        for (int i = 0; i < 10; i++) {
            Pelaaja pelaaja = null;
            if (i < huipputuloksia) {
                pelaaja = peli.getHuipputulokset().getTulokset().get(i);
            }
            piirraTeksti(grafiikat, luoRivi(i + 1, pelaaja), paikkaX, paikkaY + (i - 1) * 2 * palojenKoko);
        }

        piirraTeksti(grafiikat, "Clear highscores press X", (int) (paikkaX * palojenKoko), paikkaY);
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

    private void piirraPelinAlkuruutu(Graphics grafiikat) {

        int paikkaX = (pelikentta.getPelikentanLeveys() + palojenKoko) / 2;
        int paikkaY = (pelikentta.getPelikentanKorkeus() + palojenKoko) / 2;
        String alkuteksti = "Press enter to start a game";
        piirraTeksti(grafiikat, alkuteksti, paikkaX, paikkaY);

        piirraKontrolliohjeet(grafiikat, paikkaX, paikkaY, palojenKoko * 14);

        piirraOmaAlus(grafiikat);

        piirraPistetilanne(grafiikat);
    }

    private void piirraKontrolliohjeet(Graphics grafiikat, int paikkaX, int paikkaY, int korkeus) {
        grafiikat.drawImage(kuvat.get("nappaimet"), paikkaX + palojenKoko * 2, paikkaY + palojenKoko * 3, (int) (2.44 * korkeus), korkeus, this);
    }

    private void piirraTeksti(Graphics grafiikat, String merkkijono, int x, int y) {
        grafiikat.setColor(Color.red);
        grafiikat.drawString(merkkijono, x, y);
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

    private void piirraPomo(Graphics grafiikat) {
        if (pelikentta.getPomo() != null) {
            piirraLiikkuva(grafiikat, pelikentta.getPomo(), kuvat.get("pomo"));
        }
    }

    /**
     * Metodi asettaa uuden räjähdyksen piirrettäväksi seuraavalla
     * piirtokerralla
     *
     * @param sijainti Paikka, johon räjähdys halutaan piirrettävän
     */
    public void piirraRajahdys(Pala sijainti) {
        rajahdykset.add(sijainti);
    }

    private void piirraOmatElamapisteet(Graphics grafiikat) {
        int x = pelikentta.getPelikentanLeveys() * palojenKoko * 3 / 4;
        int y = (int) (palojenKoko * 1.5);
        piirraTeksti(grafiikat, "HP: " + pelikentta.getOmaAlus().getElamapisteet(), x, y);
    }

    private void piirraRajahdykset(Graphics grafiikat) {
        Iterator<Pala> iteraattori = rajahdykset.iterator();
        while (iteraattori.hasNext()) {
            Pala sijainti = iteraattori.next();
            if (sijainti.getX() >= 0 && sijainti.getY() >= 0) {
                piirraKuva(grafiikat, sijainti.getX(), sijainti.getY(), sijainti.getKoko(), kuvat.get("rajahdys"));
            } else {
                piirraRajahdysJonkaYTaiXOnKentanUlkopuolella(sijainti, grafiikat);
            }
            iteraattori.remove();
        }
    }

    private void piirraRajahdysJonkaYTaiXOnKentanUlkopuolella(Pala sijainti, Graphics grafiikat) {
        int x = sijainti.getX();
        int y = sijainti.getY();
        if (x < 0) {
            grafiikat.drawImage(kuvat.get("rajahdys"), 0, y * palojenKoko, sijainti.getKoko() * palojenKoko + x * palojenKoko, sijainti.getKoko() * palojenKoko, this);
        } else if (y < 0) {
            grafiikat.drawImage(kuvat.get("rajahdys"), x * palojenKoko, 0, sijainti.getKoko() * palojenKoko, sijainti.getKoko() * palojenKoko + y * palojenKoko, this);
        }
    }

}
