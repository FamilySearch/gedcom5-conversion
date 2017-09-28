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

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.folg.gedcom.model.ChildRef;
import org.folg.gedcom.model.EventFact;
import org.folg.gedcom.model.Family;
import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.model.GedcomTag;
import org.folg.gedcom.model.LdsOrdinance;
import org.folg.gedcom.model.ParentFamilyRef;
import org.folg.gedcom.model.Person;
import org.folg.gedcom.model.SpouseRef;
import org.gedcomx.conclusion.Fact;
import org.gedcomx.conclusion.Relationship;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.gedcomx.types.FactType;
import org.gedcomx.types.RelationshipType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import org.familysearch.platform.ordinances.Ordinance;


public class FamilyMapper {
  private static final Logger logger = LoggerFactory.getLogger(CommonMapper.class);

  private final MappingConfig mappingConfig;

  public FamilyMapper(MappingConfig mappingConfig) {
    this.mappingConfig = mappingConfig;
  }

  public void toRelationship(Family dqFamily, Gedcom dqGedcom, GedcomxConversionResult result) throws IOException {
    String dqFamilyId = dqFamily.getId();
    String gedxFamilyId = mappingConfig.createId(dqFamilyId);

    Marker familyContext = ConversionContext.getDetachedMarker(String.format("@%s@ FAM", dqFamilyId));
    ConversionContext.addReference(familyContext);

    List<SpouseRef> husbands = dqFamily.getHusbandRefs();
    String husbandId = (husbands.size() > 0) ? mappingConfig.createId(husbands.get(0).getRef()) : null;
    List<SpouseRef> wives = dqFamily.getWifeRefs();
    String wifeId = (wives.size() > 0) ? mappingConfig.createId(wives.get(0).getRef()) : null;
    Relationship coupleRelationship = null;

    Date lastModified = CommonMapper.toDate(dqFamily.getChange()); //todo: set the timestamp on the attribution?

    if ( husbandId != null && wifeId != null) {
      coupleRelationship = toRelationship(gedxFamilyId, husbandId, wifeId, RelationshipType.Couple);
      result.addRelationship(coupleRelationship);
    }

    for (ChildRef child : dqFamily.getChildRefs()) {
      String childId = mappingConfig.createId(child.getRef());

      Person dqChild = (dqGedcom == null) ? null : dqGedcom.getPerson(childId);
      List<ParentFamilyRef> childToFamilyLinks;
      if (dqChild != null) {
        childToFamilyLinks = dqChild.getParentFamilyRefs();
      } else {
        logger.warn(ConversionContext.getContext(), "Could not find referenced child (@{}@ INDI).", childId);
        childToFamilyLinks = Collections.emptyList();
      }

      if (husbandId != null) {
        Relationship gedxRelationship = toRelationship(gedxFamilyId, husbandId, childId, RelationshipType.ParentChild);
        addFacts(gedxRelationship, dqFamilyId, childToFamilyLinks);
        result.addRelationship(gedxRelationship);
      }
      if (wifeId != null) {
        Relationship gedxRelationship = toRelationship(gedxFamilyId, wifeId, childId, RelationshipType.ParentChild);
        addFacts(gedxRelationship, dqFamilyId, childToFamilyLinks);
        result.addRelationship(gedxRelationship);
      }
    }

    int index = 0;
    for (EventFact eventFact : dqFamily.getEventsFacts()) {
      Marker factContext = ConversionContext.getDetachedMarker(eventFact.getTag() + '.' + (++index));
      ConversionContext.addReference(factContext);

      if (coupleRelationship != null) {
        Fact fact = FactMapper.toFact(eventFact, result);
        coupleRelationship.addFact(fact);
      } else {
        logger.warn(ConversionContext.getContext(), "The GEDCOM X converter only supports the {} fact in the presence of a couple relationship.", eventFact.getTag());
      }

      ConversionContext.removeReference(factContext);
    }

    if (coupleRelationship != null) {
      coupleRelationship.setSources(CommonMapper.toSourcesAndSourceReferences(dqFamily.getSourceCitations(), result));
    } else {
      int size = dqFamily.getSourceCitations().size();
      if (size > 0) {
        logger.warn(ConversionContext.getContext(), "The GEDCOM X converter only supports a source citation(s) in the presence of a couple relationship; {} source citation(s) ignored.", size);
      }
    }

    index = 0;
    for (LdsOrdinance ldsOrdinance : dqFamily.getLdsOrdinances()) {
      Marker ordinanceContext = ConversionContext.getDetachedMarker(ldsOrdinance.getTag() + '.' + (++index));
      ConversionContext.addReference(ordinanceContext);

      if (coupleRelationship != null) {
        Ordinance ordinance = FactMapper.toOrdinance(ldsOrdinance);
        if(ordinance != null) {
          coupleRelationship.addExtensionElement(ordinance);
        }
      }
      else {
        logger.warn(ConversionContext.getContext(), "The GEDCOM X converter only supports the {} ordinance in the presence of a couple relationship.", ldsOrdinance.getTag());
      }

      ConversionContext.removeReference(ordinanceContext);
    }

    int cntNotes = dqFamily.getNotes().size() + dqFamily.getNoteRefs().size();
    if (cntNotes > 0) {
      logger.warn(ConversionContext.getContext(), "Did not process {} notes or references to notes.", cntNotes);
    }

    int cntMedia = dqFamily.getMedia().size() + dqFamily.getMediaRefs().size();
    if (cntMedia > 0) {
      logger.warn(ConversionContext.getContext(), "Did not process {} media items or references to media items.", cntMedia);
    }

    for (String refNum : dqFamily.getReferenceNumbers()) {
      Marker refnContext = ConversionContext.getDetachedMarker("REFN");
      ConversionContext.addReference(refnContext);
      logger.warn(ConversionContext.getContext(), "User reference number ({}) was ignored.", refNum);
      ConversionContext.removeReference(refnContext);
    }

    if (dqFamily.getRin() != null) {
      logger.warn(ConversionContext.getContext(), "RIN ({}) was ignored.", dqFamily.getRin());
    }

    if (dqFamily.getUid() != null) {
      Marker uidContext = ConversionContext.getDetachedMarker(dqFamily.getUidTag());
      ConversionContext.addReference(uidContext);
      logger.warn(ConversionContext.getContext(), "UID ({}) was ignored.", dqFamily.getUid());
      ConversionContext.removeReference(uidContext);
    }

    if (dqFamily.getExtensions().size() > 0) {
      for (String extensionCategory : dqFamily.getExtensions().keySet()) {
        for (GedcomTag tag : ((List<GedcomTag>)dqFamily.getExtension(extensionCategory))) {
          logger.warn(ConversionContext.getContext(), "Unsupported ({}): {}", extensionCategory, tag);
          // DATA tag (and subordinates) in GEDCOM 5.5. SOURCE_RECORD not being looked for or parsed by DallanQ code
        }
      }
    }

    ConversionContext.removeReference(familyContext);
  }

