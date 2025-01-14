# Publishing to Maven Central

This guide explains how to publish new versions of SimpleGitHub to Maven Central.

## Prerequisites

1. Sonatype OSSRH (Open Source Software Repository Hosting) account
2. GPG key for signing artifacts
3. Maven settings configured for deployment

## Setup

### 1. Create Sonatype Account

1. Create an account at [Sonatype JIRA](https://issues.sonatype.org/)
2. Create a New Project ticket
3. Wait for approval and setup of your group ID

### 2. GPG Setup

1. Install GPG:
   ```bash
   # macOS
   brew install gnupg
   
   # Ubuntu
   sudo apt-get install gnupg
   ```

2. Generate a key pair:
   ```bash
   gpg --gen-key
   ```

3. List your keys:
   ```bash
   gpg --list-keys
   ```

4. Distribute your public key:
   ```bash
   gpg --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID
   ```

### 3. Maven Settings

Configure `~/.m2/settings.xml`:

```xml
<settings>
  <servers>
    <server>
      <id>central</id>
      <username>your-sonatype-username</username>
      <password>your-sonatype-password</password>
    </server>
  </servers>
  <profiles>
    <profile>
      <id>ossrh</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <properties>
        <gpg.executable>gpg</gpg.executable>
        <gpg.passphrase>your-gpg-passphrase</gpg.passphrase>
      </properties>
    </profile>
  </profiles>
</settings>
```

## Publishing Process

### 1. Prepare for Release

1. Update version in `pom.xml`:
   ```xml
   <version>x.y.z</version>
   ```

2. Update README.md with new version
3. Commit changes:
   ```bash
   git add pom.xml README.md
   git commit -m "Prepare release x.y.z"
   ```

### 2. Deploy Snapshot (Optional)

For testing before final release:

```bash
mvn clean deploy
```

### 3. Perform Release

1. Prepare the release:
   ```bash
   mvn release:prepare
   ```
   This will:
   - Update to release version
   - Create git tag
   - Update to next snapshot version

2. Perform the release:
   ```bash
   mvn release:perform
   ```
   This will:
   - Build and sign artifacts
   - Deploy to OSSRH
   - Automatically release to Maven Central

### 4. Verify Release

1. Check [Maven Central](https://search.maven.org/) for your artifacts
2. Verify the release tag is pushed to GitHub
3. Test the released version in a new project

## Troubleshooting

### Common Issues

1. GPG signing fails:
   - Verify GPG key is properly set up
   - Check GPG passphrase in settings.xml
   - Ensure gpg-agent is running

2. Deployment fails:
   - Verify Sonatype credentials
   - Check network connectivity
   - Ensure version is not already released

3. Release plugin issues:
   - Clean local git state
   - Verify SCM settings in pom.xml

### Support

If you encounter issues:
1. Check [Sonatype OSSRH Guide](https://central.sonatype.org/publish/publish-guide/)
2. Create an issue in the project repository
3. Contact the maintainers

## Notes

- Always test with a snapshot version first
- Follow semantic versioning
- Update documentation with each release
- Keep your GPG key secure
- Backup your signing key and settings 