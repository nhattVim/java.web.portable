## Java Web Portable Project

This is a simple portable Java web application. Build without Maven/Gradle.

### Technologies Used

- **Backend:** Java, Spring Framework (MVC, Data JPA), Hibernate
- **Frontend:** Thymeleaf, HTML
- **Database:** H2 (in-memory/file-based)
- **Web Server:** Jetty or Tomcat

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

The application uses an H2 database. The database file is `testdb.mv.db`.
