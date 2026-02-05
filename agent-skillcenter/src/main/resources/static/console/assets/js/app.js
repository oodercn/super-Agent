// SkillCenter Web控制台主脚本

// 导入公共组件
import { initTemplateSystem, renderWithTemplate } from './common/index.js';

// 当前页面
let currentPage = 'dashboard';

// API基础URL
const API_BASE_URL = '/api/skillcenter';

// 菜单配置
let menuConfig = null;

// 初始化页面
function init() {
    updateTimestamp();
    loadMenuConfig();
    loadPage('dashboard');
    
    // 初始化模板系统
    if (typeof initTemplateSystem === 'function') {
        initTemplateSystem();
    } else {
        console.log('模板系统初始化函数未定义');
    }
    
    setInterval(updateTimestamp, 60000); // 每分钟更新时间戳
}

// 加载菜单配置
async function loadMenuConfig() {
    try {
        const response = await fetch('menu-config.json');
        if (!response.ok) {
            throw new Error('菜单配置加载失败');
        }
        menuConfig = await response.json();
        renderMenu();
    } catch (error) {
        console.error('加载菜单配置错误:', error);
        // 加载失败时显示默认菜单
        renderDefaultMenu();
    }
}

// 渲染菜单
function renderMenu() {
    const navMenu = document.getElementById('nav-menu');
    navMenu.innerHTML = '';
    
    menuConfig.menu.forEach(item => {
        const menuItem = createMenuItem(item);
        navMenu.appendChild(menuItem);
        // 添加菜单分隔线
        if (item.id !== 'dashboard') {
            const divider = document.createElement('div');
            divider.className = 'menu-divider';
            navMenu.appendChild(divider);
        }
    });
    
    setupNavigation();
}

// 创建菜单项
function createMenuItem(menuItem) {
    const li = document.createElement('li');
    
    if (menuItem.children && menuItem.children.length > 0) {
        // 有子菜单的菜单项
        const a = document.createElement('a');
        a.href = `#${menuItem.id}`;
        a.innerHTML = `
            <i class="${menuItem.icon}"></i>
            ${menuItem.name}
            <span class="toggle-icon">›</span>
        `;
        
        // 处理点击事件
        a.addEventListener('click', function(e) {
            e.preventDefault();
            toggleSubmenu(this);
        });
        
        li.appendChild(a);
        
        // 创建子菜单
        const submenu = document.createElement('ul');
        submenu.className = 'submenu';
        
        menuItem.children.forEach(child => {
            const childItem = createMenuItem(child);
            submenu.appendChild(childItem);
        });
        
        li.appendChild(submenu);
    } else {
        // 无子菜单的菜单项
        const a = document.createElement('a');
        a.href = `#${menuItem.id}`;
        a.innerHTML = `
            <i class="${menuItem.icon}"></i>
            ${menuItem.name}
        `;
        
        // 处理点击事件
        a.addEventListener('click', function(e) {
            e.preventDefault();
            loadPage(menuItem.page || menuItem.id);
        });
        
        li.appendChild(a);
    }
    
    return li;
}

// 切换子菜单
function toggleSubmenu(element) {
    const submenu = element.nextElementSibling;
    const toggleIcon = element.querySelector('.toggle-icon');
    
    if (submenu && submenu.classList.contains('submenu')) {
        submenu.classList.toggle('hidden');
        if (toggleIcon) {
            toggleIcon.classList.toggle('collapsed');
        }
    }
}

