/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.text.translate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class holding various entity data for HTML and XML - generally for use with
 * the LookupTranslator.
 * All Maps are generated using {@code java.util.Collections.unmodifiableMap()}.
 *
 * @since 1.0
 */
public class EntityArrays {

   /**
     * A Map&lt;CharSequence, CharSequence&gt; to to escape
     * <a href="https://secure.wikimedia.org/wikipedia/en/wiki/ISO/IEC_8859-1">ISO-8859-1</a>
     * characters to their named HTML 3.x equivalents.
     */
    public static final Map<CharSequence, CharSequence> ISO8859_1_ESCAPE;
    static {
        final Map<CharSequence, CharSequence> initialMap = new HashMap<>();
        initialMap.put("\u00A0", "&nbsp;"); // non-breaking space
        initialMap.put("\u00A1", "&iexcl;"); // inverted exclamation mark
        initialMap.put("\u00A2", "&cent;"); // cent sign
        initialMap.put("\u00A3", "&pound;"); // pound sign
        initialMap.put("\u00A4", "&curren;"); // currency sign
        initialMap.put("\u00A5", "&yen;"); // yen sign = yuan sign
        initialMap.put("\u00A6", "&brvbar;"); // broken bar = broken vertical bar
        initialMap.put("\u00A7", "&sect;"); // section sign
        initialMap.put("\u00A8", "&uml;"); // diaeresis = spacing diaeresis
        initialMap.put("\u00A9", "&copy;"); // © - copyright sign
        initialMap.put("\u00AA", "&ordf;"); // feminine ordinal indicator
        initialMap.put("\u00AB", "&laquo;"); // left-pointing double angle quotation mark = left pointing guillemet
        initialMap.put("\u00AC", "&not;"); // not sign
        initialMap.put("\u00AD", "&shy;"); // soft hyphen = discretionary hyphen
        initialMap.put("\u00AE", "&reg;"); // ® - registered trademark sign
        initialMap.put("\u00AF", "&macr;"); // macron = spacing macron = overline = APL overbar
        initialMap.put("\u00B0", "&deg;"); // degree sign
        initialMap.put("\u00B1", "&plusmn;"); // plus-minus sign = plus-or-minus sign
        initialMap.put("\u00B2", "&sup2;"); // superscript two = superscript digit two = squared
        initialMap.put("\u00B3", "&sup3;"); // superscript three = superscript digit three = cubed
        initialMap.put("\u00B4", "&acute;"); // acute accent = spacing acute
        initialMap.put("\u00B5", "&micro;"); // micro sign
        initialMap.put("\u00B6", "&para;"); // pilcrow sign = paragraph sign
        initialMap.put("\u00B7", "&middot;"); // middle dot = Georgian comma = Greek middle dot
        initialMap.put("\u00B8", "&cedil;"); // cedilla = spacing cedilla
        initialMap.put("\u00B9", "&sup1;"); // superscript one = superscript digit one
        initialMap.put("\u00BA", "&ordm;"); // masculine ordinal indicator
        initialMap.put("\u00BB", "&raquo;"); // right-pointing double angle quotation mark = right pointing guillemet
        initialMap.put("\u00BC", "&frac14;"); // vulgar fraction one quarter = fraction one quarter
        initialMap.put("\u00BD", "&frac12;"); // vulgar fraction one half = fraction one half
        initialMap.put("\u00BE", "&frac34;"); // vulgar fraction three quarters = fraction three quarters
        initialMap.put("\u00BF", "&iquest;"); // inverted question mark = turned question mark
        initialMap.put("\u00C0", "&Agrave;"); // À - uppercase A, grave accent
        initialMap.put("\u00C1", "&Aacute;"); // Á - uppercase A, acute accent
        initialMap.put("\u00C2", "&Acirc;"); // Â - uppercase A, circumflex accent
        initialMap.put("\u00C3", "&Atilde;"); // Ã - uppercase A, tilde
        initialMap.put("\u00C4", "&Auml;"); // Ä - uppercase A, umlaut
        initialMap.put("\u00C5", "&Aring;"); // � - uppercase A, ring
        initialMap.put("\u00C6", "&AElig;"); // Æ - uppercase AE
        initialMap.put("\u00C7", "&Ccedil;"); // Ç - uppercase C, cedilla
        initialMap.put("\u00C8", "&Egrave;"); // È - uppercase E, grave accent
        initialMap.put("\u00C9", "&Eacute;"); // É - uppercase E, acute accent
        initialMap.put("\u00CA", "&Ecirc;"); // Ê - uppercase E, circumflex accent
        initialMap.put("\u00CB", "&Euml;"); // Ë - uppercase E, umlaut
        initialMap.put("\u00CC", "&Igrave;"); // Ì - uppercase I, grave accent
        initialMap.put("\u00CD", "&Iacute;"); // Í - uppercase I, acute accent
        initialMap.put("\u00CE", "&Icirc;"); // Î - uppercase I, circumflex accent
        initialMap.put("\u00CF", "&Iuml;"); // Ï - uppercase I, umlaut
        initialMap.put("\u00D0", "&ETH;"); // Ð - uppercase Eth, Icelandic
        initialMap.put("\u00D1", "&Ntilde;"); // Ñ - uppercase N, tilde
        initialMap.put("\u00D2", "&Ograve;"); // Ò - uppercase O, grave accent
        initialMap.put("\u00D3", "&Oacute;"); // Ó - uppercase O, acute accent
        initialMap.put("\u00D4", "&Ocirc;"); // Ô - uppercase O, circumflex accent
        initialMap.put("\u00D5", "&Otilde;"); // Õ - uppercase O, tilde
        initialMap.put("\u00D6", "&Ouml;"); // Ö - uppercase O, umlaut
        initialMap.put("\u00D7", "&times;"); // multiplication sign
        initialMap.put("\u00D8", "&Oslash;"); // Ø - uppercase O, slash
        initialMap.put("\u00D9", "&Ugrave;"); // Ù - uppercase U, grave accent
        initialMap.put("\u00DA", "&Uacute;"); // Ú - uppercase U, acute accent
        initialMap.put("\u00DB", "&Ucirc;"); // Û - uppercase U, circumflex accent
        initialMap.put("\u00DC", "&Uuml;"); // Ü - uppercase U, umlaut
        initialMap.put("\u00DD", "&Yacute;"); // Ý - uppercase Y, acute accent
        initialMap.put("\u00DE", "&THORN;"); // Þ - uppercase THORN, Icelandic
        initialMap.put("\u00DF", "&szlig;"); // ß - lowercase sharps, German
        initialMap.put("\u00E0", "&agrave;"); // à - lowercase a, grave accent
        initialMap.put("\u00E1", "&aacute;"); // á - lowercase a, acute accent
        initialMap.put("\u00E2", "&acirc;"); // â - lowercase a, circumflex accent
        initialMap.put("\u00E3", "&atilde;"); // ã - lowercase a, tilde
        initialMap.put("\u00E4", "&auml;"); // ä - lowercase a, umlaut
        initialMap.put("\u00E5", "&aring;"); // å - lowercase a, ring
        initialMap.put("\u00E6", "&aelig;"); // æ - lowercase ae
        initialMap.put("\u00E7", "&ccedil;"); // ç - lowercase c, cedilla
        initialMap.put("\u00E8", "&egrave;"); // è - lowercase e, grave accent
        initialMap.put("\u00E9", "&eacute;"); // é - lowercase e, acute accent
        initialMap.put("\u00EA", "&ecirc;"); // ê - lowercase e, circumflex accent
        initialMap.put("\u00EB", "&euml;"); // ë - lowercase e, umlaut
        initialMap.put("\u00EC", "&igrave;"); // ì - lowercase i, grave accent
        initialMap.put("\u00ED", "&iacute;"); // í - lowercase i, acute accent
        initialMap.put("\u00EE", "&icirc;"); // î - lowercase i, circumflex accent
        initialMap.put("\u00EF", "&iuml;"); // ï - lowercase i, umlaut
        initialMap.put("\u00F0", "&eth;"); // ð - lowercase eth, Icelandic
        initialMap.put("\u00F1", "&ntilde;"); // ñ - lowercase n, tilde
        initialMap.put("\u00F2", "&ograve;"); // ò - lowercase o, grave accent
        initialMap.put("\u00F3", "&oacute;"); // ó - lowercase o, acute accent
        initialMap.put("\u00F4", "&ocirc;"); // ô - lowercase o, circumflex accent
        initialMap.put("\u00F5", "&otilde;"); // õ - lowercase o, tilde
        initialMap.put("\u00F6", "&ouml;"); // ö - lowercase o, umlaut
        initialMap.put("\u00F7", "&divide;"); // division sign
        initialMap.put("\u00F8", "&oslash;"); // ø - lowercase o, slash
        initialMap.put("\u00F9", "&ugrave;"); // ù - lowercase u, grave accent
        initialMap.put("\u00FA", "&uacute;"); // ú - lowercase u, acute accent
        initialMap.put("\u00FB", "&ucirc;"); // û - lowercase u, circumflex accent
        initialMap.put("\u00FC", "&uuml;"); // ü - lowercase u, umlaut
        initialMap.put("\u00FD", "&yacute;"); // ý - lowercase y, acute accent
        initialMap.put("\u00FE", "&thorn;"); // þ - lowercase thorn, Icelandic
        initialMap.put("\u00FF", "&yuml;"); // ÿ - lowercase y, umlaut
        ISO8859_1_ESCAPE = Collections.unmodifiableMap(initialMap);
    }

    /**
     * Reverse of {@link #ISO8859_1_ESCAPE} for unescaping purposes.
     */
    public static final Map<CharSequence, CharSequence> ISO8859_1_UNESCAPE;

    static {
        ISO8859_1_UNESCAPE = Collections.unmodifiableMap(invert(ISO8859_1_ESCAPE));
    }

    /**
     * A Map&lt;CharSequence, CharSequence&gt; to escape additional
     * <a href="http://www.w3.org/TR/REC-html40/sgml/entities.html">character entity
     * references</a>. Note that this must be used with {@link #BASIC_ESCAPE} and {@link #ISO8859_1_ESCAPE} to get the full list of
     * HTML 4.0 character entities.
     */
    public static final Map<CharSequence, CharSequence> HTML40_EXTENDED_ESCAPE;

