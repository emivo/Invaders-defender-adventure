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
    public void peliPysahtyyJaKaynnistyy() {
        assertEquals("Pelin pitäisi olla alkuruudussa, kun peli kännistetään", Pelitilanne.ALKURUUTU, peli.getTilanne());
        peli.start();
        assertEquals("Pelin tulisi käynnistyä", Pelitilanne.KAYNNISSA, peli.getTilanne());
        peli.pysaytaTaiJatkaPysahtynytta();
        assertEquals("Kun peli pysäytettään, pelin tulisi pysähtyä", Pelitilanne.PYSAYTETTY, peli.getTilanne());
        assertTrue("Pelin tulisi olla pysäytetty", !peli.isRunning());
        peli.pysaytaTaiJatkaPysahtynytta();
        assertEquals("Kun pause painetaan uudelleen pelin tulisi jatkua", Pelitilanne.KAYNNISSA, peli.getTilanne());
        assertTrue("Pelin tulisi olla käynnissä", peli.isRunning());
    }

    @Test
    public void pelinUudelleenKaynnistysKaynnistaaPelin() {
        for (int i = 0; i < 20; i++) {
            peli.lisaaPisteita();
        }
        Alus vanhaAlus = peli.getPelikentta().getOmaAlus();
        peli.kaynnistaPeliUuudelleen();

        assertEquals("Kun peli käynnistetään uudelleen, pelin tulisi olla käynnissä", Pelitilanne.KAYNNISSA, peli.getTilanne());
        assertTrue("pisteet tulisi nollaantua", peli.getPisteet() == 0);
        assertTrue(peli.getPelikentta().getViholliset().isEmpty());
        assertTrue(peli.getPelikentta().getAmmukset().isEmpty());
        assertNotEquals(vanhaAlus, peli.getPelikentta().getOmaAlus());
        assertEquals(100, peli.getDelay());
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
        assertEquals(2, peli.getTaustanLeikkauskohta());
        assertFalse(peli.getPelikentta().getViholliset().isEmpty());
    }

    @Test
    public void huipputuloksetSiirtymyminen() {
        peli.start();
        peli.asetaHuipputuloistenKatselutilaan();
        assertEquals("Peli on väärässä tilassa", Pelitilanne.TULOKSET, peli.getTilanne());
        assertFalse(peli.isRunning());
        peli.pysaytaTaiJatkaPysahtynytta();
        assertEquals("Peli on väärässä tilassa", Pelitilanne.KAYNNISSA, peli.getTilanne());
        Alus vanhaAlus = peli.getPelikentta().getOmaAlus();
        peli.peliLoppuu();
        peli.asetaHuipputuloistenKatselutilaan();
        peli.pysaytaTaiJatkaPysahtynytta();
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
        lisaaPisteitaTarvittavaMaara(500);
        peli.actionPerformed(null);

        assertTrue(peli.getPelikentta().getPomo() != null);
    }

    @Test
    public void parannaAlustaToimiiNiinKuinKuuluu() {

        aseistusEiMuutu(Aseistus.TUPLA, Aseistus.NORMAALI);
        aseistusEiMuutu(Aseistus.TRIPLA, Aseistus.NORMAALI);
        lisaaPisteitaTarvittavaMaara(1000);
        aseistusMuuttuu(Aseistus.TUPLA);
        assertEquals(0, peli.getPisteet());
        aseistusEiMuutu(Aseistus.NORMAALI, Aseistus.TUPLA);

        lisaaPisteitaTarvittavaMaara(1500);
        aseistusMuuttuu(Aseistus.TRIPLA);
        assertEquals(0, peli.getPisteet());
        aseistusEiMuutu(Aseistus.NORMAALI, Aseistus.TRIPLA);
        aseistusEiMuutu(Aseistus.TUPLA, Aseistus.TRIPLA);

    }

    @Test
    public void omaaAlustaVoiKorjataJaMutteiLiikaa() {
        peli.getPelikentta().getOmaAlus().setElamapisteet(10);
        int pisteet = peli.getPisteet();
        peli.korjaaOmaaAlusta();
        assertEquals(pisteet, peli.getPisteet());
        assertEquals(10, peli.getPelikentta().getOmaAlus().getElamapisteet());

        lisaaPisteitaTarvittavaMaara(1000);

        peli.getPelikentta().getOmaAlus().setElamapisteet(10);
        pisteet = peli.getPisteet() - 10;
        peli.korjaaOmaaAlusta();
        assertEquals(pisteet, peli.getPisteet());
        assertEquals(20, peli.getPelikentta().getOmaAlus().getElamapisteet());
        pisteet = peli.getPisteet() - 80;
        for (int i = 0; i < 10; i++) {
            peli.korjaaOmaaAlusta();
        }
        assertEquals(pisteet, peli.getPisteet());
        assertEquals(100, peli.getPelikentta().getOmaAlus().getElamapisteet());
    }

    private void lisaaPisteitaTarvittavaMaara(int tarvittavatPisteet) {
        peli.setTilanne(Pelitilanne.KAYNNISSA);
        while (peli.getPisteet() < tarvittavatPisteet) {
            peli.lisaaPisteita();
        }
    }

    private void aseistusMuuttuu(Aseistus uusiAseistus) {
        peli.parannaAluksenAseistusta(uusiAseistus);
        assertEquals(uusiAseistus, peli.getPelikentta().getOmaAlus().getAseistus());
    }

    private void aseistusEiMuutu(Aseistus uusiAseistus, Aseistus vanhaAseistus) {
        peli.parannaAluksenAseistusta(uusiAseistus);
        assertEquals(vanhaAseistus, peli.getPelikentta().getOmaAlus().getAseistus());
    }

    @Test
    public void pelillaOnAloitusviive() {
        assertEquals(100, peli.getInitialDelay());
        lisaaPisteitaTarvittavaMaara(1000);
        peli.kaynnistaPeliUuudelleen();
        assertEquals(100, peli.getInitialDelay());
        assertEquals(100, peli.getDelay());
    }
    
    @Test
    public void peliKuunteleeItseaan() {
        peli.getActionListeners()[0].equals(peli);
    }
    
    @Test
    public void huipputuloksetGetteri() {
        assertTrue(peli.getHuipputulokset() != null);
    }
}
