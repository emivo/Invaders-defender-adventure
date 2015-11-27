/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invadersdefender.sovelluslogiikka;

import java.awt.event.ActionEvent;
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
        peli.setTEST();
    }

    @After
    public void tearDown() {
        peli = null;
    }

    @Test
    public void pausePysayttaaPelinJaKaynnistaaPelin() {
        assertEquals("Pelin pitäisi olla alkuruudussa, kun peli kännistetään", Pelitilanne.ALKURUUTU, peli.getTilanne());
        peli.start();
        assertEquals("Pelin tulisi käynnistyä", Pelitilanne.KAYNNISSA, peli.getTilanne());
        peli.pause();
        assertEquals("Kun peli pysäytettään, pelin tulisi pysähtyä", Pelitilanne.PAUSE, peli.getTilanne());
        assertTrue("Pelin tulisi olla pysäytetty", !peli.isRunning());
        peli.pause();
        assertEquals("Kun pause painetaan uudelleen pelin tulisi jatkua", Pelitilanne.KAYNNISSA, peli.getTilanne());
        assertTrue("Pelin tulisi olla käynnissä", peli.isRunning());
    }

    @Test
    public void pelinUudelleenKaynnistysKaynnistaaPelin() {
        Alus vanhaAlus = peli.getPelikentta().getOmaAlus();
        peli.kaynnistaPeliUuudelleen();

        assertEquals("Kun peli käynnistetään uudelleen, pelin tulisi olla käynnissä", Pelitilanne.KAYNNISSA, peli.getTilanne());
        assertTrue("pisteet tulisi nollaantua", peli.getPisteet() == 0);
        assertTrue(peli.getPelikentta().getViholliset().isEmpty());
        assertTrue(peli.getPelikentta().getAmmukset().isEmpty());
        assertNotEquals(vanhaAlus, peli.getPelikentta().getOmaAlus());
    }

    @Test
    public void peliLoppuuLopettaaPelin() {
        peli.start();
        peli.peliLoppuu();
        assertEquals("Peli on väärässä tilassa", Pelitilanne.LOPPU, peli.getTilanne());
        assertFalse("Timer on vielä käynnissä, kun peli on loppunut", peli.isRunning());
    }

    @Test
    public void actionKutsutToimii() {
        assertEquals(0, peli.getTaustanLeikkauskohta());
        peli.start();
        peli.getPelikentta().getAmmukset().add(new Ammus(1, 1, Suunta.ALAS));
        Ammus ammuksenSijaintitulisiolla = new Ammus(1, 2, Suunta.ALAS);
        peli.actionPerformed(new ActionEvent(this, 1, null));
        assertEquals(ammuksenSijaintitulisiolla, peli.getPelikentta().getAmmukset().get(0));
        assertEquals(1, peli.getTaustanLeikkauskohta());
        assertFalse(peli.getPelikentta().getViholliset().isEmpty());
    }

    @Test
    public void huipputuloksetSiirtymyminen() {
        peli.asetaHuipputuloistenKatselutilaan();
        assertEquals("Peli on väärässä tilassa", Pelitilanne.TULOKSET, peli.getTilanne());
        assertFalse(peli.isRunning());
        peli.pause();
        assertEquals("Peli on väärässä tilassa", Pelitilanne.KAYNNISSA, peli.getTilanne());
        Alus vanhaAlus = peli.getPelikentta().getOmaAlus();
        peli.peliLoppuu();
        peli.asetaHuipputuloistenKatselutilaan();
        peli.pause();
        assertNotEquals(vanhaAlus, peli.getPelikentta().getOmaAlus());
        assertEquals("Peli on väärässä tilassa", Pelitilanne.KAYNNISSA, peli.getTilanne());

    }

    @Test
    public void peliNopeutuuPisteidenKasvaessa() {

        peli.start();
        int alkuNopeus = peli.getDelay();
        for (int i = 0; i < 5; i++) {
            peli.lisaaPisteita();
        }
        assertTrue(alkuNopeus > peli.getDelay());
    }

    @Test
    public void pomoTuleeKunPisteteitaOn500() {
        peli.start();
        while (peli.getPisteet() < 500) {
            peli.lisaaPisteita();
        }
        peli.actionPerformed(null);
        
        assertTrue(peli.getPelikentta().getPomo() != null);
    }
}
