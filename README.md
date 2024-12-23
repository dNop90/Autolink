# Autolink

## Description
An application that simulates users to view vehicles from dealers, and dealers update their inventory and communicate with users.

## Technology
- Server
    - Java
    - Spring Boot
    - AWS RDS
    - Apache Kafka
    - JUnit
    - Mockito

- Client
    - React
    - TypeScript
    - Jest

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
