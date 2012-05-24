package org.gedcomx.conversion.gedcom.dq55;

import org.folg.gedcom.model.Family;
import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.parser.ModelParser;
import org.gedcomx.common.ResourceReference;
import org.gedcomx.conclusion.Relationship;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;


public class FamilyMapperTest {
  Gedcom gedcom;

  @BeforeClass
  public void setUp() throws Exception {
    URL gedcomUrl = this.getClass().getClassLoader().getResource("Fam001.ged");
    File gedcomFile = new File(gedcomUrl.toURI());
    ModelParser modelParser = new ModelParser();

    gedcom = modelParser.parseGedcom(gedcomFile);
    gedcom.createIndexes();
    assertNotNull(gedcom);
    assertNotNull(gedcom.getRepositories());
//    assertEquals(gedcom.getRepositories().size(), 5);
  }

  @Test
  public void testToFamily1() throws Exception {
    Family dqFamily = gedcom.getFamilies().get(0);
    TestConversionResult result = new TestConversionResult();
    GedcomMapper gedcomMapper = new GedcomMapper();
    gedcomMapper.toPersons(gedcom.getPeople(), result);

    FamilyMapper mapper = new FamilyMapper();

    mapper.toRelationship(dqFamily, null, result);
    assertNotNull(result.getRelationships());
//    assertEquals(result.getRelationships().size(), 3);

    Relationship relationship = result.getRelationships().get(0);
    assertNotNull(relationship);

    ResourceReference person1 = relationship.getPerson1();
    ResourceReference person2 = relationship.getPerson2();
    assertNotNull(person1);
    assertNotNull(person2);
    assertNull(relationship.getExtensionElements());
//    assertNull(relationship.getType());

    // REPO
//    assertEquals(relationship.getId(), "REPO3");

  }

}
