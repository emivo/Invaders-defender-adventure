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
public class PelikenttaTest {

    Pelikentta pelikentta;

    public PelikenttaTest() {
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
        Peli p = new Peli(koko);
        pelikentta = p.getPelikentta();
    }

    @After
    public void tearDown() {
        pelikentta = null;
    }

    @Test
    public void omaAlusAmpuuAmmuksenKentalle() {
        pelikentta.alusAmmu(pelikentta.getOmaAlus());
        assertTrue("Yhtään ammusta ei ilmesty kentälle", !pelikentta.getAmmukset().isEmpty());
    }

    @Test
    public void ammuksetLiikkuvatKentalla() {
        pelikentta.alusAmmu(pelikentta.getOmaAlus());
        try {
            Ammus ammus = pelikentta.getAmmukset().get(0);
            int x = ammus.getSijainti().getX();
            int y = ammus.getSijainti().getY();
            Pala alkusijaintiAmmuksella = new Pala(x, y);
            pelikentta.ammuksetLiiku();
            boolean testinTulos = !alkusijaintiAmmuksella.equals(pelikentta.getAmmukset().get(0).getSijainti());
            assertTrue("Ammukset pysyvät paikoillaan, vaikka niiden pitäisi liikkua", testinTulos);
        } catch (Exception e) {
            assertTrue("Alus ei ammu, jolloin ammuksen liikkumista ei voi testata", false);
        }
    }

    @Test
    public void omaAlusEiPoistuKentalta() {
        Alus omaAlus = pelikentta.getOmaAlus();

        int matkaPoisAlhaalta = pelikentta.getPelikentanKorkeus()- omaAlus.getY() + omaAlus.getKoko();
        for (int i = 0; i <= matkaPoisAlhaalta; i++) {
            pelikentta.omaAlusLiiku(Suunta.ALAS);
        }
        assertTrue("Oma alus poistuu pelikentän alareunan yli", omaAlus.getY() + omaAlus.getKoko() <= pelikentta.getPelikentanKorkeus());
        int matkaYlhaaltaPois = omaAlus.getY();
        for (int i = 0; i <= matkaYlhaaltaPois; i++) {
            pelikentta.omaAlusLiiku(Suunta.YLOS);
        }
        assertTrue("Oma alus poistuu pelikentän yläreunan yli", omaAlus.getY() >= 0);
        int matkaVasemmaltaPois = omaAlus.getX();
        for (int i = 0; i <= matkaVasemmaltaPois; i++) {
            pelikentta.omaAlusLiiku(Suunta.VASEN);
        }
        assertTrue("Oma alus poistuu pelikentän vasemmanreunan yli", omaAlus.getX() >= 0);
        int matkaOikealtaUlos = pelikentta.getPelikentanLeveys()- omaAlus.getX() + omaAlus.getKoko();
        for (int i = 0; i < matkaOikealtaUlos; i++) {
            pelikentta.omaAlusLiiku(Suunta.OIKEA);
        }
        assertTrue("Oma alus poistuu pelikentän oikeanreunan yli", omaAlus.getX() <= pelikentta.getPelikentanLeveys());
    }

    @Test
    public void vihollisolioitaSaadaanKentalle() {
        pelikentta.vihollisetTulevatEsille();
        assertTrue("Yhtään vihollista ei tule peliin", !pelikentta.getViholliset().isEmpty());
    }

    @Test
    public void vihollisetAmpuvat() {
        pelikentta.getViholliset().add(new Vihollisolio(1, 1, 3));
        try {
            pelikentta.jokuVihollinenAmpuu();
            assertTrue("Ammuksia ei tule peliin, kun vihollisolio ampuu", !pelikentta.getAmmukset().isEmpty());
        } catch (Exception e) {
            assertTrue("Vihollisia ei tule peliin", false);
        }
    }

    @Test
    public void omaAlusTuhoutuuVihollisenAmmuksestaMutteiOmasta() {
        Ammus omaPanos = new Ammus(pelikentta.getOmaAlus().getX(), pelikentta.getOmaAlus().getY(), Suunta.YLOS);
        assertTrue("Oma alus tuhoutuu omasta panoksesta", !pelikentta.osuukoAmmus(omaPanos));
        Ammus vihollisenAmmus = new Ammus(pelikentta.getOmaAlus().getX(), pelikentta.getOmaAlus().getY(), Suunta.ALAS);
        assertTrue("Oma alus ei tuhoudu vihollisen ammuksesta", pelikentta.osuukoAmmus(vihollisenAmmus));
    }

