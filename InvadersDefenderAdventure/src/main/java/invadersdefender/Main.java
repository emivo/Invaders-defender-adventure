package invadersdefender;

import invadersdefender.graafinenkayttoliittyma.Kayttoliittyma;
import invadersdefender.sovelluslogiikka.Peli;
import javax.swing.SwingUtilities;

/**
 * Main luokka, joka käynnistää pelin ja käyttöliittymän ja niihin liittyvät
 * luokat.
 *
 * @author emivo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        kaynnistaPeli();
    }

    private static void kaynnistaPeli() {
        int palojenKoko = 10;
        Peli peli = luoPeli();
        Kayttoliittyma kaytto = new Kayttoliittyma(peli, palojenKoko);
        SwingUtilities.invokeLater(kaytto);

        peli.setPiirtoalusta(kaytto.getPiirtoalusta());
    }

    private static Peli luoPeli() {
        int pelikentanKoko = 20;
        Peli peli = new Peli(pelikentanKoko);
        return peli;
    }

}
