<a name="readme-top"></a>

[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]



<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/ISST-2023/identiDAT">
    <img src="https://i.imgur.com/0JZNpkc.png" alt="Logo" width="" height="80">
  </a>
</div>


<br><br>

La aplicación está preparada para funcionar mediante uno o varios ficheros de configuración `.yml`. Como mínimo, el fichero debe contar con las siguientes opciones para el correcto funcionamiento de todas las funcionalidades:
```yml
spring:
    profiles:
        active: <Profile name>

logging:
    file:
        name: logs/identidat.log

server:
    port: 3000
    forward-headers-strategy: native

spring:
    jpa:
        hibernate:
            ddl-auto: update
    datasource:
        url: jdbc:mysql://${MYSQL_HOST:localhost}:<Port>/<Database name>
        username: <Username>
        password: <Password>

    session:
        jdbc:
            flush-mode: on-save
            table-name: SPRING_SESSION
            initialize-schema: always
    
    security:
        oauth2:
            client:
                registration:
                    ssodat:
                        client-id: <ClientId>
                        authorization-grant-type: authorization_code
                        scope: openid

                provider:
                    ssodat:
                        issuer-uri: <https://sso.example.com/realms/exampleRealm>
                        user-name-attribute: preferred_username
        
            resourceserver:
                jwt:
                    issuer-uri: <https://sso.example.com/realms/exampleRealm>
```

Es posible generar diferentes ficheros de configuración con el nombre `application-<Profile name>.yml` y aplicar sus parámetros manteniendo únicamente la opción `spring.profiles.name=<Profile name>` en el fichero principal `application.yml`.







<!-- MARKDOWN LINKS & IMAGES -->
[contributors-shield]: https://img.shields.io/github/contributors/ISST-2023/identiDAT.svg?style=for-the-badge
[contributors-url]: https://github.com/ISST-2023/identiDAT/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/ISST-2023/identiDAT.svg?style=for-the-badge
[forks-url]: https://github.com/ISST-2023/identiDAT/network/members
[stars-shield]: https://img.shields.io/github/stars/ISST-2023/identiDAT.svg?style=for-the-badge
[stars-url]: https://github.com/ISST-2023/identiDAT/stargazers
[issues-shield]: https://img.shields.io/github/issues/ISST-2023/identiDAT.svg?style=for-the-badge
[issues-url]: https://github.com/ISST-2023/identiDAT/issues
[license-shield]: https://img.shields.io/github/license/ISST-2023/identiDAT.svg?style=for-the-badge
[license-url]: https://github.com/ISST-2023/identiDAT/blob/master/LICENSE.txt
