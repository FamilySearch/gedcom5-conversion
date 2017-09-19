package org.gedcomx.conversion.gedcom.dq55;

import java.util.List;

import org.folg.gedcom.model.Person;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created on 9/19/17
 *
 * @author Scott Greenman
 */
public class PersonsMapperTest {
  @Test
  public void testMapPersonId_noFilename() throws Exception {
    TestConversionResult result = new TestConversionResult();

    Person gedcomPerson = new Person();
    gedcomPerson.setId("P101");

    PersonMapper mapperNoIncludeFilenameInId = new PersonMapper(new MappingConfig("inputFile.ged", false));
    mapperNoIncludeFilenameInId.toPerson(gedcomPerson, result);
    List<org.gedcomx.conclusion.Person> gedxPersons = result.getPersons();

    assertEquals(gedxPersons.get(0).getId(), "P101");
  }

  @Test
  public void testMapPersonId_withFilename() throws Exception {
    TestConversionResult result = new TestConversionResult();

    Person gedcomPerson = new Person();
    gedcomPerson.setId("P101");

    PersonMapper mapperIncludeFilenameInId = new PersonMapper(new MappingConfig("inputFile.ged", true));
    mapperIncludeFilenameInId.toPerson(gedcomPerson, result);
    List<org.gedcomx.conclusion.Person> gedxPersons = result.getPersons();

    assertEquals(gedxPersons.get(0).getId(), "inputFile.ged:P101");
  }
}
