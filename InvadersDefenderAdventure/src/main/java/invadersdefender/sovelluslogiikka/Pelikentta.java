package invadersdefender.sovelluslogiikka;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Luokka hallistee aluksia ja ammuksia ja ilmoittaa tapahtuvista muutoksista
 * {@code Peli}:lle
 *
 * @author emivo
 */
public class Pelikentta {

    private Peli peli;

    private OmaAlus omaAlus;
    private final List<Vihollisolio> viholliset;
    private Vihollisolio pomo;
    private final List<Ammus> ammukset;
    private final int pelikentanLeveys;
    private final int pelikentanKorkeus;
    private static final int ALUKSIENKOKO = 3;
    private int vihollistenKestavyys;

    public Pelikentta(int pelikentanKorkeus, Peli peli) {
        this.peli = peli;
        this.pelikentanKorkeus = pelikentanKorkeus * ALUKSIENKOKO;
        this.pelikentanLeveys = (int) (this.pelikentanKorkeus * 2 / 3);
        this.omaAlus = luoOmaAlus();
        vihollistenKestavyys = 10;

        this.viholliset = new ArrayList<>();
        this.ammukset = new ArrayList<>();
        tuhoaPomo();
    }

    /**
     * Luo oman aluksen kentän alareunaan keskelle
     *
     * @return Alus, joka toimii pelaajan aluksena
     */
    private OmaAlus luoOmaAlus() {
        return new OmaAlus(this.pelikentanLeveys / 2, this.pelikentanKorkeus - (ALUKSIENKOKO + 1), ALUKSIENKOKO);
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

    public OmaAlus getOmaAlus() {
        return omaAlus;
    }

    public Vihollisolio getPomo() {
        return pomo;
    }

    /**
     * Metodi käskee parametrinä annetua alusta ampumaan ja asettaa tämän
     * ampuman ammuksen, joka lisätään ammukset listaan
     *
     * @param alus olio, jonka koordinaatien perusteella {@code Ammus} saa
     * sijaintinsa ja suuntansa
     */
    public void alusAmmu(Alus alus) {
        if (alus.getClass() != OmaAlus.class) {
            ammukset.add(alus.ammu());
        } else {
            omaAlusAmmu();
        }
    }

    private void omaAlusAmmu() {
        int omiaAmmuksiaKentalla = 0;
        int sallitutAmmuksetKentalla = 7;
        if (omaAlus.getAseistus() == Aseistus.TUPLA) {
            sallitutAmmuksetKentalla++;
            sallitutAmmuksetKentalla *= 2;
        } else if (omaAlus.getAseistus() == Aseistus.TRIPLA) {
            sallitutAmmuksetKentalla += 2;
            sallitutAmmuksetKentalla *= 3;
        }

        for (Ammus ammus : ammukset) {
            if (ammus.getSuunta() == Suunta.YLOS) {
                omiaAmmuksiaKentalla++;
            }
        }

        if (omiaAmmuksiaKentalla < sallitutAmmuksetKentalla) {
            ammukset.add(omaAlus.ammu());
            if (omaAlus.getAseistus() != Aseistus.NORMAALI) {
                ammukset.addAll(omaAlus.ammuEnemman());
            }
        }
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

            vahennaOmanAluksenElamapisteita();
            return true;
        }

        if (osuukoAmmusKasitteleVihollisetListassa(ammus)) {
            return true;
        }

        return osuukoAmmusPomoon(ammus);
    }

    private void vahennaOmanAluksenElamapisteita() {
        omaAlus.vahennaElamapisteita();
        if (omaAlus.getElamapisteet() <= 0) {
            lisaaRajahdys(omaAlus);
            peli.peliLoppuu();
        }
    }

    private boolean osuukoAmmusPomoon(Ammus ammus) {
        if (pomo != null) {
            if (pomo.osuukoAlukseen(ammus) && (ammus.getSuunta() == Suunta.YLOS)) {
                pomoVahennaElamapisteita();
                return true;
            }
        }
        return false;
    }

    private void pomoVahennaElamapisteita() {
        pomo.vahennaElamapisteita();
        if (pomo.getElamapisteet() <= 0) {
            peli.lisaaPisteita();
            peli.lisaaPisteita();
            lisaaRajahdys(pomo);
            tuhoaPomo();
        }
    }

