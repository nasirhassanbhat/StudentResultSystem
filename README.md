# Student Result System

A simple Java application for managing and displaying student results.  
This project connects to an Oracle Database using the `ojdbc8.jar` JDBC driver.

## How to Run
1. Download the project.
2. Download `ojdbc8.jar` from Oracle and place it inside a folder named `lib/` in the project root.
3. Compile the code:
   - **Windows**  
     ```bash
     javac -cp "lib/ojdbc8.jar;." *.java
     ```
   - **Linux/macOS**  
     ```bash
     javac -cp "lib/ojdbc8.jar:." *.java
     ```
4. Run the program:
   - **Windows**  
     ```bash
     java -cp "lib/ojdbc8.jar;." Main
     ```
   - **Linux/macOS**  
     ```bash
     java -cp "lib/ojdbc8.jar:." Main
     ```

## Project Files
- `DBConnect.java` – Handles database connectivity  
- `StudentManager.java` – Manages student records  
- `Main.java` – Entry point of the program  
- `StudentResultSystemUI.java` – User interface for displaying results
