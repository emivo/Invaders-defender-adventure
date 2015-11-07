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
    private final List<Vihollisolio> viholliset; // voi olla vÃ¤lillÃ¤ tyhjÃ¤
    private final List<Ammus> ammukset; // tyhjÃ¤ aina vÃ¤lillÃ¤
    private final int pelikentanKoko; // Sivunpituus
    private PelinPiirtoalusta piirtoalusta;
    private int vihollisetLiikkuViive;
    private int vihollisetAmpuuViive;
    private static final int ampumisViive = 15;
    private static final int liikkumisViive = 11;
    private int pisteet;
    private boolean peliEiJatku;

    public Peli(int pelikentanSivunpituus) {
        super(100, null);
        int aluksienKoko = 3;
        
        this.pelikentanKoko = pelikentanSivunpituus * aluksienKoko;
        this.omaAlus = new Alus(this.pelikentanKoko / 2, this.pelikentanKoko - (aluksienKoko + 1), aluksienKoko);
        
        
        this.viholliset = new ArrayList<>();
        this.ammukset = new ArrayList<>();
        
        this.vihollisetAmpuuViive = ampumisViive;
        this.vihollisetLiikkuViive = liikkumisViive;
        
        this.pisteet = 0;
        this.peliEiJatku = false;
        
        addActionListener(this);
        setInitialDelay(500);
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

    public void setPiirtoalusta(PelinPiirtoalusta piirtoalusta) {
        this.piirtoalusta = piirtoalusta;
    }

    public void alusAmmu(Alus alus) {
        ammukset.add(alus.ammu());
    }

    /**
     * Metodi tarkastaa osuuko parametrinÃ¤ annettu ammus johonkin pelissÃ¤
     * olevaan alukseen
     *
     * @param ammus tarkasteltava ammus
     * @return totuusarvo osuuko ammmus johonkin alukseen
     */
    public boolean osuukoAmmus(Ammus ammus) {
        if (osuukoAlukseen(omaAlus, ammus)) {
            // lopeta peli
            peliEiJatku = true;
            return true;
        }

        Iterator<Vihollisolio> it = viholliset.iterator();
        while (it.hasNext()) {
            Vihollisolio vihu = it.next();
            if (osuukoAlukseen(vihu, ammus)) {
                it.remove();
                pisteet += 10;
                return true;
            }
        }
        return false;
    }

    private boolean osuukoAlukseen(Alus alus, Ammus ammus) {
        return (alus.getX() <= ammus.getX()
                && alus.getX() + alus.getKoko() > ammus.getX())
                && (alus.getY() <= ammus.getY()
                && alus.getY() + alus.getKoko() > ammus.getY());
    }

    public void vihollisetTulevatEsille() {
        // kuinka monta vihollista tulee
        // KESKEN
        int maara = 3;
        int alustenKoko = omaAlus.getKoko();
        for (int i = 0; i < maara; i++) {

            viholliset.add(new Vihollisolio(1 + ((pelikentanKoko / maara) * i), 1, omaAlus.getKoko()));
        }
    }

    public void ammuksetLiiku() {
        Iterator<Ammus> it = ammukset.iterator();
        while (it.hasNext()) {
            Ammus a = it.next();
            a.liiku();
            // leveyssuuntaa ei tarvitse tarkistaa sillÃ¤ ammuksien ei tulisi poistua kentÃ¤n reunalta
            if (a.getSijainti().getY() < 0 || a.getSijainti().getY() > pelikentanKoko) {
                it.remove();
            } else if (osuukoAmmus(a)) {
                it.remove();
            }
        }
    }

    public void omaAlusLiiku() {
        Suunta suuntaJohonAlusOnLiikkumassa = omaAlus.getSuunta();
        omaAlus.liiku();
        if (!tarkistaVoikoLiikkua(omaAlus)) {
            peruLiikumminen(omaAlus, suuntaJohonAlusOnLiikkumassa);
        }
    }

    private void peruLiikumminen(Alus alus, Suunta suunta) {
        switch (suunta) {
            case ALAS:
                alus.setSuunta(Suunta.YLOS);
                break;
            case YLOS:
                alus.setSuunta(Suunta.ALAS);
                break;
            case OIKEA:
                alus.setSuunta(Suunta.VASEN);
                break;
            case VASEN:
                alus.setSuunta(Suunta.OIKEA);
                break;
        }
        alus.liiku();
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
            for (Vihollisolio o : viholliset) {
                o.liiku();
                voikoLiikkua = voikoLiikkua && tarkistaVoikoLiikkua(o);
            }
            if (!voikoLiikkua) {
                for (Vihollisolio o : viholliset) {
                    peruLiikumminen(o, suuntaJohonOliotAikovatLiikkua);
                }
            }
        }
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
        if (peliEiJatku) {
            // paluu valikkoon jota ei vielä ole
            stop();
            return;
        }
        if (viholliset.isEmpty()) {
            // Tähän tarvitaan delay
            vihollisetTulevatEsille();
        }
        ammuksetLiiku();
        vihollistenViiveAmmustenLiikkeeseen();
        paivita();
    }

    private void vihollistenViiveAmmustenLiikkeeseen() {
        vihollisetLiikkuViive--;
        vihollisetAmpuuViive--;
        if (vihollisetLiikkuViive == 0) {
            vihollisetLiiku();
            vihollisetLiikkuViive = liikkumisViive;
        }

        if (vihollisetAmpuuViive == 0) {
            jokuVihollinenAmpuu();
            vihollisetAmpuuViive = ampumisViive;
        }
    }
}
