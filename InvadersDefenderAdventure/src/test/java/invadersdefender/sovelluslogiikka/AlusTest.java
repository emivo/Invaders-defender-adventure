package invadersdefender.sovelluslogiikka;

import java.util.List;
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
public class AlusTest {

    Alus alus;

    public AlusTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        alus = new Alus(1, 1);
    }

    @After
    public void tearDown() {
        alus = null;
    }
    
    private boolean alusLiikkuuSuuntaan(Suunta suunta) {
        alus.setSuunta(suunta);
        alus.liiku();
        Pala oikeaSijantipalalle;
        if (suunta == Suunta.OIKEA) {
            oikeaSijantipalalle = new Pala(2,1);
        } else if (suunta == Suunta.VASEN) {
            oikeaSijantipalalle = new Pala(0,1);
        } else if (suunta == Suunta.ALAS) {
            oikeaSijantipalalle = new Pala(1,2);
        } else {
            oikeaSijantipalalle = new Pala(1, 0);
        }
        List<Pala> palat = alus.sijainti();
        return palat.get(0).equals(oikeaSijantipalalle);
    }
    
    // Testit
    @Test
    public void alusPysyyPaikkoillaan() {
        alus.liiku();
        assertEquals("Alus liikkuu, vaikkei pitäisi", new Pala(1,1), alus.sijainti().get(0));
    }
    @Test
    public void alusLiikkuuOikealle() {
        assertTrue("Alus ei liiku oikealle", alusLiikkuuSuuntaan(Suunta.OIKEA));
    }

    @Test
    public void alusLiikkuuVasemalle() {
        assertTrue("Alus ei liiku vasemmalle", alusLiikkuuSuuntaan(Suunta.VASEN));
    }

    @Test
    public void alusLiikkuuYlos() {
        assertTrue("Alus ei liiku ylös", alusLiikkuuSuuntaan(Suunta.YLOS));
    }

    @Test
    public void alusLiikkuuAlas() {
        assertTrue("Alus ei liiku alas", alusLiikkuuSuuntaan(Suunta.ALAS));
    }
    
    @Test
    public void alusAmpuu() {
        Ammus ammus = alus.ammu();
        assertTrue("Alus ei ammu ammuksia",(ammus != null));
    }
}
