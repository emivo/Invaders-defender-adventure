/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class PelaajaTest {

    public PelaajaTest() {
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
    public void testataanGetterit() {
        Pelaaja pekkaTestipelaaja = new Pelaaja(10, "Pekka Testipelaaja");
        assertEquals("Pekka Testipelaaja", pekkaTestipelaaja.getNimi());
        assertEquals(10, pekkaTestipelaaja.getTulos());
    }

    @Test
    public void vertailuToimiiOikein() {
        Pelaaja pekkaTestipelaaja = new Pelaaja(10, "Pekka Testipelaaja");
        Pelaaja parempiTestipelaaja = new Pelaaja(50, "Pekka Parempipelaaja");

        assertTrue(pekkaTestipelaaja.compareTo(parempiTestipelaaja) > 0);

        Pelaaja huonompi = new Pelaaja(9, "Huonompi pelaaja");
        assertTrue(pekkaTestipelaaja.compareTo(huonompi) < 0);

        parempiTestipelaaja = new Pelaaja(11, "Pekka Parempipelaaja");
        assertTrue(pekkaTestipelaaja.compareTo(parempiTestipelaaja) > 0);

        Pelaaja samaPelaaja = new Pelaaja(10, "Pekka Testipelaaja");

        assertTrue(pekkaTestipelaaja.compareTo(samaPelaaja) == 0);
        assertTrue(pekkaTestipelaaja.equals(samaPelaaja));
    }

    @Test
    public void merkkijonotesti() {
        Pelaaja merkkijonoksiPelaaja = new Pelaaja(1, "Nimi");
        assertEquals("Nimi  1", merkkijonoksiPelaaja.toString());
    }

    @Test
    public void hashJaEqualsTestit() {
        Pelaaja testipelaaja = new Pelaaja(20, "Testi");
        Pelaaja testipelaaja2 = new Pelaaja(10, "Testi");
        assertFalse(testipelaaja.equals(null));
        assertFalse(testipelaaja.equals(new Object()));
        assertFalse(testipelaaja.equals(testipelaaja2));

        assertEquals((3 * 17 + 20) * 17 + "Testi".hashCode(), testipelaaja.hashCode());
    }
}