    static {
        final Map<CharSequence, CharSequence> initialMap = new HashMap<>();
        // <!-- Latin Extended-B -->
        initialMap.put("\u0192", "&fnof;"); // latin small f with hook = function= florin, U+0192 ISOtech -->
        // <!-- Greek -->
        initialMap.put("\u0391", "&Alpha;"); // greek capital letter alpha, U+0391 -->
        initialMap.put("\u0392", "&Beta;"); // greek capital letter beta, U+0392 -->
        initialMap.put("\u0393", "&Gamma;"); // greek capital letter gamma,U+0393 ISOgrk3 -->
        initialMap.put("\u0394", "&Delta;"); // greek capital letter delta,U+0394 ISOgrk3 -->
        initialMap.put("\u0395", "&Epsilon;"); // greek capital letter epsilon, U+0395 -->
        initialMap.put("\u0396", "&Zeta;"); // greek capital letter zeta, U+0396 -->
        initialMap.put("\u0397", "&Eta;"); // greek capital letter eta, U+0397 -->
        initialMap.put("\u0398", "&Theta;"); // greek capital letter theta,U+0398 ISOgrk3 -->
        initialMap.put("\u0399", "&Iota;"); // greek capital letter iota, U+0399 -->
        initialMap.put("\u039A", "&Kappa;"); // greek capital letter kappa, U+039A -->
        initialMap.put("\u039B", "&Lambda;"); // greek capital letter lambda,U+039B ISOgrk3 -->
        initialMap.put("\u039C", "&Mu;"); // greek capital letter mu, U+039C -->
        initialMap.put("\u039D", "&Nu;"); // greek capital letter nu, U+039D -->
        initialMap.put("\u039E", "&Xi;"); // greek capital letter xi, U+039E ISOgrk3 -->
        initialMap.put("\u039F", "&Omicron;"); // greek capital letter omicron, U+039F -->
        initialMap.put("\u03A0", "&Pi;"); // greek capital letter pi, U+03A0 ISOgrk3 -->
        initialMap.put("\u03A1", "&Rho;"); // greek capital letter rho, U+03A1 -->
        // <!-- there is no Sigmaf, and no U+03A2 character either -->
        initialMap.put("\u03A3", "&Sigma;"); // greek capital letter sigma,U+03A3 ISOgrk3 -->
        initialMap.put("\u03A4", "&Tau;"); // greek capital letter tau, U+03A4 -->
        initialMap.put("\u03A5", "&Upsilon;"); // greek capital letter upsilon,U+03A5 ISOgrk3 -->
        initialMap.put("\u03A6", "&Phi;"); // greek capital letter phi,U+03A6 ISOgrk3 -->
        initialMap.put("\u03A7", "&Chi;"); // greek capital letter chi, U+03A7 -->
        initialMap.put("\u03A8", "&Psi;"); // greek capital letter psi,U+03A8 ISOgrk3 -->
        initialMap.put("\u03A9", "&Omega;"); // greek capital letter omega,U+03A9 ISOgrk3 -->
        initialMap.put("\u03B1", "&alpha;"); // greek small letter alpha,U+03B1 ISOgrk3 -->
        initialMap.put("\u03B2", "&beta;"); // greek small letter beta, U+03B2 ISOgrk3 -->
        initialMap.put("\u03B3", "&gamma;"); // greek small letter gamma,U+03B3 ISOgrk3 -->
        initialMap.put("\u03B4", "&delta;"); // greek small letter delta,U+03B4 ISOgrk3 -->
        initialMap.put("\u03B5", "&epsilon;"); // greek small letter epsilon,U+03B5 ISOgrk3 -->
        initialMap.put("\u03B6", "&zeta;"); // greek small letter zeta, U+03B6 ISOgrk3 -->
        initialMap.put("\u03B7", "&eta;"); // greek small letter eta, U+03B7 ISOgrk3 -->
        initialMap.put("\u03B8", "&theta;"); // greek small letter theta,U+03B8 ISOgrk3 -->
        initialMap.put("\u03B9", "&iota;"); // greek small letter iota, U+03B9 ISOgrk3 -->
        initialMap.put("\u03BA", "&kappa;"); // greek small letter kappa,U+03BA ISOgrk3 -->
        initialMap.put("\u03BB", "&lambda;"); // greek small letter lambda,U+03BB ISOgrk3 -->
        initialMap.put("\u03BC", "&mu;"); // greek small letter mu, U+03BC ISOgrk3 -->
        initialMap.put("\u03BD", "&nu;"); // greek small letter nu, U+03BD ISOgrk3 -->
        initialMap.put("\u03BE", "&xi;"); // greek small letter xi, U+03BE ISOgrk3 -->
        initialMap.put("\u03BF", "&omicron;"); // greek small letter omicron, U+03BF NEW -->
        initialMap.put("\u03C0", "&pi;"); // greek small letter pi, U+03C0 ISOgrk3 -->
        initialMap.put("\u03C1", "&rho;"); // greek small letter rho, U+03C1 ISOgrk3 -->
        initialMap.put("\u03C2", "&sigmaf;"); // greek small letter final sigma,U+03C2 ISOgrk3 -->
        initialMap.put("\u03C3", "&sigma;"); // greek small letter sigma,U+03C3 ISOgrk3 -->
        initialMap.put("\u03C4", "&tau;"); // greek small letter tau, U+03C4 ISOgrk3 -->
        initialMap.put("\u03C5", "&upsilon;"); // greek small letter upsilon,U+03C5 ISOgrk3 -->
        initialMap.put("\u03C6", "&phi;"); // greek small letter phi, U+03C6 ISOgrk3 -->
        initialMap.put("\u03C7", "&chi;"); // greek small letter chi, U+03C7 ISOgrk3 -->
        initialMap.put("\u03C8", "&psi;"); // greek small letter psi, U+03C8 ISOgrk3 -->
        initialMap.put("\u03C9", "&omega;"); // greek small letter omega,U+03C9 ISOgrk3 -->
        initialMap.put("\u03D1", "&thetasym;"); // greek small letter theta symbol,U+03D1 NEW -->
        initialMap.put("\u03D2", "&upsih;"); // greek upsilon with hook symbol,U+03D2 NEW -->
        initialMap.put("\u03D6", "&piv;"); // greek pi symbol, U+03D6 ISOgrk3 -->
        // <!-- General Punctuation -->
        initialMap.put("\u2022", "&bull;"); // bullet = black small circle,U+2022 ISOpub -->
        // <!-- bullet is NOT the same as bullet operator, U+2219 -->
        initialMap.put("\u2026", "&hellip;"); // horizontal ellipsis = three dot leader,U+2026 ISOpub -->
        initialMap.put("\u2032", "&prime;"); // prime = minutes = feet, U+2032 ISOtech -->
        initialMap.put("\u2033", "&Prime;"); // double prime = seconds = inches,U+2033 ISOtech -->
        initialMap.put("\u203E", "&oline;"); // overline = spacing overscore,U+203E NEW -->
        initialMap.put("\u2044", "&frasl;"); // fraction slash, U+2044 NEW -->
        // <!-- Letterlike Symbols -->
        initialMap.put("\u2118", "&weierp;"); // script capital P = power set= Weierstrass p, U+2118 ISOamso -->
        initialMap.put("\u2111", "&image;"); // blackletter capital I = imaginary part,U+2111 ISOamso -->
        initialMap.put("\u211C", "&real;"); // blackletter capital R = real part symbol,U+211C ISOamso -->
        initialMap.put("\u2122", "&trade;"); // trade mark sign, U+2122 ISOnum -->
        initialMap.put("\u2135", "&alefsym;"); // alef symbol = first transfinite cardinal,U+2135 NEW -->
        // <!-- alef symbol is NOT the same as hebrew letter alef,U+05D0 although the
        // same glyph could be used to depict both characters -->
        // <!-- Arrows -->
        initialMap.put("\u2190", "&larr;"); // leftwards arrow, U+2190 ISOnum -->
        initialMap.put("\u2191", "&uarr;"); // upwards arrow, U+2191 ISOnum-->
        initialMap.put("\u2192", "&rarr;"); // rightwards arrow, U+2192 ISOnum -->
        initialMap.put("\u2193", "&darr;"); // downwards arrow, U+2193 ISOnum -->
        initialMap.put("\u2194", "&harr;"); // left right arrow, U+2194 ISOamsa -->
        initialMap.put("\u21B5", "&crarr;"); // downwards arrow with corner leftwards= carriage return, U+21B5 NEW -->
        initialMap.put("\u21D0", "&lArr;"); // leftwards double arrow, U+21D0 ISOtech -->
        // <!-- ISO 10646 does not say that lArr is the same as the 'is implied by'
        // arrow but also does not have any other character for that function.
        // So ? lArr canbe used for 'is implied by' as ISOtech suggests -->
        initialMap.put("\u21D1", "&uArr;"); // upwards double arrow, U+21D1 ISOamsa -->
        initialMap.put("\u21D2", "&rArr;"); // rightwards double arrow,U+21D2 ISOtech -->
        // <!-- ISO 10646 does not say this is the 'implies' character but does not
        // have another character with this function so ?rArr can be used for
        // 'implies' as ISOtech suggests -->
        initialMap.put("\u21D3", "&dArr;"); // downwards double arrow, U+21D3 ISOamsa -->
        initialMap.put("\u21D4", "&hArr;"); // left right double arrow,U+21D4 ISOamsa -->
        // <!-- Mathematical Operators -->
        initialMap.put("\u2200", "&forall;"); // for all, U+2200 ISOtech -->
        initialMap.put("\u2202", "&part;"); // partial differential, U+2202 ISOtech -->
        initialMap.put("\u2203", "&exist;"); // there exists, U+2203 ISOtech -->
        initialMap.put("\u2205", "&empty;"); // empty set = null set = diameter,U+2205 ISOamso -->
        initialMap.put("\u2207", "&nabla;"); // nabla = backward difference,U+2207 ISOtech -->
        initialMap.put("\u2208", "&isin;"); // element of, U+2208 ISOtech -->
        initialMap.put("\u2209", "&notin;"); // not an element of, U+2209 ISOtech -->
        initialMap.put("\u220B", "&ni;"); // contains as member, U+220B ISOtech -->
        // <!-- should there be a more memorable name than 'ni'? -->
        initialMap.put("\u220F", "&prod;"); // n-ary product = product sign,U+220F ISOamsb -->
        // <!-- prod is NOT the same character as U+03A0 'greek capital letter pi'
        // though the same glyph might be used for both -->
        initialMap.put("\u2211", "&sum;"); // n-ary summation, U+2211 ISOamsb -->
        // <!-- sum is NOT the same character as U+03A3 'greek capital letter sigma'
        // though the same glyph might be used for both -->
        initialMap.put("\u2212", "&minus;"); // minus sign, U+2212 ISOtech -->
        initialMap.put("\u2217", "&lowast;"); // asterisk operator, U+2217 ISOtech -->
        initialMap.put("\u221A", "&radic;"); // square root = radical sign,U+221A ISOtech -->
        initialMap.put("\u221D", "&prop;"); // proportional to, U+221D ISOtech -->
        initialMap.put("\u221E", "&infin;"); // infinity, U+221E ISOtech -->
        initialMap.put("\u2220", "&ang;"); // angle, U+2220 ISOamso -->
        initialMap.put("\u2227", "&and;"); // logical and = wedge, U+2227 ISOtech -->
        initialMap.put("\u2228", "&or;"); // logical or = vee, U+2228 ISOtech -->
        initialMap.put("\u2229", "&cap;"); // intersection = cap, U+2229 ISOtech -->
        initialMap.put("\u222A", "&cup;"); // union = cup, U+222A ISOtech -->
        initialMap.put("\u222B", "&int;"); // integral, U+222B ISOtech -->
        initialMap.put("\u2234", "&there4;"); // therefore, U+2234 ISOtech -->
        initialMap.put("\u223C", "&sim;"); // tilde operator = varies with = similar to,U+223C ISOtech -->
        // <!-- tilde operator is NOT the same character as the tilde, U+007E,although
        // the same glyph might be used to represent both -->
        initialMap.put("\u2245", "&cong;"); // approximately equal to, U+2245 ISOtech -->
        initialMap.put("\u2248", "&asymp;"); // almost equal to = asymptotic to,U+2248 ISOamsr -->
        initialMap.put("\u2260", "&ne;"); // not equal to, U+2260 ISOtech -->
        initialMap.put("\u2261", "&equiv;"); // identical to, U+2261 ISOtech -->
        initialMap.put("\u2264", "&le;"); // less-than or equal to, U+2264 ISOtech -->
        initialMap.put("\u2265", "&ge;"); // greater-than or equal to,U+2265 ISOtech -->
        initialMap.put("\u2282", "&sub;"); // subset of, U+2282 ISOtech -->
        initialMap.put("\u2283", "&sup;"); // superset of, U+2283 ISOtech -->
        // <!-- note that nsup, 'not a superset of, U+2283' is not covered by the
        // Symbol font encoding and is not included. Should it be, for symmetry?
        // It is in ISOamsn -->,
        initialMap.put("\u2284", "&nsub;"); // not a subset of, U+2284 ISOamsn -->
        initialMap.put("\u2286", "&sube;"); // subset of or equal to, U+2286 ISOtech -->
        initialMap.put("\u2287", "&supe;"); // superset of or equal to,U+2287 ISOtech -->
        initialMap.put("\u2295", "&oplus;"); // circled plus = direct sum,U+2295 ISOamsb -->
        initialMap.put("\u2297", "&otimes;"); // circled times = vector product,U+2297 ISOamsb -->
        initialMap.put("\u22A5", "&perp;"); // up tack = orthogonal to = perpendicular,U+22A5 ISOtech -->
        initialMap.put("\u22C5", "&sdot;"); // dot operator, U+22C5 ISOamsb -->
        // <!-- dot operator is NOT the same character as U+00B7 middle dot -->
        // <!-- Miscellaneous Technical -->
        initialMap.put("\u2308", "&lceil;"); // left ceiling = apl upstile,U+2308 ISOamsc -->
        initialMap.put("\u2309", "&rceil;"); // right ceiling, U+2309 ISOamsc -->
        initialMap.put("\u230A", "&lfloor;"); // left floor = apl downstile,U+230A ISOamsc -->
        initialMap.put("\u230B", "&rfloor;"); // right floor, U+230B ISOamsc -->
        initialMap.put("\u2329", "&lang;"); // left-pointing angle bracket = bra,U+2329 ISOtech -->
        // <!-- lang is NOT the same character as U+003C 'less than' or U+2039 'single left-pointing angle quotation
        // mark' -->
        initialMap.put("\u232A", "&rang;"); // right-pointing angle bracket = ket,U+232A ISOtech -->
        // <!-- rang is NOT the same character as U+003E 'greater than' or U+203A
        // 'single right-pointing angle quotation mark' -->
        // <!-- Geometric Shapes -->
        initialMap.put("\u25CA", "&loz;"); // lozenge, U+25CA ISOpub -->
        // <!-- Miscellaneous Symbols -->
        initialMap.put("\u2660", "&spades;"); // black spade suit, U+2660 ISOpub -->
        // <!-- black here seems to mean filled as opposed to hollow -->
        initialMap.put("\u2663", "&clubs;"); // black club suit = shamrock,U+2663 ISOpub -->
        initialMap.put("\u2665", "&hearts;"); // black heart suit = valentine,U+2665 ISOpub -->
        initialMap.put("\u2666", "&diams;"); // black diamond suit, U+2666 ISOpub -->

        // <!-- Latin Extended-A -->
        initialMap.put("\u0152", "&OElig;"); // -- latin capital ligature OE,U+0152 ISOlat2 -->
        initialMap.put("\u0153", "&oelig;"); // -- latin small ligature oe, U+0153 ISOlat2 -->
        // <!-- ligature is a misnomer, this is a separate character in some languages -->
        initialMap.put("\u0160", "&Scaron;"); // -- latin capital letter S with caron,U+0160 ISOlat2 -->
        initialMap.put("\u0161", "&scaron;"); // -- latin small letter s with caron,U+0161 ISOlat2 -->
        initialMap.put("\u0178", "&Yuml;"); // -- latin capital letter Y with diaeresis,U+0178 ISOlat2 -->
        // <!-- Spacing Modifier Letters -->
        initialMap.put("\u02C6", "&circ;"); // -- modifier letter circumflex accent,U+02C6 ISOpub -->
        initialMap.put("\u02DC", "&tilde;"); // small tilde, U+02DC ISOdia -->
        // <!-- General Punctuation -->
        initialMap.put("\u2002", "&ensp;"); // en space, U+2002 ISOpub -->
        initialMap.put("\u2003", "&emsp;"); // em space, U+2003 ISOpub -->
        initialMap.put("\u2009", "&thinsp;"); // thin space, U+2009 ISOpub -->
        initialMap.put("\u200C", "&zwnj;"); // zero width non-joiner,U+200C NEW RFC 2070 -->
        initialMap.put("\u200D", "&zwj;"); // zero width joiner, U+200D NEW RFC 2070 -->
        initialMap.put("\u200E", "&lrm;"); // left-to-right mark, U+200E NEW RFC 2070 -->
        initialMap.put("\u200F", "&rlm;"); // right-to-left mark, U+200F NEW RFC 2070 -->
        initialMap.put("\u2013", "&ndash;"); // en dash, U+2013 ISOpub -->
        initialMap.put("\u2014", "&mdash;"); // em dash, U+2014 ISOpub -->
        initialMap.put("\u2018", "&lsquo;"); // left single quotation mark,U+2018 ISOnum -->
        initialMap.put("\u2019", "&rsquo;"); // right single quotation mark,U+2019 ISOnum -->
        initialMap.put("\u201A", "&sbquo;"); // single low-9 quotation mark, U+201A NEW -->
        initialMap.put("\u201C", "&ldquo;"); // left double quotation mark,U+201C ISOnum -->
        initialMap.put("\u201D", "&rdquo;"); // right double quotation mark,U+201D ISOnum -->
        initialMap.put("\u201E", "&bdquo;"); // double low-9 quotation mark, U+201E NEW -->
        initialMap.put("\u2020", "&dagger;"); // dagger, U+2020 ISOpub -->
        initialMap.put("\u2021", "&Dagger;"); // double dagger, U+2021 ISOpub -->
        initialMap.put("\u2030", "&permil;"); // per mille sign, U+2030 ISOtech -->
        initialMap.put("\u2039", "&lsaquo;"); // single left-pointing angle quotation mark,U+2039 ISO proposed -->
        // <!-- lsaquo is proposed but not yet ISO standardized -->
        initialMap.put("\u203A", "&rsaquo;"); // single right-pointing angle quotation mark,U+203A ISO proposed -->
        // <!-- rsaquo is proposed but not yet ISO standardized -->
        initialMap.put("\u20AC", "&euro;"); // -- euro sign, U+20AC NEW -->
        HTML40_EXTENDED_ESCAPE = Collections.unmodifiableMap(initialMap);
    }

    /**
     * Reverse of {@link #HTML40_EXTENDED_ESCAPE} for unescaping purposes.
     */
    public static final Map<CharSequence, CharSequence> HTML40_EXTENDED_UNESCAPE;

    static {
        HTML40_EXTENDED_UNESCAPE = Collections.unmodifiableMap(invert(HTML40_EXTENDED_ESCAPE));
    }

    /**
     * A Map&lt;CharSequence, CharSequence&gt; to escape additional
     * <a href="https://html.spec.whatwg.org/multipage/named-characters.html">character entity
     * references</a>. Note that this must be used with {@link #BASIC_ESCAPE}, {@link #ISO8859_1_ESCAPE} and {@link #HTML40_EXTENDED_ESCAPE} to get the full list of
     * HTML 5.0 character entities.
     */
    public static final Map<CharSequence, CharSequence> HTML50_EXTENDED_ESCAPE;

