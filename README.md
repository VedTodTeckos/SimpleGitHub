# SimpleGitHub

A Java wrapper for the GitHub API that simplifies common GitHub operations. This library provides an easy-to-use interface for interacting with GitHub repositories, issues, pull requests, and more, with special support for GitHub Copilot management.

## Features
- Simple and intuitive API
- Fluent interface design
- Common GitHub operations wrapped in easy-to-use methods
- Built on top of the official GitHub Java API
- Comprehensive branch management
- Issue tracking and management
- GitHub Copilot management for organizations
  - Seat allocation and management
  - Usage metrics and analytics
  - User access control

## Requirements
- Java 17 or higher
- Maven 3.6 or higher
- GitHub Personal Access Token with appropriate scopes

## Installation

### Maven
Add the following dependency to your `pom.xml`:
```xml
<dependency>
    <groupId>io.github.vedtodteckos</groupId>
    <artifactId>simple-github</artifactId>
    <version>0.6.0</version>
</dependency>
```

### Gradle
Add the following to your `build.gradle`:
```groovy
implementation 'io.github.vedtodteckos:simple-github:0.6.0'
```

## Usage Examples

### Initializing the Client
```java
// Create a client with your GitHub token
SimpleGitHub github = SimpleGitHub.connect("your-github-token");
```

### Working with Issues
```java
// Create a new issue
github.repository("owner", "repo")
      .createIssue()
      .title("Bug Report")
      .body("Description of the bug")
      .label("bug")
      .assignees("username1", "username2")
      .create();
```

### Working with Branches
```java
// Get a repository handler
RepositoryHandler repo = github.repository("owner", "repo");

// List all branches
List<String> branches = repo.getBranchNames();

// Get the default branch
String defaultBranch = repo.getDefaultBranch();

// Create a new branch
BranchHandler newBranch = repo.createBranch("feature-branch", "main");

// Work with a specific branch
BranchHandler branch = repo.branch("feature-branch");
String sha = branch.getSha();
boolean isProtected = branch.isProtected();

// Delete a branch
branch.delete();
```

### Managing GitHub Copilot
```java
// Get Copilot client
GitHubRestClient copilotClient = new GitHubRestClient("your-github-token");

// Get seat information
CopilotSeatInfo seatInfo = copilotClient.getCopilotSeats("your-org");
System.out.println("Used seats: " + seatInfo.getUsedSeats() + "/" + seatInfo.getTotalSeats());

// Get usage metrics for the last 30 days
LocalDateTime startDate = LocalDateTime.now().minusDays(30);
LocalDateTime endDate = LocalDateTime.now();
CopilotUsageMetrics metrics = copilotClient.getCopilotUsage("your-org", startDate, endDate);

// Print daily metrics
metrics.getDailyMetrics().forEach(daily -> {
    System.out.printf("Date: %s, Active Users: %d, Lines Accepted: %d%n",
            daily.getDate(), daily.getActiveUsers(), daily.getLinesAccepted());
});

// Manage user access
String username = "developer";
CopilotUserStatus status = copilotClient.getCopilotUserStatus("your-org", username);

if (!status.isSeatAssigned()) {
    copilotClient.assignCopilotSeat("your-org", username);
}
```

## Error Handling
The library uses checked exceptions for error handling. Most methods throw `IOException` for network-related issues or API errors. Example:

```java
try {
    CopilotSeatInfo seatInfo = copilotClient.getCopilotSeats("your-org");
} catch (IOException e) {
    // Handle API errors, network issues, etc.
    System.err.println("Error accessing GitHub API: " + e.getMessage());
}
```

## Building from Source
1. Clone the repository:
```bash
git clone https://github.com/vedtodteckos/SimpleGitHub.git
```

2. Build with Maven:
```bash
mvn clean install
```

## Running Tests
The project includes comprehensive unit tests. Run them with:
```bash
mvn test
```

## Contributing
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Publishing to Maven Central
See the [Publishing Guide](PUBLISHING.md) for detailed instructions on publishing new versions to Maven Central.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## API Documentation
Full API documentation is available in the [Javadoc](https://vedtodteckos.github.io/SimpleGitHub/apidocs/).

## Support
- Create an issue in the [Issue Tracker](https://github.com/vedtodteckos/SimpleGitHub/issues)
- Contact the maintainers at support@simplegithub.io

## Acknowledgments
- Built on top of the [GitHub API for Java](https://github-api.kohsuke.org/)
- Uses [Gson](https://github.com/google/gson) for JSON processing
- Uses [Project Lombok](https://projectlombok.org/) for reducing boilerplate code
