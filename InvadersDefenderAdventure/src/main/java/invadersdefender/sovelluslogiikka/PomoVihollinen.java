package invadersdefender.sovelluslogiikka;

import java.util.Random;

/**
 * Luokka luo olion, joka perii luokan Vihollisolio ominaisuudet, mutta ammu() metodilla on kaksi mahdollista paikkaa, johon Ammus voi ilmestyä.
 * Pelin kannalta on mielekästä että PomoVihollinen luokan olioiden koko olisi suurempi kuin Vihollisolio.
 * @author emivo
 */
public class PomoVihollinen extends Vihollisolio {

    public PomoVihollinen(int alkuX, int alkuY, int koko, int hp) {
        super(alkuX, alkuY, koko, hp);
    }

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
