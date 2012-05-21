package org.gedcomx.conversion.gedcom.dq55;

import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.model.Repository;
import org.folg.gedcom.parser.ModelParser;
import org.gedcomx.common.ResourceReference;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.gedcomx.metadata.foaf.Address;
import org.gedcomx.metadata.foaf.Organization;
import org.gedcomx.metadata.rdf.Description;
import org.gedcomx.metadata.rdf.RDFLiteral;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBElement;
import java.io.File;
import java.net.URL;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;


public class SourceDescriptionMapperOrganizationTest extends BaseTest {
  Gedcom gedcom;

  @BeforeClass
  public void setUp() throws Exception {
    URL gedcomUrl = this.getClass().getClassLoader().getResource("Case002-Repositories.ged");
    File gedcomFile = new File(gedcomUrl.toURI());
    ModelParser modelParser = new ModelParser();

    gedcom = modelParser.parseGedcom(gedcomFile);
    assertNotNull(gedcom);
    assertNotNull(gedcom.getRepositories());
    assertEquals(gedcom.getRepositories().size(), 5);
  }

  @Test (enabled = false) // unable to marshal type "org.gedcomx.metadata.foaf.Organization" as an element because it is not known to this context
  public void testToOrganization1() throws Exception {
    Repository dqRepository = gedcom.getRepositories().get(0);
    TestConversionResult result = new TestConversionResult(getTestOutputStream());
    SourceDescriptionMapper mapper = new SourceDescriptionMapper();

    mapper.toOrganization(dqRepository, result);
    assertNotNull(result.getOrganizations());
    assertEquals(result.getOrganizations().size(), 1);
    Organization gedxOrganization = result.getOrganizations().get(0);
    assertNotNull(gedxOrganization);

    assertNull(gedxOrganization.getAbout());
    assertNull(gedxOrganization.getAccounts());
    assertNull(gedxOrganization.getExtensionAttributes());
    assertNull(gedxOrganization.getExtensionElements());
    assertNull(gedxOrganization.getType());
    assertNull(gedxOrganization.getOpenid());

    // REPO
    assertEquals(gedxOrganization.getId(), "REPO3");

    // NAME
    assertNotNull(gedxOrganization.getName());
    assertNull(gedxOrganization.getName().getDatatype());
    assertNull(gedxOrganization.getName().getLang());
    assertNull(gedxOrganization.getName().getExtensionAttributes());
    assertEquals(gedxOrganization.getName().getValue(), "MyCorporation, Inc.");

    // ADDR
    assertNotNull(gedxOrganization.getAddresses());
    assertEquals(gedxOrganization.getAddresses().size(), 1);
    for (Address address : gedxOrganization.getAddresses()) {
      assertNull(address.getId());
      assertEquals(address.getValue()
        , "5000 MyCorpCampus Dr\n" +
          "Hometown, ZZ  99999\n" +
          "United States");
      assertEquals(address.getStreet(), "__ADR1_VALUE__");
      assertEquals(address.getStreet2(), "__ADR2_VALUE__");
      assertEquals(address.getStreet3(), "5000 MyCorpCampus Dr");
      assertEquals(address.getCity(), "Hometown");
      assertEquals(address.getStateOrProvince(), "ZZ");
      assertEquals(address.getPostalCode(), "99999");
      assertEquals(address.getCountry(), "United States");
    }

    // EMAIL
    assertNotNull(gedxOrganization.getEmails());
    assertEquals(gedxOrganization.getEmails().size(), 1);
    ResourceReference email = gedxOrganization.getEmails().get(0);
    assertNull(email.getExtensionAttributes());
    assertNull(email.getExtensionElements());
    assertNotNull(email.getResource());
    assertEquals(email.getResource().toString(), "mailto:info@mycorporation.com");

    // PHON and FAX
    assertNotNull(gedxOrganization.getPhones());
    assertEquals(gedxOrganization.getPhones().size(), 2);
    for (ResourceReference phone : gedxOrganization.getPhones()) {
      assertNull(phone.getExtensionAttributes());
      assertNull(phone.getExtensionElements());
      assertNotNull(phone.getResource());

      String s = phone.getResource().toString();
      if (s.startsWith("data:,Phone: ")) {
        assertEquals(s, "data:,Phone: 866-000-0000");
      } else if (s.startsWith("data:,Fax: ")) {
        assertEquals(s, "data:,Fax: 866-111-1111");
      } else {
        fail("Unexpected phone: " + s);
      }
    }

    // WWW
    assertNotNull(gedxOrganization.getHomepage());
    assertNull(gedxOrganization.getHomepage().getDatatype());
    assertNull(gedxOrganization.getHomepage().getLang());
    assertNull(gedxOrganization.getHomepage().getExtensionAttributes());
    assertEquals(gedxOrganization.getHomepage().getValue(), "https://www.mycorporation.com/");
  }

