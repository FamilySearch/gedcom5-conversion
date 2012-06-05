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
import org.gedcomx.metadata.dc.DublinCoreDescriptionDecorator;
import org.gedcomx.metadata.foaf.Organization;
import org.gedcomx.metadata.rdf.Description;
import org.gedcomx.metadata.rdf.RDFLiteral;
import org.gedcomx.metadata.rdf.RDFValue;
import org.gedcomx.types.ResourceType;
import org.gedcomx.types.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.io.IOException;
import java.util.List;


public class SourceDescriptionMapper {
  private static final Logger logger = LoggerFactory.getLogger(CommonMapper.class);

  public void toSourceDescription(Source dqSource, GedcomxConversionResult result) throws IOException {
    Marker sourceContext = ConversionContext.getDetachedMarker(String.format("@%s@ SOUR", dqSource.getId()));
    ConversionContext.addReference(sourceContext);
    try {
      Description gedxSourceDescription = new Description();
      DublinCoreDescriptionDecorator gedxDecoratedSourceDescription = DublinCoreDescriptionDecorator.newInstance(gedxSourceDescription);
      gedxSourceDescription.setId(dqSource.getId());

      if (dqSource.getAuthor() != null) {
        gedxDecoratedSourceDescription.creator(new RDFValue(dqSource.getAuthor()));
      }

      if (dqSource.getTitle() != null) {
        gedxDecoratedSourceDescription.title(new RDFLiteral(dqSource.getTitle()));
      }

      if (dqSource.getAbbreviation() != null) {
        gedxDecoratedSourceDescription.alternative(new RDFLiteral(dqSource.getAbbreviation()));
      }

      if (dqSource.getPublicationFacts() != null) {
        gedxDecoratedSourceDescription.description(new RDFValue(dqSource.getPublicationFacts()));
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
            gedxDecoratedSourceDescription.partOf(new RDFValue(CommonMapper.getOrganizationEntryName(dqRepositoryRef.getRef())));
            // TODO: map NOTEs as another note associated with this SourceDescription
          } else {
            String inlineRepoId = dqSource.getId() + ".REPO";
            Organization gedxOrganization = new Organization();
            gedxOrganization.setId(inlineRepoId);
            for (Note dqNote : dqRepositoryRef.getNotes()) {
              DublinCoreDescriptionDecorator.newInstance(gedxOrganization).description(new RDFValue(dqNote.getValue()));
            }
            for (NoteRef dqNoteRef : dqRepositoryRef.getNoteRefs()) {
              logger.warn(ConversionContext.getContext(), "Unable to associate a note ({}) with the inline-defined organization ({})", dqNoteRef.getRef(), inlineRepoId);
            }
            result.addOrganization(gedxOrganization, null);
            gedxDecoratedSourceDescription.partOf(new RDFValue(CommonMapper.getOrganizationEntryName(inlineRepoId)));
          }

          if (dqRepositoryRef.getCallNumber() != null) {
            gedxDecoratedSourceDescription.identifier(new RDFLiteral(dqRepositoryRef.getCallNumber()));
          }

          TypeReference<ResourceType> mediaTypeRef = mapToKnownResourceType(dqRepositoryRef.getMediaType());
          if (mediaTypeRef != null) {
            gedxSourceDescription.setType(mediaTypeRef);
          }
        } finally {
          ConversionContext.removeReference(repoContext);
        }
      }

      if (dqSource.getCallNumber() != null) {
        gedxDecoratedSourceDescription.identifier(new RDFLiteral(dqSource.getCallNumber()));
      }

      TypeReference<ResourceType> mediaTypeRef = mapToKnownResourceType(dqSource.getMediaType());
      if (mediaTypeRef != null) {
        gedxSourceDescription.setType(mediaTypeRef);
      }

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

      result.addDescription(gedxSourceDescription, CommonMapper.toDate(dqSource.getChange()));
    } finally {
      ConversionContext.removeReference(sourceContext);
    }
  }

  public void toOrganization(Repository dqRepository, GedcomxConversionResult result) throws IOException {
    Marker repositoryContext = ConversionContext.getDetachedMarker(String.format("@%s@ REPO", dqRepository.getId()));
    ConversionContext.addReference(repositoryContext);
    try {
      Organization gedxOrganization = new Organization();

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

  private TypeReference<ResourceType> mapToKnownResourceType(String mediaType) {
    Marker mediaTypeContext = ConversionContext.getDetachedMarker("MEDI");
    ConversionContext.addReference(mediaTypeContext);

    TypeReference<ResourceType> resourceTypeRef;

    if ("audio".equalsIgnoreCase(mediaType)) {
      resourceTypeRef = new TypeReference<ResourceType>(ResourceType.Sound);
    } else if ("book".equalsIgnoreCase(mediaType)) {
      resourceTypeRef = new TypeReference<ResourceType>(ResourceType.PhysicalObject);
    } else if ("card".equalsIgnoreCase(mediaType)) {
      resourceTypeRef = new TypeReference<ResourceType>(ResourceType.PhysicalObject);
    } else if ("electronic".equalsIgnoreCase(mediaType)) {
      resourceTypeRef = null;
    } else if ("fiche".equalsIgnoreCase(mediaType)) {
      resourceTypeRef = new TypeReference<ResourceType>(ResourceType.PhysicalObject);
    } else if ("film".equalsIgnoreCase(mediaType)) {
      resourceTypeRef = new TypeReference<ResourceType>(ResourceType.PhysicalObject);
    } else if ("magazine".equalsIgnoreCase(mediaType)) {
      resourceTypeRef = new TypeReference<ResourceType>(ResourceType.PhysicalObject);
    } else if ("manuscript".equalsIgnoreCase(mediaType)) {
      resourceTypeRef = new TypeReference<ResourceType>(ResourceType.PhysicalObject);
    } else if ("map".equalsIgnoreCase(mediaType)) {
      resourceTypeRef = new TypeReference<ResourceType>(ResourceType.PhysicalObject);
    } else if ("newspaper".equalsIgnoreCase(mediaType)) {
      resourceTypeRef = new TypeReference<ResourceType>(ResourceType.PhysicalObject);
    } else if ("photo".equalsIgnoreCase(mediaType)) {
      resourceTypeRef = new TypeReference<ResourceType>(ResourceType.StillImage);
    } else if ("tombstone".equalsIgnoreCase(mediaType)) {
      resourceTypeRef = new TypeReference<ResourceType>(ResourceType.PhysicalObject);
    } else if ("video".equalsIgnoreCase(mediaType)) {
      resourceTypeRef = new TypeReference<ResourceType>(ResourceType.MovingImage);
    } else {
      if (mediaType != null) {
        logger.warn(ConversionContext.getContext(), "Unrecognized media type value: {}", mediaType);
      }
      resourceTypeRef = null;
    }

    ConversionContext.removeReference(mediaTypeContext);

    return resourceTypeRef;
  }
}