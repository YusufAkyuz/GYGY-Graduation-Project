package com.telcox.common.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TcknValidatorTest {

    @Test
    void validTcknPassesChecksum() {
        assertThat(TcknValidator.isValid("10000000146")).isTrue();
    }

    @Test
    void rejectsWrongLength() {
        assertThat(TcknValidator.isValid("123")).isFalse();
    }

    @Test
    void rejectsLeadingZero() {
        assertThat(TcknValidator.isValid("01234567890")).isFalse();
    }

    @Test
    void rejectsBadChecksum() {
        assertThat(TcknValidator.isValid("11111111111")).isFalse();
    }

    @Test
    void rejectsNonNumeric() {
        assertThat(TcknValidator.isValid("abcdefghijk")).isFalse();
    }
}
