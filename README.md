Introduction
============
<img src="https://img.shields.io/badge/Ask%20me-anything-1abc9c.svg" alt="ask me"> &nbsp; <img src="https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot" alt="spring boot" width="100"/>


This project is a starter project for creating a rest api server with
jwt based security. It has the following functions.

1.  Allow registration with a basic auth, backed with a postgres
    database. Since JPA is being used you can replace the database with
    other ones.

2.  Allow registered users login and receive a JWT token.

3.  After receiving the token, users can access secured endpoint using
    the token.

This kind of scenario is very common, so this starter will be a good
starting point for such applications. When the necessity arises we can
remove the basic auth and jwt minting from our application and configure
an external authorization server.

Build
=====

First of all we need to generate a key pair. There are may ways to do
this. One example with openssl is as follows.

```shell
    # create rsa key pair
    openssl genrsa -out keypair.pem 2048
    # extract public key
    openssl rsa -in keypair.pem -pubout -out public.pem
    # create private key in PKCS\#8 format
    openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem
```

Then place the `public.pem` and `private.pem` files under the directory
`src\main\resources\certs`.

After setting this up the application can be run as a typical spring
boot project.If you want to run the application without using
testcontainer for local development, spin up a postgres server on your
localhost and configure url, password and username on application.yaml.

Remember that you need java 21 for this project. If you are not using
[sdkman](https://sdkman.io), check it out it is a good tool.

```shell
    ./gradlew bootRun
```

A nice addition in this project is the usage of Testcontainers for local
development. There is a TestApplication class in the test package. You
can use it for local development.