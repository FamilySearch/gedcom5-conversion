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
import org.gedcomx.conversion.GedcomxOutputstreamConversionResult;
import org.gedcomx.conversion.gedcom.dq55.GedcomMapper;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Converts a GEDCOM 5.5 file to a GEDCOM X file
 */
public class Gedcom2Gedcomx {
  private static final Logger logger = LoggerFactory.getLogger(Gedcom2Gedcomx.class);

  @Option (name="-i", required=true, usage="GEDCOM 5.5 input file")
  private File gedcomIn;

  @Option(name="-o", required=false, usage="GEDCOM X output file")
  private File gedxOut;

  private void doMain() throws SAXParseException, IOException {
    List<File> fileList = new ArrayList<File>();

    boolean gedcomInIsDirectory;
    if ((gedcomIn != null) && (gedcomIn.isDirectory()) && (gedcomIn.canRead()) && (gedcomIn.canWrite()) && (gedcomIn.canExecute())) {
      fileList.addAll(Arrays.asList(gedcomIn.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
              return pathname.getAbsolutePath().matches("(?i).*\\.ged$");
            }
          }))
        );
      gedcomInIsDirectory = true;
    }
    else {
      fileList.add(gedcomIn);
      gedcomInIsDirectory = false;
    }

    boolean gedxOutIsDirectory = false;
    if ((gedxOut != null) && (gedxOut.isDirectory()) && (gedcomInIsDirectory) && (gedxOut.canRead()) && (gedxOut.canWrite()) && (gedxOut.canExecute())) {
      gedxOutIsDirectory = true;
    }

    for (File gedcom55File : fileList) {
      ModelParser modelParser = new ModelParser();
      Gedcom gedcom = modelParser.parseGedcom(gedcom55File);
      gedcom.createIndexes();

      String name = gedcom55File.getName();
      int nameLength = name.length();

      File derivedGedxOut;
      if (gedcomInIsDirectory) {
        String directoryPart;
        if (gedxOutIsDirectory) {
          directoryPart = gedxOut.getAbsolutePath() + File.separatorChar;
        }
        else {
          directoryPart = gedcom55File.getAbsolutePath().substring(0, gedcom55File.getAbsolutePath().length() - nameLength);
          if (gedxOut != null) {
            logger.warn("Application output parameter (-o) ignored.");
          }
        }
        derivedGedxOut = new File(directoryPart + name.substring(0, nameLength - 4) + ".gedx");
      }
      else if (gedxOut == null) {
        String directoryPart = gedcom55File.getAbsolutePath().substring(0, gedcom55File.getAbsolutePath().length() - nameLength);
        derivedGedxOut = new File(directoryPart + name.substring(0, nameLength - 4) + ".gedx");
      }
      else {
        derivedGedxOut = gedxOut;
      }

      OutputStream outputStream;
      try {
        outputStream = new FileOutputStream(derivedGedxOut);
      } catch (IOException ex) {
        outputStream = null;
        logger.error("Failed to create the output file: {}", outputStream);
      }

      if (outputStream != null) {
        GedcomMapper mapper = new GedcomMapper();
        GedcomxOutputstreamConversionResult gedxResult = mapper.toGedcomx(gedcom, outputStream);
        gedxResult.finish(true);
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
