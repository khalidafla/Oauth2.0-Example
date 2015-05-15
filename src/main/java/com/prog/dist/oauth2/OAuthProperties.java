/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prog.dist.oauth2;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Khalid
 */
public class OAuthProperties {

    /**
     * The OAuth 2.0 Client ID
     */
    private final String clientId = "";

    /**
     * The OAuth 2.0 Client Secret
     */
    private final String clientSecret = "";

    /**
     * The Google APIs scopes to access
     */
    private final List<String> scopes = new ArrayList<>();

    /**
     * @return the clientId
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * @return the clientSecret
     */
    public String getClientSecret() {
        return clientSecret;
    }

    
    public List<String> getScopes() {
        scopes.add("https://www.googleapis.com/auth/userinfo.email");
        scopes.add("https://www.googleapis.com/auth/userinfo.profile");
        return scopes;
    }

}
