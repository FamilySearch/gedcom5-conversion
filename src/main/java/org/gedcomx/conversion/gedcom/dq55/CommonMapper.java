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

import org.folg.gedcom.model.Change;
import org.folg.gedcom.model.DateTime;
import org.folg.gedcom.model.SourceCitation;
import org.gedcomx.common.Attribution;
import org.gedcomx.common.Note;
import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.conclusion.Relationship;
import org.gedcomx.conclusion.SourceReference;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.gedcomx.metadata.dc.DublinCoreDescriptionDecorator;
import org.gedcomx.metadata.foaf.Address;
import org.gedcomx.metadata.foaf.Agent;
import org.gedcomx.metadata.rdf.Description;
import org.gedcomx.metadata.rdf.RDFLiteral;
import org.gedcomx.metadata.rdf.RDFValue;
import org.gedcomx.types.ConfidenceLevel;
import org.gedcomx.types.RelationshipType;
import org.gedcomx.types.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;


public class CommonMapper {
  private static final Logger logger = LoggerFactory.getLogger(CommonMapper.class);
  private static final String PARSEFORMAT_D_MMM_YY = "d MMM yy";
  private static final String PARSEFORMAT_SSS = ".SSS";
  private static final String PARSEFORMAT_SS = ":ss";
  private static final String PARSEFORMAT_HH_MM = "HH:mm";

  private static final List<String> CHAN_DATE_ONLY_PARSE_PATTERNS = Arrays.asList(PARSEFORMAT_D_MMM_YY);
  private static final List<String> CHAN_DATE_TIME_PARSE_PATTERNS = Arrays.asList(
      PARSEFORMAT_D_MMM_YY + ' ' + PARSEFORMAT_HH_MM + PARSEFORMAT_SS + PARSEFORMAT_SSS
    , PARSEFORMAT_D_MMM_YY + ' ' + PARSEFORMAT_HH_MM + PARSEFORMAT_SS
    , PARSEFORMAT_D_MMM_YY + ' ' + PARSEFORMAT_HH_MM);

  /**
   * Create a list of GedcomX Notes based on the ged5 notes.
   * @param dqNotes Gedcom 5 notes
   * @return GedcomX Notes
   */
  public static List<Note> toNotes(List<org.folg.gedcom.model.Note> dqNotes) {
    List<Note> notes = new ArrayList<Note>(dqNotes.size());
    for (org.folg.gedcom.model.Note dqNote : dqNotes) {
      Note note = new Note();
      note.setText(dqNote.getValue());
      notes.add( note );
    }
    return notes;
  }

