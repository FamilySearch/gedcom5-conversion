/**
 * Copyright 2011 Intellectual Reserve, Inc. All Rights reserved.
 */
package org.gedcomx.conversion.gedcom.dq55;

import org.folg.gedcom.model.SourceCitation;
import org.folg.gedcom.model.SpouseRef;
import org.gedcomx.common.Note;
import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.conclusion.*;
import org.gedcomx.types.FactType;
import org.gedcomx.types.RelationshipType;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class Util {

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
  public static List<SourceReference> toSources(List<SourceCitation> dqSources) {
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

  /**
   * @param description resource to be saved in zip file
   * @return entry name in the zip file for the given resource
   */
  public static String getDescriptionEntryName(String gedxDescriptionId) {
    return "descriptions/" + gedxDescriptionId;
  }

  /**
   * @param personresource to be saved in zip file
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
