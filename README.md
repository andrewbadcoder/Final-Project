# Data Viewer Refactoring Project

## Project Overview

This project involves acting as software consultants to refactor an existing legacy product, the DataViewer software. While the current software functions correctly, it was not developed using Object-Oriented Programming (OOP) principles and currently exists as a single Java file, making it difficult to maintain and enhance.

The primary objective is to reorganize the code into a proper OOP structure and implement specific design patterns to improve maintainability while ensuring the software continues to function exactly as the original implementation.

## Repository Structure

To maintain a clear history of the refactoring process, the source code is organized into specific packages:

- **dataviewer1orig**: Contains the original legacy code provided at the start of the project.
- **dataviewer2**: Contains the code for Milestone 1 (Initial OOP refactoring).
- **dataviewer3final**: Contains the code for the Final Deliverable (Design Patterns implementation).

## Development Milestones

### Milestone 1: OOP Reorganization

**Goal:** Review the software implementation and propose a reorganization of the code into various classes.

**Requirements:**

- Draft UML Class diagram of the first-phase design.
- Break the single Java file into multiple classes with proper relationships.

> **Note:** Design patterns are not implemented in this phase.

### Milestone 2: Design Pattern Integration

**Goal:** Identify and implement appropriate design patterns to further improve the architecture.

**Requirements:**

- Draft UML Class diagram showing the inclusion of at least two design patterns per team member.
- Finalized implementation of the design changes.

> **Note:** The Singleton pattern does not count toward the required number of patterns.

## User Manual & Functionality

The DataViewer software visualizes historical global temperature data at the state level on a monthly basis.

### Main Menu

Upon launch, the application reads the data file and displays the following menu options:

- **(C) Set Country**: Switch the loaded country (e.g., United States). This reloads the data file.
- **(T) Set State**: Select a specific state within the currently loaded country.
- **(S) Set Start Year**: Define the lower bound of the year range.
- **(E) Set End Year**: Define the upper bound of the year range.
- **(V) Set Visualization**: Toggle between visualization modes.
  - **Raw**: Shows raw temperature data (Blue = Cold, Red = Hot).
  - **Extrema**: Highlights data within 10% of the monthly maximum/minimum; intermediate values are grayscale.
- **(P) Plot Data**: Apply settings and generate the graphical plot.
- **(Q) Quit**: Exit the application.

### Plot View Interaction

**Display:** The X-axis represents months (Jan-Dec), and the Y-axis represents years.

**Navigation:**

- Press **'M'** to return to the Main Menu.
- Press **'Q'** to quit the application immediately.

## Technical Notes

- **Code Sharing**: If working in a team, Git is recommended for collaboration to avoid conflicts, though initial refactoring from the single file requires close coordination.
- **Functionality**: The refactored code must properly implement all original functionality; partial credit is awarded for compiling code even if features are incomplete.
