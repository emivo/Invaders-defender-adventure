package invadersdefender.sovelluslogiikka;

/**
 * Luokka toimii kuin Alus luokan tavoin, mutta se sisältää {@code Suunta} muuttujan, jonka avulla voidaan määritellä olion liikkumista.
 * Luokan oliot {@code ammu()} metodi luo ammukset olion alapuolelle ja niiden suunta on alas. Eikä setElamapisteet() metodi tee mitään.
 * 
 * @author emivo
 */
public class Vihollisolio extends Alus {

    private Suunta suunta;

    public Vihollisolio(int alkuX, int alkuY, int koko, int hp) {
        super(alkuX, alkuY, koko, hp);
        this.suunta = Suunta.OIKEA;
    }

    @Override
    public Ammus ammu() {
        return new Ammus(getX() + getKoko() / 2, getY() + getKoko() + 1, Suunta.ALAS);
    }
    
    /**
     * Vihollisolion elämäpisteitä ei saa muutta setMetodilla, joten tämä metodi ei tee mitään
     * @param elamapisteet 
     */
    @Override
    public void setElamapisteet(int elamapisteet) {
        // älä tee mitään
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
     * Metodi liikuttaa oliota, sen sille asetetun suunnan mukaan.
     */
    public void liiku() {
        super.liiku(suunta);
    }
}
