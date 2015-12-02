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
        this.vihollistenMaara = 2;

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
        setDelay(100);

        this.pisteet = 0;
        this.vihollistenMaara = 2;

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

    /**
     * Metodi antaa piirtoalustalle piirretäväksi räjähdyksen sijainnin Pala
     * oliona
     *
     * @param sijainti Paikka pelikentällä, johon räjähdys halutaan piirtää
     */
    public void lisaaRajahdysPiirrettavaksiKohtaan(Pala sijainti) {
        if (piirtoalusta != null) {
            piirtoalusta.piirraRajahdys(sijainti);
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

        if (pelikentta.getPomo() == null && pisteet != 0 && pisteet % 500 == 0) {
            pelikentta.pomoVihollinenTuleeEsille();
        }
        pelikentta.ammuksetLiiku();
        vihollistenViiveAmmustenLiikkeeseen();

        paivitaPelinpiirto();
        taustanLeikkauskohta += 2;
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
     * metodi kasvattaa käynnissä olevan pelin pistetilannetta sekä lisää
     * tarvittaessa esiin tulevien vihollisten määrää ja niiden kestävyyttyä
     */
    void lisaaPisteita() {
        if (tilanne == Pelitilanne.KAYNNISSA) {
            pisteet += 10;
            // pelinopeus
            if (pisteet != 0 && pisteet % 50 == 0 && getDelay() - 1 >= 33) {
                setDelay(getDelay() - 1);
                if (getDelay() % 5 == 0 && vihollistenMaara < 7) {
                    vihollistenMaara++;
                }
            }
            // vihollisten kestävyys
            if (pisteet % 500 == 0) {
                pelikentta.parannaVihollistenKestavyytta();
            }
        }
    }

    /**
     * Pelin huipputuloslista tallennetaan
     */
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
        stop();
        setTilanne(Pelitilanne.TULOKSET);
        paivitaPelinpiirto();
    }

    /**
     * Pelissä oleva huipputuloslista tyhjennetään
     */
    public void tyhjennaHuipputulokset() {
        huipputulokset.tyhjennaHuipputulokset();
    }

    /**
     * Parantaa pelissä olevan aluksen aseistus
     *
     * @param aseistus uusi aseistus
     */
    public void parannaAluksenAseistusta(Aseistus aseistus) {
        int kuinkaPaljonPisteitaMaksaa = 0;
        if (aseistus == Aseistus.TUPLA) {
            kuinkaPaljonPisteitaMaksaa = 1000;
        } else if (aseistus == Aseistus.TRIPLA) {
            kuinkaPaljonPisteitaMaksaa = 1500;
        }

        if (pisteet - kuinkaPaljonPisteitaMaksaa >= 0 && pelikentta.getOmaAlus().getAseistus().compareTo(aseistus) < 0) {
            pelikentta.getOmaAlus().parannaAseistusta(aseistus);
            pisteet -= kuinkaPaljonPisteitaMaksaa;
        }
    }

    /**
     * Korjaa pelissä olevan omaan alukseen syntyneitä vahinkoja
     *
     */
    public void korjaaOmaaAlusta() {
        if (pisteet - 10 > 0 && pelikentta.getOmaAlus().getElamapisteet() < 10) {
            pelikentta.getOmaAlus().korjaaSuojausta(1);
            pisteet -= 10;
        }
    }

}
