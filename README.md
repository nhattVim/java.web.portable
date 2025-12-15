## Java Web Portable Project

This is a simple portable Java web application. Build without Maven/Gradle.

### Technologies Used

- **Backend:** Java, Spring Framework (MVC, Data JPA), Hibernate
- **Frontend:** Thymeleaf, HTML
- **Database:** H2 (in-memory/file-based)
- **Web Server:** Jetty or Tomcat

### Prerequisites

[Java 21](https://www.oracle.com/java/technologies/downloads/#java21)

### How to Run

##### Linux / Unix

```sh
./run.sh jetty
# or
./run.sh tomcat
# or
./run.sh hot
```

##### Windows

Double-click the `run.bat` file and select the server type.

### Database

The application uses an H2 database. The database file is `testdb.mv.db`. Access `http://localhost:8080/h2` when run with jetty or `http://localhost:8080/webapp/h2` when run with tomcat and enter `jdbc:h2:file:./testdb` to view the database.
