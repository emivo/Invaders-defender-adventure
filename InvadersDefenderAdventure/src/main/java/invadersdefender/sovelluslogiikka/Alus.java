package invadersdefender.sovelluslogiikka;

import java.util.ArrayList;
import java.util.List;

/**
 * @author emivo
 */
public class Alus implements Liikkuva {

    private int koko; // aluksen sivunpituus
    private Pala sijainti;

    public Alus(int alkuX, int alkuY, int koko) {
        this.koko = koko;

        this.sijainti = new Pala(alkuX, alkuY);
    }

    @Override
    public int getKoko() {
        return koko;
    }

    public Ammus ammu() {
        return new Ammus(getX() + koko / 2, getY() - 1, Suunta.YLOS);
    }

    /**
     * Palauttaa totuus arvon osuuko alukseen jokin toinen liikkuva
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
     * metodi antaa luksen yläreunan koordinaatin
     *
     * @return
     */
    @Override
    public int getY() {
        return sijainti.getY();
    }

    /**
     * metodilla saadaan aluksen vasemman yläreunan palan muiden palojen paikat
     * voidaan laskea tämän avullla
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
