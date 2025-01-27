# FSTBank Management System

A Java EE banking management system that implements various design patterns and provides functionalities for managing bank accounts, transactions, and clients.

## 🏗 Architecture

The project follows a layered architecture with:

- **Model**: Business entities (Client, Account, Transaction etc.)
- **View**: User interfaces (VueClient, VueBanquier)
- **Controller**: Business logic controllers
- **Factory**: Object creation patterns
- **Observer**: Real-time transaction monitoring

## 🛠 Prerequisites

- Java JDK 22
- Maven
- PostgreSQL 15+
- WildFly 34.0.1.Final
- IDE (IntelliJ IDEA recommended)

## ⚙️ Database Setup

1. Create a PostgreSQL database:
```sql
CREATE DATABASE fstbank;
```

2. Update database credentials in `src/main/resources/META-INF/persistence.xml`

## 🚀 Installation Steps

1. Clone the repository:
```bash
git clone https://github.com/yourusername/FSTBank-Management-System.git
cd FSTBank-Management-System
```

2. Configure WildFly:
    - Add PostgreSQL driver to WildFly
    - Configure datasource in `standalone.xml`

3. Build the project:
```bash
mvn clean install
```

4. Deploy to WildFly:
    - Copy the generated WAR file to WildFly's deployment folder
    - Or use your IDE's deployment tools

## 🖥 Running the Application

### Using Console Interface
```bash
java -jar target/FSTBank-Management-System-1.0-SNAPSHOT.jar
```

### Using Web Interface
1. Start WildFly server
2. Access: `http://localhost:8080/FSTBank-Management-System-1.0-SNAPSHOT`

## 📋 Features

- Account Management (Creation, Updates, Deletion)
- Transaction Processing
- Client Management (Professional/Individual)
- Real-time Balance Updates
- Transaction History
- Multi-user Support

## 🏛 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/example/project/
│   │       ├── controller/
│   │       ├── factory/
│   │       ├── model/
│   │       ├── observer/
│   │       └── view/
│   └── resources/
│       └── META-INF/
└── test/
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch
3. Commit changes
4. Push to branch
5. Open a Pull Request

