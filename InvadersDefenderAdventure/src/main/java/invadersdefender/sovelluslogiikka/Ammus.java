package invadersdefender.sovelluslogiikka;
/**
 * @author emivo
 */
public class Ammus extends Pala {
    
    private Suunta suunta;

    
    // ammus on nyt yhden kokoinen?!"
    // Ammukset eivät ainakaan tässä vaiheessa saa vaihtaa suunta
    public Ammus(int x, int y, Suunta suunta) {
        super(x, y);
        this.suunta = suunta;
    }
    
    public Ammus(int x, int y) {
        super(x, y);
        this.suunta = Suunta.YLOS;
    }
    
    public void liiku() {
        super.liiku(this.suunta);
    }
    
    @Override
    public void liiku(Suunta s) {
        // ammus ei vaihda suuntaa tämä huolehtii siitä
        super.liiku(this.suunta);
    }

}
