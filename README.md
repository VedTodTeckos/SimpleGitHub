# SimpleGitHub

A Java wrapper for the GitHub API that simplifies common GitHub operations. This library provides an easy-to-use interface for interacting with GitHub repositories, issues, pull requests, and more.

## Features
- Simple and intuitive API
- Fluent interface design
- Common GitHub operations wrapped in easy-to-use methods
- Built on top of the official GitHub Java API

## Requirements
- Java 17 or higher
- Maven 3.6 or higher

## Getting Started

1. Clone the repository:
```bash
git clone https://github.com/yourusername/SimpleGitHub.git
```

2. Build the project:
```bash
mvn clean install
```

3. Add the dependency to your project:
```xml
<dependency>
    <groupId>com.simplegithub</groupId>
    <artifactId>simple-github</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## Usage Example
```java
SimpleGitHub github = SimpleGitHub.connect(yourGitHubToken);
github.repository("owner", "repo")
      .createIssue()
      .title("Bug Report")
      .body("Description of the bug")
      .label("bug")
      .create();
```

## License
This project is licensed under the MIT License - see the LICENSE file for details.
