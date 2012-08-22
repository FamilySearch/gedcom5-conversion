package org.gedcomx.conversion.gedcom.dq55;

import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.model.Source;
import org.folg.gedcom.parser.ModelParser;
import org.gedcomx.common.Note;
import org.gedcomx.metadata.foaf.Organization;
import org.gedcomx.metadata.source.SourceDescription;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.testng.Assert.*;


public class SourceDescriptionMapperDescriptionTest {
  Gedcom gedcom;

  @BeforeClass
  public void setUp() throws Exception {
    URL gedcomUrl = this.getClass().getClassLoader().getResource("Case004-SourceRecords.ged");
    File gedcomFile = new File(gedcomUrl.toURI());
    ModelParser modelParser = new ModelParser();

    gedcom = modelParser.parseGedcom(gedcomFile);
    assertNotNull(gedcom);
    assertNotNull(gedcom.getPeople());
    assertNotNull(gedcom.getSources());
    assertNotNull(gedcom.getRepositories());
    assertEquals(gedcom.getSources().size(), 20);
  }

  @Test
  public void testToSourceDescription1() throws Exception {
    Source dqSource = gedcom.getSources().get(0);
    TestConversionResult result = new TestConversionResult();
    SourceDescriptionMapper mapper = new SourceDescriptionMapper();

    mapper.toSourceDescription(dqSource, result);
    assertNotNull(result.getSourceDescriptions());
    assertEquals(result.getSourceDescriptions().size(), 1);
    SourceDescription gedxSourceDescription = result.getSourceDescriptions().get(0);
    assertNotNull(gedxSourceDescription);
    assertEquals(gedxSourceDescription.getId(), "SOUR1");
    assertNotNull(gedxSourceDescription.getCitation());
    assertEquals(gedxSourceDescription.getCitation().getValue(), "Brugge (West Vlaanderen). Burgerlijke Stand, Registers van de Burgerlijke Stand, 1796-1900, Salt Lake City, Utah : Filmed by the Genealogical Society of Utah, 1981-1995, https://www.familysearch.org/search/catalog/show?uri=http%3A%2F%2Fcatalog-search-api%3A8080%2Fwww-catalogapi-webservice%2Fitem%2F21034.");
    assertEquals(gedxSourceDescription.getCitation().getCitationTemplate().getResource().toString(), "http://gedcomx.org/gedcom5-conversion-v1-SOUR-mapping");
    assertNotNull(gedxSourceDescription.getCitation().getFields());
    assertEquals(gedxSourceDescription.getCitation().getFields().size(), 4);
    assertEquals(gedxSourceDescription.getCitation().getFields().get(0).getName().toURI().toString(), "http://gedcomx.org/gedcom5-conversion-v1-SOUR-mapping/author");
    assertEquals(gedxSourceDescription.getCitation().getFields().get(0).getValue(), "Brugge (West Vlaanderen). Burgerlijke Stand");
    assertEquals(gedxSourceDescription.getCitation().getFields().get(1).getName().toURI().toString(), "http://gedcomx.org/gedcom5-conversion-v1-SOUR-mapping/title");
    assertEquals(gedxSourceDescription.getCitation().getFields().get(1).getValue(), "Registers van de Burgerlijke Stand, 1796-1900");
    assertEquals(gedxSourceDescription.getCitation().getFields().get(2).getName().toURI().toString(), "http://gedcomx.org/gedcom5-conversion-v1-SOUR-mapping/publication-facts");
    assertEquals(gedxSourceDescription.getCitation().getFields().get(2).getValue(), "Salt Lake City, Utah : Filmed by the Genealogical Society of Utah, 1981-1995");
    assertEquals(gedxSourceDescription.getCitation().getFields().get(3).getName().toURI().toString(), "http://gedcomx.org/gedcom5-conversion-v1-SOUR-mapping/call-number");
    assertEquals(gedxSourceDescription.getCitation().getFields().get(3).getValue(), "https://www.familysearch.org/search/catalog/show?uri=http%3A%2F%2Fcatalog-search-api%3A8080%2Fwww-catalogapi-webservice%2Fitem%2F21034");
    assertNull(gedxSourceDescription.getAbout());
    assertEquals(gedxSourceDescription.getMediator().getResource().toURI().toString(), "organizations/REPO1");
    assertNull(gedxSourceDescription.getSources());
    assertNull(gedxSourceDescription.getComponentOf());
    assertEquals(gedxSourceDescription.getDisplayName(), "Civil Registration");
    assertNull(gedxSourceDescription.getAlternateNames());
    assertNull(gedxSourceDescription.getAttribution());
    assertNull(gedxSourceDescription.getNotes());
//    assertNotNull(gedxSourceDescription.getNotes());
//    assertEquals(gedxSourceDescription.getNotes().size(), 1);
//    assertEquals(gedxSourceDescription.getNotes().get(0).getText().getValue()
//      , "Microfilm genomen van de originele in het Gerechtshof te Brugge.\n" +
//        "\n" +
//        "Tekst in het Frans voor 1815.\n" +
//        "\n" +
//        "Met index.\n" +
//        "\n" +
//        "Tienjarige tafels op geboorten, huwelijken en overlijdens 1803-1813 zie Film 1226168 item 2. 1803-1813 1226169 item 1-2 1813-1823 1226173 item 2 " +
//        "1823-1832 1226175 item 4. 1823-1832 1226176 item 1 1833-1842 1226179 item 1-2 1843-1850 1226182 item 3-4. 1850-1860 1226185 item 3. " +
//        "1850-1860 1226236 item 1 1860-1870 1226239 item 3. 1860-1870 1226240 item 1-2 1871-1880 1358895 item 2 1881-1890 1383016 item 3. " +
//        "Geboorten, huwelijken 1881-1890 1383017 item 1 Overlijdens 1891-1900 1383021 item 2\n" +
//        "\n" +
//        "Civil registration of births, marriages and deaths of Brugge, West Flanders, Belgium. Includes marriage proclamations, indexes, and supplements. Text in French before 1815.");
    assertNull(gedxSourceDescription.getExtensionElements());
  }

