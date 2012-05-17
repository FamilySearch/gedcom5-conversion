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


public class PersonsNameTest {
  Gedcom gedcom;

  @BeforeClass
  public void setUp() throws Exception {
    URL gedcomUrl = this.getClass().getClassLoader().getResource("Case003-PersonsName.ged");
    File gedcomFile = new File(gedcomUrl.toURI());
    ModelParser modelParser = new ModelParser();

    gedcom = modelParser.parseGedcom(gedcomFile);
    assertNotNull(gedcom);
    assertNotNull(gedcom.getPeople());
  }

  @Test
  public void testToPerson1() throws Exception {
    // Simple name
    Person dqPerson = gedcom.getPeople().get(0);
    GedcomxConversionResult result = new GedcomxConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person person = result.getPersons().get(0);
    assertNotNull(person.getNames());
    assertEquals(person.getNames().size(), 1);
    NameForm nameForm = person.getNames().get(0).getPrimaryForm();
    assertEquals(nameForm.getFullText(), "John Johnson");
    checkNamePartEquals(nameForm, "John", NamePartType.Given);
    checkNamePartEquals(nameForm, "Johnson", NamePartType.Surname);
    checkNamePartDoesNotExist(nameForm, NamePartType.Prefix);
    checkNamePartDoesNotExist(nameForm, NamePartType.Suffix);
  }

  @Test
  public void testToPerson2() throws Exception {
    // No trailing slash on surname
    Person dqPerson = gedcom.getPeople().get(1);
    GedcomxConversionResult result = new GedcomxConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person person = result.getPersons().get(0);
    assertNotNull(person.getNames());
    assertEquals(person.getNames().size(), 1);
    NameForm nameForm = person.getNames().get(0).getPrimaryForm();
    assertEquals(nameForm.getFullText(), "John Johnson");
    checkNamePartEquals(nameForm, "John", NamePartType.Given);
    checkNamePartEquals(nameForm, "Johnson", NamePartType.Surname);
    checkNamePartDoesNotExist(nameForm, NamePartType.Prefix);
    checkNamePartDoesNotExist(nameForm, NamePartType.Suffix);
  }

  @Test
  public void testToPerson3() throws Exception {
    // No space before or after surname slash
    Person dqPerson = gedcom.getPeople().get(2);
    GedcomxConversionResult result = new GedcomxConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person person = result.getPersons().get(0);
    assertNotNull(person.getNames());
    assertEquals(person.getNames().size(), 1);
    NameForm nameForm = person.getNames().get(0).getPrimaryForm();
    assertEquals(nameForm.getFullText(), "John Johnson");
    checkNamePartEquals(nameForm, "John", NamePartType.Given);
    checkNamePartEquals(nameForm, "Johnson", NamePartType.Surname);
    checkNamePartDoesNotExist(nameForm, NamePartType.Prefix);
    checkNamePartDoesNotExist(nameForm, NamePartType.Suffix);
  }

  @Test
  public void testToPerson4() throws Exception {
    // No space before or after surname slashes
    Person dqPerson = gedcom.getPeople().get(3);
    GedcomxConversionResult result = new GedcomxConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person person = result.getPersons().get(0);
    assertNotNull(person.getNames());
    assertEquals(person.getNames().size(), 1);
    NameForm nameForm = person.getNames().get(0).getPrimaryForm();
    assertEquals(nameForm.getFullText(), "John Johnson");
    checkNamePartEquals(nameForm, "John", NamePartType.Given);
    checkNamePartEquals(nameForm, "Johnson", NamePartType.Surname);
    checkNamePartDoesNotExist(nameForm, NamePartType.Prefix);
    checkNamePartDoesNotExist(nameForm, NamePartType.Suffix);
  }

  @Test
  public void testToPerson5() throws Exception {
    // No slashes
    Person dqPerson = gedcom.getPeople().get(4);
    GedcomxConversionResult result = new GedcomxConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person person = result.getPersons().get(0);
    assertNotNull(person.getNames());
    assertEquals(person.getNames().size(), 1);
    NameForm nameForm = person.getNames().get(0).getPrimaryForm();
    assertEquals(nameForm.getFullText(), "John Johnson");
    checkNamePartEquals(nameForm, "John", NamePartType.Given);
    checkNamePartEquals(nameForm, "Johnson", NamePartType.Surname);
    checkNamePartDoesNotExist(nameForm, NamePartType.Prefix);
    checkNamePartDoesNotExist(nameForm, NamePartType.Suffix);
  }

