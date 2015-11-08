package invadersdefender.sovelluslogiikka;

/**
 *
 * @author Emil
 */
public interface Liikkuva {
    public void liiku(Suunta suunta);
    public int getX();
    public int getY();
    public int getKoko();
}
