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

import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.model.Repository;
import org.folg.gedcom.model.Source;
import org.gedcomx.conversion.GedcomxConversionResult;

import java.util.List;

public class GedcomMapper {
  private final PersonMapper personMapper = new PersonMapper();
  private final SourceDescriptionMapper sourceDescriptionMapper = new SourceDescriptionMapper();

  public GedcomxConversionResult toGedcomx(Gedcom dqGedcom) {
    GedcomxConversionResult resources = new GedcomxConversionResult();

    toPersons(dqGedcom.getPeople(), resources);
    toSourceDescriptions(dqGedcom.getSources(), resources);
    toOrganizations(dqGedcom.getRepositories(), resources);

    return resources;
  }

  private void toPersons(List<org.folg.gedcom.model.Person> dqPersons, GedcomxConversionResult result) {
    for (org.folg.gedcom.model.Person person : dqPersons) {
      personMapper.toPerson(person, result);
    }
  }

  private void toSourceDescriptions(List<Source> dqSources, GedcomxConversionResult result) {
    for (Source dqSource : dqSources) {
      sourceDescriptionMapper.toSourceDescription(dqSource, result);
    }
  }

  private void toOrganizations(List<Repository> dqRepositories, GedcomxConversionResult result) {
    for (Repository dqRepository : dqRepositories) {
      sourceDescriptionMapper.toOrganization(dqRepository, result);
    }
  }
}
