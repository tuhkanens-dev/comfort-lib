### About
Comfort API is a library designed to simplify development, primarily for plugins regardless of their platform — whether it's Spigot or Velocity. The library is also fully compatible with the Fabric mod loader.

The library is currently under active development and receives frequent, sometimes minor, patches. However, breaking changes between updates may occur. Even with major internal overhauls, there won't be a transition to a 2.0.0 development branch anytime soon, as the library is still deep in development, requires further refinement, and is not yet complete.

### APIs
The library provides several development tools:

- **`ConfigAPI`** – reads and saves data to `config.yml`.
- **`MessagesAPI`** – reads messages by key from `messages.yml` with `MiniMessage` support.
- **`DatabaseAPI`** – handles database operations, supporting `MYSQL` and `SQLITE`.
- **`UpdateAPI`** – checks Modrinth for new project releases.

### Config
```yml
# for DatabaseAPI
database:
  # provider: mysql or sqlite
  provider: sqlite
  database: comfort
  # for mysql
  port: 3306
  host: localhost
  username: root
  password: ''

# for UpdateAPI
check-updates: true
```

### Required dependencies:
Comfort API includes dependencies with `provided` scope, allowing you to choose how your project handles them, whether by loading them dynamically at runtime or compiling them directly into your build.

Depending on the API usage in your project, you must include the required libraries in your project. As of update 1.2.2, API registration checks whether a corresponding loaded class is present for its operation, so you can decide what you need and what you don't.

The library does not check for the presence of HikariCP, SQLite, or mysql-connector-j, so you must include them in your project yourself for proper operation. If you don't need SQLite, you can omit its dependency. Similarly, MySQL requires HikariCP and mysql-connector-j, but if you don't need MySQL, simply don't add these dependencies. However, all database types require Jdbi 3.
```kt
// ALL
compileOnly("org.slf4j:slf4j-api:2.0.18")

// MESSAGES
compileOnly("net.kyori:adventure-api:4.17.0")
compileOnly("net.kyori:adventure-text-minimessage:4.17.0")
compileOnly("net.kyori:adventure-text-serializer-legacy:5.2.0")

// MESSAGES AND CONFIG
compileOnly("org.spongepowered:configurate-yaml:4.2.0")

// UPDATE
compileOnly("com.google.code.gson:gson:2.14.0")

// DATABASE
compileOnly("org.jdbi:jdbi3:3.0.0-beta2")
// optional
compileOnly("com.zaxxer:HikariCP:7.1.0")
compileOnly("com.mysql:mysql-connector-j:9.7.0")
compileOnly("org.xerial:sqlite-jdbc:3.53.2.0")
```