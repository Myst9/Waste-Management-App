# Waste Management App
The Waste Management App is a comprehensive project aimed at effectively managing and optimizing waste collection. This project is designed to streamline waste management operations for municipalities, companies, and communities, leading to improved environmental sustainability and resource utilization. The theme of the project is `Sustainable Cities and Communities` from one of goals of `SDG`

## Table of Contents
[Introduction](#introduction)

[Keywords](#keywords)

[App flow and Features](#app-flow-and-features)

[Installation](#installation)

[Usage](#usage)

[Technologies](#technologies)

[Contributing](#contributing)

[License](#license)

## Introduction
The `Waste Management App` project tries to connect waste collectors and waste producers.

## Keywords
- New Request - Means a waste producer has placed a request to process a type of waste, that needs to be accepted by the waste collectors
- Active Request - the request currently active and needs to be finished
- Archived Request - the history of requests that have been completed

## App flow and Features
+ The app starts up with logo and splash screen. Then it checks if there is a user logged in or not.
+ If not it goes to Sign up page. You sign in by clicking `Signin Here`, if you already have an account.
+ The app has a `Home`, `Requests`, `Profile` and `Settings` pages with navigation bar.
    - Home page has welcome screen.
    - Request page shows all the requests you have. And can make a new request if you a `Seller` tye user. Else, if you are a `Buyer` type user you can accept the requests posted to you.
    - Profile page shows user details and is editable
    - Settings page allows user to logout or delete their account
+ The additional feature our app provides is that waste collectors have different waste types they process and waste producers can select a waste type they need to recycle/process and will only see those waste collectors that process that type of waste.

## Installation

Clone the repository: git clone https://github.com/myst9/waste-management-app.git
Install Android studio and open the cloned repository
In Build, select Build Bundles/APKs to create an APK  

## Usage
`Waste Collectors`:
Log in and check requests 
Accept requests
`waste producers`:
Login and place requests

## Technologies
Frontend: xml
Backend: kotlin
Database: FireBase firestore
Authentication: FireBase 
environment: Android studio

## Contributing
Contributions are welcome! To contribute to the Waste Management System project, follow these steps:
Fork the repository.
Create a new branch: git checkout -b feature-new-feature.
Make your changes and commit them: git commit -m 'Add new feature'.
Push to the branch: git push origin feature-new-feature.
Open a pull request explaining your changes.

## License
This project is licensed under the MIT License.
