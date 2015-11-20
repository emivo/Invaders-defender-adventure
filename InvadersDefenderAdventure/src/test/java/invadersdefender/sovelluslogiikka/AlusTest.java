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
        alus = new Alus(1, 1, 3);
    }

    @After
    public void tearDown() {
        alus = null;
    }

    private boolean alusLiikkuuSuuntaan(Suunta suunta) {
        alus.liiku(suunta);
        Pala oikeaSijantipalalle;
        if (suunta == Suunta.OIKEA) {
            oikeaSijantipalalle = new Pala(2, 1);
        } else if (suunta == Suunta.VASEN) {
            oikeaSijantipalalle = new Pala(0, 1);
        } else if (suunta == Suunta.ALAS) {
            oikeaSijantipalalle = new Pala(1, 2);
        } else {
            oikeaSijantipalalle = new Pala(1, 0);
        }
        return alus.sijainti().equals(oikeaSijantipalalle);
    }

    // Testit
    @Test
    public void alusPysyyPaikkoillaan() {
        assertEquals("Alus liikkuu, vaikkei pitäisi", new Pala(1, 1), alus.sijainti());
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
        assertTrue("Alus ei ammu ammuksia", (ammus != null));
        if (ammus != null) {
            Ammus oletettu = new Ammus(alus.getX() + alus.getKoko() / 2, alus.getY() - 1, Suunta.OIKEA);
            assertEquals(oletettu, ammus);
        }
    }

    @Test
    public void osuuLiikkuvaanToimii() {
        Ammus ammus = new Ammus(1, 1, Suunta.ALAS);
        assertTrue("Ammus osuu alukseen", alus.osuukoAlukseen(ammus));
        Alus toinenAlus = new Alus(alus.getX(), alus.getY(), alus.getKoko());
        // samassa kohdassa
        assertTrue(alus.osuukoAlukseen(toinenAlus));
        
        // Kulmat kohtaa
        int koordinaattiMuutos = (alus.getKoko() - 1);
        
        osuukoToiseenAlukseen(koordinaattiMuutos, -1*koordinaattiMuutos);
        osuukoToiseenAlukseen(-1*koordinaattiMuutos, koordinaattiMuutos);
        osuukoToiseenAlukseen(-1*koordinaattiMuutos, -1*koordinaattiMuutos);
        osuukoToiseenAlukseen(koordinaattiMuutos, koordinaattiMuutos);
    }

    private void osuukoToiseenAlukseen(int x, int y) {
        Alus toinenAlus;
        toinenAlus = new Alus(alus.getX() + x, alus.getY() + y, alus.getKoko());
        assertTrue(alus.osuukoAlukseen(toinenAlus));
    }
}
