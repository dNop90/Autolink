# Autolink

## Description
An application that enables users to search an inventory of vehicles provided by dealers. Users can filter their search based on essential criteria, including make, model, year, and specific features. Once a preferred vehicle has been identified, users can directly communicate with the dealer to negotiate terms and finalize the transaction. Furthermore, the application offers an option for users to apply for dealer status, pending approval by an administrator. Upon authorization, dealers can efficiently upload and manage their vehicle inventory, ensuring users can access the most current listings.

## Technology
- Server
    - Java
    - Spring Boot
    - AWS RDS
    - Apache Kafka
    - JUnit
    - Mockito
    - Javascript Web Token (JWT)
    - Web Socket Server

- Client
    - React
    - HTML, CSS, and Javascript
    - TypeScript
    - Jest
    - Bootstrap
    - Web Socket Client

## Features
- User
    - Users and dealers should be able to create and log in to accounts.
    - User accounts should have an associated profile where the user can set things like: display name, profile picture, about me.
    - A logged-in user should be able to look up dealers and see their inventory and also be able to search and filter for specific vehicles they are looking for.
    - Users can message dealers in real-time.
    - Users can Leave reviews for dealers or vehicles.
    - Users can make an appointment with dealers.

- Dealer
    - Dealers will require some approval for their account
    - A dealer should be able to manage vehicles in their inventory
    - Dealers can reply to users in real-time

- Staff/Admin
    - Administrators can set another account to be an admin account
    - Administrators can approve a dealer’s registration
    - Administrators should be able to freeze/suspend other accounts
 

## Installation
### Server
#### Requirement
- [Java](https://www.java.com/en/)

#### Running the server
- Download the latest server file from our [release](https://github.com/dNop90/Autolink/releases).
- Unpack the zip and put the `.jar` file in a folder of your choice.
- Create a folder with the name `config` in the same folder as your `.jar` file.
- Create your own `application.properties` file or you can take a look at our [example](https://github.com/dNop90/Autolink/blob/main/server/src/main/resources/application.properties).
    - You must have your database configuration in the `application.properties`.
- Run your server using `java -jar Autolink_Server.jar`.

### Client
#### Requirement
- [Nodejs](https://nodejs.org/en/download)

#### Running the client
- Download the latest client files from our [release](https://github.com/dNop90/Autolink/releases).
- Unpack the zip and extract the `build` folder.
- Install `serve` using `npm install -g serve`.
- Go in the folder where your `build` folder is located, and then run it using `serve -s build`. Your console will show you the port and the IP.
