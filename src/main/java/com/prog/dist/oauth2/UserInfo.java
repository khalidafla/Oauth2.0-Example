/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prog.dist.oauth2;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Khalid
 */
@WebServlet(name = "UserInfo", urlPatterns = {"/userinfo"})
public class UserInfo extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if(request.getParameter("error") != null && !request.getParameter("error").equals("")){
            response.setContentType("text/plain");
            response.getWriter().append("Email invalide veuilez vous connecter avec un email de UCA\n");
            return;
        }

        if ((String) request.getSession().getAttribute("info") == null) {
            // redirection
            OAuthProperties oauthProperties = new OAuthProperties();
            response.sendRedirect(new GoogleAuthorizationCodeRequestUrl(oauthProperties.getClientId(),
                    OauthCallBack.getOAuthCodeCallbackHandlerUrl(request), oauthProperties
                    .getScopes()).build());
        } else {
            // affichage des informations
            // Printing the user's task lists titles in the response
            response.setContentType("text/plain");
            response.getWriter().append(request.getSession().getAttribute("info") + "\n");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
