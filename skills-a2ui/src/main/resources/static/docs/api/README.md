# ooder API Documentation

Welcome to the complete API documentation for ooder. This section provides detailed information about all components, methods, and properties available in the library.

## 📋 Navigation

- **[Core Components](#core-components)**: Basic UI building blocks
- **[Container Components](#container-components)**: Layout and organization
- **[Form Components](#form-components)**: Input and data collection
- **[Data Display](#data-display)**: Visualization and presentation
- **[Navigation](#navigation)**: Menus, tabs, and flow control
- **[Utility Components](#utility-components)**: Supporting elements

## 🏗️ Core Components

### Button (`ood.UI.Button`)
```javascript
// Basic usage
const button = new ood.UI.Button({
  text: 'Click me',
  style: 'primary',
  onClick: () => console.log('Clicked')
});

// Methods
button.setText('New text');
button.disable();
button.enable();
button.click(); // Programmatically trigger click
```

### Input (`ood.UI.Input`)
```javascript
// Text input
const input = new ood.UI.Input({
  type: 'text',
  placeholder: 'Enter text...',
  value: '',
  onChange: (value) => console.log('Value changed:', value)
});

// Methods
input.setValue('New value');
input.focus();
input.clear();
```

### Label (`ood.UI.Label`)
```javascript
// Simple label
const label = new ood.UI.Label({
  text: 'Username:',
  for: 'username-input'
});

// Methods
label.setText('New label text');
label.setFor('new-input-id');
```

## 📦 Container Components

### Panel (`ood.UI.Panel`)
```javascript
// Panel with title
const panel = new ood.UI.Panel({
  title: 'User Settings',
  collapsible: true,
  collapsed: false
});

// Methods
panel.collapse();
panel.expand();
panel.setTitle('New Title');
```

### Dialog (`ood.UI.Dialog`)
```javascript
// Modal dialog
const dialog = new ood.UI.Dialog({
  title: 'Confirm Action',
  content: 'Are you sure you want to proceed?',
  buttons: ['OK', 'Cancel'],
  modal: true
});

// Methods
dialog.open();
dialog.close();
dialog.setTitle('New Title');
```

### Tabs (`ood.UI.Tabs`)
```javascript
// Tabbed interface
const tabs = new ood.UI.Tabs({
  tabs: [
    { title: 'General', content: 'General settings...' },
    { title: 'Advanced', content: 'Advanced options...' }
  ],
  activeTab: 0
});

// Methods
tabs.setActiveTab(1);
tabs.addTab({ title: 'New', content: 'New content' });
tabs.removeTab(0);
```

## 📝 Form Components

### FormLayout (`ood.UI.FormLayout`)
```javascript
// Form with layout
const form = new ood.UI.FormLayout({
  fields: [
    { label: 'Name', type: 'text', name: 'name' },
    { label: 'Email', type: 'email', name: 'email' }
  ],
  onSubmit: (data) => console.log('Form data:', data)
});

// Methods
form.validate();
form.reset();
form.getData();
```

### CheckBox (`ood.UI.CheckBox`)
```javascript
// Checkbox component
const checkbox = new ood.UI.CheckBox({
  text: 'Accept terms',
  checked: false,
  onChange: (checked) => console.log('Checked:', checked)
});

// Methods
checkbox.check();
checkbox.uncheck();
checkbox.toggle();
```

### RadioBox (`ood.UI.RadioBox`)
```javascript
// Radio button
const radio = new ood.UI.RadioBox({
  text: 'Option A',
  name: 'options',
  value: 'a',
  onChange: (value) => console.log('Selected:', value)
});

// Methods
radio.select();
radio.deselect();
radio.isSelected();
```

## 📊 Data Display

### List (`ood.UI.List`)
```javascript
// List component
const list = new ood.UI.List({
  items: ['Item 1', 'Item 2', 'Item 3'],
  selectable: true,
  onSelect: (item) => console.log('Selected item:', item)
});

// Methods
list.addItem('New Item');
list.removeItem(0);
list.clear();
list.getSelectedItems();
```

### TreeGrid (`ood.UI.TreeGrid`)
```javascript
// Tree grid component
const treeGrid = new ood.UI.TreeGrid({
  columns: [
    { field: 'name', title: 'Name', width: 200 },
    { field: 'type', title: 'Type', width: 100 }
  ],
  data: [
    { name: 'Root', type: 'folder', children: [
      { name: 'Child 1', type: 'file' }
    ]}
  ]
});

// Methods
treeGrid.expandAll();
treeGrid.collapseAll();
treeGrid.refresh();
```

## 🧭 Navigation

### MenuBar (`ood.UI.MenuBar`)
```javascript
// Menu bar component
const menuBar = new ood.UI.MenuBar({
  items: [
    { text: 'File', items: [
      { text: 'New', action: () => console.log('New') },
      { text: 'Open', action: () => console.log('Open') }
    ]}
  ]
});

// Methods
menuBar.addMenuItem('File', { text: 'Save', action: save });
menuBar.removeMenuItem('File', 'Save');
```

### TreeView (`ood.UI.TreeView`)
```javascript
// Tree view component
const treeView = new ood.UI.TreeView({
  data: [
    { text: 'Root', children: [
      { text: 'Child 1' }
    ]}
  ],
  onSelect: (node) => console.log('Selected node:', node)
});

// Methods
treeView.expandNode(node);
treeView.collapseNode(node);
treeView.getSelectedNode();
```

## 🛠️ Utility Components

### ProgressBar (`ood.UI.ProgressBar`)
```javascript
// Progress indicator
const progressBar = new ood.UI.ProgressBar({
  value: 50,
  max: 100,
  showLabel: true
});

// Methods
progressBar.setValue(75);
progressBar.increment(10);
progressBar.reset();
```

### ToolBar (`ood.UI.ToolBar`)
```javascript
// Toolbar component
const toolbar = new ood.UI.ToolBar({
  buttons: [
    { text: 'Save', icon: 'ri-save-line', action: save },
    { text: 'Print', icon: 'ri-printer-line', action: print }
  ]
});

// Methods
toolbar.addButton({ text: 'New', action: newItem });
toolbar.removeButton('Save');
```

## 📚 API Reference Structure

### Component Instance Methods
Each component provides standard methods:

#### Lifecycle Methods
- `render()` - Render component to DOM
- `destroy()` - Clean up resources
- `refresh()` - Update display

#### State Management
- `getState()` - Get current state
- `setState(state)` - Update state
- `reset()` - Reset to initial state

#### Event Handling
- `on(event, handler)` - Add event listener
- `off(event, handler)` - Remove event listener
- `emit(event, data)` - Trigger event

### Component Properties
Each component has configurable properties:

#### Common Properties
- `id` - Unique identifier
- `className` - CSS class names
- `style` - Inline styles
- `visible` - Visibility state

#### Component-Specific Properties
- Button: `text`, `icon`, `disabled`
- Input: `value`, `placeholder`, `type`
- Panel: `title`, `collapsible`, `collapsed`

## 🔧 Usage Examples

### Basic Component Creation
```javascript
// Create and render a button
const btn = new ood.UI.Button({
  text: 'Submit',
  style: 'primary',
  onClick: () => alert('Form submitted!')
});

// Add to DOM
document.getElementById('app').appendChild(btn.getElement());
```

### Event Handling
```javascript
// Listen for component events
const input = new ood.UI.Input({
  placeholder: 'Search...'
});

input.on('change', (value) => {
  console.log('Search query:', value);
  performSearch(value);
});

input.on('focus', () => {
  console.log('Input focused');
});
```

### Dynamic Updates
```javascript
// Update components dynamically
const label = new ood.UI.Label({ text: 'Status:' });

// Function to update based on status
function updateStatus(status) {
  label.setText(`Status: ${status}`);
  
  if (status === 'error') {
    label.setStyle('error');
  } else if (status === 'success') {
    label.setStyle('success');
  }
}
```

## 🧪 Testing

### Component Testing
```javascript
// Example test for Button component
describe('Button', () => {
  test('should create with text', () => {
    const button = new ood.UI.Button({ text: 'Test' });
    expect(button.getText()).toBe('Test');
  });

  test('should handle click', () => {
    const mockClick = jest.fn();
    const button = new ood.UI.Button({ onClick: mockClick });
    button.click();
    expect(mockClick).toHaveBeenCalled();
  });
});
```

### Integration Testing
```javascript
// Test component interactions
describe('Form Integration', () => {
  test('should submit form data', () => {
    const form = new ood.UI.FormLayout({
      fields: [{ name: 'test', type: 'text' }],
      onSubmit: (data) => {
        expect(data.test).toBe('value');
      }
    });
    
    form.getField('test').setValue('value');
    form.submit();
  });
});
```

## 🔍 Advanced Topics

### Custom Component Creation
```javascript
// Extend base component
class CustomButton extends ood.UI.Button {
  constructor(config) {
    super(config);
    this.customProperty = config.customProperty;
  }
  
  customMethod() {
    console.log('Custom method:', this.customProperty);
  }
}

// Usage
const customBtn = new CustomButton({
  text: 'Custom',
  customProperty: 'value'
});
```

### Performance Optimization
```javascript
// Lazy loading components
const LazyComponent = {
  load: async () => {
    const { Button } = await import('ooder');
    return Button;
  }
};

// Usage
const Button = await LazyComponent.load();
const btn = new Button({ text: 'Lazy loaded' });
```

## 📖 Further Reading

- **[Component Guides](../guides/components.md)** - Detailed component usage
- **[Best Practices](../guides/best-practices.md)** - Development guidelines
- **[Migration Guide](../../README-ES6-UPGRADE.md)** - Legacy to modern migration
- **[AI Learning](../../docs/ai-learning/README.md)** - AI-assisted learning framework

## 🆘 Support

- **Issue Tracker**: Report API documentation issues
- **Community Forum**: Ask questions and share knowledge
- **Contribution Guide**: Help improve the documentation

---

**Note**: This API documentation is a living document. If you find any issues or have suggestions for improvement, please contribute or open an issue.