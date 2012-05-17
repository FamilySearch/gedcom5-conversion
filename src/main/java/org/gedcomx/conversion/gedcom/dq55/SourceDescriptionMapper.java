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
import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.gedcomx.metadata.dc.ObjectFactory;
import org.gedcomx.metadata.foaf.Address;
import org.gedcomx.metadata.foaf.Organization;
import org.gedcomx.metadata.rdf.Description;
import org.gedcomx.metadata.rdf.RDFLiteral;
import org.gedcomx.metadata.rdf.RDFValue;
import org.gedcomx.types.ResourceType;
import org.gedcomx.types.TypeReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;


public class SourceDescriptionMapper {
  private ObjectFactory objectFactory = new ObjectFactory();

  public void toSourceDescription(Source dqSource, GedcomxConversionResult result) {
    Description gedxSourceDescription = new Description();
    gedxSourceDescription.setId(dqSource.getId());

    if (dqSource.getAuthor() != null) {
      gedxSourceDescription.addExtensionElement(objectFactory.createCreatorElement(new RDFValue(dqSource.getAuthor())));
    }

    if (dqSource.getTitle() != null) {
      gedxSourceDescription.addExtensionElement(objectFactory.createTitleElement(new RDFLiteral(dqSource.getTitle())));
    }

    if (dqSource.getAbbreviation() != null) {
      gedxSourceDescription.addExtensionElement(objectFactory.createAlternativeElement(new RDFLiteral(dqSource.getAbbreviation())));
    }

    if (dqSource.getPublicationFacts() != null) {
      gedxSourceDescription.addExtensionElement(objectFactory.createDescriptionElement(new RDFValue(dqSource.getPublicationFacts())));
    }

    if (dqSource.getText() != null) {
      // dqSource.getText(); // see GEDCOM X issue 121 // TODO: address when the associated issue is resolved; log for now
    }

    if (dqSource.getRepositoryRef() != null) {
      RepositoryRef dqRepositoryRef = dqSource.getRepositoryRef();
      gedxSourceDescription.addExtensionElement(objectFactory.createIsPartOfElement(new RDFValue("organizations/" + dqRepositoryRef.getRef())));

      if (dqRepositoryRef.getCallNumber() != null) {
        gedxSourceDescription.addExtensionElement(objectFactory.createIdentifierElement(new RDFLiteral(dqRepositoryRef.getCallNumber())));
      }

      if (dqRepositoryRef.getMediaType() != null) {
        TypeReference<ResourceType> mediaTypeRef = mapToKnownResourceType(dqRepositoryRef.getMediaType());
        if (mediaTypeRef != null) {
          gedxSourceDescription.setType(mediaTypeRef);
        }
      }
    }

    if (dqSource.getCallNumber() != null) {
      gedxSourceDescription.addExtensionElement(objectFactory.createIdentifierElement(new RDFLiteral(dqSource.getCallNumber())));
    }

    if (dqSource.getMediaType() != null) {
      TypeReference<ResourceType> mediaTypeRef = mapToKnownResourceType(dqSource.getMediaType());
      if (mediaTypeRef != null) {
        gedxSourceDescription.setType(mediaTypeRef);
      }
    }

    // TODO: add logging for fields we are not processing right now
    // DATA tag (and subordinates) in GEDCOM 5.5. SOURCE_RECORD not being looked for, parsed by DallanQ code
//    dqSource.getDate(); // Anyone know what sort of date this is? It is a deviation from the GEDCOM 5.5 spec.
//    dqSource.getNoteRefs();
//    dqSource.getNotes();
//    dqSource.getMediaRefs();
//    dqSource.getMedia();
//    dqSource.getUid();
//    dqSource.getRin();
//    dqSource.getExtensions();
//    dqSource.getReferenceNumber();
//    dqSource.getType();

    //dqSource.getItalic(); // PAF extension elements; will not process
    //dqSource.getParen();  // PAF extension elements; will not process

    if (dqSource.getChange() != null) {
      toChangeDescription(dqSource.getChange(), "sources/" + gedxSourceDescription.getId(), result);
    }

    result.addDescription(gedxSourceDescription);
  }

