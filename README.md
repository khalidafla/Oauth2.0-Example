# Oauth2.0-Example

### Configuration

In order to test the project you should add your Google Console cridentiels in the OAuthProperties class.

### How does it work

After you deploy the project navigate to :

http://localhost:8080/oauth2/userinfo

For the identification to complete you should use an UCA email addresse, if you want to use a normal google email then edit the OauthCallBack Class (remplirSession Function) to your liking.

To disconnect navigate to :

http://localhost:8080/oauth2/deco
