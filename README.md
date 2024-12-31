# predictions
This Java project implements a predictions application using JavaFX. Below is a detailed explanation of the system's structure and functionality, broken down by modules and components.

Team Members:
Sharon Olshansky – ID: 318845740 – sharonol2@mta.ac.il
Eitan Feagina – ID: 20653173 – eitanfea@mta.ac.il
System Overview:
The project consists of three main modules:

1. DtoManagement
This module is responsible for managing the communication between the UI and the core logic of the system.

Key Class: ManageSystemToUI
This class handles user requests received via the UI, processes them, and performs actions in the system based on the received DTO (Data Transfer Object).
Packages:
Command Interpretation: Translates user commands into system-understandable language.
Engine Execution: Executes the system's core logic based on user commands.
2. System
This is the core module, managing all system processes and logic.

3. JavaFX
This module is responsible for the user interface and communication with the user.

Key Functionality:
Captures user input and displays the output.
Sends DTOs to the system via the DtoManagement module to execute desired processes and validations.
Ensures users are only presented with relevant commands based on the current system state.
Main Classes and Functionality
Convert Package
Contains classes responsible for converting JAXB formats into the system's internal data structures.
ManageSystemToUI Class
Located in the DtoManagement module.
Purpose: Manages the linkage between the user interface and the system.
Features:
Handles multiple cases (switch-case) to process user instructions and system actions.
Stores:
All "worlds" loaded successfully from the uploaded file.
Completed simulations for quick access to past results.
World Package
Manages all information related to the system's "world."
Sub-packages:
Entities:
EntityDefinition: Contains the entity name and its number of instances.
EntityInstance: Represents a unique instance of an entity, including its attributes.
Properties:
Stores information about property types (integer, decimal, boolean, string).
Contains an interface for random value generation based on property constraints (e.g., range, format).
Environment:
Stores a list of environmental variables.
Rules:
Contains rule-related information, including actions (Action) and execution logic.
Simulation Package
Manages all simulation processes.
Key Classes:
SimulationRunner:
Implements Runnable.
Handles the entire simulation process within the run method.
SimulationExecutionDetails:
Stores data formats and results for simulation display.
Automatically syncs with the World class for updates.
SimulationExecutionManager:
Acts as a bridge between the system's core logic and the UI.
Manages simulations and threads using a map of simulation numbers and corresponding details.
SimulationsQueueManage:
Stores and manages the simulation queue.
Exception Handling
A dedicated package manages various exceptions, such as invalid formats, nonexistent values, or illegal operations.
The system throws specific exceptions with detailed error messages based on the issue.
JavaFX Module
Main Class: TaskMain
Serves as the application's entry point.
Loads the primary controller (MainController) responsible for coordinating the entire application.
Controllers:
MainResultsController:
Creates a thread to send pull requests to the system via the connector for result data.
UI Components:
Includes multiple controllers and FXML files located in the components package.
Key Features
Rule System:

Rules are stored as lists in the Rule class.
Rules include tick counts, execution probabilities, and actions like replace or kill.
The rule engine differentiates between end-of-simulation actions and standard tick-based actions.
Actions:

Each action implements an interface with the executeAction method.
Actions are categorized into types (e.g., increase, decrease, set) and managed by a central class.
Simulation Management:

Simulations are executed in threads, and their data is updated in real-time for immediate access.
Dynamic UI:

The UI dynamically adapts based on the system state, ensuring users only see relevant options.
Error Handling:

Comprehensive exception handling ensures detailed feedback for any issues.
Technological Stack
Core Framework: Java
UI Framework: JavaFX
Data Serialization: JAXB
Concurrency: Java threads for simulation execution
Modular Design: Clear separation of responsibilities across modules
Team Collaboration
This modular design and clear separation of logic ensured efficient teamwork and maintainable code. The project leverages advanced Java concepts such as multithreading, exception handling, and UI design to create a robust and dynamic prediction application.
