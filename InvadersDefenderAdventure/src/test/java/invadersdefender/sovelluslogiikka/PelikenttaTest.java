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
        p.setTEST();
        pelikentta = p.getPelikentta();
    }

    @After
    public void tearDown() {
        pelikentta = null;
    }

    @Test
    public void omaAlusAmpuuAmmuksenKentalle() {
        omaAlusAmmu();
        assertTrue("Yhtään ammusta ei ilmesty kentälle", !pelikentta.getAmmukset().isEmpty());

        // tupla aseistus ampuu kaksi
        pelikentta.getOmaAlus().parannaAseistusta(Aseistus.TUPLA);
        int ammuksia = pelikentta.getAmmukset().size();
        omaAlusAmmu();
        assertEquals(ammuksia + 2, pelikentta.getAmmukset().size());

        // tripla ampuu kolme
        pelikentta.getOmaAlus().parannaAseistusta(Aseistus.TRIPLA);
        ammuksia = pelikentta.getAmmukset().size();
        omaAlusAmmu();
        assertEquals(ammuksia + 3, pelikentta.getAmmukset().size());
    }
    
    @Test
    public void omaAlusEiAmmuLiikaa() {
        omaAlusYritaAmpua10Kertaa();
        assertEquals(7, pelikentta.getAmmukset().size());
        
        pelikentta.kaynnistaUudelleen();
        
        pelikentta.getOmaAlus().parannaAseistusta(Aseistus.TUPLA);
        omaAlusYritaAmpua10Kertaa();
        assertEquals(16, pelikentta.getAmmukset().size());
        
        pelikentta.kaynnistaUudelleen();
        
        pelikentta.getOmaAlus().parannaAseistusta(Aseistus.TRIPLA);
        omaAlusYritaAmpua10Kertaa();
        assertEquals(27, pelikentta.getAmmukset().size());
        
    }

    private void omaAlusYritaAmpua10Kertaa() {
        for (int i = 0; i < 10; i++) {
            omaAlusAmmu();
        }
    }

    private void omaAlusAmmu() {
        pelikentta.alusAmmu(pelikentta.getOmaAlus());
    }
    
    @Test
    public void ammuksetLiikkuvatKentalla() {
        omaAlusAmmu();
        try {
            Ammus ammus = pelikentta.getAmmukset().get(0);
            int x = ammus.getX();
            int y = ammus.getY();
            Ammus alkusijaintiAmmuksella = new Ammus(x, y, Suunta.YLOS);
            pelikentta.ammuksetLiiku();
            boolean testinTulos = !alkusijaintiAmmuksella.equals(pelikentta.getAmmukset().get(0));
            assertTrue("Ammukset pysyvät paikoillaan, vaikka niiden pitäisi liikkua", testinTulos);
        } catch (Exception e) {
            assertTrue("Alus ei ammu, jolloin ammuksen liikkumista ei voi testata", false);
        }
    }

    @Test
    public void omaAlusEiPoistuKentalta() {
        Alus omaAlus = pelikentta.getOmaAlus();

        int matkaPoisAlhaalta = pelikentta.getPelikentanKorkeus() - omaAlus.getY() + omaAlus.getKoko();
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
        int matkaOikealtaUlos = pelikentta.getPelikentanLeveys() - omaAlus.getX() + omaAlus.getKoko();
        for (int i = 0; i < matkaOikealtaUlos; i++) {
            pelikentta.omaAlusLiiku(Suunta.OIKEA);
        }
        assertTrue("Oma alus poistuu pelikentän oikeanreunan yli", omaAlus.getX() <= pelikentta.getPelikentanLeveys());
    }

    @Test
    public void vihollisolioitaSaadaanKentalle() {
        pelikentta.vihollisetTulevatEsille(1);
        assertTrue("Yhtään vihollista ei tule peliin", !pelikentta.getViholliset().isEmpty());
    }

    @Test
    public void pomoTuleeKentalle() {
        assertTrue("Pomon tulisi olla kentällä vasta kun se kutsutaan", pelikentta.getPomo() == null);

        pelikentta.pomoVihollinenTuleeEsille();
        assertFalse(pelikentta.getPomo() == null);
    }

    @Test
    public void vihollisetAmpuvat() {
        pelikentta.getViholliset().add(new Vihollisolio(1, 1, 3, 1));
        try {
            pelikentta.jokuVihollinenAmpuu();
            assertTrue("Ammuksia ei tule peliin, kun vihollisolio ampuu", !pelikentta.getAmmukset().isEmpty());
        } catch (Exception e) {
            assertTrue("Vihollisia ei tule peliin", false);
        }
        pelikentta.kaynnistaUudelleen();
        pelikentta.pomoVihollinenTuleeEsille();
        pelikentta.jokuVihollinenAmpuu();
        assertFalse(pelikentta.getAmmukset().isEmpty());

        pelikentta.kaynnistaUudelleen();

        vihollinenKentanUlkopuolelleJaAmpuu(1, -1);

        vihollinenKentanUlkopuolelleJaAmpuu(-4, 1);

        vihollinenKentanUlkopuolelleJaAmpuu(pelikentta.getPelikentanLeveys() + 1, 1);
    }

    private void vihollinenKentanUlkopuolelleJaAmpuu(int x, int y) {
        pelikentta.getViholliset().clear();
        pelikentta.getAmmukset().clear();
        pelikentta.getViholliset().add(new Vihollisolio(x, y, 3, 1));
        pelikentta.jokuVihollinenAmpuu();
        assertTrue(pelikentta.getAmmukset().isEmpty());
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
        pelikentta.vihollisetTulevatEsille(3);
        Ammus ammus = new Ammus(pelikentta.getViholliset().get(0).getX(), pelikentta.getViholliset().get(0).getY(), Suunta.YLOS);
        pelikentta.getPeli().start();

        int pelikentanOletetutPisteet = pelikentta.getPeli().getPisteet() + 10;
        testaaAmmusLisaaPisteet(ammus, pelikentanOletetutPisteet);

        pomoLiikutetaanKentalle();
        ammus = new Ammus(pelikentta.getPomo().getX(), pelikentta.getPomo().getY(), Suunta.YLOS);
        Ammus ammus2 = new Ammus(pelikentta.getPomo().getX(), pelikentta.getPomo().getY() + 1, Suunta.YLOS);
        pelikentta.osuukoAmmus(ammus2);
        pelikentanOletetutPisteet = pelikentta.getPeli().getPisteet() + 20;
        testaaAmmusLisaaPisteet(ammus, pelikentanOletetutPisteet);
        assertTrue(pelikentta.getPomo() == null);
    }

    private void testaaAmmusLisaaPisteet(Ammus ammus, int pelikentanOletetutPisteet) {
        assertTrue("Ammus ei osu viholliseen vaikka pitäisi", pelikentta.osuukoAmmus(ammus));
        assertEquals("Pisteet eivät kasva vaikka tuhoaa vihollisen", pelikentanOletetutPisteet, pelikentta.getPeli().getPisteet());
    }

    @Test
    public void osuukoAmmukset() {
        pelikentta.getPeli().start();
        pelikentta.vihollisetTulevatEsille(3);
        pelikentta.getOmaAlus().setElamapisteet(1);
        Ammus ammus = new Ammus(pelikentta.getViholliset().get(0).getX(), pelikentta.getViholliset().get(0).getY(), Suunta.YLOS);
        pelikentta.getAmmukset().add(ammus);
        int ammuksiaAlussa = pelikentta.getAmmukset().size();
        pelikentta.omaAlusLiiku(Suunta.OIKEA);
        assertNotEquals(ammuksiaAlussa, pelikentta.getAmmukset().size());

        ammus = new Ammus(pelikentta.getOmaAlus().getX(), pelikentta.getOmaAlus().getY(), Suunta.ALAS);
        pelikentta.getAmmukset().add(ammus);
        ammuksiaAlussa = pelikentta.getAmmukset().size();
        pelikentta.vihollisetLiiku();
        assertNotEquals(ammuksiaAlussa, pelikentta.getAmmukset().size());
        assertEquals(Pelitilanne.LOPPU, pelikentta.getPeli().getTilanne());
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
        int elamapisteet = liikutaOmaAlusVihollistaPain();
        assertTrue(pelikentta.getViholliset().isEmpty());
        assertEquals(elamapisteet - 20, pelikentta.getOmaAlus().getElamapisteet());
        for (int i = 0; i < 4; i++) {
            elamapisteet -= 20;
            assertEquals(elamapisteet, liikutaOmaAlusVihollistaPain());
        }
        assertEquals(Pelitilanne.LOPPU, pelikentta.getPeli().getTilanne());
        assertFalse(pelikentta.getPeli().isRunning());
    }

    private int liikutaOmaAlusVihollistaPain() {
        Vihollisolio olio = new Vihollisolio(pelikentta.getOmaAlus().getX(), pelikentta.getOmaAlus().getY() - pelikentta.getOmaAlus().getKoko(), pelikentta.getOmaAlus().getKoko(), 1);
        pelikentta.getViholliset().add(olio);
        int elamapisteet = pelikentta.getOmaAlus().getElamapisteet();
        pelikentta.omaAlusLiiku(Suunta.YLOS);
        return elamapisteet;
    }

    @Test
    public void vihollisetLiikkuuOmaaAlustaPain() {
        pelikentta.getPeli().setTilanne(Pelitilanne.KAYNNISSA);
        int elamapisteet = liikutaVihollinenOmaaAlustaPain();
        assertTrue(pelikentta.getViholliset().isEmpty());
        assertEquals(10, pelikentta.getPeli().getPisteet());
        assertEquals(elamapisteet - 20, pelikentta.getOmaAlus().getElamapisteet());
        for (int i = 0; i < 4; i++) {
            elamapisteet -= 20;
            assertEquals(elamapisteet, liikutaVihollinenOmaaAlustaPain());
        }
        assertEquals(Pelitilanne.LOPPU, pelikentta.getPeli().getTilanne());
        assertFalse(pelikentta.getPeli().isRunning());
    }

    private int liikutaVihollinenOmaaAlustaPain() {
        Vihollisolio olio = new Vihollisolio(pelikentta.getOmaAlus().getX() - 1, pelikentta.getOmaAlus().getY(), pelikentta.getOmaAlus().getKoko(), 1);
        pelikentta.getViholliset().add(olio);
        int elamapisteet = pelikentta.getOmaAlus().getElamapisteet();
        pelikentta.vihollisetLiiku();
        return elamapisteet;
    }

    @Test
    public void omaAlusLiikkuuPomoaPain() {
        pomoLiikutetaanKentalle();
        int pomoElamapisteet = pelikentta.getPomo().getElamapisteet();
        int elamapisteet = pelikentta.getOmaAlus().getElamapisteet();
        while (pelikentta.getOmaAlus().getY() >= pelikentta.getPomo().getY() + pelikentta.getPomo().getKoko()) {
            pelikentta.omaAlusLiiku(Suunta.YLOS);
        }

        assertEquals(pomoElamapisteet - 10, pelikentta.getPomo().getElamapisteet());
        assertEquals(elamapisteet - 20, pelikentta.getOmaAlus().getElamapisteet());
    }

    private void pomoLiikutetaanKentalle() {
        pelikentta.pomoVihollinenTuleeEsille();
        while (pelikentta.getPomo().getY() < 0) {
            pelikentta.getPomo().liiku(Suunta.ALAS);
        }
    }

    @Test
    public void aluksetTuhoutuuAmmuksistaKunAmmuksetLiikkuu() {
        try {
            int x = 1;
            int y = 1;
            pelikentta.getViholliset().add(new Vihollisolio(x, y, 3, 1));
            int vihollisiaKpl = pelikentta.getViholliset().size();
            // ammukset generoidaan alusten päälle
            pelikentta.getAmmukset().add(new Ammus(x, y + 1, Suunta.YLOS));
            int ammuksiaKpl = pelikentta.getAmmukset().size();
            pelikentta.ammuksetLiiku();
            assertEquals("Vihollisalukset eivät tuhoudu", vihollisiaKpl - 1, pelikentta.getViholliset().size());
            assertTrue(ammuksiaKpl - 1 == pelikentta.getAmmukset().size());
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
        pelikentta.vihollisetTulevatEsille(5);
        for (int i = 0; i < pelikentta.getViholliset().size(); i++) {
            liikkuukoViholliset(i);
        }

        Vihollisolio vihu = pelikentta.getViholliset().get(0);
        int vihollisia = pelikentta.getViholliset().size();
        while (vihu.getY() <= pelikentta.getPelikentanKorkeus()) {
            vihu.liiku(Suunta.ALAS);
        }
        pelikentta.vihollisetLiiku();
        assertNotEquals(vihollisia, pelikentta.getViholliset().size());

        pelikentta.pomoVihollinenTuleeEsille();
        for (int i = 0; i < 10; i++) {
            testaaEttaPomoEiOlePaikoillaan();
        }

        while (pelikentta.getPomo().getY() <= pelikentta.getPelikentanKorkeus()) {
            pelikentta.getPomo().liiku(Suunta.ALAS);
        }
        pelikentta.vihollisetLiiku();
        assertTrue(pelikentta.getPomo() == null);
    }

    private void testaaEttaPomoEiOlePaikoillaan() {
        int x = pelikentta.getPomo().getX();
        int y = pelikentta.getPomo().getY();
        pelikentta.vihollisetLiiku();
        assertTrue(x != pelikentta.getPomo().getX() || y != pelikentta.getPomo().getY());
    }

    @Test
    public void ammuksetLiikkuuPoisKentalta() {
        pelikentta.getAmmukset().add(new Ammus(1, 0, Suunta.YLOS));
        pelikentta.ammuksetLiiku();
        assertTrue(pelikentta.getAmmukset().isEmpty());
        pelikentta.getAmmukset().add(new Ammus(1, pelikentta.getPelikentanKorkeus(), Suunta.ALAS));
        pelikentta.ammuksetLiiku();
        assertTrue(pelikentta.getAmmukset().isEmpty());
    }

    private void liikkuukoViholliset(int i) {
        Pala vanhaSijaintiViholliselle = new Pala(pelikentta.getViholliset().get(i).getX(), pelikentta.getViholliset().get(i).getY());
        pelikentta.vihollisetLiiku();
        assertFalse(pelikentta.getViholliset().get(i).sijainti().equals(vanhaSijaintiViholliselle));
    }

    @Test
    public void testaaPelikentanleveys() {
        int pelikentanKorkeus = 20 * 3;
        assertEquals((int) (pelikentanKorkeus * 5 / 7), pelikentta.getPelikentanLeveys());
    }

    @Test
    public void kaynnistaUudelleen() {
        pelikentta.getAmmukset().add(new Ammus(1, 1, Suunta.ALAS));
        pelikentta.getViholliset().add(new Vihollisolio(2, 1, 3, 1));
        pelikentta.kaynnistaUudelleen();
        assertTrue(pelikentta.getViholliset().isEmpty());
        assertTrue(pelikentta.getAmmukset().isEmpty());
        assertTrue(pelikentta.getPomo() == null);
    }

    @Test
    public void parannaVihollistenKestavyyttaVihollistenElamapisteetKasvaa() {
        tarkistaVihollistenElamapisteet(10);

        tarkistaPomonElamapisteet(20);

        pelikentta.kaynnistaUudelleen();

        pelikentta.parannaVihollistenKestavyytta();
        tarkistaVihollistenElamapisteet(20);

        tarkistaPomonElamapisteet(30);

        pelikentta.kaynnistaUudelleen();

        for (int i = 0; i < 15; i++) {
            pelikentta.parannaVihollistenKestavyytta();
        }
        tarkistaVihollistenElamapisteet(100);
        tarkistaPomonElamapisteet(110);

        pelikentta.kaynnistaUudelleen();

        tarkistaVihollistenElamapisteet(10);
        tarkistaPomonElamapisteet(20);
    }

    private void tarkistaPomonElamapisteet(int oletetutElamapisteet) {
        pelikentta.pomoVihollinenTuleeEsille();
        assertEquals(oletetutElamapisteet, pelikentta.getPomo().getElamapisteet());
    }

    private void tarkistaVihollistenElamapisteet(int oletetutElamapisteet) {
        pelikentta.vihollisetTulevatEsille(1);
        assertEquals(oletetutElamapisteet, pelikentta.getViholliset().get(0).getElamapisteet());
    }

}
