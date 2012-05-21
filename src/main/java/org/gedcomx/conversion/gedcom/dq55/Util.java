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
import org.gedcomx.common.Note;
import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.conclusion.*;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.gedcomx.metadata.dc.ObjectFactory;
import org.gedcomx.metadata.rdf.Description;
import org.gedcomx.metadata.rdf.RDFLiteral;
import org.gedcomx.types.FactType;
import org.gedcomx.types.RelationshipType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class Util {
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
  public static List<SourceReference> toSourcesAndSourceReferences(List<SourceCitation> dqSources, GedcomxConversionResult result) {
    List<SourceReference> sourceReferences = new ArrayList<SourceReference>(dqSources.size());
    for (org.folg.gedcom.model.SourceCitation dqSource : dqSources) {
      SourceReference sourceReference = new SourceReference();
      //TODO
      sourceReferences.add(sourceReference);
    }
    return sourceReferences;
  }

  /**
   * Create a GedcomX fact
   * @param knownType fact type
   * @param dateStr date (can be null)
   * @param placeStr place (can be null)
   * @return GedcomX fact
   */
  public static Fact toFact(FactType knownType, String dateStr, String placeStr) {
    Fact fact = new Fact();
    fact.setKnownType(knownType);

    if (dateStr != null) {
      Date date = new Date();
      date.setOriginal(dateStr);
      fact.setDate(date);
    }

    if (placeStr != null) {
      Place place = new Place();
      place.setOriginal(placeStr);
      fact.setPlace(place);
    }
    return fact;
  }

  public static void toChangeDescription(Change dqRepositoryChange, String aboutObjId, GedcomxConversionResult result) {
    if (dqRepositoryChange != null) {
      try {
        DateTime dateTime = dqRepositoryChange.getDateTime();
        String parsePattern = "d MMM yyyy";
        if (dateTime.getTime() != null) {
          parsePattern += " HH:mm:ss.SSS";
        }
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        ((SimpleDateFormat)dateFormat).applyPattern(parsePattern);

        String dateTimeString = dateTime.getValue();
        if (dateTime.getTime() != null) {
          dateTimeString += ' ' + dateTime.getTime();
        }
        java.util.Date date = dateFormat.parse(dateTimeString);

        RDFLiteral lastModified = new RDFLiteral(date);

        Description gedxRepositoryRecordDescription = new Description();
        gedxRepositoryRecordDescription.setAbout(URI.create(aboutObjId));
        gedxRepositoryRecordDescription.addExtensionElement(objectFactory.createModifiedElement(lastModified));

        result.addDescription(gedxRepositoryRecordDescription);
      } catch (Throwable ex) {
        // something went wrong, so probably does not conform to the standard; we will skip it
      }
    }
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
    relationship.setPerson1( toReference(person1) );
    relationship.setPerson2( toReference(person2) );
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
