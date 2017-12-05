package org.gedcomx.conversion.gedcom.dq55;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.model.Person;
import org.folg.gedcom.parser.ModelParser;
import org.testng.annotations.Test;
import org.xml.sax.SAXParseException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

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

  @Test
  public void testPostProcessor() throws URISyntaxException, SAXParseException, IOException {
    URL gedcomUrl = this.getClass().getClassLoader().getResource("Case003-PersonsName.ged");
    File gedcomFile = new File(gedcomUrl.toURI());
    ModelParser modelParser = new ModelParser();

    Gedcom gedcom = modelParser.parseGedcom(gedcomFile);
    assertNotNull(gedcom);
    assertNotNull(gedcom.getPeople());

    Person dqPerson = gedcom.getPeople().get(0);
    TestConversionResult result = new TestConversionResult();
    PersonCountingPostProcessor postProcessor = new PersonCountingPostProcessor();
    PersonMapper mapper = new PersonMapper(new MappingConfig("Case003-PersonsName.ged", true), postProcessor);

    mapper.toPerson(dqPerson, result);

    assertEquals(postProcessor.personCount, 1);
  }

  private static final class PersonCountingPostProcessor implements PostProcessor {
    private int personCount = 0;
    @Override
    public void postProcessPerson(Person gedcomPerson, org.gedcomx.conclusion.Person gedxPerson) {
      personCount++;
    }
  }
}