// 渲染默认菜单（当菜单配置加载失败时）
function renderDefaultMenu() {
    const navMenu = document.getElementById('nav-menu');
    navMenu.innerHTML = '';
    
    // 默认菜单结构
    const defaultMenu = [
        {
            id: 'dashboard',
            name: '仪表盘',
            icon: 'ri-dashboard-line',
            page: 'dashboard'
        },
        {
            id: 'skill',
            name: '技能管理',
            icon: 'ri-lightbulb-line',
            page: 'skill'
        },
        {
            id: 'market',
            name: '市场管理',
            icon: 'ri-shopping-cart-line',
            page: 'market'
        },
        {
            id: 'execution',
            name: '执行管理',
            icon: 'ri-play-circle-line',
            page: 'execution'
        },
        {
            id: 'storage',
            name: '存储管理',
            icon: 'ri-database-line',
            page: 'storage'
        },
        {
            id: 'system',
            name: '系统管理',
            icon: 'ri-server-line',
            page: 'system'
        },
        {
            id: 'help',
            name: '帮助与支持',
            icon: 'ri-question-line',
            page: 'help'
        }
    ];
    
    defaultMenu.forEach((item, index) => {
        const li = document.createElement('li');
        const a = document.createElement('a');
        a.href = `#${item.id}`;
        a.innerHTML = `
            <i class="${item.icon}"></i>
            ${item.name}
        `;
        
        // 处理点击事件
        a.addEventListener('click', function(e) {
            e.preventDefault();
            loadPage(item.page || item.id);
        });
        
        li.appendChild(a);
        navMenu.appendChild(li);
        
        // 添加菜单分隔线
        if (index < defaultMenu.length - 1) {
            const divider = document.createElement('div');
            divider.className = 'menu-divider';
            navMenu.appendChild(divider);
        }
    });
    
    setupNavigation();
}

// 设置导航
function setupNavigation() {
    // 为所有菜单项添加点击事件
    const menuItems = document.querySelectorAll('.nav-menu a');
    menuItems.forEach(item => {
        item.addEventListener('click', function(e) {
            // 移除所有活动状态
            menuItems.forEach(mi => mi.classList.remove('active'));
            // 添加活动状态
            this.classList.add('active');
        });
    });
}

// 加载页面
function loadPage(pageName) {
    // 隐藏所有页面
    const pages = document.querySelectorAll('.page');
    pages.forEach(page => {
        page.classList.add('hidden');
    });
    
    // 显示指定页面
    const targetPage = document.getElementById(`page-${pageName}`);
    if (targetPage) {
        targetPage.classList.remove('hidden');
        currentPage = pageName;
        
        // 根据页面名称加载对应的数据
        loadPageData(pageName);
    }
}

// 加载页面数据
function loadPageData(pageName) {
    switch (pageName) {
        case 'dashboard':
            loadDashboardData();
            break;
        case 'skill':
            loadSkillData();
            break;
        case 'market':
            loadMarketData();
            break;
        case 'execution':
            loadExecutionData();
            break;
        case 'storage':
            loadStorageData();
            break;
        case 'system':
            loadSystemData();
            break;
        default:
            break;
    }
}

// 加载仪表盘数据
function loadDashboardData() {
    // 模拟API调用，后续替换为真实API
    console.log('加载仪表盘数据...');
    
    // 这里可以添加真实的API调用代码
    // fetch(`${API_BASE_URL}/dashboard/stats`)
    //     .then(response => response.json())
    //     .then(data => {
    //         // 更新仪表盘数据
    //     })
    //     .catch(error => {
    //         console.error('加载仪表盘数据失败:', error);
    //     });
}

// 加载技能数据
function loadSkillData() {
    // 模拟API调用，后续替换为真实API
    console.log('加载技能数据...');
    
    // 模拟技能数据
    const mockSkills = [
        {
            id: 1,
            name: '文本翻译',
            description: '将文本从一种语言翻译到另一种语言',
            category: '语言处理',
            version: '1.0.0'
        },
        {
            id: 2,
            name: '图像识别',
            description: '识别图像中的物体和场景',
            category: '计算机视觉',
            version: '1.2.0'
        },
        {
            id: 3,
            name: '数据分析',
            description: '分析和处理数据，生成统计结果',
            category: '数据处理',
            version: '2.0.0'
        }
    ];
    
    // 使用模板渲染技能列表
    const skillListContainer = document.querySelector('.skill-list-container');
    if (skillListContainer) {
        skillListContainer.innerHTML = '';
        
        mockSkills.forEach(skill => {
            // 使用模板渲染技能项
            renderWithTemplate('skillListItem', skill, skillListContainer, {
                execute: function() {
                    console.log('执行技能:', skill.name);
                },
                details: function() {
                    console.log('查看技能详情:', skill.name);
                }
            });
        });
    }
    
    // 这里可以添加真实的API调用代码
    // fetch(`${API_BASE_URL}/skills`)
    //     .then(response => response.json())
    //     .then(data => {
    //         // 使用模板渲染技能列表
    //         const skillListContainer = document.querySelector('.skill-list-container');
    //         if (skillListContainer) {
    //             skillListContainer.innerHTML = '';
    //             
    //             data.skills.forEach(skill => {
    //                 renderWithTemplate('skillListItem', skill, skillListContainer, {
    //                     execute: function() {
    //                         console.log('执行技能:', skill.name);
    //                     },
    //                     details: function() {
    //                         console.log('查看技能详情:', skill.name);
    //                     }
    //                 });
    //             });
    //         }
    //     })
    //     .catch(error => {
    //         console.error('加载技能数据失败:', error);
    //     });
}

