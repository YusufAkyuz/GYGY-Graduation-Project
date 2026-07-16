package com.telcox.common.security.pii;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "telcox.security")
public class PiiEncryptionProperties {

    /** Base64 encoded 256-bit AES anahtarı. MVP'de config-repo'da; Faz 8'de K8s Secret/Vault'a taşınır. */
    private String piiEncryptionKey;

    public String getPiiEncryptionKey() {
        return piiEncryptionKey;
    }

    public void setPiiEncryptionKey(String piiEncryptionKey) {
        this.piiEncryptionKey = piiEncryptionKey;
    }
}
