package org.gedcomx.conversion.gedcom.dq55;

import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.model.Person;
import org.folg.gedcom.parser.ModelParser;
import org.gedcomx.conclusion.Fact;
import org.gedcomx.types.FactType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;
import java.util.List;

import static org.testng.Assert.*;


public class PersonsFactsTest {
  Gedcom gedcom;

  @BeforeClass
  public void setUp() throws Exception {
    URL gedcomUrl = this.getClass().getClassLoader().getResource("Case006-PersonsFacts.ged");
    File gedcomFile = new File(gedcomUrl.toURI());
    ModelParser modelParser = new ModelParser();

    gedcom = modelParser.parseGedcom(gedcomFile);
    assertNotNull(gedcom);
    assertNotNull(gedcom.getPeople());
  }


  @Test
  public void testSimpleFacts() throws Exception {
    Person dqPerson = gedcom.getPeople().get(0);
    TestConversionResult result = new TestConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person gedxPerson = result.getPersons().get(0);

    boolean deathFound = false;
    boolean birthFound = false;
    for(Fact fact : gedxPerson.getFacts()) {
      if(fact.getKnownType().equals(FactType.Birth)) {
        assertEquals(fact.getDate().getOriginal(), "3 May 1875");
        assertEquals(fact.getPlace().getOriginal(), "Gent, Oost-Vlaanderen, Belgium");
        assertNull(fact.getOriginal());
        birthFound = true;
      }
      else if(fact.getKnownType().equals(FactType.Death)) {
        assertEquals(fact.getDate().getOriginal(), "18 Apr 1947");
        assertEquals(fact.getPlace().getOriginal(), "San Francisco, California, United States");
        assertNull(fact.getOriginal());
        deathFound = true;
      }
    }
    assertTrue(deathFound);
    assertTrue(birthFound);
  }

  @Test
  public void testMultipleFacts() throws Exception {
    Person dqPerson = gedcom.getPeople().get(0);
    TestConversionResult result = new TestConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person gedxPerson = result.getPersons().get(0);
    int resiCount = 0;
    boolean resi1Found = false;
    boolean resi2Found = false;
    boolean resi3Found = false;
    for(Fact fact : gedxPerson.getFacts()) {
      if(fact.getKnownType().equals(FactType.Residence)) {
        resiCount++;
        if(fact.getDate().getOriginal().equals("9 Apr 1930")) {
          resi1Found = true;
        }
        else if(fact.getDate().getOriginal().equals("1898")) {
          resi2Found = true;
        }
        else if(fact.getDate().getOriginal().equals("3 Jan 1920")) {
          resi3Found = true;
        }
      }
    }

    assertEquals(resiCount, 3);
    assertTrue(resi1Found);
    assertTrue(resi2Found);
    assertTrue(resi3Found);
  }

  @Test
  public void testDateOnly() throws Exception {
    Person dqPerson = gedcom.getPeople().get(0);
    TestConversionResult result = new TestConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person gedxPerson = result.getPersons().get(0);
    boolean factFound = false;
    for(Fact fact : gedxPerson.getFacts()) {
      if(fact.getKnownType().equals(FactType.Naturalization)) {
        factFound = true;
        assertEquals(fact.getDate().getOriginal(), "1887");
        assertNull(fact.getPlace());
        assertNull(fact.getOriginal());
      }
    }
    assertTrue(factFound);
  }

  @Test
  public void testPlaceOnly() throws Exception {
    Person dqPerson = gedcom.getPeople().get(0);
    TestConversionResult result = new TestConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person gedxPerson = result.getPersons().get(0);
    boolean factFound = false;
    for(Fact fact : gedxPerson.getFacts()) {
      if(fact.getKnownType().equals(FactType.ScholasticAchievement)) {
        factFound = true;
        assertEquals(fact.getPlace().getOriginal(), "San Francisco, San Francisco, California, United States");
        assertNull(fact.getDate());
        assertNull(fact.getOriginal());
      }
    }
    assertTrue(factFound);
  }

  @Test
  public void testValueOnly() throws Exception {
    Person dqPerson = gedcom.getPeople().get(0);
    TestConversionResult result = new TestConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person gedxPerson = result.getPersons().get(0);
    boolean factFound = false;
    for(Fact fact : gedxPerson.getFacts()) {
      if(fact.getKnownType().equals(FactType.Occupation)) {
        factFound = true;
        assertEquals(fact.getOriginal(), "Dressmaker");
        assertNull(fact.getPlace());
        assertNull(fact.getDate());
      }
    }
    assertTrue(factFound);
  }