  @Test
  public void testToPerson6() throws Exception {
    // Surname only
    Person dqPerson = gedcom.getPeople().get(5);
    GedcomxConversionResult result = new GedcomxConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person person = result.getPersons().get(0);
    assertNotNull(person.getNames());
    assertEquals(person.getNames().size(), 1);
    NameForm nameForm = person.getNames().get(0).getPrimaryForm();
    assertEquals(nameForm.getFullText(), "Johnson");
    checkNamePartDoesNotExist(nameForm, NamePartType.Given);
    checkNamePartEquals(nameForm, "Johnson", NamePartType.Surname);
    checkNamePartDoesNotExist(nameForm, NamePartType.Prefix);
    checkNamePartDoesNotExist(nameForm, NamePartType.Suffix);
  }

  @Test
  public void testToPerson7() throws Exception {
    // Surname contains trailing space
    Person dqPerson = gedcom.getPeople().get(6);
    GedcomxConversionResult result = new GedcomxConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person person = result.getPersons().get(0);
    assertNotNull(person.getNames());
    assertEquals(person.getNames().size(), 1);
    NameForm nameForm = person.getNames().get(0).getPrimaryForm();
    assertEquals(nameForm.getFullText(), "John Johnson");
    checkNamePartEquals(nameForm, "John", NamePartType.Given);
    checkNamePartEquals(nameForm, "Johnson", NamePartType.Surname);
    checkNamePartDoesNotExist(nameForm, NamePartType.Prefix);
    checkNamePartDoesNotExist(nameForm, NamePartType.Suffix);
  }

  @Test
  public void testToPerson8() throws Exception {
    // Surname with suffix
    Person dqPerson = gedcom.getPeople().get(7);
    GedcomxConversionResult result = new GedcomxConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person person = result.getPersons().get(0);
    assertNotNull(person.getNames());
    assertEquals(person.getNames().size(), 1);
    NameForm nameForm = person.getNames().get(0).getPrimaryForm();
    assertEquals(nameForm.getFullText(), "John Johnson Jr.");
    checkNamePartEquals(nameForm, "John", NamePartType.Given);
    checkNamePartEquals(nameForm, "Johnson", NamePartType.Surname);
    checkNamePartDoesNotExist(nameForm, NamePartType.Prefix);
    checkNamePartEquals(nameForm, "Jr.", NamePartType.Suffix);
  }

  @Test
  public void testToPerson9() throws Exception {
    // Surname first
    Person dqPerson = gedcom.getPeople().get(8);
    GedcomxConversionResult result = new GedcomxConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person person = result.getPersons().get(0);
    assertNotNull(person.getNames());
    assertEquals(person.getNames().size(), 1);
    NameForm nameForm = person.getNames().get(0).getPrimaryForm();
    assertEquals(nameForm.getFullText(), "Johnson John");
    checkNamePartEquals(nameForm, "John", NamePartType.Given);
    checkNamePartEquals(nameForm, "Johnson", NamePartType.Surname);
    checkNamePartDoesNotExist(nameForm, NamePartType.Prefix);
    checkNamePartDoesNotExist(nameForm, NamePartType.Suffix);
  }

  @Test
  public void testToPerson10() throws Exception {
    // Surname not specified, but extracted from name text
    Person dqPerson = gedcom.getPeople().get(9);
    GedcomxConversionResult result = new GedcomxConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person person = result.getPersons().get(0);
    assertNotNull(person.getNames());
    assertEquals(person.getNames().size(), 1);
    NameForm nameForm = person.getNames().get(0).getPrimaryForm();
    assertEquals(nameForm.getFullText(), "John Johnson");
    checkNamePartDoesNotExist(nameForm, NamePartType.Given);
    checkNamePartEquals(nameForm, "Johnson", NamePartType.Surname);
    checkNamePartDoesNotExist(nameForm, NamePartType.Prefix);
    checkNamePartDoesNotExist(nameForm, NamePartType.Suffix);
  }

  @Test
  public void testToPerson11() throws Exception {
    // Given name only
    Person dqPerson = gedcom.getPeople().get(10);
    GedcomxConversionResult result = new GedcomxConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person person = result.getPersons().get(0);
    assertNotNull(person.getNames());
    assertEquals(person.getNames().size(), 1);
    NameForm nameForm = person.getNames().get(0).getPrimaryForm();
    assertEquals(nameForm.getFullText(), "John");
    checkNamePartEquals(nameForm, "John", NamePartType.Given);
    checkNamePartDoesNotExist(nameForm, NamePartType.Surname);
    checkNamePartDoesNotExist(nameForm, NamePartType.Prefix);
    checkNamePartDoesNotExist(nameForm, NamePartType.Suffix);
  }

