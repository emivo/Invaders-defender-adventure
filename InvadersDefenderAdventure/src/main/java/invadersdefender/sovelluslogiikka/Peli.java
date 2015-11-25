package invadersdefender.sovelluslogiikka;

import invadersdefender.sovelluslogiikka.huipputulokset.Huipputulokset;
import invadersdefender.gui.PelinPiirtoalusta;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * Luokka on pelin ydin, joka luo pelikentän ja kertoo mitä pelissä tapahtuu.
 * Luokka pitää huolen pelissä liikkuvien liikkeestä, pistetilanteesta ja
 * taustakuvan vierityksestä
 *
 * @author emivo
 */
public class Peli extends Timer implements ActionListener {
    /* peli jatkuu kunnes oma alus tuhoutuu */

    private final Pelikentta pelikentta;
    private JFrame ikkuna;
    private Huipputulokset huipputulokset;
    private PelinPiirtoalusta piirtoalusta;
    private int vihollisetLiikkuViiveLaskuri;
    private int vihollisetAmpuuViiveLaskuri;
    private static final int AMPUMISVIIVE = 15;
    private static final int LIIKKUMISVIIVE = 5;
    private Pelitilanne tilanne;
    private int pisteet;
    private int taustanLeikkauskohta;
    private int vihollistenMaara;

    public Peli(int pelikentanSivunpituus) {
        super(100, null);

        this.pelikentta = new Pelikentta(pelikentanSivunpituus, this);

        this.vihollisetAmpuuViiveLaskuri = AMPUMISVIIVE;
        this.vihollisetLiikkuViiveLaskuri = LIIKKUMISVIIVE;
        this.huipputulokset = new Huipputulokset();
        huipputulokset.lataaTulokset();
        this.taustanLeikkauskohta = 0;
        this.piirtoalusta = null;

        this.pisteet = 0;
        this.tilanne = Pelitilanne.ALKURUUTU;
        this.vihollistenMaara = 1;

        setInitialDelay(200);
        lisaaKuuntelija();
    }

    /**
     * metodi asettaa olion testaus käyttöön, jolloin huipputuloksia ei lueta
     * eikä tallenneta
     */
    public void setTEST() {
        huipputulokset = new Huipputulokset();
    }

    private void lisaaKuuntelija() {
        addActionListener(this);
    }

    public Pelikentta getPelikentta() {
        return pelikentta;
    }

    public int getPisteet() {
        return pisteet;
    }

    public int getTaustanLeikkauskohta() {
        return taustanLeikkauskohta;
    }

    public void setTaustanLeikkauskohta(int taustanLeikkauskohta) {
        this.taustanLeikkauskohta = taustanLeikkauskohta;
    }

    public Huipputulokset getHuipputulokset() {
        return huipputulokset;
    }

    public void setIkkuna(JFrame ikkuna) {
        this.ikkuna = ikkuna;
    }

    public Pelitilanne getTilanne() {
        return tilanne;
    }

    public void setTilanne(Pelitilanne tilanne) {
        this.tilanne = tilanne;
    }

    /**
     * Pysäyttää pelin, kun peli on käynnissä. Käynnistää pelin, kun peli on
     * pysäytetty
     */
    public void pause() {
        if (tilanne == Pelitilanne.KAYNNISSA) {
            stop();
            setTilanne(Pelitilanne.PAUSE);
        } else if (tilanne != Pelitilanne.LOPPU) {
            start();
        }
    }

    /**
     * Käynnistää Timerin sekä asettaa pelitilanteen käynnissä tilaan
     */
    @Override
    public void start() {
        setTilanne(Pelitilanne.KAYNNISSA);
        super.start();
    }

    public void setPiirtoalusta(PelinPiirtoalusta piirtoalusta) {
        this.piirtoalusta = piirtoalusta;
    }

    /**
     * Nollaa pelikentän ja pisteet sekä kännistä pelin
     */
    public void kaynnistaPeliUuudelleen() {
        pelikentta.kaynnistaUudelleen();

        this.pisteet = 0;
        this.vihollistenMaara = 1;

        start();
    }

    /**
     * Metodi päivittää pelin piirtoalustan, jos sellainen on sille asetettu
     */
    public void paivitaPelinpiirto() {
        if (piirtoalusta != null) {
            piirtoalusta.paivitaPiirto();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (pelikentta.getViholliset().isEmpty()) {
            pelikentta.vihollisetTulevatEsille(vihollistenMaara);
        }
        // kun saavutetaan tuhat pistettä laitetaan vihollisia tulemaan tolkuton määrä siis jatkuvalla syötöllä
        if (pisteet > 600 && pelikentta.getViholliset().get(pelikentta.getViholliset().size() - 1).getY() > 1) {
            pelikentta.vihollisetTulevatEsille(vihollistenMaara);
        }
        pelikentta.ammuksetLiiku();
        vihollistenViiveAmmustenLiikkeeseen();

        paivitaPelinpiirto();
        taustanLeikkauskohta++;
    }

    /**
     * metodi asettaa pelin loppu tilaan ja tarkistaa onko syntynyt tallennetava
     * huipputulos
     */
    public void peliLoppuu() {
        stop();
        setTilanne(Pelitilanne.LOPPU);
        if (piirtoalusta != null) {
            if (huipputulokset.getTulokset().size() < 10 || huipputulokset.getViimeinen().getTulos() < pisteet) {
                huipputulokset.lisaaTulos(piirtoalusta.uusiHuipputulos(), pisteet);
                tallennaTulokset();
            }
        }
    }

    private void vihollistenViiveAmmustenLiikkeeseen() {
        vihollisetLiikkuViiveLaskuri--;
        vihollisetAmpuuViiveLaskuri--;
        if (vihollisetLiikkuViiveLaskuri == 0) {
            pelikentta.vihollisetLiiku();
            vihollisetLiikkuViiveLaskuri = LIIKKUMISVIIVE;
        }

        if (vihollisetAmpuuViiveLaskuri == 0) {
            pelikentta.jokuVihollinenAmpuu();
            vihollisetAmpuuViiveLaskuri = AMPUMISVIIVE;
        }
    }

    /**
     * metodi kasvattaa käynnissä olevan pelin pistetilannetta
     */
    void lisaaPisteita() {
        if (tilanne == Pelitilanne.KAYNNISSA) {
            pisteet += 10;
            if (pisteet != 0 && pisteet % 50 == 0 && getDelay() - 1 >= 40) {
                setDelay(getDelay() - 1);
                if (getDelay() % 10 == 0) {
                    vihollistenMaara++;
                }
            }
        }
    }

    public void tallennaTulokset() {
        huipputulokset.tallennaTulokset();
    }

    /**
     * metodi asettaa pelin huipputulosten katselutilaan
     */
    public void asetaHuipputuloistenKatselutilaan() {
        if (tilanne == Pelitilanne.LOPPU) {
            kaynnistaPeliUuudelleen();
        }
        pause();
        stop();
        setTilanne(Pelitilanne.TULOKSET);
        paivitaPelinpiirto();
    }

    public void tyhjennaHuipputulokset() {
        huipputulokset.tyhjennaHuipputulokset();
    }

}
