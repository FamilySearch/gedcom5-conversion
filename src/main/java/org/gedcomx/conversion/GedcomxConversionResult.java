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

import org.gedcomx.conclusion.Person;
import org.gedcomx.conclusion.Relationship;
import org.gedcomx.metadata.foaf.Organization;
import org.gedcomx.metadata.source.SourceDescription;

import java.io.IOException;
import java.util.Date;


public interface GedcomxConversionResult {
  void addPerson(Person person, Date lastModified) throws IOException;

  void addRelationship(Relationship relationship, Date lastModified) throws IOException;

  void addSourceDescription(SourceDescription description, Date lastModified) throws IOException;

  public void setDatasetContributor(org.gedcomx.metadata.foaf.Person person, Date lastModified) throws IOException;

  void addOrganization(Organization organization, Date lastModified) throws IOException;
}
