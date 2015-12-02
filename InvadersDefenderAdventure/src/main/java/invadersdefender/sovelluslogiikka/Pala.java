package invadersdefender.sovelluslogiikka;
/**
 * Luokkalla voidaan luoda yksinkertainen kaksiulotteinen pala jota voidaan liikutella liiku metodin avulla
 * @author emivo
 */
public class Pala implements Liikkuva {
    
    private int x;
    private int y;
    private int koko;

    public Pala(int x, int y) {
        this(x,y,1);
    }
    
    public Pala(int x, int y, int koko) {
        this.x = x;
        this.y = y;
        this.koko = koko;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
    
    /**
     * Olion koordinaatit muuttuvat sen mukaan mikä {@link Suunta} parametrinä annetaan
     * @param suunta Suunta, johon halutaan palaa liikuttaa
     */
    @Override
    public void liiku(Suunta suunta) {
        
        switch(suunta) {
            case OIKEA: x++;
                break;
            case VASEN: x--;
                break;
            case ALAS: y++;
                break;
            case YLOS: y--;
                break;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pala other = (Pala) obj;
        
        return ((this.y == other.y) && (this.x == other.x));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + this.x;
        hash = 43 * hash + this.y;
        return hash;
    }

    @Override
    public int getKoko() {
        return koko;
    }
}
