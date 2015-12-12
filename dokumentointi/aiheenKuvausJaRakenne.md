**Aihe:** Invaders Defender Adventure on peli, jonka ideana on omalla avaruusaluksella tuhota vastaan tulevat vihamieliset avaruusalukset sekä -oliot. Pelin kuvakulma on ylhäältä päin, jolloin pelaajana avaruusalus sijaitsee ruudun alareunassa ja viholliset tulevat esiin ruudun yläreunasta. Pisteitä keräämällä voi päivittää avaruusaluksen aseistusta sekä korjata syntyneitä vahinkoja.

**Käyttäjät:** Pelaaja

**Kaikkien käyttäjien toiminnot:**
* Pelin käynnistys
* Pelin pelaaminen
  * aluksen ohjaus
  * ampuminen
* Pelin pysäyttäminen
* Näytä huipputulokset
	* huipputulosten tyhjennys
* Päivitä aseistusta ja korjata omaan avaruusalukseen syntyneitä vahinkoja
* Pelin lopettaminen

**Rakenne**

Ohjelman päätoiminnallisuus ohjaillaan Peli-luokalla, joka perii javax.swing.Timer luokan. Peli-luokka huolehtii pelintilasta, pisteistä sekä vaikeusasteesta. Peli-luokka käynnissä ollessaan suorittaa aika ajoin metodin actionPerformed, jolloin peli antaa käskyjä pelikentälle, joka sitten liikuttaa ammuksia tai aluksia ja suorittaa niihin liittyviä tarkistuksia. Muutosten tapahtuessa peli ilmoittaa piirtoalustalle tapahtuneesta muutoksesta, jolloin se piirtää pelitilanteen uudelleen. Pelikentta luokka hallitsee kaikki ammuksiin ja aluksiin liittyvän tapahtuman kuten liikkeet, tuhoutumiset, törmäykset jne. 

Pala luokan oliot ovat yksinkertaisia olioita, joilla on sijainti x ja y sekä koko. Palat ovat neliön muotoisia, joilloin koolla tarkoitetaan sivunpituutta. Pala luokka toteuttaa Liikkuva rajapinnan, joka määrää luokalle liiku(Suunta) metodin. Ammus luokka perii palan sillä se ei eroa pala luokasta vain siten, että sillä on suunta muuttuja, joka kertoo mihin suuntaan sen tulisi liikkua. Alus luokan perivät luokat ovat jo hieman monimutkaisempia ja ne eivät vain peri pala luokkaa vaan koostuvat paloista vaikkakin vain vasemman ylänurkan pala on oliolla muistissa, sillä kaikki muut palat voidaan laskea sen perusteella tarvittaessa.

Käyttöliittymä on yksinkertainen JFrame ikkuna, johon lisätään valikkopalkki valikoineen, näppäimistönkuuntelija sekä JPanel, joka toimii pelin piirtoalustana. Ikkunaan liitetyn näppäimistökuuntelijan tai valikon avulla pelaaja voi antaa käskyjä pelille esim. liikuttaa alusta tai käynnistää peli uudelleen.