    @Test
    public void vihollisetTuhoutuvatAmmuksistaJaPisteetLisaantyy() {
        pelikentta.vihollisetTulevatEsille();
        Ammus ammus = new Ammus(pelikentta.getViholliset().get(0).getX(), pelikentta.getViholliset().get(0).getY(), Suunta.YLOS);

        assertTrue("Ammus ei osu viholliseen vaikka pitäisi", pelikentta.osuukoAmmus(ammus));
        assertEquals("Pisteet eivät kasva vaikka tuhoaa vihollisen", 10, pelikentta.getPeli().getPisteet());
    }

    @Test
    public void omaAlusLiikku() {
        liikutaAlustaPelissa(Suunta.OIKEA);
        liikutaAlustaPelissa(Suunta.YLOS);
        liikutaAlustaPelissa(Suunta.ALAS);
        liikutaAlustaPelissa(Suunta.VASEN);
    }

    private void liikutaAlustaPelissa(Suunta suunta) {
        Pala oikeaSijainti = new Pala(pelikentta.getOmaAlus().getX(), pelikentta.getOmaAlus().getY());
        oikeaSijainti.liiku(suunta);
        pelikentta.omaAlusLiiku(suunta);
        assertEquals("alus ei liiku oikeaan suuntaan tai ei liiku ollenkaan", oikeaSijainti, pelikentta.getOmaAlus().sijainti());
    }

    @Test
    public void omaAlusLiikkuuVihollistaPain() {
        Vihollisolio olio = new Vihollisolio(pelikentta.getOmaAlus().getX(), pelikentta.getOmaAlus().getY() + pelikentta.getOmaAlus().getKoko(), pelikentta.getOmaAlus().getKoko());
        pelikentta.getViholliset().add(olio);
        pelikentta.omaAlusLiiku(Suunta.YLOS);
        assertTrue("Pelin tulisi loppua kun törmää vihollisalukseen", !pelikentta.getPeli().isRunning());
    }

    @Test
    public void aluksetTuhoutuuAmmuksistaKunAmmuksetLiikkuu() {
        // Testin onnistuminen edellytää kaikkien edellisten testien toimivuutta
        try {
            int x = 1;
            int y = 1;
            pelikentta.getViholliset().add(new Vihollisolio(x, y, 3));
            int vihollisiaKpl = pelikentta.getViholliset().size();
            // ammukset generoidaan alusten päälle
            pelikentta.getAmmukset().add(new Ammus(x, y + 1, Suunta.YLOS));
            pelikentta.ammuksetLiiku();
            assertEquals("Vihollisalukset eivät tuhoudu", vihollisiaKpl - 1, pelikentta.getViholliset().size());
            int omaalusX = pelikentta.getOmaAlus().getX();
            int omaalusY = pelikentta.getOmaAlus().getY();
            pelikentta.getAmmukset().add(new Ammus(omaalusX, omaalusY - 1, Suunta.ALAS));
            pelikentta.ammuksetLiiku();
            assertTrue("Omaan alukseen osuminen ei lopeta pelia", !pelikentta.getPeli().isRunning());
        } catch (Exception e) {
            assertTrue("Kaikkien muiden testien on onnistuttava, että tätä testiä voidaan testata", false);
        }
    }

    @Test
    public void vihollisetLiikkuu() {
        pelikentta.vihollisetTulevatEsille();
        for (int i = 0; i < pelikentta.getViholliset().size(); i++) {
            liikkuukoViholliset(i);
        }
    }

    private void liikkuukoViholliset(int i) {
        Pala vanhaSijaintiViholliselle = new Pala(pelikentta.getViholliset().get(i).getX(), pelikentta.getViholliset().get(i).getY());
        pelikentta.vihollisetLiiku();
        assertTrue(!pelikentta.getViholliset().get(i).sijainti().equals(vanhaSijaintiViholliselle));
    }

}
