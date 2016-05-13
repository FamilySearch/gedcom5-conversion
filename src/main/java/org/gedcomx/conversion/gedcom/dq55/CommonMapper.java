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
import org.gedcomx.agent.Address;
import org.gedcomx.agent.Agent;
import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.TextValue;
import org.gedcomx.common.URI;
import org.gedcomx.conclusion.Relationship;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.gedcomx.source.CitationField;
import org.gedcomx.source.SourceDescription;
import org.gedcomx.source.SourceReference;
import org.gedcomx.types.ConfidenceLevel;
import org.gedcomx.types.RelationshipType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URISyntaxException;
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
        SourceDescription gedxSourceDescription = new SourceDescription();

        org.gedcomx.source.SourceCitation citation = new org.gedcomx.source.SourceCitation();
        citation.setCitationTemplate(new ResourceReference(URI.create("gedcom5:citation-template")));
        citation.setFields(new ArrayList<CitationField>());
        citation.setValue("");

        if (dqSource.getRef() != null) {
          gedxSourceDescription.setId(dqSource.getRef() + "-" + Long.toHexString(SequentialIdentifierGenerator.getNextId()));

          SourceReference componentOf = new SourceReference();
          componentOf.setDescriptionRef(URI.create(CommonMapper.getSourceDescriptionReference(dqSource.getRef())));
          gedxSourceDescription.setComponentOf(componentOf);
          sourceDescriptionHasData = true;

          if (dqSource.getDate() != null) {
            CitationField field = new CitationField();
            field.setName(URI.create("gedcom5:citation-template/date"));
            field.setValue(dqSource.getDate());
            citation.getFields().add(field);
            citation.setValue(citation.getValue() + (citation.getValue().length() > 0 ? ", " + dqSource.getDate() : dqSource.getDate()));
          }

          if (dqSource.getPage() != null) {
            CitationField field = new CitationField();
            field.setName(URI.create("gedcom5:citation-template/page"));
            field.setValue(dqSource.getPage());
            citation.getFields().add(field);
            citation.setValue(citation.getValue() + (citation.getValue().length() > 0 ? ", " + dqSource.getPage() : dqSource.getPage()));
          }
        } else if (dqSource.getValue() != null) {
          gedxSourceDescription.setId("SOUR-" + Long.toHexString(SequentialIdentifierGenerator.getNextId()));

          citation.setValue(dqSource.getValue());
          citation.setCitationTemplate(null);
          sourceDescriptionHasData = true;
        }

        String entryName = CommonMapper.getSourceDescriptionReference(gedxSourceDescription.getId());
        SourceReference gedxSourceReference = new SourceReference();
        gedxSourceReference.setDescriptionRef(URI.create(entryName));

        if (dqSource.getText() != null) {
          logger.warn(ConversionContext.getContext(), "GEDCOM X does not currently support text extracted from a source.");
          // dqSource.getText(); // see GEDCOM X issue 121 // TODO: address when the associated issue is resolved; log for now
          // sourceDescriptionHasData = true;
        }

        ConfidenceLevel gedxConfidenceLevel = toConfidenceLevel(dqSource.getQuality());
        if (gedxConfidenceLevel != null) {
          //todo: confidence level on source reference?
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
          gedxSourceDescription.setCitations(Arrays.asList(citation));
          result.addSourceDescription(gedxSourceDescription);
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
        // the parse pattern being tried did not match the string we were given; try the next one
      }
    }

    if (extractedDate == null) {
      // none of the legitimate parse patterns matched the string we were given
      logger.warn(ConversionContext.getContext(), "Could not parse DATE {}", dateTimeString);
    }

    return extractedDate;
  }

  public static ConfidenceLevel toConfidenceLevel(String dqQuality) {
    ConfidenceLevel confidenceLevel;

    if ("3".equals(dqQuality)) {
      confidenceLevel = ConfidenceLevel.High;
    } else if ("2".equals(dqQuality)) {
      confidenceLevel = ConfidenceLevel.Medium;
    } else if ("1".equals(dqQuality)) {
      confidenceLevel = ConfidenceLevel.Low;
    } else if ("0".equals(dqQuality)) {
      confidenceLevel = ConfidenceLevel.Low;
    } else {
      confidenceLevel = null;

      if (dqQuality != null) {
        Marker qualityContext = ConversionContext.getDetachedMarker("QUAY");
        ConversionContext.addReference(qualityContext);
        try {
          logger.warn(ConversionContext.getContext(), "Unrecognized value for QUAL tag {}", dqQuality);
        } finally {
          ConversionContext.removeReference(qualityContext);
        }
      }
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
  public static String getPersonReference(String gedxPersonId) {
    return "#" + gedxPersonId;
  }

  public static String getRelationshipReference(String id) {
    return "#" + id;
  }

  /**
   * @param gedxDescriptionId resource to be saved in zip file
   * @return entry name in the zip file for the given resource
   */
  public static String getSourceDescriptionReference(String gedxDescriptionId) {
    return "#" + gedxDescriptionId;
  }

  public static String getContributorReference(String id) {
    return "#" + id;
  }

  public static String getOrganizationReference(String id) {
    return "#" + id;
  }

  /**
   * Creates a GEDCOM X relationship.
   * @param familyId  the GEDCOM 5.5 identifier associated with the family to which this relationship belongs
   * @param personId1  the GEDCOM 5.5 identifier associated with the person 1
   * @param personId2  the GEDCOM 5.5 identifier associated with the person 1
   * @param relationshipType  the relationship type
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
    reference.setResource( new URI(getPersonReference(gedxPersonId)));
    return reference;
  }

  public static boolean inCanonicalGlobalFormat(String telephoneNumber) {
    final Pattern pattern = Pattern.compile("^\\+[\\d \\.\\(\\)\\-/]+");
    return pattern.matcher(telephoneNumber).matches();
  }

  public static void populateAgent(Agent agent, String id, String name, org.folg.gedcom.model.Address address, String phone, String fax, String email, String www) {
    agent.setId(id);
    agent.setNames(Arrays.asList(new TextValue(name)));

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

      if(address.getName() != null) {
        Marker addressContext = ConversionContext.getDetachedMarker("ADDR");
        ConversionContext.addReference(addressContext);
        try {
          logger.warn(ConversionContext.getContext(), "Ignoring extension tag for address name: {}", address.getName());
        }
        finally {
          ConversionContext.removeReference(addressContext);
        }
      }
    }

    if (phone != null || fax != null) {
      agent.setPhones(new ArrayList<ResourceReference>());
      if (phone != null) {
        ResourceReference phoneRef = new ResourceReference();
        boolean inGlobalFormat = CommonMapper.inCanonicalGlobalFormat(phone);
        String scheme = inGlobalFormat ? "tel" : "data";
        try {
          phoneRef.setResource(URI.create(new java.net.URI(scheme, (inGlobalFormat ? phone : ",Phone: " + phone), null)));
        }
        catch (URISyntaxException e) {
          throw new RuntimeException(e);
        }
        agent.getPhones().add(phoneRef);
      }
      if (fax != null) {
        ResourceReference faxRef = new ResourceReference();
        boolean inGlobalFormat = CommonMapper.inCanonicalGlobalFormat(fax);
        String scheme = inGlobalFormat ? "fax" : "data";
        try {
          faxRef.setResource(URI.create(new java.net.URI(scheme, (inGlobalFormat ? fax : ",Fax: " + fax), null)));
        }
        catch (URISyntaxException e) {
          throw new RuntimeException();
        }
        agent.getPhones().add(faxRef);
      }
    }

    if (email != null) {
      try {
        ResourceReference emailRef = new ResourceReference();
        emailRef.setResource(URI.create(java.net.URI.create("mailto:" + email).toString()));
        agent.setEmails(new ArrayList<ResourceReference>());
        agent.getEmails().add(emailRef);
      }
      catch (RuntimeException ex) {
        Marker emailContext = ConversionContext.getDetachedMarker("EMAIL");
        ConversionContext.addReference(emailContext);
        try {
          logger.warn(ConversionContext.getContext(), "Invalid value for EMAIL ({}) was ignored.", email);
        }
        finally {
          ConversionContext.removeReference(emailContext);
        }
      }
    }

    if (www != null) {
      agent.setHomepage(new ResourceReference(URI.create(www)));
    }
  }

  private CommonMapper() { } // added to remove "major" sonar warning
                             // formatted to minimize impact on code coverage metrics
}
