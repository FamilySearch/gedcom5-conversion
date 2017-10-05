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
 * An enum to hold the mapping between Gedcom Languages and their corresponding Language Codes.
 *
 * Created by andrewpk on 10/4/17.
 */
public enum LanguageMapping {
  Afrikaans("af"),
  Albanian("sq"),
  Amharic("am"),
  Anglo_Saxon("ang"),
  Arabic("ar"),
  Armenian("hy"),
  Assamese("as"),
  Belorusian("be"),
  Bengali("bn"),
  Braj("bra"),
  Bulgarian("bg"),
  Burmese("my"),
  Cantonese("zh"),
  Catalan("ca"),
  Catalan_Spn(null),
  Church_Slavic("cu"),
  Czech("cs"),
  Danish("da"),
  Dogri("doi"),
  Dutch("nl"),
  English("en"),
  Esperanto("eo"),
  Estonian("et"),
  Faroese("fo"),
  Finnish("fi"),
  French("fr"),
  German("de"),
  Georgian("ka"),
  Greek("el"),
  Gujarati("gu"),
  Hawaiian("haw"),
  Hebrew("he"),
  Hindi("hi"),
  Hungarian("hu"),
  Icelandic("is"),
  Indonesian("id"),
  Italian("it"),
  Japanese("ja"),
  Kannada("kn"),
  Khmer("km"),
  Konkani("kok"),
  Korean("ko"),
  Lahnda("lah"),
  Lao("lo"),
  Latvian("lv"),
  Lithuanian("lt"),
  Macedonian("mk"),
  Maithili("mai"),
  Malayalam("ml"),
  Mandrin("zh"),
  Manipuri("mni"),
  Marathi("mr"),
  Mewari(null),
  Navaho("nv"),
  Nepali("ne"),
  Norwegian("no"),
  Oriya("or"),
  Pahari("him"),
  Pali("pi"),
  Panjabi("pa"),
  Persian("fa"),
  Polish("pl"),
  Portuguese("pt"),
  Prakrit("pra"),
  Pusto("ps"),
  Rajasthani("raj"),
  Romanian("ro"),
  Russian("ru"),
  Sanskrit("sa"),
  Serb("sr"),
  Serbo_Croa("hr"),
  Slovak("sk"),
  Slovene("sl"),
  Spanish("es"),
  Swedish("sv"),
  Tagalog("tl"),
  Tamil("ta"),
  Telugu("te"),
  Thai("th"),
  Tibetan("bo"),
  Turkish("tr"),
  Ukrainian("uk"),
  Urdu("ur"),
  Vietnamese("vi"),
  Wendic(null),
  Yiddish("yi");

  private String languageCode;

  LanguageMapping(String languageCode) {
    this.languageCode = languageCode;
  }

  public String getLanguageCode() {
    return this.languageCode;
  }

  public static LanguageMapping fromString(String text) {
    if(text == null) {
      return null;
    }
    if(text.contains("-")) {
      text.replaceAll("-", "_");
    }
    for(LanguageMapping mapping : LanguageMapping.values()) {
      if(mapping.name().equalsIgnoreCase(text)) {
        return mapping;
      }
    }
    return null;
  }
}
