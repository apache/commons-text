package org.apache.commons.text.io;

import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.TextStringBuilder;
import org.apache.commons.text.matcher.StringMatcher;

class SubstitutorHandler {

    private final StringSubstitutor stringSubstitutor;
    private final TextStringBuilder buffer;
    private final StringMatcher prefixEscapeMatcher;
    private int toDrain;

    public SubstitutorHandler(StringSubstitutor stringSubstitutor, TextStringBuilder buffer,
                              StringMatcher prefixEscapeMatcher) {
        this.stringSubstitutor = new StringSubstitutor(stringSubstitutor);
        this.buffer = buffer;
        this.prefixEscapeMatcher = prefixEscapeMatcher;
    }

    public void substituteAndDrain() {
        stringSubstitutor.replaceIn(buffer);
        toDrain = buffer.size();
        drain(toDrain);
    }

    public boolean isDraining() {
        return toDrain > 0;
    }

    public StringMatcher getPrefixMatcher() {
        return stringSubstitutor.getVariablePrefixMatcher();
    }

    public StringMatcher getSuffixMatcher() {
        return stringSubstitutor.getVariableSuffixMatcher();
    }

    public void setToDrain(int value) {
        toDrain = value;
    }

    public int getToDrain() {
        return toDrain;
    }

    private void drain(int drainCount) {
        int actualLen = Math.min(buffer.length(), drainCount);
        buffer.drainChars(0, actualLen, new char[0], 0);
        toDrain -= actualLen;
        if (buffer.isEmpty() || toDrain == 0) {
            toDrain = 0;
        }
    }
}
