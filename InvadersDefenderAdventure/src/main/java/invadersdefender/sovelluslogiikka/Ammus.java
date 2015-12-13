package invadersdefender.sovelluslogiikka;

/**
 * Luokka Pala-luokan laajennos, niin että Ammus-luokalla on suunta, johon
 * liikuttaessa oliot liikuvat. Luokan oliot liikkuvat aina ainoastaan konstruktiossa
 * annettuun suuntaan.
 *
 * @author emivo
 */
public class Ammus extends Pala {

    private final Suunta suunta;

    /**
     * Luo {@code Ammus}-luokan olion.
     *
     * @param x x-koordinaatti, johon olio luodaan
     * @param y x-koordinaatti, johon olio luodaan
     * @param suunta suunta, johon olio aina liikkuu
     */
    public Ammus(int x, int y, Suunta suunta) {
        super(x, y);
        this.suunta = suunta;
    }

    /**
     * Metodi antaa suunnan, johon ammus on liikkumassa.
     *
     * @return suunta, johon ammus liikkumassa
     */
    public Suunta getSuunta() {
        return suunta;
    }

    /**
     * Metodilla siirretään oliota samoin kuin
     * {@link invadersdefender.sovelluslogiikka.Pala#liiku(Suunta)}, mutta olio
     * liikkuu aina vain siihen suuntaan, johon se on luotu kulkemaan.
     */
    public void liiku() {
        super.liiku(this.suunta);
    }

    /**
     * Metodi kutsuu {@link #liiku()}-metodin.
     *
     * @param suunta Parametrillä ei ole merkitystä, sillä ammukset liikkuvat
     * ainoastaan luotuun suuntaan.
     */
    @Override
    public void liiku(Suunta suunta) {
        liiku();
    }

}
