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
        alus = new OmaAlus(1, 1, 3);
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
    public void osuuLiikkuvaanToimii() {
        int x = alus.getX();
        int y = alus.getY();
        int koko = alus.getKoko();
        Ammus ammus;
        for (int i = 0; i < koko; i++) {
            for (int j = 0; j < koko; j++) {
                ammus = new Ammus(x + i, y + j, Suunta.ALAS);
                assertTrue("Ammus osuu alukseen", alus.osuukoAlukseen(ammus));
            }
        }
        
        Alus toinenAlus = new OmaAlus(alus.getX(), alus.getY(), alus.getKoko());
        // samassa kohdassa
        assertTrue(alus.osuukoAlukseen(toinenAlus));

        // Kulmat kohtaa
        int koordinaattiMuutos = (alus.getKoko() - 1);
        
        osuukoToiseenAlukseen(koordinaattiMuutos, -1 * koordinaattiMuutos);
        osuukoToiseenAlukseen(-1 * koordinaattiMuutos, koordinaattiMuutos);
        osuukoToiseenAlukseen(-1 * koordinaattiMuutos, -1 * koordinaattiMuutos);
        osuukoToiseenAlukseen(koordinaattiMuutos, koordinaattiMuutos);
        
        int aluksenKoko = alus.getKoko();
        aluksenVasenJaOikeaEiOsu(-1,aluksenKoko);
        
        eiOsuToiseenAluksee(0, -1 * aluksenKoko);
        eiOsuToiseenAluksee(0, aluksenKoko);
        
        aluksenVasenJaOikeaEiOsu(1,aluksenKoko);
    }

    private void aluksenVasenJaOikeaEiOsu(int x, int aluksenKoko) {
        eiOsuToiseenAluksee(x * aluksenKoko, -1 * aluksenKoko);
        eiOsuToiseenAluksee(x * aluksenKoko, 0);
        eiOsuToiseenAluksee(x * aluksenKoko, aluksenKoko);
    }
    
    private void osuukoToiseenAlukseen(int x, int y) {
        Alus toinenAlus;
        toinenAlus = new OmaAlus(alus.getX() + x, alus.getY() + y, alus.getKoko());
        assertTrue(alus.osuukoAlukseen(toinenAlus));
    }
    
    private void eiOsuToiseenAluksee(int x, int y) {
        Alus toinenAlus;
        toinenAlus = new OmaAlus(alus.getX() + x, alus.getY() + y, alus.getKoko());
        assertFalse(alus.osuukoAlukseen(toinenAlus));
    }
}
