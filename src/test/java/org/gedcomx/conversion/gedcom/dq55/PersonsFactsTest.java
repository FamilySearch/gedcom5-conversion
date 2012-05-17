package org.gedcomx.conversion.gedcom.dq55;

import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.model.Person;
import org.folg.gedcom.parser.ModelParser;
import org.gedcomx.conclusion.NameForm;
import org.gedcomx.conclusion.NamePart;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.gedcomx.types.GenderType;
import org.gedcomx.types.NamePartType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;

import static org.testng.Assert.*;
import static org.testng.Assert.assertEquals;


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
  public void testToPerson1() throws Exception {
    // Sex M
    Person dqPerson = gedcom.getPeople().get(0);
    GedcomxConversionResult result = new GedcomxConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person person = result.getPersons().get(0);
    assertNotNull(person.getGender());
    assertEquals(person.getGender().getKnownType(), GenderType.Male);
  }

  @Test
  public void testToPerson2() throws Exception {
    // Sex F
    Person dqPerson = gedcom.getPeople().get(1);
    GedcomxConversionResult result = new GedcomxConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person person = result.getPersons().get(0);
    assertNotNull(person.getGender());
    assertEquals(person.getGender().getKnownType(), GenderType.Female);
  }

  @Test
  public void testToPerson3() throws Exception {
    // Sex U
    Person dqPerson = gedcom.getPeople().get(2);
    GedcomxConversionResult result = new GedcomxConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person person = result.getPersons().get(0);
    assertNotNull(person.getGender());
    assertEquals(person.getGender().getKnownType(), GenderType.Unknown);
  }

  @Test
  public void testToPerson4() throws Exception {
    // Sex INVALID
    Person dqPerson = gedcom.getPeople().get(3);
    GedcomxConversionResult result = new GedcomxConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person person = result.getPersons().get(0);
    assertNull(person.getGender());
  }
}
