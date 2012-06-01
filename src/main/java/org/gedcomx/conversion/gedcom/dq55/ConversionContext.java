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

import org.slf4j.Marker;
import org.slf4j.helpers.BasicMarkerFactory;


public class ConversionContext {
  private static final ThreadLocal<BasicMarkerFactory> factory;

  private static final ThreadLocal<Marker> rootContext;

  static {

    factory = new ThreadLocal<BasicMarkerFactory>() {
        @Override
        protected BasicMarkerFactory initialValue() {
          synchronized (ConversionContext.class) {
            return new BasicMarkerFactory();
          }
        }
      };
    rootContext = new ThreadLocal<Marker>() {
        @Override
        protected Marker initialValue() {
          return factory.get().getDetachedMarker("");
        }
      };
  }

  public static Marker getDetachedMarker(String name) {
    return factory.get().getDetachedMarker(name);
  }

  public static void addReference(Marker reference) {
    rootContext.get().add(reference);
  }

  public static void removeReference(Marker reference) {
    rootContext.get().remove(reference);
  }

  public static Marker getContext() {
    return rootContext.get();
  }

  private ConversionContext() { } // added to remove "major" sonar warning
                                     // formatted to minimize impact on code coverage metrics
}
