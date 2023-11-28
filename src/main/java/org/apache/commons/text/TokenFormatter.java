package org.apache.commons.text;

public interface TokenFormatter {
    String format(char[] prior, int tokenIndex, char[] token);
}
