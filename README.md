# Shift Planner
Shift Planner is a web application designed to assist in planning work shifts for nurses using **Google OR-Tools**’ **CpSolver**. It leverages Vaadin’s **Hilla** framework to provide a seamless frontend-backend interaction and uses **Spring ApplicationEvents** to handle communication between different backend components. This tool helps optimize scheduling while adhering to constraints, ensuring fair and efficient shift distribution.

## Table of Contents
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Configuration](#configuration)
- [Contributing](#contributing)
- [License](#license)

## Features
- **Automated Shift Planning**: Uses Google OR-Tools' CpSolver to optimize nurse shifts.
- **Frontend-Backend Interaction**: Powered by Vaadin Hilla, allowing real-time updates between frontend and backend.
- **Event-Driven Architecture**: Backend utilizes **Spring ApplicationEvents** for communication between components.
- **Constraint Handling**: Supports scheduling constraints such as maximum working hours, consecutive shifts, and rest periods.
- **User-Friendly Interface**: Nurses and admins can interact with the planner via a responsive web UI.

## Installation

### Prerequisites
- **Java 17+**
- **Maven** (or Gradle, depending on your build tool)
- **Node.js and npm**: Required by Vaadin Hilla for frontend development

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/SvabRadek/shiftplanner.git
    ```
2. Navigate to the project directory:
   ```bash
   cd shiftplanner
   ```
3. Build the project:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```
## Frontend Development
To work on the frontend part of the application:

1. Start the Hilla frontend development server:
   ```bash
   mvn hilla:dev
   ```
## Usage
1. Access the application: Once the server is running, open a browser and go to http://localhost:8080.
2. Login: Administrators and nurses can log in to the system.
3. Shift Scheduling: Administrators can input constraints, such as maximum hours per week, consecutive working days, etc.
4. Optimize Shifts: The backend uses CpSolver to compute the optimal shift allocation and sends the results back to the frontend.
5. Review and Edit Shifts: The shifts can be reviewed and manually adjusted if necessary.

## Configuration
### Application Properties
You can configure the application in src/main/resources/application.properties.

- Database Settings:
   ```properties
   spring.datasource.url=jdbc:h2:mem:testdb
   spring.datasource.driverClassName=org.h2.Driver
   spring.datasource.username=sa
   spring.datasource.password=password
   ```
- Frontend Port (if using the Hilla development server):

   ```properties
   vaadin.server.port=8081
   ```

- Solver Configuration
You can configure the OR-Tools CpSolver to change the optimization strategy or add additional constraints in the backend.

## Contributing
Fork the project.
1. Create your feature branch: git checkout -b feature/your-feature.
2. Commit your changes: git commit -m 'Add some feature'.
3. Push to the branch: git push origin feature/your-feature.
4. Open a pull request.

## License
This project is licensed under the MIT License.