    private boolean osuukoAmmusKasitteleVihollisetListassa(Ammus ammus) {
        Iterator<Vihollisolio> iterator = viholliset.iterator();
        while (iterator.hasNext()) {
            Vihollisolio vihollinen = iterator.next();
            if (vihollinen.osuukoAlukseen(ammus) && (ammus.getSuunta() == Suunta.YLOS)) {
                vihollinen.vahennaElamapisteita();
                if (vihollinen.getElamapisteet() <= 0) {
                    lisaaRajahdys(vihollinen);
                    iterator.remove();
                    peli.lisaaPisteita();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * metodi asettaa vihollisoliota kentän yläreunan yläpuolelle, josta ne
     * lähtevät liikkumaan alas kentälle päin
     *
     * @param montako Kuinka monta vihollista tulee esille
     */
    public void vihollisetTulevatEsille(int montako) {

        // asetetaan viholliset pelikentältä liian ylös peli kentästä mistä ne voivat sitten "ryömiä esiin"
        // voivat mennä reunoilta yli ja ryömiä esiin sivusta lisäksi
        Random satunaismuuttuja = new Random();
        int alusRykelmanLeveys = montako * ALUKSIENKOKO * 2;
        int ensimmaisenAluksenX = satunaismuuttuja.nextInt(pelikentanLeveys + alusRykelmanLeveys - 1) - alusRykelmanLeveys;
        int aluksienYSuuntainenValitys = (int) (ALUKSIENKOKO * 1.5);
        for (int i = 0; i < montako; i++) {
            int x = ensimmaisenAluksenX + (ALUKSIENKOKO * 2 * i);

            int y = -1 * (montako * aluksienYSuuntainenValitys) + i * aluksienYSuuntainenValitys;
            Vihollisolio vihollinen = new Vihollisolio(x, y, ALUKSIENKOKO, vihollistenKestavyys);

            viholliset.add(vihollinen);
        }
    }

    /**
     * Pomo vihollinen saapuu peliin. Pomo vihollisen koko on tuplasti suurempi
     * kuin tavallisten vihollisten ja sen kestävyys on myös hieman parempi
     */
    public void pomoVihollinenTuleeEsille() {
        pomo = new PomoVihollinen(pelikentanLeveys / 2, -1 * ALUKSIENKOKO * 2, 2 * ALUKSIENKOKO, vihollistenKestavyys + 10);
    }

    /**
     * Metodi liikuttaa kaikkia kentällä olevia ammuksia sekä tarkistaa osuvatko
     * ne johonkin alukseen tai poistuvatko ne kentältä
     */
    public void ammuksetLiiku() {
        Iterator<Ammus> iterator = ammukset.iterator();
        while (iterator.hasNext()) {
            Ammus ammus = iterator.next();
            ammus.liiku();
            // ammukset eivät voi poistua kentänreunalta
            if (ammus.getY() < 0 || ammus.getY() > pelikentanKorkeus) {
                iterator.remove();
            } else if (osuukoAmmus(ammus)) {
                lisaaRajahdys(ammus);
                iterator.remove();
            }
        }
    }

    private void lisaaRajahdys(Liikkuva liikkuva) {
        peli.lisaaRajahdysPiirrettavaksiKohtaan(new Pala(liikkuva.getX(), liikkuva.getY(), liikkuva.getKoko()));
    }

    private void osuukoAmmukset() {
        Iterator<Ammus> iterator = ammukset.iterator();
        while (iterator.hasNext()) {
            Ammus ammus = iterator.next();
            if (osuukoAmmus(ammus)) {
                lisaaRajahdys(ammus);
                iterator.remove();
            }
        }
    }

    /**
     * Metodi liikuttaa omaa alusta pelikentällä
     *
     * @param suunta Suunta, johon alusta halutaan liikuttaa
     */
    public void omaAlusLiiku(Suunta suunta) {
        if (voikoOmaAlusLiikkua(suunta)) {
            omaAlus.liiku(suunta);
        }
        osuvatkoVihollisetOmaanAlukseen();
        osuukoAmmukset();
    }

    /**
     * Metodi tarkistaa osuuko jokin vihollisista omaan alukseen
     *
     */
    public void osuvatkoVihollisetOmaanAlukseen() {
        Iterator<Vihollisolio> iteraattori = viholliset.iterator();
        while (iteraattori.hasNext()) {
            Vihollisolio vihollinen = iteraattori.next();
            if (omaAlus.osuukoAlukseen(vihollinen)) {
                lisaaRajahdys(vihollinen);
                iteraattori.remove();
                peli.lisaaPisteita();
                omaAlus.vahennaElamapisteita();
                vahennaOmanAluksenElamapisteita();

            }
        }
        if (pomo != null && omaAlus.osuukoAlukseen(pomo)) {
            pomoVahennaElamapisteita();
            omaAlus.vahennaElamapisteita();
            vahennaOmanAluksenElamapisteita();
        }
    }

    /**
     * Metodi käskee jokaisen vihollisolion kentällä liikkumaan
     */
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

            pomoLiiku();

            osuukoAmmukset();
            osuvatkoVihollisetOmaanAlukseen();
        }
    }

    private void pomoLiiku() {
        if (pomo != null) {
            if (pomo.getY() - pomo.getKoko() > 0) {
                boolean satunnainenAlasMeno = new Random().nextBoolean();
                if (satunnainenAlasMeno) {
                    pomo.liiku(Suunta.ALAS);
                } else {
                    valitseVihollistenSuunta(pomo);
                }
                boolean satunnainenSuunnanVaihto = new Random().nextBoolean();
                if (satunnainenSuunnanVaihto) {
                    if (pomo.getSuunta() == Suunta.VASEN) {
                        pomo.setSuunta(Suunta.OIKEA);
                    } else {
                        pomo.setSuunta(Suunta.VASEN);
                    }
                }
            } else {
                pomo.liiku(Suunta.ALAS);
            }

            if (pomo.getY() > pelikentanKorkeus) {
                tuhoaPomo();
            }
        }
    }

    private void tuhoaPomo() {
        pomo = null;
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
        if (vihollinen.getX() <= 1) {
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
        if (vihollinen.getX() + vihollinen.getKoko() >= pelikentanLeveys - 1) {
            kaannaVihollinen(vihollinen, Suunta.VASEN);
        } else {
            vihollinen.liiku();
        }
    }


    /**
     * Satunnainen kentällä oleva vihollinen ampuu {@code Ammus} olion
     */
    public void jokuVihollinenAmpuu() {
        if (!viholliset.isEmpty()) {
            Vihollisolio vihu = viholliset.get(new Random().nextInt(viholliset.size()));
            if (vihu.getY() >= 0 && vihu.getX() + vihu.getKoko() >= 0 && vihu.getX() <= pelikentanLeveys) {
                alusAmmu(vihu);
            }
        }
        if (pomo != null) {
            alusAmmu(pomo);
        }
    }

    private boolean voikoOmaAlusLiikkua(Suunta suunta) {
        Pala uusiSijainti = new Pala(omaAlus.getX(), omaAlus.getY());
        uusiSijainti.liiku(suunta);
        return !(uusiSijainti.getX() < 0 || uusiSijainti.getX() + omaAlus.getKoko() > pelikentanLeveys
                || uusiSijainti.getY() < 0 || uusiSijainti.getY() + omaAlus.getKoko() > pelikentanKorkeus);
    }

    /**
     * Nollaa pelikentän alkuasentoon, ei ammuksia ei vihollisia ja oma alus
     * alkupaikalla
     */
    public void kaynnistaUudelleen() {
        this.omaAlus = luoOmaAlus();

        this.viholliset.clear();
        this.ammukset.clear();
        this.vihollistenKestavyys = 10;

        tuhoaPomo();

    }

    /**
     * Vihollisten ammusten kestävyys paranee yhdellä metodia kutsuttaessa.
     * Vihollisten kestävyyden maksimi on 100
     */
    public void parannaVihollistenKestavyytta() {
        if (vihollistenKestavyys < 100) {
            this.vihollistenKestavyys += 10;
        }
    }

}
