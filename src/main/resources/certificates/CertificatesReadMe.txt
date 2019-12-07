https://www.baeldung.com/x-509-authentication-in-spring-security

I defined all data in MakeFile

For creating a new keystore with a certificate authority, we can run make as follows:

$> make create-keystore PASSWORD=changeit
Now, we will add a certificate for our development host to this created keystore and sign it by our certificate authority:

$> make add-host HOSTNAME=localhost
To allow client authentication, we also need a keystore called “truststore”. This truststore has to contain valid certificates of our certificate authority and all of the allowed clients. For reference on using keytool, please look into the Makefile at the following given sections:

$> make create-truststore PASSWORD=changeit
$> make add-client CLIENTNAME=cid

-------------------------------------------------------------------------------
BROWSER:
-------
An exemplary installation of our certificate authority for Mozilla Firefox would look like follows:

Type about:preferences in the address bar
Open Advanced -> Certificates -> View Certificates -> Authorities
Click on Import
Locate the Baeldung tutorials folder and its subfolder spring-security-x509/keystore
Select the ca.crt file and click OK
Choose “Trust this CA to identify websites” and click OK