    static {
        final Map<CharSequence, CharSequence> initialMap = new HashMap<>();
        initialMap.put("\u0009", "&Tab;");
        /* "\u000A" is a Unicode control character pre-processed as a literal line break */
        initialMap.put("\u005C\u006E", "&NewLine;");
        initialMap.put("\u0021", "&excl;");
        initialMap.put("\u005C\u0022", "&QUOT;");
        initialMap.put("\u0023", "&num;");
        initialMap.put("\u0024", "&dollar;");
        initialMap.put("\u0025", "&percnt;");
        initialMap.put("\u0026", "&AMP;");
        initialMap.put("\u0027", "&apos;");
        initialMap.put("\u0028", "&lpar;");
        initialMap.put("\u0029", "&rpar;");
        initialMap.put("\u002A", "&midast;");
        initialMap.put("\u002B", "&plus;");
        initialMap.put("\u002C", "&comma;");
        initialMap.put("\u002E", "&period;");
        initialMap.put("\u002F", "&sol;");
        initialMap.put("\u003A", "&colon;");
        initialMap.put("\u003B", "&semi;");
        initialMap.put("\u003C", "&LT;");
        initialMap.put("\u003C\u20D2", "&nvlt;");
        initialMap.put("\u003D", "&equals;");
        initialMap.put("\u003D\u20E5", "&bne;");
        initialMap.put("\u003E", "&GT;");
        initialMap.put("\u003E\u20D2", "&nvgt;");
        initialMap.put("\u003F", "&quest;");
        initialMap.put("\u0040", "&commat;");
        initialMap.put("\u005B", "&lsqb;");
        initialMap.put("\u005C\u005C", "&bsol;");
        initialMap.put("\u005D", "&rsqb;");
        initialMap.put("\u005E", "&Hat;");
        initialMap.put("\u005F", "&lowbar;");
        initialMap.put("\u0060", "&grave;");
        initialMap.put("\u0066\u006A", "&fjlig;");
        initialMap.put("\u007B", "&lcub;");
        initialMap.put("\u007C", "&vert;");
        initialMap.put("\u007D", "&rcub;");
        initialMap.put("\u0100", "&Amacr;");
        initialMap.put("\u0101", "&amacr;");
        initialMap.put("\u0102", "&Abreve;");
        initialMap.put("\u0103", "&abreve;");
        initialMap.put("\u0104", "&Aogon;");
        initialMap.put("\u0105", "&aogon;");
        initialMap.put("\u0106", "&Cacute;");
        initialMap.put("\u0107", "&cacute;");
        initialMap.put("\u0108", "&Ccirc;");
        initialMap.put("\u0109", "&ccirc;");
        initialMap.put("\u010A", "&Cdot;");
        initialMap.put("\u010B", "&cdot;");
        initialMap.put("\u010C", "&Ccaron;");
        initialMap.put("\u010D", "&ccaron;");
        initialMap.put("\u010E", "&Dcaron;");
        initialMap.put("\u010F", "&dcaron;");
        initialMap.put("\u0110", "&Dstrok;");
        initialMap.put("\u0111", "&dstrok;");
        initialMap.put("\u0112", "&Emacr;");
        initialMap.put("\u0113", "&emacr;");
        initialMap.put("\u0116", "&Edot;");
        initialMap.put("\u0117", "&edot;");
        initialMap.put("\u0118", "&Eogon;");
        initialMap.put("\u0119", "&eogon;");
        initialMap.put("\u011A", "&Ecaron;");
        initialMap.put("\u011B", "&ecaron;");
        initialMap.put("\u011C", "&Gcirc;");
        initialMap.put("\u011D", "&gcirc;");
        initialMap.put("\u011E", "&Gbreve;");
        initialMap.put("\u011F", "&gbreve;");
        initialMap.put("\u0120", "&Gdot;");
        initialMap.put("\u0121", "&gdot;");
        initialMap.put("\u0122", "&Gcedil;");
        initialMap.put("\u0124", "&Hcirc;");
        initialMap.put("\u0125", "&hcirc;");
        initialMap.put("\u0126", "&Hstrok;");
        initialMap.put("\u0127", "&hstrok;");
        initialMap.put("\u0128", "&Itilde;");
        initialMap.put("\u0129", "&itilde;");
        initialMap.put("\u012A", "&Imacr;");
        initialMap.put("\u012B", "&imacr;");
        initialMap.put("\u012E", "&Iogon;");
        initialMap.put("\u012F", "&iogon;");
        initialMap.put("\u0130", "&Idot;");
        initialMap.put("\u0131", "&inodot;");
        initialMap.put("\u0132", "&IJlig;");
        initialMap.put("\u0133", "&ijlig;");
        initialMap.put("\u0134", "&Jcirc;");
        initialMap.put("\u0135", "&jcirc;");
        initialMap.put("\u0136", "&Kcedil;");
        initialMap.put("\u0137", "&kcedil;");
        initialMap.put("\u0138", "&kgreen;");
        initialMap.put("\u0139", "&Lacute;");
        initialMap.put("\u013A", "&lacute;");
        initialMap.put("\u013B", "&Lcedil;");
        initialMap.put("\u013C", "&lcedil;");
        initialMap.put("\u013D", "&Lcaron;");
        initialMap.put("\u013E", "&lcaron;");
        initialMap.put("\u013F", "&Lmidot;");
        initialMap.put("\u0140", "&lmidot;");
        initialMap.put("\u0141", "&Lstrok;");
        initialMap.put("\u0142", "&lstrok;");
        initialMap.put("\u0143", "&Nacute;");
        initialMap.put("\u0144", "&nacute;");
        initialMap.put("\u0145", "&Ncedil;");
        initialMap.put("\u0146", "&ncedil;");
        initialMap.put("\u0147", "&Ncaron;");
        initialMap.put("\u0148", "&ncaron;");
        initialMap.put("\u0149", "&napos;");
        initialMap.put("\u014A", "&ENG;");
        initialMap.put("\u014B", "&eng;");
        initialMap.put("\u014C", "&Omacr;");
        initialMap.put("\u014D", "&omacr;");
        initialMap.put("\u0150", "&Odblac;");
        initialMap.put("\u0151", "&odblac;");
        initialMap.put("\u0154", "&Racute;");
        initialMap.put("\u0155", "&racute;");
        initialMap.put("\u0156", "&Rcedil;");
        initialMap.put("\u0157", "&rcedil;");
        initialMap.put("\u0158", "&Rcaron;");
        initialMap.put("\u0159", "&rcaron;");
        initialMap.put("\u015A", "&Sacute;");
        initialMap.put("\u015B", "&sacute;");
        initialMap.put("\u015C", "&Scirc;");
        initialMap.put("\u015D", "&scirc;");
        initialMap.put("\u015E", "&Scedil;");
        initialMap.put("\u015F", "&scedil;");
        initialMap.put("\u0162", "&Tcedil;");
        initialMap.put("\u0163", "&tcedil;");
        initialMap.put("\u0164", "&Tcaron;");
        initialMap.put("\u0165", "&tcaron;");
        initialMap.put("\u0166", "&Tstrok;");
        initialMap.put("\u0167", "&tstrok;");
        initialMap.put("\u0168", "&Utilde;");
        initialMap.put("\u0169", "&utilde;");
        initialMap.put("\u016A", "&Umacr;");
        initialMap.put("\u016B", "&umacr;");
        initialMap.put("\u016C", "&Ubreve;");
        initialMap.put("\u016D", "&ubreve;");
        initialMap.put("\u016E", "&Uring;");
        initialMap.put("\u016F", "&uring;");
        initialMap.put("\u0170", "&Udblac;");
        initialMap.put("\u0171", "&udblac;");
        initialMap.put("\u0172", "&Uogon;");
        initialMap.put("\u0173", "&uogon;");
        initialMap.put("\u0174", "&Wcirc;");
        initialMap.put("\u0175", "&wcirc;");
        initialMap.put("\u0176", "&Ycirc;");
        initialMap.put("\u0177", "&ycirc;");
        initialMap.put("\u0179", "&Zacute;");
        initialMap.put("\u017A", "&zacute;");
        initialMap.put("\u017B", "&Zdot;");
        initialMap.put("\u017C", "&zdot;");
        initialMap.put("\u017D", "&Zcaron;");
        initialMap.put("\u017E", "&zcaron;");
        initialMap.put("\u01B5", "&imped;");
        initialMap.put("\u01F5", "&gacute;");
        initialMap.put("\u0237", "&jmath;");
        initialMap.put("\u02C7", "&caron;");
        initialMap.put("\u02D8", "&breve;");
        initialMap.put("\u02D9", "&dot;");
        initialMap.put("\u02DA", "&ring;");
        initialMap.put("\u02DB", "&ogon;");
        initialMap.put("\u02DD", "&dblac;");
        initialMap.put("\u0311", "&DownBreve;");
        initialMap.put("\u03D5", "&varphi;");
        initialMap.put("\u03DC", "&Gammad;");
        initialMap.put("\u03DD", "&gammad;");
        initialMap.put("\u03F0", "&varkappa;");
        initialMap.put("\u03F1", "&varrho;");
        initialMap.put("\u03F5", "&varepsilon;");
        initialMap.put("\u03F6", "&bepsi;");
        initialMap.put("\u0401", "&IOcy;");
        initialMap.put("\u0402", "&DJcy;");
        initialMap.put("\u0403", "&GJcy;");
        initialMap.put("\u0404", "&Jukcy;");
        initialMap.put("\u0405", "&DScy;");
        initialMap.put("\u0406", "&Iukcy;");
        initialMap.put("\u0407", "&YIcy;");
        initialMap.put("\u0408", "&Jsercy;");
        initialMap.put("\u0409", "&LJcy;");
        initialMap.put("\u040A", "&NJcy;");
        initialMap.put("\u040B", "&TSHcy;");
        initialMap.put("\u040C", "&KJcy;");
        initialMap.put("\u040E", "&Ubrcy;");
        initialMap.put("\u040F", "&DZcy;");
        initialMap.put("\u0410", "&Acy;");
        initialMap.put("\u0411", "&Bcy;");
        initialMap.put("\u0412", "&Vcy;");
        initialMap.put("\u0413", "&Gcy;");
        initialMap.put("\u0414", "&Dcy;");
        initialMap.put("\u0415", "&IEcy;");
        initialMap.put("\u0416", "&ZHcy;");
        initialMap.put("\u0417", "&Zcy;");
        initialMap.put("\u0418", "&Icy;");
        initialMap.put("\u0419", "&Jcy;");
        initialMap.put("\u041A", "&Kcy;");
        initialMap.put("\u041B", "&Lcy;");
        initialMap.put("\u041C", "&Mcy;");
        initialMap.put("\u041D", "&Ncy;");
        initialMap.put("\u041E", "&Ocy;");
        initialMap.put("\u041F", "&Pcy;");
        initialMap.put("\u0420", "&Rcy;");
        initialMap.put("\u0421", "&Scy;");
        initialMap.put("\u0422", "&Tcy;");
        initialMap.put("\u0423", "&Ucy;");
        initialMap.put("\u0424", "&Fcy;");
        initialMap.put("\u0425", "&KHcy;");
        initialMap.put("\u0426", "&TScy;");
        initialMap.put("\u0427", "&CHcy;");
        initialMap.put("\u0428", "&SHcy;");
        initialMap.put("\u0429", "&SHCHcy;");
        initialMap.put("\u042A", "&HARDcy;");
        initialMap.put("\u042B", "&Ycy;");
        initialMap.put("\u042C", "&SOFTcy;");
        initialMap.put("\u042D", "&Ecy;");
        initialMap.put("\u042E", "&YUcy;");
        initialMap.put("\u042F", "&YAcy;");
        initialMap.put("\u0430", "&acy;");
        initialMap.put("\u0431", "&bcy;");
        initialMap.put("\u0432", "&vcy;");
        initialMap.put("\u0433", "&gcy;");
        initialMap.put("\u0434", "&dcy;");
        initialMap.put("\u0435", "&iecy;");
        initialMap.put("\u0436", "&zhcy;");
        initialMap.put("\u0437", "&zcy;");
        initialMap.put("\u0438", "&icy;");
        initialMap.put("\u0439", "&jcy;");
        initialMap.put("\u043A", "&kcy;");
        initialMap.put("\u043B", "&lcy;");
        initialMap.put("\u043C", "&mcy;");
        initialMap.put("\u043D", "&ncy;");
        initialMap.put("\u043E", "&ocy;");
        initialMap.put("\u043F", "&pcy;");
        initialMap.put("\u0440", "&rcy;");
        initialMap.put("\u0441", "&scy;");
        initialMap.put("\u0442", "&tcy;");
        initialMap.put("\u0443", "&ucy;");
        initialMap.put("\u0444", "&fcy;");
        initialMap.put("\u0445", "&khcy;");
        initialMap.put("\u0446", "&tscy;");
        initialMap.put("\u0447", "&chcy;");
        initialMap.put("\u0448", "&shcy;");
        initialMap.put("\u0449", "&shchcy;");
        initialMap.put("\u044A", "&hardcy;");
        initialMap.put("\u044B", "&ycy;");
        initialMap.put("\u044C", "&softcy;");
        initialMap.put("\u044D", "&ecy;");
        initialMap.put("\u044E", "&yucy;");
        initialMap.put("\u044F", "&yacy;");
        initialMap.put("\u0451", "&iocy;");
        initialMap.put("\u0452", "&djcy;");
        initialMap.put("\u0453", "&gjcy;");
        initialMap.put("\u0454", "&jukcy;");
        initialMap.put("\u0455", "&dscy;");
        initialMap.put("\u0456", "&iukcy;");
        initialMap.put("\u0457", "&yicy;");
        initialMap.put("\u0458", "&jsercy;");
        initialMap.put("\u0459", "&ljcy;");
        initialMap.put("\u045A", "&njcy;");
        initialMap.put("\u045B", "&tshcy;");
        initialMap.put("\u045C", "&kjcy;");
        initialMap.put("\u045E", "&ubrcy;");
        initialMap.put("\u045F", "&dzcy;");
        initialMap.put("\u2004", "&emsp13;");
        initialMap.put("\u2005", "&emsp14;");
        initialMap.put("\u2007", "&numsp;");
        initialMap.put("\u2008", "&puncsp;");
        initialMap.put("\u200A", "&hairsp;");
        initialMap.put("\u200B", "&ZeroWidthSpace;");
        initialMap.put("\u2010", "&hyphen;");
        initialMap.put("\u2015", "&horbar;");
        initialMap.put("\u2016", "&Vert;");
        initialMap.put("\u2025", "&nldr;");
        initialMap.put("\u2031", "&pertenk;");
        initialMap.put("\u2034", "&tprime;");
        initialMap.put("\u2035", "&bprime;");
        initialMap.put("\u2041", "&caret;");
        initialMap.put("\u2043", "&hybull;");
        initialMap.put("\u204F", "&bsemi;");
        initialMap.put("\u2057", "&qprime;");
        initialMap.put("\u205F", "&MediumSpace;");
        initialMap.put("\u205F\u200A", "&ThickSpace;");
        initialMap.put("\u2060", "&NoBreak;");
        initialMap.put("\u2061", "&af;");
        initialMap.put("\u2062", "&it;");
        initialMap.put("\u2063", "&ic;");
        initialMap.put("\u20DB", "&tdot;");
        initialMap.put("\u20DC", "&DotDot;");
        initialMap.put("\u2102", "&complexes;");
        initialMap.put("\u2105", "&incare;");
        initialMap.put("\u210A", "&gscr;");
        initialMap.put("\u210B", "&hamilt;");
        initialMap.put("\u210C", "&Poincareplane;");
        initialMap.put("\u210D", "&quaternions;");
        initialMap.put("\u210E", "&planckh;");
        initialMap.put("\u210F", "&plankv;");
        initialMap.put("\u2110", "&imagline;");
        initialMap.put("\u2112", "&lagran;");
        initialMap.put("\u2113", "&ell;");
        initialMap.put("\u2115", "&naturals;");
        initialMap.put("\u2116", "&numero;");
        initialMap.put("\u2117", "&copysr;");
        initialMap.put("\u2119", "&primes;");
        initialMap.put("\u211A", "&rationals;");
        initialMap.put("\u211B", "&realine;");
        initialMap.put("\u211D", "&reals;");
        initialMap.put("\u211E", "&rx;");
        initialMap.put("\u2124", "&integers;");
        initialMap.put("\u2127", "&mho;");
        initialMap.put("\u2128", "&zeetrf;");
        initialMap.put("\u2129", "&iiota;");
        initialMap.put("\u212C", "&bernou;");
        initialMap.put("\u212D", "&Cfr;");
        initialMap.put("\u212F", "&escr;");
        initialMap.put("\u2130", "&expectation;");
        initialMap.put("\u2131", "&Fscr;");
        initialMap.put("\u2133", "&phmmat;");
        initialMap.put("\u2134", "&oscr;");
        initialMap.put("\u2136", "&beth;");
        initialMap.put("\u2137", "&gimel;");
        initialMap.put("\u2138", "&daleth;");
        initialMap.put("\u2145", "&DD;");
        initialMap.put("\u2146", "&dd;");
        initialMap.put("\u2147", "&exponentiale;");
        initialMap.put("\u2148", "&ii;");
        initialMap.put("\u2153", "&frac13;");
        initialMap.put("\u2154", "&frac23;");
        initialMap.put("\u2155", "&frac15;");
        initialMap.put("\u2156", "&frac25;");
        initialMap.put("\u2157", "&frac35;");
        initialMap.put("\u2158", "&frac45;");
        initialMap.put("\u2159", "&frac16;");
        initialMap.put("\u215A", "&frac56;");
        initialMap.put("\u215B", "&frac18;");
        initialMap.put("\u215C", "&frac38;");
        initialMap.put("\u215D", "&frac58;");
        initialMap.put("\u215E", "&frac78;");
        initialMap.put("\u2195", "&varr;");
        initialMap.put("\u2196", "&nwarrow;");
        initialMap.put("\u2197", "&nearrow;");
        initialMap.put("\u2198", "&searrow;");
        initialMap.put("\u2199", "&swarrow;");
        initialMap.put("\u219A", "&nleftarrow;");
        initialMap.put("\u219B", "&nrightarrow;");
        initialMap.put("\u219D", "&rightsquigarrow;");
        initialMap.put("\u219D\u0338", "&nrarrw;");
        initialMap.put("\u219E", "&twoheadleftarrow;");
        initialMap.put("\u219F", "&Uarr;");
        initialMap.put("\u21A0", "&twoheadrightarrow;");
        initialMap.put("\u21A1", "&Darr;");
        initialMap.put("\u21A2", "&leftarrowtail;");
        initialMap.put("\u21A3", "&rightarrowtail;");
        initialMap.put("\u21A4", "&mapstoleft;");
        initialMap.put("\u21A5", "&mapstoup;");
        initialMap.put("\u21A6", "&mapsto;");
        initialMap.put("\u21A7", "&mapstodown;");
        initialMap.put("\u21A9", "&larrhk;");
        initialMap.put("\u21AA", "&rarrhk;");
        initialMap.put("\u21AB", "&looparrowleft;");
        initialMap.put("\u21AC", "&rarrlp;");
        initialMap.put("\u21AD", "&leftrightsquigarrow;");
        initialMap.put("\u21AE", "&nleftrightarrow;");
        initialMap.put("\u21B0", "&lsh;");
        initialMap.put("\u21B1", "&rsh;");
        initialMap.put("\u21B2", "&ldsh;");
        initialMap.put("\u21B3", "&rdsh;");
        initialMap.put("\u21B6", "&curvearrowleft;");
        initialMap.put("\u21B7", "&curvearrowright;");
        initialMap.put("\u21BA", "&olarr;");
        initialMap.put("\u21BB", "&orarr;");
        initialMap.put("\u21BC", "&lharu;");
        initialMap.put("\u21BD", "&lhard;");
        initialMap.put("\u21BE", "&upharpoonright;");
        initialMap.put("\u21BF", "&upharpoonleft;");
        initialMap.put("\u21C0", "&rightharpoonup;");
        initialMap.put("\u21C1", "&rightharpoondown;");
        initialMap.put("\u21C2", "&downharpoonright;");
        initialMap.put("\u21C3", "&downharpoonleft;");
        initialMap.put("\u21C4", "&rlarr;");
        initialMap.put("\u21C5", "&udarr;");
        initialMap.put("\u21C6", "&lrarr;");
        initialMap.put("\u21C7", "&llarr;");
        initialMap.put("\u21C8", "&uuarr;");
        initialMap.put("\u21C9", "&rrarr;");
        initialMap.put("\u21CA", "&downdownarrows;");
        initialMap.put("\u21CB", "&lrhar;");
        initialMap.put("\u21CC", "&rlhar;");
        initialMap.put("\u21CD", "&nlArr;");
        initialMap.put("\u21CE", "&nhArr;");
        initialMap.put("\u21CF", "&nrArr;");
        initialMap.put("\u21D5", "&vArr;");
        initialMap.put("\u21D6", "&nwArr;");
        initialMap.put("\u21D7", "&neArr;");
        initialMap.put("\u21D8", "&seArr;");
        initialMap.put("\u21D9", "&swArr;");
        initialMap.put("\u21DA", "&lAarr;");
        initialMap.put("\u21DB", "&rAarr;");
        initialMap.put("\u21DD", "&zigrarr;");
        initialMap.put("\u21E4", "&larrb;");
        initialMap.put("\u21E5", "&rarrb;");
        initialMap.put("\u21F5", "&duarr;");
        initialMap.put("\u21FD", "&loarr;");
        initialMap.put("\u21FE", "&roarr;");
        initialMap.put("\u21FF", "&hoarr;");
        initialMap.put("\u2201", "&complement;");
        initialMap.put("\u2202\u0338", "&npart;");
        initialMap.put("\u2204", "&nexists;");
        initialMap.put("\u220C", "&notniva;");
        initialMap.put("\u2210", "&coprod;");
        initialMap.put("\u2213", "&mp;");
        initialMap.put("\u2214", "&plusdo;");
        initialMap.put("\u2216", "&ssetmn;");
        initialMap.put("\u2218", "&compfn;");
        initialMap.put("\u221F", "&angrt;");
        initialMap.put("\u2220\u20D2", "&nang;");
        initialMap.put("\u2221", "&measuredangle;");
        initialMap.put("\u2222", "&angsph;");
        initialMap.put("\u2223", "&smid;");
        initialMap.put("\u2224", "&nsmid;");
        initialMap.put("\u2225", "&spar;");
        initialMap.put("\u2226", "&nspar;");
        initialMap.put("\u2229\uFE00", "&caps;");
        initialMap.put("\u222A\uFE00", "&cups;");
        initialMap.put("\u222C", "&Int;");
        initialMap.put("\u222D", "&tint;");
        initialMap.put("\u222E", "&oint;");
        initialMap.put("\u222F", "&DoubleContourIntegral;");
        initialMap.put("\u2230", "&Cconint;");
        initialMap.put("\u2231", "&cwint;");
        initialMap.put("\u2232", "&cwconint;");
        initialMap.put("\u2233", "&awconint;");
        initialMap.put("\u2235", "&because;");
        initialMap.put("\u2236", "&ratio;");
        initialMap.put("\u2237", "&Proportion;");
        initialMap.put("\u2238", "&minusd;");
        initialMap.put("\u223A", "&mDDot;");
        initialMap.put("\u223B", "&homtht;");
        initialMap.put("\u223C\u20D2", "&nvsim;");
        initialMap.put("\u223D", "&bsim;");
        initialMap.put("\u223D\u0331", "&race;");
        initialMap.put("\u223E", "&mstpos;");
        initialMap.put("\u223E\u0333", "&acE;");
        initialMap.put("\u223F", "&acd;");
        initialMap.put("\u2240", "&wreath;");
        initialMap.put("\u2241", "&nsim;");
        initialMap.put("\u2242", "&esim;");
        initialMap.put("\u2242\u0338", "&nesim;");
        initialMap.put("\u2243", "&simeq;");
        initialMap.put("\u2244", "&nsimeq;");
        initialMap.put("\u2246", "&simne;");
        initialMap.put("\u2247", "&ncong;");
        initialMap.put("\u2249", "&napprox;");
        initialMap.put("\u224A", "&approxeq;");
        initialMap.put("\u224B", "&apid;");
        initialMap.put("\u224B\u0338", "&napid;");
        initialMap.put("\u224C", "&bcong;");
        initialMap.put("\u224D", "&asympeq;");
        initialMap.put("\u224D\u20D2", "&nvap;");
        initialMap.put("\u224E", "&bump;");
        initialMap.put("\u224E\u0338", "&nbump;");
        initialMap.put("\u224F", "&bumpeq;");
        initialMap.put("\u224F\u0338", "&nbumpe;");
        initialMap.put("\u2250", "&esdot;");
        initialMap.put("\u2250\u0338", "&nedot;");
        initialMap.put("\u2251", "&eDot;");
        initialMap.put("\u2252", "&fallingdotseq;");
        initialMap.put("\u2253", "&risingdotseq;");
        initialMap.put("\u2254", "&coloneq;");
        initialMap.put("\u2255", "&eqcolon;");
        initialMap.put("\u2256", "&eqcirc;");
        initialMap.put("\u2257", "&cire;");
        initialMap.put("\u2259", "&wedgeq;");
        initialMap.put("\u225A", "&veeeq;");
        initialMap.put("\u225C", "&trie;");
        initialMap.put("\u225F", "&questeq;");
        initialMap.put("\u2261\u20E5", "&bnequiv;");
        initialMap.put("\u2262", "&nequiv;");
        initialMap.put("\u2264\u20D2", "&nvle;");
        initialMap.put("\u2265\u20D2", "&nvge;");
        initialMap.put("\u2266", "&leqq;");
        initialMap.put("\u2266\u0338", "&nleqq;");
        initialMap.put("\u2267", "&geqq;");
        initialMap.put("\u2267\u0338", "&ngeqq;");
        initialMap.put("\u2268", "&lneqq;");
        initialMap.put("\u2268\uFE00", "&lvnE;");
        initialMap.put("\u2269", "&gneqq;");
        initialMap.put("\u2269\uFE00", "&gvnE;");
        initialMap.put("\u226A", "&ll;");
        initialMap.put("\u226A\u0338", "&nLtv;");
        initialMap.put("\u226A\u20D2", "&nLt;");
        initialMap.put("\u226B", "&gg;");
        initialMap.put("\u226B\u0338", "&nGtv;");
        initialMap.put("\u226B\u20D2", "&nGt;");
        initialMap.put("\u226C", "&twixt;");
        initialMap.put("\u226D", "&NotCupCap;");
        initialMap.put("\u226E", "&nlt;");
        initialMap.put("\u226F", "&ngtr;");
        initialMap.put("\u2270", "&nleq;");
        initialMap.put("\u2271", "&ngeq;");
        initialMap.put("\u2272", "&lsim;");
        initialMap.put("\u2273", "&gtrsim;");
        initialMap.put("\u2274", "&nlsim;");
        initialMap.put("\u2275", "&ngsim;");
        initialMap.put("\u2276", "&lg;");
        initialMap.put("\u2277", "&gtrless;");
        initialMap.put("\u2278", "&ntlg;");
        initialMap.put("\u2279", "&ntgl;");
        initialMap.put("\u227A", "&prec;");
        initialMap.put("\u227B", "&succ;");
        initialMap.put("\u227C", "&preccurlyeq;");
        initialMap.put("\u227D", "&succcurlyeq;");
        initialMap.put("\u227E", "&prsim;");
        initialMap.put("\u227F", "&succsim;");
        initialMap.put("\u227F\u0338", "&NotSucceedsTilde;");
        initialMap.put("\u2280", "&nprec;");
        initialMap.put("\u2281", "&nsucc;");
        initialMap.put("\u2282\u20D2", "&vnsub;");
        initialMap.put("\u2283\u20D2", "&vnsup;");
        initialMap.put("\u2285", "&nsup;");
        initialMap.put("\u2288", "&nsubseteq;");
        initialMap.put("\u2289", "&nsupseteq;");
        initialMap.put("\u228A", "&subsetneq;");
        initialMap.put("\u228A\uFE00", "&vsubne;");
        initialMap.put("\u228B", "&supsetneq;");
        initialMap.put("\u228B\uFE00", "&vsupne;");
        initialMap.put("\u228D", "&cupdot;");
        initialMap.put("\u228E", "&uplus;");
        initialMap.put("\u228F", "&sqsubset;");
        initialMap.put("\u228F\u0338", "&NotSquareSubset;");
        initialMap.put("\u2290", "&sqsupset;");
        initialMap.put("\u2290\u0338", "&NotSquareSuperset;");
        initialMap.put("\u2291", "&sqsubseteq;");
        initialMap.put("\u2292", "&sqsupseteq;");
        initialMap.put("\u2293", "&sqcap;");
        initialMap.put("\u2293\uFE00", "&sqcaps;");
        initialMap.put("\u2294", "&sqcup;");
        initialMap.put("\u2294\uFE00", "&sqcups;");
        initialMap.put("\u2296", "&ominus;");
        initialMap.put("\u2298", "&osol;");
        initialMap.put("\u2299", "&odot;");
        initialMap.put("\u229A", "&ocir;");
        initialMap.put("\u229B", "&oast;");
        initialMap.put("\u229D", "&odash;");
        initialMap.put("\u229E", "&plusb;");
        initialMap.put("\u229F", "&minusb;");
        initialMap.put("\u22A0", "&timesb;");
        initialMap.put("\u22A1", "&sdotb;");
        initialMap.put("\u22A2", "&vdash;");
        initialMap.put("\u22A3", "&dashv;");
        initialMap.put("\u22A4", "&top;");
        initialMap.put("\u22A7", "&models;");
        initialMap.put("\u22A8", "&vDash;");
        initialMap.put("\u22A9", "&Vdash;");
        initialMap.put("\u22AA", "&Vvdash;");
        initialMap.put("\u22AB", "&VDash;");
        initialMap.put("\u22AC", "&nvdash;");
        initialMap.put("\u22AD", "&nvDash;");
        initialMap.put("\u22AE", "&nVdash;");
        initialMap.put("\u22AF", "&nVDash;");
        initialMap.put("\u22B0", "&prurel;");
        initialMap.put("\u22B2", "&vltri;");
        initialMap.put("\u22B3", "&vrtri;");
        initialMap.put("\u22B4", "&trianglelefteq;");
        initialMap.put("\u22B4\u20D2", "&nvltrie;");
        initialMap.put("\u22B5", "&trianglerighteq;");
        initialMap.put("\u22B5\u20D2", "&nvrtrie;");
        initialMap.put("\u22B6", "&origof;");
        initialMap.put("\u22B7", "&imof;");
        initialMap.put("\u22B8", "&mumap;");
        initialMap.put("\u22B9", "&hercon;");
        initialMap.put("\u22BA", "&intercal;");
        initialMap.put("\u22BB", "&veebar;");
        initialMap.put("\u22BD", "&barvee;");
        initialMap.put("\u22BE", "&angrtvb;");
        initialMap.put("\u22BF", "&lrtri;");
        initialMap.put("\u22C0", "&xwedge;");
        initialMap.put("\u22C1", "&xvee;");
        initialMap.put("\u22C2", "&xcap;");
        initialMap.put("\u22C3", "&xcup;");
        initialMap.put("\u22C4", "&diamond;");
        initialMap.put("\u22C6", "&sstarf;");
        initialMap.put("\u22C7", "&divonx;");
        initialMap.put("\u22C8", "&bowtie;");
        initialMap.put("\u22C9", "&ltimes;");
        initialMap.put("\u22CA", "&rtimes;");
        initialMap.put("\u22CB", "&lthree;");
        initialMap.put("\u22CC", "&rthree;");
        initialMap.put("\u22CD", "&bsime;");
        initialMap.put("\u22CE", "&cuvee;");
        initialMap.put("\u22CF", "&cuwed;");
        initialMap.put("\u22D0", "&Subset;");
        initialMap.put("\u22D1", "&Supset;");
        initialMap.put("\u22D2", "&Cap;");
        initialMap.put("\u22D3", "&Cup;");
        initialMap.put("\u22D4", "&pitchfork;");
        initialMap.put("\u22D5", "&epar;");
        initialMap.put("\u22D6", "&ltdot;");
        initialMap.put("\u22D7", "&gtrdot;");
        initialMap.put("\u22D8", "&Ll;");
        initialMap.put("\u22D8\u0338", "&nLl;");
        initialMap.put("\u22D9", "&ggg;");
        initialMap.put("\u22D9\u0338", "&nGg;");
        initialMap.put("\u22DA", "&lesseqgtr;");
        initialMap.put("\u22DA\uFE00", "&lesg;");
        initialMap.put("\u22DB", "&gtreqless;");
        initialMap.put("\u22DB\uFE00", "&gesl;");
        initialMap.put("\u22DE", "&curlyeqprec;");
        initialMap.put("\u22DF", "&curlyeqsucc;");
        initialMap.put("\u22E0", "&nprcue;");
        initialMap.put("\u22E1", "&nsccue;");
        initialMap.put("\u22E2", "&nsqsube;");
        initialMap.put("\u22E3", "&nsqsupe;");
        initialMap.put("\u22E6", "&lnsim;");
        initialMap.put("\u22E7", "&gnsim;");
        initialMap.put("\u22E8", "&prnsim;");
        initialMap.put("\u22E9", "&succnsim;");
        initialMap.put("\u22EA", "&ntriangleleft;");
        initialMap.put("\u22EB", "&ntriangleright;");
        initialMap.put("\u22EC", "&ntrianglelefteq;");
        initialMap.put("\u22ED", "&ntrianglerighteq;");
        initialMap.put("\u22EE", "&vellip;");
        initialMap.put("\u22EF", "&ctdot;");
        initialMap.put("\u22F0", "&utdot;");
        initialMap.put("\u22F1", "&dtdot;");
        initialMap.put("\u22F2", "&disin;");
        initialMap.put("\u22F3", "&isinsv;");
        initialMap.put("\u22F4", "&isins;");
        initialMap.put("\u22F5", "&isindot;");
        initialMap.put("\u22F5\u0338", "&notindot;");
        initialMap.put("\u22F6", "&notinvc;");
        initialMap.put("\u22F7", "&notinvb;");
        initialMap.put("\u22F9", "&isinE;");
        initialMap.put("\u22F9\u0338", "&notinE;");
        initialMap.put("\u22FA", "&nisd;");
        initialMap.put("\u22FB", "&xnis;");
        initialMap.put("\u22FC", "&nis;");
        initialMap.put("\u22FD", "&notnivc;");
        initialMap.put("\u22FE", "&notnivb;");
        initialMap.put("\u2305", "&barwedge;");
        initialMap.put("\u2306", "&doublebarwedge;");
        initialMap.put("\u230C", "&drcrop;");
        initialMap.put("\u230D", "&dlcrop;");
        initialMap.put("\u230E", "&urcrop;");
        initialMap.put("\u230F", "&ulcrop;");
        initialMap.put("\u2310", "&bnot;");
        initialMap.put("\u2312", "&profline;");
        initialMap.put("\u2313", "&profsurf;");
        initialMap.put("\u2315", "&telrec;");
        initialMap.put("\u2316", "&target;");
        initialMap.put("\u231C", "&ulcorner;");
        initialMap.put("\u231D", "&urcorner;");
        initialMap.put("\u231E", "&llcorner;");
        initialMap.put("\u231F", "&lrcorner;");
        initialMap.put("\u2322", "&sfrown;");
        initialMap.put("\u2323", "&ssmile;");
        initialMap.put("\u232D", "&cylcty;");
        initialMap.put("\u232E", "&profalar;");
        initialMap.put("\u2336", "&topbot;");
        initialMap.put("\u233D", "&ovbar;");
        initialMap.put("\u233F", "&solbar;");
        initialMap.put("\u237C", "&angzarr;");
        initialMap.put("\u23B0", "&lmoustache;");
        initialMap.put("\u23B1", "&rmoustache;");
        initialMap.put("\u23B4", "&tbrk;");
        initialMap.put("\u23B5", "&bbrk;");
        initialMap.put("\u23B6", "&bbrktbrk;");
        initialMap.put("\u23DC", "&OverParenthesis;");
        initialMap.put("\u23DD", "&UnderParenthesis;");
        initialMap.put("\u23DE", "&OverBrace;");
        initialMap.put("\u23DF", "&UnderBrace;");
        initialMap.put("\u23E2", "&trpezium;");
        initialMap.put("\u23E7", "&elinters;");
        initialMap.put("\u2423", "&blank;");
        initialMap.put("\u24C8", "&oS;");
        initialMap.put("\u2500", "&boxh;");
        initialMap.put("\u2502", "&boxv;");
        initialMap.put("\u250C", "&boxdr;");
        initialMap.put("\u2510", "&boxdl;");
        initialMap.put("\u2514", "&boxur;");
        initialMap.put("\u2518", "&boxul;");
        initialMap.put("\u251C", "&boxvr;");
        initialMap.put("\u2524", "&boxvl;");
        initialMap.put("\u252C", "&boxhd;");
        initialMap.put("\u2534", "&boxhu;");
        initialMap.put("\u253C", "&boxvh;");
        initialMap.put("\u2550", "&boxH;");
        initialMap.put("\u2551", "&boxV;");
        initialMap.put("\u2552", "&boxdR;");
        initialMap.put("\u2553", "&boxDr;");
        initialMap.put("\u2554", "&boxDR;");
        initialMap.put("\u2555", "&boxdL;");
        initialMap.put("\u2556", "&boxDl;");
        initialMap.put("\u2557", "&boxDL;");
        initialMap.put("\u2558", "&boxuR;");
        initialMap.put("\u2559", "&boxUr;");
        initialMap.put("\u255A", "&boxUR;");
        initialMap.put("\u255B", "&boxuL;");
        initialMap.put("\u255C", "&boxUl;");
        initialMap.put("\u255D", "&boxUL;");
        initialMap.put("\u255E", "&boxvR;");
        initialMap.put("\u255F", "&boxVr;");
        initialMap.put("\u2560", "&boxVR;");
        initialMap.put("\u2561", "&boxvL;");
        initialMap.put("\u2562", "&boxVl;");
        initialMap.put("\u2563", "&boxVL;");
        initialMap.put("\u2564", "&boxHd;");
        initialMap.put("\u2565", "&boxhD;");
        initialMap.put("\u2566", "&boxHD;");
        initialMap.put("\u2567", "&boxHu;");
        initialMap.put("\u2568", "&boxhU;");
        initialMap.put("\u2569", "&boxHU;");
        initialMap.put("\u256A", "&boxvH;");
        initialMap.put("\u256B", "&boxVh;");
        initialMap.put("\u256C", "&boxVH;");
        initialMap.put("\u2580", "&uhblk;");
        initialMap.put("\u2584", "&lhblk;");
        initialMap.put("\u2588", "&block;");
        initialMap.put("\u2591", "&blk14;");
        initialMap.put("\u2592", "&blk12;");
        initialMap.put("\u2593", "&blk34;");
        initialMap.put("\u25A1", "&square;");
        initialMap.put("\u25AA", "&squf;");
        initialMap.put("\u25AB", "&EmptyVerySmallSquare;");
        initialMap.put("\u25AD", "&rect;");
        initialMap.put("\u25AE", "&marker;");
        initialMap.put("\u25B1", "&fltns;");
        initialMap.put("\u25B3", "&xutri;");
        initialMap.put("\u25B4", "&utrif;");
        initialMap.put("\u25B5", "&utri;");
        initialMap.put("\u25B8", "&rtrif;");
        initialMap.put("\u25B9", "&triangleright;");
        initialMap.put("\u25BD", "&xdtri;");
        initialMap.put("\u25BE", "&dtrif;");
        initialMap.put("\u25BF", "&triangledown;");
        initialMap.put("\u25C2", "&ltrif;");
        initialMap.put("\u25C3", "&triangleleft;");
        initialMap.put("\u25CB", "&cir;");
        initialMap.put("\u25EC", "&tridot;");
        initialMap.put("\u25EF", "&xcirc;");
        initialMap.put("\u25F8", "&ultri;");
        initialMap.put("\u25F9", "&urtri;");
        initialMap.put("\u25FA", "&lltri;");
        initialMap.put("\u25FB", "&EmptySmallSquare;");
        initialMap.put("\u25FC", "&FilledSmallSquare;");
        initialMap.put("\u2605", "&starf;");
        initialMap.put("\u2606", "&star;");
        initialMap.put("\u260E", "&phone;");
        initialMap.put("\u2640", "&female;");
        initialMap.put("\u2642", "&male;");
        initialMap.put("\u266A", "&sung;");
        initialMap.put("\u266D", "&flat;");
        initialMap.put("\u266E", "&natural;");
        initialMap.put("\u266F", "&sharp;");
        initialMap.put("\u2713", "&checkmark;");
        initialMap.put("\u2717", "&cross;");
        initialMap.put("\u2720", "&maltese;");
        initialMap.put("\u2736", "&sext;");
        initialMap.put("\u2758", "&VerticalSeparator;");
        initialMap.put("\u2772", "&lbbrk;");
        initialMap.put("\u2773", "&rbbrk;");
        initialMap.put("\u27C8", "&bsolhsub;");
        initialMap.put("\u27C9", "&suphsol;");
        initialMap.put("\u27E6", "&lobrk;");
        initialMap.put("\u27E7", "&robrk;");
        initialMap.put("\u27E8", "&langle;");
        initialMap.put("\u27E9", "&rangle;");
        initialMap.put("\u27EA", "&Lang;");
        initialMap.put("\u27EB", "&Rang;");
        initialMap.put("\u27EC", "&loang;");
        initialMap.put("\u27ED", "&roang;");
        initialMap.put("\u27F5", "&xlarr;");
        initialMap.put("\u27F6", "&xrarr;");
        initialMap.put("\u27F7", "&xharr;");
        initialMap.put("\u27F8", "&xlArr;");
        initialMap.put("\u27F9", "&xrArr;");
        initialMap.put("\u27FA", "&xhArr;");
        initialMap.put("\u27FC", "&xmap;");
        initialMap.put("\u27FF", "&dzigrarr;");
        initialMap.put("\u2902", "&nvlArr;");
        initialMap.put("\u2903", "&nvrArr;");
        initialMap.put("\u2904", "&nvHarr;");
        initialMap.put("\u2905", "&Map;");
        initialMap.put("\u290C", "&lbarr;");
        initialMap.put("\u290D", "&rbarr;");
        initialMap.put("\u290E", "&lBarr;");
        initialMap.put("\u290F", "&rBarr;");
        initialMap.put("\u2910", "&drbkarow;");
        initialMap.put("\u2911", "&DDotrahd;");
        initialMap.put("\u2912", "&UpArrowBar;");
        initialMap.put("\u2913", "&DownArrowBar;");
        initialMap.put("\u2916", "&Rarrtl;");
        initialMap.put("\u2919", "&latail;");
        initialMap.put("\u291A", "&ratail;");
        initialMap.put("\u291B", "&lAtail;");
        initialMap.put("\u291C", "&rAtail;");
        initialMap.put("\u291D", "&larrfs;");
        initialMap.put("\u291E", "&rarrfs;");
        initialMap.put("\u291F", "&larrbfs;");
        initialMap.put("\u2920", "&rarrbfs;");
        initialMap.put("\u2923", "&nwarhk;");
        initialMap.put("\u2924", "&nearhk;");
        initialMap.put("\u2925", "&searhk;");
        initialMap.put("\u2926", "&swarhk;");
        initialMap.put("\u2927", "&nwnear;");
        initialMap.put("\u2928", "&toea;");
        initialMap.put("\u2929", "&tosa;");
        initialMap.put("\u292A", "&swnwar;");
        initialMap.put("\u2933", "&rarrc;");
        initialMap.put("\u2933\u0338", "&nrarrc;");
        initialMap.put("\u2935", "&cudarrr;");
        initialMap.put("\u2936", "&ldca;");
        initialMap.put("\u2937", "&rdca;");
        initialMap.put("\u2938", "&cudarrl;");
        initialMap.put("\u2939", "&larrpl;");
        initialMap.put("\u293C", "&curarrm;");
        initialMap.put("\u293D", "&cularrp;");
        initialMap.put("\u2945", "&rarrpl;");
        initialMap.put("\u2948", "&harrcir;");
        initialMap.put("\u2949", "&Uarrocir;");
        initialMap.put("\u294A", "&lurdshar;");
        initialMap.put("\u294B", "&ldrushar;");
        initialMap.put("\u294E", "&LeftRightVector;");
        initialMap.put("\u294F", "&RightUpDownVector;");
        initialMap.put("\u2950", "&DownLeftRightVector;");
        initialMap.put("\u2951", "&LeftUpDownVector;");
        initialMap.put("\u2952", "&LeftVectorBar;");
        initialMap.put("\u2953", "&RightVectorBar;");
        initialMap.put("\u2954", "&RightUpVectorBar;");
        initialMap.put("\u2955", "&RightDownVectorBar;");
        initialMap.put("\u2956", "&DownLeftVectorBar;");
        initialMap.put("\u2957", "&DownRightVectorBar;");
        initialMap.put("\u2958", "&LeftUpVectorBar;");
        initialMap.put("\u2959", "&LeftDownVectorBar;");
        initialMap.put("\u295A", "&LeftTeeVector;");
        initialMap.put("\u295B", "&RightTeeVector;");
        initialMap.put("\u295C", "&RightUpTeeVector;");
        initialMap.put("\u295D", "&RightDownTeeVector;");
        initialMap.put("\u295E", "&DownLeftTeeVector;");
        initialMap.put("\u295F", "&DownRightTeeVector;");
        initialMap.put("\u2960", "&LeftUpTeeVector;");
        initialMap.put("\u2961", "&LeftDownTeeVector;");
        initialMap.put("\u2962", "&lHar;");
        initialMap.put("\u2963", "&uHar;");
        initialMap.put("\u2964", "&rHar;");
        initialMap.put("\u2965", "&dHar;");
        initialMap.put("\u2966", "&luruhar;");
        initialMap.put("\u2967", "&ldrdhar;");
        initialMap.put("\u2968", "&ruluhar;");
        initialMap.put("\u2969", "&rdldhar;");
        initialMap.put("\u296A", "&lharul;");
        initialMap.put("\u296B", "&llhard;");
        initialMap.put("\u296C", "&rharul;");
        initialMap.put("\u296D", "&lrhard;");
        initialMap.put("\u296E", "&udhar;");
        initialMap.put("\u296F", "&duhar;");
        initialMap.put("\u2970", "&RoundImplies;");
        initialMap.put("\u2971", "&erarr;");
        initialMap.put("\u2972", "&simrarr;");
        initialMap.put("\u2973", "&larrsim;");
        initialMap.put("\u2974", "&rarrsim;");
        initialMap.put("\u2975", "&rarrap;");
        initialMap.put("\u2976", "&ltlarr;");
        initialMap.put("\u2978", "&gtrarr;");
        initialMap.put("\u2979", "&subrarr;");
        initialMap.put("\u297B", "&suplarr;");
        initialMap.put("\u297C", "&lfisht;");
        initialMap.put("\u297D", "&rfisht;");
        initialMap.put("\u297E", "&ufisht;");
        initialMap.put("\u297F", "&dfisht;");
        initialMap.put("\u2985", "&lopar;");
        initialMap.put("\u2986", "&ropar;");
        initialMap.put("\u298B", "&lbrke;");
        initialMap.put("\u298C", "&rbrke;");
        initialMap.put("\u298D", "&lbrkslu;");
        initialMap.put("\u298E", "&rbrksld;");
        initialMap.put("\u298F", "&lbrksld;");
        initialMap.put("\u2990", "&rbrkslu;");
        initialMap.put("\u2991", "&langd;");
        initialMap.put("\u2992", "&rangd;");
        initialMap.put("\u2993", "&lparlt;");
        initialMap.put("\u2994", "&rpargt;");
        initialMap.put("\u2995", "&gtlPar;");
        initialMap.put("\u2996", "&ltrPar;");
        initialMap.put("\u299A", "&vzigzag;");
        initialMap.put("\u299C", "&vangrt;");
        initialMap.put("\u299D", "&angrtvbd;");
        initialMap.put("\u29A4", "&ange;");
        initialMap.put("\u29A5", "&range;");
        initialMap.put("\u29A6", "&dwangle;");
        initialMap.put("\u29A7", "&uwangle;");
        initialMap.put("\u29A8", "&angmsdaa;");
        initialMap.put("\u29A9", "&angmsdab;");
        initialMap.put("\u29AA", "&angmsdac;");
        initialMap.put("\u29AB", "&angmsdad;");
        initialMap.put("\u29AC", "&angmsdae;");
        initialMap.put("\u29AD", "&angmsdaf;");
        initialMap.put("\u29AE", "&angmsdag;");
        initialMap.put("\u29AF", "&angmsdah;");
        initialMap.put("\u29B0", "&bemptyv;");
        initialMap.put("\u29B1", "&demptyv;");
        initialMap.put("\u29B2", "&cemptyv;");
        initialMap.put("\u29B3", "&raemptyv;");
        initialMap.put("\u29B4", "&laemptyv;");
        initialMap.put("\u29B5", "&ohbar;");
        initialMap.put("\u29B6", "&omid;");
        initialMap.put("\u29B7", "&opar;");
        initialMap.put("\u29B9", "&operp;");
        initialMap.put("\u29BB", "&olcross;");
        initialMap.put("\u29BC", "&odsold;");
        initialMap.put("\u29BE", "&olcir;");
        initialMap.put("\u29BF", "&ofcir;");
        initialMap.put("\u29C0", "&olt;");
        initialMap.put("\u29C1", "&ogt;");
        initialMap.put("\u29C2", "&cirscir;");
        initialMap.put("\u29C3", "&cirE;");
        initialMap.put("\u29C4", "&solb;");
        initialMap.put("\u29C5", "&bsolb;");
        initialMap.put("\u29C9", "&boxbox;");
        initialMap.put("\u29CD", "&trisb;");
        initialMap.put("\u29CE", "&rtriltri;");
        initialMap.put("\u29CF", "&LeftTriangleBar;");
        initialMap.put("\u29CF\u0338", "&NotLeftTriangleBar;");
        initialMap.put("\u29D0", "&RightTriangleBar;");
        initialMap.put("\u29D0\u0338", "&NotRightTriangleBar;");
        initialMap.put("\u29DC", "&iinfin;");
        initialMap.put("\u29DD", "&infintie;");
        initialMap.put("\u29DE", "&nvinfin;");
        initialMap.put("\u29E3", "&eparsl;");
        initialMap.put("\u29E4", "&smeparsl;");
        initialMap.put("\u29E5", "&eqvparsl;");
        initialMap.put("\u29EB", "&lozf;");
        initialMap.put("\u29F4", "&RuleDelayed;");
        initialMap.put("\u29F6", "&dsol;");
        initialMap.put("\u2A00", "&xodot;");
        initialMap.put("\u2A01", "&xoplus;");
        initialMap.put("\u2A02", "&xotime;");
        initialMap.put("\u2A04", "&xuplus;");
        initialMap.put("\u2A06", "&xsqcup;");
        initialMap.put("\u2A0C", "&qint;");
        initialMap.put("\u2A0D", "&fpartint;");
        initialMap.put("\u2A10", "&cirfnint;");
        initialMap.put("\u2A11", "&awint;");
        initialMap.put("\u2A12", "&rppolint;");
        initialMap.put("\u2A13", "&scpolint;");
        initialMap.put("\u2A14", "&npolint;");
        initialMap.put("\u2A15", "&pointint;");
        initialMap.put("\u2A16", "&quatint;");
        initialMap.put("\u2A17", "&intlarhk;");
        initialMap.put("\u2A22", "&pluscir;");
        initialMap.put("\u2A23", "&plusacir;");
        initialMap.put("\u2A24", "&simplus;");
        initialMap.put("\u2A25", "&plusdu;");
        initialMap.put("\u2A26", "&plussim;");
        initialMap.put("\u2A27", "&plustwo;");
        initialMap.put("\u2A29", "&mcomma;");
        initialMap.put("\u2A2A", "&minusdu;");
        initialMap.put("\u2A2D", "&loplus;");
        initialMap.put("\u2A2E", "&roplus;");
        initialMap.put("\u2A2F", "&Cross;");
        initialMap.put("\u2A30", "&timesd;");
        initialMap.put("\u2A31", "&timesbar;");
        initialMap.put("\u2A33", "&smashp;");
        initialMap.put("\u2A34", "&lotimes;");
        initialMap.put("\u2A35", "&rotimes;");
        initialMap.put("\u2A36", "&otimesas;");
        initialMap.put("\u2A37", "&Otimes;");
        initialMap.put("\u2A38", "&odiv;");
        initialMap.put("\u2A39", "&triplus;");
        initialMap.put("\u2A3A", "&triminus;");
        initialMap.put("\u2A3B", "&tritime;");
        initialMap.put("\u2A3C", "&iprod;");
        initialMap.put("\u2A3F", "&amalg;");
        initialMap.put("\u2A40", "&capdot;");
        initialMap.put("\u2A42", "&ncup;");
        initialMap.put("\u2A43", "&ncap;");
        initialMap.put("\u2A44", "&capand;");
        initialMap.put("\u2A45", "&cupor;");
        initialMap.put("\u2A46", "&cupcap;");
        initialMap.put("\u2A47", "&capcup;");
        initialMap.put("\u2A48", "&cupbrcap;");
        initialMap.put("\u2A49", "&capbrcup;");
        initialMap.put("\u2A4A", "&cupcup;");
        initialMap.put("\u2A4B", "&capcap;");
        initialMap.put("\u2A4C", "&ccups;");
        initialMap.put("\u2A4D", "&ccaps;");
        initialMap.put("\u2A50", "&ccupssm;");
        initialMap.put("\u2A53", "&And;");
        initialMap.put("\u2A54", "&Or;");
        initialMap.put("\u2A55", "&andand;");
        initialMap.put("\u2A56", "&oror;");
        initialMap.put("\u2A57", "&orslope;");
        initialMap.put("\u2A58", "&andslope;");
        initialMap.put("\u2A5A", "&andv;");
        initialMap.put("\u2A5B", "&orv;");
        initialMap.put("\u2A5C", "&andd;");
        initialMap.put("\u2A5D", "&ord;");
        initialMap.put("\u2A5F", "&wedbar;");
        initialMap.put("\u2A66", "&sdote;");
        initialMap.put("\u2A6A", "&simdot;");
        initialMap.put("\u2A6D", "&congdot;");
        initialMap.put("\u2A6D\u0338", "&ncongdot;");
        initialMap.put("\u2A6E", "&easter;");
        initialMap.put("\u2A6F", "&apacir;");
        initialMap.put("\u2A70", "&apE;");
        initialMap.put("\u2A70\u0338", "&napE;");
        initialMap.put("\u2A71", "&eplus;");
        initialMap.put("\u2A72", "&pluse;");
        initialMap.put("\u2A73", "&Esim;");
        initialMap.put("\u2A74", "&Colone;");
        initialMap.put("\u2A75", "&Equal;");
        initialMap.put("\u2A77", "&eDDot;");
        initialMap.put("\u2A78", "&equivDD;");
        initialMap.put("\u2A79", "&ltcir;");
        initialMap.put("\u2A7A", "&gtcir;");
        initialMap.put("\u2A7B", "&ltquest;");
        initialMap.put("\u2A7C", "&gtquest;");
        initialMap.put("\u2A7D", "&les;");
        initialMap.put("\u2A7D\u0338", "&nles;");
        initialMap.put("\u2A7E", "&ges;");
        initialMap.put("\u2A7E\u0338", "&nges;");
        initialMap.put("\u2A7F", "&lesdot;");
        initialMap.put("\u2A80", "&gesdot;");
        initialMap.put("\u2A81", "&lesdoto;");
        initialMap.put("\u2A82", "&gesdoto;");
        initialMap.put("\u2A83", "&lesdotor;");
        initialMap.put("\u2A84", "&gesdotol;");
        initialMap.put("\u2A85", "&lessapprox;");
        initialMap.put("\u2A86", "&gtrapprox;");
        initialMap.put("\u2A87", "&lneq;");
        initialMap.put("\u2A88", "&gneq;");
        initialMap.put("\u2A89", "&lnapprox;");
        initialMap.put("\u2A8A", "&gnapprox;");
        initialMap.put("\u2A8B", "&lesseqqgtr;");
        initialMap.put("\u2A8C", "&gtreqqless;");
        initialMap.put("\u2A8D", "&lsime;");
        initialMap.put("\u2A8E", "&gsime;");
        initialMap.put("\u2A8F", "&lsimg;");
        initialMap.put("\u2A90", "&gsiml;");
        initialMap.put("\u2A91", "&lgE;");
        initialMap.put("\u2A92", "&glE;");
        initialMap.put("\u2A93", "&lesges;");
        initialMap.put("\u2A94", "&gesles;");
        initialMap.put("\u2A95", "&eqslantless;");
        initialMap.put("\u2A96", "&eqslantgtr;");
        initialMap.put("\u2A97", "&elsdot;");
        initialMap.put("\u2A98", "&egsdot;");
        initialMap.put("\u2A99", "&el;");
        initialMap.put("\u2A9A", "&eg;");
        initialMap.put("\u2A9D", "&siml;");
        initialMap.put("\u2A9E", "&simg;");
        initialMap.put("\u2A9F", "&simlE;");
        initialMap.put("\u2AA0", "&simgE;");
        initialMap.put("\u2AA1", "&LessLess;");
        initialMap.put("\u2AA1\u0338", "&NotNestedLessLess;");
        initialMap.put("\u2AA2", "&GreaterGreater;");
        initialMap.put("\u2AA2\u0338", "&NotNestedGreaterGreater;");
        initialMap.put("\u2AA4", "&glj;");
        initialMap.put("\u2AA5", "&gla;");
        initialMap.put("\u2AA6", "&ltcc;");
        initialMap.put("\u2AA7", "&gtcc;");
        initialMap.put("\u2AA8", "&lescc;");
        initialMap.put("\u2AA9", "&gescc;");
        initialMap.put("\u2AAA", "&smt;");
        initialMap.put("\u2AAB", "&lat;");
        initialMap.put("\u2AAC", "&smte;");
        initialMap.put("\u2AAC\uFE00", "&smtes;");
        initialMap.put("\u2AAD", "&late;");
        initialMap.put("\u2AAD\uFE00", "&lates;");
        initialMap.put("\u2AAE", "&bumpE;");
        initialMap.put("\u2AAF", "&preceq;");
        initialMap.put("\u2AAF\u0338", "&npreceq;");
        initialMap.put("\u2AB0", "&succeq;");
        initialMap.put("\u2AB0\u0338", "&nsucceq;");
        initialMap.put("\u2AB3", "&prE;");
        initialMap.put("\u2AB4", "&scE;");
        initialMap.put("\u2AB5", "&prnE;");
        initialMap.put("\u2AB6", "&succneqq;");
        initialMap.put("\u2AB7", "&precapprox;");
        initialMap.put("\u2AB8", "&succapprox;");
        initialMap.put("\u2AB9", "&prnap;");
        initialMap.put("\u2ABA", "&succnapprox;");
        initialMap.put("\u2ABB", "&Pr;");
        initialMap.put("\u2ABC", "&Sc;");
        initialMap.put("\u2ABD", "&subdot;");
        initialMap.put("\u2ABE", "&supdot;");
        initialMap.put("\u2ABF", "&subplus;");
        initialMap.put("\u2AC0", "&supplus;");
        initialMap.put("\u2AC1", "&submult;");
        initialMap.put("\u2AC2", "&supmult;");
        initialMap.put("\u2AC3", "&subedot;");
        initialMap.put("\u2AC4", "&supedot;");
        initialMap.put("\u2AC5", "&subseteqq;");
        initialMap.put("\u2AC5\u0338", "&nsubseteqq;");
        initialMap.put("\u2AC6", "&supseteqq;");
        initialMap.put("\u2AC6\u0338", "&nsupseteqq;");
        initialMap.put("\u2AC7", "&subsim;");
        initialMap.put("\u2AC8", "&supsim;");
        initialMap.put("\u2ACB", "&subsetneqq;");
        initialMap.put("\u2ACB\uFE00", "&vsubnE;");
        initialMap.put("\u2ACC", "&supsetneqq;");
        initialMap.put("\u2ACC\uFE00", "&vsupnE;");
        initialMap.put("\u2ACF", "&csub;");
        initialMap.put("\u2AD0", "&csup;");
        initialMap.put("\u2AD1", "&csube;");
        initialMap.put("\u2AD2", "&csupe;");
        initialMap.put("\u2AD3", "&subsup;");
        initialMap.put("\u2AD4", "&supsub;");
        initialMap.put("\u2AD5", "&subsub;");
        initialMap.put("\u2AD6", "&supsup;");
        initialMap.put("\u2AD7", "&suphsub;");
        initialMap.put("\u2AD8", "&supdsub;");
        initialMap.put("\u2AD9", "&forkv;");
        initialMap.put("\u2ADA", "&topfork;");
        initialMap.put("\u2ADB", "&mlcp;");
        initialMap.put("\u2AE4", "&DoubleLeftTee;");
        initialMap.put("\u2AE6", "&Vdashl;");
        initialMap.put("\u2AE7", "&Barv;");
        initialMap.put("\u2AE8", "&vBar;");
        initialMap.put("\u2AE9", "&vBarv;");
        initialMap.put("\u2AEB", "&Vbar;");
        initialMap.put("\u2AEC", "&Not;");
        initialMap.put("\u2AED", "&bNot;");
        initialMap.put("\u2AEE", "&rnmid;");
        initialMap.put("\u2AEF", "&cirmid;");
        initialMap.put("\u2AF0", "&midcir;");
        initialMap.put("\u2AF1", "&topcir;");
        initialMap.put("\u2AF2", "&nhpar;");
        initialMap.put("\u2AF3", "&parsim;");
        initialMap.put("\u2AFD", "&parsl;");
        initialMap.put("\u2AFD\u20E5", "&nparsl;");
        initialMap.put("\uD835\uDC9C", "&Ascr;");
        initialMap.put("\uD835\uDC9E", "&Cscr;");
        initialMap.put("\uD835\uDC9F", "&Dscr;");
        initialMap.put("\uD835\uDCA2", "&Gscr;");
        initialMap.put("\uD835\uDCA5", "&Jscr;");
        initialMap.put("\uD835\uDCA6", "&Kscr;");
        initialMap.put("\uD835\uDCA9", "&Nscr;");
        initialMap.put("\uD835\uDCAA", "&Oscr;");
        initialMap.put("\uD835\uDCAB", "&Pscr;");
        initialMap.put("\uD835\uDCAC", "&Qscr;");
        initialMap.put("\uD835\uDCAE", "&Sscr;");
        initialMap.put("\uD835\uDCAF", "&Tscr;");
        initialMap.put("\uD835\uDCB0", "&Uscr;");
        initialMap.put("\uD835\uDCB1", "&Vscr;");
        initialMap.put("\uD835\uDCB2", "&Wscr;");
        initialMap.put("\uD835\uDCB3", "&Xscr;");
        initialMap.put("\uD835\uDCB4", "&Yscr;");
        initialMap.put("\uD835\uDCB5", "&Zscr;");
        initialMap.put("\uD835\uDCB6", "&ascr;");
        initialMap.put("\uD835\uDCB7", "&bscr;");
        initialMap.put("\uD835\uDCB8", "&cscr;");
        initialMap.put("\uD835\uDCB9", "&dscr;");
        initialMap.put("\uD835\uDCBB", "&fscr;");
        initialMap.put("\uD835\uDCBD", "&hscr;");
        initialMap.put("\uD835\uDCBE", "&iscr;");
        initialMap.put("\uD835\uDCBF", "&jscr;");
        initialMap.put("\uD835\uDCC0", "&kscr;");
        initialMap.put("\uD835\uDCC1", "&lscr;");
        initialMap.put("\uD835\uDCC2", "&mscr;");
        initialMap.put("\uD835\uDCC3", "&nscr;");
        initialMap.put("\uD835\uDCC5", "&pscr;");
        initialMap.put("\uD835\uDCC6", "&qscr;");
        initialMap.put("\uD835\uDCC7", "&rscr;");
        initialMap.put("\uD835\uDCC8", "&sscr;");
        initialMap.put("\uD835\uDCC9", "&tscr;");
        initialMap.put("\uD835\uDCCA", "&uscr;");
        initialMap.put("\uD835\uDCCB", "&vscr;");
        initialMap.put("\uD835\uDCCC", "&wscr;");
        initialMap.put("\uD835\uDCCD", "&xscr;");
        initialMap.put("\uD835\uDCCE", "&yscr;");
        initialMap.put("\uD835\uDCCF", "&zscr;");
        initialMap.put("\uD835\uDD04", "&Afr;");
        initialMap.put("\uD835\uDD05", "&Bfr;");
        initialMap.put("\uD835\uDD07", "&Dfr;");
        initialMap.put("\uD835\uDD08", "&Efr;");
        initialMap.put("\uD835\uDD09", "&Ffr;");
        initialMap.put("\uD835\uDD0A", "&Gfr;");
        initialMap.put("\uD835\uDD0D", "&Jfr;");
        initialMap.put("\uD835\uDD0E", "&Kfr;");
        initialMap.put("\uD835\uDD0F", "&Lfr;");
        initialMap.put("\uD835\uDD10", "&Mfr;");
        initialMap.put("\uD835\uDD11", "&Nfr;");
        initialMap.put("\uD835\uDD12", "&Ofr;");
        initialMap.put("\uD835\uDD13", "&Pfr;");
        initialMap.put("\uD835\uDD14", "&Qfr;");
        initialMap.put("\uD835\uDD16", "&Sfr;");
        initialMap.put("\uD835\uDD17", "&Tfr;");
        initialMap.put("\uD835\uDD18", "&Ufr;");
        initialMap.put("\uD835\uDD19", "&Vfr;");
        initialMap.put("\uD835\uDD1A", "&Wfr;");
        initialMap.put("\uD835\uDD1B", "&Xfr;");
        initialMap.put("\uD835\uDD1C", "&Yfr;");
        initialMap.put("\uD835\uDD1E", "&afr;");
        initialMap.put("\uD835\uDD1F", "&bfr;");
        initialMap.put("\uD835\uDD20", "&cfr;");
        initialMap.put("\uD835\uDD21", "&dfr;");
        initialMap.put("\uD835\uDD22", "&efr;");
        initialMap.put("\uD835\uDD23", "&ffr;");
        initialMap.put("\uD835\uDD24", "&gfr;");
        initialMap.put("\uD835\uDD25", "&hfr;");
        initialMap.put("\uD835\uDD26", "&ifr;");
        initialMap.put("\uD835\uDD27", "&jfr;");
        initialMap.put("\uD835\uDD28", "&kfr;");
        initialMap.put("\uD835\uDD29", "&lfr;");
        initialMap.put("\uD835\uDD2A", "&mfr;");
        initialMap.put("\uD835\uDD2B", "&nfr;");
        initialMap.put("\uD835\uDD2C", "&ofr;");
        initialMap.put("\uD835\uDD2D", "&pfr;");
        initialMap.put("\uD835\uDD2E", "&qfr;");
        initialMap.put("\uD835\uDD2F", "&rfr;");
        initialMap.put("\uD835\uDD30", "&sfr;");
        initialMap.put("\uD835\uDD31", "&tfr;");
        initialMap.put("\uD835\uDD32", "&ufr;");
        initialMap.put("\uD835\uDD33", "&vfr;");
        initialMap.put("\uD835\uDD34", "&wfr;");
        initialMap.put("\uD835\uDD35", "&xfr;");
        initialMap.put("\uD835\uDD36", "&yfr;");
        initialMap.put("\uD835\uDD37", "&zfr;");
        initialMap.put("\uD835\uDD38", "&Aopf;");
        initialMap.put("\uD835\uDD39", "&Bopf;");
        initialMap.put("\uD835\uDD3B", "&Dopf;");
        initialMap.put("\uD835\uDD3C", "&Eopf;");
        initialMap.put("\uD835\uDD3D", "&Fopf;");
        initialMap.put("\uD835\uDD3E", "&Gopf;");
        initialMap.put("\uD835\uDD40", "&Iopf;");
        initialMap.put("\uD835\uDD41", "&Jopf;");
        initialMap.put("\uD835\uDD42", "&Kopf;");
        initialMap.put("\uD835\uDD43", "&Lopf;");
        initialMap.put("\uD835\uDD44", "&Mopf;");
        initialMap.put("\uD835\uDD46", "&Oopf;");
        initialMap.put("\uD835\uDD4A", "&Sopf;");
        initialMap.put("\uD835\uDD4B", "&Topf;");
        initialMap.put("\uD835\uDD4C", "&Uopf;");
        initialMap.put("\uD835\uDD4D", "&Vopf;");
        initialMap.put("\uD835\uDD4E", "&Wopf;");
        initialMap.put("\uD835\uDD4F", "&Xopf;");
        initialMap.put("\uD835\uDD50", "&Yopf;");
        initialMap.put("\uD835\uDD52", "&aopf;");
        initialMap.put("\uD835\uDD53", "&bopf;");
        initialMap.put("\uD835\uDD54", "&copf;");
        initialMap.put("\uD835\uDD55", "&dopf;");
        initialMap.put("\uD835\uDD56", "&eopf;");
        initialMap.put("\uD835\uDD57", "&fopf;");
        initialMap.put("\uD835\uDD58", "&gopf;");
        initialMap.put("\uD835\uDD59", "&hopf;");
        initialMap.put("\uD835\uDD5A", "&iopf;");
        initialMap.put("\uD835\uDD5B", "&jopf;");
        initialMap.put("\uD835\uDD5C", "&kopf;");
        initialMap.put("\uD835\uDD5D", "&lopf;");
        initialMap.put("\uD835\uDD5E", "&mopf;");
        initialMap.put("\uD835\uDD5F", "&nopf;");
        initialMap.put("\uD835\uDD60", "&oopf;");
        initialMap.put("\uD835\uDD61", "&popf;");
        initialMap.put("\uD835\uDD62", "&qopf;");
        initialMap.put("\uD835\uDD63", "&ropf;");
        initialMap.put("\uD835\uDD64", "&sopf;");
        initialMap.put("\uD835\uDD65", "&topf;");
        initialMap.put("\uD835\uDD66", "&uopf;");
        initialMap.put("\uD835\uDD67", "&vopf;");
        initialMap.put("\uD835\uDD68", "&wopf;");
        initialMap.put("\uD835\uDD69", "&xopf;");
        initialMap.put("\uD835\uDD6A", "&yopf;");
        initialMap.put("\uD835\uDD6B", "&zopf;");
        initialMap.put("\uFB00", "&fflig;");
        initialMap.put("\uFB01", "&filig;");
        initialMap.put("\uFB02", "&fllig;");
        initialMap.put("\uFB03", "&ffilig;");
        initialMap.put("\uFB04", "&ffllig;");
        HTML50_EXTENDED_ESCAPE = Collections.unmodifiableMap(initialMap);
    }

