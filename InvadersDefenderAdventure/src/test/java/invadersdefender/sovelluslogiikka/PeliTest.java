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
public class PeliTest {

    Peli peli;

    public PeliTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        int koko = 20;
        peli = new Peli(koko);
    }

    @After
    public void tearDown() {
        peli = null;
    }

    @Test
    public void pausePysayttaaPelinJaKaynnistaaPelin() {
        assertEquals("Pelin pitäisi käynnistyä heti pelin aloitettua", Pelitilanne.KAYNNISSA, peli.getTilanne());
        peli.pause();
        assertEquals("Kun peli pysäytettään, pelin tulisi pysähtyä", Pelitilanne.PAUSE, peli.getTilanne());
        assertTrue("Pelin tulisi olla pysäytetty", !peli.isRunning());
        peli.pause();
        assertEquals("Kun pause painetaan uudelleen pelin tulisi jatkua", Pelitilanne.KAYNNISSA, peli.getTilanne());
        assertTrue("Pelin tulisi olla käynnissä", peli.isRunning());
    }

    @Test
    public void pelinUudelleenKaynnistysKaynnistaaPelin() {

        peli.kaynnistaPeliUuudelleen();

        assertEquals("Kun peli käynnistetään uudelleen, pelin tulisi olla käynnissä", Pelitilanne.KAYNNISSA, peli.getTilanne());
        assertTrue("pisteet tulisi nollaantua", peli.getPisteet() == 0);
    }
}
