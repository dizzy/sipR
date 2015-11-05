package org.sipr.core.service.impl;

import org.sipr.core.domain.AuthDetails;

public class DigestAuthDetails implements AuthDetails {
    String username;
    String realm;
    String uri;
    String algorithm;
    String nonce;
    String cnonce;
    String response;
    String method;

    public DigestAuthDetails(String username, String realm, String uri, String algorithm, String nonce, String cnonce, String response, String method) {
        this.username = username;
        this.realm = realm;
        this.uri = uri;
        this.algorithm = algorithm;
        this.nonce = nonce;
        this.cnonce = cnonce;
        this.response = response;
        this.method = method;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getUsername() {
        return username;
    }

    public String getRealm() {
        return realm;
    }

    public String getUri() {
        return uri;
    }

    public String getNonce() {
        return nonce;
    }

    public String getCnonce() {
        return cnonce;
    }

    public String getResponse() {
        return response;
    }

    public String getMethod() {
        return method;
    }
}
