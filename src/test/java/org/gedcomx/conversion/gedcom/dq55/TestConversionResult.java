/**
 * Copyright 2011 Intellectual Reserve, Inc. All Rights reserved.
 */
package org.gedcomx.conversion.gedcom.dq55;

import org.gedcomx.conclusion.Person;
import org.gedcomx.conclusion.Relationship;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.gedcomx.fileformat.GedcomxTimeStampUtil;
import org.gedcomx.metadata.foaf.Organization;
import org.gedcomx.metadata.source.SourceDescription;

import java.io.IOException;
import java.util.*;


public class TestConversionResult implements GedcomxConversionResult {
  private Map<String, Map<String, String>> entryAttributes = new HashMap<String, Map<String, String>>();
  private List<Person> persons = new ArrayList<Person>();
  private List<Relationship> relationships = new ArrayList<Relationship>();
  private List<SourceDescription> descriptions = new ArrayList<SourceDescription>();
  private List<org.gedcomx.metadata.foaf.Person> contributors = new ArrayList<org.gedcomx.metadata.foaf.Person>();
  private List<Organization> organizations = new ArrayList<Organization>();

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

  public List<SourceDescription> getSourceDescriptions() {
    return descriptions;
  }

  @Override
  public void addSourceDescription(SourceDescription description, Date lastModified) throws IOException {
    if (lastModified != null) {
      handleLastModified(CommonMapper.getDescriptionEntryName(description.getId()), lastModified);
    }

    this.descriptions.add(description);
  }

  public List<org.gedcomx.metadata.foaf.Person> getContributors() {
    return contributors;
  }

  @Override
  public void setDatasetContributor(org.gedcomx.metadata.foaf.Person person, Date lastModified) throws IOException {
    if (lastModified != null) {
      handleLastModified(CommonMapper.getOrganizationEntryName(person.getId()), lastModified);
    }

    this.contributors.add(person);
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

    entryAttributes.get(entryName).put("X-DC-modified", GedcomxTimeStampUtil.formatAsXmlUTC(lastModified));
  }
}
