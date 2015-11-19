package invadersdefender.sovelluslogiikka;

import invadersdefender.sovelluslogiikka.huipputulokset.Huipputulokset;
import invadersdefender.gui.PelinPiirtoalusta;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 * @author emivo
 */
public class Peli extends Timer implements ActionListener {
    /* peli jatkuu kunnes oma alus tuhoutuu */

    private final Pelikentta pelikentta;
    private JFrame ikkuna;
    private final Huipputulokset huipputulokset;
    private PelinPiirtoalusta piirtoalusta;
    private int vihollisetLiikkuViiveLaskuri;
    private int vihollisetAmpuuViiveLaskuri;
    private static final int AMPUMISVIIVE = 15;
    private static final int LIIKKUMISVIIVE = 5;
    private Pelitilanne tilanne;
    private int pisteet;
    private int taustanLeikkauskohta;

    public Peli(int pelikentanSivunpituus) {
        super(100, null);

        this.pelikentta = new Pelikentta(pelikentanSivunpituus, this);

        this.vihollisetAmpuuViiveLaskuri = AMPUMISVIIVE;
        this.vihollisetLiikkuViiveLaskuri = LIIKKUMISVIIVE;
        this.huipputulokset = new Huipputulokset();
        this.taustanLeikkauskohta = 0;

        this.pisteet = 0;
        this.tilanne = Pelitilanne.KAYNNISSA;

        setInitialDelay(500);
        lisaaKuuntelija();
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
            setTilanne(Pelitilanne.KAYNNISSA);
        }
    }

    public void setPiirtoalusta(PelinPiirtoalusta piirtoalusta) {
        this.piirtoalusta = piirtoalusta;
    }

    public void kaynnistaPeliUuudelleen() {
        pelikentta.kaynnistaUudelleen();

        this.pisteet = 0;
        this.tilanne = Pelitilanne.KAYNNISSA;

        start();
    }

    public void paivita() {
        piirtoalusta.paivitaPiirto();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (pelikentta.getViholliset().isEmpty()) {
            pelikentta.vihollisetTulevatEsille();
        }
        pelikentta.ammuksetLiiku();
        vihollistenViiveAmmustenLiikkeeseen();

        if (getDelay() - pisteet / 10 > 30) {
            setInitialDelay(getDelay() - pisteet / 10);
        }
        paivita();
        taustanLeikkauskohta++;
    }

    public void peliLoppuu() {
        stop();
        setTilanne(Pelitilanne.LOPPU);
        if (huipputulokset.getTulokset().size() < 10 || huipputulokset.getViimeinen().getTulos() < pisteet) {
            String input = (String) JOptionPane.showInputDialog(
                    ikkuna,
                    "Enter name:",
                    "New Highscore",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "Galatic Warrior");
            if (input != null) {
                huipputulokset.lisaaTulos(input, pisteet);
            } else {
                huipputulokset.lisaaTulos("xxxxx", pisteet);
            }
        }
        tallennaTulokset();
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

    void lisaaPisteita() {
        pisteet += 10;
    }

    public void tallennaTulokset() {
        huipputulokset.tallennaTulokset();
    }

    public void huipputulokset() {
        if (tilanne == Pelitilanne.LOPPU) {
            kaynnistaPeliUuudelleen();
        }
        pause();
        setTilanne(Pelitilanne.TULOKSET);
        paivita();

    }

}
