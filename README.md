# Oauth2.0-Example

### Configuration

In order to test the project you should connect to your google console : https://code.google.com/apis/console/.
Then activate the OAuth2 API and put the correspanding values to the folowing attributes in the OAuthProperties Class:
  - clientId
  - clientSecret

You'll have also to set the redirect URL to the callback Servelet like this : http://localhost:8080/%applicationName%/oauthCallBack

### How does it work

After you deploy the project navigate to :

http://localhost:8080/%applicationName%/userinfo

For the identification to complete you should use an UCA email addresse, if you want to use a normal google email then edit the OauthCallBack Class (remplirSession Function) to your liking.

To disconnect navigate to :

http://localhost:8080/%applicationName%/deco
