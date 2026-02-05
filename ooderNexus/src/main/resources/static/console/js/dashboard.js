let currentPage = 'dashboard';
let menuConfig = null;
let agentListManager = null;

function init() {
    updateTimestamp();
    loadMenuConfig();
    initListManagers();
    loadDashboard();
    setInterval(updateTimestamp, 60000);
}

function initListManagers() {
    agentListManager = new ListManager({
        resource: 'agents',
        page: 1,
        pageSize: 10,
        sortBy: 'id',
        sortOrder: 'asc',
        onLoad: function(response) {
            console.log('Agent data loaded:', response);
        },
        onError: function(error) {
            console.error('Error loading agent data:', error);
            utils.showNotification('加载Agent数据失败', 'error');
        }
    });
}

async function loadMenuConfig() {
    try {
        let response = await fetch('../menu-config.json');
        if (!response.ok) {
            response = await fetch('/console/menu-config.json');
            if (!response.ok) {
                throw new Error('菜单配置加载失败');
            }
        }
        menuConfig = await response.json();
        renderMenu();
    } catch (error) {
        console.error('加载菜单配置错误:', error);
        renderDefaultMenu();
    }
}

function renderMenu() {
    const navMenu = document.getElementById('nav-menu');
    navMenu.innerHTML = '';
    
    menuConfig.menu.forEach(item => {
        if (item.status === 'implemented' || item.id === 'dashboard') {
            const menuItem = createMenuItem(item);
            navMenu.appendChild(menuItem);
            if (item.id !== 'dashboard') {
                const divider = document.createElement('div');
                divider.className = 'menu-divider';
                navMenu.appendChild(divider);
            }
        }
    });
    
    setupNavigation();
}

function createMenuItem(menuItem) {
    const li = document.createElement('li');
    
    if (menuItem.children && menuItem.children.length > 0) {
        const a = document.createElement('a');
        a.href = `#${menuItem.id}`;
        a.innerHTML = `
            <i class="${menuItem.icon}"></i>
            ${menuItem.name}
            <span class="toggle-icon">›</span>
        `;
        
        a.addEventListener('click', function(e) {
            e.preventDefault();
            const toggleIcon = this.querySelector('.toggle-icon');
            const submenu = this.nextElementSibling;
            
            if (submenu) {
                submenu.classList.toggle('hidden');
                toggleIcon.classList.toggle('collapsed');
            }
        });
        
        li.appendChild(a);
        
        const submenu = document.createElement('ul');
        submenu.className = 'submenu';
        
        menuItem.children.forEach(childItem => {
            if (childItem.status === 'implemented') {
                const childLi = createMenuItem(childItem);
                submenu.appendChild(childLi);
            }
        });
        
        li.appendChild(submenu);
    } else {
        const a = document.createElement('a');
        if (menuItem.url) {
            a.href = menuItem.url;
        } else {
            a.href = `#${menuItem.id}`;
            a.setAttribute('data-page', menuItem.page || menuItem.id);
        }
        a.innerHTML = `
            <i class="${menuItem.icon}"></i>
            ${menuItem.name}
        `;
        li.appendChild(a);
    }
    
    return li;
}

function renderDefaultMenu() {
    const navMenu = document.getElementById('nav-menu');
    navMenu.innerHTML = `
        <li><a href="../index.html" class="active"><i class="ri-dashboard-line"></i> 仪表盘</a></li>
        <li><a href="../pages/system/request-management.html"><i class="ri-computer-line"></i> 系统管理</a></li>
        <li><a href="../pages/network/route-management.html"><i class="ri-wifi-line"></i> 网络管理</a></li>
        <li><a href="../pages/terminal/terminal-management.html"><i class="ri-terminal-line"></i> 终端管理</a></li>
        <li><a href="../pages/config/service-config.html"><i class="ri-settings-3-line"></i> 配置中心</a></li>
    `;
    setupNavigation();
}

function setupNavigation() {
    const navLinks = document.querySelectorAll('.nav-menu a');
    navLinks.forEach(link => {
        if (link.hasAttribute('href') && link.getAttribute('href') !== '#' && link.getAttribute('href') !== '') {
            const href = link.getAttribute('href');
            if (href.startsWith('pages/')) {
            } else if (!href.startsWith('http')) {
                link.setAttribute('href', `../${href}`);
            }
        }
        else if (link.hasAttribute('data-page') && link.getAttribute('href') === '#') {
            link.addEventListener('click', function(e) {
                e.preventDefault();
                navLinks.forEach(l => l.classList.remove('active'));
                this.classList.add('active');
            });
        }
    });
}