  @Test (enabled = false) // unable to marshal type "org.gedcomx.metadata.foaf.Organization" as an element because it is not known to this context
  public void testToOrganization2() throws Exception {
    Repository dqRepository = gedcom.getRepositories().get(1);
    TestConversionResult result = new TestConversionResult(getTestOutputStream());
    SourceDescriptionMapper mapper = new SourceDescriptionMapper();

    mapper.toOrganization(dqRepository, result);
    assertNotNull(result.getOrganizations());
    assertEquals(result.getOrganizations().size(), 1);
    Organization gedxOrganization = result.getOrganizations().get(0);
    assertNotNull(gedxOrganization);

    // always null in GEDCOM 5.5 conversions
    assertNull(gedxOrganization.getAbout());
    assertNull(gedxOrganization.getAccounts());
    assertNull(gedxOrganization.getExtensionAttributes());
    assertNull(gedxOrganization.getExtensionElements());
    assertNull(gedxOrganization.getType());
    assertNull(gedxOrganization.getOpenid());

    // REPO
    assertEquals(gedxOrganization.getId(), "REPO4");

    // NAME
    assertNotNull(gedxOrganization.getName());
    assertNull(gedxOrganization.getName().getDatatype());
    assertNull(gedxOrganization.getName().getLang());
    assertNull(gedxOrganization.getName().getExtensionAttributes());
    assertEquals(gedxOrganization.getName().getValue(), "York County Archive");

    // null in this repository
    assertNull(gedxOrganization.getAddresses());
    assertNull(gedxOrganization.getEmails());
    assertNull(gedxOrganization.getPhones());
    assertNull(gedxOrganization.getHomepage());
  }

  @Test (enabled = false) // unable to marshal type "org.gedcomx.metadata.foaf.Organization" as an element because it is not known to this context
  public void testToOrganization3() throws Exception {
    Repository dqRepository = gedcom.getRepositories().get(2);
    TestConversionResult result = new TestConversionResult(getTestOutputStream());
    SourceDescriptionMapper mapper = new SourceDescriptionMapper();

    mapper.toOrganization(dqRepository, result);
    assertNotNull(result.getOrganizations());
    assertEquals(result.getOrganizations().size(), 1);
    Organization gedxOrganization = result.getOrganizations().get(0);
    assertNotNull(gedxOrganization);

    // always null in GEDCOM 5.5 conversions
    assertNull(gedxOrganization.getAbout());
    assertNull(gedxOrganization.getAccounts());
    assertNull(gedxOrganization.getExtensionAttributes());
    assertNull(gedxOrganization.getExtensionElements());
    assertNull(gedxOrganization.getType());
    assertNull(gedxOrganization.getOpenid());

    // REPO
    assertEquals(gedxOrganization.getId(), "REPO5");

    // NAME
    assertNotNull(gedxOrganization.getName());
    assertNull(gedxOrganization.getName().getDatatype());
    assertNull(gedxOrganization.getName().getLang());
    assertNull(gedxOrganization.getName().getExtensionAttributes());
    assertEquals(gedxOrganization.getName().getValue(), "Henry County Archive");

    // ADDR
    assertNotNull(gedxOrganization.getAddresses());
    assertEquals(gedxOrganization.getAddresses().size(), 1);
    for (Address address : gedxOrganization.getAddresses()) {
      assertNull(address.getId());
      assertEquals(address.getValue()
        , "55 Jones Bend Rd Ext\n" +
          "Paris, TN  38242\n" +
          "United States");
      assertNull(address.getStreet());
      assertNull(address.getStreet2());
      assertNull(address.getStreet3());
      assertNull(address.getCity());
      assertNull(address.getStateOrProvince());
      assertNull(address.getPostalCode());
      assertNull(address.getCountry());
    }

    // EMAIL
    assertNotNull(gedxOrganization.getEmails());
    assertEquals(gedxOrganization.getEmails().size(), 1);
    ResourceReference email = gedxOrganization.getEmails().get(0);
    assertNull(email.getExtensionAttributes());
    assertNull(email.getExtensionElements());
    assertNotNull(email.getResource());
    assertEquals(email.getResource().toString(), "mailto:henrycountyarchive@gmail.com");

    // PHON and FAX
    assertNotNull(gedxOrganization.getPhones());
    assertEquals(gedxOrganization.getPhones().size(), 1);
    for (ResourceReference phone : gedxOrganization.getPhones()) {
      assertNull(phone.getExtensionAttributes());
      assertNull(phone.getExtensionElements());
      assertNotNull(phone.getResource());

      String s = phone.getResource().toString();
      if (s.startsWith("data:,Phone: ")) {
        assertEquals(s, "data:,Phone: (731) 642-8655, Extension #109");
      } else {
        fail("Unexpected phone: " + s);
      }
    }

    // WWW
    assertNotNull(gedxOrganization.getHomepage());
    assertNull(gedxOrganization.getHomepage().getDatatype());
    assertNull(gedxOrganization.getHomepage().getLang());
    assertNull(gedxOrganization.getHomepage().getExtensionAttributes());
    assertEquals(gedxOrganization.getHomepage().getValue(), "http://www.rootsweb.ancestry.com/~tnhenry2/");

    // Description that is the result of the CHAN tag
    assertNotNull(result.getDescriptions());
    assertEquals(result.getDescriptions().size(), 1);
    Description gedxDescription = result.getDescriptions().get(0);
    assertNotNull(gedxDescription);
    assertNull(gedxDescription.getExtensionAttributes());
    assertNull(gedxDescription.getId());
    assertNull(gedxDescription.getType());
    assertNotNull(gedxDescription.getAbout());
    assertEquals(gedxDescription.getAbout().toString(), "organizations/REPO5");
    assertNotNull(gedxDescription.getExtensionElements());
    assertEquals(gedxDescription.getExtensionElements().size(), 1);
    JAXBElement<RDFLiteral> modifiedContainer = (JAXBElement<RDFLiteral>)gedxDescription.getExtensionElements().get(0);
    assertEquals(modifiedContainer.getName().getNamespaceURI(), "http://purl.org/dc/terms/");
    assertEquals(modifiedContainer.getName().getLocalPart(), "modified");
    assertEquals(modifiedContainer.getDeclaredType(), RDFLiteral.class);
    RDFLiteral value = modifiedContainer.getValue();
    assertNotNull(value);
    assertNull(value.getLang());
    assertNull(value.getExtensionAttributes());
    assertEquals(value.getDatatype().toString(), "http://www.w3.org/2001/XMLSchema#dateTime");
    assertEquals(value.getValue(), "2011-11-11T11:11:11.111-07:00");
  }

