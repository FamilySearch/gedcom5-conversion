package org.gedcomx.conversion.gedcom.dq55;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.model.Person;
import org.folg.gedcom.parser.ModelParser;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.familysearch.platform.ordinances.Ordinance;
import org.familysearch.platform.ordinances.OrdinanceType;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;


public class PersonsOrdinancesTest {
  Gedcom gedcom;

  @BeforeClass
  public void setUp() throws Exception {
    URL gedcomUrl = this.getClass().getClassLoader().getResource("Case010-PersonOrdinances.ged");
    File gedcomFile = new File(gedcomUrl.toURI());
    ModelParser modelParser = new ModelParser();

    gedcom = modelParser.parseGedcom(gedcomFile);
    assertNotNull(gedcom);
    assertNotNull(gedcom.getPeople());
  }

  @Test
  public void testOrdinances() throws Exception {
    Person dqPerson = gedcom.getPeople().get(0);
    TestConversionResult result = new TestConversionResult();
    PersonMapper mapper = new PersonMapper();

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);
    org.gedcomx.conclusion.Person person = result.getPersons().get(0);
    List<Object> extensionElements = person.getExtensionElements();
    validateOrdinanceExists(extensionElements, OrdinanceType.Baptism, "16 Feb 1985", "TOKYO");
    validateOrdinanceExists(extensionElements, OrdinanceType.Confirmation, "16 Feb 1985", "SUVA");
    validateOrdinanceExists(extensionElements, OrdinanceType.Initiatory, "12 Apr 1985", "ABA");
    validateOrdinanceExists(extensionElements, OrdinanceType.Endowment, "3 May 1985", "TOKYO");
    validateOrdinanceExists(extensionElements, OrdinanceType.SealingChildToParents, "12 Jul 1985", "TOKYO");
  }

  private void validateOrdinanceExists(List<Object> extensionElements, OrdinanceType ordinanceType, String originalDate, String templeCode) {
    for (Object each : extensionElements) {
      Ordinance ordinance = (Ordinance) each;
      if (ordinanceType == ordinance.getKnownType() && equalOrBothNull(originalDate, ordinance.getPerformedDate().getOriginal())
          && equalOrBothNull(templeCode, ordinance.getTempleCode())) {
        return;
      }
    }
    fail(String.format("Failed to find an ordinance on the extension list. ordinanceType=%s originalDate=%s templeCode=%s", ordinanceType,
        originalDate, templeCode));
  }

  private boolean equalOrBothNull(String value, String value2) {
    return (value == null && value2 == null) || (value != null && value.equals(value2));
  }


}
