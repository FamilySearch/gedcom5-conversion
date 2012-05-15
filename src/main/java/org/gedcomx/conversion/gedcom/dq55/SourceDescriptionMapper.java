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

import org.folg.gedcom.model.DateTime;
import org.folg.gedcom.model.Repository;
import org.folg.gedcom.model.Source;
import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.gedcomx.metadata.dc.ObjectFactory;
import org.gedcomx.metadata.foaf.Address;
import org.gedcomx.metadata.foaf.Organization;
import org.gedcomx.metadata.rdf.Description;
import org.gedcomx.metadata.rdf.RDFLiteral;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class SourceDescriptionMapper {
  private ObjectFactory objectFactory = new ObjectFactory();

  public void toSourceDescription(Source dqSource, GedcomxConversionResult result) {
    Description gedxSourceDescription = new Description();

    dqSource.getAbbreviation();
    dqSource.getAuthor();
    dqSource.getCallNumber();
    dqSource.getChange();
    dqSource.getDate();
    dqSource.getExtensions();
    dqSource.getId();
    dqSource.getItalic();
    dqSource.getMedia();
    dqSource.getMediaType();
    dqSource.getMediaRefs();
    dqSource.getNoteRefs();
    dqSource.getNotes();
    dqSource.getParen();
    dqSource.getPublicationFacts();
    dqSource.getReferenceNumber();
    //dqSource.getRepository();
    dqSource.getRepositoryRef();
    dqSource.getRin();
    dqSource.getText();
    dqSource.getTitle();
    dqSource.getType();
    dqSource.getUid();
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
        phone.setResource(URI.create("tel:" + dqRepository.getPhone()));
        gedxOrganization.getPhones().add(phone);
      }
      if (dqRepository.getFax() != null) {
        ResourceReference fax = new ResourceReference();
        fax.setResource(URI.create("fax:" + dqRepository.getFax()));
        gedxOrganization.getPhones().add(fax);
      }
    }

    if (dqRepository.getEmail() != null) {
      gedxOrganization.setEmails(new ArrayList<ResourceReference>());
      ResourceReference email = new ResourceReference();
      email.setResource(URI.create("mailto:" + dqRepository.getEmail()));
      gedxOrganization.getEmails().add(email);
    }

    if (dqRepository.getWww() != null) {
      gedxOrganization.setHomepage(new RDFLiteral(dqRepository.getWww()));
    }

    // TODO: what to do with these?
//    dqRepository.getExtensions();
//    dqRepository.getNoteRefs();
//    dqRepository.getNotes();
//    dqRepository.getRin();
//    dqRepository.getValue(); // expected to always be null

    if (dqRepository.getChange() != null) {
      try {
        DateTime dateTime = dqRepository.getChange().getDateTime();
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
        gedxRepositoryRecordDescription.setAbout(URI.create("organizations/" + gedxOrganization.getId()));
        gedxRepositoryRecordDescription.addExtensionElement(objectFactory.createModifiedElement(lastModified));

        result.addDescription(gedxRepositoryRecordDescription);
      } catch (Throwable ex) {
        // something went wrong, so probably not standard; we will skip it
      }
    }

    result.addOrganization(gedxOrganization);
  }
}