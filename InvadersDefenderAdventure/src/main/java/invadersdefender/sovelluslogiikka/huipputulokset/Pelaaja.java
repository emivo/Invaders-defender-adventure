package invadersdefender.sovelluslogiikka.huipputulokset;

import java.io.Serializable;

/**
 *
 * @author Emil
 */
public class Pelaaja implements Comparable<Pelaaja>, Serializable {

    private final int tulos;
    private final String nimi;

    public Pelaaja(int tulos, String nimi) {
        this.tulos = tulos;
        this.nimi = nimi;
    }

    public String getNimi() {
        return nimi;
    }

    public int getTulos() {
        return tulos;
    }

    @Override
    public int compareTo(Pelaaja o) {
        return o.getTulos() - this.tulos;
    }

}
