package org.gedcomx.conversion.gedcom.dq55;

import org.folg.gedcom.model.Family;
import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.parser.ModelParser;
import org.gedcomx.common.ResourceReference;
import org.gedcomx.conclusion.Fact;
import org.gedcomx.conclusion.Relationship;
import org.gedcomx.types.FactType;
import org.gedcomx.types.RelationshipType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.net.URL;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;


public class FamilyMapperTest {
  Gedcom gedcom;
  protected TestConversionResult result;

  @BeforeClass
  public void setUp() throws Exception {
    URL gedcomUrl = this.getClass().getClassLoader().getResource("Case009-Family.ged");
    File gedcomFile = new File(gedcomUrl.toURI());
    ModelParser modelParser = new ModelParser();

    gedcom = modelParser.parseGedcom(gedcomFile);
    gedcom.createIndexes();
    assertNotNull(gedcom);
  }

  @BeforeMethod
  public void setUpTest() throws Exception {
    result = new TestConversionResult();
    GedcomMapper gedcomMapper = new GedcomMapper();
    gedcomMapper.toPersons(gedcom.getPeople(), result);
  }

  @Test
  public void testToFamilyF1() throws Exception {
    FamilyMapper mapper = new FamilyMapper();
    Family dqFamily = gedcom.getFamilies().get(0);

    mapper.toRelationship(dqFamily, gedcom, result);
    assertEquals(result.getRelationships().size(), 5);

    Relationship relCouple = testRelationship(0, RelationshipType.Couple, "I1", "I11", 1);

    testRelationship(1, RelationshipType.ParentChild, "I1", "I2", 0);
    testRelationship(2, RelationshipType.ParentChild, "I11", "I2", 0);
    testRelationship(3, RelationshipType.ParentChild, "I1", "I8", 0);
    testRelationship(4, RelationshipType.ParentChild, "I11", "I8", 0);
  }

  @Test
  public void testToFamilyF2() throws Exception {
    FamilyMapper mapper = new FamilyMapper();
    Family dqFamily = gedcom.getFamilies().get(1);

    mapper.toRelationship(dqFamily, gedcom, result);
    assertEquals(result.getRelationships().size(), 3);

    Relationship relCouple = testRelationship(0, RelationshipType.Couple, "I15", "I14", 1);

    testRelationship(1, RelationshipType.ParentChild, "I15", "I1", 0);
    testRelationship(2, RelationshipType.ParentChild, "I14", "I1", 0);
  }

  @Test
  public void testToFamilyF10() throws Exception {
    FamilyMapper mapper = new FamilyMapper();
    Family dqFamily = gedcom.getFamilies().get(2);

    mapper.toRelationship(dqFamily, gedcom, result);
    assertEquals(result.getRelationships().size(), 1);

    Relationship relCouple = testRelationship(0, RelationshipType.Couple, "I117", "I14", 2);
  }

  private Relationship testRelationship(int relNumber, RelationshipType relationshipType, String person1Id, String person2Id, int factCount) {
    Relationship relationship = result.getRelationships().get(relNumber);
    testRelationship(relationship, relationshipType, person1Id, person2Id, factCount);
    return relationship;
  }

  private void testRelationship(Relationship relationship, RelationshipType relationshipType, String person1Id, String person2Id, int factCount) {
    assertEquals(relationship.getKnownType(), relationshipType);
    ResourceReference person1 = relationship.getPerson1();
    ResourceReference person2 = relationship.getPerson2();
    assertEquals(person1.getResource().toString(), CommonMapper.getPersonEntryName(person1Id));
    assertEquals(person2.getResource().toString(), CommonMapper.getPersonEntryName(person2Id));

    if (factCount > 0) {
      assertEquals(relationship.getFacts().size(), factCount);
      Fact fact0 = relationship.getFacts().get(0);
      if (relationshipType.equals(RelationshipType.Couple)) {
        assertEquals(fact0.getKnownType(), FactType.Marriage);
      }
      else if (relationshipType.equals(RelationshipType.ParentChild)) {
        assertEquals(fact0.getKnownType(), FactType.Marriage);
      }
      for (Fact fact : relationship.getFacts()) {
        assertNotNull(fact.getDate());
        assertNotNull(fact.getPlace());
      }
    }

    assertNull(relationship.getExtensionElements());
  }

}
