package invadersdefender.sovelluslogiikka;

/**
 *
 * @author emivo
 */
public interface Liikkuva {
    public void liiku(Suunta suunta);
    public int getX();
    public int getY();
    public int getKoko();
}
