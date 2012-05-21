package org.gedcomx.conversion.gedcom.dq55;

import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.model.Person;
import org.folg.gedcom.parser.ModelParser;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


public class PersonsFactsTest extends BaseTest {
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
  public void testToPerson1() throws Exception {
    // Birth and Death - Dates and places
    Person dqPerson = gedcom.getPeople().get(0);
    TestConversionResult result = new TestConversionResult(getTestOutputStream());
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person person = result.getPersons().get(0);
    assertEquals(person.getNames().get(0).getPrimaryForm().getFullText(), "Petrus Marinus Louwaert");
  }
}
