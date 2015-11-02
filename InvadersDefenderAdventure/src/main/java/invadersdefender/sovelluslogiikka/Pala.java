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
    

}
