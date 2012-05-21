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
package org.gedcomx.conversion;

import org.folg.gedcom.model.SpouseRef;
import org.gedcomx.conclusion.ConclusionModel;
import org.gedcomx.conclusion.Person;
import org.gedcomx.conclusion.Relationship;
import org.gedcomx.conversion.gedcom.dq55.Util;
import org.gedcomx.fileformat.GedcomxOutputStream;
import org.gedcomx.metadata.foaf.Organization;
import org.gedcomx.metadata.rdf.Description;
import org.gedcomx.types.RelationshipType;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;


public class GedcomxConversionResult {
  private GedcomxOutputStream gedxOutputStream;

  public GedcomxConversionResult(OutputStream outputStream) throws IOException {
    gedxOutputStream = new GedcomxOutputStream(outputStream);
  }

  public void finish(boolean closeStream) throws IOException {
    if (closeStream)
      close();
  }

  public void close() throws IOException {
    gedxOutputStream.close();
  }

  public void addPerson(Person person) throws IOException {
    String entryName = Util.getPersonEntryName(person.getId());
    gedxOutputStream.addResource(ConclusionModel.GEDCOMX_CONCLUSION_V1_XML_MEDIA_TYPE, entryName, person);
  }

  public void addRelationship(Relationship relationship) throws IOException {
    String entryName = Util.getRelationshipEntryName(relationship.getId());
    gedxOutputStream.addResource(ConclusionModel.GEDCOMX_CONCLUSION_V1_XML_MEDIA_TYPE, entryName, relationship);
  }

  /**
   * Creates and adds a GedcomX relationship from gedcom 5 objects to results.
   * @param person1
   * @param person2
   * @param relationshipType
   * @return relationship that was added
   */
  public Relationship addRelationship(SpouseRef person1, SpouseRef person2, RelationshipType relationshipType) throws IOException {
    Relationship relationship = Util.toRelationship(person1, person2, relationshipType);
    addRelationship(relationship);
    return relationship;
  }

  public void addDescription(Description description) throws IOException {
    String entryName = Util.getDescriptionEntryName(description.getId());
    gedxOutputStream.addResource(ConclusionModel.GEDCOMX_CONCLUSION_V1_XML_MEDIA_TYPE, entryName, description);
  }

  public void addOrganization(Organization organization) throws IOException {
    String entryName = Util.getDescriptionEntryName(organization.getId());
    gedxOutputStream.addResource(ConclusionModel.GEDCOMX_CONCLUSION_V1_XML_MEDIA_TYPE, entryName, organization);
  }

/*
  public void write(OutputStream outputStream) throws IOException {
    GedcomxOutputStream gedxOutputStream = new GedcomxOutputStream(outputStream);
    try {
      // Persons
      for (Person person : getPersons()) {
        String entryName = Util.getPersonEntryName(person.getId());
        gedxOutputStream.addResource(ConclusionModel.GEDCOMX_CONCLUSION_V1_XML_MEDIA_TYPE
          , entryName
          , person);
      }

      // Source Descriptions
      for (Description description : getDescriptions()) {
      }
    } finally {
      gedxOutputStream.close();
    }
  }
*/

}
