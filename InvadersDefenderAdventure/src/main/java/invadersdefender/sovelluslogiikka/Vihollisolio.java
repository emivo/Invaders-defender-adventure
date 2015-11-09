package invadersdefender.sovelluslogiikka;

/**
 *
 * @author Emil
 */
public class Vihollisolio extends Alus {

    private Suunta suunta;

    public Vihollisolio(int alkuX, int alkuY, int koko) {
        super(alkuX, alkuY, koko);
        this.suunta = Suunta.OIKEA;
    }

    @Override
    public Ammus ammu() {
        return new Ammus(getX() + getKoko() / 2, getY() + getKoko() + 1, Suunta.ALAS);
    }

    public Suunta getSuunta() {
        return suunta;
    }

    public void setSuunta(Suunta suunta) {
        this.suunta = suunta;
    }
    
    public void liiku() {
        liiku(suunta);
    }
}
