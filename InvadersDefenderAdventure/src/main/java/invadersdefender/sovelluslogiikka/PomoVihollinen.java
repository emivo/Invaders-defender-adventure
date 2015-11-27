package invadersdefender.sovelluslogiikka;

import java.util.Random;

/**
 *
 * @author emivo
 */
public class PomoVihollinen extends Vihollisolio {

    public PomoVihollinen(int alkuX, int alkuY, int koko) {
        super(alkuX, alkuY, koko);
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
