package invadersdefender.sovelluslogiikka;

/**
 * Rajapinta, joka määrää sen toteutaville luokille liikuttelu metodin sekä
 * getterit sijainnille ja koolle.
 *
 * @author emivo
 */
public interface Liikkuva {

    public void liiku(Suunta suunta);

    public int getX();

    public int getY();

    public int getKoko();
}
