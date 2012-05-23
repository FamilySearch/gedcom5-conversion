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

import org.folg.gedcom.model.Change;
import org.folg.gedcom.model.DateTime;
import org.folg.gedcom.model.SourceCitation;
import org.folg.gedcom.model.SpouseRef;
import org.gedcomx.common.Attribution;
import org.gedcomx.common.Note;
import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.conclusion.*;
import org.gedcomx.conclusion.Date;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.gedcomx.metadata.dc.DublinCoreDescriptionDecorator;
import org.gedcomx.metadata.dc.ObjectFactory;
import org.gedcomx.metadata.rdf.Description;
import org.gedcomx.metadata.rdf.RDFLiteral;
import org.gedcomx.metadata.rdf.RDFValue;
import org.gedcomx.types.ConfidenceLevel;
import org.gedcomx.types.FactType;
import org.gedcomx.types.RelationshipType;
import org.gedcomx.types.TypeReference;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 */
public class CommonMapper {
  private static ObjectFactory objectFactory = new ObjectFactory();

  /**
   * Create a list of GedcomX Notes based on the ged5 notes.
   * @param dqNotes Gedcom 5 notes
   * @return GedcomX Notes
   */
  public static List<Note> toNotes(List<org.folg.gedcom.model.Note> dqNotes) {
    List<Note> notes = new ArrayList<Note>(dqNotes.size());
    for (org.folg.gedcom.model.Note dqNote : dqNotes) {
      Note note = new Note();
      note.setText(dqNote.getValue());
      notes.add( note );
    }
    return notes;
  }

  /**
   * Create a list of GedcomX SourceReference based on the ged5 SourceCitations.
   * @param dqSources Gedcom 5 source citations
   * @return GedcomX SourceReferences
   */
  public static List<SourceReference> toSourcesAndSourceReferences(List<SourceCitation> dqSources, GedcomxConversionResult result) throws IOException {
    List<SourceReference> sourceReferences = new ArrayList<SourceReference>(dqSources.size());

    for (org.folg.gedcom.model.SourceCitation dqSource : dqSources) {
      if ((dqSource.getRef() != null) || ((dqSource.getValue() != null) || (dqSource.getText() != null) || (dqSource.getQuality() != null))) { // TODO: may need to update the condition to include notes or media
        Description gedxSourceDescription = new Description();
        DublinCoreDescriptionDecorator gedxDecoratedSourceDescription = DublinCoreDescriptionDecorator.newInstance(gedxSourceDescription);
        gedxSourceDescription.setId(Long.toHexString(SequentialIdentifierGenerator.getNextId()));

        result.addDescription(gedxSourceDescription, null);

        String entryName = CommonMapper.getDescriptionEntryName(gedxSourceDescription.getId());
        SourceReference gedxSourceReference = new SourceReference();
        gedxSourceReference.setDescription(new ResourceReference());
        gedxSourceReference.getDescription().setResource(URI.create(entryName));

        if (dqSource.getRef() != null) {
          gedxDecoratedSourceDescription.partOf(new RDFValue(CommonMapper.getDescriptionEntryName(dqSource.getRef())));

          if (dqSource.getPage() != null) {
            gedxDecoratedSourceDescription.description(new RDFValue(dqSource.getPage()));
          }

          if (dqSource.getDate() != null) {
            final String parsePattern = "d MMM yyyy";
            try {
              java.util.Date date = parseDateString(parsePattern, dqSource.getDate());
              gedxDecoratedSourceDescription.created(date);
            }
            catch (Throwable ex) {
              // something went wrong, so probably does not conform to the standard; we will skip it
              // TODO: log?
            }
          }
        } else if (dqSource.getValue() != null) {
          gedxDecoratedSourceDescription.description(new RDFValue(dqSource.getValue()));
        }

        if (dqSource.getText() != null) {
          // dqSource.getText(); // see GEDCOM X issue 121 // TODO: address when the associated issue is resolved; log for now
        }

        if (dqSource.getQuality() != null) {
          Attribution attribution = new Attribution();
          attribution.setConfidence(new TypeReference<ConfidenceLevel>(toConfidenceLevel(dqSource.getQuality())));
          gedxSourceReference.setAttribution(attribution);
        }

        // TODO: log?
        //dqSource.getNotes();
        //dqSource.getNoteRefs();
        //dqSource.getMedia();
        //dqSource.getMediaRefs();

        sourceReferences.add(gedxSourceReference);
      } else {
        // TODO: DallanQ produced a source without any data to convert; log?
      }
    }

    return sourceReferences.size() > 0 ? sourceReferences : null;
  }