  @Test
  public void testToSourceDescription2() throws Exception {
    Source dqSource = gedcom.getSources().get(1);
    TestConversionResult result = new TestConversionResult();
    SourceDescriptionMapper mapper = new SourceDescriptionMapper();

    mapper.toSourceDescription(dqSource, result);
    assertNotNull(result.getSourceDescriptions());
    assertEquals(result.getSourceDescriptions().size(), 1);
    SourceDescription gedxSourceDescription = result.getSourceDescriptions().get(0);
    assertNotNull(gedxSourceDescription);
    assertEquals(gedxSourceDescription.getId(), "SOUR2");
    assertNotNull(gedxSourceDescription.getCitation());
    assertEquals(gedxSourceDescription.getCitation().getValue(), "__callno__, __callno__specdeviation__.");
    assertEquals(gedxSourceDescription.getCitation().getCitationTemplate().getResource().toString(), "http://gedcomx.org/gedcom5-conversion-v1-SOUR-mapping");
    assertNotNull(gedxSourceDescription.getCitation().getFields());
    assertEquals(gedxSourceDescription.getCitation().getFields().size(), 2);
    assertEquals(gedxSourceDescription.getCitation().getFields().get(0).getName().toURI().toString(), "http://gedcomx.org/gedcom5-conversion-v1-SOUR-mapping/call-number");
    assertEquals(gedxSourceDescription.getCitation().getFields().get(0).getValue(), "__callno__");
    assertEquals(gedxSourceDescription.getCitation().getFields().get(1).getName().toURI().toString(), "http://gedcomx.org/gedcom5-conversion-v1-SOUR-mapping/call-number");
    assertEquals(gedxSourceDescription.getCitation().getFields().get(1).getValue(), "__callno__specdeviation__");
    assertNull(gedxSourceDescription.getAbout());
    assertEquals(gedxSourceDescription.getMediator().getResource().toURI().toString(), CommonMapper.getOrganizationEntryName(gedxSourceDescription.getId() + ".REPO"));
    assertNull(gedxSourceDescription.getSources());
    assertNull(gedxSourceDescription.getComponentOf());
    assertNull(gedxSourceDescription.getDisplayName());
    assertNull(gedxSourceDescription.getAlternateNames());
    assertNull(gedxSourceDescription.getAttribution());
    assertNull(gedxSourceDescription.getNotes());
    assertNull(gedxSourceDescription.getExtensionElements());

    assertNotNull(result.getOrganizations());
    assertEquals(result.getOrganizations().size(), 1);
    Organization gedxOrganization = result.getOrganizations().get(0);
    assertNotNull(gedxOrganization);
    assertEquals(gedxOrganization.getId(), gedxSourceDescription.getId() + ".REPO");
    assertNotNull(gedxOrganization.findExtensionsOfType(Note.class));
    assertEquals(gedxOrganization.findExtensionsOfType(Note.class).size(), 1);
    assertEquals(gedxOrganization.findExtensionsOfType(Note.class).get(0).getText().getValue(), "__sour2_inline_2__");
  }

