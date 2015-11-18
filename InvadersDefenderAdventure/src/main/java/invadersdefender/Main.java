package invadersdefender;

import invadersdefender.gui.Kayttoliittyma;
import invadersdefender.sovelluslogiikka.Peli;
import javax.swing.SwingUtilities;

/**
 *
 * @author emivo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        int pelikentanKoko = 25;
        int palojenKoko = 10;
        Peli peli = new Peli(pelikentanKoko);

        Kayttoliittyma kaytto = new Kayttoliittyma(peli, palojenKoko);
        SwingUtilities.invokeLater(kaytto);

        while (kaytto.getPiirtoalusta() == null) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                System.out.println("Piirtoalusta ei ole vielä valmis");
            }
        }
        peli.setPiirtoalusta(kaytto.getPiirtoalusta());
        peli.start();
    }

}
