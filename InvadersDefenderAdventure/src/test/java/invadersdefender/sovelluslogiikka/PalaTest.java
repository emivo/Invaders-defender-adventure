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
public class PalaTest {
    
    Pala pala;
    
    public PalaTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        pala = new Pala(1,1);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void palaLiikkuu() {
        pala.liiku(Suunta.OIKEA);
        assertTrue(pala.getX() == 2);
        pala.liiku(Suunta.VASEN);
        assertTrue(pala.getX() == 1);
        pala.liiku(Suunta.ALAS);
        assertTrue(pala.getY() == 2);
        pala.liiku(Suunta.YLOS);
        assertTrue(pala.getY() == 1);
    }
}
