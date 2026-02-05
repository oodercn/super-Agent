# ooder - Modern ES6 Module Edition

A comprehensive UI component library with full ES6 module support, built for modern web applications while maintaining backward compatibility with legacy codebases.

## 🚀 Features

### Modern Architecture
- **ES6 Module First**: Native ES6 module support with clean imports
- **Progressive Migration**: Seamless transition from legacy `ood.Class` system
- **Tree Shaking Ready**: Optimized for modern bundlers (Webpack, Rollup)

### Component Richness
- **60+ UI Components**: Button, Input, Dialog, TreeGrid, Tabs, and more
- **Responsive Design**: Mobile-first, fully responsive components
- **Theme System**: Extensive theming support with CSS custom properties

### Developer Experience
- **TypeScript Ready**: Full type definitions available
- **Comprehensive Documentation**: Detailed API references and guides
- **Built-in Testing**: Full test suite for quality assurance

## 📦 Installation

### NPM
```bash
npm install ooder
```

### Yarn
```bash
yarn add ooder
```

### CDN (Global Usage)
```html
<!-- Development Version -->
<script src="https://unpkg.com/ooder/dist/ood-compat.js"></script>

<!-- Production Version -->
<script src="https://unpkg.com/ooder/dist/ood-compat.min.js"></script>
```

## 🎯 Quick Start

### ES6 Module Usage
```javascript
import { Button, Input, Dialog } from 'ooder';

// Create a button
const btn = new Button({
  text: 'Click Me',
  onClick: () => console.log('Button clicked!')
});

// Create an input field
const input = new Input({
  placeholder: 'Enter text...',
  value: ''
});

// Create a dialog
const dialog = new Dialog({
  title: 'Welcome',
  content: 'Hello from ooder!',
  buttons: ['OK', 'Cancel']
});
```

### Legacy Compatibility Mode
```javascript
// Still works with legacy code
const btn = new ood.UI.Button({
  text: 'Legacy Button'
});
```

## 📁 Project Structure

```
A2UI2/
├── src/                          # Source code
│   ├── es6/                      # ES6 compatibility layer
│   │   ├── index.js             # Main entry point
│   │   ├── shim.js              # Global object bridging
│   │   └── registry.js          # Module registry
│   ├── modules/                  # ES6 modules (migration target)
│   │   └── Cookies.js           # Migrated example
│   └── js/                       # Legacy code (unchanged)
│       ├── UI.js                # UI core
│       └── 36+ modules          # Full functionality
├── dist/                         # Build outputs
│   ├── ood-es6.js               # ES6 bundle (development)
│   └── ood-es6.min.js           # ES6 bundle (production)
├── docs/                         # Documentation
├── examples/                     # Usage examples
├── tests/                        # Test files
└── package.json                  # Project configuration
```

## 🔧 Build & Development

### Development Server
```bash
npm run dev
# Open http://localhost:8080
```

### Production Build
```bash
npm run build
# Outputs to dist/ folder
```

### Code Quality
```bash
npm run lint              # ESLint check
npm run lint:fix          # ESLint auto-fix
npm run test              # Run test suite
npm run test:coverage     # Generate coverage report
```

## 📚 Documentation

### Available Documentation
- **[API Reference](./docs/api/)** - Complete API documentation
- **[Migration Guide](./README-ES6-UPGRADE.md)** - From legacy to ES6 modules
- **[Component Guide](./docs/guides/)** - Usage examples for each component
- **[Testing Guide](./TEST-GUIDE.md)** - How to test components
- **[AI-Assisted Learning](./docs/ai-learning/)** - Using AI for framework learning

### Quick Links
- [Getting Started](./docs/guides/getting-started.md)
- [Component Examples](./examples/)
- [Migration Examples](./ood/modules/Cookies.js)
- [Build Configuration](./webpack.config.js)

## 🤝 Contributing

We welcome contributions! Please read our [Contribution Guidelines](./CONTRIBUTING.md) before getting started.

### Ways to Contribute
- **Bug Reports**: Submit issues with detailed reproduction steps
- **Feature Requests**: Suggest new features or improvements
- **Code Contributions**: Submit pull requests for bug fixes or new features
- **Documentation**: Help improve docs, add examples, or translate
- **Testing**: Write tests, improve test coverage

### Development Workflow
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 🧪 Testing

### Test Structure
```
tests/
├── unit/                    # Unit tests
├── integration/            # Integration tests
├── e2e/                    # End-to-end tests
└── fixtures/               # Test data
```

### Running Tests
```bash
npm test                    # Run all tests
npm run test:unit          # Unit tests only
npm run test:integration   # Integration tests only
npm run test:e2e           # End-to-end tests
npm run test:coverage      # Generate coverage report
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](./LICENSE) file for details.

## 🙏 Acknowledgements

- Original OOD Framework Developers
- All contributors who have helped improve this library
- Modern web standards that make this migration possible

## 🚀 What's Next?

### Short-term Goals
- Complete migration of core UI components to ES6 modules
- Enhance test coverage to 90%+
- Improve documentation with more examples
- Add TypeScript type definitions for all components

### Long-term Vision
- Full TypeScript migration
- Web Components compatibility
- Framework-agnostic component architecture
- Enhanced accessibility (a11y) features

---

**Need Help?** Check out our [troubleshooting guide](./docs/troubleshooting.md) or [open an issue](https://github.com/your-username/ooder/issues).

**Found a Bug?** Please report it using our [issue template](./.github/ISSUE_TEMPLATE/bug_report.md).

**Have a Feature Request?** Use our [feature request template](./.github/ISSUE_TEMPLATE/feature_request.md).