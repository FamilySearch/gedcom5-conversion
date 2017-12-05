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

import org.folg.gedcom.model.Person;

/**
 * A PostProcessor can be used to map information that is not mapped by the gedcom5-conversion code.
 * To use a PostProcessor, create a custom class that implements this interface, then pass it in as
 * a parameter to the GedcomMapper constructor. It is called for each individual in the gedcom file
 * after that individual has been mapped to a GEDCOMX Person. Your PostProcessor can read information
 * from the provided org.folg.gedcom.model.Person and use it to add information to or modify the
 * provided org.gedcomx.conclusion.Person.
 *
 * There isn't support for supplying a PostProcessor to be used when run from the command-line.
 *
 * Created on 12/5/17
 *
 * @author Scott Greenman
 */
public interface PostProcessor {
  void postProcessPerson(Person gedcomPerson, org.gedcomx.conclusion.Person gedxPerson);
}
