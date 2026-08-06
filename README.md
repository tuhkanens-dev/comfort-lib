### Required dependencies:
Comfort API includes dependencies with `provided` scope, allowing you to choose how your project handles them, whether by loading them dynamically at runtime or compiling them directly into your build.
```kt
implementation("net.kyori:adventure-api:4.17.0")
implementation("net.kyori:adventure-text-minimessage:4.17.0")
implementation("net.kyori:adventure-text-serializer-legacy:5.2.0")

implementation("org.jetbrains.exposed:exposed-core:1.3.1")
implementation("org.jetbrains.exposed:exposed-jdbc:1.3.1")
implementation("org.jetbrains.exposed:exposed-migration-jdbc:1.3.1")

implementation("com.zaxxer:HikariCP:7.1.0")
implementation("org.spongepowered:configurate-yaml:4.2.0")
implementation("com.mysql:mysql-connector-j:9.7.0")
```