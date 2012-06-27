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

import org.codehaus.jackson.smile.SmileFactory;
import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.parser.ModelParser;
import org.gedcomx.conversion.GedcomxOutputstreamConversionResult;
import org.gedcomx.conversion.gedcom.dq55.GedcomMapper;
import org.gedcomx.fileformat.*;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.xml.sax.SAXParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;


/**
 * Converts a GEDCOM 5.5 file to a GEDCOM X file
 */
public class Gedcom2Gedcomx {

  @Option ( name = "-i", aliases = {"--input"}, required = true, usage = "GEDCOM 5.5 input file" )
  private File gedcomIn;

  @Option ( name = "-ix", aliases = {"--inputx"}, required = false, usage = "GEDCOM X input file (experimental, used for benchmarking)" )
  private File gedcomxIn;

  @Option ( name = "-o", aliases = {"--output"}, required = false, usage = "GEDCOM X output file" )
  private File gedxOut;

  @Option ( name = "-js", aliases = {"--json"}, required = false, usage = "Use JSON instead of XML for serialization (experimental, used for proof-of-concept)" )
  private boolean json;

  @Option ( name = "-bs", aliases = {"--bson"}, required = false, usage = "Use binary JSON instead of XML for serialization (experimental, used for proof-of-concept)" )
  private boolean bson;

  @Option ( name = "-v", aliases = {"--verbose"}, required = false, usage = "Output all the warnings that are generated during the conversion." )
  private boolean verbose;

  @Option ( name = "-vv", aliases = {"--very-verbose"}, required = false, usage = "Output all the warnings and informational messages that are generated during the conversion." )
  private boolean vverbose;

  private void doMain() throws SAXParseException, IOException {
    if (verbose) {
      System.setProperty("gedcom-log-level", "WARN");
    }

    if (vverbose) {
      System.setProperty("gedcom-log-level", "INFO");
    }

    List<File> fileList = new ArrayList<File>();

    final boolean gedxIn;
    final String scanPattern;
    if (gedcomxIn != null) {
      gedxIn = true;
      scanPattern = "(?i).*\\.gedx$";
      gedcomIn = gedcomxIn;
    }
    else {
      gedxIn = false;
      scanPattern = "(?i).*\\.ged$";
    }

    boolean gedcomInIsDirectory;
    if ((gedcomIn != null) && (gedcomIn.isDirectory()) && (gedcomIn.canRead()) && (gedcomIn.canWrite()) && (gedcomIn.canExecute())) {
      fileList.addAll(Arrays.asList(gedcomIn.listFiles(new FileFilter() {
        @Override
        public boolean accept(File pathname) {
          return pathname.getAbsolutePath().matches(scanPattern);
        }
      })));
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

    for (File inFile : fileList) {
      String name = inFile.getName();
      int nameLength = name.length();

      File derivedGedxOut;
      if (gedcomInIsDirectory) {
        String directoryPart;
        if (gedxOutIsDirectory) {
          directoryPart = gedxOut.getAbsolutePath() + File.separatorChar;
        }
        else {
          directoryPart = inFile.getAbsolutePath().substring(0, inFile.getAbsolutePath().length() - nameLength);
          if (gedxOut != null) {
            System.out.println("Application output parameter (-o) ignored.");
          }
        }
        derivedGedxOut = new File(directoryPart + name.substring(0, nameLength - 4) + ".gedx");
      }
      else if (gedxOut == null) {
        String directoryPart = inFile.getAbsolutePath().substring(0, inFile.getAbsolutePath().length() - nameLength);
        derivedGedxOut = new File(directoryPart + name.substring(0, nameLength - 4) + ".gedx");
      }
      else {
        derivedGedxOut = gedxOut;
      }

      OutputStream outputStream;
      try {
        outputStream = new FileOutputStream(derivedGedxOut);
      }
      catch (IOException ex) {
        outputStream = null;
        System.err.println("Failed to create the output file: " + derivedGedxOut);
      }

      if (gedxIn) {
        convertXFile(inFile, outputStream);
      }
      else {
        convert55File(inFile, outputStream);
      }
    }
  }

  private void convertXFile(File inFile, OutputStream outputStream) throws SAXParseException, IOException {
    GedcomxFile gxFile = new GedcomxFile(new JarFile(inFile));
    GedcomxOutputStream out = new GedcomxOutputStream(outputStream);
    Map<String,String> attributes = gxFile.getAttributes();
    for (Map.Entry<String, String> attribute : attributes.entrySet()) {
      out.addAttribute(attribute.getKey(), attribute.getValue());
    }

    for (GedcomxFileEntry entry : gxFile.getEntries()) {
      Object resource = gxFile.readResource(entry);
      out.addResource(entry.getContentType(), entry.getJarEntry().getName(), resource, null, entry.getAttributes());
    }

    out.close();
  }

  private void convert55File(File inFile, OutputStream outputStream) throws SAXParseException, IOException {
    ModelParser modelParser = new ModelParser();
    Gedcom gedcom = modelParser.parseGedcom(inFile);
    gedcom.createIndexes();

    if (outputStream != null) {
      GedcomMapper mapper = new GedcomMapper();
      GedcomxEntrySerializer serializer;

      if (json) {
        serializer = new JacksonJsonSerialization();
      }
      else if (bson) {
        serializer = new JacksonJsonSerialization(new SmileFactory());
      }
      else {
        serializer = new DefaultXMLSerialization();
      }

      GedcomxOutputstreamConversionResult result = new GedcomxOutputstreamConversionResult(new GedcomxOutputStream(outputStream, serializer));
      mapper.toGedcomx(gedcom, result);
      result.finish(true);
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
