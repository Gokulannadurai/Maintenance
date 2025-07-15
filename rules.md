Think like an java expert

- Proper comments for class and method based on open source standards
- Mention Auther name, verison, date in class level comments
- Proper intentation based on java open source standrads
- unit testcases & edge cases
- Consider sonar vulnerability, security, mainteainablity issue
- Make sonar coverage as 90% and above
- Reuse common logics and implment properly
- For Static data use sepearte constants for that
- Proper Error handling
- Proper logs
- use @Data annotation for getter and setter for pojos
- Add necessary dependency in pom.xml
- follow the following folder structure
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── ideas2it/
│   │   │           └── servicename/
│   │   │               ├── config/
│   │   │               ├── controller/
│   │   │               ├── dto/
│   │   │               ├── entity/
│   │   │               ├── exception/
│   │   │               ├── repository/
│   │   │               ├── security/
│   │   │               ├── service/
│   │   │               └── ServicenameApplication.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── logback-spring.xml
│   │       └── ...
│   └── test/
│       └── java/
│           └── com/
│               └── ideas2it/
│                   └── servicename/
│                       └── ...
├── Dockerfile
├── pom.xml
├── README.md
└── .gitignore
