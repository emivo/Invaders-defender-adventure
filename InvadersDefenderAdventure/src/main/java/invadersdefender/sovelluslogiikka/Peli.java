package invadersdefender.sovelluslogiikka;

import invadersdefender.sovelluslogiikka.huipputulokset.Huipputulokset;
import invadersdefender.graafinenkayttoliittyma.PelinPiirtoalusta;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.Timer;

/**
 * Luokka on pelin ydin, joka luo pelikentän ja kertoo mitä pelissä tapahtuu.
 * Tapahtuvat muutokset luokka ilmoittaa PelinPiirtoalustalle, joka voi pelin
 * piirtää JFrame ikkunaan. Luokka antaa käskyjä {@link Pelikentta}-luokalla
 * tarvittaessa liikuttaa liikkuvia ja tuoda pelikentälle lisää vihollisia.
 * Luokka pitää huolen pelintilanteesta, pistetilanteesta ja taustakuvan
 * vierityksestä. Pistetilanteen ollessa riittävän suuri voi luokka päivittää
 * pelikentällä olevan oman aluksen aseistusta tai korjata siihen syntyneitä
 * vahinkoja
 *
 * Pelissä syntyvät huipputulokset luokka käskee {@link Huipputulokset}-luokan
 * tallentaa tai poistaa.
 *
 * @see Pelitilanne
 * @see Pelikentta
 * @see Huipputulokset
 *
 * @author emivo
 */
public class Peli extends Timer implements ActionListener {

    private final Pelikentta pelikentta;
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

    /**
     * Luo {@code Peli}-luokan olion ja alustaa sen alkutilaan.
     *
     * @param pelikentanSivunpituus kuinka montaa palaa pitkä pelikentän korkeus
     * on
     */
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

        setInitialDelay(100);
        lisaaKuuntelija();

    }

    /**
     * Metodi asettaa olion testaus käyttöön, jolloin huipputuloksia ei lueta
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

    public Pelitilanne getTilanne() {
        return tilanne;
    }

    public void setTilanne(Pelitilanne tilanne) {
        this.tilanne = tilanne;
    }

    /**
     * Pysäyttää pelin, kun peli on käynnissä ja käynnistää pelin, kun peli on
     * pysäytetty
     */
    public void pysaytaTaiJatkaPysahtynytta() {
        if (tilanne == Pelitilanne.KAYNNISSA) {
            stop();
            setTilanne(Pelitilanne.PYSAYTETTY);
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
     * oliona, jolloin piirtoalusta saa helposti koordinaatit sekä räjähdyksen
     * koon
     *
     * @param sijainti Paikka pelikentällä, johon räjähdys halutaan piirtää
     */
    public void lisaaRajahdysPiirrettavaksiKohtaan(Pala sijainti) {
        if (piirtoalusta != null) {
            piirtoalusta.piirraRajahdys(sijainti);
        }
    }

    /**
     * Metodi kutsuu vihollisia kentälle, liikuttaa niitä tai käskee niiden
     * ampua tarvittaessa. Lisäksi metodi liikuttaa ammuksia, vierittää
     * taustakuvaa sekä ilmoittaa piirtoalustalle päivittämään pelin piirron.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        lisataankoVihollisiaKentalle();

        lisataankoPomoKentalle();

        pelikentta.ammuksetLiiku();
        vihollistenViiveAmmustenLiikkeeseen();

        taustanLeikkauskohta += 2;
        paivitaPelinpiirto();

    }

    private void lisataankoPomoKentalle() {
        if (pelikentta.getPomo() == null && pisteet != 0 && pisteet % 500 == 0) {
            pelikentta.pomoVihollinenTuleeEsille();
        }
    }

    private void lisataankoVihollisiaKentalle() {
        List<Vihollisolio> viholliset = pelikentta.getViholliset();
        // viimeisen vihollisen indeksi, nimetty lyhyesti
        int i = viholliset.size() - 1;
        
        if (viholliset.isEmpty()) {
            pelikentta.vihollisetTulevatEsille(vihollistenMaara);
        } else if (getDelay() < 80
                && viholliset.get(i).getY() > viholliset.get(i).getKoko()) {
            pelikentta.vihollisetTulevatEsille(vihollistenMaara / 2);
        }
    }

    /**
     * Metodi asettaa pelin lopputilaan ja tarkistaa onko syntynyt tallennetava
     * huipputulos.
     */
    public void peliLoppuu() {
        paivitaPelinpiirto();
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
     * Metodi kasvattaa käynnissä olevan pelin pistetilannetta sekä lisää
     * tarvittaessa esiin tulevien vihollisten määrää ja niiden kestävyyttyä.
     */
    public void lisaaPisteita() {
        if (tilanne == Pelitilanne.KAYNNISSA) {
            pisteet += 10;
            // pelinopeus
            if (pisteet != 0 && pisteet % 50 == 0 && getDelay() - 1 >= 33) {
                setDelay(getDelay() - 2);
                if (getDelay() % vihollistenMaara == 0 && vihollistenMaara < 7) {
                    vihollistenMaara++;
                }
            }
            // vihollisten kestävyys
            if (pisteet != 0 && pisteet % 100 == 0) {
                pelikentta.parannaVihollistenKestavyytta();
            }
        }
    }

    /**
     * Pelin huipputuloslista tallennetaan.
     */
    public void tallennaTulokset() {
        huipputulokset.tallennaTulokset();
    }

    /**
     * Metodi asettaa pelin huipputulosten katselutilaan.
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
     * Pelissä oleva huipputuloslista tyhjennetään.
     */
    public void tyhjennaHuipputulokset() {
        huipputulokset.tyhjennaHuipputulokset();
    }

    /**
     * Parantaa pelissä olevan aluksen aseistus.
     *
     * @param aseistus uusi aseistus, joka omalle alukselle asetetaan
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
     * Korjaa pelissä olevan omaan alukseen syntyneitä vahinkoja.
     *
     */
    public void korjaaOmaaAlusta() {
        if (pisteet - 10 > 0 && pelikentta.getOmaAlus().getElamapisteet() < 100) {
            pelikentta.getOmaAlus().korjaaSuojausta(10);
            pisteet -= 10;
        }
    }

}
