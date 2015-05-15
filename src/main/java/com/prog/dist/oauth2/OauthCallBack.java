/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prog.dist.oauth2;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.Oauth2.Userinfo;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Khalid
 */
@WebServlet(name = "OauthCallBack", urlPatterns = {"/oauthCallBack"})
public class OauthCallBack extends HttpServlet {

    public static final String APPLICATION_NAME = "Test application";
    /**
     * The name of the Oauth code URL parameter
     */
    public static final String CODE_URL_PARAM_NAME = "code";

    /**
     * The name of the OAuth error URL parameter
     */
    public static final String ERROR_URL_PARAM_NAME = "error";

    /**
     * The URL suffix of the servlet
     */
    public static final String URL_MAPPING = "/oauthCallBack";

    /**
     * The URL to redirect the user to after handling the callback. Consider
     * saving this in a cookie before redirecting users to the Google
     * authorization URL if you have multiple possible URL to redirect people
     * to.
     */
    public static final String REDIRECT_URL = "/userinfo";

    public String codeError = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Getting the "error" URL parameter
        String[] error = request.getParameterValues(ERROR_URL_PARAM_NAME);

        // Checking if there was an error such as the user denied access
        if (error != null && error.length > 0) {
            response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "There was an error: \"" + error[0] + "\".");
            return;
        }
        // Getting the "code" URL parameter
        String[] code = request.getParameterValues(CODE_URL_PARAM_NAME);

        // Checking conditions on the "code" URL parameter
        if (code == null || code.length == 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "The \"code\" URL parameter is missing");
            return;
        }

        // Construct incoming request URL
        String requestUrl = getOAuthCodeCallbackHandlerUrl(request);

        // Exchange the code for OAuth tokens
        GoogleTokenResponse accessTokenResponse = exchangeCodeForAccessAndRefreshTokens(code[0],
                requestUrl);

        // remplir la session
        remplirSession(request, accessTokenResponse);

        if (codeError != null) {
            response.sendRedirect(request.getContextPath() + REDIRECT_URL + "?error=" + codeError);
        } else {
            response.sendRedirect(request.getContextPath() + REDIRECT_URL);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    /**
     * Construct the OAuth code callback handler URL.
     *
     * @param req the HttpRequest object
     * @return The constructed request's URL
     */
    public static String getOAuthCodeCallbackHandlerUrl(HttpServletRequest req) {
        String scheme = req.getScheme() + "://";
        String serverName = req.getServerName();
        String serverPort = (req.getServerPort() == 80) ? "" : ":" + req.getServerPort();
        String contextPath = req.getContextPath();
        String servletPath = URL_MAPPING;
        String pathInfo = (req.getPathInfo() == null) ? "" : req.getPathInfo();
        return scheme + serverName + serverPort + contextPath + servletPath + pathInfo;
    }

    /**
     * Exchanges the given code for an exchange and a refresh token.
     *
     * @param code The code gotten back from the authorization service
     * @param currentUrl The URL of the callback
     * @param oauthProperties The object containing the OAuth configuration
     * @return The object containing both an access and refresh token
     * @throws IOException
     */
    public GoogleTokenResponse exchangeCodeForAccessAndRefreshTokens(String code, String currentUrl)
            throws IOException {

        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();

        // Loading the oauth config file
        OAuthProperties oauthProperties = new OAuthProperties();

        return new GoogleAuthorizationCodeTokenRequest(httpTransport, jsonFactory, oauthProperties
                .getClientId(), oauthProperties.getClientSecret(), code, currentUrl).execute();
    }

    public void remplirSession(HttpServletRequest request, GoogleTokenResponse googleTokenResponse) {
        // Initializing the Tasks service
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        OAuthProperties oauthProperties = new OAuthProperties();

        GoogleCredential googleCredential = new GoogleCredential().setAccessToken(googleTokenResponse.getAccessToken());
        googleCredential.setRefreshToken(googleTokenResponse.getRefreshToken());

        // set up global Oauth2 instance
        Oauth2 oauth2 = new Oauth2.Builder(transport, jsonFactory, googleCredential).setApplicationName(
                APPLICATION_NAME).build();

        Userinfo userInfo = oauth2.userinfo();
        try {
            String email = userInfo.get().execute().getEmail();
            String emailDomain = email.split("@")[1];
            if (!emailDomain.equals("edu.uca.ma")) {
                codeError = "1"; // email invalide
            } else {
                codeError = null;
                request.getSession().setAttribute("info", userInfo.get().execute().toPrettyString());
            }
        } catch (IOException ex) {
            Logger.getLogger(OauthCallBack.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
