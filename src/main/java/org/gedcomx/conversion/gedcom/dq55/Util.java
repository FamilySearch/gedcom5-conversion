/**
 * Copyright 2011 Intellectual Reserve, Inc. All Rights reserved.
 */
package org.gedcomx.conversion.gedcom.dq55;

import org.folg.gedcom.model.SourceCitation;
import org.gedcomx.common.Note;
import org.gedcomx.conclusion.Date;
import org.gedcomx.conclusion.Fact;
import org.gedcomx.conclusion.Place;
import org.gedcomx.conclusion.SourceReference;
import org.gedcomx.types.FactType;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class Util {

  public static List<Note> createNotes(List<org.folg.gedcom.model.Note> dqNotes) {
    List<Note> notes = new ArrayList<Note>(dqNotes.size());
    for (org.folg.gedcom.model.Note dqNote : dqNotes) {
      Note note = new Note();
      note.setText(dqNote.getValue());
      notes.add( note );
    }
    return notes;
  }

  public static List<SourceReference> createSources(List<SourceCitation> dqSources) {
    List<SourceReference> sourceReferences = new ArrayList<SourceReference>(dqSources.size());
    for (org.folg.gedcom.model.SourceCitation dqSource : dqSources) {
      SourceReference sourceReference = new SourceReference();
      //TODO
      sourceReferences.add(sourceReference);
    }
    return sourceReferences;
  }

  public static Fact createFact(FactType knownType, String dateStr, String placeStr) {
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
}
