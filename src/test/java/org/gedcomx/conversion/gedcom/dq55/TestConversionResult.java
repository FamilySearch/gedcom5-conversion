/**
 * Copyright 2011 Intellectual Reserve, Inc. All Rights reserved.
 */
package org.gedcomx.conversion.gedcom.dq55;

import org.gedcomx.Gedcomx;
import org.gedcomx.agent.Agent;
import org.gedcomx.conclusion.Person;
import org.gedcomx.conclusion.Relationship;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.gedcomx.source.SourceDescription;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TestConversionResult implements GedcomxConversionResult {

  private Map<String, Map<String, String>> entryAttributes = new HashMap<String, Map<String, String>>();
  private List<Person> persons = new ArrayList<Person>();
  private List<Relationship> relationships = new ArrayList<Relationship>();
  private List<SourceDescription> descriptions = new ArrayList<SourceDescription>();
  private List<org.gedcomx.agent.Agent> contributors = new ArrayList<org.gedcomx.agent.Agent>();
  private List<Agent> organizations = new ArrayList<Agent>();
  private String langCode = null;

  @Override
  public Gedcomx getDataset() {
    return null;
  }

  @Override
  public Agent getDatasetContributor() {
    return null;
  }

  public List<Person> getPersons() {
    return persons;
  }

  @Override
  public void addPerson(Person person) throws IOException {
    this.persons.add(person);
  }

  public List<Relationship> getRelationships() {
    return relationships;
  }

  @Override
  public void addRelationship(Relationship relationship) throws IOException {
    this.relationships.add(relationship);
  }

  public List<SourceDescription> getSourceDescriptions() {
    return descriptions;
  }

  @Override
  public void addSourceDescription(SourceDescription description) throws IOException {
    this.descriptions.add(description);
  }

  public List<org.gedcomx.agent.Agent> getContributors() {
    return contributors;
  }

  @Override
  public void setDatasetContributor(org.gedcomx.agent.Agent person) throws IOException {
    this.contributors.add(person);
  }

  public List<Agent> getOrganizations() {
    return organizations;
  }

  @Override
  public void addOrganization(Agent organization) throws IOException {
    this.organizations.add(organization);
  }

  public String getLang() {
    return this.langCode;
  }

  @Override
  public void addLanguage(String langCode) {
    this.langCode = langCode;
  }

  public Map<String, String> getEntryAttributes(String entryName) {
    return entryAttributes.get(entryName);
  }

}