  /**
   * Creates a GEDCOM X relationship.
   * @param familyId  the GEDCOM 5.5 identifier associated with the family to which this relationship belongs
   * @param personId1  the GEDCOM 5.5 identifier associated with the person 1
   * @param personId2  the GEDCOM 5.5 identifier associated with the person 1
   * @param relationshipType  the relationship type
   * @return relationship that was added
   */
  private Relationship toRelationship(String familyId, String personId1, String personId2, RelationshipType relationshipType) {
    Relationship relationship = new Relationship();

    relationship.setKnownType(relationshipType);
    relationship.setId(createRelationshipId(familyId, personId1, personId2));
    relationship.setPerson1(CommonMapper.toReference(personId1));
    relationship.setPerson2(CommonMapper.toReference(personId2));

    return relationship;
  }

  private String createRelationshipId(String familyId, String personId1, String personId2) {
    if (mappingConfig.isIncludeFilenameInIds()) {
      // We don't want to repeat the file name three times, so remove it from the person IDs
      String shortPersonId1 = personId1.substring(personId1.indexOf(':') + 1);
      String shortPersonId2 = personId2.substring(personId2.indexOf(':') + 1);
      return familyId + '-' + shortPersonId1 + '-' + shortPersonId2;
    }
    return familyId + '-' + personId1 + '-' + personId2;
  }


  private void addFacts(Relationship gedxRelationship, String ged5FamilyId, List<ParentFamilyRef> childToFamilyLinks) {
    for (ParentFamilyRef ref : childToFamilyLinks) {
      if (ref.getRef().equals(ged5FamilyId)) {
        String relationshipType = ref.getRelationshipType();
        if (relationshipType != null) {
          relationshipType = relationshipType.toLowerCase().trim();
          if (relationshipType.equalsIgnoreCase("adopted")) {
            Fact fact = new Fact();
            fact.setKnownType(FactType.AdoptiveParent);
            gedxRelationship.addFact(fact);
          } else if (relationshipType.equalsIgnoreCase("birth")) {
            Fact fact = new Fact();
            fact.setKnownType(FactType.BiologicalParent);
            gedxRelationship.addFact(fact);
          } else if (relationshipType.equalsIgnoreCase("foster")) {
            Fact fact = new Fact();
            fact.setKnownType(FactType.FosterParent);
            gedxRelationship.addFact(fact);
          } else {
            logger.warn(ConversionContext.getContext(), "Information designating this relationship as \"{}\" was dropped.", ref.getRelationshipType());
          }
        }
      }
    }
  }
}
