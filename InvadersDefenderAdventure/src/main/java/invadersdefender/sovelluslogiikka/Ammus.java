package invadersdefender.sovelluslogiikka;

/**
 * Luokan olioilla on konstruktiossa annettu suunta, johon luokan oliot liikuttaessa aina liikkuvat
 * @author emivo
 */
public class Ammus extends Pala {

    private final Suunta suunta;

    public Ammus(int x, int y, Suunta suunta) {
        super(x, y);
        this.suunta = suunta;
    }
    /**
     * metodi antaa suunnan johon ammus on liikkumassa
     * @return paluttaa suunnan, johon ammus liikkuu
     */
    public Suunta getSuunta() {
        return suunta;
    }

    /**
     * metodilla siirretään oliota samoin kuin Pala.liiku(Suunta)
     * @see Pala#liiku(Suunta)
     */
    public void liiku() {
        liiku(this.suunta);
    }

}
