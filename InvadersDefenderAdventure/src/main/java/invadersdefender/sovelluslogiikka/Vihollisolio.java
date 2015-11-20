package invadersdefender.sovelluslogiikka;

/**
 *  Luokka toimii samoin tavoin kuin yläluokkansa, mutta sisältää {@code Suunta} muuttujan, jonka avulla voidaan määritellä olion liikkumista.
 * Poiketen {@link Alus} luokan olioihin, tämän luokan oliot {@code ammu()} metodi luo ammukset olion alapuolelle ja niiden suunta on alas.
 * 
 * @author emivo
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

    /**
     * Palauttaa suunnan, johon olio on seuraavan kerran liikkumassa
     * @return suunta, johon olio on liikkumassa
     */
    public Suunta getSuunta() {
        return suunta;
    }

    /**
     * Asettaa olion liikkumissuunnan, johon seuraavan kerran liikutaan, kun kutsutaan {@code liiku()}
     * @param suunta Suunta, johon halutaan olion liikkuvan
     */
    public void setSuunta(Suunta suunta) {
        this.suunta = suunta;
    }

    /**
     * metodi liikuttaa oliota, sen sille asetetun suunnan mukaan
     * @see setSuunta(Suunta)
     */
    public void liiku() {
        super.liiku(suunta);
    }
}
