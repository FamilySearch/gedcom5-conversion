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
import org.folg.gedcom.model.Source;
import org.gedcomx.conclusion.Person;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.gedcomx.metadata.rdf.Description;

import java.util.ArrayList;
import java.util.List;

public class GedcomMapper {
  private final PersonMapper personMapper = new PersonMapper();
  private final SourceDescriptionMapper sourceDescriptionMapper = new SourceDescriptionMapper();

  public GedcomxConversionResult toGedcomx(Gedcom dqGedcom) {
    GedcomxConversionResult resources = new GedcomxConversionResult();

    resources.setPersons(toPersons(dqGedcom.getPeople()));
    resources.setDescriptions(toSourceDescriptions(dqGedcom.getSources()));

    return resources;
  }

  private List<Person> toPersons(List<org.folg.gedcom.model.Person> dqPersons) {
    List<Person> gedxPersons = new ArrayList<Person>(dqPersons.size());

    for (org.folg.gedcom.model.Person person : dqPersons) {
      gedxPersons.add(personMapper.toPerson(person));
    }

    return gedxPersons;
  }

  private List<Description> toSourceDescriptions(List<Source> dqSources) {
    List<Description> gedxDescriptions = new ArrayList<Description>(dqSources.size());

    for (Source dqSource : dqSources) {
      gedxDescriptions.add(sourceDescriptionMapper.toSourceDescription(dqSource));
    }

    return gedxDescriptions;
  }
}
