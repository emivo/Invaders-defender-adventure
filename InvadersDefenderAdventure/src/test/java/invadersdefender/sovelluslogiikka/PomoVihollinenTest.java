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
 * @author emivo
 */
public class PomoVihollinenTest {

    public PomoVihollinenTest() {
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
    }

    @Test
    public void testaaPomonAmpuminen() {
        PomoVihollinen pomo = new PomoVihollinen(1, 1, 6, 1);
        Ammus ammus = pomo.ammu();
        boolean xKoordinaattiOikein = ammus.getX() == pomo.getX() + pomo.getKoko() / 4 || ammus.getX() == pomo.getX() + pomo.getKoko() * 3 / 4;
        boolean yKoordinaattiOikein = ammus.getY() == pomo.getY() + pomo.getKoko() + 1;
        boolean suuntaOikein = ammus.getSuunta() == Suunta.ALAS;
        assertTrue(xKoordinaattiOikein && yKoordinaattiOikein && suuntaOikein);
    }

}
