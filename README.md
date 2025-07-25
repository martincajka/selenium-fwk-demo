# Selenium Enterprise Framework

A lightweight, extensible Selenium framework for automated web testing with advanced capabilities and a clean architecture.

## Current Features

- **Browser Management**: Support for Chrome, Firefox, Edge, and Safari with configurable options for headless mode and viewport sizes
- **Parallel Test Execution**: Run tests concurrently using Java 21 features with configurable thread count
- **Hamcrest Assertions**: Enhanced assertion capabilities with detailed error reporting and listener support
- **Performance Monitoring**: Built-in timing service to track and report operation durations
- **Configurable Logging**: Flexible logging options with different levels and output formats
- **Test Annotations**: Custom annotations for test configuration (@Test, @Ignore, @SingleThreaded)

## Planned Features

- **Screenshot Capture**: Automatic screenshot capture on test failures
- **Response Mocking**: Network interception and response modification
- **Visual Testing**: Screenshot comparison and visual element detection
- **Component-Based Architecture**: Reusable UI components with specialized interfaces
- **Enhanced Reporting**: Detailed test execution metrics with multiple report formats
- **Auto-Healing**: Element recovery and alternative locator strategies

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

Configure the framework using the `framework.properties` file in `src/test/resources`:

```properties
# Browser Configuration
browser=chrome
browser.headless=false

# Test Execution Configuration
execution.parallel=true
execution.threadCount=4
execution.timeout=10

# Logging Configuration
logging.level=INFO
```

### Basic Usage

Create a test class with methods annotated with `@Test`:

```java
public class LoginTest {
    @Test
    public void testSuccessfulLogin(WebDriver driver) {
        driver.get("https://example.com/login");
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).sendKeys("password");
        driver.findElement(By.id("login-button")).click();
        
        WebElement welcomeMessage = driver.findElement(By.id("welcome"));
        assertThat(welcomeMessage.getText(), equalTo("Welcome, Admin!"));
    }
    
    @Test
    @SingleThreaded
    public void testThatShouldRunSequentially(WebDriver driver) {
        // This test will run in single-threaded mode
    }
}
```

## Documentation

Additional documentation:
- [Hamcrest Assertions Guide](docs/hamcrest_assertions.md)

## License

This project is licensed under the MIT License.