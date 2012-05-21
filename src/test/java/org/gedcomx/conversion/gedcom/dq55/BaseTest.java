/**
 * Copyright 2011 Intellectual Reserve, Inc. All Rights reserved.
 */
package org.gedcomx.conversion.gedcom.dq55;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;

/**
 */
public class BaseTest {

  protected ByteArrayOutputStream byteArrayOutputStream = null;

  OutputStream getTestOutputStream() {
    if (byteArrayOutputStream == null)
      byteArrayOutputStream = new ByteArrayOutputStream();
    return byteArrayOutputStream;
  }
}
