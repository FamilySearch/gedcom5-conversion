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
import org.folg.gedcom.model.Family;
import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.model.Person;
import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.conclusion.Relationship;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.gedcomx.types.RelationshipType;

import java.util.List;

public class FamilyMapper {

  private ResourceReference createRef( Person person, GedcomxConversionResult result ) {
    ResourceReference reference = new ResourceReference();
    org.gedcomx.conclusion.Person gedxPerson = result.lookupPerson(person.getId());
    reference.setResource( new URI(result.getEntryName(gedxPerson)));
    return reference;
  }

  public void toRelationship(Family dqFamily, GedcomxConversionResult result, Gedcom dqGedcom) {
    List<Person> husbands = dqFamily.getHusbands(dqGedcom);
    Person husband = husbands.size() > 0 ? husbands.get(0) : null;
    List<Person> wives = dqFamily.getWives(dqGedcom);
    Person wife = wives.size() > 0 ? wives.get(0) : null;

    if ( husband != null && wife != null) {
      addRelationship(result, husband, wife, RelationshipType.Couple);
    }

    for (Person child : dqFamily.getChildren(dqGedcom)) {
      if (husband != null)
        addRelationship(result, husband, child, RelationshipType.ParentChild);
      if (wife != null)
        addRelationship(result, wife, child, RelationshipType.ParentChild);
    }

    for (EventFact eventFact : dqFamily.getEventsFacts()) {
      System.out.println("eventFact.getTag() = " + eventFact.getTag());
      System.out.println("eventFact.getDate() = " + eventFact.getDate());
      System.out.println("eventFact.getPlace() = " + eventFact.getPlace());
      if ("MARR".equalsIgnoreCase(eventFact.getTag())) {

      }
    }
  }

  private void addRelationship(GedcomxConversionResult result, Person husband, Person wife, RelationshipType relationshipType) {
    Relationship relationship = createRelationship(result, husband, wife, relationshipType);
    result.addRelationship(relationship);
  }

  private Relationship createRelationship(GedcomxConversionResult result, Person person1, Person person2, RelationshipType relationshipType) {
    Relationship relationship = new Relationship();
    relationship.setKnownType(relationshipType);
    relationship.setPerson1( createRef(person1, result) );
    relationship.setPerson2(createRef(person2, result));
    return relationship;
  }
}
