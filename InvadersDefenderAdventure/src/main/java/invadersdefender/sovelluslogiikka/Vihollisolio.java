package invadersdefender.sovelluslogiikka;

/**
 *
 * @author Emil
 */
public class Vihollisolio extends Alus {

    private int liikkumisKuvionApuMuuttuja;
    
    public Vihollisolio(int alkuX, int alkuY, int koko) {
        super(alkuX, alkuY, koko);
        liikkumisKuvionApuMuuttuja = 0;
        setSuunta(Suunta.ALAS);
    }
    
    @Override
    public Ammus ammu() {
        return new Ammus(getX() + getKoko() / 2, getY() + getKoko() + 1, Suunta.ALAS);
    }
    
    @Override
    public void liiku() {
        super.liiku();
        // viholliset liiku aluksi siten, että oikea oikea alas vasen vasen alas
        switch(liikkumisKuvionApuMuuttuja) {
            case 0: case 1:
                setSuunta(Suunta.OIKEA);
                break;
            case 2:
                setSuunta(Suunta.ALAS);
                break;
            case 3: case 4:
                setSuunta(Suunta.VASEN);
                break;
            case 5:
                setSuunta(Suunta.ALAS);
                break;
        }
        liikkumisKuvionApuMuuttuja++;
        if (liikkumisKuvionApuMuuttuja > 5) {
            liikkumisKuvionApuMuuttuja = 0;
        }
    }
}
