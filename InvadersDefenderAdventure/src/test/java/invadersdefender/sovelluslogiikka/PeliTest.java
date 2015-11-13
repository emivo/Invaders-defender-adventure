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
    public void omaAlusAmpuuAmmuksenKentalle() {
        peli.alusAmmu(peli.getOmaAlus());
        assertTrue("Yhtään ammusta ei ilmesty kentälle", !peli.getAmmukset().isEmpty());
    }

    @Test
    public void ammuksetLiikkuvatKentalla() {
        peli.alusAmmu(peli.getOmaAlus());
        try {
            Ammus ammus = peli.getAmmukset().get(0);
            int x = ammus.getSijainti().getX();
            int y = ammus.getSijainti().getY();
            Pala alkusijaintiAmmuksella = new Pala(x, y);
            peli.ammuksetLiiku();
            boolean testinTulos = !alkusijaintiAmmuksella.equals(peli.getAmmukset().get(0).getSijainti());
            assertTrue("Ammukset pysyvät paikoillaan, vaikka niiden pitäisi liikkua", testinTulos);
        } catch (Exception e) {
            assertTrue("Alus ei ammu, jolloin ammuksen liikkumista ei voi testata", false);
        }
    }

    @Test
    public void omaAlusEiPoistuKentalta() {
        Alus omaAlus = peli.getOmaAlus();

        int matkaPoisAlhaalta = peli.getPelikentanKoko() - omaAlus.getY() + omaAlus.getKoko();
        for (int i = 0; i <= matkaPoisAlhaalta; i++) {
            peli.omaAlusLiiku(Suunta.ALAS);
        }
        assertTrue("Oma alus poistuu pelikentän alareunan yli", omaAlus.getY() + omaAlus.getKoko() <= peli.getPelikentanKoko());
        int matkaYlhaaltaPois = omaAlus.getY();
        for (int i = 0; i <= matkaYlhaaltaPois; i++) {
            peli.omaAlusLiiku(Suunta.YLOS);
        }
        assertTrue("Oma alus poistuu pelikentän yläreunan yli", omaAlus.getY() >= 0);
        int matkaVasemmaltaPois = omaAlus.getX();
        for (int i = 0; i <= matkaVasemmaltaPois; i++) {
            peli.omaAlusLiiku(Suunta.VASEN);
        }
        assertTrue("Oma alus poistuu pelikentän vasemmanreunan yli", omaAlus.getX() >= 0);
        int matkaOikealtaUlos = peli.getPelikentanKoko() - omaAlus.getX() + omaAlus.getKoko();
        for (int i = 0; i < matkaOikealtaUlos; i++) {
            peli.omaAlusLiiku(Suunta.OIKEA);
        }
        assertTrue("Oma alus poistuu pelikentän oikeanreunan yli", omaAlus.getX() <= peli.getPelikentanKoko());
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
    public void omaAlusTuhoutuuVihollisenAmmuksestaMutteiOmasta() {
        Ammus omaPanos = new Ammus(peli.getOmaAlus().getX(), peli.getOmaAlus().getY(), Suunta.YLOS);
        assertTrue("Oma alus tuhoutuu omasta panoksesta", !peli.osuukoAmmus(omaPanos));
        Ammus vihollisenAmmus = new Ammus(peli.getOmaAlus().getX(), peli.getOmaAlus().getY(), Suunta.ALAS);
        assertTrue("Oma alus ei tuhoudu vihollisen ammuksesta", peli.osuukoAmmus(vihollisenAmmus));
    }

    @Test
    public void vihollisetTuhoutuvatAmmuksistaJaPisteetLisaantyy() {
        peli.vihollisetTulevatEsille();
        Ammus ammus = new Ammus(peli.getViholliset().get(0).getX(), peli.getViholliset().get(0).getY(), Suunta.YLOS);

        assertTrue("Ammus ei osu viholliseen vaikka pitäisi", peli.osuukoAmmus(ammus));
        assertEquals("Pisteet eivät kasva vaikka tuhoaa vihollisen", 10, peli.getPisteet());
    }

    @Test
    public void omaAlusLiikku() {
        liikutaAlustaPelissa(Suunta.OIKEA);
        liikutaAlustaPelissa(Suunta.YLOS);
        liikutaAlustaPelissa(Suunta.ALAS);
        liikutaAlustaPelissa(Suunta.VASEN);
    }

    private void liikutaAlustaPelissa(Suunta suunta) {
        Pala oikeaSijainti = new Pala(peli.getOmaAlus().getX(), peli.getOmaAlus().getY());
        oikeaSijainti.liiku(suunta);
        peli.omaAlusLiiku(suunta);
        assertEquals("alus ei liiku oikeaan suuntaan tai ei liiku ollenkaan", oikeaSijainti, peli.getOmaAlus().sijainti());
    }

    @Test
    public void omaAlusLiikkuuVihollistaPain() {
        Vihollisolio olio = new Vihollisolio(peli.getOmaAlus().getX(), peli.getOmaAlus().getY() + peli.getOmaAlus().getKoko(), peli.getOmaAlus().getKoko());
        peli.getViholliset().add(olio);
        peli.omaAlusLiiku(Suunta.YLOS);
        assertTrue("Pelin tulisi loppua kun törmää vihollisalukseen", !peli.isRunning());
    }

    @Test
    public void vihollisetLiikkuu() {
        peli.vihollisetTulevatEsille();
        for (int i = 0; i < peli.getViholliset().size(); i++) {
            liikkuukoViholliset(i);
        }
    }
    
    private void liikkuukoViholliset(int i) {
        Pala vanhaSijaintiViholliselle = new Pala(peli.getViholliset().get(i).getX(), peli.getViholliset().get(i).getY());
        peli.vihollisetLiiku();
        assertTrue(!peli.getViholliset().get(i).sijainti().equals(vanhaSijaintiViholliselle));
    }

    @Test
    public void aluksetTuhoutuuAmmuksistaKunAmmuksetLiikkuu() {
        // Testin onnistuminen edellytää kaikkien edellisten testien toimivuutta
        try {
            peli.vihollisetTulevatEsille();
            int vihollisiaKpl = peli.getViholliset().size();
            // ammukset generoidaan alusten päälle
            int vihollisalusX = peli.getViholliset().get(0).getX();
            int vihollisalusY = peli.getViholliset().get(0).getY();
            peli.getAmmukset().add(new Ammus(vihollisalusX, vihollisalusY + 1, Suunta.YLOS));
            peli.ammuksetLiiku();
            assertEquals("Vihollisalukset eivät tuhoudu", vihollisiaKpl - 1, peli.getViholliset().size());
            int omaalusX = peli.getOmaAlus().getX();
            int omaalusY = peli.getOmaAlus().getY();
            peli.getAmmukset().add(new Ammus(omaalusX, omaalusY - 1, Suunta.ALAS));
            peli.ammuksetLiiku();
            assertTrue("Omaan alukseen osuminen ei lopeta pelia", !peli.isRunning());
        } catch (Exception e) {
            assertTrue("Kaikkien muiden testien on onnistuttava, että tätä testiä voidaan testata", false);
        }
    }

    @Test
    public void pausePysayttaaPelinJaKaynnistaaPelin() {
        assertTrue("Pelin pitäisi käynnistyä heti pelin aloitettua", !peli.isPause());
        peli.pause();
        assertTrue("Kun peli pysäytettään, pelin tulisi pysähtyä", peli.isPause());
        peli.pause();
        assertTrue("Kun pause painetaan uudelleen pelin tulisi jatkua", !peli.isPause() && peli.isRunning());
    }

    @Test
    public void pelinUudelleenKaynnistysKaynnistaaPelin() {

        peli.kaynnistaPeliUuudelleen();

        assertTrue("Kun peli käynnistetään uudelleen, pelin tulisi olla käynnissä", !peli.isPause() && peli.isRunning());
        assertTrue("pisteet tulisi nollaantua", peli.getPisteet() == 0);
    }
}
