package org.gedcomx.conversion.gedcom.dq55;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.model.Person;
import org.folg.gedcom.parser.ModelParser;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.familysearch.platform.ordinances.Ordinance;
import org.familysearch.platform.ordinances.OrdinanceStatus;
import org.familysearch.platform.ordinances.OrdinanceType;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;


public class PersonsOrdinancesTest {
  private MappingConfig mappingConfig = new MappingConfig("intputFile.ged", true);
  private Gedcom gedcom;

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
    PersonMapper mapper = new PersonMapper(mappingConfig);

    mapper.toPerson(dqPerson, result);
    assertNotNull(result.getPersons());
    assertEquals(result.getPersons().size(), 1);
    org.gedcomx.conclusion.Person person = result.getPersons().get(0);
    List<Ordinance> extensionElements = person.findExtensionsOfType(Ordinance.class);
    validateOrdinanceExists(extensionElements, OrdinanceType.Baptism, OrdinanceStatus.Completed, "16 Feb 1985", "TOKYO");
    validateOrdinanceExists(extensionElements, OrdinanceType.Confirmation, null, "16 Feb 1985", "SUVA");
    validateOrdinanceExists(extensionElements, OrdinanceType.Initiatory, null, "12 Apr 1985", "ABA");
    validateOrdinanceExists(extensionElements, OrdinanceType.Endowment, OrdinanceStatus.InProgress, "3 May 1985", "TOKYO");
    validateOrdinanceExists(extensionElements, OrdinanceType.SealingChildToParents, OrdinanceStatus.NeedMoreInformation, null, null);
  }

  private void validateOrdinanceExists(List<Ordinance> extensionElements, OrdinanceType ordinanceType, OrdinanceStatus status, String originalDate, String templeCode) {
    for (Ordinance ordinance : extensionElements) {
      if (ordinanceType == ordinance.getKnownType() && status == ordinance.getKnownStatus()
          && ((originalDate == null && ordinance.getDate() == null) || originalDate.equals(ordinance.getDate().getOriginal()))
          && equalOrBothNull(templeCode, ordinance.getTempleCode())) {
        return;
      }
    }
    fail(String.format("Failed to find an ordinance on the extension list. ordinanceType=%s status=%s originalDate=%s templeCode=%s", ordinanceType,
        status, originalDate, templeCode));
  }

  private boolean equalOrBothNull(String value, String value2) {
    return (value == null && value2 == null) || (value != null && value.equals(value2));
  }


}