  public void toOrganization(Repository dqRepository, GedcomxConversionResult result) {
    Organization gedxOrganization = new Organization();
    gedxOrganization.setId(dqRepository.getId());

    gedxOrganization.setName(new RDFLiteral(dqRepository.getName()));

    final org.folg.gedcom.model.Address dqAddress = dqRepository.getAddress();
    if (dqAddress != null) {
      Address gedxAddress = new Address();

      gedxAddress.setValue(dqAddress.getValue());
      gedxAddress.setStreet(dqAddress.getAddressLine1());
      gedxAddress.setStreet2(dqAddress.getAddressLine2());
      gedxAddress.setStreet3(dqAddress.getAddressLine3());
      gedxAddress.setCity(dqAddress.getCity());
      gedxAddress.setStateOrProvince(dqAddress.getState());
      gedxAddress.setPostalCode(dqAddress.getPostalCode());
      gedxAddress.setCountry(dqAddress.getCountry());

      gedxOrganization.setAddresses(Arrays.asList(gedxAddress));
    }

    if ((dqRepository.getPhone() != null) || (dqRepository.getFax() != null)) {
      gedxOrganization.setPhones(new ArrayList<ResourceReference>());
      if (dqRepository.getPhone() != null) {
        ResourceReference phone = new ResourceReference();
        boolean inGlobalFormat = inCanonicalGlobalFormat(dqRepository.getPhone());
        String scheme = inGlobalFormat ? "tel:" : "data:,Phone: ";
        phone.setResource(URI.create(scheme + dqRepository.getPhone()));
        gedxOrganization.getPhones().add(phone);
      }
      if (dqRepository.getFax() != null) {
        ResourceReference fax = new ResourceReference();
        boolean inGlobalFormat = inCanonicalGlobalFormat(dqRepository.getFax());
        String scheme = inGlobalFormat ? "fax:" : "data:,Fax: ";
        fax.setResource(URI.create(scheme + dqRepository.getFax()));
        gedxOrganization.getPhones().add(fax);
      }
    }

    if (dqRepository.getEmail() != null) {
      ResourceReference email = new ResourceReference();
      gedxOrganization.setEmails(new ArrayList<ResourceReference>());
      email.setResource(URI.create("mailto:" + dqRepository.getEmail()));
      gedxOrganization.getEmails().add(email);
    }

    if (dqRepository.getWww() != null) {
      gedxOrganization.setHomepage(new RDFLiteral(dqRepository.getWww()));
    }

    // TODO: add logging for fields we are not processing right now
//    dqRepository.getExtensions();
//    dqRepository.getNoteRefs();
//    dqRepository.getNotes();
//    dqRepository.getRin();
//    dqRepository.getValue(); // expected to always be null

    if (dqRepository.getChange() != null) {
      toChangeDescription(dqRepository.getChange(), "organizations/" + gedxOrganization.getId(), result);
    }

    result.addOrganization(gedxOrganization);
  }

  private TypeReference<ResourceType> mapToKnownResourceType(String mediaType) {
    TypeReference<ResourceType> resourceTypeRef;

    if ("audio".equalsIgnoreCase(mediaType)) {
      resourceTypeRef = new TypeReference<ResourceType>(ResourceType.Sound);
    } else if ("book".equalsIgnoreCase(mediaType)) {
      resourceTypeRef = new TypeReference<ResourceType>(ResourceType.PhysicalObject);
    } else if ("card".equalsIgnoreCase(mediaType)) {
      resourceTypeRef = new TypeReference<ResourceType>(ResourceType.PhysicalObject);
    } else if ("electronic".equalsIgnoreCase(mediaType)) {
      resourceTypeRef = null;
    } else if ("fiche".equalsIgnoreCase(mediaType)) {
      resourceTypeRef = new TypeReference<ResourceType>(ResourceType.PhysicalObject);
    } else if ("film".equalsIgnoreCase(mediaType)) {
      resourceTypeRef = new TypeReference<ResourceType>(ResourceType.PhysicalObject);
    } else if ("magazine".equalsIgnoreCase(mediaType)) {
      resourceTypeRef = new TypeReference<ResourceType>(ResourceType.PhysicalObject);
    } else if ("manuscript".equalsIgnoreCase(mediaType)) {
      resourceTypeRef = new TypeReference<ResourceType>(ResourceType.PhysicalObject);
    } else if ("map".equalsIgnoreCase(mediaType)) {
      resourceTypeRef = new TypeReference<ResourceType>(ResourceType.PhysicalObject);
    } else if ("newspaper".equalsIgnoreCase(mediaType)) {
      resourceTypeRef = new TypeReference<ResourceType>(ResourceType.PhysicalObject);
    } else if ("photo".equalsIgnoreCase(mediaType)) {
      resourceTypeRef = new TypeReference<ResourceType>(ResourceType.StillImage);
    } else if ("tombstone".equalsIgnoreCase(mediaType)) {
      resourceTypeRef = new TypeReference<ResourceType>(ResourceType.PhysicalObject);
    } else if ("video".equalsIgnoreCase(mediaType)) {
      resourceTypeRef = new TypeReference<ResourceType>(ResourceType.MovingImage);
    } else {
      resourceTypeRef = null;
    }

    return resourceTypeRef;
  }

  private void toChangeDescription(Change dqRepositoryChange, String aboutObjId, GedcomxConversionResult result) {
    if (dqRepositoryChange != null) {
      try {
        DateTime dateTime = dqRepositoryChange.getDateTime();
        String parsePattern = "d MMM yyyy";
        if (dateTime.getTime() != null) {
          parsePattern += " HH:mm:ss.SSS";
        }
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        ((SimpleDateFormat)dateFormat).applyPattern(parsePattern);

        String dateTimeString = dateTime.getValue();
        if (dateTime.getTime() != null) {
          dateTimeString += ' ' + dateTime.getTime();
        }
        Date date = dateFormat.parse(dateTimeString);

        RDFLiteral lastModified = new RDFLiteral(date);

        Description gedxRepositoryRecordDescription = new Description();
        gedxRepositoryRecordDescription.setAbout(URI.create(aboutObjId));
        gedxRepositoryRecordDescription.addExtensionElement(objectFactory.createModifiedElement(lastModified));

        result.addDescription(gedxRepositoryRecordDescription);
      } catch (Throwable ex) {
        // something went wrong, so probably does not conform to the standard; we will skip it
      }
    }
  }

  private boolean inCanonicalGlobalFormat(String telephoneNumber) {
    final Pattern pattern = Pattern.compile("^\\+[\\d \\.\\(\\)\\-/]+");
    return pattern.matcher(telephoneNumber).matches();
  }
}