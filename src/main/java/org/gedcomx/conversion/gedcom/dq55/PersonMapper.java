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
import org.gedcomx.common.FormalValue;
import org.gedcomx.conclusion.*;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.gedcomx.types.FactType;
import org.gedcomx.types.GenderType;
import org.gedcomx.types.NamePartType;
import org.gedcomx.types.NameType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class PersonMapper {
  static final HashMap<String, FactType> factMap = new HashMap<String, FactType>();

  static {
    // Facts (Short and long tag names, from the standard)
    factMap.put("CAST", FactType.CasteName);  factMap.put("CASTE", FactType.CasteName);
    factMap.put("DSCR", FactType.PhysicalDescription);  factMap.put("PHY_DESCRIPTION", FactType.PhysicalDescription);
    factMap.put("EDUC", FactType.ScholasticAchievement);  factMap.put("EDUCATION", FactType.ScholasticAchievement);
    factMap.put("IDNO", FactType.NationalId);  factMap.put("IDENT_NUMBER", FactType.NationalId);
    factMap.put("NATI", FactType.NationalOrigin);  factMap.put("NATIONALITY", FactType.NationalOrigin);
    factMap.put("NCHI", FactType.CountOfChildren);  factMap.put("CHILDREN_COUNT", FactType.CountOfChildren);
    factMap.put("NMR", FactType.CountOfMarriages);  factMap.put("MARRIAGE_COUNT", FactType.CountOfMarriages);
    factMap.put("OCCU", FactType.Occupation);  factMap.put("OCCUPATION", FactType.Occupation);
    factMap.put("PROP", FactType.Possessions);  factMap.put("PROPERTY", FactType.Possessions);
    factMap.put("RELI", FactType.ReligiousAffiliation);  factMap.put("RELIGION", FactType.ReligiousAffiliation);
    factMap.put("RESI", FactType.Dwelling);  factMap.put("RESIDENCE", FactType.Dwelling);
    factMap.put("SSN", FactType.SocialSecurityNumber);  factMap.put("SOC_SEC_NUMBER", FactType.SocialSecurityNumber);
    factMap.put("TITL", FactType.TitleOfNobility);  factMap.put("TITLE", FactType.TitleOfNobility);
    //TODO Handle custom
//    factMap.put("FACT", FactType.);

    // Events (Short and long tag names, from the standard)
    factMap.put("ADOP", FactType.Adopted);  factMap.put("ADOPTION", FactType.Adopted);
    factMap.put("BAPM", FactType.Baptism);  factMap.put("BAPTISM", FactType.Baptism);
    factMap.put("BARM", FactType.BarMitzvah);  factMap.put("BAR_MITZVAH", FactType.BarMitzvah);
    factMap.put("BASM", FactType.BatMitzvah);  factMap.put("BAS_MITZVAH", FactType.BatMitzvah);
    factMap.put("BATM", FactType.BatMitzvah);  factMap.put("BAT_MITZVAH", FactType.BatMitzvah);
    factMap.put("BLES", FactType.Blessing);  factMap.put("BLESSING", FactType.Blessing);
    factMap.put("BIRT", FactType.Birth);  factMap.put("BIRTH", FactType.Birth);
    factMap.put("BURI", FactType.Burial);  factMap.put("BURIAL", FactType.Burial);
    factMap.put("CENS", FactType.Census);  factMap.put("CENSUS", FactType.Census);
    factMap.put("CHR", FactType.Christening);  factMap.put("CHRISTENING", FactType.Christening);
    factMap.put("CHRA", FactType.AdultChristening);  factMap.put("ADULT_CHRISTNG", FactType.AdultChristening);
    factMap.put("CONF", FactType.Confirmation);  factMap.put("CONFIRMATION", FactType.Confirmation);
    factMap.put("CREM", FactType.Cremation);  factMap.put("CREMATION", FactType.Cremation);
    factMap.put("DEAT", FactType.Death);  factMap.put("DEATH", FactType.Death);
    factMap.put("EMIG", FactType.Emigration);  factMap.put("EMIGRATION", FactType.Emigration);
    factMap.put("FCOM", FactType.FirstCommunion);  factMap.put("FIRST_COMMUNION", FactType.FirstCommunion);
    factMap.put("GRAD", FactType.Graduation);  factMap.put("GRADUATION", FactType.Graduation);
    factMap.put("IMMI", FactType.Immigration);  factMap.put("IMMIGRATION", FactType.Immigration);
    factMap.put("ORDN", FactType.Ordination);  factMap.put("ORDINATION", FactType.Ordination);
    factMap.put("NATU", FactType.Naturalization);  factMap.put("NATURALIZATION", FactType.Naturalization);
    factMap.put("PROB", FactType.Probate);  factMap.put("PROBATE", FactType.Probate);
    factMap.put("RETI", FactType.Retirement);  factMap.put("RETIREMENT", FactType.Retirement);
    factMap.put("WILL", FactType.Will);  /* long name is the same */
    //TODO Handle custom
//    factMap.put("EVEN", FactType.Christening);

    // Non-standard tags
    factMap.put("CIRC", FactType.Circumcision);
    factMap.put("CITN", FactType.Citizenship);
    factMap.put("BLESS", FactType.Blessing);
    factMap.put("BLSL", FactType.Blessing); // Blessing - LDS
    factMap.put("DWEL", FactType.Dwelling);
    factMap.put("_EXCM", FactType.Excommunication);
    factMap.put("EXCO", FactType.Excommunication);
    factMap.put("_FNRL", FactType.Funeral);
    factMap.put("_FUN", FactType.Funeral);
    factMap.put("ILLN", FactType.Illness);
    factMap.put("ILL", FactType.Illness);
    factMap.put("_INTE", FactType.Interment);
    factMap.put("LVG", FactType.Living);
    factMap.put("LVNG", FactType.Living);
    factMap.put("MIL", FactType.MilitaryService);
    factMap.put("_MIL", FactType.MilitaryService);
    factMap.put("MILI", FactType.MilitaryService);
    factMap.put("_MILI", FactType.MilitaryService);
    factMap.put("MILT", FactType.MilitaryService);
    factMap.put("_MILT", FactType.MilitaryService);
    factMap.put("_MILITARY_SERVICE", FactType.MilitaryService);
    factMap.put("MISE", FactType.MilitaryService);
    factMap.put("_MISE", FactType.MilitaryService);
    factMap.put("_MILTID", FactType.MilitaryService);
    factMap.put("MILA", FactType.MilitaryAward);
    factMap.put("MILD", FactType.MilitaryDischarge);
    factMap.put("_MISN ", FactType.Mission);
    factMap.put("MISN", FactType.Mission);
    factMap.put("MOVE", FactType.Move);
    factMap.put("ORDI", FactType.Ordinance); // In 5.5 standard
    factMap.put("ORDL", FactType.Ordination); // Ordination - LDS
    factMap.put("DPRT", FactType.PortOfDeparture);
    factMap.put("DEPA", FactType.PortOfDeparture);
    factMap.put("RESIR", FactType.Dwelling);
    factMap.put("RACE", FactType.Race);
    factMap.put("STLB", FactType.Stillborn);
    factMap.put("STIL", FactType.Stillborn);
    factMap.put("BAP", FactType.Baptism);
    factMap.put("BAPT", FactType.Baptism);
    factMap.put("_NAMS", FactType.Namesake);
    factMap.put("SOC_", FactType.SocialSecurityNumber);
  }

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
    // Process facts

    processFacts(gedxPerson, dqPerson.getEventsFacts());


    //////////////////////////////////////////////////////////////////////
    // Add the person to the conversion results

    result.addPerson(gedxPerson, dqPerson.getId());
  }

  private void processFacts(Person gedxPerson, List<EventFact> facts) {
    if(facts == null) {
      return;
    }

    for(EventFact fact : facts) {
      fact.getType();
      if(fact.getTag() == null) {
        //TODO warn/log
      }
      else if(fact.getTag().equalsIgnoreCase("SEX")) {
        processSex(gedxPerson, fact);
      }
      else {
        String upperTag = fact.getTag().trim().toUpperCase();
        FactType factType = factMap.get(upperTag);
        if(factType != null) {

          String factValue = fact.getValue();
          if(factValue != null) {
            //TODO OK to trim?
            factValue = factValue.trim();
            if(factValue.equals("")) {
              factValue = null;
            }
          }

          String factPlace = fact.getPlace();
          if(factPlace != null) {
            //TODO OK to trim?
            factPlace = factPlace.trim();
            if(factPlace.equals("")) {
              factPlace = null;
            }
          }

          String factDate = fact.getDate();
          if(factDate != null) {
            //TODO OK to trim?
            factDate = factDate.trim();
            if(factDate.equals("")) {
              factDate = null;
            }
          }

          if(factDate == null && factPlace == null && factValue == null) {
            //TODO log/warn
          }
          else {
            Fact gedxFact = new Fact();
            gedxFact.setKnownType(factType);

            if(factDate != null) {
              Date date = new Date();
              date.setOriginal(factDate);
              FormalValue formalValue = new FormalValue();
              formalValue.setText(factDate);
              date.setFormal(formalValue);
              gedxFact.setDate(date);
            }

            if(factPlace != null) {
              Place place = new Place();
              place.setOriginal(factPlace);
              FormalValue formalValue = new FormalValue();
              formalValue.setText(factPlace);
              place.setFormal(formalValue);
              gedxFact.setPlace(place);
            }

            if(factValue != null) {
              gedxFact.setOriginal(factValue);
              FormalValue formalValue = new FormalValue();
              formalValue.setText(factValue);
              gedxFact.setFormal(formalValue);
            }

            gedxPerson.addFact(gedxFact);
          }
        }
      }
    }
  }

  private void processSex(Person gedxPerson, EventFact fact) {
    if(gedxPerson.getGender() != null) {
      //TODO warn/log (but continue)
    }

    if(fact.getValue().equalsIgnoreCase("M")) {
      //TODO Use gender constructor that takes type
      Gender gender = new Gender();
      gender.setKnownType(GenderType.Male);
      gedxPerson.setGender(gender);
    }
    else if(fact.getValue().equalsIgnoreCase("F")) {
      Gender gender = new Gender();
      gender.setKnownType(GenderType.Female);
      gedxPerson.setGender(gender);
    }
    else if(fact.getValue().equalsIgnoreCase("U")) {
      Gender gender = new Gender();
      gender.setKnownType(GenderType.Unknown);
      gedxPerson.setGender(gender);
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