// 加载市场数据
function loadMarketData() {
    // 模拟API调用，后续替换为真实API
    console.log('加载市场数据...');
    
    // 这里可以添加真实的API调用代码
    // fetch(`${API_BASE_URL}/market/skills`)
    //     .then(response => response.json())
    //     .then(data => {
    //         // 更新市场技能列表
    //     })
    //     .catch(error => {
    //         console.error('加载市场数据失败:', error);
    //     });
}

// 加载执行数据
function loadExecutionData() {
    // 模拟API调用，后续替换为真实API
    console.log('加载执行数据...');
    
    // 这里可以添加真实的API调用代码
    // fetch(`${API_BASE_URL}/execution/history`)
    //     .then(response => response.json())
    //     .then(data => {
    //         // 更新执行历史
    //     })
    //     .catch(error => {
    //         console.error('加载执行数据失败:', error);
    //     });
}

// 加载存储数据
function loadStorageData() {
    // 模拟API调用，后续替换为真实API
    console.log('加载存储数据...');
    
    // 这里可以添加真实的API调用代码
    // fetch(`${API_BASE_URL}/storage/status`)
    //     .then(response => response.json())
    //     .then(data => {
    //         // 更新存储状态
    //     })
    //     .catch(error => {
    //         console.error('加载存储数据失败:', error);
    //     });
}

// 加载系统数据
function loadSystemData() {
    // 模拟API调用，后续替换为真实API
    console.log('加载系统数据...');
    
    // 这里可以添加真实的API调用代码
    // fetch(`${API_BASE_URL}/system/status`)
    //     .then(response => response.json())
    //     .then(data => {
    //         // 更新系统状态
    //     })
    //     .catch(error => {
    //         console.error('加载系统数据失败:', error);
    //     });
}

// 更新时间戳
function updateTimestamp() {
    const now = new Date();
    const timestamp = document.getElementById('timestamp');
    if (timestamp) {
        timestamp.textContent = now.toLocaleString('zh-CN');
    }
}

// 技能管理相关函数
function showSkillList() {
    // 显示技能列表
    document.getElementById('skill-list-section').classList.remove('hidden');
}

function showSkillPublishForm() {
    // 显示发布技能表单
    alert('发布技能功能开发中...');
}

// 市场管理相关函数
function searchMarketSkills() {
    // 搜索市场技能
    const searchTerm = document.querySelector('input[placeholder="搜索技能..."]').value;
    console.log('搜索市场技能:', searchTerm);
    // 这里可以添加真实的API调用代码
}

// 执行管理相关函数
function viewExecutionDetail(executionId) {
    // 查看执行详情
    console.log('查看执行详情:', executionId);
    // 这里可以添加真实的API调用代码
}

// 存储管理相关函数
function backupStorage() {
    // 备份存储
    console.log('备份存储数据...');
    // 这里可以添加真实的API调用代码
    alert('备份存储功能开发中...');
}

function cleanStorage() {
    // 清理存储
    console.log('清理存储数据...');
    // 这里可以添加真实的API调用代码
    alert('清理存储功能开发中...');
}

function restoreStorage() {
    // 恢复存储
    console.log('恢复存储数据...');
    // 这里可以添加真实的API调用代码
    alert('恢复存储功能开发中...');
}

// 系统管理相关函数
function restartSystem() {
    // 重启系统
    console.log('重启系统...');
    // 这里可以添加真实的API调用代码
    if (confirm('确定要重启系统吗？')) {
        alert('重启系统功能开发中...');
    }
}

function shutdownSystem() {
    // 关闭系统
    console.log('关闭系统...');
    // 这里可以添加真实的API调用代码
    if (confirm('确定要关闭系统吗？')) {
        alert('关闭系统功能开发中...');
    }
}

// 页面加载完成后初始化
window.onload = init;