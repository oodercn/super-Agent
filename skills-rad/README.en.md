# ooder-agent-rad

ooder-agent-rad is a visual designer for ooderAgent A2UI, allowing users to visually edit frontend pages with deep AI participation in design through local skills bridging.

## Features

- **Visual Design**: Intuitive drag-and-drop interface for designing frontend pages
- **Component-based Development**: Rich predefined component library with support for custom components
- **Responsive Design**: Automatically adapts to different device sizes
- **Deep AI Participation**: AI-assisted design and code generation through local skills bridging
- **Real-time Preview**: WYSIWYG design experience
- **Code Generation**: Automatically generates maintainable frontend code
- **Modular Architecture**: Easy to extend and secondary develop
- **Multi-language Support**: Supports Chinese, English, Japanese, Russian, Traditional Chinese, etc.
- **Dynamic Themes**: Supports multiple built-in themes with custom theme options
- **API Integration**: Supports integration with various API services
- **File Version Control**: Supports file version management and history
- **Team Collaboration**: Supports multi-person collaborative development
- **Plugin System**: Supports plugin extension functionality

## Technology Stack

- **Backend**: Java 1.8+, Spring Boot 2.7.0
- **Frontend**: JavaScript, HTML5, CSS3, SVG
- **Template Engine**: FreeMarker 2.3.31
- **Build Tool**: Maven 3.0+
- **Logging Framework**: SLF4J 1.7.25
- **JSON Processing**: FastJSON 1.2.62
- **HTTP Client**: Apache HttpClient 4.5.13
- **MQTT Client**: Eclipse Paho 1.2.5
- **Netty**: 4.1.53.Final
- **Code Parsing**: JavaParser 3.18.0
- **License**: GNU General Public License v3.0

## Installation and Running

### System Requirements

- JDK 1.8 or higher
- Maven 3.0 or higher
- Modern browser with HTML5 support

### Build the Project

1. Clone or download the project code
2. Navigate to the project root directory
3. Execute the Maven build command:

```bash
mvn clean package
```

4. After successful build, an executable jar file `ooder-agent-rad.jar` will be generated in the `target` directory

### Run the Project

#### Method 1: Directly run the jar file

```bash
java -jar target/ooder-agent-rad.jar
```

#### Method 2: Run using Maven Spring Boot plugin

```bash
mvn spring-boot:run
```

#### Access the Application

Open a browser and visit `http://localhost:8080`

### Configuration

The main configuration file of the project is located at `src/main/resources/application.properties`, and you can modify the following configurations as needed:

- `server.port`: Server port, default is 8080
- `spring.application.name`: Application name
- Other Spring Boot related configurations

### Development Mode

In development mode, you can use the following command to start the project with hot deployment support:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Project Structure

```
ooder-agent-rad/
├── components/         # Component-related files
├── docs/               # Documentation directory
├── form/               # Form design related files
├── lib/                # Dependent libraries
├── ooder-pro/          # Professional version features
├── ooder-skills/       # AI skills integration
├── src/                # Source code
│   ├── main/           # Main code
│   │   ├── java/       # Java backend code
│   │   │   └── net/ooder/  # Main package structure
│   │   └── resources/  # Resource files
│   │       ├── static/ # Static resources (CSS, JS, images, etc.)
│   │       │   ├── Locale/ # Multi-language resources
│   │       │   ├── RAD/     # RAD-related resources
│   │       │   ├── css/     # Style files
│   │       │   ├── js/      # JavaScript files
│   │       │   └── ood/     # ooder core resources
│   │       └── templates/   # FreeMarker templates
│   └── test/           # Test code
├── LICENSE             # GPL3.0 license
├── README.md           # Project description document
├── README.en.md        # English project description document
└── pom.xml             # Maven configuration file
```

## Usage Instructions

### Create a New Project

1. Open the application and visit `http://localhost:8080`
2. Enter username and password on the login page (default configuration can be found in application.properties file)
3. After entering the main interface, click "File" -> "New Project" in the top menu bar
4. Fill in the project name, description, access type and other configurations in the pop-up dialog
5. Select a project template (optional)
6. Click the "Create" button to complete project creation

### Design Pages

