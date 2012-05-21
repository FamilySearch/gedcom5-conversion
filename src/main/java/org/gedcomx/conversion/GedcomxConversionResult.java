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
import java.util.ArrayList;
import java.util.List;


public class GedcomxConversionResult {
  private List<Person> persons = new ArrayList<Person>();
  private List<Description> descriptions = new ArrayList<Description>();
  private List<Organization> organizations = new ArrayList<Organization>();
  private List<Relationship> relationships = new ArrayList<Relationship>();

  public List<Person> getPersons() {
    return persons;
  }

  public void addPerson(Person person) {
    this.persons.add(person);
  }

  public List<Relationship> getRelationships() {
    return relationships;
  }

  public void addRelationship(Relationship relationship) {
    this.relationships.add(relationship);
  }

  public void addRelationships(List<Relationship> relationships) {
    this.relationships.addAll(relationships);
  }

  public List<Description> getDescriptions() {
    return descriptions;
  }

  public void addDescription(Description description) {
    this.descriptions.add(description);
  }

  public void addDescriptions(List<Description> descriptions) {
    this.descriptions.addAll(descriptions);
  }

  public List<Organization> getOrganizations() {
    return organizations;
  }

  public void addOrganization(Organization organization) {
    this.organizations.add(organization);
  }

  public void addOrganizations(List<Organization> organizations) {
    this.organizations.addAll(organizations);
  }

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
        String entryName = Util.getDescriptionEntryName(description.getId());
        gedxOutputStream.addResource(ConclusionModel.GEDCOMX_CONCLUSION_V1_XML_MEDIA_TYPE
          , entryName
          , description);
      }
    } finally {
      gedxOutputStream.close();
    }
  }

  /**
   * Creates and adds a GedcomX relationship from gedcom 5 objects to results.
   * @param person1
   * @param person2
   * @param relationshipType
   * @return relationship that was added
   */
  public Relationship addRelationship(SpouseRef person1, SpouseRef person2, RelationshipType relationshipType) {
    Relationship relationship = Util.toRelationship(person1, person2, relationshipType);
    addRelationship(relationship);
    return relationship;
  }

}
