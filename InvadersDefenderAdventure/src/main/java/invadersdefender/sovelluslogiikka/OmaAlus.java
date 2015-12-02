package invadersdefender.sovelluslogiikka;

import java.util.ArrayList;
import java.util.List;

/**
 * Luokalla luodaan pelille pelaajan oma alus. Oman aluksen aseistusta on
 * mahdollista parantaa pelin edetessä Oman aluksen elämäpisteet ovat alussa 10,
 * mutta pisteillä voidaan ostaa lisää suojaa
 *
 * @author emivo
 */
public class OmaAlus extends Alus {

    private Aseistus aseistus;

    public OmaAlus(int alkuX, int alkuY, int koko) {
        super(alkuX, alkuY, koko, 10);
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
     * Paranna aluksen aseistusta
     *
     * @param uusiAseistus Uusi aseistus, joka alukselle asennetaan
     */
    public void parannaAseistusta(Aseistus uusiAseistus) {
        aseistus = uusiAseistus;
    }

    /**
     * Aseistus get metodi
     *
     * @return palauttaa aluksen aseistuksen
     */
    public Aseistus getAseistus() {
        return aseistus;
    }

    public void korjaaSuojausta(int kuinkaPaljon) {
        setElamapisteet(getElamapisteet() + kuinkaPaljon);
    }
}