  @Test (enabled = false) // unable to marshal type "org.gedcomx.metadata.foaf.Organization" as an element because it is not known to this context
  public void testToOrganization4() throws Exception {
    Repository dqRepository = gedcom.getRepositories().get(3);
    TestConversionResult result = new TestConversionResult(getTestOutputStream());
    SourceDescriptionMapper mapper = new SourceDescriptionMapper();

    mapper.toOrganization(dqRepository, result);
    assertNotNull(result.getOrganizations());
    assertEquals(result.getOrganizations().size(), 1);
    Organization gedxOrganization = result.getOrganizations().get(0);
    assertNotNull(gedxOrganization);

    // always null in GEDCOM 5.5 conversions
    assertNull(gedxOrganization.getAbout());
    assertNull(gedxOrganization.getAccounts());
    assertNull(gedxOrganization.getExtensionAttributes());
    assertNull(gedxOrganization.getExtensionElements());
    assertNull(gedxOrganization.getType());
    assertNull(gedxOrganization.getOpenid());

    // REPO
    assertEquals(gedxOrganization.getId(), "REPO6");

    // NAME
    assertNotNull(gedxOrganization.getName());
    assertNull(gedxOrganization.getName().getDatatype());
    assertNull(gedxOrganization.getName().getLang());
    assertNull(gedxOrganization.getName().getExtensionAttributes());
    assertEquals(gedxOrganization.getName().getValue(), "Washington County Archives");

    // ADDR
    assertNotNull(gedxOrganization.getAddresses());
    assertEquals(gedxOrganization.getAddresses().size(), 1);
    for (Address address : gedxOrganization.getAddresses()) {
      assertNull(address.getId());
      assertEquals(address.getValue()
        , "208 N College Ave\n" +
          "Fayetteville, AR  72701-4202");
      assertNull(address.getStreet());
      assertNull(address.getStreet2());
      assertNull(address.getStreet3());
      assertNull(address.getCity());
      assertNull(address.getStateOrProvince());
      assertNull(address.getPostalCode());
      assertNull(address.getCountry());
    }

    // EMAIL
    assertNull(gedxOrganization.getEmails());

    // PHON and FAX
    assertNotNull(gedxOrganization.getPhones());
    assertEquals(gedxOrganization.getPhones().size(), 1);
    for (ResourceReference phone : gedxOrganization.getPhones()) {
      assertNull(phone.getExtensionAttributes());
      assertNull(phone.getExtensionElements());
      assertNotNull(phone.getResource());

      String s = phone.getResource().toString();
      if (s.startsWith("data:,Fax: ")) {
        assertEquals(s, "data:,Fax: 479-444-1777");
      } else {
        fail("Unexpected phone: " + s);
      }
    }

    // WWW
    assertNull(gedxOrganization.getHomepage());

    // Description that is the result of the CHAN tag
    assertNotNull(result.getDescriptions());
    assertEquals(result.getDescriptions().size(), 1);
    Description gedxDescription = result.getDescriptions().get(0);
    assertNotNull(gedxDescription);
    assertNull(gedxDescription.getExtensionAttributes());
    assertNull(gedxDescription.getId());
    assertNull(gedxDescription.getType());
    assertNotNull(gedxDescription.getAbout());
    assertEquals(gedxDescription.getAbout().toString(), "organizations/REPO6");
    assertNotNull(gedxDescription.getExtensionElements());
    assertEquals(gedxDescription.getExtensionElements().size(), 1);
    JAXBElement<RDFLiteral> modifiedContainer = (JAXBElement<RDFLiteral>)gedxDescription.getExtensionElements().get(0);
    assertEquals(modifiedContainer.getName().getNamespaceURI(), "http://purl.org/dc/terms/");
    assertEquals(modifiedContainer.getName().getLocalPart(), "modified");
    assertEquals(modifiedContainer.getDeclaredType(), RDFLiteral.class);
    RDFLiteral value = modifiedContainer.getValue();
    assertNotNull(value);
    assertNull(value.getLang());
    assertNull(value.getExtensionAttributes());
    assertEquals(value.getDatatype().toString(), "http://www.w3.org/2001/XMLSchema#dateTime");
    assertEquals(value.getValue(), "2011-11-11T00:00:00.000-07:00");
  }

