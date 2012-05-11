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

import org.gedcomx.conclusion.Name;
import org.gedcomx.conclusion.NameForm;
import org.gedcomx.conclusion.NamePart;
import org.gedcomx.conclusion.Person;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.gedcomx.types.NamePartType;
import org.gedcomx.types.NameType;

import java.util.ArrayList;
import java.util.List;


public class PersonMapper {
  public PersonMapper() {
  }

  public void toPerson(org.folg.gedcom.model.Person dqPerson, GedcomxConversionResult result) {
    if (dqPerson == null) {
      return;
    }

    Person gedxPerson = new Person();

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
    // Process

    result.addPerson(gedxPerson);
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
    if ((dqName.getExtensions() != null) && (dqName.getExtensions().size() > 0)) {
      // TODO: appears in resource data set from Paul Dupaix
    }
    if ((dqName.getGiven() != null) && (dqName.getGiven().trim().length() > 0)) {
      // TODO: appears in resource data set from Paul Dupaix
    }
    if ((dqName.getMarriedName() != null) && (dqName.getMarriedName().trim().length() > 0)) {
      // TODO: appears in resource data set from Paul Dupaix
    }
    if ((dqName.getMarriedNameTag() != null) && (dqName.getMarriedNameTag().trim().length() > 0)) {
      // TODO: appears in resource data set from Paul Dupaix
    }
    if ((dqName.getMedia() != null) && (dqName.getMedia().size() > 0)) {
      assert false;
    }
    if ((dqName.getMediaRefs() != null) && (dqName.getMediaRefs().size() > 0)) {
      assert false;
    }
    if ((dqName.getNickname() != null) && (dqName.getNickname().trim().length() > 0)) {
      // TODO: appears in resource data set from Paul Dupaix
    }
    if ((dqName.getNoteRefs() != null) && (dqName.getNoteRefs().size() > 0)) {
      assert false;
    }
    if ((dqName.getNotes() != null) && (dqName.getNotes().size() > 0)) {
      // TODO: rare (and probably an error on ancestry's side) in my data
    }
    if ((dqName.getPrefix() != null) && (dqName.getPrefix().trim().length() > 0)) {
      // TODO: appears in resource data set from Paul Dupaix
    }
    if ((dqName.getSourceCitations() != null) && (dqName.getSourceCitations().size() > 0)) {
      // TODO: common case in my data
    }
    if ((dqName.getSuffix() != null) && (dqName.getSuffix().trim().length() > 0)) {
      // TODO: appears in resource data set from Paul Dupaix
    }
    if ((dqName.getSurname() != null) && (dqName.getSurname().trim().length() > 0)) {
      // TODO: appears in resource data set from Paul Dupaix
    }
    if ((dqName.getSurnamePrefix() != null) && (dqName.getSurnamePrefix().trim().length() > 0)) {
      assert false;
    }
    if ((dqName.getType() != null) && (dqName.getType().trim().length() > 0)) {
      // TODO: appears in resource data set from Paul Dupaix
    }
    if ((dqName.getTypeTag() != null) && (dqName.getTypeTag().trim().length() > 0)) {
      assert false;
    }

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
    StringBuilder buf;
    final String dqNameValue = dqName.getValue();
    if (dqNameValue != null) {
      buf = new StringBuilder(dqNameValue);
      int indexOfSlash;
      while ((indexOfSlash = buf.toString().indexOf('/')) >= 0){
        buf.deleteCharAt(indexOfSlash);
      }
    }
    else {
      buf = new StringBuilder();
      appendValue(buf, dqName.getPrefix());
      appendValue(buf, dqName.getGiven());
      appendValue(buf, dqName.getSurnamePrefix());
      appendValue(buf, dqName.getSurname());
      appendValue(buf, dqName.getSuffix());
    }

    return buf.toString().trim();
  }

  private List<NamePart> getNameParts(org.folg.gedcom.model.Name dqName) {
    List<NamePart> nameParts = new ArrayList<NamePart>(4);
    NamePart part;

    part = newNamePartInstance(dqName.getPrefix(), NamePartType.Prefix);
    if (part != null) {
      nameParts.add(part);
    }

    part = newNamePartInstance(dqName.getGiven(), NamePartType.Given);
    if (part != null) {
      nameParts.add(part);
    }

    part = newNamePartInstance(getSurname(dqName), NamePartType.Surname);
    if (part != null) {
      nameParts.add(part);
    }

    part = newNamePartInstance(dqName.getSuffix(), NamePartType.Suffix);
    if (part != null) {
      nameParts.add(part);
    }

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

  private NamePart newNamePartInstance(String value, NamePartType type) {
    NamePart part;

    if (value != null) {
      String[] pieces = value.split(",\\s*");
      String partValue;
      if (pieces.length > 1) {
        StringBuilder partBuilder = new StringBuilder();
        for (String piece : pieces){
          appendValue(partBuilder, piece);
        }
        partValue = partBuilder.toString().trim();
      } else if (pieces.length == 1) {
        partValue = pieces[0].trim();
        if (partValue.length() == 0){
          partValue = null;
        }
      } else {
        partValue = null; // empty string, so don't create a part
      }

      if ((partValue != null) && (partValue.length() > 0)) {
        part = new NamePart();
        part.setKnownType(type);
        part.setText(value);
      } else {
        part = null;
      }
    } else {
      part = null;
    }

    return part;
  }

  private void appendValue(StringBuilder buf, String value) {
    if (value != null) {
      if (buf.length() > 0) {
        buf.append(' ');
      }
      buf.append(value);
    }
  }
}