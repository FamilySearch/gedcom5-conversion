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

import org.folg.gedcom.model.EventFact;
import org.gedcomx.conclusion.*;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.gedcomx.types.GenderType;
import org.gedcomx.types.NamePartType;
import org.gedcomx.types.NameType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PersonMapper {

  public PersonMapper() {
  }

  public void toPerson(org.folg.gedcom.model.Person dqPerson, GedcomxConversionResult result) throws IOException {
    if (dqPerson == null) {
      return;
    }

    Person gedxPerson = new Person();
    gedxPerson.setId(dqPerson.getId());

    //////////////////////////////////////////////////////////////////////
    // Process NAMES

    List<Name> gedxNames = new ArrayList<Name>();
    for (org.folg.gedcom.model.Name dqName : dqPerson.getNames()) {
      gedxNames.addAll(toNameList(dqName));
    }

    if (gedxNames.size() > 0) {
      gedxPerson.setNames(gedxNames);
    }


    //////////////////////////////////////////////////////////////////////
    // Process facts

    processFacts(gedxPerson, dqPerson.getEventsFacts(), result);


    //////////////////////////////////////////////////////////////////////
    // Add the person to the conversion results

    result.addPerson(gedxPerson);
  }

  private void processFacts(Person gedxPerson, List<EventFact> facts, GedcomxConversionResult result) throws IOException {
    if(facts == null) {
      return;
    }

    for(EventFact fact : facts) {
      Fact gedxFact = FactMapper.toFact(fact, result);

      if(gedxFact == null) {
        fact.getType();
        if(fact.getTag() != null && fact.getTag().equalsIgnoreCase("SEX")) {
          processSex(gedxPerson, fact);
        }
      }
    }
  }

  private void processSex(Person gedxPerson, EventFact fact) {
    if(gedxPerson.getGender() != null) {
      //TODO warn/log (but continue)
    }

    if(fact.getValue().equalsIgnoreCase("M")) {
      gedxPerson.setGender(new Gender(GenderType.Male));
    }
    else if(fact.getValue().equalsIgnoreCase("F")) {
      gedxPerson.setGender(new Gender(GenderType.Female));
    }
    else if(fact.getValue().equalsIgnoreCase("U")) {
      gedxPerson.setGender(new Gender(GenderType.Unknown));
    }
    else  {
      //TODO warn/log
    }
  }

  private List<Name> toNameList(org.folg.gedcom.model.Name dqName) {
    List<Name> nameList = new ArrayList<Name>();

    if (dqName == null) {
      return nameList;
    }

    Name gedxName = new Name();
    //gedxName.setId(); // no equivalent; probably system dependent anyway

    gedxName.setPrimaryForm(new NameForm());
    gedxName.getPrimaryForm().setFullText(getNameValue(dqName));
    List<NamePart> parts = getNameParts(dqName);
    if (parts != null) {
      gedxName.getPrimaryForm().setParts(parts);
    }
    nameList.add(gedxName);

    if (dqName.getNickname() != null) {
      Name gedxNickname = new Name();
      gedxNickname.setKnownType(NameType.Nickname);
      NameForm nickname = new NameForm();
      nickname.setFullText(dqName.getNickname()); // TODO: append the surname?  might end up being a bad idea
      gedxNickname.setPrimaryForm(nickname);
      nameList.add(gedxNickname);
    }

    if (dqName.getMarriedName() != null) {
      Name gedxMarriedName = new Name();
      gedxMarriedName.setKnownType(NameType.MarriedName);
      NameForm marriedName = new NameForm();
      marriedName.setFullText(dqName.getMarriedName()); // TODO: is this just a surname?
      gedxMarriedName.setPrimaryForm(marriedName);
      nameList.add(gedxMarriedName);
    }

    if (dqName.getAka() != null) {
      Name gedxAka = new Name();
      gedxAka.setKnownType(NameType.AlsoKnownAs);
      NameForm alias = new NameForm();
      alias.setFullText(dqName.getMarriedName()); // TODO: is this just a surname?
      gedxAka.setPrimaryForm(alias);
      nameList.add(gedxAka);
    }

    // dqName.getAkaTag() // GEDCOM 5.5 formatting data that we will not preserve

//    if ((dqName.getExtensions() != null) && (dqName.getExtensions().size() > 0)) {
//      // TODO: appears in resource data set from Paul Dupaix
//    }
//    if ((dqName.getMarriedName() != null) && (dqName.getMarriedName().trim().length() > 0)) {
//      // TODO: appears in resource data set from Paul Dupaix
//    }
//    if ((dqName.getMarriedNameTag() != null) && (dqName.getMarriedNameTag().trim().length() > 0)) {
//      // TODO: appears in resource data set from Paul Dupaix
//    }
//    if ((dqName.getMedia() != null) && (dqName.getMedia().size() > 0)) {
//      assert false;
//    }
//    if ((dqName.getMediaRefs() != null) && (dqName.getMediaRefs().size() > 0)) {
//      assert false;
//    }
//    if ((dqName.getNickname() != null) && (dqName.getNickname().trim().length() > 0)) {
//      // TODO: appears in resource data set from Paul Dupaix
//    }
//    if ((dqName.getNoteRefs() != null) && (dqName.getNoteRefs().size() > 0)) {
//      assert false;
//    }
//    if ((dqName.getNotes() != null) && (dqName.getNotes().size() > 0)) {
//      // TODO: rare (and probably an error on ancestry's side) in my data
//    }
//    if ((dqName.getSourceCitations() != null) && (dqName.getSourceCitations().size() > 0)) {
//      // TODO: common case in my data
//    }
//    if ((dqName.getSurnamePrefix() != null) && (dqName.getSurnamePrefix().trim().length() > 0)) {
//      assert false;
//    }
//    if ((dqName.getType() != null) && (dqName.getType().trim().length() > 0)) {
//      // TODO: appears in resource data set from Paul Dupaix
//    }
//    if ((dqName.getTypeTag() != null) && (dqName.getTypeTag().trim().length() > 0)) {
//      assert false;
//    }

    //dqName.getAllMedia(); // requires dq's Gedcom instance
    //dqName.getAllNotes(); // requires dq's Gedcom instance

//    gedxName.setAlternateForms();
//    gedxName.setAttribution();
//    gedxName.setExtensionElements();
//    gedxName.setId();
//    gedxName.setKnownType();
//    gedxName.setPreferred();
//    gedxName.setPrimaryForm();
//    gedxName.setSources();
//    gedxName.setType();

    return nameList;
  }

  private String getNameValue(org.folg.gedcom.model.Name dqName) {
    String value = dqName.getValue();
    if (value == null) {
      return null;
    }

    int indexOfSlash;
    while ((indexOfSlash = value.indexOf('/')) >= 0){
      // If both characters around the slash are not a space, replace the slash with a space, otherwise just remove it.
      boolean replaceWithSpace = false;
      if(indexOfSlash > 0 && indexOfSlash < value.length() - 1) {
        char c = value.charAt(indexOfSlash - 1);
        if(c != ' ') {
          c = value.charAt(indexOfSlash + 1);
          if(c != ' ') {
            replaceWithSpace = true;
          }
        }
      }
      if(replaceWithSpace) {
        value = replaceCharAt(value, indexOfSlash, ' ');
      }
      else {
        value = deleteCharAt(value, indexOfSlash);
      }
    }
    return value.trim();
  }

  private List<NamePart> getNameParts(org.folg.gedcom.model.Name dqName) {
    List<NamePart> nameParts = new ArrayList<NamePart>(4);
    NamePart part;

    nameParts.addAll(newNamePartInstances(dqName.getPrefix(), NamePartType.Prefix));
    nameParts.addAll(newNamePartInstances(dqName.getGiven(), NamePartType.Given));
    nameParts.addAll(newNamePartInstances(getSurname(dqName), NamePartType.Surname));
    nameParts.addAll(newNamePartInstances(dqName.getSuffix(), NamePartType.Suffix));

    return nameParts.size() > 0 ? nameParts : null;
  }

  private String getSurname(org.folg.gedcom.model.Name dqName) {
    if ((dqName == null) || ((dqName.getValue() == null) && (dqName.getSurname() == null))) {
      return null;
    }

    String value = dqName.getSurname();
    if (value == null) {
      value = dqName.getValue();

      int slashIndex = value.indexOf('/');
      if (slashIndex >= 0) {
        StringBuilder builder = new StringBuilder(value);
        builder.replace(0, slashIndex + 1, "");
        value = builder.toString();
        slashIndex = value.indexOf('/');
        if (slashIndex >= 0) {
          builder.replace(slashIndex, builder.length(), "");
        }
        value = builder.toString().trim();
      } else {
        value = null;
      }
    }

    return value;
  }

  private List<NamePart> newNamePartInstances(String value, NamePartType type) {
    if(value == null) {
      return Collections.emptyList();
    }

    ArrayList<NamePart> nameParts = new ArrayList<NamePart>();

    String[] pieces = value.split(",\\s*");
    for (String piece : pieces){
      piece = piece.trim();
      if(!piece.equals("")) {
        NamePart namePart = new NamePart();
        namePart.setKnownType(type);
        namePart.setText(piece);
        nameParts.add(namePart);
      }
    }

    return nameParts;
  }

  protected String deleteCharAt(String value, int index) {
    if(value == null || index < 0 || index >= value.length()) {
      return value;
    }

    if(index == value.length() - 1) {
      return value.substring(0, index);
    }
    else if(index == 0) {
      return value.substring(index + 1);
    }
    else {
      return value.substring(0, index) + value.substring(index + 1);
    }
  }

  protected String replaceCharAt(String value, int index, char c) {
    if(value == null || index < 0 || index >= value.length()) {
      return value;
    }

    if(index == value.length() - 1) {
      return value.substring(0, index) + c;
    }
    else if(index == 0) {
      return c + value.substring(index + 1);
    }
    else {
      return value.substring(0, index) + c + value.substring(index + 1);
    }
  }
}