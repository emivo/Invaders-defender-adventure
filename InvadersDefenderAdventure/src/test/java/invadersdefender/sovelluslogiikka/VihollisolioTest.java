/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invadersdefender.sovelluslogiikka;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Emil
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
        vihollinen = new Vihollisolio(1, 1, 1);
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
        Pala oikeaSijainti = new Pala(ammus.getX(),ammus.getY());
        oikeaSijainti.liiku(Suunta.ALAS);
        ammus.liiku();
        assertEquals(oikeaSijainti,ammus.getSijainti());
    }
}