  @Test
  public void testEmptyValues() throws Exception {
    Person dqPerson = gedcom.getPeople().get(0);
    TestConversionResult result = new TestConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person gedxPerson = result.getPersons().get(0);
    boolean factFound = false;
    for(Fact fact : gedxPerson.getFacts()) {
      if(fact.getKnownType().equals(FactType.BatMitzvah)) {
        factFound = true;
        assertNull(fact.getPlace());
        assertNull(fact.getDate());
        assertNull(fact.getOriginal());
      }
    }
    assertTrue(factFound);
  }

  @Test
  public void testBooleanFact() throws Exception {
    Person dqPerson = gedcom.getPeople().get(0);
    TestConversionResult result = new TestConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person gedxPerson = result.getPersons().get(0);
    boolean factFound = false;
    for(Fact fact : gedxPerson.getFacts()) {
      if(fact.getKnownType().equals(FactType.Stillborn)) {
        factFound = true;
        assertNull(fact.getPlace());
        assertNull(fact.getDate());
        assertNull(fact.getOriginal());
      }
    }
    assertTrue(factFound);
  }

  @Test
  public void testNonStandardTags() throws Exception {
    Person dqPerson = gedcom.getPeople().get(2);
    TestConversionResult result = new TestConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person gedxPerson = result.getPersons().get(0);
    assertEquals(gedxPerson.getFacts().size(), 39);
    checkFact(gedxPerson.getFacts(), FactType.Citizenship, null, "1853", null);
    checkFact(gedxPerson.getFacts(), FactType.Residence, "88 Brookside Drive, Toronto", null, null);
    checkFact(gedxPerson.getFacts(), FactType.Excommunication, null, "17 JAN 1850", "GEORGIA FAMILY PLANTATION");
    checkFact(gedxPerson.getFacts(), FactType.Excommunication, null, "31 DEC 1911", "Church of Chrisi Blue Bayou, disorderly conduct");
    checkFact(gedxPerson.getFacts(), FactType.Funeral, null, "1 MAR 1766", "Mexico");
    checkFact(gedxPerson.getFacts(), FactType.Funeral, null, "1 JAN 1911", "Canada");
    checkFact(gedxPerson.getFacts(), FactType.Illness, "Dysentery", "1 JUL 1853", "Oregon Trail");
    checkFact(gedxPerson.getFacts(), FactType.Illness, "Typhus", "13 JUN 1821", "England");
    checkFact(gedxPerson.getFacts(), FactType.Blessing, null, "31 May 1928", "Bakersfield, Kern, California");
    checkFact(gedxPerson.getFacts(), FactType.Blessing, null, "1 JUN 1922", "Johannesburg, South Africa");
    checkFact(gedxPerson.getFacts(), FactType.Interment, null, "3 DEC 1867", null);
    checkFact(gedxPerson.getFacts(), FactType.Living, null, null, null);
    checkFact(gedxPerson.getFacts(), FactType.MilitaryService, "Corporal In The Militia, 1707", null, null);
    checkFact(gedxPerson.getFacts(), FactType.MilitaryService, null, "from 15 JUN 1949 until 1 JUL 1971", null);
    checkFact(gedxPerson.getFacts(), FactType.MilitaryService, "Naval Lieutenant - Senior Grade", null, null);
    checkFact(gedxPerson.getFacts(), FactType.MilitaryService, "Union Soldier during Civil War", null, null);
    checkFact(gedxPerson.getFacts(), FactType.MilitaryService, null, null, "Co K 182d Regt OVI - Civil War");
    checkFact(gedxPerson.getFacts(), FactType.MilitaryService, null, "1776", "Private in the Revolutionary War");
    checkFact(gedxPerson.getFacts(), FactType.MilitaryService, null, "10 FEB 1865", "Private, Mustered Out, Harpers Ferry, VA");
    checkFact(gedxPerson.getFacts(), FactType.MilitaryService, null, "11 FEB 1865", null);
    checkFact(gedxPerson.getFacts(), FactType.MilitaryService, null, "13 FEB 1865", null);
    checkFact(gedxPerson.getFacts(), FactType.MilitaryService, null, "14 FEB 1865", null);
    checkFact(gedxPerson.getFacts(), FactType.MilitaryAward, null, "15 FEB 1865", null);
    checkFact(gedxPerson.getFacts(), FactType.MilitaryDischarge, null, "13 JUN 1866", null);
    //TODO Figure out why these are not working properly
//    checkFact(gedxPerson.getFacts(), FactType.Mission, null, "6 JUL 1930", "Southern States");
//    checkFact(gedxPerson.getFacts(), FactType.Mission, null, "7 JUL 1934", "Utah");
    checkFact(gedxPerson.getFacts(), FactType.Move, null, "1886", "Albany, Gentry Co, MO");
    checkFact(gedxPerson.getFacts(), FactType.Ordinance, "no", null, null);
    checkFact(gedxPerson.getFacts(), FactType.Ordination, null, "1886", null);
    checkFact(gedxPerson.getFacts(), FactType.Race, "German", null, null);
    checkFact(gedxPerson.getFacts(), FactType.Stillborn, "death", null, null);
    checkFact(gedxPerson.getFacts(), FactType.Stillborn, null, null, null);
    checkFact(gedxPerson.getFacts(), FactType.Residence, "Colorado", null, null);
    checkFact(gedxPerson.getFacts(), FactType.Baptism, null, "8 AUG 1770", "All Saints Church, Fulham, London");
    checkFact(gedxPerson.getFacts(), FactType.Baptism, null, "12 NOV 1797", null);
    checkFact(gedxPerson.getFacts(), FactType.Namesake, "Jacob's brother", null, null);
    checkFact(gedxPerson.getFacts(), FactType.Occupation, null, "1978", "Nashville, Tennessee");
    checkFact(gedxPerson.getFacts(), FactType.Occupation, null, "1981", "Nashville, Tennessee");
    checkFact(gedxPerson.getFacts(), FactType.ScholasticAchievement, null, "DEC 1973", "Bachelor of Arts, English");
    checkFact(gedxPerson.getFacts(), FactType.ScholasticAchievement, null, "10 MAY 1980", "Masters in Education");
    checkFact(gedxPerson.getFacts(), FactType.MilitaryService, null, "1881", null);
    //TODO Figure out why this is not working properly
//    checkFact(gedxPerson.getFacts(), FactType.SocialSecurityNumber, "528-30-8888", null, null);

    //TODO Add tests for ARVL, ARRI, ARRIVAL, DPRT, DEPA and DEPARTURE
  }

