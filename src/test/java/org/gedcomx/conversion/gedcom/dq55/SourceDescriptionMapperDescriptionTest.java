package org.gedcomx.conversion.gedcom.dq55;

import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.model.Source;
import org.folg.gedcom.parser.ModelParser;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.gedcomx.metadata.dc.DublinCoreDescriptionDecorator;
import org.gedcomx.metadata.rdf.Description;
import org.gedcomx.metadata.rdf.RDFLiteral;
import org.gedcomx.types.ResourceType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBElement;
import java.io.File;
import java.net.URL;

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
    assertEquals(gedcom.getSources().size(), 2);
  }

  @Test
  public void testToSourceDescription1() throws Exception {
    Source dqSource = gedcom.getSources().get(0);
    GedcomxConversionResult result = new GedcomxConversionResult();
    SourceDescriptionMapper mapper = new SourceDescriptionMapper();

    mapper.toSourceDescription(dqSource, result);
    assertNotNull(result.getDescriptions());
    assertEquals(result.getDescriptions().size(), 1);
    Description gedxSourceDescription = result.getDescriptions().get(0);
    assertNotNull(gedxSourceDescription);
    assertEquals(gedxSourceDescription.getId(), "SOUR1");
    assertNull(gedxSourceDescription.getAbout());
    assertEquals(gedxSourceDescription.getType().getType().toString(), ResourceType.PhysicalObject.toQNameURI().toString());
    assertNull(gedxSourceDescription.getExtensionAttributes());
    assertNotNull(gedxSourceDescription.getExtensionElements());
    assertEquals(gedxSourceDescription.findExtensionsOfType(JAXBElement.class).size(), 6);
    assertEquals(gedxSourceDescription.getExtensionElements().size(), 6);
    DublinCoreDescriptionDecorator gedxDecoratedSourceDescription = DublinCoreDescriptionDecorator.newInstance(gedxSourceDescription);
    assertEquals(gedxDecoratedSourceDescription.getAbstract().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getAccessRights().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getAccrualMethod().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getAccrualPeriodicity().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getAccrualPolicy().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getAlternative().size(), 1);
    assertEquals(gedxDecoratedSourceDescription.getAlternative().get(0).getValue(), "Civil Registration");
    assertEquals(gedxDecoratedSourceDescription.getAudience().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getAvailable().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getBibliographicCitation().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getConformsTo().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getContributor().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getCoverage().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getCreated().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getCreator().size(), 1);
    assertEquals(gedxDecoratedSourceDescription.getCreator().get(0).getValue(), "Brugge (West Vlaanderen). Burgerlijke Stand");
    assertEquals(gedxDecoratedSourceDescription.getDate().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getDateAccepted().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getDateCopyrighted().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getDateSubmitted().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getDescription().size(), 1);
    assertEquals(gedxDecoratedSourceDescription.getDescription().get(0).getValue()
      , "Microfilm genomen van de originele in het Gerechtshof te Brugge.\n" +
        "\n" +
        "Tekst in het Frans voor 1815.\n" +
        "\n" +
        "Met index.\n" +
        "\n" +
        "Tienjarige tafels op geboorten, huwelijken en overlijdens 1803-1813 zie Film 1226168 item 2. 1803-1813 1226169 item 1-2 1813-1823 1226173 item 2 " +
        "1823-1832 1226175 item 4. 1823-1832 1226176 item 1 1833-1842 1226179 item 1-2 1843-1850 1226182 item 3-4. 1850-1860 1226185 item 3. " +
        "1850-1860 1226236 item 1 1860-1870 1226239 item 3. 1860-1870 1226240 item 1-2 1871-1880 1358895 item 2 1881-1890 1383016 item 3. " +
        "Geboorten, huwelijken 1881-1890 1383017 item 1 Overlijdens 1891-1900 1383021 item 2\n" +
        "\n" +
        "Civil registration of births, marriages and deaths of Brugge, West Flanders, Belgium. Includes marriage proclamations, indexes, and supplements. Text in French before 1815.");
    assertEquals(gedxDecoratedSourceDescription.getEducationLevel().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getExtent().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getFormat().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getHasFormat().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getHasPart().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getHasVersion().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getIdentifier().size(), 1);
    assertEquals(gedxDecoratedSourceDescription.getIdentifier().get(0).getValue(), "https://www.familysearch.org/search/catalog/show?uri=http%3A%2F%2Fcatalog-search-api%3A8080%2Fwww-catalogapi-webservice%2Fitem%2F21034");
    assertEquals(gedxDecoratedSourceDescription.getInstructionalMethod().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getIsFormatOf().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getIsPartOf().size(), 1);
    assertEquals(gedxDecoratedSourceDescription.getIsPartOf().get(0).getValue(), "organizations/REPO1");
    assertEquals(gedxDecoratedSourceDescription.getIsReferencedBy().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getIsReplacedBy().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getIsRequiredBy().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getIssued().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getIsVersionOf().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getLanguage().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getLicense().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getMediator().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getMedium().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getModified().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getProvenance().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getPublisher().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getReferences().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getRelation().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getReplaces().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getRequires().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getRights().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getRightsHolder().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getSource().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getSpatial().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getSubject().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getTableOfContents().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getTemporal().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getTitle().size(), 1);
    assertEquals(gedxDecoratedSourceDescription.getTitle().get(0).getValue(), "Registers van de Burgerlijke Stand, 1796-1900");
    assertEquals(gedxDecoratedSourceDescription.getValid().size(), 0);
  }

  @Test
  public void testToSourceDescription2() throws Exception {
    Source dqSource = gedcom.getSources().get(1);
    GedcomxConversionResult result = new GedcomxConversionResult();
    SourceDescriptionMapper mapper = new SourceDescriptionMapper();

    mapper.toSourceDescription(dqSource, result);
    assertNotNull(result.getDescriptions());
    assertEquals(result.getDescriptions().size(), 1);
    Description gedxSourceDescription = result.getDescriptions().get(0);
    assertNotNull(gedxSourceDescription);
    assertEquals(gedxSourceDescription.getId(), "SOUR2");
    assertNull(gedxSourceDescription.getAbout());
    assertEquals(dqSource.getMediaType(), "electronic");
    assertNull(gedxSourceDescription.getType());
    assertNull(gedxSourceDescription.getExtensionAttributes());
    assertNotNull(gedxSourceDescription.getExtensionElements());
    assertEquals(gedxSourceDescription.findExtensionsOfType(JAXBElement.class).size(), 3);
    assertEquals(gedxSourceDescription.getExtensionElements().size(), 3);
    DublinCoreDescriptionDecorator gedxDecoratedSourceDescription = DublinCoreDescriptionDecorator.newInstance(gedxSourceDescription);
    assertEquals(gedxDecoratedSourceDescription.getAbstract().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getAccessRights().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getAccrualMethod().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getAccrualPeriodicity().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getAccrualPolicy().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getAlternative().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getAudience().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getAvailable().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getBibliographicCitation().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getConformsTo().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getContributor().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getCoverage().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getCreated().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getCreator().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getDate().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getDateAccepted().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getDateCopyrighted().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getDateSubmitted().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getDescription().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getEducationLevel().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getExtent().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getFormat().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getHasFormat().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getHasPart().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getHasVersion().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getIdentifier().size(), 2);
    boolean foundCallno = false, foundCallnoSpecdeviation = false;
    for (RDFLiteral identifier: gedxDecoratedSourceDescription.getIdentifier()) {
      if (!("__callno__".equals(identifier.getValue()))) {
        if (!foundCallno) {
          foundCallno = true;
        } else {
          fail("Duplicated identifier: " + identifier.getValue());
        }
      } else if (!("__callno__specdeviation__".equals(identifier.getValue()))) {
        if (!foundCallnoSpecdeviation) {
          foundCallnoSpecdeviation = true;
        } else {
          fail("Duplicated identifier: " + identifier.getValue());
        }
      } else {
        fail("Unexpected identifier value: " + identifier.getValue());
      }
    }
    assertEquals(gedxDecoratedSourceDescription.getInstructionalMethod().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getIsFormatOf().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getIsPartOf().size(), 1);
    assertEquals(gedxDecoratedSourceDescription.getIsPartOf().get(0).getValue(), "organizations/SOUR2.REPO");
    assertEquals(gedxDecoratedSourceDescription.getIsReferencedBy().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getIsReplacedBy().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getIsRequiredBy().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getIssued().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getIsVersionOf().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getLanguage().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getLicense().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getMediator().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getMedium().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getModified().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getProvenance().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getPublisher().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getReferences().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getRelation().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getReplaces().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getRequires().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getRights().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getRightsHolder().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getSource().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getSpatial().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getSubject().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getTableOfContents().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getTemporal().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getTitle().size(), 0);
    assertEquals(gedxDecoratedSourceDescription.getValid().size(), 0);
  }
}
