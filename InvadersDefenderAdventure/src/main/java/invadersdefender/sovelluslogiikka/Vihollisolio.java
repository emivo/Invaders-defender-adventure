package invadersdefender.sovelluslogiikka;

/**
 *
 * @author Emil
 */
public class Vihollisolio extends Alus {
    
    private Suunta suunta;

    public Vihollisolio(int alkuX, int alkuY) {
        super(alkuX, alkuY);
        this.suunta = Suunta.ALAS;
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
