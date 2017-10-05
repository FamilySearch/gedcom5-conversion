package org.gedcomx.conversion.gedcom.dq55;

import java.io.File;
import java.net.URL;

import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.parser.ModelParser;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

/**
 * a class to test that the language mappings work.
 *
 * Created by andrewpk on 10/4/17.
 */
public class LanguageMappingTest {

  private Gedcom gedcom;
  private MappingConfig mappingConfig = new MappingConfig("intputFile.ged", true);

  @Test
  public void testLanguageSetAfterConversion() throws Exception {
    URL gedcomUrl = this.getClass().getClassLoader().getResource("Case006-PersonsFacts.ged");
    File gedcomFile = new File(gedcomUrl.toURI());
    ModelParser modelParser = new ModelParser();

    gedcom = modelParser.parseGedcom(gedcomFile);
    assertNotNull(gedcom);

    GedcomMapper mapper = new GedcomMapper(mappingConfig);
    TestConversionResult result = new TestConversionResult();
    mapper.toGedcomx(gedcom, result);

    assertEquals(result.getLang(), "en");
  }

  @Test
  public void testLanguageInGedcomNotRecognized() throws Exception {
    URL gedcomUrl = this.getClass().getClassLoader().getResource("Case003-PersonsName.ged");
    File gedcomFile = new File(gedcomUrl.toURI());
    ModelParser modelParser = new ModelParser();

    gedcom = modelParser.parseGedcom(gedcomFile);
    assertNotNull(gedcom);

    GedcomMapper mapper = new GedcomMapper(mappingConfig);
    TestConversionResult result = new TestConversionResult();
    mapper.toGedcomx(gedcom, result);

    assertNull(result.getLang());
  }

  @Test
  public void testLanguageNotInGedcom() throws Exception {
    URL gedcomUrl = this.getClass().getClassLoader().getResource("Case002-Repositories.ged");
    File gedcomFile = new File(gedcomUrl.toURI());
    ModelParser modelParser = new ModelParser();

    gedcom = modelParser.parseGedcom(gedcomFile);
    assertNotNull(gedcom);

    GedcomMapper mapper = new GedcomMapper(mappingConfig);
    TestConversionResult result = new TestConversionResult();
    mapper.toGedcomx(gedcom, result);

    assertNull(result.getLang());
  }

}
