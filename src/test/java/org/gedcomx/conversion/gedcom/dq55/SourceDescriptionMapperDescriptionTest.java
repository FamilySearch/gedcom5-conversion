package org.gedcomx.conversion.gedcom.dq55;

import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.model.Repository;
import org.folg.gedcom.model.Source;
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

  }
}
