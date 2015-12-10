package invadersdefender.sovelluslogiikka;

/**
 * Luokka luo olion, joka perii pala luokan. Lisäominaisuuten luokan olioilla on
 * konstruktiossa annettu suunta, johon luokan oliot liikuttaessa aina
 * liikkuvat.
 *
 * @author emivo
 */
public class Ammus extends Pala {

    private final Suunta suunta;

    public Ammus(int x, int y, Suunta suunta) {
        super(x, y);
        this.suunta = suunta;
    }

    /**
     * Metodi antaa suunnan johon ammus on liikkumassa
     *
     * @return paluttaa suunnan, johon ammus liikkuu
     */
    public Suunta getSuunta() {
        return suunta;
    }

    /**
     * Metodilla siirretään oliota samoin kuin Pala.liiku(Suunta), mutta olio
     * liikkuu aina vain siihen suuntaan, johon se on luotu kulkemaan.
     *
     * @see Pala#liiku(Suunta)
     */
    public void liiku() {
        super.liiku(this.suunta);
    }

}
