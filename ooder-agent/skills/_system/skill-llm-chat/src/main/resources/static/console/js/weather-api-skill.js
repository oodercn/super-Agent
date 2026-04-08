// 当前选中的API提供商
let selectedProvider = null;

// API提供商配置
const providers = {
    openweathermap: {
        name: 'OpenWeatherMap',
        baseUrl: 'https://api.openweathermap.org/data/2.5',
        docs: 'https://openweathermap.org/api'
    },
    weatherapi: {
        name: 'WeatherAPI.com',
        baseUrl: 'https://api.weatherapi.com/v1',
        docs: 'https://www.weatherapi.com/docs/'
    },
    accuweather: {
        name: 'AccuWeather',
        baseUrl: 'https://dataservice.accuweather.com',
        docs: 'https://developer.accuweather.com/apis'
    }
};

// 切换标签页
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
    const city = document.getElementById('city').value;
    
    if (!selectedProvider) {
        alert('请先选择API提供商');
        return;
    }
    
    try {
        let weatherData;
        
        if (selectedProvider === 'openweathermap') {
            // OpenWeatherMap API测试
            const response = await fetch(`${apiBaseUrl}/weather?q=${city}&appid=${apiKey}&units=metric&lang=zh_cn`);
            if (!response.ok) {
                throw new Error('API调用失败');
            }
            weatherData = await response.json();
        } else if (selectedProvider === 'weatherapi') {
            // WeatherAPI.com API测试
            const response = await fetch(`${apiBaseUrl}/current.json?key=${apiKey}&q=${city}&aqi=no`);
            if (!response.ok) {
                throw new Error('API调用失败');
            }
            weatherData = await response.json();
        } else if (selectedProvider === 'accuweather') {
            // AccuWeather API测试
            // 先获取位置ID
            const locationResponse = await fetch(`${apiBaseUrl}/locations/v1/cities/search?apikey=${apiKey}&q=${city}`);
            if (!locationResponse.ok) {
                throw new Error('获取位置ID失败');
            }
            const locations = await locationResponse.json();
            if (locations.length === 0) {
                throw new Error('未找到城市');
            }
            const locationKey = locations[0].Key;
            
            // 再获取天气数据
            const weatherResponse = await fetch(`${apiBaseUrl}/currentconditions/v1/${locationKey}?apikey=${apiKey}&language=zh-cn&details=false`);
            if (!weatherResponse.ok) {
                throw new Error('API调用失败');
            }
            weatherData = await weatherResponse.json();
        }
        
        // 显示成功消息
        const weatherPreview = document.getElementById('weather-preview');
        weatherPreview.innerHTML = `
            <h3>${city}天气</h3>
            <p>温度: ${weatherData.main ? weatherData.main.temp : weatherData.current ? weatherData.current.temp_c : weatherData[0].Temperature.Metric.Value}°C</p>
            <p>天气: ${weatherData.weather ? weatherData.weather[0].description : weatherData.current ? weatherData.current.condition.text : weatherData[0].WeatherText}</p>
            <p>湿度: ${weatherData.main ? weatherData.main.humidity : weatherData.current ? weatherData.current.humidity : 'N/A'}%</p>
            <p>风速: ${weatherData.wind ? weatherData.wind.speed : weatherData.current ? weatherData.current.wind_kph : 'N/A'} km/h</p>
        `;
        
        alert('API连接测试成功！');
    } catch (error) {
        alert('API连接测试失败: ' + error.message);
    }
}

// 保存API配置
function saveApiConfig() {
    const apiKey = document.getElementById('api-key').value;
    const apiBaseUrl = document.getElementById('api-base-url').value;
    const city = document.getElementById('city').value;
    const units = document.getElementById('units').value;
    const language = document.getElementById('language').value;
    
    if (!selectedProvider) {
        alert('请先选择API提供商');
        return;
    }
    
    const config = {
        provider: selectedProvider,
        apiKey: apiKey,
        apiBaseUrl: apiBaseUrl,
        city: city,
        units: units,
        language: language
    };
    
    localStorage.setItem('weatherApiConfig', JSON.stringify(config));
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
    
    localStorage.setItem('weatherSkillSettings', JSON.stringify(settings));
    alert('Skill设置保存成功！');
}

// 测试天气API
function testWeatherApi() {
    const config = JSON.parse(localStorage.getItem('weatherApiConfig'));
    if (!config) {
        alert('请先保存API配置');
        return;
    }
    
    testApiConnection();
}

// 发布到市场
function publishToMarket() {
    const config = JSON.parse(localStorage.getItem('weatherApiConfig'));
    const settings = JSON.parse(localStorage.getItem('weatherSkillSettings'));
    
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
    const savedConfig = localStorage.getItem('weatherApiConfig');
    if (savedConfig) {
        const config = JSON.parse(savedConfig);
        document.getElementById('api-key').value = config.apiKey || '';
        document.getElementById('api-base-url').value = config.apiBaseUrl || '';
        document.getElementById('city').value = config.city || '';
        document.getElementById('units').value = config.units || 'metric';
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
    const savedSettings = localStorage.getItem('weatherSkillSettings');
    if (savedSettings) {
        const settings = JSON.parse(savedSettings);
        document.getElementById('skill-name').value = settings.name || '天气查询';
        document.getElementById('skill-description').value = settings.description || '基于第三方API的天气查询技能，可以获取全球城市的实时天气信息。';
        document.getElementById('skill-version').value = settings.version || '1.0.0';
        document.getElementById('skill-category').value = settings.category || 'weather';
        document.getElementById('skill-author').value = settings.author || '';
    }
};