  /**
   * Create a list of GedcomX SourceReference based on the ged5 SourceCitations.
   * @param dqSources Gedcom 5 source citations
   * @return GedcomX SourceReferences
   */
  public static List<SourceReference> toSourcesAndSourceReferences(List<SourceCitation> dqSources, GedcomxConversionResult result) throws IOException {
    List<SourceReference> sourceReferences = new ArrayList<SourceReference>(dqSources.size());

    int index = 0;
    for (org.folg.gedcom.model.SourceCitation dqSource : dqSources) {
      Marker sourceContext = ConversionContext.getDetachedMarker("SOUR." + (++index));
      ConversionContext.addReference(sourceContext);
      try {
        boolean sourceDescriptionHasData = false;
        boolean sourceReferenceHasData = false;
        Description gedxSourceDescription = new Description();
        DublinCoreDescriptionDecorator gedxDecoratedSourceDescription = DublinCoreDescriptionDecorator.newInstance(gedxSourceDescription);
        gedxSourceDescription.setId(Long.toHexString(SequentialIdentifierGenerator.getNextId()));

        String entryName = CommonMapper.getDescriptionEntryName(gedxSourceDescription.getId());
        SourceReference gedxSourceReference = new SourceReference();
        gedxSourceReference.setDescription(new ResourceReference());
        gedxSourceReference.getDescription().setResource(URI.create(entryName));

        if (dqSource.getRef() != null) {
          gedxDecoratedSourceDescription.partOf(new RDFValue(CommonMapper.getDescriptionEntryName(dqSource.getRef())));
          sourceDescriptionHasData = true;

          if (dqSource.getPage() != null) {
            gedxDecoratedSourceDescription.description(new RDFValue(dqSource.getPage()));
          }

          if (dqSource.getDate() != null) {
            gedxDecoratedSourceDescription.created(dqSource.getDate());
          }
        } else if (dqSource.getValue() != null) {
          gedxDecoratedSourceDescription.description(new RDFValue(dqSource.getValue()));
          sourceDescriptionHasData = true;
        }

        if (dqSource.getText() != null) {
          logger.warn(ConversionContext.getContext(), "Did not process the text from the source. (See GEDCOM X issue 121.)");
          // dqSource.getText(); // see GEDCOM X issue 121 // TODO: address when the associated issue is resolved; log for now
          // sourceDescriptionHasData = true;
        }

        ConfidenceLevel gedxConfidenceLevel = toConfidenceLevel(dqSource.getQuality());
        if (gedxConfidenceLevel != null) {
          Attribution attribution = new Attribution();
          attribution.setConfidence(new TypeReference<ConfidenceLevel>(gedxConfidenceLevel));
          gedxSourceReference.setAttribution(attribution);
          sourceReferenceHasData = true;
        }

        int cntNotes = dqSource.getNotes().size() + dqSource.getNoteRefs().size();
        if (cntNotes > 0) {
          logger.warn(ConversionContext.getContext(), "Did not process {} notes or references to notes.", cntNotes);
        }

        int cntMedia = dqSource.getMedia().size() + dqSource.getMediaRefs().size();
        if (cntMedia > 0) {
          logger.warn(ConversionContext.getContext(), "Did not process {} media items or references to media items.", cntMedia);
        }

        if (sourceDescriptionHasData) {
          result.addDescription(gedxSourceDescription, null);
          sourceReferenceHasData = true;
        }

        if (sourceReferenceHasData) {
          sourceReferences.add(gedxSourceReference);
        }

        if ((!sourceDescriptionHasData) && (!sourceReferenceHasData)) {
          logger.warn(ConversionContext.getContext(), "Source citation did not have any data that was mapped into GEDCOM X");
        }
      } finally {
        ConversionContext.removeReference(sourceContext);
      }
    }

    return sourceReferences.size() > 0 ? sourceReferences : null;
  }

  public static java.util.Date toDate(Change dqChange) {
    if (dqChange == null) {
      return null;
    }
    Marker changeContext = ConversionContext.getDetachedMarker("CHAN");
    ConversionContext.addReference(changeContext);
    java.util.Date date;
    try {
      date = toDate(dqChange.getDateTime());
    } finally {
      ConversionContext.removeReference(changeContext);
    }
    return date;
  }

  private static java.util.Date toDate(DateTime dateTime) {
    List<String> legitimateParsePatterns = dateTime.getTime() != null ? CHAN_DATE_TIME_PARSE_PATTERNS : CHAN_DATE_ONLY_PARSE_PATTERNS;

    String dateTimeString = dateTime.getValue();
    if (dateTime.getTime() != null) {
      dateTimeString += ' ' + dateTime.getTime();
    }

    java.util.Date extractedDate = null;
    for (String parsePattern : legitimateParsePatterns) {
      try {
        extractedDate = parseDateString(parsePattern, dateTimeString);
        break;
      }
      catch (ParseException e) {
      }
    }

    if (extractedDate == null) {
      logger.warn(ConversionContext.getContext(), "Could not parse DATE {}", dateTimeString);
    }

    return extractedDate;
  }

  public static ConfidenceLevel toConfidenceLevel(String dqQuality) {
    ConfidenceLevel confidenceLevel = null;
    if ("3".equals(dqQuality)) {
      confidenceLevel = ConfidenceLevel.Certainly;
    } else if ("2".equals(dqQuality)) {
      confidenceLevel = ConfidenceLevel.Possibly;
    } else if ("1".equals(dqQuality)) {
      confidenceLevel = ConfidenceLevel.Apparently;
    } else if ("0".equals(dqQuality)) {
      confidenceLevel = ConfidenceLevel.Perhaps;
    }
    return confidenceLevel;
  }

  public static java.util.Date parseDateString (String parsePattern, String value) throws ParseException {
    DateFormat dateFormat = DateFormat.getDateTimeInstance();
    ((SimpleDateFormat)dateFormat).applyPattern(parsePattern);
    return dateFormat.parse(value);
  }

