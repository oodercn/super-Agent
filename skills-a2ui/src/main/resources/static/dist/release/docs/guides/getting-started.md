# Getting Started with ooder

This guide will help you get started with ooder, from installation to building your first application.

## 📦 Installation

### Prerequisites
- Node.js 14.0 or higher
- npm 6.0 or higher (or yarn, pnpm)
- Modern web browser (Chrome 80+, Firefox 75+, Safari 13+)

### Install via npm
```bash
npm install ooder
```

### Install via yarn
```bash
yarn add ooder
```

### Using CDN (for quick prototyping)
```html
<!-- Development version -->
<script src="https://unpkg.com/ooder/dist/ood-compat.js"></script>

<!-- Production version -->
<script src="https://unpkg.com/ooder/dist/ood-compat.min.js"></script>
```

## 🚀 Quick Setup

### Create a Simple HTML File
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ooder Demo</title>
    <link rel="stylesheet" href="https://unpkg.com/ooder/dist/styles.css">
</head>
<body>
    <div id="app"></div>
    
    <!-- Load ooder Library -->
    <script src="https://unpkg.com/ooder/dist/ood-compat.js"></script>
    <script>
        // Your code here
    </script>
</body>
</html>
```

### Using ES6 Modules (Recommended)
```javascript
// In your main JavaScript file
import { Button, Input, Dialog } from 'ooder';

// Or import specific components for better tree shaking
import Button from 'ooder/components/Button';
import Input from 'ooder/components/Input';
```

## 🛠️ Project Setup

### Using with Webpack
If you're using Webpack, ensure your configuration supports ES6 modules:

```javascript
// webpack.config.js
module.exports = {
  // ... other config
  module: {
    rules: [
      {
        test: /\.js$/,
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader',
          options: {
            presets: ['@babel/preset-env']
          }
        }
      }
    ]
  }
};
```

### Using with Vite
Vite works out of the box with ES6 modules:

```javascript
// vite.config.js
export default {
  // No special configuration needed
};
```

## 🎯 Your First Component

### Creating a Button
```javascript
import { Button } from 'ooder';

// Create a button instance
const myButton = new Button({
  text: 'Click Me',
  style: 'primary',
  onClick: () => {
    alert('Button clicked!');
  }
});

// Render the button
document.getElementById('app').appendChild(myButton.getElement());
```

### Creating an Input Field
```javascript
import { Input } from 'ooder';

const myInput = new Input({
  type: 'text',
  placeholder: 'Enter your name...',
  value: '',
  onChange: (value) => {
    console.log('Input changed:', value);
  }
});

// Add to DOM
document.getElementById('app').appendChild(myInput.getElement());
```

### Creating a Dialog
```javascript
import { Dialog } from 'ooder';

const myDialog = new Dialog({
  title: 'Welcome',
  content: 'Hello from ooder!',
  buttons: [
    {
      text: 'OK',
      action: () => {
        console.log('OK clicked');
        myDialog.close();
      }
    },
    {
      text: 'Cancel',
      action: () => {
        console.log('Cancel clicked');
        myDialog.close();
      }
    }
  ]
});

// Open the dialog
myButton.onClick = () => myDialog.open();
```

## 📁 Project Structure Example

Here's a recommended project structure for an ooder application:

```
my-app/
├── src/                          # Source code
│   ├── components/          # Your custom components
│   │   ├── Header.js
│   │   └── Sidebar.js
│   ├── pages/              # Page components
│   │   └── HomePage.js
│   ├── styles/             # CSS/SCSS files
│   │   └── main.scss
│   ├── utils/              # Utility functions
│   │   └── helpers.js
│   └── index.js           # Application entry point
├── public/                # Static assets
│   └── index.html
├── package.json
└── webpack.config.js
```

## 🔧 Development Workflow

### Start Development Server
If using the project's built-in development setup:

```bash
npm run dev
# Open http://localhost:8080
```

### Build for Production
```bash
npm run build
# Outputs to dist/ folder
```

### Run Tests
```bash
npm test
npm run test:coverage  # With coverage report
```

## 📖 Next Steps

1. **Explore Components**: Check out the [Component Guides](./components.md) for detailed usage
2. **Learn Concepts**: Read about [Basic Concepts](./basic-concepts.md) to understand the architecture
3. **Try Examples**: Look at the `examples/` directory for working demos
4. **Build Something**: Create a small project to practice

## 🆘 Troubleshooting

### Common Issues

#### "Module not found" error
Ensure you've installed the package correctly:
```bash
npm install ooder --save
```

#### "Cannot use import statement outside a module"
Add `type="module"` to your script tag:
```html
<script type="module" src="./app.js"></script>
```

#### Components not rendering
Check if the stylesheet is loaded:
```html
<link rel="stylesheet" href="path/to/ooder/styles.css">
```

### Getting Help
- Check the [API Reference](../api/README.md)
- Look at existing [examples](../../examples/)
- Open an issue on GitHub
- Ask in the community forum

---

**Congratulations!** You've successfully set up ooder. Continue to the next guide to learn about specific components and advanced features.