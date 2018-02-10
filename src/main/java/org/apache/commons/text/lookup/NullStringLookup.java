package org.apache.commons.text.lookup;

/**
 * Always returns null.
 * 
 * @since 1.3
 */
public final class NullStringLookup extends AbstractStringLookup {

    /**
     * Defines the singleton for this class.
     */
    public static final NullStringLookup INSTANCE = new NullStringLookup();

    /**
     * No need to build instances for now.
     */
    private NullStringLookup() {
        // empty
    }

    /**
     * Always returns null.
     */
    @Override
    public String lookup(final String key) {
        return null;
    }

}