  @Test
  public void testStandardTags() throws Exception {
    Person dqPerson = gedcom.getPeople().get(3);
    TestConversionResult result = new TestConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person gedxPerson = result.getPersons().get(0);
    assertEquals(gedxPerson.getFacts().size(), 66);
    checkFact(gedxPerson.getFacts(), FactType.CasteName, null, "1850", "Newbury, Ma.");
    checkFact(gedxPerson.getFacts(), FactType.CasteName, null, null, "Brahmin");
    checkFact(gedxPerson.getFacts(), FactType.PhysicalDescription, "Age: 44", null, null);
    checkFact(gedxPerson.getFacts(), FactType.PhysicalDescription, "5'7\", 165#, medium complexion, brown eyes & hair", null, null);
    checkFact(gedxPerson.getFacts(), FactType.ScholasticAchievement, "B.A. In Sociology And Applied Criminal Justice", null, null);
    checkFact(gedxPerson.getFacts(), FactType.ScholasticAchievement, "Ryder College,", "1934-", "Trenton, NJ");
    checkFact(gedxPerson.getFacts(), FactType.NationalId, "Glendale Az.", null, null);
    checkFact(gedxPerson.getFacts(), FactType.NationalId, "Whistler, Alabama now Prichard Al", null, null);
    checkFact(gedxPerson.getFacts(), FactType.NationalOrigin, "Chinese", null, null);
    checkFact(gedxPerson.getFacts(), FactType.NationalOrigin, "Swedish", null, null);
    checkFact(gedxPerson.getFacts(), FactType.CountOfChildren, "2", null, null);
    checkFact(gedxPerson.getFacts(), FactType.CountOfChildren, "5", null, null);
    checkFact(gedxPerson.getFacts(), FactType.CountOfMarriages, null, null, "4");
    checkFact(gedxPerson.getFacts(), FactType.CountOfMarriages, "3", null, null);
    checkFact(gedxPerson.getFacts(), FactType.Occupation, "Teacher, Tabernacle Christian School", null, null);
    checkFact(gedxPerson.getFacts(), FactType.Occupation, "Kruidenier", null, null);
    checkFact(gedxPerson.getFacts(), FactType.Possessions, null, "18 FEB 1768", "Bought 100 acres in Windham county, Connecticut for 84 pounds");
    checkFact(gedxPerson.getFacts(), FactType.Possessions, "43 MAPPERLAY ROAD (THE LODGE HOUSE)", null, null);
    checkFact(gedxPerson.getFacts(), FactType.ReligiousAffiliation, "Quaker", null, null);
    checkFact(gedxPerson.getFacts(), FactType.ReligiousAffiliation, "Church of Christ", null, null);
    checkFact(gedxPerson.getFacts(), FactType.Residence, "Russia, Oh, Dayton, Oh, West Milton, Oh", null, null);
    checkFact(gedxPerson.getFacts(), FactType.Residence, null, "ABT 1740", "Brocks Gap, VA");
    checkFact(gedxPerson.getFacts(), FactType.SocialSecurityNumber, "485-70-7507", null, null);
    checkFact(gedxPerson.getFacts(), FactType.SocialSecurityNumber, null, null, "094-28-1549");
    //TODO: TITL is getting mapped into name, not a title fact, since real gedcoms are using TITL for name prefixes and suffixes, not titles of nobility
//    checkFact(gedxPerson.getFacts(), FactType.TitleOfNobility, "Jr", null, null);
//    checkFact(gedxPerson.getFacts(), FactType.TitleOfNobility, "Rev.", null, null);
    checkFact(gedxPerson.getFacts(), FactType.Adoption, null, null, "Troy, NY, USA");
    checkFact(gedxPerson.getFacts(), FactType.Adoption, null, null, "Honolulu, Oahu, HI");
    checkFact(gedxPerson.getFacts(), FactType.Baptism, null, "16 APR 1923", "St Nathanial PE Church, Phila");
    checkFact(gedxPerson.getFacts(), FactType.Baptism, null, "20 JUN 1919", "Chicago, IL");
    checkFact(gedxPerson.getFacts(), FactType.BarMitzvah, null, "1864", "KRAKOW, AUSTRIA");
    checkFact(gedxPerson.getFacts(), FactType.BarMitzvah, null, "4 SEP 1983", "Temple Israel, Memphis, Tennessee, United States of America");
    checkFact(gedxPerson.getFacts(), FactType.BatMitzvah, null, "1964", "BETH ABRAHAM, NO. BERGEN, NJ");
    checkFact(gedxPerson.getFacts(), FactType.BatMitzvah, null, "1965", "BETH ABRAHAM, NO. BERGEN, NJ");
    checkFact(gedxPerson.getFacts(), FactType.BatMitzvah, null, "1864", "BETH ABRAHAM, NO. BERGEN, NJ");
    checkFact(gedxPerson.getFacts(), FactType.BatMitzvah, null, "1866", "BETH ABRAHAM, NO. BERGEN, NJ");
    checkFact(gedxPerson.getFacts(), FactType.Blessing, null, null, "HUBBARDSVILLE, NEW YORK");
    checkFact(gedxPerson.getFacts(), FactType.Blessing, null, "31 JUL 1994", "St. Joseph's, Stephentown, NY");
    checkFact(gedxPerson.getFacts(), FactType.Burial, null, "13 Dec 1946", "St. Louis No. 3 Cemetery, Toca/Rotge Tomb.");
    checkFact(gedxPerson.getFacts(), FactType.Burial, null, "14 Dec 1946", "St. Louis No. 3 Cemetery, Toca/Rotge Tomb.");
    checkFact(gedxPerson.getFacts(), FactType.Census, null, "02 JUN 1880", "1880, Sullivan Co., MO, USA");
    checkFact(gedxPerson.getFacts(), FactType.Census, null, "02 JUN 1881", "1880, Sullivan Co., MO, USA");
    checkFact(gedxPerson.getFacts(), FactType.Christening, null, "30 MAY 1865", "Brigham City,Weber Co,UT");
    checkFact(gedxPerson.getFacts(), FactType.Christening, null, "1 MAY 1865", "Brigham City,Weber Co,UT");
    checkFact(gedxPerson.getFacts(), FactType.AdultChristening, null, "24 APR 1674", null);
    checkFact(gedxPerson.getFacts(), FactType.AdultChristening, null, "25 APR 1674", null);
    checkFact(gedxPerson.getFacts(), FactType.Confirmation, null, "16 Nov 1997", "Mass of Christian Burial, St. Boniface Church, Piqua 10:00 AM.");
    checkFact(gedxPerson.getFacts(), FactType.Confirmation, null, "13 Nov 1997", "Mass of Christian Burial, St. Boniface Church, Piqua 10:00 AM.");
    checkFact(gedxPerson.getFacts(), FactType.Cremation, null, "10 MAR 1999", "Hudson, Fla");
    checkFact(gedxPerson.getFacts(), FactType.Cremation, null, "1 MAR 1999", "Hudson, Fla");
    checkFact(gedxPerson.getFacts(), FactType.Emigration, null, "1885", "Came to Thomas County, Iowa in covered wagon");
    checkFact(gedxPerson.getFacts(), FactType.Emigration, null, "1886", "Came to Thomas County, Iowa in covered wagon");
    checkFact(gedxPerson.getFacts(), FactType.FirstCommunion, null, "7 MAY 1921", "Church of Good Shepard, Manhatten, N.Y.C.,NY");
    checkFact(gedxPerson.getFacts(), FactType.FirstCommunion, null, "7 MAY 1922", "Church of Good Shepard, Manhatten, N.Y.C.,NY");
    checkFact(gedxPerson.getFacts(), FactType.Graduation, null, "5 JUN 1954", "Central Kitsap High  Silverdale Washington");
    checkFact(gedxPerson.getFacts(), FactType.Graduation, null, "18 JUN 1954", "Central Kitsap High  Silverdale Washington");
    checkFact(gedxPerson.getFacts(), FactType.Immigration, null, "1724", "\"Francis\"");
    checkFact(gedxPerson.getFacts(), FactType.Immigration, null, "1734", "\"Francis\"");
    checkFact(gedxPerson.getFacts(), FactType.Ordination, null, "31 MAR 1744", "Canterbury, Windham Co., CT.");
    checkFact(gedxPerson.getFacts(), FactType.Ordination, null, "1 MAY 1744", "Canterbury, Windham Co., CT.");
    checkFact(gedxPerson.getFacts(), FactType.Naturalization, null, "6 OCT 1854", "Philadelphia, Philadelphia Co., Pennsylvania");
    checkFact(gedxPerson.getFacts(), FactType.Naturalization, null, "7 OCT 1854", "Philadelphia, Philadelphia Co., Pennsylvania");
    checkFact(gedxPerson.getFacts(), FactType.Probate, null, "31 APR 1807", "Allegheny Co., Pennsylvania");
    checkFact(gedxPerson.getFacts(), FactType.Probate, null, "28 APR 1807", "Allegheny Co., Pennsylvania");
    checkFact(gedxPerson.getFacts(), FactType.Retirement, null, null, "Seidersville, PA");
    checkFact(gedxPerson.getFacts(), FactType.Retirement, null, "1967", "Oklahoma City, Oklahoma");
    checkFact(gedxPerson.getFacts(), FactType.Will, null, "22 APR 1761", "Rowan  Co, NC");
  }

  static void checkFact(List<Fact> facts, FactType factType, String value, String date, String place) {
    boolean found = false;
    for(Fact fact : facts) {
      if(fact.getKnownType().equals(factType)) {
        if(value == null) {
          if(fact.getOriginal() != null)
            continue;
        }
        else {
          if(!value.equals(fact.getOriginal())) {
            continue;
          }
        }
        if(date == null) {
          if(fact.getDate() != null)
            continue;
        }
        else {
          if(fact.getDate() == null || !date.equals(fact.getDate().getOriginal())) {
            continue;
          }
        }
        if(place == null) {
          if(fact.getPlace() != null)
            continue;
        }
        else {
          if(fact.getPlace() == null || !place.equals(fact.getPlace().getOriginal())) {
            continue;
          }
        }
        found = true;
      }
    }
    assertTrue(found);
  }
}
