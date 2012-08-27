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

import org.folg.gedcom.model.GedcomTag;
import org.folg.gedcom.model.Note;
import org.folg.gedcom.model.NoteRef;
import org.folg.gedcom.model.Repository;
import org.folg.gedcom.model.RepositoryRef;
import org.folg.gedcom.model.Source;
import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.TextValue;
import org.gedcomx.common.URI;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.gedcomx.contributor.Agent;
import org.gedcomx.source.CitationField;
import org.gedcomx.source.SourceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SourceDescriptionMapper {
  private static final Logger logger = LoggerFactory.getLogger(CommonMapper.class);

  public void toSourceDescription(Source dqSource, GedcomxConversionResult result) throws IOException {
    Marker sourceContext = ConversionContext.getDetachedMarker(String.format("@%s@ SOUR", dqSource.getId()));
    ConversionContext.addReference(sourceContext);
    try {
      SourceDescription gedxSourceDescription = new SourceDescription();
      gedxSourceDescription.setId(dqSource.getId());

      if (dqSource.getAbbreviation() != null) {
        gedxSourceDescription.setDisplayName(dqSource.getAbbreviation());
      } else {
        gedxSourceDescription.setDisplayName(dqSource.getTitle());
      }

      org.gedcomx.source.SourceCitation citation = new org.gedcomx.source.SourceCitation();
      citation.setCitationTemplate(new ResourceReference(URI.create("gedcom5:source-template")));
      citation.setFields(new ArrayList<CitationField>());
      citation.setValue("");

      if (dqSource.getAuthor() != null) {
        CitationField field = new CitationField();
        field.setName(URI.create("gedcom5:source-template/author"));
        field.setValue(dqSource.getAuthor());
        citation.getFields().add(field);
        citation.setValue(citation.getValue() + (citation.getValue().length() > 0 ? ", " + dqSource.getAuthor() : dqSource.getAuthor()));
      }

      if (dqSource.getTitle() != null) {
        CitationField field = new CitationField();
        field.setName(URI.create("gedcom5:source-template/title"));
        field.setValue(dqSource.getTitle());
        citation.getFields().add(field);
        citation.setValue(citation.getValue() + (citation.getValue().length() > 0 ? ", " + dqSource.getTitle() : dqSource.getTitle()));
      }

      if (dqSource.getPublicationFacts() != null) {
        CitationField field = new CitationField();
        field.setName(URI.create("gedcom5:source-template/publication-facts"));
        field.setValue(dqSource.getPublicationFacts());
        citation.getFields().add(field);
        citation.setValue(citation.getValue() + (citation.getValue().length() > 0 ? ", " + dqSource.getPublicationFacts() : dqSource.getPublicationFacts()));
      }

      if (dqSource.getText() != null) {
        logger.warn(ConversionContext.getContext(), "GEDCOM X does not currently support text extracted from a source.");
      }

      if (dqSource.getRepositoryRef() != null) {
        Marker repoContext = ConversionContext.getDetachedMarker("REPO");
        ConversionContext.addReference(repoContext);
        try {
          RepositoryRef dqRepositoryRef = dqSource.getRepositoryRef();
          if (dqRepositoryRef.getRef() != null) {
            gedxSourceDescription.setMediator(new ResourceReference(URI.create(CommonMapper.getOrganizationEntryName(dqRepositoryRef.getRef()))));
            // TODO: map NOTEs as another note associated with this SourceDescription
          } else {
            String inlineRepoId = dqSource.getId() + ".REPO";
            Agent gedxOrganization = new Agent();
            gedxOrganization.setId(inlineRepoId);
            for (Note dqNote : dqRepositoryRef.getNotes()) {
              org.gedcomx.common.Note gedxNote = new org.gedcomx.common.Note();
              gedxNote.setText(new TextValue(dqNote.getValue()));
              gedxOrganization.addExtensionElement(gedxNote);
            }
            for (NoteRef dqNoteRef : dqRepositoryRef.getNoteRefs()) {
              logger.warn(ConversionContext.getContext(), "Unable to associate a note ({}) with the inline-defined organization ({})", dqNoteRef.getRef(), inlineRepoId);
            }
            result.addOrganization(gedxOrganization, null);
            gedxSourceDescription.setMediator(new ResourceReference(URI.create(CommonMapper.getOrganizationEntryName(inlineRepoId))));
          }

          if (dqRepositoryRef.getCallNumber() != null) {
            CitationField field = new CitationField();
            field.setName(URI.create("gedcom5:source-template/call-number"));
            field.setValue(dqRepositoryRef.getCallNumber());
            citation.getFields().add(field);
            citation.setValue(citation.getValue() + (citation.getValue().length() > 0 ? ", " + dqRepositoryRef.getCallNumber() : dqRepositoryRef.getCallNumber()));
          }
        } finally {
          ConversionContext.removeReference(repoContext);
        }
      }

      if (dqSource.getCallNumber() != null) {
        CitationField field = new CitationField();
        field.setName(URI.create("gedcom5:source-template/call-number"));
        field.setValue(dqSource.getCallNumber());
        citation.getFields().add(field);
        citation.setValue(citation.getValue() + (citation.getValue().length() > 0 ? ", " + dqSource.getCallNumber() : dqSource.getCallNumber()));
      }

      if (citation.getValue().length() > 0) {
        citation.setValue(citation.getValue() + '.');
        gedxSourceDescription.setCitation(citation);
      }

      // dqSource.getMediaType();  // nothing equivalent in the GEDCOM X model

      int cntNotes = dqSource.getNotes().size() + dqSource.getNoteRefs().size();
      if (cntNotes > 0) {
        logger.warn(ConversionContext.getContext(), "Did not process {} notes or references to notes.", cntNotes);
      }

      int cntMedia = dqSource.getMedia().size() + dqSource.getMediaRefs().size();
      if (cntMedia > 0) {
        logger.warn(ConversionContext.getContext(), "Did not process {} media items or references to media items.", cntMedia);
      }

      if (dqSource.getType() != null) {
        Marker nameTypeContext = ConversionContext.getDetachedMarker(dqSource.getTypeTag());
        ConversionContext.addReference(nameTypeContext);
        logger.warn(ConversionContext.getContext(), "Source type ({}) was ignored.", dqSource.getType());
        ConversionContext.removeReference(nameTypeContext);
      }

      if (dqSource.getDate() != null) {
        Marker dateContext = ConversionContext.getDetachedMarker("DATE");
        ConversionContext.addReference(dateContext);
        logger.warn(ConversionContext.getContext(), "Specificaton does not define the meaning of DATE in this context; value ({}) was ignored.", dqSource.getDate());
        ConversionContext.removeReference(dateContext);
      }

      if (dqSource.getReferenceNumber() != null) {
        Marker refnContext = ConversionContext.getDetachedMarker("REFN");
        ConversionContext.addReference(refnContext);
        logger.warn(ConversionContext.getContext(), "User reference number ({}) was ignored.", dqSource.getReferenceNumber());
        ConversionContext.removeReference(refnContext);
      }

      if (dqSource.getRin() != null) {
        logger.warn(ConversionContext.getContext(), "RIN ({}) was ignored.", dqSource.getRin());
      }

      if (dqSource.getUid() != null) {
        Marker uidContext = ConversionContext.getDetachedMarker(dqSource.getUidTag());
        ConversionContext.addReference(uidContext);
        logger.warn(ConversionContext.getContext(), "UID ({}) was ignored.", dqSource.getUid());
        ConversionContext.removeReference(uidContext);
      }

      if (dqSource.getExtensions().size() > 0) {
        for (String extensionCategory : dqSource.getExtensions().keySet()) {
          for (GedcomTag tag : ((List<GedcomTag>)dqSource.getExtension(extensionCategory))) {
            logger.warn(ConversionContext.getContext(), "Unsupported ({}): {}", extensionCategory, tag);
            // DATA tag (and subordinates) in GEDCOM 5.5. SOURCE_RECORD not being looked for or parsed by DallanQ code
          }
        }
      }

      //dqSource.getItalic(); // PAF extension elements; will not process
      //dqSource.getParen();  // PAF extension elements; will not process

      result.addSourceDescription(gedxSourceDescription, CommonMapper.toDate(dqSource.getChange()));
    } finally {
      ConversionContext.removeReference(sourceContext);
    }
  }

  public void toOrganization(Repository dqRepository, GedcomxConversionResult result) throws IOException {
    Marker repositoryContext = ConversionContext.getDetachedMarker(String.format("@%s@ REPO", dqRepository.getId()));
    ConversionContext.addReference(repositoryContext);
    try {
      Agent gedxOrganization = new Agent();

      CommonMapper.populateAgent(gedxOrganization
          , dqRepository.getId()
          , dqRepository.getName()
          , dqRepository.getAddress()
          , dqRepository.getPhone()
          , dqRepository.getFax()
          , dqRepository.getEmail()
          , dqRepository.getWww()
        );

      int cntNotes = dqRepository.getNotes().size() + dqRepository.getNoteRefs().size();
      if (cntNotes > 0) {
        logger.warn(ConversionContext.getContext(), "Did not process {} notes or references to notes.", cntNotes);
      }

      if (dqRepository.getRin() != null) {
        logger.warn(ConversionContext.getContext(), "RIN ({}) was ignored.", dqRepository.getRin());
      }

      if (dqRepository.getValue() != null) {
        logger.warn(ConversionContext.getContext(), "Unexpected repository value ({}) was ignored.", dqRepository.getValue());
      }

      if (dqRepository.getExtensions().size() > 0) {
        for (String extensionCategory : dqRepository.getExtensions().keySet()) {
          for (GedcomTag tag : ((List<GedcomTag>)dqRepository.getExtension(extensionCategory))) {
            logger.warn(ConversionContext.getContext(), "Unsupported ({}): {}", extensionCategory, tag);
          }
        }
      }

      //dqRepository.getAllNotes(); // notes not handled via this method; see getNotes and getNoteRefs

      result.addOrganization(gedxOrganization, CommonMapper.toDate(dqRepository.getChange()));
    } finally {
      ConversionContext.removeReference(repositoryContext);
    }
  }
}