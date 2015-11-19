package invadersdefender.sovelluslogiikka;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author emivo
 */
public class Pelikentta {

    private Peli peli;

    private Alus omaAlus;
    private final List<Vihollisolio> viholliset;
    private final List<Ammus> ammukset;
    private final int pelikentanLeveys;
    private final int pelikentanKorkeus;
    private static final int ALUKSIENKOKO = 3;

    public Pelikentta(int pelikentanSivunpituus, Peli peli) {
        this.peli = peli;
        this.pelikentanKorkeus = pelikentanSivunpituus * ALUKSIENKOKO;
        this.pelikentanLeveys = (int) (pelikentanKorkeus * 2 / 3);
        this.omaAlus = luoOmaAlus();

        this.viholliset = new ArrayList<>();
        this.ammukset = new ArrayList<>();
    }

    private Alus luoOmaAlus() {
        return new Alus(this.pelikentanLeveys / 2, this.pelikentanKorkeus - (ALUKSIENKOKO + 1), ALUKSIENKOKO);
    }

    public List<Ammus> getAmmukset() {
        return ammukset;
    }

    public List<Vihollisolio> getViholliset() {
        return viholliset;
    }

    public int getPelikentanLeveys() {
        return pelikentanLeveys;
    }

    public int getPelikentanKorkeus() {
        return pelikentanKorkeus;
    }

    public Alus getOmaAlus() {
        return omaAlus;
    }

    public void alusAmmu(Alus alus) {
        ammukset.add(alus.ammu());
    }

    public Peli getPeli() {
        return peli;
    }

    /**
     * Metodi tarkastaa osuuko parametrinÃ¤ annettu ammus johonkin pelissÃ¤
     * olevaan alukseen
     *
     * @param ammus tarkasteltava ammus
     * @return totuusarvo osuuko ammmus johonkin alukseen
     */
    public boolean osuukoAmmus(Ammus ammus) {
        if (omaAlus.osuukoAlukseen(ammus) && (ammus.getSuunta() == Suunta.ALAS)) {
            // lopeta peli
            peli.peliLoppuu();
            return true;
        }

        Iterator<Vihollisolio> iterator = viholliset.iterator();
        while (iterator.hasNext()) {
            Vihollisolio vihollinen = iterator.next();
            if (vihollinen.osuukoAlukseen(ammus) && (ammus.getSuunta() == Suunta.YLOS)) {
                iterator.remove();
                peli.lisaaPisteita();
                return true;
            }
        }
        return false;
    }

    public void vihollisetTulevatEsille() {
        // kuinka monta vihollista tulee
        // KESKEN
        int esiinTulevienVihollisteMaara = 5;
        // asetetaan viholliset pelikentältä liian ylös peli kentästä mistä ne voivat sitten "ryömiä esiin"
        int ensimmaisenAluksenX = (int) (pelikentanLeveys / 2) - (esiinTulevienVihollisteMaara - 1) * ALUKSIENKOKO;
        for (int i = 0; i < esiinTulevienVihollisteMaara; i++) {
            int x = ensimmaisenAluksenX + (ALUKSIENKOKO * 2 * i);
            int y = -1 * (esiinTulevienVihollisteMaara * ALUKSIENKOKO) + i * ALUKSIENKOKO;
            Vihollisolio vihollinen = new Vihollisolio(x, y, ALUKSIENKOKO);

            viholliset.add(vihollinen);
        }
    }

    public void ammuksetLiiku() {
        Iterator<Ammus> iterator = ammukset.iterator();
        while (iterator.hasNext()) {
            Ammus ammus = iterator.next();
            ammus.liiku();
            // ammukset eivät voi poistua kentänreunalta
            if (ammus.getSijainti().getY() < 0 || ammus.getSijainti().getY() > pelikentanKorkeus) {
                iterator.remove();
            } else if (osuukoAmmus(ammus)) {
                iterator.remove();
            }
        }
    }

    private void osuukoAmmukset() {
        Iterator<Ammus> iterator = ammukset.iterator();
        while (iterator.hasNext()) {
            Ammus ammus = iterator.next();
            if (osuukoAmmus(ammus)) {
                iterator.remove();
            }
        }
    }

    public void omaAlusLiiku(Suunta suunta) {
        if (voikoLiikkua(omaAlus, suunta)) {
            omaAlus.liiku(suunta);
        }
        if (osuukoVihollisetOmaanAlukseen()) {
            peli.peliLoppuu();
        }
        osuukoAmmukset();
    }

    public void vihollisetLiiku() {
        if (!viholliset.isEmpty()) {
            boolean onkoViimeinenVihollinenKentalla = viholliset.get(0).getY() >= 0;
            Iterator<Vihollisolio> iteraattori = viholliset.iterator();
            while (iteraattori.hasNext()) {
                Vihollisolio vihollinen = iteraattori.next();
                if (onkoViimeinenVihollinenKentalla) {
                    valitseVihollistenSuunta(vihollinen);
                    if (vihollinen.getY() > pelikentanKorkeus) {
                        iteraattori.remove();
                    }
                } else {
                    vihollinen.liiku(Suunta.ALAS);
                }
            }

            osuukoAmmukset();
            if (osuukoVihollisetOmaanAlukseen()) {
                peli.peliLoppuu();
            }
            if (!viholliset.isEmpty()) {
                if (viholliset.get(viholliset.size() - 1).getY() > pelikentanKorkeus) {
                    viholliset.remove(viholliset.size() - 1);
                }
            }

        }
    }

    private void valitseVihollistenSuunta(Vihollisolio vihollinen) {

        Suunta suunta = vihollinen.getSuunta();
        if (suunta == Suunta.OIKEA) {
            kasitteleKunMenossaOikealle(vihollinen);

        } else if (suunta == Suunta.VASEN) {
            kasitteleKunMenossaVasemmalle(vihollinen);
        }

    }

    private void kasitteleKunMenossaVasemmalle(Vihollisolio vihollinen) {
        if (vihollinen.getX() == 1) {
            kaannaVihollinen(vihollinen, Suunta.OIKEA);
        } else {
            vihollinen.liiku();
        }
    }

    private void kaannaVihollinen(Vihollisolio vihollinen, Suunta suunta) {
        vihollinen.setSuunta(Suunta.ALAS);
        vihollinen.liiku();
        vihollinen.setSuunta(suunta);
    }

    private void kasitteleKunMenossaOikealle(Vihollisolio vihollinen) {
        if (vihollinen.getX() + ALUKSIENKOKO == pelikentanLeveys - 1) {
            kaannaVihollinen(vihollinen, Suunta.VASEN);
        } else {
            vihollinen.liiku();
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

    public void jokuVihollinenAmpuu() {
        if (!viholliset.isEmpty()) {
            Vihollisolio vihu = viholliset.get(new Random().nextInt(viholliset.size()));
            if (vihu.getY() >= 0) {
                alusAmmu(vihu);
            }
        }
    }

    private boolean voikoLiikkua(Alus alus, Suunta suunta) {
        Pala uusiSijainti = new Pala(alus.getX(), alus.getY());
        uusiSijainti.liiku(suunta);
        return !(uusiSijainti.getX() < 0 || uusiSijainti.getX() + alus.getKoko() > pelikentanLeveys
                || uusiSijainti.getY() < 0 || uusiSijainti.getY() + alus.getKoko() > pelikentanKorkeus);
    }

    void kaynnistaUudelleen() {
        this.omaAlus = luoOmaAlus();

        this.viholliset.clear();
        this.ammukset.clear();

    }

}
