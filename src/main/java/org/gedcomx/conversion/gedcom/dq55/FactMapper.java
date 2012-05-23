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
import org.gedcomx.conclusion.Date;
import org.gedcomx.conclusion.Fact;
import org.gedcomx.conclusion.Place;
import org.gedcomx.types.FactType;

import java.util.HashMap;

public class FactMapper {
  static final HashMap<String, FactType> factMap = new HashMap<String, FactType>();

  static {
    // Attributes (Short and long tag names, from the standard)
    // (Individual)
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
    // Individual
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
//    factMap.put("EVEN", FactType.);   factMap.put("EVENT", FactType.);
    // (Family)
    factMap.put("ANUL", FactType.Annulment);  factMap.put("ANNULMENT", FactType.Annulment);
    factMap.put("DIV", FactType.Divorce);  factMap.put("DIVORCE", FactType.Divorce);
    factMap.put("DIVF", FactType.DivorceFiling);  factMap.put("DIVORCE_FILED", FactType.DivorceFiling);
    factMap.put("ENGA", FactType.Engagement);  factMap.put("ENGAGEMENT", FactType.Engagement);
    factMap.put("MARB", FactType.MarriageBanns);  factMap.put("MARRIAGE_BANN", FactType.MarriageBanns);
    factMap.put("MARC", FactType.MarriageContract);  factMap.put("MARR_CONTRACT", FactType.MarriageContract);
    factMap.put("MARR", FactType.Marriage);  factMap.put("MARRIAGE", FactType.Marriage);
    factMap.put("MARL", FactType.MarriageLicense);  factMap.put("MARR_LICENSE", FactType.MarriageLicense);
    factMap.put("MARS", FactType.MarriageSettlement);  factMap.put("MARR_SETTLEMENT", FactType.MarriageSettlement);

    // Non-standard tags
    // (Individual)
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
    // (Family)
    factMap.put("CLAW", FactType.CommonLawMarriage);
    factMap.put("_DIV", FactType.Divorce);
//    factMap.put("_MBON", FactType.MarriageBanns);  According to other sources, this maps to "Marriage Bond", so not including it for now...
    factMap.put("SEPA", FactType.Separation);
    factMap.put("_SEPARATED", FactType.Separation);
    factMap.put("_SEPR", FactType.Separation);
  }

  static Fact toFact(EventFact fact) {
    fact.getType();
    if(fact.getTag() == null) {
      //TODO warn/log
    }
    else {
      // TODO don't warn on SEX, but warn on all other invalid tags...

      String upperTag = fact.getTag().trim().toUpperCase();
      FactType factType = factMap.get(upperTag);
      if(factType == null) {
        //TODO warn/log
      } else {
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
            gedxFact.setDate(date);
          }

          if(factPlace != null) {
            Place place = new Place();
            place.setOriginal(factPlace);
            gedxFact.setPlace(place);
          }

          if(factValue != null) {
            gedxFact.setOriginal(factValue);
          }
          return gedxFact;
        }
      }
    }

    return null;
  }
}
