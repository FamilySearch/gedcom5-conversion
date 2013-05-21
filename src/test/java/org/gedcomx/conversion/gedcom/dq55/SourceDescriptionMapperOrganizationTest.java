package org.gedcomx.conversion.gedcom.dq55;

import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.model.Repository;
import org.folg.gedcom.parser.ModelParser;
import org.gedcomx.common.ResourceReference;
import org.gedcomx.agent.Address;
import org.gedcomx.agent.Agent;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;


public class SourceDescriptionMapperOrganizationTest {
  Gedcom gedcom;

  @BeforeClass
  public void setUp() throws Exception {
    URL gedcomUrl = this.getClass().getClassLoader().getResource("Case002-Repositories.ged");
    File gedcomFile = new File(gedcomUrl.toURI());
    ModelParser modelParser = new ModelParser();

    gedcom = modelParser.parseGedcom(gedcomFile);
    assertNotNull(gedcom);
    assertNotNull(gedcom.getRepositories());
    assertEquals(gedcom.getRepositories().size(), 7);
  }

  @Test
  public void testToOrganization1() throws Exception {
    Repository dqRepository = gedcom.getRepositories().get(0);
    TestConversionResult result = new TestConversionResult();
    SourceDescriptionMapper mapper = new SourceDescriptionMapper();

    mapper.toOrganization(dqRepository, result);
    assertNotNull(result.getOrganizations());
    assertEquals(result.getOrganizations().size(), 1);
    Agent gedxOrganization = result.getOrganizations().get(0);
    assertNotNull(gedxOrganization);

    assertNull(gedxOrganization.getAccounts());
    assertNull(gedxOrganization.getExtensionElements());
    assertNull(gedxOrganization.getOpenid());

    // REPO
    assertEquals(gedxOrganization.getId(), "REPO3");

    // NAME
    assertEquals(gedxOrganization.getName().getValue(), "MyCorporation, Inc.");

    // ADDR
    assertNotNull(gedxOrganization.getAddresses());
    assertEquals(gedxOrganization.getAddresses().size(), 1);
    for (Address address : gedxOrganization.getAddresses()) {
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
    assertNotNull(email.getResource());
    assertEquals(email.getResource().toString(), "mailto:info@mycorporation.com");

    // PHON and FAX
    assertNotNull(gedxOrganization.getPhones());
    assertEquals(gedxOrganization.getPhones().size(), 2);
    for (ResourceReference phone : gedxOrganization.getPhones()) {
      assertNotNull(phone.getResource());

      String s = phone.getResource().toString();
      if (s.startsWith("data:,Phone%3A%20")) {
        assertEquals(s, "data:,Phone%3A%20866-000-0000");
      } else if (s.startsWith("data:,Fax%3A%20")) {
        assertEquals(s, "data:,Fax%3A%20866-111-1111");
      } else {
        fail("Unexpected phone: " + s);
      }
    }

    // WWW
    assertNotNull(gedxOrganization.getHomepage());
    assertEquals(gedxOrganization.getHomepage().getResource().toString(), "https://www.mycorporation.com/");
  }

  @Test
  public void testToOrganization2() throws Exception {
    Repository dqRepository = gedcom.getRepositories().get(1);
    TestConversionResult result = new TestConversionResult();
    SourceDescriptionMapper mapper = new SourceDescriptionMapper();

    mapper.toOrganization(dqRepository, result);
    assertNotNull(result.getOrganizations());
    assertEquals(result.getOrganizations().size(), 1);
    Agent gedxOrganization = result.getOrganizations().get(0);
    assertNotNull(gedxOrganization);

    // always null in GEDCOM 5.5 conversions
    assertNull(gedxOrganization.getAccounts());
    assertNull(gedxOrganization.getExtensionElements());
    assertNull(gedxOrganization.getOpenid());

    // REPO
    assertEquals(gedxOrganization.getId(), "REPO4");

    // NAME
    assertNotNull(gedxOrganization.getName());
    assertEquals(gedxOrganization.getName().getValue(), "York County Archive");

    // null in this repository
    assertNull(gedxOrganization.getAddresses());
    assertNull(gedxOrganization.getEmails());
    assertNull(gedxOrganization.getPhones());
    assertNull(gedxOrganization.getHomepage());
  }

  @Test
  public void testToOrganization3() throws Exception {
    Repository dqRepository = gedcom.getRepositories().get(2);
    TestConversionResult result = new TestConversionResult();
    SourceDescriptionMapper mapper = new SourceDescriptionMapper();

    mapper.toOrganization(dqRepository, result);
    assertNotNull(result.getOrganizations());
    assertEquals(result.getOrganizations().size(), 1);
    Agent gedxOrganization = result.getOrganizations().get(0);
    assertNotNull(gedxOrganization);

    // always null in GEDCOM 5.5 conversions
    assertNull(gedxOrganization.getAccounts());
    assertNull(gedxOrganization.getExtensionElements());
    assertNull(gedxOrganization.getOpenid());

    // REPO
    assertEquals(gedxOrganization.getId(), "REPO5");

    // NAME
    assertNotNull(gedxOrganization.getName());
    assertEquals(gedxOrganization.getName().getValue(), "Henry County Archive");

    // ADDR
    assertNotNull(gedxOrganization.getAddresses());
    assertEquals(gedxOrganization.getAddresses().size(), 1);
    for (Address address : gedxOrganization.getAddresses()) {
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
    assertNotNull(email.getResource());
    assertEquals(email.getResource().toString(), "mailto:henrycountyarchive@gmail.com");

    // PHON and FAX
    assertNotNull(gedxOrganization.getPhones());
    assertEquals(gedxOrganization.getPhones().size(), 1);
    for (ResourceReference phone : gedxOrganization.getPhones()) {
      assertNotNull(phone.getResource());

      String s = phone.getResource().toString();
      if (s.startsWith("data:,Phone%3A%20")) {
        assertEquals(s, "data:,Phone%3A%20(731)%20642-8655,%20Extension%20%23109");
      } else {
        fail("Unexpected phone: " + s);
      }
    }

    // WWW
    assertNotNull(gedxOrganization.getHomepage());
    assertEquals(gedxOrganization.getHomepage().getResource().toString(), "http://www.rootsweb.ancestry.com/~tnhenry2/");
  }

  @Test
  public void testToOrganization4() throws Exception {
    Repository dqRepository = gedcom.getRepositories().get(3);
    TestConversionResult result = new TestConversionResult();
    SourceDescriptionMapper mapper = new SourceDescriptionMapper();

    mapper.toOrganization(dqRepository, result);
    assertNotNull(result.getOrganizations());
    assertEquals(result.getOrganizations().size(), 1);
    Agent gedxOrganization = result.getOrganizations().get(0);
    assertNotNull(gedxOrganization);

    // always null in GEDCOM 5.5 conversions
    assertNull(gedxOrganization.getAccounts());
    assertNull(gedxOrganization.getExtensionElements());
    assertNull(gedxOrganization.getOpenid());

    // REPO
    assertEquals(gedxOrganization.getId(), "REPO6");

    // NAME
    assertNotNull(gedxOrganization.getName());
    assertEquals(gedxOrganization.getName().getValue(), "Washington County Archives");

    // ADDR
    assertNotNull(gedxOrganization.getAddresses());
    assertEquals(gedxOrganization.getAddresses().size(), 1);
    for (Address address : gedxOrganization.getAddresses()) {
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
      assertNotNull(phone.getResource());

      String s = phone.getResource().toString();
      if (s.startsWith("data:,Fax%3A%20")) {
        assertEquals(s, "data:,Fax%3A%20479-444-1777");
      } else {
        fail("Unexpected phone: " + s);
      }
    }

    // WWW
    assertNull(gedxOrganization.getHomepage());
  }

  @Test
  public void testToOrganization5() throws Exception {
    Repository dqRepository = gedcom.getRepositories().get(4);
    TestConversionResult result = new TestConversionResult();
    SourceDescriptionMapper mapper = new SourceDescriptionMapper();

    mapper.toOrganization(dqRepository, result);
    assertNotNull(result.getOrganizations());
    assertEquals(result.getOrganizations().size(), 1);
    Agent gedxOrganization = result.getOrganizations().get(0);
    assertNotNull(gedxOrganization);

    // always null in GEDCOM 5.5 conversions
    assertNull(gedxOrganization.getAccounts());
    assertNull(gedxOrganization.getExtensionElements());
    assertNull(gedxOrganization.getOpenid());

    // REPO
    assertEquals(gedxOrganization.getId(), "REPO7");

    // NAME
    assertNotNull(gedxOrganization.getName());
    assertEquals(gedxOrganization.getName().getValue(), "Cape Girardeau County Archive Center");

    // PHON and FAX
    assertNotNull(gedxOrganization.getPhones());
    assertEquals(gedxOrganization.getPhones().size(), 2);
    for (ResourceReference phone : gedxOrganization.getPhones()) {
      assertNotNull(phone.getResource());

      String s = phone.getResource().toString();
      if (s.startsWith("tel:")) {
        assertEquals(s, "tel:+1%20573.204-2331");
      } else if (s.startsWith("fax:")) {
        assertEquals(s, "fax:+1%20(573)204/2334");
      } else {
        fail("Unexpected phone: " + s);
      }
    }

    // null in this repository
    assertNull(gedxOrganization.getAddresses());
    assertNull(gedxOrganization.getEmails());
    assertNull(gedxOrganization.getHomepage());

    // Description that is the result of the CHAN tag with a bogus value
    assertNotNull(result.getSourceDescriptions());
    assertEquals(result.getSourceDescriptions().size(), 0);
  }

  @Test
  public void testToOrganization6() throws Exception {
    Repository dqRepository = gedcom.getRepositories().get(5);
    TestConversionResult result = new TestConversionResult();
    SourceDescriptionMapper mapper = new SourceDescriptionMapper();

    mapper.toOrganization(dqRepository, result);
    assertNotNull(result.getOrganizations());
    assertEquals(result.getOrganizations().size(), 1);
    Agent gedxOrganization = result.getOrganizations().get(0);
    assertNotNull(gedxOrganization);

    // always null in GEDCOM 5.5 conversions
    assertNull(gedxOrganization.getAccounts());
    assertNull(gedxOrganization.getExtensionElements());
    assertNull(gedxOrganization.getOpenid());

    // REPO
    assertEquals(gedxOrganization.getId(), "REPO8");

    // NAME
    assertNotNull(gedxOrganization.getName());
    assertEquals(gedxOrganization.getName().getValue(), "Cape Girardeau County Archive Center");

    // PHON and FAX
    assertNull(gedxOrganization.getPhones());

    // null in this repository
    assertNull(gedxOrganization.getAddresses());
    assertNull(gedxOrganization.getEmails());
    assertNull(gedxOrganization.getHomepage());

    // Description that is the result of the CHAN tag with a bogus value
    assertNotNull(result.getSourceDescriptions());
    assertEquals(result.getSourceDescriptions().size(), 0);
  }

  @Test
  public void testToOrganization7() throws Exception {
    Repository dqRepository = gedcom.getRepositories().get(6);
    TestConversionResult result = new TestConversionResult();
    SourceDescriptionMapper mapper = new SourceDescriptionMapper();

    mapper.toOrganization(dqRepository, result);
    assertNotNull(result.getOrganizations());
    assertEquals(result.getOrganizations().size(), 1);
    Agent gedxOrganization = result.getOrganizations().get(0);
    assertNotNull(gedxOrganization);

    // always null in GEDCOM 5.5 conversions
    assertNull(gedxOrganization.getAccounts());
    assertNull(gedxOrganization.getExtensionElements());
    assertNull(gedxOrganization.getOpenid());

    // REPO
    assertEquals(gedxOrganization.getId(), "REPO9");

    // NAME
    assertNotNull(gedxOrganization.getName());
    assertEquals(gedxOrganization.getName().getValue(), "Utah State Archives");

    // PHON and FAX
    assertNotNull(gedxOrganization.getPhones());
    assertEquals(gedxOrganization.getPhones().size(), 2);
    for (ResourceReference phone : gedxOrganization.getPhones()) {
      assertNotNull(phone.getResource());

      String s = phone.getResource().toString();
      if (s.startsWith("data:,Phone%3A%20")) {
        assertEquals(s, "data:,Phone%3A%20801-533-3535%20%3C%3E");
      } else if (s.startsWith("data:,Fax%3A%20")) {
        assertEquals(s, "data:,Fax%3A%20%20801-533-3504%20%3C%3E");
      } else {
        fail("Unexpected phone: " + s);
      }
    }

    // EMAIL
    assertNull(gedxOrganization.getEmails());

    // ADDR
    assertNotNull(gedxOrganization.getAddresses());
    assertEquals(gedxOrganization.getAddresses().size(), 1);
    Address address = gedxOrganization.getAddresses().get(0);
    assertEquals(address.getValue()
      , "300 S Rio Grande St\n" +
        "Salt Lake City, UT 84101-1106");

    // null in this repository
    assertNull(gedxOrganization.getHomepage());

    // Description that is the result of the CHAN tag with a bogus value
    assertNotNull(result.getSourceDescriptions());
    assertEquals(result.getSourceDescriptions().size(), 0);
  }
}