  @Test
  public void testToSourceDescription4() throws Exception {
    Source dqSource = gedcom.getSources().get(17);
    TestConversionResult result = new TestConversionResult();
    SourceDescriptionMapper mapper = new SourceDescriptionMapper();

    mapper.toSourceDescription(dqSource, result);
    assertNotNull(result.getSourceDescriptions());
    assertEquals(result.getSourceDescriptions().size(), 1);
    SourceDescription gedxSourceDescription = result.getSourceDescriptions().get(0);
    assertNotNull(gedxSourceDescription);
    assertEquals(gedxSourceDescription.getId(), "SOUR18");
    assertNotNull(gedxSourceDescription.getCitation());
    assertEquals(gedxSourceDescription.getCitation().getValue(), "__sour18_titl__.");
    assertEquals(gedxSourceDescription.getCitation().getCitationTemplate().getResource().toURI().toString(), "http://gedcomx.org/gedcom5-conversion-v1-SOUR-mapping");
    assertNotNull(gedxSourceDescription.getCitation().getFields());
    assertEquals(gedxSourceDescription.getCitation().getFields().size(), 1);
    assertEquals(gedxSourceDescription.getCitation().getFields().get(0).getName().toURI().toString(), "http://gedcomx.org/gedcom5-conversion-v1-SOUR-mapping/title");
    assertEquals(gedxSourceDescription.getCitation().getFields().get(0).getValue(), "__sour18_titl__");
    assertNull(gedxSourceDescription.getAbout());
    assertEquals(gedxSourceDescription.getMediator().getResource().toURI().toString(), "organizations/REPO1");
    assertNull(gedxSourceDescription.getSources());
    assertNull(gedxSourceDescription.getComponentOf());
    assertEquals(gedxSourceDescription.getDisplayName(), "__sour18_titl__");
    assertNull(gedxSourceDescription.getAlternateNames());
    assertNull(gedxSourceDescription.getAttribution());
    assertNull(gedxSourceDescription.getNotes());
    assertNull(gedxSourceDescription.getExtensionElements());
  }

