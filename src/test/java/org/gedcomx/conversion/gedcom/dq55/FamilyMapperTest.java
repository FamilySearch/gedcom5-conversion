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

import java.io.File;
import java.net.URL;
import java.util.List;

import static org.testng.Assert.*;


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
    // executed once before each @Test case
    // reset the result to a known and empty state
    result = new TestConversionResult();
    GedcomMapper gedcomMapper = new GedcomMapper();
    gedcomMapper.toPersons(gedcom.getPeople(), result);
  }

  @Test
  public void testFamilyF1() throws Exception {
    FamilyMapper mapper = new FamilyMapper();
    Family dqFamily = gedcom.getFamilies().get(0);

    mapper.toRelationship(dqFamily, gedcom, result);
    assertEquals(result.getRelationships().size(), 5);

    Relationship relCouple = testRelationship(0, RelationshipType.Couple, "I1", "I11", 1);
    assertSize(relCouple.getSources(), 1);
    assertSize(relCouple.getNotes(), 0);

    testRelationship(1, RelationshipType.ParentChild, "I1", "I2", 0);
    testRelationship(2, RelationshipType.ParentChild, "I11", "I2", 0);
    testRelationship(3, RelationshipType.ParentChild, "I1", "I8", 0);
    testRelationship(4, RelationshipType.ParentChild, "I11", "I8", 0);
  }

  @Test
  public void testFamilyF2() throws Exception {
    FamilyMapper mapper = new FamilyMapper();
    Family dqFamily = gedcom.getFamilies().get(1);

    mapper.toRelationship(dqFamily, gedcom, result);
    assertEquals(result.getRelationships().size(), 3);

    Relationship relCouple = testRelationship(0, RelationshipType.Couple, "I15", "I14", 1);
    assertSize(relCouple.getSources(), 0);
    assertSize(relCouple.getNotes(), 0);

    testRelationship(1, RelationshipType.ParentChild, "I15", "I1", 0);
    testRelationship(2, RelationshipType.ParentChild, "I14", "I1", 0);
  }

  @Test
  public void testFamilyF10() throws Exception {
    FamilyMapper mapper = new FamilyMapper();
    Family dqFamily = gedcom.getFamilies().get(2);

    mapper.toRelationship(dqFamily, gedcom, result);
    assertEquals(result.getRelationships().size(), 1);

    Relationship relCouple = testRelationship(0, RelationshipType.Couple, "I117", "I14", 2);
    assertSize(relCouple.getSources(), 1);
    assertSize(relCouple.getNotes(), 0);
  }

  @Test
  public void testFamilyF20() throws Exception {
    // Test child to family facts
    Relationship rel;
    FamilyMapper mapper = new FamilyMapper();
    Family dqFamily = gedcom.getFamilies().get(3);

    mapper.toRelationship(dqFamily, gedcom, result);
    assertEquals(result.getRelationships().size(), 9);

    testRelationship(0, RelationshipType.Couple, "I1000", "I1001", 0);
    rel = testRelationship(1, RelationshipType.ParentChild, "I1000", "I1002", 1);
    testFactExistance(rel, FactType.Adopted);
    rel = testRelationship(2, RelationshipType.ParentChild, "I1001", "I1002", 1);
    testFactExistance(rel, FactType.Adopted);
    rel = testRelationship(3, RelationshipType.ParentChild, "I1000", "I1003", 1);
    testFactExistance(rel, FactType.Foster);
    rel = testRelationship(4, RelationshipType.ParentChild, "I1001", "I1003", 1);
    testFactExistance(rel, FactType.Foster);
    rel = testRelationship(5, RelationshipType.ParentChild, "I1000", "I1004", 1);
    testFactExistance(rel, FactType.Biological);
    rel = testRelationship(6, RelationshipType.ParentChild, "I1001", "I1004", 1);
    testFactExistance(rel, FactType.Biological);
    rel = testRelationship(7, RelationshipType.ParentChild, "I1000", "I1005", 0);
    assertNotNull(rel);
    rel = testRelationship(8, RelationshipType.ParentChild, "I1001", "I1005", 0);
    assertNotNull(rel);
  }

  @Test
  public void testFamilyF25() throws Exception {
    // Test couple facts
    Relationship rel;
    FamilyMapper mapper = new FamilyMapper();
    Family dqFamily = gedcom.getFamilies().get(8);

    mapper.toRelationship(dqFamily, gedcom, result);
    assertEquals(result.getRelationships().size(), 1);

    rel = testRelationship(0, RelationshipType.Couple, "I1006", "I1007", 17);
    PersonsFactsTest.checkFact(rel.getFacts(), FactType.CountOfChildren, "5", null, null);
    PersonsFactsTest.checkFact(rel.getFacts(), FactType.Annulment, null, "31 MAR 1932", "San Francisco, California, United States");
    PersonsFactsTest.checkFact(rel.getFacts(), FactType.Engagement, null, "1 JAN 1932", "San Francisco, California, United States");
    PersonsFactsTest.checkFact(rel.getFacts(), FactType.DivorceFiling, null, "1 APR 1932", "San Francisco, California, United States");
    PersonsFactsTest.checkFact(rel.getFacts(), FactType.Divorce, null, "2 APR 1932", "San Francisco, California, United States");
    PersonsFactsTest.checkFact(rel.getFacts(), FactType.MarriageBanns, null, "1 DEC 1931", "San Francisco, California, United States");
    PersonsFactsTest.checkFact(rel.getFacts(), FactType.MarriageLicense, null, "3 MAY 1932", "San Francisco, California, United States");
    PersonsFactsTest.checkFact(rel.getFacts(), FactType.MarriageSettlement, null, "4 JUL 1932", "San Francisco, California, United States");
    PersonsFactsTest.checkFact(rel.getFacts(), FactType.Residence, null, "5 NOV 1932", "San Francisco, California, United States");
    PersonsFactsTest.checkFact(rel.getFacts(), FactType.CommonLawMarriage, null, null, null);
    PersonsFactsTest.checkFact(rel.getFacts(), FactType.Divorce, null, "2 APR 1900", "San Francisco, California, United States");
    PersonsFactsTest.checkFact(rel.getFacts(), FactType.Separation, null, "DEC 1896", null);
    PersonsFactsTest.checkFact(rel.getFacts(), FactType.Separation, null, "DEC 1897", null);
    PersonsFactsTest.checkFact(rel.getFacts(), FactType.Separation, null, "DEC 1898", null);
  }

  private void assertSize(List list, int count) {
    if (count == 0) {
      if (list != null)
        assertEquals(list.size(), 0);
    }
    else {
      assertEquals(list.size(), count);
    }
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
      assertNotNull(relationship.getFacts());
      assertEquals(relationship.getFacts().size(), factCount);
      Fact fact0 = relationship.getFacts().get(0);
      if (relationshipType.equals(RelationshipType.Couple)) {
        assertEquals(fact0.getKnownType(), FactType.Marriage);
        assertNotNull(fact0.getDate());
        assertNotNull(fact0.getPlace());
      }
    } else {
      assertNull(relationship.getFacts());
    }

    assertNull(relationship.getExtensionElements());
  }

  private void testFactExistance(Relationship relationship, FactType factType) {
    boolean found = false;
    for (Fact fact : relationship.getFacts()) {
      if(fact.getKnownType().equals(factType)) {
        found = true;
        break;
      }
    }
    assertTrue(found);
  }
}
