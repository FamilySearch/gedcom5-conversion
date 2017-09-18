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
import org.folg.gedcom.model.GedcomTag;
import org.folg.gedcom.model.LdsOrdinance;
import org.gedcomx.conclusion.Date;
import org.gedcomx.conclusion.Fact;
import org.gedcomx.conclusion.PlaceReference;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.gedcomx.types.FactType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.familysearch.platform.ordinances.Ordinance;
import org.familysearch.platform.ordinances.OrdinanceType;

public class FactMapper {
  private static final Logger logger = LoggerFactory.getLogger(CommonMapper.class);
  static final Map<String, FactType> factMap = new HashMap<String, FactType>();
  static final Map<String, OrdinanceType> ordinanceMap = new HashMap<String, OrdinanceType>();

  static {
    // Attributes (Short and long tag names, from the standard)
    // (Individual)
    factMap.put("CAST", FactType.Caste);  factMap.put("CASTE", FactType.Caste);
    factMap.put("DSCR", FactType.PhysicalDescription);  factMap.put("PHY_DESCRIPTION", FactType.PhysicalDescription);
    factMap.put("EDUC", FactType.Education);  factMap.put("EDUCATION", FactType.Education);
    factMap.put("IDNO", FactType.NationalId);  factMap.put("IDENT_NUMBER", FactType.NationalId);
    factMap.put("NATI", FactType.Nationality);  factMap.put("NATIONALITY", FactType.Nationality);
    factMap.put("NCHI", FactType.NumberOfChildren);  factMap.put("CHILDREN_COUNT", FactType.NumberOfChildren);
    factMap.put("NMR", FactType.NumberOfMarriages);  factMap.put("MARRIAGE_COUNT", FactType.NumberOfMarriages);
    factMap.put("OCCU", FactType.Occupation);  factMap.put("OCCUPATION", FactType.Occupation);
    factMap.put("PROP", FactType.Property);  factMap.put("PROPERTY", FactType.Property);
    factMap.put("RELI", FactType.Religion);  factMap.put("RELIGION", FactType.Religion);
    factMap.put("RESI", FactType.Residence);  factMap.put("RESIDENCE", FactType.Residence);
    factMap.put("SSN", FactType.NationalId);  factMap.put("SOC_SEC_NUMBER", FactType.NationalId);
//    factMap.put("TITL", FactType.TitleOfNobility);  factMap.put("TITLE", FactType.TitleOfNobility);
    //TODO Handle custom
//    factMap.put("FACT", FactType.);

    // Events (Short and long tag names, from the standard)
    // Individual
    factMap.put("ADOP", FactType.Adoption);  factMap.put("ADOPTION", FactType.Adoption);
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
    factMap.put("GRAD", FactType.Education);  factMap.put("GRADUATION", FactType.Education);
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
    factMap.put("MARS", FactType.MarriageContract);  factMap.put("MARR_SETTLEMENT", FactType.MarriageContract);

    // Non-standard tags
    // (Individual)
    factMap.put("CIRC", FactType.Circumcision);
    factMap.put("CITN", FactType.Nationality);
    factMap.put("BLESS", FactType.Blessing);
    factMap.put("BLSL", FactType.Blessing); // Blessing - LDS
    factMap.put("DWEL", FactType.Residence);
    factMap.put("_EXCM", FactType.Excommunication);
    factMap.put("EXCO", FactType.Excommunication);
    factMap.put("_FNRL", FactType.Funeral);
    factMap.put("_FUN", FactType.Funeral);
    factMap.put("ILLN", FactType.Medical);
    factMap.put("ILL", FactType.Medical);
    factMap.put("_INTE", FactType.Burial);
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
    factMap.put("MOVE", FactType.MoveTo);
    factMap.put("ORDI", FactType.Ordination); // In 5.5 standard
    factMap.put("ORDL", FactType.Ordination); // Ordination - LDS
    factMap.put("ARVL", FactType.Immigration);
    factMap.put("ARRI", FactType.Immigration);
    factMap.put("ARRIVAL", FactType.Immigration);
    factMap.put("DPRT", FactType.Emigration);
    factMap.put("DEPA", FactType.Emigration);
    factMap.put("DEPARTURE", FactType.Emigration);
    factMap.put("RESIR", FactType.Residence);
    factMap.put("RACE", FactType.Ethnicity);
    factMap.put("STLB", FactType.Stillbirth);
    factMap.put("STIL", FactType.Stillbirth);
    factMap.put("BAP", FactType.Baptism);
    factMap.put("BAPT", FactType.Baptism);
//    factMap.put("_NAMS", FactType.Namesake);
    factMap.put("SOC_", FactType.NationalId);
    factMap.put("ENLIST", FactType.MilitaryService);
    factMap.put("_DEG", FactType.Education);
    factMap.put("_DEGREE", FactType.Education);
    factMap.put("EMPL", FactType.Occupation);
    factMap.put("_EMPLOY", FactType.Occupation);

    // (Family)
    factMap.put("CLAW", FactType.CommonLawMarriage);
    factMap.put("_DIV", FactType.Divorce);
//    factMap.put("_MBON", FactType.MarriageBanns);  According to other sources, this maps to "Marriage Bond", so not including it for now...
    factMap.put("SEPA", FactType.Separation);
    factMap.put("_SEPARATED", FactType.Separation);
    factMap.put("_SEPR", FactType.Separation);

    // Ordinances
    ordinanceMap.put("BAPL", OrdinanceType.Baptism);
    ordinanceMap.put("CONL", OrdinanceType.Confirmation);
    ordinanceMap.put("WAC", OrdinanceType.Initiatory);
    ordinanceMap.put("ENDL", OrdinanceType.Endowment);
    ordinanceMap.put("SLGS", OrdinanceType.SealingToSpouse);
    ordinanceMap.put("SLGC", OrdinanceType.SealingChildToParents);
  }

