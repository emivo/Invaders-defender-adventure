package invadersdefender.sovelluslogiikka;

import invadersdefender.gui.PelinPiirtoalusta;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.Timer;

/**
 * @author emivo
 */
public class Peli extends Timer implements ActionListener {
    /* peli jatkuu kunnes oma alus tuhoutuu */

    private Alus omaAlus;
    private final List<Vihollisolio> viholliset;
    private final List<Ammus> ammukset;
    private final int pelikentanKoko;
    private PelinPiirtoalusta piirtoalusta;
    private int vihollisetLiikkuViive;
    private int vihollisetAmpuuViive;
    private static final int AMPUMISVIIVE = 15;
    private static final int LIIKKUMISVIIVE = 11;
    private static final int ALUKSIENKOKO = 3;
    private int pisteet;
    private boolean pause;

    public Peli(int pelikentanSivunpituus) {
        super(100, null);

        this.pelikentanKoko = pelikentanSivunpituus * ALUKSIENKOKO;
        this.omaAlus = luoOmaAlus();

        this.viholliset = new ArrayList<>();
        this.ammukset = new ArrayList<>();

        this.vihollisetAmpuuViive = AMPUMISVIIVE;
        this.vihollisetLiikkuViive = LIIKKUMISVIIVE;

        this.pisteet = 0;
        this.pause = false;

        addActionListener(this);
        setInitialDelay(500);
    }

    private Alus luoOmaAlus() {
        return new Alus(this.pelikentanKoko / 2, this.pelikentanKoko - (ALUKSIENKOKO + 1), ALUKSIENKOKO);
    }

    public List<Ammus> getAmmukset() {
        return ammukset;
    }

    public List<Vihollisolio> getViholliset() {
        return viholliset;
    }

    public int getPelikentanKoko() {
        return pelikentanKoko;
    }

    public Alus getOmaAlus() {
        return omaAlus;
    }

    public int getPisteet() {
        return pisteet;
    }

    /**
     * Palauttaa true, jos peli on vielä käynnissä
     *
     * @return palauttaa true, jos peli on vielä käynnissä
     */
    public boolean isPause() {
        return pause;
    }

    /**
     * Pysäyttää pelin, kun peli on käynnissä. Käynnistää pelin, kun peli on
     * pysäytetty
     */
    public void pause() {
        this.pause = !this.pause;
        if (this.pause) {
            stop();
        } else {
            start();
        }
    }

    public void setPiirtoalusta(PelinPiirtoalusta piirtoalusta) {
        this.piirtoalusta = piirtoalusta;
    }

    public void alusAmmu(Alus alus) {
        ammukset.add(alus.ammu());
    }

    public void kaynnistaPeliUuudelleen() {
        this.omaAlus = luoOmaAlus();

        this.viholliset.clear();
        this.ammukset.clear();

        this.pisteet = 0;
        this.pause = false;

        start();
    }

    /**
     * Metodi tarkastaa osuuko parametrinÃ¤ annettu ammus johonkin pelissÃ¤
     * olevaan alukseen
     *
     * @param ammus tarkasteltava ammus
     * @return totuusarvo osuuko ammmus johonkin alukseen
     */
    public boolean osuukoAmmus(Ammus ammus) {
        if (omaAlus.osuukoAlukseen(ammus)) {
            // lopeta peli
            stop();
            return true;
        }

        Iterator<Vihollisolio> iterator = viholliset.iterator();
        while (iterator.hasNext()) {
            Vihollisolio vihollinen = iterator.next();
            if (vihollinen.osuukoAlukseen(ammus)) {
                iterator.remove();
                pisteet += 10;
                return true;
            }
        }
        return false;
    }

    public void vihollisetTulevatEsille() {
        // kuinka monta vihollista tulee
        // KESKEN
        int esiinTulevienVihollisteMaara = 3;
        int alustenKoko = omaAlus.getKoko();
        for (int i = 0; i < esiinTulevienVihollisteMaara; i++) {

            viholliset.add(new Vihollisolio(1 + ((pelikentanKoko / esiinTulevienVihollisteMaara) * i), 1, omaAlus.getKoko()));
        }
    }

