package com.telcox.common.util;

/**
 * TC Kimlik Numarası (TCKN) resmi checksum algoritmasıyla doğrulama (FR-01).
 */
public final class TcknValidator {

    private TcknValidator() {
    }

    public static boolean isValid(String tckn) {
        if (tckn == null || !tckn.matches("\\d{11}") || tckn.charAt(0) == '0') {
            return false;
        }

        int[] digits = tckn.chars().map(c -> c - '0').toArray();

        int oddSum = digits[0] + digits[2] + digits[4] + digits[6] + digits[8];
        int evenSum = digits[1] + digits[3] + digits[5] + digits[7];
        int digit10 = ((oddSum * 7) - evenSum) % 10;
        if (digit10 < 0) {
            digit10 += 10;
        }
        if (digit10 != digits[9]) {
            return false;
        }

        int sumFirst10 = 0;
        for (int i = 0; i < 10; i++) {
            sumFirst10 += digits[i];
        }
        int digit11 = sumFirst10 % 10;
        return digit11 == digits[10];
    }
}
