# CI/CD walkthrough

This project tests an externally hosted banking demo. CI compiles and runs the automated tests. Delivery publishes a versioned automation bundle after a successful run on `main`; it does not deploy or modify the QA Playground application.

## 1. Understand the trigger

The workflow is `.github/workflows/banking-tests.yml`. It starts on pushes to `main`, pull requests targeting `main`, and manual runs. No repository secrets are needed for the existing public demo accounts. GitHub Actions must be enabled for the repository.

## 2. Prepare the runner

GitHub provides an Ubuntu runner. The workflow checks out the selected commit, installs Temurin Java 21, caches Maven dependencies using the POM as the cache key input, and prints Java, Maven, and Chrome versions. Chrome is supplied by the hosted runner; Selenium Manager resolves ChromeDriver when `ChromeDriver` starts.

## 3. Build and test

The workflow runs from `qaplaygroundbank/`:

```bash
mvn --batch-mode --no-transfer-progress clean verify -Dheadless=true
```

- `clean` removes stale build output, including committed historical reports.
- Compilation uses the Java 21 release setting.
- Surefire discovers all eight `*Tests` classes and runs them sequentially.
- `-Dheadless=true` runs Chrome without opening a desktop window.
- Test failures return a nonzero Maven exit code and fail the job.
- If tests pass, Maven packages page objects and test classes into separate JARs.

The browser driver remains shared through static fields, so parallel execution is intentionally not enabled. There are no automatic retries or ignored test failures.

## 4. Collect evidence

After the build step, report upload is attempted even when tests fail. Download `test-evidence-<run-id>-<attempt>` from the run's Artifacts section. It contains available Surefire reports and failure screenshots.

Read the Surefire XML/text reports for failed assertions and configuration errors. Screenshots are captured after unsuccessful test methods when a browser session is available; a browser-startup or compilation failure may produce no screenshot or test report. In that case, use the workflow logs. Artifacts are retained for 14 days, subject to repository policy.

## 5. Deliver only after success

On a successful push or manual run on `main`, `banking-framework-<commit-sha>` contains compiled JARs, the POM, source code, and usage documentation. Pull-request runs and failed runs do not deliver this bundle.

This is a downloadable test-framework package, not a self-contained executable application. To rerun it, extract the artifact, install JDK 21/Maven/Chrome, enter the included `qaplaygroundbank` directory, and run the Maven command above. Dependencies and access to the demo site are still required.

## 6. Start a manual run

1. Open the repository on GitHub.
2. Select **Actions → Banking UI CI**.
3. Select **Run workflow**, choose `main`, and confirm **Run workflow**.
4. Open the new run and expand **Build and test (Chrome)**.
5. Inspect **Build and run Selenium tests**, then download the available artifacts.

A new commit on the same branch cancels an older in-progress run to avoid spending time testing outdated code.

## 7. Use CI during development

Create a feature branch, push your changes, and open a pull request to `main`. The workflow tests the pull-request code. Investigate failures using logs, reports, and screenshots before merging.

To enforce the result, a repository administrator can configure a branch ruleset for `main` and require **Build and test (Chrome)** after that check has appeared in a run. The workflow itself does not configure branch protection or prevent merging.

## 8. Reproduce locally

```bash
cd qaplaygroundbank

# Visible Chrome
mvn clean verify

# Same headless mode as CI
mvn clean verify -Dheadless=true

# One class or method
mvn test -Dheadless=true -Dtest=LoginTests
mvn test -Dheadless=true -Dtest=AccountsTests#testAddNewAccount

# Override the application URL when testing a compatible environment
mvn test -Dheadless=true -DbaseUrl=https://qaplayground.com/bank/login
```

`baseUrl` defaults to the public banking login page. The page objects and assertions still require the same application structure and seed data.

## Troubleshooting

| Failure | Next action |
| --- | --- |
| Dependency resolution | Inspect Maven's exact artifact/version error and network access. |
| Chrome/driver startup | Inspect tool versions and Selenium Manager logs. |
| Element timeout | Inspect the screenshot and confirm the locator and expected user state. |
| Financial assertion mismatch | Compare expected and actual values; do not weaken the assertion to make CI green. |
| Missing artifact | Check whether compilation/browser startup failed before evidence was generated. |
| No workflow execution | Check Actions is enabled and the event targets `main`. |

The suite includes a known-defect check using `error_user` that expects an incorrect loan total. Its passing status means the demo defect was reproduced, not fixed. A live-site outage or UI/data change can also fail this suite.
