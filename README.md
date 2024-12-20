# Project 2 Specification

## Project Development Requirements
- You must create a project proposal, which after approval, you can begin development
    - The project proposal should contain enough work for all your team members
    - You can refer to this example for an idea of what is expected in a proposal:
        - https://docs.google.com/document/d/1VRY7mCuX8WVist3HS8h-DeTgGHxQWfuz1cUpK64ptms/edit?pli=1#heading=h.z6ne0og04bp5
- Must follow Scrum for the project
	- Must have a ScrumMaster
	- Conduct Daily Standup
	- Must have notes on each team member
	- Write notes on the Trello board

## General Presentation Guidelines

- Zoom Backgrounds
    - No animations or moving backgrounds.
    - Nothing offensive on backgrounds.
    - All members must have the same background.
- Preparations
    - Business professional attire.
    - Cameras always on for all members.
    - Everyone must speak during presentation.
- Considerations to Make
    - Make sure multiple people answer questions during Q&A
    - Have a code freeze at least 24-48 hours before the showcase

## General Documentation
You are responsible for detailing the README.md with general application information, prerequisites/additional software needed to get started, Environment Variables needed to be set, etc

## Project Technology Requirements

The application must be deployed (both frontend and backend)
- The Spring Boot application (backend) may be deployed to an EC2 instance (using a docker container)
- The React application (frontend) may be deployed to an EC2 instance (using a docker container) or may be deployed to an S3 bucket

Note: Please refer to the CI/CD Pipeline card which defines the requirement of creating a CI/CD pipeline that enables an automated or semi-automated deployment to EC2.

### CICD Pipeline
Develop a CI/CD pipeline that will at minimum do the following for both the frontend and backend of the application.
- Run existing tests
- Build the application
- Deploy the application

These steps should not run if any previous steps have failed.

### Project Features

- Register
- Login
- User Profile
- Consuming a REST API
	- The application must be consuming API data from an external REST API that you did not make yourself

### Technology

#### Backend
- Postman for testing endpoints
- AWS RDS
- Java
- Spring
- Apache Kafka
- Hashing of passwords
- JWTs for auth
- JUnit
- Mockito

#### Frontend
- React
- TypeScript
- Jest
- CSS Framework (such as Bootstrap)

#### DevOps
- AWS EC2
- Docker
- Jenkins
- AWS S3