package invadersdefender.sovelluslogiikka;

import java.util.ArrayList;
import java.util.List;

/**
 * Luokalla luodaan pelille pelaajan oma alus. Oman aluksen aseistusta on alussa
 * on normaali, mutta sitä voidaan parantaa tuplasti tai triplasti ampuvaan.
 * Oman aluksen elämäpisteet ovat alussa 10, mutta pisteitä on mahdollista
 * lisätä.
 *
 * @author emivo
 */
public class OmaAlus extends Alus {

    private Aseistus aseistus;

    /**
     * Luo OmaAlus-luokan olion, jolle asetetaan alkusijainti sekä koko.
     *
     * @param alkuX x-koordinaatti, johon aluksen vasen ylänurkan pala tulee.
     * @param alkuY y-koordinaatti, johon aluksen vasen ylänurkan pala tulee
     * @param koko aluksen sivunpituus
     */
    public OmaAlus(int alkuX, int alkuY, int koko) {
        super(alkuX, alkuY, koko, 100);
        aseistus = Aseistus.NORMAALI;
    }

    /**
     * Luo {@link Ammus} -luokan olion aluksen yläreunan yläpuolelle ja
     * leveyssuunnassa aluksen keskelle
     *
     * @return {@link Ammus} -luokan olio
     */
    @Override
    public Ammus ammu() {
        if (aseistus == Aseistus.TUPLA) {
            return new Ammus(getX(), getY() - 1, Suunta.YLOS);
        } else {
            return new Ammus(getX() + getKoko() / 2, getY() - 1, Suunta.YLOS);
        }
    }

    /**
     * Jos aluksen Aseistus on muu kuin normaali niin tällä metodilla saadaan
     * lista muita ammuksia, joita alus haluaa luoda paremalla aseituksella.
     *
     * @return Palauttaa listan ammuksia, joita alus haluaisi ammuttavan.
     */
    public List<Ammus> ammuEnemman() {
        ArrayList<Ammus> ammukset = new ArrayList<>();
        if (aseistus.compareTo(Aseistus.TUPLA) >= 0) {
            ammukset.add(new Ammus(getX() + getKoko() - 1, getY() - 1, Suunta.YLOS));
        }
        if (aseistus == Aseistus.TRIPLA) {
            ammukset.add(new Ammus(getX(), getY() - 1, Suunta.YLOS));
        }

        return ammukset;
    }

    /**
     * Metodi parantaa aluksen aseistusta.
     *
     * @param uusiAseistus Uusi aseistus, joka alukselle asennetaan
     */
    public void parannaAseistusta(Aseistus uusiAseistus) {
        aseistus = uusiAseistus;
    }

    public Aseistus getAseistus() {
        return aseistus;
    }

    /**
     * Metodilla voi korjata omaa alusta niin, että sen elämäpisteet
     * lisääntyvät.
     *
     * @param kuinkaPaljon kuinka monta elämäpistettä alukselle lisätään
     */
    public void korjaaSuojausta(int kuinkaPaljon) {
        setElamapisteet(getElamapisteet() + kuinkaPaljon);
    }
}
