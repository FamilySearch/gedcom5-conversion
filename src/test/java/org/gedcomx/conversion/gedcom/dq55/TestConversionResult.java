/**
 * Copyright 2011 Intellectual Reserve, Inc. All Rights reserved.
 */
package org.gedcomx.conversion.gedcom.dq55;

import org.gedcomx.conclusion.Person;
import org.gedcomx.conclusion.Relationship;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.gedcomx.metadata.foaf.Organization;
import org.gedcomx.metadata.rdf.Description;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 */
public class TestConversionResult implements GedcomxConversionResult {
  private List<Person> persons = new ArrayList<Person>();
  private List<Description> descriptions = new ArrayList<Description>();
  private List<Organization> organizations = new ArrayList<Organization>();
  private List<Relationship> relationships = new ArrayList<Relationship>();

  public List<Person> getPersons() {
    return persons;
  }

  @Override
  public void addPerson(Person person, Date lastModified) throws IOException {
    this.persons.add(person);
  }

  public List<Relationship> getRelationships() {
    return relationships;
  }

  @Override
  public void addRelationship(Relationship relationship, Date lastModified) throws IOException {
    this.relationships.add(relationship);
  }

  public List<Description> getDescriptions() {
    return descriptions;
  }

  @Override
  public void addDescription(Description description, Date lastModified) throws IOException {
    this.descriptions.add(description);
  }

  public List<Organization> getOrganizations() {
    return organizations;
  }

  @Override
  public void addOrganization(Organization organization, Date lastModified) throws IOException {
    this.organizations.add(organization);
  }
}
