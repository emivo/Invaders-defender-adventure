
package invadersdefender.sovelluslogiikka.huipputulokset;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author emivo
 */
public class HuipputuloksetTest {

    Huipputulokset tulokset;

    public HuipputuloksetTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        tulokset = new Huipputulokset();
    }

    @After
    public void tearDown() {
        tulokset = null;
    }

    @Test
    public void lisaaTulosToimii() {
        String nimi = "Pekka Testaaja";
        int tulos = 10;
        Pelaaja testiPelaaja = new Pelaaja(tulos, nimi);
        tulokset.lisaaTulos(nimi, tulos);
        assertTrue("Pelaaja ei lisäydy huipputuloksiin", tulokset.getTulokset().contains(testiPelaaja));
        Pelaaja testiPelaaja2 = new Pelaaja(tulos + 10, nimi);
        tulokset.lisaaTulos(nimi, tulos + 10);
        assertTrue("Pelaaja ei lisäydy huipputuloksiin", tulokset.getTulokset().contains(testiPelaaja2));
    }

    @Test
    public void huipputulostenViimeinenOnOikein() {
        String nimi = "Pekka Testaaja";
        int tulos = 10;
        Pelaaja testiPelaaja = new Pelaaja(tulos, nimi);
        tulokset.lisaaTulos(nimi, tulos);
        Pelaaja testiPelaaja2 = new Pelaaja(tulos + 10, nimi);
        tulokset.lisaaTulos(nimi, tulos + 10);
        assertTrue("getViimeinen toimii väärin", tulokset.getViimeinen().equals(testiPelaaja));
    }

    @Test
    public void tuloksetPysyvatJarjestyksessa() {
        String nimi = "Pekka Testaaja";
        int tulos = 10;
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                tulokset.lisaaTulos(nimi, tulos + i * 100);
            } else {
                tulokset.lisaaTulos(nimi, tulos + (i+1) * 200);
            }
        }
        Pelaaja edellinen = tulokset.getTulokset().get(0);
        for (Pelaaja pelaaja : tulokset.getTulokset()) {
            assertTrue(pelaaja.getTulos() <= edellinen.getTulos());
            edellinen = pelaaja;
        }
    }
}
