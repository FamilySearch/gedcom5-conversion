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

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.conclusion.ConclusionModel;
import org.gedcomx.conclusion.Person;
import org.gedcomx.conclusion.Relationship;
import org.gedcomx.fileformat.GedcomxOutputStream;
import org.gedcomx.metadata.foaf.Organization;
import org.gedcomx.metadata.rdf.Description;
import org.gedcomx.types.RelationshipType;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GedcomxConversionResult {
  private List<Person> persons = new ArrayList<Person>();
  private List<Description> descriptions = new ArrayList<Description>();
  private List<Organization> organizations = new ArrayList<Organization>();
  private List<Relationship> relationships = new ArrayList<Relationship>();
  private Map<String,Person> personMap = new HashMap<String, Person>();

  public List<Person> getPersons() {
    return persons;
  }

  public void addPerson(Person person, String ged5Ref ) {
    this.persons.add(person);
    personMap.put( ged5Ref, person );
  }

  public Person lookupPerson( String ged5Ref ) {
    return personMap.get(ged5Ref);
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
        String entryName = getEntryName(person);
        gedxOutputStream.addResource(ConclusionModel.GEDCOMX_CONCLUSION_V1_XML_MEDIA_TYPE
          , entryName
          , person);
      }

      // Source Descriptions
      for (Description description : getDescriptions()) {
        String entryName = getEntryName(description);
        gedxOutputStream.addResource(ConclusionModel.GEDCOMX_CONCLUSION_V1_XML_MEDIA_TYPE
          , entryName
          , description);
      }
    } finally {
      gedxOutputStream.close();
    }
  }

  /**
   * @param description resource to be saved in zip file
   * @return entry name in the zip file for the given resource
   */
  public String getEntryName(Description description) {
    return "descriptions/" + description.getId();
  }

  /**
   * @param personresource to be saved in zip file
   * @return entry name in the zip file for the given resource
   */
  public String getEntryName(Person person) {
    return "persons/" + person.getId();
  }

  /**
   * Creates and adds a GedcomX relationship from gedcom 5 objects to results.
   * @param person1
   * @param person2
   * @param relationshipType
   * @return relationship that was added
   */
  public Relationship addRelationship(org.folg.gedcom.model.Person person1, org.folg.gedcom.model.Person person2, RelationshipType relationshipType) {
    Relationship relationship = createRelationship(person1, person2, relationshipType);
    addRelationship(relationship);
    return relationship;
  }

  /**
   * Creates a GedcomX relationship from gedcom 5 objects.
   * @param person1
   * @param person2
   * @param relationshipType
   * @return relationship that was added
   */
  public Relationship createRelationship(org.folg.gedcom.model.Person person1, org.folg.gedcom.model.Person person2, RelationshipType relationshipType) {
    Relationship relationship = new Relationship();
    relationship.setKnownType(relationshipType);
    relationship.setPerson1( createReference(person1) );
    relationship.setPerson2(createReference(person2));
    return relationship;
  }

  /**
   * Finds the gedcomx person corresponding to the ged5 person and creates a GedcomX reference to it.
   * @param dqPerson gedcom 5 person
   * @return
   */
  public ResourceReference createReference(org.folg.gedcom.model.Person dqPerson) {
    ResourceReference reference = new ResourceReference();
    Person gedxPerson = lookupPerson(dqPerson.getId());
    reference.setResource( new URI(getEntryName(gedxPerson)));
    return reference;
  }
}