1. Select a project in the left project tree
2. Right-click the "Pages" folder and select "New Page"
3. Fill in the page name and related configurations
4. After entering the design interface, drag components from the left toolbar to the middle design canvas
5. Configure various properties of components in the right property panel
6. Use the functions in the top menu bar for saving, undoing, redoing, etc.
7. During design, you can click the "Preview" button at any time to view the actual effect

### Component Library Usage

- **Basic Components**: Includes buttons, text boxes, labels, images and other basic UI elements
- **Layout Components**: Includes containers, grids, tables and other layout elements
- **Form Components**: Includes various form controls such as drop-down boxes, checkboxes, radio buttons, etc.
- **Data Components**: Includes charts, data lists, tree structures and other data display components
- **Advanced Components**: Includes rich text editors, maps, video players and other advanced functional components

### Preview and Publish

1. In the design interface, click the "Preview" button in the top menu bar
2. View the design effect in the preview window, supporting preview of different device sizes
3. Click the "Publish" button and select the publishing format (such as HTML, Vue, React, etc.)
4. Select the publishing target location, supporting local file system or remote server
5. Click the "Confirm Publish" button to generate the final code

### File Version Control

1. Right-click the file that needs version control in the project tree
2. Select "Version Management" -> "Create Version"
3. Fill in the version description
4. Click the "Confirm" button to create the version
5. You can view and restore historical versions in "Version History"

### API Integration

1. Click "Tools" -> "API Configuration" in the top menu bar
2. Add API services in the API configuration interface
3. Configure API URL, request method, parameters, etc.
4. Use API components in the design page to call configured APIs
5. You can test API connections and responses in the API test interface

### Theme Switching

1. Click "View" -> "Theme" in the top menu bar
2. Select a built-in theme, such as "Default", "Dark", "Blue", "Green", "Purple", etc.
3. You can customize theme colors and styles in the theme editor
4. Click the "Save Theme" button to save the custom theme

### Plugin Management

1. Click "Tools" -> "Plugin Management" in the top menu bar
2. View installed plugins in the plugin management interface
3. You can enable, disable or uninstall plugins
4. Click the "Install Plugin" button to install new plugins
5. Plugins can extend the functionality of the application, such as adding new components, integrating third-party services, etc.

## Secondary Development

ooder-agent-rad adopts a modular architecture, which is easy to extend and secondary develop. For detailed secondary development guidelines, please refer to the project's docs directory.

## License

This project adopts the GNU General Public License v3.0 open source protocol. See the [LICENSE](LICENSE) file for details.

## Contact

- Author: ooderTeam
- Email: Please contact us through GitHub Issues
- GitHub: [https://github.com/ooder/ooder-agent-rad](https://github.com/ooder/ooder-agent-rad)

## Acknowledgments

Thanks to all developers and users who have contributed to the ooder-agent-rad project!

## Changelog

### v1.0.0 (2026-01-24)

- Initial version release
- Implemented visual design functionality
- Supported component-based development
- Integrated AI-assisted design
- Implemented multi-language support
- Supported dynamic themes
- Implemented file version control
- Provided complete secondary development documentation

## Future Plans

- Enhance AI-assisted design capabilities
- Support code generation for more frontend frameworks
- Improve performance and user experience
- Add more built-in components and templates
- Enhance team collaboration functionality
- Support integration with more cloud services
- Provide mobile design support
- Enhance security and stability

## FAQ

### Q: How to solve build failure problems?
A: Please ensure that JDK and Maven versions meet the requirements, check if dependencies are correct, and rebuild after cleaning the Maven local repository.

### Q: How to add new language support?
A: Create a new language file in the `src/main/resources/static/Locale` directory and add translation content according to the format of existing language files.

### Q: How to improve application performance?
A: Performance can be improved by optimizing code, reducing resource file size, enabling caching, adjusting JVM parameters, etc.

### Q: How to backup and restore project data?
A: Project data can be backed up by backing up the `form` directory and database files. To restore, copy the backup files to the corresponding location.

### Q: How to report bugs and make suggestions?
A: Please report bugs and make suggestions in GitHub Issues, providing detailed problem descriptions and reproduction steps.
