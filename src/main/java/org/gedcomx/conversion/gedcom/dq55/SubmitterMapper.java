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

import org.folg.gedcom.model.GedcomTag;
import org.folg.gedcom.model.Submitter;
import org.gedcomx.common.LiteralValue;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.gedcomx.metadata.foaf.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.io.IOException;
import java.util.List;


public class SubmitterMapper {
  private static final Logger logger = LoggerFactory.getLogger(CommonMapper.class);

  public void toContributor(Submitter dqSubmitter, GedcomxConversionResult result) throws IOException {
    Marker submitterContext = ConversionContext.getDetachedMarker(String.format("@%s@ SUBM", dqSubmitter.getId()));
    ConversionContext.addReference(submitterContext);

    Person gedxContributor = new Person();

    CommonMapper.populateAgent(gedxContributor
        , dqSubmitter.getId()
        , dqSubmitter.getName()
        , dqSubmitter.getAddress()
        , dqSubmitter.getPhone()
        , dqSubmitter.getFax()
        , dqSubmitter.getEmail()
        , dqSubmitter.getWww()
      );

    if (dqSubmitter.getLanguage() != null) {
      gedxContributor.setLanguage(new LiteralValue(dqSubmitter.getLanguage()));
    }

    if (dqSubmitter.getRin() != null) {
      logger.warn(ConversionContext.getContext(), "RIN ({}) was ignored.", dqSubmitter.getRin());
    }

    if (dqSubmitter.getValue() != null) {
      logger.warn(ConversionContext.getContext(), "Unexpected submitter value ({}) was ignored.", dqSubmitter.getValue());
    }

    if (dqSubmitter.getExtensions().size() > 0) {
      for (String extensionCategory : dqSubmitter.getExtensions().keySet()) {
        for (GedcomTag tag : ((List<GedcomTag>)dqSubmitter.getExtension(extensionCategory))) {
          logger.warn(ConversionContext.getContext(), "Unsupported ({}): {}", extensionCategory, tag);
          // DATA tag (and subordinates) in GEDCOM 5.5. SOURCE_RECORD not being looked for or parsed by DallanQ code
        }
      }
    }

    result.setDatasetContributor(gedxContributor, CommonMapper.toDate(dqSubmitter.getChange()));

    ConversionContext.removeReference(submitterContext);
  }
}
