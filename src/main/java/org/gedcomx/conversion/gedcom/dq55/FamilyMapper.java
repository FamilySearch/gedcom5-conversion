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

import java.io.IOException;
import java.util.List;

public class FamilyMapper {

  public void toRelationship(Family ged5Family, GedcomxConversionResult result) throws IOException {
    List<SpouseRef> husbands = ged5Family.getHusbandRefs();
    SpouseRef husband = husbands.size() > 0 ? husbands.get(0) : null;
    List<SpouseRef> wives = ged5Family.getWifeRefs();
    SpouseRef wife = wives.size() > 0 ? wives.get(0) : null;
    Relationship coupleRelationship = null;
    if ( husband != null && wife != null) {
      coupleRelationship = result.addRelationship(husband, wife, RelationshipType.Couple);
    }

    for (ChildRef child : ged5Family.getChildRefs()) {
      if (husband != null)
        result.addRelationship(husband, child, RelationshipType.ParentChild);
      if (wife != null)
        result.addRelationship(wife, child, RelationshipType.ParentChild);
    }

    if (coupleRelationship != null) {
      for (EventFact eventFact : ged5Family.getEventsFacts()) {
        System.out.println("eventFact " + eventFact.getTag() + ": " + eventFact.getDate() + " at " + eventFact.getPlace());
        if ("MARR".equalsIgnoreCase(eventFact.getTag())) {
          Fact fact = Util.toFact(FactType.Marriage, eventFact.getDate(), eventFact.getPlace());
          coupleRelationship.addFact(fact);
        }
      }
      coupleRelationship.setNotes(Util.toNotes(ged5Family.getNotes()));
      coupleRelationship.setSources(Util.toSourcesAndSourceReferences(ged5Family.getSourceCitations(), result));
    }
  }

}
