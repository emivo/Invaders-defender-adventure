package invadersdefender.sovelluslogiikka;

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
        alus = new Alus(0, 0);
    }

    @After
    public void tearDown() {
    }

    // testit eivät vielä valmiita
    
    @Test
    public void alusLiikkuuOikealle() {
        alus.liiku(Suunta.OIKEA);
        assertTrue("Alus ei liiku oikealle", true);
    }

    @Test
    public void alusLiikkuuVasemalle() {

    }

    @Test
    public void alusLiikkuuYlos() {

    }

    @Test
    public void alusLiikkuuAlas() {

    }

    @Test
    public void alusEiVoiLiikkuaNegatiiviselleKoordinaatistolle() {
        // 
    }
}