    /**
     * Reverse of {@link #HTML50_EXTENDED_ESCAPE} for unescaping purposes.
     * Additional character entities, synonymous with ones found in {@link #BASIC_ESCAPE},
     * {@link #ISO8859_1_ESCAPE}, {@link #HTML40_EXTENDED_ESCAPE} and
     * {@link #HTML50_EXTENDED_ESCAPE} are added.
     */
    public static final Map<CharSequence, CharSequence> HTML50_EXTENDED_UNESCAPE;

    static {
        final Map<CharSequence, CharSequence> initialMap = invert(HTML50_EXTENDED_ESCAPE);
        initialMap.put("&ast;", "\u002A");
        initialMap.put("&lbrack;", "\u005B");
        initialMap.put("&rbrack;", "\u005D");
        initialMap.put("&UnderBar;", "\u005F");
        initialMap.put("&DiacriticalGrave;", "\u0060");
        initialMap.put("&lbrace;", "\u007B");
        initialMap.put("&VerticalLine;", "\u007C");
        initialMap.put("&verbar;", "\u007C");
        initialMap.put("&rbrace;", "\u007D");
        initialMap.put("&NonBreakingSpace;", "\u00A0");
        initialMap.put("&Dot;", "\u00A8");
        initialMap.put("&DoubleDot;", "\u00A8");
        initialMap.put("&die;", "\u00A8");
        initialMap.put("&COPY;", "\u00A9");
        initialMap.put("&REG;", "\u00AE");
        initialMap.put("&circledR;", "\u00AE");
        initialMap.put("&strns;", "\u00AF");
        initialMap.put("&PlusMinus;", "\u00B1");
        initialMap.put("&pm;", "\u00B1");
        initialMap.put("&DiacriticalAcute;", "\u00B4");
        initialMap.put("&CenterDot;", "\u00B7");
        initialMap.put("&centerdot;", "\u00B7");
        initialMap.put("&Cedilla;", "\u00B8");
        initialMap.put("&half;", "\u00BD");
        initialMap.put("&angst;", "\u00C5");
        initialMap.put("&div;", "\u00F7");
        initialMap.put("&imath;", "\u0131");
        initialMap.put("&Hacek;", "\u02C7");
        initialMap.put("&Breve;", "\u02D8");
        initialMap.put("&DiacriticalDot;", "\u02D9");
        initialMap.put("&DiacriticalTilde;", "\u02DC");
        initialMap.put("&DiacriticalDoubleAcute;", "\u02DD");
        initialMap.put("&ohm;", "\u03A9");
        initialMap.put("&epsi;", "\u03B5");
        initialMap.put("&sigmav;", "\u03C2");
        initialMap.put("&varsigma;", "\u03C2");
        initialMap.put("&upsi;", "\u03C5");
        initialMap.put("&thetav;", "\u03D1");
        initialMap.put("&vartheta;", "\u03D1");
        initialMap.put("&Upsi;", "\u03D2");
        initialMap.put("&phiv;", "\u03D5");
        initialMap.put("&straightphi;", "\u03D5");
        initialMap.put("&varpi;", "\u03D6");
        initialMap.put("&digamma;", "\u03DD");
        initialMap.put("&kappav;", "\u03F0");
        initialMap.put("&rhov;", "\u03F1");
        initialMap.put("&epsiv;", "\u03F5");
        initialMap.put("&straightepsilon;", "\u03F5");
        initialMap.put("&backepsilon;", "\u03F6");
        initialMap.put("&ThinSpace;", "\u2009");
        initialMap.put("&VeryThinSpace;", "\u200A");
        initialMap.put("&NegativeMediumSpace;", "\u200B");
        initialMap.put("&NegativeThickSpace;", "\u200B");
        initialMap.put("&NegativeThinSpace;", "\u200B");
        initialMap.put("&NegativeVeryThinSpace;", "\u200B");
        initialMap.put("&dash;", "\u2010");
        initialMap.put("&Verbar;", "\u2016");
        initialMap.put("&OpenCurlyQuote;", "\u2018");
        initialMap.put("&CloseCurlyQuote;", "\u2019");
        initialMap.put("&rsquor;", "\u2019");
        initialMap.put("&lsquor;", "\u201A");
        initialMap.put("&OpenCurlyDoubleQuote;", "\u201C");
        initialMap.put("&CloseCurlyDoubleQuote;", "\u201D");
        initialMap.put("&rdquor;", "\u201D");
        initialMap.put("&ldquor;", "\u201E");
        initialMap.put("&ddagger;", "\u2021");
        initialMap.put("&bullet;", "\u2022");
        initialMap.put("&mldr;", "\u2026");
        initialMap.put("&backprime;", "\u2035");
        initialMap.put("&OverBar;", "\u203E");
        initialMap.put("&ApplyFunction;", "\u2061");
        initialMap.put("&InvisibleTimes;", "\u2062");
        initialMap.put("&InvisibleComma;", "\u2063");
        initialMap.put("&TripleDot;", "\u20DB");
        initialMap.put("&Copf;", "\u2102");
        initialMap.put("&HilbertSpace;", "\u210B");
        initialMap.put("&Hscr;", "\u210B");
        initialMap.put("&Hfr;", "\u210C");
        initialMap.put("&Hopf;", "\u210D");
        initialMap.put("&hbar;", "\u210F");
        initialMap.put("&hslash;", "\u210F");
        initialMap.put("&planck;", "\u210F");
        initialMap.put("&Iscr;", "\u2110");
        initialMap.put("&Ifr;", "\u2111");
        initialMap.put("&Im;", "\u2111");
        initialMap.put("&imagpart;", "\u2111");
        initialMap.put("&Laplacetrf;", "\u2112");
        initialMap.put("&Lscr;", "\u2112");
        initialMap.put("&Nopf;", "\u2115");
        initialMap.put("&wp;", "\u2118");
        initialMap.put("&Popf;", "\u2119");
        initialMap.put("&Qopf;", "\u211A");
        initialMap.put("&Rscr;", "\u211B");
        initialMap.put("&Re;", "\u211C");
        initialMap.put("&Rfr;", "\u211C");
        initialMap.put("&realpart;", "\u211C");
        initialMap.put("&Ropf;", "\u211D");
        initialMap.put("&TRADE;", "\u2122");
        initialMap.put("&Zopf;", "\u2124");
        initialMap.put("&Zfr;", "\u2128");
        initialMap.put("&Bernoullis;", "\u212C");
        initialMap.put("&Bscr;", "\u212C");
        initialMap.put("&Cayleys;", "\u212D");
        initialMap.put("&Escr;", "\u2130");
        initialMap.put("&Fouriertrf;", "\u2131");
        initialMap.put("&Mellintrf;", "\u2133");
        initialMap.put("&Mscr;", "\u2133");
        initialMap.put("&order;", "\u2134");
        initialMap.put("&orderof;", "\u2134");
        initialMap.put("&aleph;", "\u2135");
        initialMap.put("&CapitalDifferentialD;", "\u2145");
        initialMap.put("&DifferentialD;", "\u2146");
        initialMap.put("&ExponentialE;", "\u2147");
        initialMap.put("&ee;", "\u2147");
        initialMap.put("&ImaginaryI;", "\u2148");
        initialMap.put("&LeftArrow;", "\u2190");
        initialMap.put("&ShortLeftArrow;", "\u2190");
        initialMap.put("&leftarrow;", "\u2190");
        initialMap.put("&slarr;", "\u2190");
        initialMap.put("&ShortUpArrow;", "\u2191");
        initialMap.put("&UpArrow;", "\u2191");
        initialMap.put("&uparrow;", "\u2191");
        initialMap.put("&RightArrow;", "\u2192");
        initialMap.put("&ShortRightArrow;", "\u2192");
        initialMap.put("&rightarrow;", "\u2192");
        initialMap.put("&srarr;", "\u2192");
        initialMap.put("&DownArrow;", "\u2193");
        initialMap.put("&ShortDownArrow;", "\u2193");
        initialMap.put("&downarrow;", "\u2193");
        initialMap.put("&LeftRightArrow;", "\u2194");
        initialMap.put("&leftrightarrow;", "\u2194");
        initialMap.put("&UpDownArrow;", "\u2195");
        initialMap.put("&updownarrow;", "\u2195");
        initialMap.put("&UpperLeftArrow;", "\u2196");
        initialMap.put("&nwarr;", "\u2196");
        initialMap.put("&UpperRightArrow;", "\u2197");
        initialMap.put("&nearr;", "\u2197");
        initialMap.put("&LowerRightArrow;", "\u2198");
        initialMap.put("&searr;", "\u2198");
        initialMap.put("&LowerLeftArrow;", "\u2199");
        initialMap.put("&swarr;", "\u2199");
        initialMap.put("&nlarr;", "\u219A");
        initialMap.put("&nrarr;", "\u219B");
        initialMap.put("&rarrw;", "\u219D");
        initialMap.put("&Larr;", "\u219E");
        initialMap.put("&Rarr;", "\u21A0");
        initialMap.put("&larrtl;", "\u21A2");
        initialMap.put("&rarrtl;", "\u21A3");
        initialMap.put("&LeftTeeArrow;", "\u21A4");
        initialMap.put("&UpTeeArrow;", "\u21A5");
        initialMap.put("&RightTeeArrow;", "\u21A6");
        initialMap.put("&map;", "\u21A6");
        initialMap.put("&DownTeeArrow;", "\u21A7");
        initialMap.put("&hookleftarrow;", "\u21A9");
        initialMap.put("&hookrightarrow;", "\u21AA");
        initialMap.put("&larrlp;", "\u21AB");
        initialMap.put("&looparrowright;", "\u21AC");
        initialMap.put("&harrw;", "\u21AD");
        initialMap.put("&nharr;", "\u21AE");
        initialMap.put("&Lsh;", "\u21B0");
        initialMap.put("&Rsh;", "\u21B1");
        initialMap.put("&cularr;", "\u21B6");
        initialMap.put("&curarr;", "\u21B7");
        initialMap.put("&circlearrowleft;", "\u21BA");
        initialMap.put("&circlearrowright;", "\u21BB");
        initialMap.put("&LeftVector;", "\u21BC");
        initialMap.put("&leftharpoonup;", "\u21BC");
        initialMap.put("&DownLeftVector;", "\u21BD");
        initialMap.put("&leftharpoondown;", "\u21BD");
        initialMap.put("&RightUpVector;", "\u21BE");
        initialMap.put("&uharr;", "\u21BE");
        initialMap.put("&LeftUpVector;", "\u21BF");
        initialMap.put("&uharl;", "\u21BF");
        initialMap.put("&RightVector;", "\u21C0");
        initialMap.put("&rharu;", "\u21C0");
        initialMap.put("&DownRightVector;", "\u21C1");
        initialMap.put("&rhard;", "\u21C1");
        initialMap.put("&RightDownVector;", "\u21C2");
        initialMap.put("&dharr;", "\u21C2");
        initialMap.put("&LeftDownVector;", "\u21C3");
        initialMap.put("&dharl;", "\u21C3");
        initialMap.put("&RightArrowLeftArrow;", "\u21C4");
        initialMap.put("&rightleftarrows;", "\u21C4");
        initialMap.put("&UpArrowDownArrow;", "\u21C5");
        initialMap.put("&LeftArrowRightArrow;", "\u21C6");
        initialMap.put("&leftrightarrows;", "\u21C6");
        initialMap.put("&leftleftarrows;", "\u21C7");
        initialMap.put("&upuparrows;", "\u21C8");
        initialMap.put("&rightrightarrows;", "\u21C9");
        initialMap.put("&ddarr;", "\u21CA");
        initialMap.put("&ReverseEquilibrium;", "\u21CB");
        initialMap.put("&leftrightharpoons;", "\u21CB");
        initialMap.put("&Equilibrium;", "\u21CC");
        initialMap.put("&rightleftharpoons;", "\u21CC");
        initialMap.put("&nLeftarrow;", "\u21CD");
        initialMap.put("&nLeftrightarrow;", "\u21CE");
        initialMap.put("&nRightarrow;", "\u21CF");
        initialMap.put("&DoubleLeftArrow;", "\u21D0");
        initialMap.put("&Leftarrow;", "\u21D0");
        initialMap.put("&DoubleUpArrow;", "\u21D1");
        initialMap.put("&Uparrow;", "\u21D1");
        initialMap.put("&DoubleRightArrow;", "\u21D2");
        initialMap.put("&Implies;", "\u21D2");
        initialMap.put("&Rightarrow;", "\u21D2");
        initialMap.put("&DoubleDownArrow;", "\u21D3");
        initialMap.put("&Downarrow;", "\u21D3");
        initialMap.put("&DoubleLeftRightArrow;", "\u21D4");
        initialMap.put("&Leftrightarrow;", "\u21D4");
        initialMap.put("&iff;", "\u21D4");
        initialMap.put("&DoubleUpDownArrow;", "\u21D5");
        initialMap.put("&Updownarrow;", "\u21D5");
        initialMap.put("&Lleftarrow;", "\u21DA");
        initialMap.put("&Rrightarrow;", "\u21DB");
        initialMap.put("&LeftArrowBar;", "\u21E4");
        initialMap.put("&RightArrowBar;", "\u21E5");
        initialMap.put("&DownArrowUpArrow;", "\u21F5");
        initialMap.put("&ForAll;", "\u2200");
        initialMap.put("&comp;", "\u2201");
        initialMap.put("&PartialD;", "\u2202");
        initialMap.put("&Exists;", "\u2203");
        initialMap.put("&NotExists;", "\u2204");
        initialMap.put("&nexist;", "\u2204");
        initialMap.put("&emptyset;", "\u2205");
        initialMap.put("&emptyv;", "\u2205");
        initialMap.put("&varnothing;", "\u2205");
        initialMap.put("&Del;", "\u2207");
        initialMap.put("&Element;", "\u2208");
        initialMap.put("&in;", "\u2208");
        initialMap.put("&isinv;", "\u2208");
        initialMap.put("&NotElement;", "\u2209");
        initialMap.put("&notinva;", "\u2209");
        initialMap.put("&ReverseElement;", "\u220B");
        initialMap.put("&SuchThat;", "\u220B");
        initialMap.put("&niv;", "\u220B");
        initialMap.put("&NotReverseElement;", "\u220C");
        initialMap.put("&notni;", "\u220C");
        initialMap.put("&Product;", "\u220F");
        initialMap.put("&Coproduct;", "\u2210");
        initialMap.put("&Sum;", "\u2211");
        initialMap.put("&MinusPlus;", "\u2213");
        initialMap.put("&mnplus;", "\u2213");
        initialMap.put("&dotplus;", "\u2214");
        initialMap.put("&Backslash;", "\u2216");
        initialMap.put("&setminus;", "\u2216");
        initialMap.put("&setmn;", "\u2216");
        initialMap.put("&smallsetminus;", "\u2216");
        initialMap.put("&SmallCircle;", "\u2218");
        initialMap.put("&Sqrt;", "\u221A");
        initialMap.put("&Proportional;", "\u221D");
        initialMap.put("&propto;", "\u221D");
        initialMap.put("&varpropto;", "\u221D");
        initialMap.put("&vprop;", "\u221D");
        initialMap.put("&angle;", "\u2220");
        initialMap.put("&angmsd;", "\u2221");
        initialMap.put("&VerticalBar;", "\u2223");
        initialMap.put("&mid;", "\u2223");
        initialMap.put("&shortmid;", "\u2223");
        initialMap.put("&NotVerticalBar;", "\u2224");
        initialMap.put("&nmid;", "\u2224");
        initialMap.put("&nshortmid;", "\u2224");
        initialMap.put("&DoubleVerticalBar;", "\u2225");
        initialMap.put("&par;", "\u2225");
        initialMap.put("&parallel;", "\u2225");
        initialMap.put("&shortparallel;", "\u2225");
        initialMap.put("&NotDoubleVerticalBar;", "\u2226");
        initialMap.put("&npar;", "\u2226");
        initialMap.put("&nparallel;", "\u2226");
        initialMap.put("&nshortparallel;", "\u2226");
        initialMap.put("&wedge;", "\u2227");
        initialMap.put("&vee;", "\u2228");
        initialMap.put("&Integral;", "\u222B");
        initialMap.put("&iiint;", "\u222D");
        initialMap.put("&ContourIntegral;", "\u222E");
        initialMap.put("&conint;", "\u222E");
        initialMap.put("&Conint;", "\u222F");
        initialMap.put("&ClockwiseContourIntegral;", "\u2232");
        initialMap.put("&CounterClockwiseContourIntegral;", "\u2233");
        initialMap.put("&Therefore;", "\u2234");
        initialMap.put("&therefore;", "\u2234");
        initialMap.put("&Because;", "\u2235");
        initialMap.put("&becaus;", "\u2235");
        initialMap.put("&Colon;", "\u2237");
        initialMap.put("&dotminus;", "\u2238");
        initialMap.put("&Tilde;", "\u223C");
        initialMap.put("&thicksim;", "\u223C");
        initialMap.put("&thksim;", "\u223C");
        initialMap.put("&backsim;", "\u223D");
        initialMap.put("&ac;", "\u223E");
        initialMap.put("&VerticalTilde;", "\u2240");
        initialMap.put("&wr;", "\u2240");
        initialMap.put("&NotTilde;", "\u2241");
        initialMap.put("&EqualTilde;", "\u2242");
        initialMap.put("&eqsim;", "\u2242");
        initialMap.put("&NotEqualTilde;", "\u2242\u0338");
        initialMap.put("&TildeEqual;", "\u2243");
        initialMap.put("&sime;", "\u2243");
        initialMap.put("&NotTildeEqual;", "\u2244");
        initialMap.put("&nsime;", "\u2244");
        initialMap.put("&TildeFullEqual;", "\u2245");
        initialMap.put("&NotTildeFullEqual;", "\u2247");
        initialMap.put("&TildeTilde;", "\u2248");
        initialMap.put("&ap;", "\u2248");
        initialMap.put("&approx;", "\u2248");
        initialMap.put("&thickapprox;", "\u2248");
        initialMap.put("&thkap;", "\u2248");
        initialMap.put("&NotTildeTilde;", "\u2249");
        initialMap.put("&nap;", "\u2249");
        initialMap.put("&ape;", "\u224A");
        initialMap.put("&backcong;", "\u224C");
        initialMap.put("&CupCap;", "\u224D");
        initialMap.put("&Bumpeq;", "\u224E");
        initialMap.put("&HumpDownHump;", "\u224E");
        initialMap.put("&NotHumpDownHump;", "\u224E\u0338");
        initialMap.put("&HumpEqual;", "\u224F");
        initialMap.put("&bumpe;", "\u224F");
        initialMap.put("&NotHumpEqual;", "\u224F\u0338");
        initialMap.put("&DotEqual;", "\u2250");
        initialMap.put("&doteq;", "\u2250");
        initialMap.put("&doteqdot;", "\u2251");
        initialMap.put("&efDot;", "\u2252");
        initialMap.put("&erDot;", "\u2253");
        initialMap.put("&Assign;", "\u2254");
        initialMap.put("&colone;", "\u2254");
        initialMap.put("&ecolon;", "\u2255");
        initialMap.put("&ecir;", "\u2256");
        initialMap.put("&circeq;", "\u2257");
        initialMap.put("&triangleq;", "\u225C");
        initialMap.put("&equest;", "\u225F");
        initialMap.put("&NotEqual;", "\u2260");
        initialMap.put("&Congruent;", "\u2261");
        initialMap.put("&NotCongruent;", "\u2262");
        initialMap.put("&leq;", "\u2264");
        initialMap.put("&GreaterEqual;", "\u2265");
        initialMap.put("&geq;", "\u2265");
        initialMap.put("&LessFullEqual;", "\u2266");
        initialMap.put("&lE;", "\u2266");
        initialMap.put("&nlE;", "\u2266\u0338");
        initialMap.put("&GreaterFullEqual;", "\u2267");
        initialMap.put("&gE;", "\u2267");
        initialMap.put("&NotGreaterFullEqual;", "\u2267\u0338");
        initialMap.put("&ngE;", "\u2267\u0338");
        initialMap.put("&lnE;", "\u2268");
        initialMap.put("&lvertneqq;", "\u2268\uFE00");
        initialMap.put("&gnE;", "\u2269");
        initialMap.put("&gvertneqq;", "\u2269\uFE00");
        initialMap.put("&Lt;", "\u226A");
        initialMap.put("&NestedLessLess;", "\u226A");
        initialMap.put("&NotLessLess;", "\u226A\u0338");
        initialMap.put("&Gt;", "\u226B");
        initialMap.put("&NestedGreaterGreater;", "\u226B");
        initialMap.put("&NotGreaterGreater;", "\u226B\u0338");
        initialMap.put("&between;", "\u226C");
        initialMap.put("&NotLess;", "\u226E");
        initialMap.put("&nless;", "\u226E");
        initialMap.put("&NotGreater;", "\u226F");
        initialMap.put("&ngt;", "\u226F");
        initialMap.put("&NotLessEqual;", "\u2270");
        initialMap.put("&nle;", "\u2270");
        initialMap.put("&NotGreaterEqual;", "\u2271");
        initialMap.put("&nge;", "\u2271");
        initialMap.put("&LessTilde;", "\u2272");
        initialMap.put("&lesssim;", "\u2272");
        initialMap.put("&GreaterTilde;", "\u2273");
        initialMap.put("&gsim;", "\u2273");
        initialMap.put("&NotLessTilde;", "\u2274");
        initialMap.put("&NotGreaterTilde;", "\u2275");
        initialMap.put("&LessGreater;", "\u2276");
        initialMap.put("&lessgtr;", "\u2276");
        initialMap.put("&GreaterLess;", "\u2277");
        initialMap.put("&gl;", "\u2277");
        initialMap.put("&NotLessGreater;", "\u2278");
        initialMap.put("&NotGreaterLess;", "\u2279");
        initialMap.put("&Precedes;", "\u227A");
        initialMap.put("&pr;", "\u227A");
        initialMap.put("&Succeeds;", "\u227B");
        initialMap.put("&sc;", "\u227B");
        initialMap.put("&PrecedesSlantEqual;", "\u227C");
        initialMap.put("&prcue;", "\u227C");
        initialMap.put("&SucceedsSlantEqual;", "\u227D");
        initialMap.put("&sccue;", "\u227D");
        initialMap.put("&PrecedesTilde;", "\u227E");
        initialMap.put("&precsim;", "\u227E");
        initialMap.put("&SucceedsTilde;", "\u227F");
        initialMap.put("&scsim;", "\u227F");
        initialMap.put("&NotPrecedes;", "\u2280");
        initialMap.put("&npr;", "\u2280");
        initialMap.put("&NotSucceeds;", "\u2281");
        initialMap.put("&nsc;", "\u2281");
        initialMap.put("&subset;", "\u2282");
        initialMap.put("&NotSubset;", "\u2282\u20D2");
        initialMap.put("&nsubset;", "\u2282\u20D2");
        initialMap.put("&Superset;", "\u2283");
        initialMap.put("&supset;", "\u2283");
        initialMap.put("&NotSuperset;", "\u2283\u20D2");
        initialMap.put("&nsupset;", "\u2283\u20D2");
        initialMap.put("&SubsetEqual;", "\u2286");
        initialMap.put("&subseteq;", "\u2286");
        initialMap.put("&SupersetEqual;", "\u2287");
        initialMap.put("&supseteq;", "\u2287");
        initialMap.put("&NotSubsetEqual;", "\u2288");
        initialMap.put("&nsube;", "\u2288");
        initialMap.put("&NotSupersetEqual;", "\u2289");
        initialMap.put("&nsupe;", "\u2289");
        initialMap.put("&subne;", "\u228A");
        initialMap.put("&varsubsetneq;", "\u228A\uFE00");
        initialMap.put("&supne;", "\u228B");
        initialMap.put("&varsupsetneq;", "\u228B\uFE00");
        initialMap.put("&UnionPlus;", "\u228E");
        initialMap.put("&SquareSubset;", "\u228F");
        initialMap.put("&sqsub;", "\u228F");
        initialMap.put("&SquareSuperset;", "\u2290");
        initialMap.put("&sqsup;", "\u2290");
        initialMap.put("&SquareSubsetEqual;", "\u2291");
        initialMap.put("&sqsube;", "\u2291");
        initialMap.put("&SquareSupersetEqual;", "\u2292");
        initialMap.put("&sqsupe;", "\u2292");
        initialMap.put("&SquareIntersection;", "\u2293");
        initialMap.put("&SquareUnion;", "\u2294");
        initialMap.put("&CirclePlus;", "\u2295");
        initialMap.put("&CircleMinus;", "\u2296");
        initialMap.put("&CircleTimes;", "\u2297");
        initialMap.put("&CircleDot;", "\u2299");
        initialMap.put("&circledcirc;", "\u229A");
        initialMap.put("&circledast;", "\u229B");
        initialMap.put("&circleddash;", "\u229D");
        initialMap.put("&boxplus;", "\u229E");
        initialMap.put("&boxminus;", "\u229F");
        initialMap.put("&boxtimes;", "\u22A0");
        initialMap.put("&dotsquare;", "\u22A1");
        initialMap.put("&RightTee;", "\u22A2");
        initialMap.put("&LeftTee;", "\u22A3");
        initialMap.put("&DownTee;", "\u22A4");
        initialMap.put("&UpTee;", "\u22A5");
        initialMap.put("&bot;", "\u22A5");
        initialMap.put("&bottom;", "\u22A5");
        initialMap.put("&DoubleRightTee;", "\u22A8");
        initialMap.put("&LeftTriangle;", "\u22B2");
        initialMap.put("&vartriangleleft;", "\u22B2");
        initialMap.put("&RightTriangle;", "\u22B3");
        initialMap.put("&vartriangleright;", "\u22B3");
        initialMap.put("&LeftTriangleEqual;", "\u22B4");
        initialMap.put("&ltrie;", "\u22B4");
        initialMap.put("&RightTriangleEqual;", "\u22B5");
        initialMap.put("&rtrie;", "\u22B5");
        initialMap.put("&multimap;", "\u22B8");
        initialMap.put("&intcal;", "\u22BA");
        initialMap.put("&Wedge;", "\u22C0");
        initialMap.put("&bigwedge;", "\u22C0");
        initialMap.put("&Vee;", "\u22C1");
        initialMap.put("&bigvee;", "\u22C1");
        initialMap.put("&Intersection;", "\u22C2");
        initialMap.put("&bigcap;", "\u22C2");
        initialMap.put("&Union;", "\u22C3");
        initialMap.put("&bigcup;", "\u22C3");
        initialMap.put("&Diamond;", "\u22C4");
        initialMap.put("&diam;", "\u22C4");
        initialMap.put("&Star;", "\u22C6");
        initialMap.put("&divideontimes;", "\u22C7");
        initialMap.put("&leftthreetimes;", "\u22CB");
        initialMap.put("&rightthreetimes;", "\u22CC");
        initialMap.put("&backsimeq;", "\u22CD");
        initialMap.put("&curlyvee;", "\u22CE");
        initialMap.put("&curlywedge;", "\u22CF");
        initialMap.put("&Sub;", "\u22D0");
        initialMap.put("&Sup;", "\u22D1");
        initialMap.put("&fork;", "\u22D4");
        initialMap.put("&lessdot;", "\u22D6");
        initialMap.put("&gtdot;", "\u22D7");
        initialMap.put("&Gg;", "\u22D9");
        initialMap.put("&LessEqualGreater;", "\u22DA");
        initialMap.put("&leg;", "\u22DA");
        initialMap.put("&GreaterEqualLess;", "\u22DB");
        initialMap.put("&gel;", "\u22DB");
        initialMap.put("&cuepr;", "\u22DE");
        initialMap.put("&cuesc;", "\u22DF");
        initialMap.put("&NotPrecedesSlantEqual;", "\u22E0");
        initialMap.put("&NotSucceedsSlantEqual;", "\u22E1");
        initialMap.put("&NotSquareSubsetEqual;", "\u22E2");
        initialMap.put("&NotSquareSupersetEqual;", "\u22E3");
        initialMap.put("&precnsim;", "\u22E8");
        initialMap.put("&scnsim;", "\u22E9");
        initialMap.put("&NotLeftTriangle;", "\u22EA");
        initialMap.put("&nltri;", "\u22EA");
        initialMap.put("&NotRightTriangle;", "\u22EB");
        initialMap.put("&nrtri;", "\u22EB");
        initialMap.put("&NotLeftTriangleEqual;", "\u22EC");
        initialMap.put("&nltrie;", "\u22EC");
        initialMap.put("&NotRightTriangleEqual;", "\u22ED");
        initialMap.put("&nrtrie;", "\u22ED");
        initialMap.put("&barwed;", "\u2305");
        initialMap.put("&Barwed;", "\u2306");
        initialMap.put("&LeftCeiling;", "\u2308");
        initialMap.put("&RightCeiling;", "\u2309");
        initialMap.put("&LeftFloor;", "\u230A");
        initialMap.put("&RightFloor;", "\u230B");
        initialMap.put("&ulcorn;", "\u231C");
        initialMap.put("&urcorn;", "\u231D");
        initialMap.put("&dlcorn;", "\u231E");
        initialMap.put("&drcorn;", "\u231F");
        initialMap.put("&frown;", "\u2322");
        initialMap.put("&smile;", "\u2323");
        initialMap.put("&lmoust;", "\u23B0");
        initialMap.put("&rmoust;", "\u23B1");
        initialMap.put("&OverBracket;", "\u23B4");
        initialMap.put("&UnderBracket;", "\u23B5");
        initialMap.put("&circledS;", "\u24C8");
        initialMap.put("&HorizontalLine;", "\u2500");
        initialMap.put("&Square;", "\u25A1");
        initialMap.put("&squ;", "\u25A1");
        initialMap.put("&FilledVerySmallSquare;", "\u25AA");
        initialMap.put("&blacksquare;", "\u25AA");
        initialMap.put("&squarf;", "\u25AA");
        initialMap.put("&bigtriangleup;", "\u25B3");
        initialMap.put("&blacktriangle;", "\u25B4");
        initialMap.put("&triangle;", "\u25B5");
        initialMap.put("&blacktriangleright;", "\u25B8");
        initialMap.put("&rtri;", "\u25B9");
        initialMap.put("&bigtriangledown;", "\u25BD");
        initialMap.put("&blacktriangledown;", "\u25BE");
        initialMap.put("&dtri;", "\u25BF");
        initialMap.put("&blacktriangleleft;", "\u25C2");
        initialMap.put("&ltri;", "\u25C3");
        initialMap.put("&lozenge;", "\u25CA");
        initialMap.put("&bigcirc;", "\u25EF");
        initialMap.put("&bigstar;", "\u2605");
        initialMap.put("&spadesuit;", "\u2660");
        initialMap.put("&clubsuit;", "\u2663");
        initialMap.put("&heartsuit;", "\u2665");
        initialMap.put("&diamondsuit;", "\u2666");
        initialMap.put("&natur;", "\u266E");
        initialMap.put("&check;", "\u2713");
        initialMap.put("&malt;", "\u2720");
        initialMap.put("&LeftDoubleBracket;", "\u27E6");
        initialMap.put("&RightDoubleBracket;", "\u27E7");
        initialMap.put("&LeftAngleBracket;", "\u27E8");
        initialMap.put("&lang;", "\u27E8");
        initialMap.put("&RightAngleBracket;", "\u27E9");
        initialMap.put("&rang;", "\u27E9");
        initialMap.put("&LongLeftArrow;", "\u27F5");
        initialMap.put("&longleftarrow;", "\u27F5");
        initialMap.put("&LongRightArrow;", "\u27F6");
        initialMap.put("&longrightarrow;", "\u27F6");
        initialMap.put("&LongLeftRightArrow;", "\u27F7");
        initialMap.put("&longleftrightarrow;", "\u27F7");
        initialMap.put("&DoubleLongLeftArrow;", "\u27F8");
        initialMap.put("&Longleftarrow;", "\u27F8");
        initialMap.put("&DoubleLongRightArrow;", "\u27F9");
        initialMap.put("&Longrightarrow;", "\u27F9");
        initialMap.put("&DoubleLongLeftRightArrow;", "\u27FA");
        initialMap.put("&Longleftrightarrow;", "\u27FA");
        initialMap.put("&longmapsto;", "\u27FC");
        initialMap.put("&bkarow;", "\u290D");
        initialMap.put("&dbkarow;", "\u290F");
        initialMap.put("&RBarr;", "\u2910");
        initialMap.put("&hksearow;", "\u2925");
        initialMap.put("&hkswarow;", "\u2926");
        initialMap.put("&nesear;", "\u2928");
        initialMap.put("&seswar;", "\u2929");
        initialMap.put("&UpEquilibrium;", "\u296E");
        initialMap.put("&ReverseUpEquilibrium;", "\u296F");
        initialMap.put("&blacklozenge;", "\u29EB");
        initialMap.put("&bigodot;", "\u2A00");
        initialMap.put("&bigoplus;", "\u2A01");
        initialMap.put("&bigotimes;", "\u2A02");
        initialMap.put("&biguplus;", "\u2A04");
        initialMap.put("&bigsqcup;", "\u2A06");
        initialMap.put("&iiiint;", "\u2A0C");
        initialMap.put("&intprod;", "\u2A3C");
        initialMap.put("&ddotseq;", "\u2A77");
        initialMap.put("&LessSlantEqual;", "\u2A7D");
        initialMap.put("&leqslant;", "\u2A7D");
        initialMap.put("&NotLessSlantEqual;", "\u2A7D\u0338");
        initialMap.put("&nleqslant;", "\u2A7D\u0338");
        initialMap.put("&GreaterSlantEqual;", "\u2A7E");
        initialMap.put("&geqslant;", "\u2A7E");
        initialMap.put("&NotGreaterSlantEqual;", "\u2A7E\u0338");
        initialMap.put("&ngeqslant;", "\u2A7E\u0338");
        initialMap.put("&lap;", "\u2A85");
        initialMap.put("&gap;", "\u2A86");
        initialMap.put("&lne;", "\u2A87");
        initialMap.put("&gne;", "\u2A88");
        initialMap.put("&lnap;", "\u2A89");
        initialMap.put("&gnap;", "\u2A8A");
        initialMap.put("&lEg;", "\u2A8B");
        initialMap.put("&gEl;", "\u2A8C");
        initialMap.put("&els;", "\u2A95");
        initialMap.put("&egs;", "\u2A96");
        initialMap.put("&PrecedesEqual;", "\u2AAF");
        initialMap.put("&pre;", "\u2AAF");
        initialMap.put("&NotPrecedesEqual;", "\u2AAF\u0338");
        initialMap.put("&npre;", "\u2AAF\u0338");
        initialMap.put("&SucceedsEqual;", "\u2AB0");
        initialMap.put("&sce;", "\u2AB0");
        initialMap.put("&NotSucceedsEqual;", "\u2AB0\u0338");
        initialMap.put("&nsce;", "\u2AB0\u0338");
        initialMap.put("&precneqq;", "\u2AB5");
        initialMap.put("&scnE;", "\u2AB6");
        initialMap.put("&prap;", "\u2AB7");
        initialMap.put("&scap;", "\u2AB8");
        initialMap.put("&precnapprox;", "\u2AB9");
        initialMap.put("&scnap;", "\u2ABA");
        initialMap.put("&subE;", "\u2AC5");
        initialMap.put("&nsubE;", "\u2AC5\u0338");
        initialMap.put("&supE;", "\u2AC6");
        initialMap.put("&nsupE;", "\u2AC6\u0338");
        initialMap.put("&subnE;", "\u2ACB");
        initialMap.put("&varsubsetneqq;", "\u2ACB\uFE00");
        initialMap.put("&supnE;", "\u2ACC");
        initialMap.put("&varsupsetneqq;", "\u2ACC\uFE00");
        initialMap.put("&Dashv;", "\u2AE4");
        HTML50_EXTENDED_UNESCAPE = Collections.unmodifiableMap(initialMap);
    }