  @Test
  public void testToPerson12() throws Exception {
    // Empty full text
    Person dqPerson = gedcom.getPeople().get(11);
    GedcomxConversionResult result = new GedcomxConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person person = result.getPersons().get(0);
    assertNotNull(person.getNames());
    assertEquals(person.getNames().size(), 1);
    NameForm nameForm = person.getNames().get(0).getPrimaryForm();
    assertNull(nameForm.getFullText());
    checkNamePartEquals(nameForm, "npfx", NamePartType.Prefix);
    checkNamePartEquals(nameForm, "John", NamePartType.Given);
    checkNamePartEquals(nameForm, "Johnson", NamePartType.Surname);
    checkNamePartEquals(nameForm, "nsfx", NamePartType.Suffix);
  }

  @Test
  public void testToPerson13() throws Exception {
    // Empty surname at first
    Person dqPerson = gedcom.getPeople().get(12);
    GedcomxConversionResult result = new GedcomxConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person person = result.getPersons().get(0);
    assertNotNull(person.getNames());
    assertEquals(person.getNames().size(), 1);
    NameForm nameForm = person.getNames().get(0).getPrimaryForm();
    assertEquals(nameForm.getFullText(), "John");
    checkNamePartEquals(nameForm, "John", NamePartType.Given);
    checkNamePartDoesNotExist(nameForm, NamePartType.Surname);
    checkNamePartDoesNotExist(nameForm, NamePartType.Prefix);
    checkNamePartDoesNotExist(nameForm, NamePartType.Suffix);
  }

  @Test
  public void testToPerson14() throws Exception {
    // Empty surname at first
    Person dqPerson = gedcom.getPeople().get(13);
    GedcomxConversionResult result = new GedcomxConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person person = result.getPersons().get(0);
    assertNotNull(person.getNames());
    assertEquals(person.getNames().size(), 1);
    NameForm nameForm = person.getNames().get(0).getPrimaryForm();
    assertEquals(nameForm.getFullText(), "John Jr.");
    checkNamePartDoesNotExist(nameForm, NamePartType.Surname);
    checkNamePartDoesNotExist(nameForm, NamePartType.Prefix);
    checkNamePartDoesNotExist(nameForm, NamePartType.Suffix);
  }

  @Test
  public void testToPerson15() throws Exception {
    // Empty surname at first
    Person dqPerson = gedcom.getPeople().get(14);
    GedcomxConversionResult result = new GedcomxConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person person = result.getPersons().get(0);
    assertNotNull(person.getNames());
    assertEquals(person.getNames().size(), 1);
    NameForm nameForm = person.getNames().get(0).getPrimaryForm();
    assertEquals(nameForm.getFullText(), "John");
    checkNamePartEquals(nameForm, "John", NamePartType.Given);
    checkNamePartDoesNotExist(nameForm, NamePartType.Surname);
    checkNamePartDoesNotExist(nameForm, NamePartType.Prefix);
    checkNamePartDoesNotExist(nameForm, NamePartType.Suffix);
  }

  @Test
  public void testToPerson16() throws Exception {
    // Comma separated name part
    Person dqPerson = gedcom.getPeople().get(15);
    GedcomxConversionResult result = new GedcomxConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person person = result.getPersons().get(0);
    assertNotNull(person.getNames());
    assertEquals(person.getNames().size(), 1);
    NameForm nameForm = person.getNames().get(0).getPrimaryForm();
    //TODO Full text comes in as 'A, B, C'
//    assertNull(nameForm.getFullText());
    checkNamePartEquals(nameForm, "A", NamePartType.Given);
    checkNamePartEquals(nameForm, "B", NamePartType.Given);
    checkNamePartEquals(nameForm, "C", NamePartType.Given);
    checkNamePartDoesNotExist(nameForm, NamePartType.Surname);
    checkNamePartDoesNotExist(nameForm, NamePartType.Prefix);
    checkNamePartDoesNotExist(nameForm, NamePartType.Suffix);
  }

  private void checkNamePartEquals(NameForm nameForm, String expected, NamePartType type) {
    assertNotNull(nameForm.getParts());
    boolean found = false;
    for(NamePart part : nameForm.getParts()) {
      if(part.getKnownType().equals(type)) {
        if(part.getText().equals(expected)) {
          found = true;
          break;
        }
      }
    }
    assertTrue(found);
  }

  private void checkNamePartDoesNotExist(NameForm nameForm, NamePartType type) {
    if(nameForm.getParts() != null) {
      for(NamePart part : nameForm.getParts()) {
        assertFalse(part.getKnownType().equals(type));
      }
    }
  }
}
