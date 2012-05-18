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

import java.util.List;

public class GedcomMapper {
  private final PersonMapper personMapper = new PersonMapper();
  private final FamilyMapper familyMapper = new FamilyMapper();
  private final SourceDescriptionMapper sourceDescriptionMapper = new SourceDescriptionMapper();

  public GedcomxConversionResult toGedcomx(Gedcom dqGedcom) {
    GedcomxConversionResult result = new GedcomxConversionResult();

    toPersons(dqGedcom.getPeople(), result);
    toRelationships(dqGedcom.getFamilies(), result, dqGedcom);
    toSourceDescriptions(dqGedcom.getSources(), result);
    toOrganizations(dqGedcom.getRepositories(), result);

    return result;
  }

  private void toPersons(List<Person> dqPersons, GedcomxConversionResult result) {
    for (Person person : dqPersons) {
      personMapper.toPerson(person, result);
    }
  }

  private void toRelationships(List<Family> dqFamilies, GedcomxConversionResult result, Gedcom dqGedcom) {
    for (Family family : dqFamilies) {
      familyMapper.toRelationship(family, result, dqGedcom);
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
