package org.gedcomx.conversion.gedcom.dq55;

import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.model.Submitter;
import org.folg.gedcom.parser.ModelParser;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


public class SubmitterMapperTest {
  private Gedcom gedcom;

  @BeforeClass
  public void setUp() throws Exception {
    URL gedcomUrl = this.getClass().getClassLoader().getResource("Case008-SubmitterRecord.ged");
    File gedcomFile = new File(gedcomUrl.toURI());
    ModelParser modelParser = new ModelParser();

    gedcom = modelParser.parseGedcom(gedcomFile);
    assertNotNull(gedcom);
    assertNotNull(gedcom.getSubmitter());
  }

  @Test
  public void testToContributor1() throws Exception {
    Submitter dqSubmitter = gedcom.getSubmitter();
    TestConversionResult result = new TestConversionResult();
    SubmitterMapper mapper = new SubmitterMapper();

    mapper.toContributor(dqSubmitter, result);
    assertNotNull(result.getContributors());
    assertEquals(result.getContributors().size(), 1);
  }
}
