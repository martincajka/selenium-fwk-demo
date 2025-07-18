# Selenium Enterprise Framework

An enterprise-level Selenium framework for testing web applications. This framework is designed as an MVP (Minimum Viable Product) that showcases advanced testing capabilities while maintaining a clean, maintainable architecture.

## Features

- **Browser Management**: Support for multiple browsers, versions, headless mode, and responsive testing
- **Parallel Test Execution**: Custom implementation using Java 21 features
- **Response Mocking**: Network interception and response modification
- **Visual Testing**: Screenshot comparison and visual element detection
- **Component-Based Architecture**: Reusable UI components with specialized interfaces
- **Comprehensive Logging**: Configurable logging levels and formats
- **Custom Reporting**: Detailed test execution metrics and multiple report formats
- **Future Auto-Healing**: Planned support for element recovery and alternative locator strategies

## Getting Started

### Prerequisites

- Java 21 or higher
- Gradle 8.0 or higher
- Chrome, Firefox, Edge, or Safari browser

### Installation

1. Clone the repository
2. Build the project with Gradle:

```bash
./gradlew build
```

### Configuration

The framework can be configured using the `framework.properties` file in the `src/test/resources` directory. See the sample file for available options.

## Usage Guidelines

### Creating Tests

1. **Use Component-Based Approach**

   Instead of directly using WebElements, use the component classes:

   ```java
   // Instead of this:
   WebElement button = driver.findElement(By.id("submit-button"));
   button.click();

   // Do this:
   Button submitButton = new Button(driver, By.id("submit-button"));
   submitButton.click();
   ```

2. **Implement Page Objects**

   Create page objects that use components:

   ```java
   public class LoginPage {
       private final WebDriver driver;
       private final Input usernameInput;
       private final Input passwordInput;
       private final Button loginButton;

       public LoginPage(WebDriver driver) {
           this.driver = driver;
           this.usernameInput = new Input(driver, By.id("username"));
           this.passwordInput = new Input(driver, By.id("password"));
           this.loginButton = new Button(driver, By.id("login"));
       }

       public void login(String username, String password) {
           usernameInput.type(username);
           passwordInput.type(password);
           loginButton.click();
       }
   }
   ```

3. **Use Annotations for Test Configuration**

   Use annotations to configure tests:

   ```java
   @Test
   @Sequential
   @MockResponseDefinition(url = "/api/users", body = "{\"name\": \"John\", \"age\": 30}")
   public void testUserProfile() {
       // Test code
   }
   ```

### Parallel Execution

The framework supports parallel execution by default. To run tests sequentially, use the `@Sequential` annotation:

```java
@Sequential
public class SequentialTests {
    // All tests in this class will run sequentially
}

// Or for a specific test
@Test
@Sequential
public void testThatShouldRunSequentially() {
    // This test will run sequentially
}
```

### Network Interception

To mock network responses:

```java
// Programmatically
NetworkInterceptor interceptor = new NetworkInterceptor(driver);
interceptor.addMockResponse(
    new MockResponse("/api/users")
        .withBody("{\"name\": \"John\", \"age\": 30}")
);
interceptor.start();

// Or using annotations
@Test
@MockResponseDefinition(url = "/api/users", body = "{\"name\": \"John\", \"age\": 30}")
public void testWithMockResponse() {
    // Test code
}
```

### Visual Testing

To perform visual testing:

```java
VisualTester visualTester = new VisualTester(driver);

// Set baseline image
visualTester.setBaseline("login-page", "path/to/baseline/login-page.png");

// Compare current state with baseline
ComparisonResult result = visualTester.compareWithBaseline("login-page");
assertTrue(result.isPassed());
```

### Reporting

To generate reports:

```java
TestReport report = TestReport.getInstance();

// Start a test suite
TestSuite suite = report.startSuite("Login Tests", "Tests for login functionality");

// Start a test
TestCase testCase = report.startTest("Login Tests", "Valid Login", "Test valid login credentials");

// Record actions
TestAction action = new TestAction("Enter username");
// Perform action
action.end();
report.recordAction("Login Tests", "Valid Login", action);

// End test
report.endTest("Login Tests", "Valid Login", TestStatus.PASSED, null);

// End suite
report.endSuite("Login Tests");

// Finalize and generate reports
report.finalizeReport();
report.generateHtmlReport();
report.generateJUnitXmlReport();
```

## Best Practices

1. **Component Reuse**: Create a library of reusable components for common UI elements
2. **Consistent Waiting Strategy**: Use the built-in waiting methods in components
3. **Proper Logging**: Include meaningful log messages at appropriate levels
4. **Clean Test Data**: Separate test data from test logic
5. **Independent Tests**: Ensure tests can run independently and in any order
6. **Meaningful Assertions**: Use descriptive assertion messages
7. **Regular Visual Baseline Updates**: Update visual baselines when UI changes are expected

## Implementation Roadmap

### Phase 1: Core Framework (Current)
- Basic browser management
- Component architecture
- Parallel execution
- Simple reporting

### Phase 2: Advanced Features
- Enhanced network interception
- Comprehensive visual testing
- Detailed reporting with charts
- Test data management

### Phase 3: Auto-Healing and AI
- Element recovery mechanisms
- Learning from successful recoveries
- AI-assisted test maintenance
- Predictive test failure analysis

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.