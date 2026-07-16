package com.telcox.apigateway.config;

/**
 * JWT doğrulamasını atlayan bir kural: path her zaman kontrol edilir, method belirtilmişse
 * (ör. "POST") sadece o method için geçerlidir; null/boş ise tüm method'lar için geçerlidir.
 */
public class PublicPathRule {

    private String pattern;
    private String method;

    public PublicPathRule() {
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
