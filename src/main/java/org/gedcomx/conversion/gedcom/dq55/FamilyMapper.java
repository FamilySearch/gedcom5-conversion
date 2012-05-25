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
import org.slf4j.Marker;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class FamilyMapper {

  public void toRelationship(Family ged5Family, Gedcom dqGedcom, GedcomxConversionResult result) throws IOException {
    Marker familyContext = ConversionContext.getDetachedMarker(String.format("@%s@ FAM", ged5Family.getId()));
    ConversionContext.addReference(familyContext);

    List<SpouseRef> husbands = ged5Family.getHusbandRefs();
    SpouseRef husband = husbands.size() > 0 ? husbands.get(0) : null;
    List<SpouseRef> wives = ged5Family.getWifeRefs();
    SpouseRef wife = wives.size() > 0 ? wives.get(0) : null;
    Relationship coupleRelationship = null;
    Date lastModified = CommonMapper.toDate(ged5Family.getChange());
    if ( husband != null && wife != null) {
      coupleRelationship = CommonMapper.toRelationship(husband, wife, RelationshipType.Couple);
      result.addRelationship(coupleRelationship, lastModified);
    }

    for (ChildRef child : ged5Family.getChildRefs()) {
      String childId = child.getRef();
      Person dqChild = (dqGedcom == null || childId == null)?null: dqGedcom.getPerson(childId);

      if (husband != null) {
        Relationship gedxRelationship = CommonMapper.toRelationship(husband, child, RelationshipType.ParentChild);
        addFacts(gedxRelationship, ged5Family, dqChild);
        result.addRelationship(gedxRelationship, lastModified);
      }
      if (wife != null) {
        Relationship gedxRelationship = CommonMapper.toRelationship(wife, child, RelationshipType.ParentChild);
        addFacts(gedxRelationship, ged5Family, dqChild);
        result.addRelationship(gedxRelationship, lastModified);
      }
    }

    if (coupleRelationship != null) {
      int index = 0;
      for (EventFact eventFact : ged5Family.getEventsFacts()) {
        Marker factContext = ConversionContext.getDetachedMarker(eventFact.getTag() + '.' + (++index));
        ConversionContext.addReference(factContext);

        System.out.println("eventFact " + eventFact.getTag() + ": " + eventFact.getDate() + " at " + eventFact.getPlace());
        Fact fact = FactMapper.toFact(eventFact, result);
        coupleRelationship.addFact(fact);

        ConversionContext.removeReference(factContext);
      }
      coupleRelationship.setNotes(CommonMapper.toNotes(ged5Family.getNotes()));
      coupleRelationship.setSources(CommonMapper.toSourcesAndSourceReferences(ged5Family.getSourceCitations(), result));
    }

    ConversionContext.removeReference(familyContext);
  }

  private void addFacts(Relationship gedxRelationship, Family ged5Family, Person dqChild) {
    if(dqChild == null) {
      //TODO log/warn
      return;
    }
    List<ParentFamilyRef> refs = dqChild.getParentFamilyRefs();
    for(ParentFamilyRef ref : refs) {
      if(ref.getRef().equals(ged5Family.getId())) {
        String relationshipType = ref.getRelationshipType();
        if(relationshipType != null) {
          relationshipType = relationshipType.toLowerCase().trim();
          if(relationshipType.equals("adopted")) {
            Fact fact = new Fact();
            fact.setKnownType(FactType.Adopted);
            gedxRelationship.addFact(fact);
          }
          else if(relationshipType.equals("birth")) {
            Fact fact = new Fact();
            fact.setKnownType(FactType.Biological);
            gedxRelationship.addFact(fact);
          }
          else if(relationshipType.equals("foster")) {
            Fact fact = new Fact();
            fact.setKnownType(FactType.Foster);
            gedxRelationship.addFact(fact);
          }
          else if(relationshipType.equals("sealing")) {
            //TODO log/warn that we are dropping for now
          }
          else {
            //TODO log/warn
          }
        }
      }
    }
  }
}