    /**
     * A Map&lt;CharSequence, CharSequence&gt; to unescape the character
     * entities without semicolon (&amp;amp, &amp;quot, &amp;nsbp...).
     *
     * Note that these character entities are not part of the HTML 5.0 standard
     * but are nonetheless understood by virtually every modern browsers.
     */
    public static final Map<CharSequence, CharSequence> NO_SEMICOLON_UNESCAPE;

    static {
        final Map<CharSequence, CharSequence> initialMap = new HashMap<>();
        initialMap.put("&QUOT", "\u005C\u0022");
        initialMap.put("&quot", "\u005C\u0022");
        initialMap.put("&AMP", "\u0026");
        initialMap.put("&amp", "\u0026");
        initialMap.put("&LT", "\u003C");
        initialMap.put("&lt", "\u003C");
        initialMap.put("&GT", "\u003E");
        initialMap.put("&gt", "\u003E");
        initialMap.put("&nbsp", "\u00A0");
        initialMap.put("&iexcl", "\u00A1");
        initialMap.put("&cent", "\u00A2");
        initialMap.put("&pound", "\u00A3");
        initialMap.put("&curren", "\u00A4");
        initialMap.put("&yen", "\u00A5");
        initialMap.put("&brvbar", "\u00A6");
        initialMap.put("&sect", "\u00A7");
        initialMap.put("&uml", "\u00A8");
        initialMap.put("&COPY", "\u00A9");
        initialMap.put("&copy", "\u00A9");
        initialMap.put("&ordf", "\u00AA");
        initialMap.put("&laquo", "\u00AB");
        initialMap.put("&not", "\u00AC");
        initialMap.put("&shy", "\u00AD");
        initialMap.put("&REG", "\u00AE");
        initialMap.put("&reg", "\u00AE");
        initialMap.put("&macr", "\u00AF");
        initialMap.put("&deg", "\u00B0");
        initialMap.put("&plusmn", "\u00B1");
        initialMap.put("&sup2", "\u00B2");
        initialMap.put("&sup3", "\u00B3");
        initialMap.put("&acute", "\u00B4");
        initialMap.put("&micro", "\u00B5");
        initialMap.put("&para", "\u00B6");
        initialMap.put("&middot", "\u00B7");
        initialMap.put("&cedil", "\u00B8");
        initialMap.put("&sup1", "\u00B9");
        initialMap.put("&ordm", "\u00BA");
        initialMap.put("&raquo", "\u00BB");
        initialMap.put("&frac14", "\u00BC");
        initialMap.put("&frac12", "\u00BD");
        initialMap.put("&frac34", "\u00BE");
        initialMap.put("&iquest", "\u00BF");
        initialMap.put("&Agrave", "\u00C0");
        initialMap.put("&Aacute", "\u00C1");
        initialMap.put("&Acirc", "\u00C2");
        initialMap.put("&Atilde", "\u00C3");
        initialMap.put("&Auml", "\u00C4");
        initialMap.put("&Aring", "\u00C5");
        initialMap.put("&AElig", "\u00C6");
        initialMap.put("&Ccedil", "\u00C7");
        initialMap.put("&Egrave", "\u00C8");
        initialMap.put("&Eacute", "\u00C9");
        initialMap.put("&Ecirc", "\u00CA");
        initialMap.put("&Euml", "\u00CB");
        initialMap.put("&Igrave", "\u00CC");
        initialMap.put("&Iacute", "\u00CD");
        initialMap.put("&Icirc", "\u00CE");
        initialMap.put("&Iuml", "\u00CF");
        initialMap.put("&ETH", "\u00D0");
        initialMap.put("&Ntilde", "\u00D1");
        initialMap.put("&Ograve", "\u00D2");
        initialMap.put("&Oacute", "\u00D3");
        initialMap.put("&Ocirc", "\u00D4");
        initialMap.put("&Otilde", "\u00D5");
        initialMap.put("&Ouml", "\u00D6");
        initialMap.put("&times", "\u00D7");
        initialMap.put("&Oslash", "\u00D8");
        initialMap.put("&Ugrave", "\u00D9");
        initialMap.put("&Uacute", "\u00DA");
        initialMap.put("&Ucirc", "\u00DB");
        initialMap.put("&Uuml", "\u00DC");
        initialMap.put("&Yacute", "\u00DD");
        initialMap.put("&THORN", "\u00DE");
        initialMap.put("&szlig", "\u00DF");
        initialMap.put("&agrave", "\u00E0");
        initialMap.put("&aacute", "\u00E1");
        initialMap.put("&acirc", "\u00E2");
        initialMap.put("&atilde", "\u00E3");
        initialMap.put("&auml", "\u00E4");
        initialMap.put("&aring", "\u00E5");
        initialMap.put("&aelig", "\u00E6");
        initialMap.put("&ccedil", "\u00E7");
        initialMap.put("&egrave", "\u00E8");
        initialMap.put("&eacute", "\u00E9");
        initialMap.put("&ecirc", "\u00EA");
        initialMap.put("&euml", "\u00EB");
        initialMap.put("&igrave", "\u00EC");
        initialMap.put("&iacute", "\u00ED");
        initialMap.put("&icirc", "\u00EE");
        initialMap.put("&iuml", "\u00EF");
        initialMap.put("&eth", "\u00F0");
        initialMap.put("&ntilde", "\u00F1");
        initialMap.put("&ograve", "\u00F2");
        initialMap.put("&oacute", "\u00F3");
        initialMap.put("&ocirc", "\u00F4");
        initialMap.put("&otilde", "\u00F5");
        initialMap.put("&ouml", "\u00F6");
        initialMap.put("&divide", "\u00F7");
        initialMap.put("&oslash", "\u00F8");
        initialMap.put("&ugrave", "\u00F9");
        initialMap.put("&uacute", "\u00FA");
        initialMap.put("&ucirc", "\u00FB");
        initialMap.put("&uuml", "\u00FC");
        initialMap.put("&yacute", "\u00FD");
        initialMap.put("&thorn", "\u00FE");
        initialMap.put("&yuml", "\u00FF");
        NO_SEMICOLON_UNESCAPE = Collections.unmodifiableMap(initialMap);
    }

