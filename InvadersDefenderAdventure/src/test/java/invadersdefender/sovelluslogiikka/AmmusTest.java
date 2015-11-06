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
public class AmmusTest {
    
    Ammus ammus;
    
    public AmmusTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
        ammus = null;
    }
    
    private boolean ammusLiikkuu(Suunta suunta) {
        ammus = new Ammus(1,1,suunta);
        ammus.liiku();
        Pala oikeaPala;
        if (suunta == Suunta.YLOS) {
            oikeaPala = new Pala(1,0);
        } else {
            oikeaPala = new Pala(1,2);
        }
        return oikeaPala.equals(ammus.getSijainti());
    }

    @Test
    public void ammusLiikkuuYlos() {
        assertTrue("Ammus ei liiku oikein", ammusLiikkuu(Suunta.YLOS));
    }
    
    @Test
    public void ammusLiikkuuAlas() {
        assertTrue("Ammus ei liiku oikein", ammusLiikkuu(Suunta.ALAS));
    }
}
