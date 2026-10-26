package org.springframework.samples.petclinic.config;

import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;

/**
 * Reads application secrets from Azure Key Vault.
 *
 * DefaultAzureCredential uses the VM/VMSS managed identity when the
 * application runs in Azure, and can use Azure CLI credentials locally.
 */
public class KeyVaultConfig {

    private static final String VAULT_URL =
            "https://kv-middle-prod.vault.azure.net/";
    private static final String MANAGED_IDENTITY_CLIENT_ID =
            "94e40db5-1617-415f-9efa-8bc6e17ccc1b";

    private final SecretClient secretClient;

    public KeyVaultConfig() {
        DefaultAzureCredential credential =
                new DefaultAzureCredentialBuilder()
                        .managedIdentityClientId(MANAGED_IDENTITY_CLIENT_ID)
                        .build();

        this.secretClient = new SecretClientBuilder()
                .vaultUrl(VAULT_URL)
                .credential(credential)
                .buildClient();
    }

    public String getSecret(String name) {
        return secretClient.getSecret(name).getValue();
    }

    public String getDbHost() {
        return getSecret("DB-HOST");
    }

    public String getDbName() {
        return getSecret("DB-NAME");
    }

    public String getDbUsername() {
        return getSecret("DB-USERNAME");
    }

    public String getDbPassword() {
        return getSecret("DB-PASSWORD");
    }

    public String getJdbcUrl() {
        return "jdbc:mysql://" + getDbHost()
                + ":3306/" + getDbName()
                + "?useUnicode=true"
                + "&characterEncoding=UTF-8"
                + "&useSSL=true"
                + "&serverTimezone=UTC";
    }
}
