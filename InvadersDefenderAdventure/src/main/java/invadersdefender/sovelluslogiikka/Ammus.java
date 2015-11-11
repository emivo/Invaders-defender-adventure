package invadersdefender.sovelluslogiikka;
/**
 * @author emivo
 */
public class Ammus implements Liikkuva {
    
    private final Suunta suunta;
    private Pala sijainti;

    
    // ammus on nyt yhden kokoinen?!"
    // Ammukset eivät ainakaan tässä vaiheessa saa vaihtaa suunta
    public Ammus(int x, int y, Suunta suunta) {
        this.sijainti = new Pala(x,y);
        this.suunta = suunta;
    }

    @Override
    public int getX() {
        return sijainti.getX();
    }

    @Override
    public int getY() {
        return sijainti.getY();
    }

    public Pala getSijainti() {
        return sijainti;
    }

    public Suunta getSuunta() {
        return suunta;
    }
    
    @Override
    public void liiku(Suunta suunta) {
        sijainti.liiku(suunta);
    }

    public void liiku() {
        liiku(this.suunta);
    }
    
    @Override
    public int getKoko() {
        return 1;
    }

}