    public void ammuksetLiiku() {
        Iterator<Ammus> iterator = ammukset.iterator();
        while (iterator.hasNext()) {
            Ammus ammus = iterator.next();
            ammus.liiku();
            // leveyssuuntaa ei tarvitse tarkistaa sillÃ¤ ammuksien ei tulisi poistua kentÃ¤n reunalta
            if (ammus.getSijainti().getY() < 0 || ammus.getSijainti().getY() > pelikentanKoko) {
                iterator.remove();
            } else if (osuukoAmmus(ammus)) {
                iterator.remove();
            }
        }
    }

    private boolean osuukoAmmuksetOmaanAlukseen() {
        for (Ammus ammus : ammukset) {
            if (osuukoAmmus(ammus)) {
                return true;
            }
        }
        return false;
    }

    public void omaAlusLiiku(Suunta suunta) {
        omaAlus.liiku(suunta);
        if (osuukoVihollisetOmaanAlukseen() || osuukoAmmuksetOmaanAlukseen()) {
            stop();
        }
        if (!tarkistaVoikoLiikkua(omaAlus)) {
            peruLiikumminen(omaAlus, suunta);
        }
    }

    private void peruLiikumminen(Alus alus, Suunta suunta) {
        switch (suunta) {
            case ALAS:
                alus.liiku(Suunta.YLOS);
                break;
            case YLOS:
                alus.liiku(Suunta.ALAS);
                break;
            case OIKEA:
                alus.liiku(Suunta.VASEN);
                break;
            case VASEN:
                alus.liiku(Suunta.OIKEA);
                break;
        }
    }

    /**
     * Metodilla voidaan tarkistaa onko alus poistumassa pelikentalta
     *
     * @param alus tarkastelva alus
     * @return true - jos alus edelleen pelissä. false - jos poistunut
     * pelikentältä
     */
    private boolean tarkistaVoikoLiikkua(Alus alus) {
        return !(alus.getX() < 0 || alus.getX() + alus.getKoko() > pelikentanKoko
                || alus.getY() < 0 || alus.getY() + alus.getKoko() > pelikentanKoko);
    }

    public void vihollisetLiiku() {
        // kaikilla vihollisilla sama suunta
        if (!viholliset.isEmpty()) {
            Suunta suuntaJohonOliotAikovatLiikkua = viholliset.get(0).getSuunta();
            boolean voikoLiikkua = true;
            for (Vihollisolio olio : viholliset) {
                olio.liiku();
                voikoLiikkua = voikoLiikkua && tarkistaVoikoLiikkua(olio);
            }
            if (!voikoLiikkua) {
                for (Vihollisolio olio : viholliset) {
                    peruLiikumminen(olio, suuntaJohonOliotAikovatLiikkua);
                }
            }
        }
    }

    public boolean osuukoVihollisetOmaanAlukseen() {
        for (Vihollisolio olio : viholliset) {
            if (omaAlus.osuukoAlukseen(olio)) {
                return true;
            }
        }
        return false;
    }

    public void paivita() {
        piirtoalusta.paivitaPiirto();
    }

    public void jokuVihollinenAmpuu() {
        if (!viholliset.isEmpty()) {
            alusAmmu(viholliset.get(new Random().nextInt(viholliset.size())));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (viholliset.isEmpty()) {
            // Tähän tarvitaan delay
            vihollisetTulevatEsille();
        }
        ammuksetLiiku();
        vihollistenViiveAmmustenLiikkeeseen();
        if (osuukoVihollisetOmaanAlukseen()) {
            stop();
        }
        paivita();
    }

    private void vihollistenViiveAmmustenLiikkeeseen() {
        vihollisetLiikkuViive--;
        vihollisetAmpuuViive--;
        if (vihollisetLiikkuViive == 0) {
            vihollisetLiiku();
            vihollisetLiikkuViive = LIIKKUMISVIIVE;
        }

        if (vihollisetAmpuuViive == 0) {
            jokuVihollinenAmpuu();
            vihollisetAmpuuViive = AMPUMISVIIVE;
        }
    }
}
