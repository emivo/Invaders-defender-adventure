package invadersdefender.sovelluslogiikka;

/**
 *
 * @author Emil
 */
public class Vihollisolio extends Alus {

    private int liikkumisKuvionApuMuuttuja;
    private Suunta suunta;

    public Vihollisolio(int alkuX, int alkuY, int koko) {
        super(alkuX, alkuY, koko);
        liikkumisKuvionApuMuuttuja = 0;
        this.suunta = Suunta.OIKEA;
    }

    @Override
    public Ammus ammu() {
        return new Ammus(getX() + getKoko() / 2, getY() + getKoko() + 1, Suunta.ALAS);
    }

    public Suunta getSuunta() {
        return suunta;
    }
    
    public void liiku() {
        valitseSuunta();
        liiku(suunta);
        kuvioEtene();
    }

    private void kuvioEtene() {
        liikkumisKuvionApuMuuttuja++;
        if (liikkumisKuvionApuMuuttuja > 5) {
            liikkumisKuvionApuMuuttuja = 0;
        }
    }

    private void valitseSuunta() {
        // viholliset liiku aluksi siten, että oikea oikea alas vasen vasen alas
        switch (liikkumisKuvionApuMuuttuja) {
            case 0:
            case 1:
                suunta = Suunta.OIKEA;
                break;
            case 2:
                suunta = Suunta.ALAS;
                break;
            case 3:
            case 4:
                suunta = Suunta.VASEN;
                break;
            case 5:
                suunta = Suunta.ALAS;
                break;
        }
    }
}
