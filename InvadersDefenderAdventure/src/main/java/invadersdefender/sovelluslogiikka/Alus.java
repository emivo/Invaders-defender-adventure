package invadersdefender.sovelluslogiikka;


/**
 * Luokka muodostaa olioita, joita voidaan liikutella x,y koordinaatistossa ja voiva ampua {@link Ammus} luokan oliota
 * 
 * @author emivo
 */
public class Alus implements Liikkuva {

    private final int koko; // aluksen sivunpituus
    private final Pala sijainti;

    public Alus(int alkuX, int alkuY, int koko) {
        this.koko = koko;

        this.sijainti = new Pala(alkuX, alkuY);
    }

    @Override
    public int getKoko() {
        return koko;
    }

    /**
     * Luo {@link Ammus} -luokan olion aluksen yläreunan yläpuolelle ja leveyssuunnassa aluksen keskelle
     * 
     * @return {@link Ammus} -luokan olio
     */
    public Ammus ammu() {
        return new Ammus(getX() + koko / 2, getY() - 1, Suunta.YLOS);
    }

    /**
     * Palauttaa totuus arvon osuuko alukseen jokin toinen liikkuva.
     * Kaikki liikkuvat ovat neliön muotoisia, joten metodi tutkii osuuko jokin kulma 
     * 
     * @param liikkuva vertailtava liikkuva
     * @return Palauttaa true, jos jokin liikkuva on samalla koordinaatilla
     * aluksen kanssa
     */
    public boolean osuukoAlukseen(Liikkuva liikkuva) {
        // liikkuvat ovat neliön muotoisia jolloin nurkat riittää tarkistaa
        boolean vasenYlakulma = (getX() <= liikkuva.getX()
                && getX() + getKoko() > liikkuva.getX())
                && (getY() <= liikkuva.getY()
                && getY() + getKoko() > liikkuva.getY());
        if (liikkuva.getKoko() == 1 || vasenYlakulma) {
            return vasenYlakulma;
        }
        boolean oikeaYlakulma = osuukoAlukseen(new Pala(liikkuva.getX() + (liikkuva.getKoko() - 1), liikkuva.getY()));
        if (oikeaYlakulma) return oikeaYlakulma;
        boolean vasenAlakulma = osuukoAlukseen(new Pala(liikkuva.getX(), liikkuva.getY() + (liikkuva.getKoko() - 1)));
        if (vasenAlakulma) return vasenAlakulma;
        // oikea alakulma
        return osuukoAlukseen(new Pala(liikkuva.getX() + (liikkuva.getKoko() - 1), liikkuva.getY() + (liikkuva.getKoko() - 1)));

    }

    /**
     * metodi antaa aluksen vasemman reunan x koordinaatin
     *
     * @return
     */
    @Override
    public int getX() {
        return sijainti.getX();
    }

    /**
     * metodi antaa aluksen yläreunan koordinaatin
     *
     * @return
     */
    @Override
    public int getY() {
        return sijainti.getY();
    }

    /**
     * metodilla saadaan aluksen vasemman yläreunan palan. Muiden palojen paikat
     * voidaan laskea tämän avulla 
     *
     * @return
     */
    public Pala sijainti() {
        return sijainti;
    }

    @Override
    public void liiku(Suunta suunta) {
        this.sijainti.liiku(suunta);
    }
}
