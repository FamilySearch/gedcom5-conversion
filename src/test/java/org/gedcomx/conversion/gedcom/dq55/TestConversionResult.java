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
import java.util.*;


public class TestConversionResult implements GedcomxConversionResult {
  private Map<String, Map<String, String>> entryAttributes = new HashMap<String, Map<String, String>>();
  private List<Person> persons = new ArrayList<Person>();
  private List<Description> descriptions = new ArrayList<Description>();
  private List<Organization> organizations = new ArrayList<Organization>();
  private List<Relationship> relationships = new ArrayList<Relationship>();

  public List<Person> getPersons() {
    return persons;
  }

  @Override
  public void addPerson(Person person, Date lastModified) throws IOException {
    if (lastModified != null) {
      handleLastModified(CommonMapper.getPersonEntryName(person.getId()), lastModified);
    }

    this.persons.add(person);
  }

  public List<Relationship> getRelationships() {
    return relationships;
  }

  @Override
  public void addRelationship(Relationship relationship, Date lastModified) throws IOException {
    if (lastModified != null) {
      handleLastModified(CommonMapper.getRelationshipEntryName(relationship.getId()), lastModified);
    }

    this.relationships.add(relationship);
  }

  public List<Description> getDescriptions() {
    return descriptions;
  }

  @Override
  public void addDescription(Description description, Date lastModified) throws IOException {
    if (lastModified != null) {
      handleLastModified(CommonMapper.getDescriptionEntryName(description.getId()), lastModified);
    }

    this.descriptions.add(description);
  }

  public List<Organization> getOrganizations() {
    return organizations;
  }

  @Override
  public void addOrganization(Organization organization, Date lastModified) throws IOException {
    if (lastModified != null) {
      handleLastModified(CommonMapper.getOrganizationEntryName(organization.getId()), lastModified);
    }

    this.organizations.add(organization);
  }

  public Map<String, String> getEntryAttributes(String entryName) {
    return entryAttributes.get(entryName);
  }

  private void handleLastModified(String entryName, Date lastModified) {
    if (!entryAttributes.containsKey(entryName)) {
      entryAttributes.put(entryName, new HashMap<String, String>());
    }
    entryAttributes.get(entryName).put("Last-Modified", lastModified.toString());
  }
}
