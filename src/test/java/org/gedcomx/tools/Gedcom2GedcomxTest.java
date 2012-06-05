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
//    listContents(ouputFile);
    JarFile jarFile = new JarFile(ouputFile);
    assertEquals(jarFile.getManifest().getEntries().size(), 71);
/*
Following are a count of entries that are expected in the gedcom file:

0 @SUB1@ SUBM

31 Descriptions (1 on @F24@ not created)
---------------
0 @S1@ SOUR (7 refs)
0 @S5@ SOUR (9 refs)
0 @S13@ SOUR (2 refs)
0 @S17@ SOUR (5 refs)
0 @S80@ SOUR (4 refs)

0 @REPO1@ REPO
0 @REPO14@ REPO
0 @REPO15@ REPO

13 individuals
--------------
I1 I2 I8 I11 I14 I15 I117 I1000 I1001 I1002 I1003 I1004 I1005

23 relationships
----------------
0 @F1@ FAM (5 relationships)
1 HUSB @I1@
1 WIFE @I11@
1 CHIL @I2@
1 CHIL @I8@

0 @F2@ FAM (3 relationships)
1 HUSB @I15@
1 WIFE @I14@
1 CHIL @I1@

0 @F10@ FAM (1 relationships)
1 HUSB @I117@
1 WIFE @I14@

0 @F20@ FAM (9 relationships)
1 HUSB @I1000@
1 WIFE @I1001@
1 CHIL @I1002@
1 CHIL @I1003@
1 CHIL @I1004@
1 CHIL @I1005@

0 @F21@ FAM (3 relationships)
1 HUSB @I1000@
1 WIFE @I1001@
1 CHIL @I1005@

0 @F22@ FAM (1 relationships)
1 HUSB @I1000@
1 CHIL @I1005@

0 @F23@ FAM (1 relationships)
1 WIFE @I1001@
1 CHIL @I1005@

0 @F24@ FAM (0 relationships)
1 CHIL @I1004@
1 CHIL @I1005@
 */
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
