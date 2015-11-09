package invadersdefender;

import invadersdefender.gui.Kayttoliittyma;
import invadersdefender.sovelluslogiikka.Alus;
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
        
        Peli peli = new Peli(15);

        Kayttoliittyma kaytto = new Kayttoliittyma(peli, 10);
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
