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
public class VihollisolioTest {

    Vihollisolio vihollinen;

    public VihollisolioTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        vihollinen = new Vihollisolio(1, 1, 3, 1);
    }

    @After
    public void tearDown() {
        vihollinen = null;
    }

    @Test
    public void testaaAsetaSuunta() {
        vihollinen.setSuunta(Suunta.OIKEA);
        assertEquals(Suunta.OIKEA, vihollinen.getSuunta());
    }

    @Test
    public void vihollisetLiikkuvatAsetetunSuunnanMukaan() {
        vihollinenLiikutaSuuntaan(Suunta.OIKEA);
        vihollinenLiikutaSuuntaan(Suunta.VASEN);
        vihollinenLiikutaSuuntaan(Suunta.YLOS);
        vihollinenLiikutaSuuntaan(Suunta.ALAS);
    }

    private void vihollinenLiikutaSuuntaan(Suunta suunta) {
        Pala oikeaSijainti = new Pala(vihollinen.getX(), vihollinen.getY());
        oikeaSijainti.liiku(suunta);

        vihollinen.setSuunta(suunta);
        vihollinen.liiku();

        assertTrue(vihollinen.osuukoAlukseen(oikeaSijainti));
    }

    @Test
    public void vihollisetAmpuvatAlas() {
        Ammus ammus = vihollinen.ammu();
        assertEquals(vihollinen.getX() + vihollinen.getKoko() / 2, ammus.getX());
        assertEquals(vihollinen.getX() + vihollinen.getKoko() + 1, ammus.getY());
        assertEquals(Suunta.ALAS, ammus.getSuunta());
        Ammus oikeaSijainti = new Ammus(ammus.getX(), ammus.getY(), ammus.getSuunta());
        oikeaSijainti.liiku(Suunta.ALAS);
        ammus.liiku();
        assertEquals(oikeaSijainti, ammus);
    }
    
    @Test
    public void vihollisenElamapisteitaEiVoiMuuttaa() {
        int elamapisteetVanha = vihollinen.getElamapisteet();
        vihollinen.setElamapisteet(10);
        assertEquals(elamapisteetVanha, vihollinen.getElamapisteet());
    }
}
