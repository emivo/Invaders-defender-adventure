package invadersdefender.sovelluslogiikka;

/**
 * Abstrakti alus-luokka, joka sisältää kaikkille aluksille tulevat piirteet
 * esimerkiksi liikuttamisen x,y koordinaatistossa ja ampumisen.
 *
 * @author emivo
 */
public abstract class Alus implements Liikkuva {

    private final int koko;
    private final Pala sijainti;
    private int elamapisteet;

    public Alus(int alkuX, int alkuY, int koko, int elamapisteet) {
        this.koko = koko;

        this.sijainti = new Pala(alkuX, alkuY);
        this.elamapisteet = elamapisteet;
    }

    /**
     * Metodilla saadaa neliönmuotoisen aluksen sivunpituus.
     *
     * @return aluksen sivunpituus
     */
    @Override
    public int getKoko() {
        return koko;
    }

    /**
     * Jokaisella luotavalla aluksella on mahdollisuus ampua.
     *
     * @return palauttaa ammutun Ammuksen
     */
    abstract Ammus ammu();

    /**
     * Metodi tarkistaa osuuko annettu liikkuva tähän alukseen.
     *
     * @param liikkuva vertailtava liikkuva
     * @return Palauttaa true, jos jokin liikkuva on samalla koordinaatilla
     * aluksen kanssa
     */
    public boolean osuukoAlukseen(Liikkuva liikkuva) {
        // liikkuvat ovat neliön muotoisia, jolloin nurkat riittää tarkistaa
        boolean vasenYlakulma = (getX() <= liikkuva.getX()
                && getX() + getKoko() > liikkuva.getX())
                && (getY() <= liikkuva.getY()
                && getY() + getKoko() > liikkuva.getY());
        if (liikkuva.getKoko() == 1 || vasenYlakulma) {
            return vasenYlakulma;
        }
        boolean oikeaYlakulma = osuukoAlukseen(new Pala(liikkuva.getX() + (liikkuva.getKoko() - 1), liikkuva.getY()));
        if (oikeaYlakulma) {
            return oikeaYlakulma;
        }
        boolean vasenAlakulma = osuukoAlukseen(new Pala(liikkuva.getX(), liikkuva.getY() + (liikkuva.getKoko() - 1)));
        if (vasenAlakulma) {
            return vasenAlakulma;
        }
        // oikea alakulma
        return osuukoAlukseen(new Pala(liikkuva.getX() + (liikkuva.getKoko() - 1), liikkuva.getY() + (liikkuva.getKoko() - 1)));

    }

    /**
     * Metodi antaa aluksen vasemman reunan koordinaatin.
     *
     * @return x -koordinaatti vasemmassa yläreunassa
     */
    @Override
    public int getX() {
        return sijainti.getX();
    }

    /**
     * Metodi antaa aluksen yläreunan koordinaatin.
     *
     * @return y -koordinaatti vasemmassa yläreunassa.
     */
    @Override
    public int getY() {
        return sijainti.getY();
    }

    /**
     * Metodi antaa aluksen elämäpisteet.
     *
     * @return aluksen elämäpisteet
     */
    public int getElamapisteet() {
        return elamapisteet;
    }

    /**
     * Metodilla voidaan asettaa uudet elämäpisteet alukselle.
     *
     * @param elamapisteet Pistemäärä, joka halutaan asettaa aluksen
     * elämäpisteiksi
     */
    public void setElamapisteet(int elamapisteet) {
        this.elamapisteet = elamapisteet;
    }

    /**
     * Metodilla saadaan aluksen vasemman yläreunan palan. Muiden palojen paikat
     * voidaan laskea tämän avulla.
     *
     * @return Palauttaa Pala-oliona aluksen vasemman yläreunan palan.
     */
    public Pala sijainti() {
        return sijainti;
    }

    /**
     * Metodi liikutta aluksen sijainti palasta haluttuun suuntaan.
     *
     * @param suunta Suunta, johon halutaan palasta liikuttaa
     */
    @Override
    public void liiku(Suunta suunta) {
        this.sijainti.liiku(suunta);
    }

    /**
     * Metodi vähentää aluksen elämäpisteitä kymmenellä.
     */
    public void vahennaElamapisteita() {
        this.elamapisteet -= 10;
    }
}
