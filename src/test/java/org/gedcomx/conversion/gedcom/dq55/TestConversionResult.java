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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class TestConversionResult extends GedcomxConversionResult {
  private List<Person> persons = new ArrayList<Person>();
  private List<Description> descriptions = new ArrayList<Description>();
  private List<Organization> organizations = new ArrayList<Organization>();
  private List<Relationship> relationships = new ArrayList<Relationship>();

  public TestConversionResult(OutputStream outputStream) throws IOException {
    super(outputStream);
  }

  public List<Person> getPersons() {
    return persons;
  }

  public void addPerson(Person person) throws IOException {
    super.addPerson(person);
    this.persons.add(person);
  }

  public List<Relationship> getRelationships() {
    return relationships;
  }

  public void addRelationship(Relationship relationship) throws IOException {
    super.addRelationship(relationship);
    this.relationships.add(relationship);
  }

  public List<Description> getDescriptions() {
    return descriptions;
  }

  public void addDescription(Description description) throws IOException {
    super.addDescription(description);
    this.descriptions.add(description);
  }

  public List<Organization> getOrganizations() {
    return organizations;
  }

  public void addOrganization(Organization organization) throws IOException {
    super.addOrganization(organization);
    this.organizations.add(organization);
  }
}
