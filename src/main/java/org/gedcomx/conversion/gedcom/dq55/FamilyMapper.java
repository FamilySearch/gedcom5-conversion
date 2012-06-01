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

import org.folg.gedcom.model.*;
import org.gedcomx.conclusion.Fact;
import org.gedcomx.conclusion.Relationship;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.gedcomx.types.FactType;
import org.gedcomx.types.RelationshipType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FamilyMapper {
  private static final Logger logger = LoggerFactory.getLogger(CommonMapper.class);

  public void toRelationship(Family dqFamily, Gedcom dqGedcom, GedcomxConversionResult result) throws IOException {
    String familyId = dqFamily.getId();
    Marker familyContext = ConversionContext.getDetachedMarker(String.format("@%s@ FAM", familyId));
    ConversionContext.addReference(familyContext);

    List<SpouseRef> husbands = dqFamily.getHusbandRefs();
    String husbandId = husbands.size() > 0 ? husbands.get(0).getRef() : null;
    List<SpouseRef> wives = dqFamily.getWifeRefs();
    String wifeId = wives.size() > 0 ? wives.get(0).getRef() : null;
    Relationship coupleRelationship = null;

    Date lastModified = CommonMapper.toDate(dqFamily.getChange());

    if ( husbandId != null && wifeId != null) {
      coupleRelationship = CommonMapper.toRelationship(familyId, husbandId, wifeId, RelationshipType.Couple);
      result.addRelationship(coupleRelationship, lastModified);
    }

    for (ChildRef child : dqFamily.getChildRefs()) {
      String childId = child.getRef();

      Person dqChild = (dqGedcom == null) ? null : dqGedcom.getPerson(childId);
      List<ParentFamilyRef> childToFamilyLinks;
      if (dqChild != null) {
        childToFamilyLinks = dqChild.getParentFamilyRefs();
      } else {
        logger.warn(ConversionContext.getContext(), "Could not find referenced child (@{}@ INDI).", childId);
        childToFamilyLinks = Collections.<ParentFamilyRef>emptyList();
      }

      if (husbandId != null) {
        Relationship gedxRelationship = CommonMapper.toRelationship(familyId, husbandId, childId, RelationshipType.ParentChild);
        addFacts(gedxRelationship, familyId, childToFamilyLinks);
        result.addRelationship(gedxRelationship, lastModified);
      }
      if (wifeId != null) {
        Relationship gedxRelationship = CommonMapper.toRelationship(familyId, wifeId, childId, RelationshipType.ParentChild);
        addFacts(gedxRelationship, familyId, childToFamilyLinks);
        result.addRelationship(gedxRelationship, lastModified);
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
        logger.warn(ConversionContext.getContext(), "Did not convert {} because it could not associated it with a couple.  (See GEDCOM X issue 7.)", eventFact.getTag());
      }

      ConversionContext.removeReference(factContext);
    }

    if (coupleRelationship != null) {
      coupleRelationship.setSources(CommonMapper.toSourcesAndSourceReferences(dqFamily.getSourceCitations(), result));
    } else {
      int size = dqFamily.getSourceCitations().size();
      if (size > 0) {
        logger.warn(ConversionContext.getContext(), "Did not convert {} source citation(s) because it could not associated it with a couple.  (See GEDCOM X issue 7.)", size);
      }
    }

    int cntLdsOrdinances = dqFamily.getLdsOrdinances().size();
    if (cntLdsOrdinances > 0) {
      logger.warn(ConversionContext.getContext(), "Did not convert information for {} LDS ordinances.", cntLdsOrdinances);
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

  private void addFacts(Relationship gedxRelationship, String ged5FamilyId, List<ParentFamilyRef> childToFamilyLinks) {
    for (ParentFamilyRef ref : childToFamilyLinks) {
      if (ref.getRef().equals(ged5FamilyId)) {
        String relationshipType = ref.getRelationshipType();
        if (relationshipType != null) {
          relationshipType = relationshipType.toLowerCase().trim();
          if (relationshipType.equalsIgnoreCase("adopted")) {
            Fact fact = new Fact();
            fact.setKnownType(FactType.Adopted);
            gedxRelationship.addFact(fact);
          } else if (relationshipType.equalsIgnoreCase("birth")) {
            Fact fact = new Fact();
            fact.setKnownType(FactType.Biological);
            gedxRelationship.addFact(fact);
          } else if (relationshipType.equalsIgnoreCase("foster")) {
            Fact fact = new Fact();
            fact.setKnownType(FactType.Foster);
            gedxRelationship.addFact(fact);
          } else {
            logger.warn(ConversionContext.getContext(), "Information designating this relationship as \"{}\" was dropped.", ref.getRelationshipType());
          }
        }
      }
    }
  }
}
