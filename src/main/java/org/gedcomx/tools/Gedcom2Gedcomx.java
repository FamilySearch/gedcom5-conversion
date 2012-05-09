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
package org.gedcomx.tools;

import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.parser.ModelParser;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.gedcomx.conversion.gedcom.dq55.GedcomMapper;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.xml.sax.SAXParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.LogManager;


/**
 * Converts a GEDCOM 5.5 file to a GEDCOM X file
 */
public class Gedcom2Gedcomx {

  @Option (name="-i", required=true, usage="GEDCOM 5.5 input file")
  private File gedcomIn;

  @Option(name="-o", required=false, usage="GEDCOM X output file")
  private File gedxOut;

  @Option(name="-a", required=false, usage="GEDCOM 5.5 input files")
  private File gedxFilesDir;

  private void doMain() throws SAXParseException, IOException {
    List<File> fileList = new ArrayList<File>();
    if ((gedxFilesDir != null) && (gedxFilesDir.isDirectory()) && (gedxFilesDir.canRead()) && (gedxFilesDir.canWrite()) && (gedxFilesDir.canExecute())) {
      fileList.addAll(Arrays.asList(gedxFilesDir.listFiles(new FileFilter() {
        @Override
        public boolean accept(File pathname) {
          return pathname.getAbsolutePath().matches(".*\\.ged$");
        }
      })));
    }
    if (gedcomIn != null) {
      fileList.add(gedcomIn);
    }

    for (File gedcom55File : fileList) {
      ModelParser modelParser = new ModelParser();
      Gedcom gedcom = modelParser.parseGedcom(gedcom55File);

      if (gedxOut != null) {
        GedcomMapper mapper = new GedcomMapper();
        GedcomxConversionResult gedxResult = mapper.toGedcomx(gedcom);
//        OutputStream outputStream = new FileOutputStream(gedxOut);
//        gedxResult.write(outputStream);
      }
    }
  }

  public static void main(String[] args) throws SAXParseException, IOException {
    Gedcom2Gedcomx converter = new Gedcom2Gedcomx();
    CmdLineParser parser = new CmdLineParser(converter);
    try {
      parser.parseArgument(args);
      converter.doMain();
    }
    catch (CmdLineException e) {
      System.err.println(e.getMessage());
      parser.printUsage(System.err);
    }
  }

}