    /**
     * A Map&lt;CharSequence, CharSequence&gt; to escape the basic XML and HTML
     * character entities.
     *
     * Namely: {@code " & < >}
     */
    public static final Map<CharSequence, CharSequence> BASIC_ESCAPE;

    static {
        final Map<CharSequence, CharSequence> initialMap = new HashMap<>();
        initialMap.put("\"", "&quot;"); // " - double-quote
        initialMap.put("&", "&amp;");   // & - ampersand
        initialMap.put("<", "&lt;");    // < - less-than
        initialMap.put(">", "&gt;");    // > - greater-than
        BASIC_ESCAPE = Collections.unmodifiableMap(initialMap);
    }

    /**
     * Reverse of {@link #BASIC_ESCAPE} for unescaping purposes.
     */
    public static final Map<CharSequence, CharSequence> BASIC_UNESCAPE;

    static {
        BASIC_UNESCAPE = Collections.unmodifiableMap(invert(BASIC_ESCAPE));
    }

    /**
     * A Map&lt;CharSequence, CharSequence&gt; to escape the apostrophe character to
     * its XML character entity.
     */
    public static final Map<CharSequence, CharSequence> APOS_ESCAPE;

    static {
        final Map<CharSequence, CharSequence> initialMap = new HashMap<>();
        initialMap.put("'", "&apos;"); // XML apostrophe
        APOS_ESCAPE = Collections.unmodifiableMap(initialMap);
    }

