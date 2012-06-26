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

import org.folg.gedcom.model.*;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.gedcomx.conversion.GedcomxOutputstreamConversionResult;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class GedcomMapper {

  private final PersonMapper personMapper = new PersonMapper();
  private final FamilyMapper familyMapper = new FamilyMapper();
  private final SubmitterMapper submitterMapper = new SubmitterMapper();
  private final SourceDescriptionMapper sourceDescriptionMapper = new SourceDescriptionMapper();

  public GedcomxOutputstreamConversionResult toGedcomx(Gedcom dqGedcom, OutputStream outputStream) throws IOException {
    return toGedcomx(dqGedcom, new GedcomxOutputstreamConversionResult(outputStream));
  }

  public GedcomxOutputstreamConversionResult toGedcomx(Gedcom dqGedcom, GedcomxOutputstreamConversionResult result) throws IOException {
    toPersons(dqGedcom.getPeople(), result);
    toRelationships(dqGedcom.getFamilies(), dqGedcom, result);
    toSourceDescriptions(dqGedcom.getSources(), result);
    toOrganizations(dqGedcom.getRepositories(), result);

    submitterMapper.toContributor(dqGedcom.getSubmitter(), result);

    return result;
  }

  void toPersons(List<Person> dqPersons, GedcomxConversionResult result) throws IOException {
    for (Person person : dqPersons) {
      personMapper.toPerson(person, result);
    }
  }

  private void toRelationships(List<Family> dqFamilies, Gedcom dqGedcom, GedcomxConversionResult result) throws IOException {
    for (Family family : dqFamilies) {
      familyMapper.toRelationship(family, dqGedcom, result);
    }
  }

  private void toSourceDescriptions(List<Source> dqSources, GedcomxConversionResult result) throws IOException {
    for (Source dqSource : dqSources) {
      sourceDescriptionMapper.toSourceDescription(dqSource, result);
    }
  }

  private void toOrganizations(List<Repository> dqRepositories, GedcomxConversionResult result) throws IOException {
    for (Repository dqRepository : dqRepositories) {
      sourceDescriptionMapper.toOrganization(dqRepository, result);
    }
  }
}