  /**
   * @param gedxPersonId to be saved in zip file
   * @return entry name in the zip file for the given resource
   */
  public static String getPersonEntryName(String gedxPersonId) {
    return "persons/" + gedxPersonId;
  }

  public static String getRelationshipEntryName(String id) {
    return "relationships/" + id;
  }

  /**
   * @param gedxDescriptionId resource to be saved in zip file
   * @return entry name in the zip file for the given resource
   */
  public static String getDescriptionEntryName(String gedxDescriptionId) {
    return "descriptions/" + gedxDescriptionId;
  }

  public static String getContributorEntryName(String id) {
    return "contributors/" + id;
  }

  public static String getOrganizationEntryName(String id) {
    return "organizations/" + id;
  }

  /**
   * Creates a GedcomX relationship from gedcom 5 objects.
   * @param personId1
   * @param personId2
   * @param relationshipType
   * @return relationship that was added
   */
  public static Relationship toRelationship(String familyId, String personId1, String personId2, RelationshipType relationshipType) {
    Relationship relationship = new Relationship();

    relationship.setKnownType(relationshipType);
    relationship.setId(familyId + '-' + personId1 + '-' + personId2);
    relationship.setPerson1(toReference(personId1));
    relationship.setPerson2(toReference(personId2));

    return relationship;
  }

  /**
   * Returns a GEDCOM X reference for the given person identifier.
   * @param gedxPersonId the identifier of the GEDCOM X person
   * @return a ResourceReference instance for the person entry of the identified person
   */
  public static ResourceReference toReference(String gedxPersonId) {
    ResourceReference reference = new ResourceReference();
    reference.setResource( new URI(getPersonEntryName(gedxPersonId)));
    return reference;
  }

  public static boolean inCanonicalGlobalFormat(String telephoneNumber) {
    final Pattern pattern = Pattern.compile("^\\+[\\d \\.\\(\\)\\-/]+");
    return pattern.matcher(telephoneNumber).matches();
  }

  public static void populateAgent(Agent agent, String id, String name, org.folg.gedcom.model.Address address, String phone, String fax, String email, String www) {
    agent.setId(id);
    agent.setName(new RDFLiteral(name));

    if(address != null) {
      agent.setAddresses(new ArrayList<Address>());
      Address gedxAddress = new Address();
      gedxAddress.setValue(address.getValue());
      gedxAddress.setCity(address.getCity());
      gedxAddress.setCountry(address.getCountry());
      gedxAddress.setPostalCode(address.getPostalCode());
      gedxAddress.setStateOrProvince(address.getState());
      gedxAddress.setStreet(address.getAddressLine1());
      gedxAddress.setStreet2(address.getAddressLine2());
      gedxAddress.setStreet3(address.getAddressLine3());
      agent.getAddresses().add(gedxAddress);

      // TODO log/warn if address.getName() is valued
    }

    if (phone != null || fax != null) {
      agent.setPhones(new ArrayList<ResourceReference>());
      if (phone != null) {
        ResourceReference phoneRef = new ResourceReference();
        boolean inGlobalFormat = CommonMapper.inCanonicalGlobalFormat(phone);
        String scheme = inGlobalFormat ? "tel:" : "data:,Phone: ";
        phoneRef.setResource(URI.create(scheme + phone));
        agent.getPhones().add(phoneRef);
      }
      if (fax != null) {
        ResourceReference faxRef = new ResourceReference();
        boolean inGlobalFormat = CommonMapper.inCanonicalGlobalFormat(fax);
        String scheme = inGlobalFormat ? "fax:" : "data:,Fax: ";
        faxRef.setResource(URI.create(scheme + fax));
        agent.getPhones().add(faxRef);
      }
    }

    if (email != null) {
      ResourceReference emailRef = new ResourceReference();
      agent.setEmails(new ArrayList<ResourceReference>());
      //TODO catch URI creation exceptions and log/warn
      emailRef.setResource(URI.create("mailto:" + email));
      agent.getEmails().add(emailRef);
    }

    if (www != null) {
      agent.setHomepage(new RDFLiteral(www));
    }
  }

  private CommonMapper() { } // added to remove "major" sonar warning
                             // formatted to minimize impact on code coverage metrics
}
