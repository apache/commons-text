package org.apache.commons.text.similarity;

/**
 * PPCH-framework test addition: a deliberately over-complex method used only
 * to exercise a live cognitive-complexity-guard test on this fork. Not
 * intended for upstream apache/commons-text.
 */
public final class ComplexityTestUtil {

    private ComplexityTestUtil() {
    }

    public static String classifyTransaction(int amount, String currency, boolean isInternational,
            boolean isFlagged, int riskScore, String region, boolean isNewCustomer, int previousFailures) {
        String result;
        if (amount <= 0) {
            result = "invalid";
        } else {
            if (isFlagged) {
                if (riskScore > 80) {
                    result = "blocked";
                } else if (riskScore > 50) {
                    if (isNewCustomer) {
                        result = "manual_review";
                    } else {
                        if (previousFailures > 2) {
                            result = "manual_review";
                        } else {
                            result = "approved_with_delay";
                        }
                    }
                } else {
                    result = "approved";
                }
            } else {
                if (isInternational) {
                    if (region.equals("HIGH_RISK")) {
                        if (amount > 10000) {
                            result = "blocked";
                        } else if (amount > 5000) {
                            if (isNewCustomer && previousFailures > 0) {
                                result = "manual_review";
                            } else {
                                result = "approved_with_delay";
                            }
                        } else {
                            result = "approved";
                        }
                    } else if (region.equals("MEDIUM_RISK")) {
                        if (amount > 20000) {
                            result = "manual_review";
                        } else {
                            result = "approved";
                        }
                    } else {
                        result = "approved";
                    }
                } else {
                    if (currency.equals("USD") || currency.equals("EUR")) {
                        if (amount > 50000) {
                            result = "manual_review";
                            for (int i = 0; i < previousFailures; i++) {
                                if (i > 3) {
                                    result = "blocked";
                                    break;
                                }
                            }
                        } else {
                            result = "approved";
                        }
                    } else {
                        if (isNewCustomer) {
                            result = "manual_review";
                        } else {
                            result = "approved";
                        }
                    }
                }
            }
        }
        return result;
    }

    public static int scoreCustomer(int age, int accountAgeMonths, int loginCount, boolean hasVerifiedEmail,
            boolean hasVerifiedPhone, int failedLogins, String tier) {
        int score = 0;
        if (age < 18) {
            score -= 50;
        } else if (age < 25) {
            score -= 10;
        } else {
            score += 5;
        }

        if (accountAgeMonths > 24) {
            score += 20;
        } else if (accountAgeMonths > 12) {
            score += 10;
        } else if (accountAgeMonths > 1) {
            if (loginCount > 50) {
                score += 5;
            } else {
                score -= 5;
            }
        } else {
            score -= 15;
        }

        if (hasVerifiedEmail && hasVerifiedPhone) {
            score += 15;
        } else if (hasVerifiedEmail || hasVerifiedPhone) {
            score += 5;
        } else {
            score -= 20;
        }

        if (failedLogins > 10) {
            score -= 30;
        } else if (failedLogins > 5) {
            score -= 15;
        } else if (failedLogins > 2) {
            score -= 5;
        }

        if ("PLATINUM".equals(tier)) {
            score += 25;
        } else if ("GOLD".equals(tier)) {
            score += 15;
        } else if ("SILVER".equals(tier)) {
            score += 5;
        }

        return score;
    }
}