  static Fact toFact(EventFact dqFact, GedcomxConversionResult result) throws IOException {
    //dqFact.getType();
    if(dqFact.getTag() == null) {
      logger.warn(ConversionContext.getContext(), "Empty tag encountered");
    }
    else {
      String upperTag = dqFact.getTag().trim().toUpperCase();
      FactType factType = factMap.get(upperTag);
      if(factType == null) {
        // We don't show a warning for SEX since it is handled as gender in PersonMapper
        if(!upperTag.equals("SEX")) {
          logger.warn(ConversionContext.getContext(), "Ignoring tag: {}", dqFact.getTag());
        }
      } else {
        String factValue = dqFact.getValue();
        if(factValue != null) {
          factValue = factValue.trim();
          if(factValue.equals("")) {
            factValue = null;
          }
        }

        String factPlace = dqFact.getPlace();
        if(factPlace != null) {
          factPlace = factPlace.trim();
          if(factPlace.equals("")) {
            factPlace = null;
          }
        }

        String factDate = dqFact.getDate();
        if(factDate != null) {
          factDate = factDate.trim();
          if(factDate.equals("")) {
            factDate = null;
          }
        }

        Fact gedxFact = new Fact();
        gedxFact.setKnownType(factType);

        if(factDate != null) {
          Date date = new Date();
          date.setOriginal(factDate);
          gedxFact.setDate(date);
        }

        if(factPlace != null) {
          PlaceReference place = new PlaceReference();
          place.setOriginal(factPlace);
          gedxFact.setPlace(place);
        }

        if(factValue != null) {
          gedxFact.setValue(factValue);
        }

        // add source references to the fact
        gedxFact.setSources(CommonMapper.toSourcesAndSourceReferences(dqFact.getSourceCitations(), result));

        if (dqFact.getCause() != null) {
          logger.warn(ConversionContext.getContext(), "CAUS was ignored.");
        }

        if (dqFact.getAddress() != null) {
          logger.warn(ConversionContext.getContext(), "Address was ignored: {}", dqFact.getAddress().getDisplayValue());
        }

        if (dqFact.getEmail() != null) {
          logger.warn(ConversionContext.getContext(), "e-mail ({}) was ignored.", dqFact.getEmail());
        }
        if (dqFact.getFax() != null) {
          logger.warn(ConversionContext.getContext(), "fax ({}) was ignored.", dqFact.getFax());
        }
        if (dqFact.getPhone() != null) {
          logger.warn(ConversionContext.getContext(), "phone ({}) was ignored.", dqFact.getPhone());
        }
        if (dqFact.getWww() != null) {
          logger.warn(ConversionContext.getContext(), "www ({}) was ignored.", dqFact.getWww());
        }

        if (dqFact.getUid() != null) {
          Marker uidContext = ConversionContext.getDetachedMarker(dqFact.getUidTag());
          ConversionContext.addReference(uidContext);
          logger.warn(ConversionContext.getContext(), "UID ({}) was ignored.", dqFact.getUid());
          ConversionContext.removeReference(uidContext);
        }

        if (dqFact.getRin() != null) {
          logger.warn(ConversionContext.getContext(), "RIN ({}) was ignored.", dqFact.getRin());
        }

        int cntNotes = dqFact.getNotes().size() + dqFact.getNoteRefs().size();
        if (cntNotes > 0) {
          logger.warn(ConversionContext.getContext(), "Did not process {} notes or references to notes.", cntNotes);
        }

        int cntMedia = dqFact.getMedia().size() + dqFact.getMediaRefs().size();
        if (cntMedia > 0) {
          logger.warn(ConversionContext.getContext(), "Did not process {} media items or references to media items.", cntMedia);
        }

        if (dqFact.getExtensions().size() > 0) {
          for (String extensionCategory : dqFact.getExtensions().keySet()) {
            for (GedcomTag tag : ((List<GedcomTag>)dqFact.getExtension(extensionCategory))) {
              logger.warn(ConversionContext.getContext(), "Unsupported ({}): {}", extensionCategory, tag);
              // DATA tag (and subordinates) in GEDCOM 5.5. SOURCE_RECORD not being looked for or parsed by DallanQ code
            }
          }
        }

        return gedxFact;
      }
    }

    return null;
  }

  static Ordinance toOrdinance(LdsOrdinance dqOrdinance) throws IOException {
    String type = dqOrdinance.getTag();
    if (type == null) {
      return null;
    }
    Ordinance ordinance = new Ordinance();
    ordinance.setKnownType(getType(type));
    if (dqOrdinance.getDate() != null) {
      Date ordinanceDate = new Date();
      ordinanceDate.setOriginal(dqOrdinance.getDate());
      ordinance.setDate(ordinanceDate);
    }
    if (dqOrdinance.getTemple() != null) {
      ordinance.setTempleCode(dqOrdinance.getTemple());
    }
    if (dqOrdinance.getPlace() != null) {
      logger.warn(ConversionContext.getContext(), "#ignoredField# PLAC in ordinance was ignored.", dqOrdinance.getPlace());
    }
    return ordinance;
  }

  private static OrdinanceType getType(String value) {
    return ordinanceMap.get(value);
  }
}
