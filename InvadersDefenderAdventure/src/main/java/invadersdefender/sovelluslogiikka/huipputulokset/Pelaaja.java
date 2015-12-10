package invadersdefender.sovelluslogiikka.huipputulokset;

import java.io.Serializable;
import java.util.Objects;

/**
 * Luokka toimii apuna tallentaa nimi ja numero yhtenä oliona sekä helpottaa niiden järjestämistä.
 * @author emivo
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + this.tulos;
        hash = 17 * hash + Objects.hashCode(this.nimi);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pelaaja other = (Pelaaja) obj;
        if (this.tulos != other.tulos) {
            return false;
        }
        return Objects.equals(this.nimi, other.nimi);
    }

    @Override
    public String toString() {
        return nimi + "  " + tulos;
    }

}
