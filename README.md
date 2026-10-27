# uniezz-testing

Automated tests for the Uniezz web and mobile apps.

| Module         | What it tests        | Tooling                   | CI workflow                         |
|----------------|----------------------|---------------------------|-------------------------------------|
| `api-tests`    | Backend REST API     | REST Assured + JUnit 5    | `.github/workflows/api-tests.yml`    |
| `web-tests`    | Web app UI           | Playwright + JUnit 5      | `.github/workflows/web-tests.yml`    |
| `mobile-tests` | Android / iOS app    | Appium + JUnit 5          | `.github/workflows/mobile-tests.yml` |
| `test-core`    | Shared config/utils  | —                         | (built by all of the above)          |

## Structure

```
uniezz-testing/
├── pom.xml                         # parent POM: modules + all dependency versions
├── test-core/                      # shared code, no tests
│   └── src/main/
│       ├── java/.../core/config/   # Config: -D property > ENV var > {env}.properties
│       └── resources/config/       # dev.properties, staging.properties
├── api-tests/
│   └── src/
│       ├── main/java/.../api/
│       │   ├── client/             # one client per API resource (endpoints live here)
│       │   ├── models/             # request/response POJOs
│       │   └── specs/              # shared RequestSpecifications
│       └── test/
│           ├── java/.../api/tests/ # test classes
│           └── resources/schemas/  # JSON schemas for contract checks
├── web-tests/
│   └── src/
│       ├── main/java/.../web/
│       │   ├── BaseWebTest.java    # browser/context/page lifecycle + traces
│       │   ├── pages/              # Page Objects
│       │   └── components/         # reusable UI pieces (header, modal...)
│       └── test/java/.../web/tests/
├── mobile-tests/
│   ├── apps/                       # .apk / .app downloaded here (git-ignored)
│   └── src/
│       ├── main/java/.../mobile/
│       │   ├── BaseMobileTest.java
│       │   ├── driver/             # DriverFactory (Android / iOS)
│       │   └── screens/            # Screen Objects
│       └── test/java/.../mobile/tests/
└── .github/workflows/              # one independent pipeline per test type
```

Rule of thumb: **`src/main` = framework code (clients, pages, screens), `src/test` = the tests themselves.**

## Running locally

Requires JDK 21+ and Maven 3.9+.

```bash
# API
mvn -pl api-tests -am verify

# Web (first time only: install browsers)
mvn -pl web-tests -am install -DskipTests
mvn -pl web-tests exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
mvn -pl web-tests verify -Dweb.headless=false

# Mobile (Appium server running, emulator/simulator up, app in mobile-tests/apps/)
mvn -pl mobile-tests -am verify -Dmobile.platform=android
```

Common options:

| Option                    | Meaning                              |
|---------------------------|--------------------------------------|
| `-Denv=staging`           | pick `config/staging.properties`     |
| `-Dgroups=smoke`          | run only tests tagged `@Tag("smoke")` |
| `-Dapi.base.url=...`      | override any config key              |

## CI

Each workflow runs independently and only when its own module (or shared code) changes:

- **push / pull request**: path-filtered, so a web-only change does not start the mobile pipeline
- **nightly schedule**: full regression
- **manual** (Actions tab → *Run workflow*): choose env, tags, platform
- **repository_dispatch**: the app repos can trigger a run after deploying, e.g.

  ```bash
  curl -X POST -H "Authorization: Bearer $TOKEN" \
    https://api.github.com/repos/Uniezz/uniezz-testing/dispatches \
    -d '{"event_type":"web-tests","client_payload":{"env":"staging"}}'
  ```

Repository settings to configure:

- **Secrets**: `API_TOKEN`, `WEB_USER_PASSWORD` (any config key works as an ENV var: `api.token` → `API_TOKEN`)
- **Variables**: `ANDROID_APP_URL`, `IOS_APP_URL`, optional `IOS_DEVICE`
