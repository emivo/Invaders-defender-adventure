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
    Alus omaAlus;

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
        omaAlus = new Alus(koko / 2, koko - 1);
        peli = new Peli(koko, omaAlus);
    }

    @After
    public void tearDown() {
        peli = null;
    }

    @Test
    public void omaAlusAmpuuAmmuksenKentalle() {
        peli.alusAmmu(omaAlus);
        assertTrue("Yhtään ammusta ei ilmesty kentälle", !peli.getAmmukset().isEmpty());
    }

    @Test
    public void ammuksetLiikkuvatKentalla() {
        peli.alusAmmu(omaAlus);
        try {
            Ammus ammus = peli.getAmmukset().get(0);
            int x = ammus.getSijainti().getX();
            int y = ammus.getSijainti().getY();
            Pala alkusijaintiAmmuksella = new Pala(x,y);
            peli.ammuksetLiiku();
            boolean testinTulos = !alkusijaintiAmmuksella.equals(peli.getAmmukset().get(0).getSijainti());
            assertTrue("Ammukset pysyvät paikoillaan, vaikka niiden pitäisi liikkua", testinTulos);
        } catch (Exception e) {
            assertTrue("Alus ei ammu, jolloin ammuksen liikkumista ei voi testata", false);
        }
    }

    @Test
    public void vihollisolioitaSaadaanKentalle() {
        peli.vihollisetTulevatEsille();
        assertTrue("Yhtään vihollista ei tule peliin", !peli.getViholliset().isEmpty());
    }

    @Test
    public void vihollisetAmpuvat() {
        peli.vihollisetTulevatEsille();
        try {
            peli.jokuVihollinenAmpuu();
            assertTrue("Ammuksia ei tule peliin, kun vihollisolio ampuu", !peli.getAmmukset().isEmpty());
        } catch (Exception e) {
            assertTrue("Vihollisia ei tule peliin", false);
        }
    }

    @Test
    public void aluksetTuhoutuuAmmuksista() {
        // Testin onnistuminen edellytää kaikkien edellisten testien toimivuutta
        try {
            peli.vihollisetTulevatEsille();
            int vihollisiaKpl = peli.getViholliset().size();
            // ammukset generoidaan alusten päälle
            int vihollisalusX = peli.getViholliset().get(0).sijainti().get(0).getX();
            int vihollisalusY = peli.getViholliset().get(0).sijainti().get(0).getY();
            peli.getAmmukset().add(new Ammus(vihollisalusX, vihollisalusY + 1, Suunta.YLOS));
            peli.ammuksetLiiku();
            assertEquals("Vihollisalukset eivät tuhoudu", vihollisiaKpl - 1, peli.getViholliset().size());
            int omaalusX = peli.getOmaAlus().sijainti().get(0).getX();
            int omaalusY = peli.getOmaAlus().sijainti().get(0).getY();
            peli.getAmmukset().add(new Ammus(omaalusX, omaalusY - 1, Suunta.ALAS));
            peli.ammuksetLiiku();
            assertTrue("Oma alus ei tuhoudu", peli.getOmaAlus() == null);
        } catch (Exception e) {
            assertTrue("Kaikkien muiden testien on onnistuttava, että tätä testiä voidaan testata", false);
        }
    }
}
