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

import org.gedcomx.conversion.gedcom.dq55.GedcomMapper;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class Gedcom2GedcomxTest {
  @Test
  public void testMain() throws Exception {
    URL gedcomUrl = GedcomMapper.class.getClassLoader().getResource("Case009-Family.ged");
    String ouputFile = System.getProperty("user.dir") + "/target/test.gedx";
    String[] args = new String[] {"-i", gedcomUrl.getPath(), "-o", ouputFile};
    Gedcom2Gedcomx.main(args);
    assertTrue(new File(ouputFile).exists());
    JarFile jarFile = new JarFile(ouputFile);
    assertEquals(jarFile.getManifest().getEntries().size(), 54);
//    listContents(ouputFile);
  }

  private static void listContents(String ouputFile) throws IOException {
    JarFile jarFile = new JarFile(ouputFile);
    System.out.print("Main Attributes: ");
    printAttributes(jarFile.getManifest().getMainAttributes());
    Enumeration entries = jarFile.entries();
    while (entries.hasMoreElements()) {
      printEntry(entries.nextElement());
    }
  }

  private static void printEntry(Object jarEntry) throws IOException {
    JarEntry entry = (JarEntry)jarEntry;
    String name = entry.getName();
    long size = entry.getSize();
    long compressedSize = entry.getCompressedSize();

    Attributes attributes = entry.getAttributes();
    System.out.print(name + "\t" + size + "\t" + compressedSize + "\t");
    printAttributes(attributes);
  }

  private static void printAttributes(Attributes attributes) {
    if (attributes != null ) {
      System.out.print("[");
      String sep = "";
      for (Object attrKey : attributes.keySet()) {
        System.out.print(sep + attrKey + ":" + attributes.get(attrKey));
        sep = ",";
      }
      System.out.println("]");
    }
  }

  private static void printProp(String prop) {
    System.out.println(prop + "=" + System.getProperty(prop));
  }
}
