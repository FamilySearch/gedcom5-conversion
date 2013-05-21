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

import org.gedcomx.Gedcomx;
import org.gedcomx.agent.Agent;
import org.gedcomx.conclusion.Person;
import org.gedcomx.conclusion.Relationship;
import org.gedcomx.source.SourceDescription;

import java.io.IOException;
import java.util.ArrayList;


public class DefaultGedcomxConversionResult implements GedcomxConversionResult {

  private Gedcomx dataset = new Gedcomx();
  private Agent datasetContributor;

  @Override
  public Gedcomx getDataset() {
    return dataset;
  }

  @Override
  public Agent getDatasetContributor() {
    return datasetContributor;
  }

  @Override
  public void setDatasetContributor(Agent person) throws IOException {
    this.datasetContributor = person;
  }

  @Override
  public void addPerson(Person person) throws IOException {
    if (dataset.getPersons() == null) {
      dataset.setPersons(new ArrayList<Person>());
    }

    dataset.getPersons().add(person);
  }

  @Override
  public void addRelationship(Relationship relationship) throws IOException {
    if (dataset.getRelationships() == null) {
      dataset.setRelationships(new ArrayList<Relationship>());
    }

    dataset.getRelationships().add(relationship);
  }

  @Override
  public void addSourceDescription(SourceDescription description) throws IOException {
    if (dataset.getSourceDescriptions() == null) {
      dataset.setSourceDescriptions(new ArrayList<SourceDescription>());
    }

    dataset.getSourceDescriptions().add(description);
  }

  @Override
  public void addOrganization(Agent organization) throws IOException {
    if (dataset.getAgents() == null) {
      dataset.setAgents(new ArrayList<Agent>());
    }

    dataset.getAgents().add(organization);
  }

}
