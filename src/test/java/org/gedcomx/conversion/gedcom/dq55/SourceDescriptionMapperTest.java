package org.gedcomx.conversion.gedcom.dq55;

import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.model.Repository;
import org.folg.gedcom.parser.ModelParser;
import org.gedcomx.common.ResourceReference;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.gedcomx.metadata.foaf.Address;
import org.gedcomx.metadata.foaf.Organization;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;


public class SourceDescriptionMapperTest {
  Gedcom gedcom;

  @BeforeClass
  public void setUp() throws Exception {
    URL gedcomUrl = this.getClass().getClassLoader().getResource("Case002-Repositories.ged");
    File gedcomFile = new File(gedcomUrl.toURI());
    ModelParser modelParser = new ModelParser();

    gedcom = modelParser.parseGedcom(gedcomFile);
    assertNotNull(gedcom);
    assertNotNull(gedcom.getRepositories());
    assertEquals(gedcom.getRepositories().size(), 1);
  }

  @Test
  public void testToOrganization() throws Exception {
    Repository dqRepository = gedcom.getRepositories().get(0);
    GedcomxConversionResult result = new GedcomxConversionResult();
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
      if (s.startsWith("tel:")) {
        assertEquals(s, "tel:866-000-0000");
      } else if (s.startsWith("fax:")) {
        assertEquals(s, "fax:866-111-1111");
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
}
