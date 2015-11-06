package invadersdefender.sovelluslogiikka;
/**
 * @author emivo
 */
public class Pala {
    
    private int x;
    private int y;

    public Pala(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
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
        
        return ((this.y != other.y) && (this.x != other.x));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + this.x;
        hash = 43 * hash + this.y;
        return hash;
    }
}