    /**
     * Reverse of {@link #APOS_ESCAPE} for unescaping purposes.
     */
    public static final Map<CharSequence, CharSequence> APOS_UNESCAPE;

    static {
        APOS_UNESCAPE = Collections.unmodifiableMap(invert(APOS_ESCAPE));
    }

    /**
     * A Map&lt;CharSequence, CharSequence&gt; to escape the Java
     * control characters.
     *
     * Namely: {@code \b \n \t \f \r}
     */
    public static final Map<CharSequence, CharSequence> JAVA_CTRL_CHARS_ESCAPE;

    static {
        final Map<CharSequence, CharSequence> initialMap = new HashMap<>();
        initialMap.put("\b", "\\b");
        initialMap.put("\n", "\\n");
        initialMap.put("\t", "\\t");
        initialMap.put("\f", "\\f");
        initialMap.put("\r", "\\r");
        JAVA_CTRL_CHARS_ESCAPE = Collections.unmodifiableMap(initialMap);
    }

    /**
     * Reverse of {@link #JAVA_CTRL_CHARS_ESCAPE} for unescaping purposes.
     */
    public static final Map<CharSequence, CharSequence> JAVA_CTRL_CHARS_UNESCAPE;

    static {
        JAVA_CTRL_CHARS_UNESCAPE = Collections.unmodifiableMap(invert(JAVA_CTRL_CHARS_ESCAPE));
    }

    /**
     * Inverts an escape Map into an unescape Map.
     *
     * @param map Map&lt;String, String&gt; to be inverted
     * @return Map&lt;String, String&gt; inverted array
     */
    public static Map<CharSequence, CharSequence> invert(final Map<CharSequence, CharSequence> map) {
        final Map<CharSequence, CharSequence> newMap = new HashMap<>();
        for (final Map.Entry<CharSequence, CharSequence> pair : map.entrySet()) {
            newMap.put(pair.getValue(), pair.getKey());
        }
        return newMap;
    }

}
