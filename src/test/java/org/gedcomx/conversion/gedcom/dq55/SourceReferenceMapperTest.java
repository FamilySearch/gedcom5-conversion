/**
 * Copyright 2012 Intellectual Reserve, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gedcomx.conversion.gedcom.dq55;

import org.folg.gedcom.model.Family;
import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.parser.ModelParser;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;

import static org.testng.Assert.*;


public class SourceReferenceMapperTest {
  private Gedcom gedcom;

  @BeforeClass
  public void setUp() throws Exception {
    URL gedcomUrl = this.getClass().getClassLoader().getResource("Case007-SourceCitatoins.ged");
    File gedcomFile = new File(gedcomUrl.toURI());
    ModelParser modelParser = new ModelParser();

    gedcom = modelParser.parseGedcom(gedcomFile);
    assertNotNull(gedcom);
    assertNotNull(gedcom.getPeople());
    assertNotNull(gedcom.getSources());
    assertNotNull(gedcom.getRepositories());
//    assertEquals(gedcom.getSources().size(), 20);
  }

  @Test
  public void testToSourcesAndSourceReferences1() throws Exception {
    Family dqFamily = gedcom.getFamilies().get(0);
    TestConversionResult result = new TestConversionResult();
    GedcomMapper gedcomMapper = new GedcomMapper();
    gedcomMapper.toPersons(gedcom.getPeople(), result);

    FamilyMapper mapper = new FamilyMapper();

    mapper.toRelationship(dqFamily, result);
    assertNotNull(result.getRelationships());
  }

  @Test
  public void testToSourcesAndSourceReferences2() throws Exception {
    Family dqFamily = gedcom.getFamilies().get(1);
    TestConversionResult result = new TestConversionResult();
    GedcomMapper gedcomMapper = new GedcomMapper();
    gedcomMapper.toPersons(gedcom.getPeople(), result);

    FamilyMapper mapper = new FamilyMapper();

    mapper.toRelationship(dqFamily, result);
    assertNotNull(result.getRelationships());
  }
}
