// 当前选中的API提供商
let selectedProvider = null;

// API提供商配置
const providers = {
    alphavantage: {
        name: 'Alpha Vantage',
        baseUrl: 'https://www.alphavantage.co/query',
        docs: 'https://www.alphavantage.co/documentation/'
    },
    finnhub: {
        name: 'Finnhub',
        baseUrl: 'https://finnhub.io/api/v1',
        docs: 'https://finnhub.io/docs/api'
    },
    polygon: {
        name: 'Polygon.io',
        baseUrl: 'https://api.polygon.io/v2',
        docs: 'https://polygon.io/docs/stocks/getting-started'
    }
};

// 选择API提供商
function selectProvider(providerId) {
    selectedProvider = providerId;
    
    // 移除所有选中状态
    document.querySelectorAll('.api-provider').forEach(provider => {
        provider.classList.remove('selected');
    });
    
    // 添加当前选中状态
    event.target.classList.add('selected');
    
    // 填充API基础URL
    const provider = providers[providerId];
    if (provider) {
        document.getElementById('api-base-url').value = provider.baseUrl;
    }
}

// 测试API连接
async function testApiConnection() {
    const apiKey = document.getElementById('api-key').value;
    const apiBaseUrl = document.getElementById('api-base-url').value;
    const symbol = document.getElementById('symbol').value;
    
    if (!selectedProvider) {
        alert('请先选择API提供商');
        return;
    }
    
    try {
        let stockData;
        
        if (selectedProvider === 'alphavantage') {
            // Alpha Vantage API测试
            const response = await fetch(`${apiBaseUrl}?function=GLOBAL_QUOTE&symbol=${symbol}&apikey=${apiKey}`);
            if (!response.ok) {
                throw new Error('API调用失败');
            }
            stockData = await response.json();
        } else if (selectedProvider === 'finnhub') {
            // Finnhub API测试
            const response = await fetch(`${apiBaseUrl}/quote?symbol=${symbol}&token=${apiKey}`);
            if (!response.ok) {
                throw new Error('API调用失败');
            }
            stockData = await response.json();
        } else if (selectedProvider === 'polygon') {
            // Polygon.io API测试
            const response = await fetch(`${apiBaseUrl}/aggs/ticker/${symbol}/prev?apiKey=${apiKey}`);
            if (!response.ok) {
                throw new Error('API调用失败');
            }
            stockData = await response.json();
        }
        
        // 显示成功消息
        const stockPreview = document.getElementById('stock-preview');
        let stockInfo = '';
        
        if (selectedProvider === 'alphavantage' && stockData['Global Quote']) {
            const quote = stockData['Global Quote'];
            const change = parseFloat(quote['09. change']);
            const changePercent = parseFloat(quote['10. change percent']);
            stockInfo = `
                <h3>${symbol} - 实时价格</h3>
                <p>当前价格: $${quote['05. price']}</p>
                <p>开盘价: $${quote['02. open']}</p>
                <p>最高价: $${quote['03. high']}</p>
                <p>最低价: $${quote['04. low']}</p>
                <p>昨日收盘价: $${quote['08. previous close']}</p>
                <p class="${change >= 0 ? 'stock-up' : 'stock-down'}">涨跌: ${change >= 0 ? '+' : ''}${change} (${changePercent >= 0 ? '+' : ''}${changePercent}%)</p>
                <p>成交量: ${quote['06. volume']}</p>
            `;
        } else if (selectedProvider === 'finnhub') {
            const change = stockData.c - stockData.pc;
            const changePercent = (change / stockData.pc) * 100;
            stockInfo = `
                <h3>${symbol} - 实时价格</h3>
                <p>当前价格: $${stockData.c}</p>
                <p>开盘价: $${stockData.o}</p>
                <p>最高价: $${stockData.h}</p>
                <p>最低价: $${stockData.l}</p>
                <p>昨日收盘价: $${stockData.pc}</p>
                <p class="${change >= 0 ? 'stock-up' : 'stock-down'}">涨跌: ${change >= 0 ? '+' : ''}${change.toFixed(2)} (${changePercent >= 0 ? '+' : ''}${changePercent.toFixed(2)}%)</p>
                <p>时间戳: ${new Date(stockData.t * 1000).toLocaleString()}</p>
            `;
        } else if (selectedProvider === 'polygon' && stockData.results) {
            const result = stockData.results[0];
            const change = result.c - result.o;
            const changePercent = (change / result.o) * 100;
            stockInfo = `
                <h3>${symbol} - 实时价格</h3>
                <p>当前价格: $${result.c}</p>
                <p>开盘价: $${result.o}</p>
                <p>最高价: $${result.h}</p>
                <p>最低价: $${result.l}</p>
                <p class="${change >= 0 ? 'stock-up' : 'stock-down'}">涨跌: ${change >= 0 ? '+' : ''}${change.toFixed(2)} (${changePercent >= 0 ? '+' : ''}${changePercent.toFixed(2)}%)</p>
                <p>成交量: ${result.v}</p>
            `;
        }
        
        stockPreview.innerHTML = stockInfo;
        alert('API连接测试成功！');
    } catch (error) {
        alert('API连接测试失败: ' + error.message);
    }
}

