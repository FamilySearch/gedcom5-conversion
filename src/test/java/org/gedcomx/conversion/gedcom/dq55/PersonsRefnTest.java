package org.gedcomx.conversion.gedcom.dq55;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.model.Person;
import org.folg.gedcom.parser.ModelParser;
import org.gedcomx.conclusion.Identifier;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


public class PersonsRefnTest {
  private MappingConfig mappingConfig = new MappingConfig("intputFile.ged", true);
  private Gedcom gedcom;

  @BeforeClass
  public void setUp() throws Exception {
    URL gedcomUrl = this.getClass().getClassLoader().getResource("Case011-PersonsRefns.ged");
    File gedcomFile = new File(gedcomUrl.toURI());
    ModelParser modelParser = new ModelParser();

    gedcom = modelParser.parseGedcom(gedcomFile);
    assertNotNull(gedcom);
    assertNotNull(gedcom.getPeople());
  }


  @Test
  public void testToPerson1() throws Exception {
    Person dqPerson = gedcom.getPeople().get(0);
    TestConversionResult result = new TestConversionResult();
    PersonMapper mapper = new PersonMapper(mappingConfig);

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);

    org.gedcomx.conclusion.Person person = result.getPersons().get(0);
    List<Identifier> identifiers = person.getIdentifiers();
    assertEquals(identifiers.size(), 2);
    assertEquals(identifiers.get(0).getType().toString(), "USER_REFERENCE_NUMBER");
    assertEquals(identifiers.get(0).getValue().toString(), "2112");
    assertEquals(identifiers.get(1).getType().toString(), "USER_REFERENCE_NUMBER");
    assertEquals(identifiers.get(1).getValue().toString(), "5555");
  }
}
