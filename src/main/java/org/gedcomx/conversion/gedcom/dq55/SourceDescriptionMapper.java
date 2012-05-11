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

import org.folg.gedcom.model.Repository;
import org.folg.gedcom.model.Source;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.gedcomx.metadata.dc.ObjectFactory;
import org.gedcomx.metadata.foaf.Organization;
import org.gedcomx.metadata.rdf.Description;
import org.gedcomx.metadata.rdf.RDFLiteral;
import org.gedcomx.metadata.rdf.RDFValue;


public class SourceDescriptionMapper {
  private ObjectFactory objectFactory = new ObjectFactory();

  public void toSourceDescription(Source dqSource, GedcomxConversionResult result) {
    Description gedxSourceDescription = new Description();

    dqSource.getAbbreviation();
    dqSource.getAuthor();
    dqSource.getCallNumber();
    dqSource.getChange();
    dqSource.getDate();
    dqSource.getExtensions();
    dqSource.getId();
    dqSource.getItalic();
    dqSource.getMedia();
    dqSource.getMediaType();
    dqSource.getMediaRefs();
    dqSource.getNoteRefs();
    dqSource.getNotes();
    dqSource.getParen();
    dqSource.getPublicationFacts();
    dqSource.getReferenceNumber();
    //dqSource.getRepository();
    dqSource.getRepositoryRef();
    dqSource.getRin();
    dqSource.getText();
    dqSource.getTitle();
    dqSource.getType();
    dqSource.getUid();
  }

  void toOrganization(Repository dqRepository, GedcomxConversionResult result) {
    Organization gedxOrganization = new Organization();

    if ((dqRepository.getChange() != null) && (dqRepository.getChange().getDateTime() != null) && (dqRepository.getChange().getDateTime().getValue() != null)) {
      RDFLiteral lastModified = new RDFLiteral(dqRepository.getChange().getDateTime().getValue());

      // TODO: create a description
      Description gedxRepositoryRecordDescription = new Description();
      gedxRepositoryRecordDescription.addExtensionElement(objectFactory.createModifiedElement(lastModified));

      result.addDescription(gedxRepositoryRecordDescription);
    }

    dqRepository.getAddress();
    dqRepository.getEmail();
    dqRepository.getExtensions();
    dqRepository.getId();
    dqRepository.getName();
    dqRepository.getNoteRefs();
    dqRepository.getNotes();
    dqRepository.getPhone();
    dqRepository.getRin();
    dqRepository.getValue();
    dqRepository.getWww();
  }
}