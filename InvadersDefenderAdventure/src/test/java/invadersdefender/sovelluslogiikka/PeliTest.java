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
        omaAlus = new Alus(koko/2, koko-1);
        peli = new Peli(koko, omaAlus);
    }
    
    @After
    public void tearDown() {
    }

    
    
    @Test
    public void alusLoytyyAlussaKentalta() {
        
    }
    
    @Test
    public void ammuksetLiikkuvatKentalla() {
        
    }
    
    @Test
    public void vihollisolioitaSaadaanKentalle() {
        
    }
    
    @Test
    public void aluksetTuohutuuAmmuksista() {
        
    }
}