async function loadDashboard() {
    try {
        const loader = utils.showLoader('加载仪表盘数据...');
        
        setTimeout(() => {
            document.getElementById('system-status').textContent = '正常';
            document.getElementById('network-count').textContent = '12';
            document.getElementById('cpu-usage').textContent = '25%';
            document.getElementById('memory-usage').textContent = '45%';
            document.getElementById('disk-usage').textContent = '32%';
            document.getElementById('system-load').textContent = '0.8';
            document.getElementById('request-rate').textContent = '1000/秒';
            document.getElementById('response-time').textContent = '120ms';
            document.getElementById('active-tasks').textContent = '15';
            document.getElementById('system-temp').textContent = '45°C';
            document.getElementById('network-bandwidth').textContent = '2.5Gbps';
            document.getElementById('health-score').textContent = '95';
        }, 500);
        
        generateNetworkTopology();
        await loadAgentData();
        
        loader.hide();
        utils.showNotification('仪表盘数据加载成功', 'success');
    } catch (error) {
        console.error('加载仪表盘数据失败:', error);
        utils.showNotification('加载仪表盘数据失败', 'error');
    }
}

async function loadAgentData() {
    try {
        await agentListManager.load();
        const agentData = agentListManager.getCurrentPageData();
        console.log('Agent data:', agentData);
    } catch (error) {
        console.error('加载Agent数据失败:', error);
        throw error;
    }
}

function generateNetworkTopology() {
    const topologyContainer = document.getElementById('network-topology');
    if (!topologyContainer) {
        console.error('Network topology container not found');
        return;
    }
    
    topologyContainer.innerHTML = '';
    
    const containerWidth = topologyContainer.clientWidth || 800;
    const containerHeight = 320;
    const centerX = containerWidth / 2;
    const centerY = containerHeight / 2;
    
    const nodes = [
        { id: 'mcp', name: 'MCP', x: centerX, y: centerY - 80 },
        { id: 'route1', name: 'Route1', x: centerX - 200, y: centerY - 40 },
        { id: 'route2', name: 'Route2', x: centerX - 200, y: centerY + 40 },
        { id: 'end1', name: 'End1', x: centerX + 200, y: centerY - 60 },
        { id: 'end2', name: 'End2', x: centerX + 200, y: centerY },
        { id: 'end3', name: 'End3', x: centerX + 200, y: centerY + 60 },
        { id: 'end4', name: 'End4', x: centerX + 200, y: centerY + 120 }
    ];
    
    const links = [
        { source: 'mcp', target: 'route1' },
        { source: 'mcp', target: 'route2' },
        { source: 'route1', target: 'end1' },
        { source: 'route1', target: 'end2' },
        { source: 'route2', target: 'end3' },
        { source: 'route2', target: 'end4' }
    ];
    
    nodes.forEach(node => {
        const nodeElement = document.createElement('div');
        nodeElement.className = 'topology-node';
        nodeElement.style.left = `${node.x - 32}px`;
        nodeElement.style.top = `${node.y - 32}px`;
        nodeElement.textContent = node.name;
        nodeElement.title = node.name;
        topologyContainer.appendChild(nodeElement);
    });
    
    links.forEach(link => {
        const sourceNode = nodes.find(n => n.id === link.source);
        const targetNode = nodes.find(n => n.id === link.target);
        
        if (sourceNode && targetNode) {
            const linkElement = document.createElement('div');
            linkElement.className = 'topology-link';
            
            const dx = targetNode.x - sourceNode.x;
            const dy = targetNode.y - sourceNode.y;
            const length = Math.sqrt(dx * dx + dy * dy);
            const angle = Math.atan2(dy, dx) * 180 / Math.PI;
            
            linkElement.style.width = `${length}px`;
            linkElement.style.left = `${sourceNode.x}px`;
            linkElement.style.top = `${sourceNode.y}px`;
            linkElement.style.transform = `rotate(${angle}deg)`;
            
            topologyContainer.appendChild(linkElement);
        }
    });
}

function updateTimestamp() {
    const now = new Date();
    document.getElementById('timestamp').textContent = utils.formatTimestamp(now.getTime());
}

window.onload = init;