// 保存API配置
function saveApiConfig() {
    const apiKey = document.getElementById('api-key').value;
    const apiBaseUrl = document.getElementById('api-base-url').value;
    const symbol = document.getElementById('symbol').value;
    const interval = document.getElementById('interval').value;
    const language = document.getElementById('language').value;
    
    if (!selectedProvider) {
        alert('请先选择API提供商');
        return;
    }
    
    const config = {
        provider: selectedProvider,
        apiKey: apiKey,
        apiBaseUrl: apiBaseUrl,
        symbol: symbol,
        interval: interval,
        language: language
    };
    
    localStorage.setItem('stockApiConfig', JSON.stringify(config));
    alert('API配置保存成功！');
}

// 保存Skill设置
function saveSkillSettings() {
    const skillName = document.getElementById('skill-name').value;
    const skillDescription = document.getElementById('skill-description').value;
    const skillVersion = document.getElementById('skill-version').value;
    const skillCategory = document.getElementById('skill-category').value;
    const skillAuthor = document.getElementById('skill-author').value;
    
    const settings = {
        name: skillName,
        description: skillDescription,
        version: skillVersion,
        category: skillCategory,
        author: skillAuthor
    };
    
    localStorage.setItem('stockSkillSettings', JSON.stringify(settings));
    alert('Skill设置保存成功！');
}

// 测试股票API
function testStockApi() {
    const config = JSON.parse(localStorage.getItem('stockApiConfig'));
    if (!config) {
        alert('请先保存API配置');
        return;
    }
    
    testApiConnection();
}

// 加载历史数据
async function loadHistoryData() {
    const apiKey = document.getElementById('api-key').value;
    const apiBaseUrl = document.getElementById('api-base-url').value;
    const symbol = document.getElementById('symbol').value;
    const interval = document.getElementById('interval').value;
    
    if (!selectedProvider) {
        alert('请先选择API提供商');
        return;
    }
    
    try {
        let historyData;
        
        if (selectedProvider === 'alphavantage') {
            // Alpha Vantage 历史数据
            const functionName = interval === 'daily' ? 'TIME_SERIES_DAILY' : interval === 'weekly' ? 'TIME_SERIES_WEEKLY' : interval === 'monthly' ? 'TIME_SERIES_MONTHLY' : 'TIME_SERIES_INTRADAY';
            const response = await fetch(`${apiBaseUrl}?function=${functionName}&symbol=${symbol}&interval=${interval}&apikey=${apiKey}&outputsize=compact`);
            if (!response.ok) {
                throw new Error('API调用失败');
            }
            historyData = await response.json();
        } else if (selectedProvider === 'finnhub') {
            // Finnhub 历史数据
            const response = await fetch(`${apiBaseUrl}/stock/candle?symbol=${symbol}&resolution=${interval === '1min' ? '1' : interval === '5min' ? '5' : interval === '15min' ? '15' : interval === '30min' ? '30' : interval === '60min' ? '60' : interval === 'daily' ? 'D' : interval === 'weekly' ? 'W' : 'M'}&count=10&token=${apiKey}`);
            if (!response.ok) {
                throw new Error('API调用失败');
            }
            historyData = await response.json();
        }
        
        // 显示历史数据
        const stockHistory = document.querySelector('#stock-history table tbody');
        let historyHtml = '';
        
        if (selectedProvider === 'alphavantage') {
            const timeSeriesKey = Object.keys(historyData).find(key => key.includes('Time Series'));
            if (timeSeriesKey && historyData[timeSeriesKey]) {
                const timeSeries = historyData[timeSeriesKey];
                Object.entries(timeSeries).slice(0, 10).forEach(([date, data]) => {
                    const close = parseFloat(data['4. close']);
                    const open = parseFloat(data['1. open']);
                    const change = close - open;
                    historyHtml += `
                        <tr>
                            <td>${date}</td>
                            <td>$${data['1. open']}</td>
                            <td>$${data['2. high']}</td>
                            <td>$${data['3. low']}</td>
                            <td class="${change >= 0 ? 'stock-up' : 'stock-down'}">$${close}</td>
                            <td>${data['5. volume']}</td>
                        </tr>
                    `;
                });
            }
        } else if (selectedProvider === 'finnhub' && historyData.t) {
            for (let i = historyData.t.length - 1; i >= 0; i--) {
                const date = new Date(historyData.t[i] * 1000).toLocaleString();
                const close = historyData.c[i];
                const open = historyData.o[i];
                const change = close - open;
                historyHtml += `
                    <tr>
                        <td>${date}</td>
                        <td>$${open}</td>
                        <td>$${historyData.h[i]}</td>
                        <td>$${historyData.l[i]}</td>
                        <td class="${change >= 0 ? 'stock-up' : 'stock-down'}">$${close}</td>
                        <td>${historyData.v[i]}</td>
                    </tr>
                `;
            }
        }
        
        stockHistory.innerHTML = historyHtml || '<tr><td colspan="6">暂无数据</td></tr>';
    } catch (error) {
        alert('加载历史数据失败: ' + error.message);
    }
}

