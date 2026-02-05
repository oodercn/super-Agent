# Contributing to ooder

Thank you for your interest in contributing to OOD UI Library! This document provides guidelines and instructions for contributing to the project.

## 📋 Table of Contents
- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Development Workflow](#development-workflow)
- [Code Standards](#code-standards)
- [Commit Guidelines](#commit-guidelines)
- [Pull Request Process](#pull-request-process)
- [Testing](#testing)
- [Documentation](#documentation)
- [Community](#community)

## 📜 Code of Conduct

Please read and follow our [Code of Conduct](./CODE_OF_CONDUCT.md). We are committed to providing a welcoming and inclusive environment for all contributors.

## 🚀 Getting Started

### Prerequisites
- Node.js 14+ and npm/yarn
- Basic understanding of ES6 modules
- Familiarity with the OOD framework (helpful but not required)

### Setting Up Development Environment
1. Fork the repository
2. Clone your fork:
   ```bash
   git clone https://github.com/YOUR-USERNAME/A2UI2.git
   cd A2UI2
   ```
3. Install dependencies:
   ```bash
   npm install
   ```
4. Start the development server:
   ```bash
   npm run dev
   ```
5. Open http://localhost:8080 in your browser

## 🔧 Development Workflow

### Branch Strategy
- `main`: Stable, production-ready code
- `develop`: Development branch for features
- `feature/*`: New features or enhancements
- `bugfix/*`: Bug fixes
- `docs/*`: Documentation updates

### Creating a Feature
1. Create a feature branch from `develop`:
   ```bash
   git checkout develop
   git pull origin develop
   git checkout -b feature/your-feature-name
   ```
2. Make your changes
3. Write or update tests
4. Update documentation
5. Run tests and ensure they pass
6. Commit your changes following commit guidelines
7. Push to your fork
8. Create a Pull Request

## 📝 Code Standards

### ES6 Module Migration Standards
When migrating legacy code to ES6 modules:

1. **File Location**: Place migrated modules in `ood/modules/`
2. **Naming Convention**: Use PascalCase for class names
3. **Export Style**: Use named exports for primary functionality
4. **Decorator Usage**: Apply `@register()` decorator for global registration
5. **Import Paths**: Use relative imports within the `ood/` directory

### Example Migration Template
```javascript
import { register } from '../es6/shim.js';
import { LegacyDependency } from '../js/LegacyModule.js';

/**
 * Description of the module
 * @class MyModule
 */
@register('ood.MyModule')
export class MyModule {
  /**
   * Static method example
   * @static
   */
  static staticMethod() {
    // Implementation
  }

  /**
   * Instance method example
   */
  instanceMethod() {
    // Implementation, may use LegacyDependency
  }
}
```

### Code Quality Rules
- Use ESLint with the provided configuration
- No console.log in production code (debug mode only)
- Prefer const over let, avoid var
- Use meaningful variable and function names
- Add JSDoc comments for public APIs
- Keep functions small and focused (under 50 lines)

## 💾 Commit Guidelines

### Commit Message Format
```
type(scope): subject

body

footer
```

### Types
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code formatting, missing semi-colons, etc.
- `refactor`: Code change that neither fixes a bug nor adds a feature
- `perf`: Performance improvement
- `test`: Adding or fixing tests
- `chore`: Changes to build process or auxiliary tools

### Examples
```
feat(Button): add loading state support

- Add loading prop to Button component
- Implement spinner animation
- Update documentation with examples

Closes #123
```

```
fix(Dialog): correct close button positioning

- Adjust CSS for close button
- Add responsive handling
- Update test cases

Resolves #456
```

## 🔍 Pull Request Process

### PR Checklist
Before submitting a PR, ensure:

1. ✅ Code follows project standards
2. ✅ Tests pass (`npm test`)
3. ✅ ESLint passes (`npm run lint`)
4. ✅ Documentation is updated
5. ✅ Commit messages follow guidelines
6. ✅ No conflicts with base branch

### PR Description Template
```markdown
## Description
Brief description of the changes

## Related Issues
Fixes # (issue number)

## Type of Change
- [ ] Bug fix (non-breaking change)
- [ ] New feature (non-breaking change)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update

## Testing
- [ ] Unit tests added/updated
- [ ] Integration tests pass
- [ ] End-to-end tests pass

## Screenshots (if applicable)

## Additional Notes
Any additional information
```

### Review Process
1. PR is automatically tested
2. Maintainers review code and provide feedback
3. Address review comments
4. Once approved, PR is merged
5. Changes are automatically deployed (if applicable)

## 🧪 Testing

### Test Categories
1. **Unit Tests**: Test individual functions/methods
2. **Integration Tests**: Test module interactions
3. **E2E Tests**: Test full user workflows
4. **Compatibility Tests**: Ensure legacy compatibility

### Writing Tests
```javascript
// Example unit test for Button component
import { Button } from '../../src/modules/Button.js';

describe('Button', () => {
  test('should create button with text', () => {
    const button = new Button({ text: 'Click me' });
    expect(button.getText()).toBe('Click me');
  });

  test('should handle click events', () => {
    const mockClick = jest.fn();
    const button = new Button({ onClick: mockClick });
    button.click();
    expect(mockClick).toHaveBeenCalled();
  });
});
```

### Running Tests
```bash
npm test                    # Run all tests
npm run test:unit          # Unit tests only
npm run test:integration   # Integration tests
npm run test:e2e           # End-to-end tests
npm run test:coverage      # Coverage report
```

## 📚 Documentation

### Documentation Standards
- All public APIs must have JSDoc comments
- Examples should be practical and testable
- Keep documentation in sync with code changes
- Use clear, concise language

### Adding Documentation
1. Update API references in JSDoc comments
2. Add examples in `docs/examples/`
3. Update relevant guides in `docs/guides/`
4. Ensure cross-references are correct

## 👥 Community

### Getting Help
- Check the [documentation](./docs/) first
- Search existing issues and discussions
- Join our community chat (if available)
- Ask questions in discussions

### Reporting Issues
When reporting issues, include:
1. Clear description of the problem
2. Steps to reproduce
3. Expected vs actual behavior
4. Environment details (browser, OS, Node version)
5. Relevant code snippets

### Feature Requests
For feature requests, provide:
1. Detailed description of the feature
2. Use cases and benefits
3. Proposed implementation approach (if known)
4. Potential challenges

## 📊 Contribution Recognition

### Contributor List
All contributors are listed in the [AUTHORS](./AUTHORS) file. Make sure to add yourself if you're a new contributor.

### Contributor Badges
- First-time contributor: 🎉
- Significant contribution: 🚀
- Documentation hero: 📚
- Bug hunter: 🐛

## 🛠️ Development Tools

### Useful Scripts
```bash
# Build scripts
npm run build              # Production build
npm run build:dev          # Development build
npm run build:watch       # Watch mode

# Code quality
npm run lint              # ESLint check
npm run lint:fix          # Auto-fix lint issues

# Migration tools
npm run migrate:help      # Show migration help
npm run migrate:cookies   # Migrate Cookies.js
npm run migrate:all       # Migrate all modules
```

### Editor Configuration
Recommended VS Code extensions:
- ESLint
- Prettier
- JSDoc comments
- Code Spell Checker

## 📈 Contribution Metrics

We track contributions in the following areas:
- Code contributions (lines of code, features)
- Bug fixes and issue resolution
- Documentation improvements
- Test coverage improvements
- Community support and mentoring

## 🎉 Thank You!

Thank you for considering contributing to OOD UI Library. Your contributions help make this project better for everyone!

---

*This document is a living document. If you have suggestions for improving it, please submit a PR or open an issue.*