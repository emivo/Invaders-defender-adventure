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
public class OmaAlusTest {

    OmaAlus alus;

    public OmaAlusTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        alus = new OmaAlus(1, 1, 3);
    }

    @After
    public void tearDown() {
        alus = null;
    }

    @Test
    public void alusAmpuu() {
        Ammus ammus = alus.ammu();
        assertTrue("Alus ei ammu ammuksia", (ammus != null));
        if (ammus != null) {
            Ammus oletettu = new Ammus(alus.getX() + alus.getKoko() / 2, alus.getY() - 1, Suunta.YLOS);
            assertEquals(oletettu, ammus);
        }
    }

    @Test
    public void aseistuksenParannusSekaAmpuuEriTavalla() {
        assertEquals(Aseistus.NORMAALI, alus.getAseistus());
        
        alus.parannaAseistusta(Aseistus.TUPLA);
        assertEquals(Aseistus.TUPLA, alus.getAseistus());
        Ammus ammus = alus.ammu();

        Ammus oletettu = new Ammus(alus.getX(), alus.getY() - 1, Suunta.YLOS);
        assertEquals(oletettu, ammus);

        List<Ammus> lisaAmmukset = alus.ammuEnemman();
        ammus = lisaAmmukset.get(0);
        oletettu = new Ammus(alus.getX() + alus.getKoko() - 1, alus.getY() - 1, Suunta.YLOS);
        assertEquals(ammus, oletettu);

        alus.parannaAseistusta(Aseistus.TRIPLA);
        assertEquals(Aseistus.TRIPLA, alus.getAseistus());
        alusAmpuu();

        lisaAmmukset = alus.ammuEnemman();

        int i = 1;
        for (Ammus panos : lisaAmmukset) {
            oletettu = new Ammus(alus.getX() + alus.getKoko() - i, alus.getY() - 1, Suunta.YLOS);
            assertEquals(panos, oletettu);
            i += alus.getKoko() - 1;
        }
    }

    @Test
    public void korjaaAlustaLisaaElamapisteita() {
        int elamapisteetEnsin = alus.getElamapisteet();
        alus.korjaaSuojausta(1);
        assertEquals(elamapisteetEnsin + 1, alus.getElamapisteet());
    }

}
