/**
 * Copyright 2012 Intellectual Reserve, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gedcomx.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

import org.familysearch.platform.ordinances.Ordinance;
import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.parser.ModelParser;
import org.gedcomx.conversion.GedcomxConversionResult;
import org.gedcomx.conversion.gedcom.dq55.GedcomMapper;
import org.gedcomx.conversion.gedcom.dq55.MappingConfig;
import org.gedcomx.fileformat.GedcomxEntrySerializer;
import org.gedcomx.fileformat.GedcomxFile;
import org.gedcomx.fileformat.GedcomxFileEntry;
import org.gedcomx.fileformat.GedcomxOutputStream;
import org.gedcomx.fileformat.GedcomxTimeStampUtil;
import org.gedcomx.fileformat.JacksonJsonSerialization;
import org.gedcomx.rt.GedcomxConstants;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.xml.sax.SAXParseException;


/**
 * Converts a GEDCOM 5.5 file to a GEDCOM X file
 */
public class Gedcom2Gedcomx {

  @Option(name = "-i", aliases = {"--input"}, usage = "GEDCOM 5.5 input file")
  private File gedcomIn;

  @Option(name = "-ix", aliases = {"--inputx"}, usage = "GEDCOM X input file (experimental, used for benchmarking)")
  private File gedcomxIn;

  @Option(name = "-o", aliases = {"--output"}, usage = "GEDCOM X output file")
  private File gedxOut;

  @Option(name = "-fi", aliases = {"--filename-in-ids"}, usage = "Include the input filename in the person and relationship ids in the generated gedcomx")
  private boolean includeFilenameInIds;

  @Option(name = "-P", aliases = {"--pause"}, usage = "Pause before starting the conversion process (experimental, used for profiling)")
  private boolean pause;

  @Option(name = "-v", aliases = {"--verbose"}, usage = "Output all the warnings that are generated during the conversion.")
  private boolean verbose;

  @Option(name = "-vv", aliases = {"--very-verbose"}, usage = "Output all the warnings and informational messages that are generated during the conversion.")
  private boolean vverbose;

  public Gedcom2Gedcomx() {
  }

  private void doMain(CmdLineParser parser) throws SAXParseException, IOException {
    if (verbose) {
      System.setProperty("gedcom-log-level", "WARN");
    }

    if (vverbose) {
      System.setProperty("gedcom-log-level", "INFO");
    }

    if (pause) {
      System.out.print("Press any key to continue...");
      System.in.read();
    }

    List<File> fileList = new ArrayList<>();

    final boolean gedxIn;
    final String scanPattern;
    if (gedcomxIn != null) {
      gedxIn = true;
      scanPattern = "(?i).*\\.gedx$";
      gedcomIn = gedcomxIn;
    } else if (gedcomIn != null) {
      gedxIn = false;
      scanPattern = "(?i).*\\.ged$";
    } else {
      System.err.println("Input file(s) must be specified.");
      parser.printUsage(System.err);
      return;
    }

    boolean gedcomInIsDirectory;
    if (gedcomIn.isDirectory() && gedcomIn.canRead() && gedcomIn.canWrite() && gedcomIn.canExecute()) {
      fileList.addAll(Arrays.asList(gedcomIn.listFiles(pathname -> pathname.getAbsolutePath().matches(scanPattern))));
      gedcomInIsDirectory = true;
    } else {
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
        } else {
          directoryPart = inFile.getAbsolutePath().substring(0, inFile.getAbsolutePath().length() - nameLength);
          if (gedxOut != null) {
            System.out.println("Application output parameter (-o) ignored.");
          }
        }
        derivedGedxOut = new File(directoryPart + name.substring(0, nameLength - 4) + ".gedx");
      } else if (gedxOut == null) {
        String directoryPart = inFile.getAbsolutePath().substring(0, inFile.getAbsolutePath().length() - nameLength);
        derivedGedxOut = new File(directoryPart + name.substring(0, nameLength - 4) + ".gedx");
      } else {
        derivedGedxOut = gedxOut;
      }

      OutputStream outputStream;
      try {
        outputStream = new FileOutputStream(derivedGedxOut);
      } catch (IOException ex) {
        outputStream = null;
        System.err.println("Failed to create the output file: " + derivedGedxOut);
      }

      if (gedxIn) {
        convertXFile(inFile, outputStream);
      } else {
        MappingConfig mappingConfig = new MappingConfig(inFile.getName(), includeFilenameInIds);
        convert55File(inFile, outputStream, mappingConfig);
      }
    }
  }

  private void convertXFile(File inFile, OutputStream outputStream) throws IOException {
    GedcomxFile gxFile = new GedcomxFile(new JarFile(inFile));
    GedcomxOutputStream out = new GedcomxOutputStream(outputStream);
    Map<String, String> attributes = gxFile.getAttributes();
    for (Map.Entry<String, String> attribute : attributes.entrySet()) {
      out.addAttribute(attribute.getKey(), attribute.getValue());
    }

    for (GedcomxFileEntry entry : gxFile.getEntries()) {
      if (!entry.getJarEntry().isDirectory() && !entry.getJarEntry().getName().endsWith("MANIFEST.MF")) {
        Object resource = gxFile.readResource(entry);
        String contentType = entry.getContentType();
        if (contentType == null) {
          contentType = GedcomxConstants.GEDCOMX_XML_MEDIA_TYPE;
        }
        out.addResource(contentType, entry.getJarEntry().getName(), resource, null, entry.getAttributes());
      }
    }

    out.close();
  }

  private void convert55File(File inFile, OutputStream outputStream, MappingConfig mappingConfig) throws SAXParseException, IOException {
    ModelParser modelParser = new ModelParser();
    Gedcom gedcom = modelParser.parseGedcom(inFile);
    gedcom.createIndexes();

    if (outputStream != null) {
      GedcomMapper mapper = new GedcomMapper(mappingConfig);
      GedcomxEntrySerializer serializer;

      String outputFileName = "tree.json";
      serializer = new JacksonJsonSerialization(Ordinance.class);
      GedcomxConversionResult result = mapper.toGedcomx(gedcom);
      GedcomxOutputStream output = new GedcomxOutputStream(outputStream, serializer);

      output.addAttribute("User-Agent", "Gedcom To Gedcomx Java Conversion Utility/1.0");
      output.addAttribute("X-DC-conformsTo", "http://gedcomx.org/file/v1");
      output.addAttribute("X-DC-created", GedcomxTimeStampUtil.formatAsXmlUTC(new Date()));
      if (result.getDatasetContributor() != null && result.getDatasetContributor().getId() != null) {
        output.addAttribute("X-DC-creator", outputFileName + "#" + result.getDatasetContributor().getId());
      }

      output.addResource(outputFileName, result.getDataset(), null);
      output.close();
    }
  }

  public static void main(String[] args) throws SAXParseException, IOException {
    Gedcom2Gedcomx converter = new Gedcom2Gedcomx();
    CmdLineParser parser = new CmdLineParser(converter);
    try {
      parser.parseArgument(args);
      converter.doMain(parser);
    } catch (CmdLineException e) {
      System.err.println(e.getMessage());
      parser.printUsage(System.err);
    }
  }

}
