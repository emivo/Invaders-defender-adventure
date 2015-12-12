package invadersdefender.sovelluslogiikka;

import java.util.Random;

/**
 * Luokka perii luokan Vihollisolio ominaisuudet, mutta luo ammu() metodilla on
 * kaksi mahdollista paikkaa, johon Ammus voi ilmestyä. Pelin kannalta on
 * mielekästä että PomoVihollinen luokan olioiden koko olisi suurempi kuin
 * Vihollisolio.
 *
 * @author emivo
 */
public class PomoVihollinen extends Vihollisolio {

    /**
     * Luo {@code PomoVihollinen}-luokan olin annettuun paikkaan, annetun
     * kokoisena sekä annetulla elämäpisteillä.
     *
     * @param alkuX x-koordinaatti, johon alus luodaan
     * @param alkuY y-koordinaatti, johon alus luodaan
     * @param koko aluksen koko
     * @param elamapisteet elämäpisteet, jotka alus saa
     */
    public PomoVihollinen(int alkuX, int alkuY, int koko, int elamapisteet) {
        super(alkuX, alkuY, koko, elamapisteet);
    }

    /**
     * Metodi luo ammuksen, joko aluksen vasemmalle tai oikealle puolella
     * leveyssunnassa, mutta alapuolelle niin kuin Vihollisolio-luokassa.
     *
     * @see invadersdefender.sovelluslogiikka.Vihollisolio#ammu()
     * @return metodin luoma uusi {@code Ammus}
     */
    @Override
    public Ammus ammu() {
        boolean kumpiPuoli = new Random().nextBoolean();
        Ammus ammus;
        int x;
        int y = getY() + getKoko() + 1;
        if (kumpiPuoli) {
            x = getX() + (int) (getKoko() / 4);
            ammus = new Ammus(x, y, Suunta.ALAS);
        } else {
            x = getX() + (int) (getKoko() * 3 / 4);
            ammus = new Ammus(x, y, Suunta.ALAS);
        }
        return ammus;
    }

}