  @Test
  public void testToSourceDescription5() throws Exception {
    Source dqSource = gedcom.getSources().get(18);
    TestConversionResult result = new TestConversionResult();
    SourceDescriptionMapper mapper = new SourceDescriptionMapper();

    mapper.toSourceDescription(dqSource, result);
    assertNotNull(result.getSourceDescriptions());
    assertEquals(result.getSourceDescriptions().size(), 1);
    SourceDescription gedxSourceDescription = result.getSourceDescriptions().get(0);
    assertNotNull(gedxSourceDescription);
    assertEquals(gedxSourceDescription.getId(), "SOUR19");
    assertNotNull(gedxSourceDescription.getCitation());
    assertEquals(gedxSourceDescription.getCitation().getValue(), "__sour19_titl__.");
    assertEquals(gedxSourceDescription.getCitation().getCitationTemplate().getResource().toURI().toString(), "http://gedcomx.org/gedcom5-conversion-v1-SOUR-mapping");
    assertNotNull(gedxSourceDescription.getCitation().getFields());
    assertEquals(gedxSourceDescription.getCitation().getFields().size(), 1);
    assertEquals(gedxSourceDescription.getCitation().getFields().get(0).getName().toURI().toString(), "http://gedcomx.org/gedcom5-conversion-v1-SOUR-mapping/title");
    assertEquals(gedxSourceDescription.getCitation().getFields().get(0).getValue(), "__sour19_titl__");
    assertNull(gedxSourceDescription.getAbout());
    assertNull(gedxSourceDescription.getMediator());
    assertNull(gedxSourceDescription.getSources());
    assertNull(gedxSourceDescription.getComponentOf());
    assertEquals(gedxSourceDescription.getDisplayName(), "__sour19_titl__");
    assertNull(gedxSourceDescription.getAlternateNames());
    assertNull(gedxSourceDescription.getAttribution());
    assertNull(gedxSourceDescription.getNotes());
    assertNull(gedxSourceDescription.getExtensionElements());
    // result of the CHAN tag
    SimpleDateFormat localFormat = (SimpleDateFormat)DateFormat.getDateTimeInstance();
    localFormat.applyPattern("d MMM yy HH:mm:ss.SSS");
    Date date  = localFormat.parse("11 Nov 2011 11:11:11.111");
    SimpleDateFormat targetFormat = (SimpleDateFormat) DateFormat.getDateTimeInstance();
    targetFormat.applyPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    targetFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    assertEquals(result.getEntryAttributes("descriptions/" + gedxSourceDescription.getId()).get("X-DC-modified"), targetFormat.format(date));
  }

  @Test
  public void testToSourceDescription6() throws Exception {
    Source dqSource = gedcom.getSources().get(19);
    TestConversionResult result = new TestConversionResult();
    SourceDescriptionMapper mapper = new SourceDescriptionMapper();

    mapper.toSourceDescription(dqSource, result);
    assertNotNull(result.getSourceDescriptions());
    assertEquals(result.getSourceDescriptions().size(), 1);
    SourceDescription gedxSourceDescription = result.getSourceDescriptions().get(0);
    assertNotNull(gedxSourceDescription);
    assertEquals(gedxSourceDescription.getId(), "SOUR20");
    assertNotNull(gedxSourceDescription.getCitation());
    assertEquals(gedxSourceDescription.getCitation().getValue(), "__sour20_titl__.");
    assertEquals(gedxSourceDescription.getCitation().getCitationTemplate().getResource().toString(), "http://gedcomx.org/gedcom5-conversion-v1-SOUR-mapping");
    assertNotNull(gedxSourceDescription.getCitation().getFields());
    assertEquals(gedxSourceDescription.getCitation().getFields().size(), 1);
    assertEquals(gedxSourceDescription.getCitation().getFields().get(0).getName().toURI().toString(), "http://gedcomx.org/gedcom5-conversion-v1-SOUR-mapping/title");
    assertEquals(gedxSourceDescription.getCitation().getFields().get(0).getValue(), "__sour20_titl__");
    assertNull(gedxSourceDescription.getAbout());
    assertNull(gedxSourceDescription.getMediator());
    assertNull(gedxSourceDescription.getSources());
    assertNull(gedxSourceDescription.getComponentOf());
    assertEquals(gedxSourceDescription.getDisplayName(), "__sour20_titl__");
    assertNull(gedxSourceDescription.getAlternateNames());
    assertNull(gedxSourceDescription.getAttribution());
    assertNull(gedxSourceDescription.getNotes());
    assertNull(gedxSourceDescription.getExtensionElements());
    // result of the CHAN tag
    SimpleDateFormat localFormat = (SimpleDateFormat)DateFormat.getDateTimeInstance();
    localFormat.applyPattern("d MMM yy HH:mm:ss.SSS");
    Date date  = localFormat.parse("11 Nov 2011 00:00:00.000");
    SimpleDateFormat targetFormat = (SimpleDateFormat) DateFormat.getDateTimeInstance();
    targetFormat.applyPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    targetFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    assertEquals(result.getEntryAttributes("descriptions/" + gedxSourceDescription.getId()).get("X-DC-modified"), targetFormat.format(date));
  }
}