// 发布到市场
function publishToMarket() {
    const config = JSON.parse(localStorage.getItem('stockApiConfig'));
    const settings = JSON.parse(localStorage.getItem('stockSkillSettings'));
    
    if (!config || !settings) {
        alert('请先完成API配置和Skill设置');
        return;
    }
    
    const publishTitle = document.getElementById('publish-title').value;
    const publishDescription = document.getElementById('publish-description').value;
    const publishTags = document.getElementById('publish-tags').value;
    const publishPrice = document.getElementById('publish-price').value;
    
    const skillData = {
        title: publishTitle,
        description: publishDescription,
        tags: publishTags.split(',').map(tag => tag.trim()),
        price: publishPrice,
        config: config,
        settings: settings
    };
    
    // 模拟发布到市场
    console.log('发布到市场:', skillData);
    alert('发布到市场成功！Skill已添加到市场界面。');
}

// 切换标签页函数
function switchTab(tabId) {
    // 移除所有活动状态
    document.querySelectorAll('.nav-menu a').forEach(a => {
        a.classList.remove('active');
    });
    
    // 添加当前活动状态
    event.target.classList.add('active');
    
    // 隐藏所有标签页内容
    document.querySelectorAll('.tab-content').forEach(tab => {
        tab.style.display = 'none';
    });
    
    // 显示选中的标签页内容
    document.getElementById(tabId + '-tab').style.display = 'block';
}

// 更新时间戳
function updateTimestamp() {
    const now = new Date();
    const timestamp = document.getElementById('timestamp');
    if (timestamp) {
        timestamp.textContent = now.toLocaleString('zh-CN');
    }
}

// 页面加载完成后初始化
window.onload = function() {
    updateTimestamp();
    setInterval(updateTimestamp, 60000); // 每分钟更新时间戳
    
    // 加载保存的配置
    const savedConfig = localStorage.getItem('stockApiConfig');
    if (savedConfig) {
        const config = JSON.parse(savedConfig);
        document.getElementById('api-key').value = config.apiKey || '';
        document.getElementById('api-base-url').value = config.apiBaseUrl || '';
        document.getElementById('symbol').value = config.symbol || 'AAPL';
        document.getElementById('interval').value = config.interval || 'daily';
        document.getElementById('language').value = config.language || 'zh_cn';
        
        // 选择API提供商
        if (config.provider) {
            selectedProvider = config.provider;
            const providers = document.querySelectorAll('.api-provider');
            providers.forEach(provider => {
                if (provider.textContent.includes(config.provider)) {
                    provider.classList.add('selected');
                }
            });
        }
    }
    
    // 加载保存的Skill设置
    const savedSettings = localStorage.getItem('stockSkillSettings');
    if (savedSettings) {
        const settings = JSON.parse(savedSettings);
        document.getElementById('skill-name').value = settings.name || '股票查询';
        document.getElementById('skill-description').value = settings.description || '基于第三方API的股票查询技能，可以获取全球股票的实时价格和历史数据。';
        document.getElementById('skill-version').value = settings.version || '1.0.0';
        document.getElementById('skill-category').value = settings.category || 'finance';
        document.getElementById('skill-author').value = settings.author || '';
    }
};