  public static void toChangeDescription(Change dqChange, String entryNameForDescribedObject, GedcomxConversionResult result) {
    if (dqChange != null) {
      try {
        java.util.Date date = toDate(dqChange);

        RDFLiteral lastModified = new RDFLiteral(date);

        Description gedxDescription = new Description();
        gedxDescription.setAbout(URI.create(entryNameForDescribedObject));
        gedxDescription.addExtensionElement(objectFactory.createModifiedElement(lastModified));

        result.addDescription(gedxDescription, date);
      } catch (Throwable ex) {
        // something went wrong, so probably does not conform to the standard; we will skip it
        // TODO: log?
      }
    }
  }

  public static java.util.Date toDate(Change dqChange) {
    if (dqChange == null)
      return null;
    DateTime dateTime = dqChange.getDateTime();
    String parsePattern = "d MMM yy";
    if (dateTime.getTime() != null) {
      parsePattern += " HH:mm:ss.SSS";
    }

    String dateTimeString = dateTime.getValue();
    if (dateTime.getTime() != null) {
      dateTimeString += ' ' + dateTime.getTime();
    }
    try {
      return parseDateString(parsePattern, dateTimeString);
    }
    catch (ParseException e) {
      //TODO: logWarning
    }
    return null;
  }

  public static ConfidenceLevel toConfidenceLevel(String dqQuality) {
    ConfidenceLevel confidenceLevel = null;
    if ("3".equals(dqQuality)) {
      confidenceLevel = ConfidenceLevel.Certainly;
    } else if ("2".equals(dqQuality)) {
      confidenceLevel = ConfidenceLevel.Possibly;
    } else if ("1".equals(dqQuality)) {
      confidenceLevel = ConfidenceLevel.Apparently;
    } else if ("0".equals(dqQuality)) {
      confidenceLevel = ConfidenceLevel.Perhaps;
    }
    return confidenceLevel;
  }

  public static java.util.Date parseDateString (String parsePattern, String value) throws ParseException {
    DateFormat dateFormat = DateFormat.getDateTimeInstance();
    ((SimpleDateFormat)dateFormat).applyPattern(parsePattern);
    return dateFormat.parse(value);
  }

  /**
   * @param gedxDescriptionId resource to be saved in zip file
   * @return entry name in the zip file for the given resource
   */
  public static String getDescriptionEntryName(String gedxDescriptionId) {
    return "descriptions/" + gedxDescriptionId;
  }

  /**
   * @param gedxPersonId to be saved in zip file
   * @return entry name in the zip file for the given resource
   */
  public static String getPersonEntryName(String gedxPersonId) {
    return "persons/" + gedxPersonId;
  }

  public static String getRelationshipEntryName(String id) {
    return "relationships/" + id;
  }

  public static String getOrganizationEntryName(String id) {
    return "organizations/" + id;
  }

  /**
   * Creates a GedcomX relationship from gedcom 5 objects.
   * @param person1
   * @param person2
   * @param relationshipType
   * @return relationship that was added
   */
  public static Relationship toRelationship(SpouseRef person1, SpouseRef person2, RelationshipType relationshipType) {
    Relationship relationship = new Relationship();
    relationship.setKnownType(relationshipType);
    relationship.setPerson1(toReference(person1));
    relationship.setPerson2(toReference(person2));
    relationship.setId(person1.getRef() + "-" + person2.getRef());
    return relationship;
  }

  /**
   * Finds the gedcomx person corresponding to the ged5 person and creates a GedcomX reference to it.
   * @param ged5PersonRef gedcom 5 person
   * @return
   */
  public static ResourceReference toReference(SpouseRef ged5PersonRef) {
    ResourceReference reference = new ResourceReference();
    String gedxPersonId = ged5PersonRef.getRef(); // gedx id is same as ged5 id
    reference.setResource( new URI(getPersonEntryName(gedxPersonId)));
    return reference;
  }
}
