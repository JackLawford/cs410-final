# cs410-final

This program is a Java command-line application for managing class grades using a local MySQL database.

## Database Setup

To set up the database, make sure MySQL Server is running on your machine and log in with a user that has permission to create databases. From the MySQL prompt, run:

```sql
SOURCE path/to/schema.sql;
```

The schema creates a database called school and all required tables.
If you want a dedicated MySQL user for this program, you may create one manually and grant it privileges on the school database.
## Configuring the Database Connection

Before compiling, open Database.java and make sure the connection URL, username, and password match your MySQL setup. A typical configuration looks like:

```
private static final String URL  = "jdbc:mysql://localhost:3306/school";
private static final String USER = "grades_user";
private static final String PASS = "password123";
```

Update these values according to your MySQL installation.
## Compiling

Make sure you have the MySQL JDBC driver JAR available (for example mysql-connector-j.jar).

Compile the program with:

```
javac -cp lib/mysql-connector-j.jar grades/ *.java
```

If you prefer compiled output in a separate directory, create an out folder and run:

```
mkdir out
javac -cp lib/mysql-connector-j.jar -d out grades/ *.java
```

## Running the Program

Run the shell with:
```
java -cp out:lib/mysql-connector-j.jar grades.Main
```

On Windows, use ; instead of : in the classpath.

When the program starts, you will see a prompt. You may enter commands such as:

```
new-class CS410 Sp25 1 "Databases"
list-classes
select-class CS410 Sp25 1
show-class
```

Once a class is selected, you may continue with commands for categories, assignments, students, and grading. Use quit or exit to close the program.