  @Test (enabled = false) // unable to marshal type "org.gedcomx.metadata.foaf.Organization" as an element because it is not known to this context
  public void testToOrganization5() throws Exception {
    Repository dqRepository = gedcom.getRepositories().get(4);
    TestConversionResult result = new TestConversionResult(getTestOutputStream());
    SourceDescriptionMapper mapper = new SourceDescriptionMapper();

    mapper.toOrganization(dqRepository, result);
    assertNotNull(result.getOrganizations());
    assertEquals(result.getOrganizations().size(), 1);
    Organization gedxOrganization = result.getOrganizations().get(0);
    assertNotNull(gedxOrganization);

    // always null in GEDCOM 5.5 conversions
    assertNull(gedxOrganization.getAbout());
    assertNull(gedxOrganization.getAccounts());
    assertNull(gedxOrganization.getExtensionAttributes());
    assertNull(gedxOrganization.getExtensionElements());
    assertNull(gedxOrganization.getType());
    assertNull(gedxOrganization.getOpenid());

    // REPO
    assertEquals(gedxOrganization.getId(), "REPO7");

    // NAME
    assertNotNull(gedxOrganization.getName());
    assertNull(gedxOrganization.getName().getDatatype());
    assertNull(gedxOrganization.getName().getLang());
    assertNull(gedxOrganization.getName().getExtensionAttributes());
    assertEquals(gedxOrganization.getName().getValue(), "Cape Girardeau County Archive Center");

    // PHON and FAX
    assertNotNull(gedxOrganization.getPhones());
    assertEquals(gedxOrganization.getPhones().size(), 2);
    for (ResourceReference phone : gedxOrganization.getPhones()) {
      assertNull(phone.getExtensionAttributes());
      assertNull(phone.getExtensionElements());
      assertNotNull(phone.getResource());

      String s = phone.getResource().toString();
      if (s.startsWith("tel:")) {
        assertEquals(s, "tel:+1 573.204-2331");
      } else if (s.startsWith("fax:")) {
        assertEquals(s, "fax:+1 (573)204/2334");
      } else {
        fail("Unexpected phone: " + s);
      }
    }

    // null in this repository
    assertNull(gedxOrganization.getAddresses());
    assertNull(gedxOrganization.getEmails());
    assertNull(gedxOrganization.getHomepage());

    // Description that is the result of the CHAN tag with a bogus value
    assertNotNull(result.getDescriptions());
    assertEquals(result.getDescriptions().size(), 0);
  }
}
