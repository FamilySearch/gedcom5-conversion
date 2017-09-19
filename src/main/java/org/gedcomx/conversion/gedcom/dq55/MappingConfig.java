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

/**
 * Created on 9/18/17
 *
 * @author Scott Greenman
 */
public class MappingConfig {
  private String inputFilename;
  private boolean includeFilenameInIds;

  public MappingConfig(String inputFilename, boolean includeFilenameInIds) {
    this.inputFilename = inputFilename;
    this.includeFilenameInIds = includeFilenameInIds;
  }

  public String getInputFilename() {
    return inputFilename;
  }

  public boolean isIncludeFilenameInIds() {
    return includeFilenameInIds;
  }

  public String createId(String id) {
    if (includeFilenameInIds) {
      return inputFilename + ":" + id;
    }
    return id;
  }

}
