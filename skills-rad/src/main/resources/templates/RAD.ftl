<!--
 * ooder-agent-rad
 * Copyright (C) 2026 ooderTeam
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 -->
<!DOCTYPE html>
<html>
<head>


    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta http-equiv="Content-Style-Type" content="text/css"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta http-equiv="imagetoolbar" content="no"/>
    <meta name="viewport"
          content="user-scalable=no, initial-scale=1.2, minimum-scale=0.5, maximum-scale=2.0,width=device-width, height=device-height"/>
    <link rel="stylesheet" type="text/css" href="/css/default.css"/>


    <link rel="stylesheet" type="text/css" href="/ood/webfonts/all.min.css">
    <!-- 引入Remix Icon -->
    <link rel="stylesheet" type="text/css" href="/ood/iconfont/iconfont.css"/>
    <!-- 默认加载dark主题 -->
    <link rel="stylesheet" type="text/css" href="/ood/appearance/dark/theme.css" id="theme-style"/>
    <link rel="stylesheet" type="text/css" href="/ood/js/mobile/mobile.css" id="theme-style"/>
    <link rel="stylesheet" type="text/css" href="/css/remixicon/remixicon.css"/>
    <link rel="stylesheet" type="text/css" href="/plugins/formlayout/handsontable.full.min.css"/>
    <!-- 主题切换控制 -->
    <script>
        // 设置默认主题
        document.addEventListener('DOMContentLoaded', function () {
            // 从localStorage读取保存的主题
            const savedTheme = localStorage.getItem('ood-theme') || 'dark';
            setTheme(savedTheme);

            // 添加主题切换按钮
            addThemeToggle();
        });

        function setTheme(theme) {
            const themeStyle = document.getElementById('theme-style');
            themeStyle.href = "/ood/appearance/" + theme + "/theme.css";
            localStorage.setItem('ood-theme', theme);
        }

        function addThemeToggle() {
            const toggle = document.createElement('div');
            toggle.id = 'theme-toggle';
            toggle.innerHTML = `
          <button class="theme-btn dark" title="dark Theme">
            <i class="ri-moon-line"></i>
          </button>
          <button class="theme-btn light" title="light Theme">
            <i class="ri-sun-line"></i>
          </button>
          <button class="theme-btn hc" title="High Contrast">
            <i class="ri-contrast-2-line"></i>
          </button>
        `;
            toggle.style.position = 'fixed';
            toggle.style.bottom = '20px';
            toggle.style.right = '20px';
            toggle.style.zIndex = '1000';
            toggle.style.display = 'flex';
            toggle.style.gap = '10px';

            document.body.appendChild(toggle);

            // 添加按钮事件
            document.querySelectorAll('.theme-btn').forEach(btn => {
                btn.addEventListener('click', function () {
                const theme = this.classList.contains('dark') ? 'dark' :
                        this.classList.contains('light') ? 'light' : 'high-contrast';
                setTheme(theme);
            });
        });
        }

    </script>

    <!-- 移除这些冲突的引用 -->
    <#list cssList as item>
        <link rel="stylesheet" type="text/css" href="/root/${item.path}"/>
    </#list>

    <title>欢迎使用 ESDStudio 工具1.0</title>
</head>
<body spellcheck="false">

<style>
    /* 全局加载动画样式 */
    #loading {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        width: 100vw;
        background-color: #f5f5f5;
        position: fixed;
        top: 0;
        left: 0;
        z-index: 9999;
        transition: opacity 1s ease-in-out;
    }

    @keyframes floating {
        0%, 100% {
            transform: translateY(0);
        }
        50% {
            transform: translateY(-8px);
        }
    }

    @keyframes glowing {
        0%, 100% {
            filter: drop-shadow(0 0 0px #2196F3);
        }
        50% {
            filter: drop-shadow(0 0 5px #2196F3);
        }
    }

    .loading-container {
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .loading-status {
        margin-top: 20px;
        font-family: 'Comic Sans MS', cursive, sans-serif;
        font-size: 16px;
        color: #333;
    }
</style>

<!-- 主加载容器 - 确保只会有一个加载页面 -->
<div id='loading'>
    <div class="loading-container">
        <!-- 简化的SVG动画 -->
        <svg width="400" height="300" viewBox="0 0 400 300">
            <!-- 显示编程代码的电脑 -->
            <g class="computer" style="animation: floating 5s ease-in-out infinite;">
                <!-- 显示器 -->
                <rect x="100" y="80" width="120" height="80" rx="3" ry="3" fill="#f0f0f0" stroke="#333" stroke-width="2">
                    <animate attributeName="opacity" values="0;1" dur="1s" fill="freeze"/>
                </rect>
                <!-- 显示器底座 -->
                <rect x="140" y="160" width="40" height="5" rx="1" ry="1" fill="#333">
                    <animate attributeName="opacity" values="0;1" dur="1.2s" fill="freeze"/>
                </rect>
                <!-- 显示器屏幕 -->
                <rect x="105" y="85" width="110" height="70" fill="#2d3748" stroke="none">
                    <animate attributeName="opacity" values="0;1" dur="1.5s" fill="freeze"/>
                </rect>
                <!-- 模拟代码显示 -->
                <g class="code-display">
                    <text x="110" y="105" font-family="'Courier New', monospace" font-size="7" fill="#a0aec0" opacity="0.8">
                        <animate attributeName="opacity" values="0;0.8" dur="2s" fill="freeze"/>
                        function helloWorld() {
                    </text>
                    <text x="130" y="120" font-family="'Courier New', monospace" font-size="7" fill="#4fd1c5" opacity="0.8">
                        <animate attributeName="opacity" values="0;0.8" dur="2.2s" fill="freeze"/>
                        console.log("Hello OPQ!");
                    </text>
                    <text x="110" y="135" font-family="'Courier New', monospace" font-size="7" fill="#a0aec0" opacity="0.8">
                        <animate attributeName="opacity" values="0;0.8" dur="2.4s" fill="freeze"/>
                        }
                    </text>
                    <text x="110" y="150" font-family="'Courier New', monospace" font-size="7" fill="#f56565" opacity="0.8">
                        <animate attributeName="opacity" values="0;0.8" dur="2.6s" fill="freeze"/>
                        // SuperAgent-RAD
                    </text>
                </g>
            </g>

            <!-- OPQ牌子 -->
            <g class="opq-sign" style="animation: floating 4.5s ease-in-out infinite; animation-delay: 1s;">
                <rect x="250" y="100" width="100" height="60" rx="5" ry="5" fill="#fff" stroke="#333" stroke-width="2">
                    <animate attributeName="opacity" values="0;1" dur="1.5s" fill="freeze"/>
                </rect>
                <text x="300" y="140" font-family="'Comic Sans MS', cursive, sans-serif" font-size="36"
                      font-weight="bold" text-anchor="middle" fill="#333">
                    <animate attributeName="opacity" values="0;1" dur="2s" fill="freeze"/>
                    OPQ
                </text>
            </g>

            <!-- 主题文字 -->
            <text x="200" y="50" font-family="'Comic Sans MS', cursive, sans-serif" font-size="24" font-weight="bold"
                  text-anchor="middle" fill="#2196F3">
                <animate attributeName="opacity" values="0;1" dur="2s" fill="freeze"/>
                SuperAgent-RAD
            </text>
        </svg>

        <!-- 加载状态文本 -->
        <div class="loading-status">正在准备开发环境...</div>
    </div>
</div>
</body>


<!-- 使用打包后的JS文件 -->
<script type="text/javascript" src="/release/projectManager/js/hammer.js"></script>
<script type="text/javascript" src="/release/projectManager/js/ood_core.js"></script>
<script type="text/javascript" src="/release/projectManager/js/ood_ui.js"></script>
<script type="text/javascript" src="/release/projectManager/js/ood_mobile.js"></script>




<script type="text/javascript" src="/release/projectManager/js/raphael.js"></script>
<script type="text/javascript" src="/release/projectManager/js/svg.js"></script>
<script type="text/javascript" src="/release/projectManager/js/SVGPaper.js"></script>
<script type="text/javascript" src="/release/projectManager/js/FusionChartsXT.js"></script>
<script type="text/javascript" src="/release/projectManager/js/ECharts.js"></script>
<script type="text/javascript" src="/release/projectManager/js/Coder.js"></script>
<script type="text/javascript" src="/release/projectManager/js/JSONEditor.js"></script>
<script type="text/javascript" src="/release/projectManager/js/AdvResizer.js"></script>
<script type="text/javascript" src="/release/projectManager/js/codemirror.js"></script>
<script type="text/javascript" src="/release/projectManager/js/rad.js"></script>
<script type="text/javascript" src="/release/projectManager/js/handsontable.full.min.js"></script>
<script type="text/javascript" src="/release/projectManager/js/cn.js"></script>


<script>

    // 全局加载动画管理 - 统一管理所有加载状态
    window.loadingAnimationManager = {
        // 创建并显示加载动画
        showLoadingAnimation: function (message) {
            // 检查是否已存在loading元素，避免重复创建
            var loadingElement = document.getElementById('loading');
            if (!loadingElement) {
                // 创建新的加载元素
                loadingElement = document.createElement('div');
                loadingElement.id = 'loading';
                loadingElement.innerHTML = `
                    <div class="loading-container">
                        <!-- 简化的SVG动画 -->
                        <svg width="300" height="250" viewBox="0 0 300 250">
                        <!-- 电脑设备 -->
                        <g class="computer" style="animation: floating 3s ease-in-out infinite;">
                        <!-- 显示器 -->
                        <rect x="70" y="50" width="160" height="100" rx="3" fill="#f0f0f0" stroke="#333" stroke-width="2">
                        <animate attributeName="opacity" values="0;1" dur="1s" fill="freeze"/>
                        </rect>
                        <!-- 显示器底座 -->
                        <rect x="120" y="150" width="60" height="5" rx="1" ry="1" fill="#333">
                        <animate attributeName="opacity" values="0;1" dur="1.2s" fill="freeze"/>
                        </rect>
                        <!-- 显示器屏幕 -->
                        <rect x="75" y="55" width="150" height="90" fill="#2d3748" stroke="none">
                        <animate attributeName="opacity" values="0;1" dur="1.5s" fill="freeze"/>
                        </rect>
                        <!-- 屏幕内容 - 代码 -->
                        <g class="screen-content">
                        <!-- JavaScript代码 -->
                        <text x="85" y="75" font-family="'Courier New', monospace" font-size="8" fill="#a0aec0">function helloWorld() {
                        </text>
                        <text x="105" y="90" font-family="'Courier New', monospace" font-size="8" fill="#4fd1c5">  console.log("Hello OPQ!");
                        </text>
                        <text x="85" y="105" font-family="'Courier New', monospace" font-size="8" fill="#a0aec0">}
                        </text>
                        <!-- 闪烁光标 -->
                        <rect x="90" y="115" width="5" height="10" fill="#3498db">
                        <animate attributeName="opacity" values="1;0;1" dur="1s" repeatCount="indefinite"/>
                        </rect>
                        </g>
                        </g>
                        <!-- OPQ牌子 -->
                        <g class="opq-sign" style="animation: floating 4s ease-in-out infinite; animation-delay: 1s;">
                        <rect x="240" y="90" width="50" height="30" rx="2" fill="#fff" stroke="#333" stroke-width="2">
                        <animate attributeName="opacity" values="0;1" dur="1.5s" fill="freeze"/>
                        </rect>
                        <text x="265" y="110" font-family="Arial, sans-serif" font-size="14" font-weight="bold" fill="#333" text-anchor="middle">OPQ</text>
                        </g>
                        <!-- 主题文字 -->
                        <text x="150" y="30" font-family="Arial, sans-serif" font-size="18" font-weight="bold" fill="#2196F3" text-anchor="middle" style="animation: floating 3s ease-in-out infinite;">
                        SuperAgent-RAD
                        <animate attributeName="opacity" values="0.9;1;0.9" dur="3s" repeatCount="indefinite"/>
                        </text>
                        </svg>
                <#--// <div class="loading-status">${message || '加载中...'}</div>-->
                        </div>
                        `

                            document.body.appendChild(loadingElement);
                        }

                        // 更新加载消息
                        var statusElement = loadingElement.querySelector('.loading-status');
                        if (statusElement && message) {
                            statusElement.textContent = message;
                        }
                    },

                    // 隐藏加载动画
                    hideLoadingAnimation: function() {
                        var loadingElement = document.getElementById('loading');
                        if (loadingElement && loadingElement.parentNode) {
                            // 淡出动画
                            loadingElement.style.opacity = '0';

                            // 等待淡出动画完成后移除元素
                            setTimeout(function() {
                                loadingElement.remove();
                            }, 1000);
                        }
                    }
                };

                // 页面主加载动画管理
                document.addEventListener('DOMContentLoaded', function() {
                    console.log('DOM内容加载完成，初始化加载动画管理');

                    // 应用模块加载完成后移除加载动画
                    function removeLoadingAnimation() {
                        window.loadingAnimationManager.hideLoadingAnimation();
                    }

                    // 监听应用模块加载完成事件
                    document.addEventListener('appLoaded', function() {
                        removeLoadingAnimation();
                    });

                    // 备用方案：设置最长显示时间（防止加载卡住）
                    setTimeout(function() {
                        removeLoadingAnimation();
                    }, 15000); // 15秒后自动隐藏
                });

                ood.include("ood.Locale.cn.doc", "/Locale/cn.js", function () {
                    ood.Module.load('rad.Main', function () {
                        var that = this;
                        console.log('加载模块完成');
                        ood.ini.$PageAppearance = {
                            theme: "dark"
                        };

                        // 应用初始化完成，触发移除加载动画事件
                        setTimeout(function() {
                            var event = new CustomEvent('appLoaded');
                            document.dispatchEvent(event);
                        }, 500);

                    }, 'cn');
                });


</script>




<script type="text/javascript">

    CONF.clientType = 'ESDClient';
    currProjectName = "${projectName}";
    domainId = "${domainId}";
    window.handleId
    $E = ood.execExpression;
    $BPD = {
        open: function () {
            $E('$BPD.open()')
        },
        close: function () {
            $E('$BPD.close()')
        },
        newprocess: function () {
            var paramArr = {projectName: "${projectName}"};
            paramArr.handleId = window.handleId;
            $E('$ESD.newprocess()', paramArr);
        },
    },


            $ESD = {
                export: function (url, params) {
                    var paramArr = params || {projectName: "${projectName}"};
                    if (url) {
                        paramArr.url = url;
                        paramArr.handleId = window.handleId;
                    }
                    $E('$ESD.export()', paramArr);

                },


                publicRemote: function () {
                    var paramArr = {
                        projectName: "${projectName}",
                        className:SPA.getCurrentClassName()
                    };
                    paramArr.url = window.location.href;
                    $E('$ESD.publicRemote()', paramArr)
                },
                publicLocal: function () {
                    var paramArr = {
                        projectName: "${projectName}",
                        className:SPA.getCurrentClassName()
                    };
                    paramArr.url = window.location.href;
                    $E('$ESD.publicLocal()', paramArr)
                },


                exportRemoteServer: function () {
                    var paramArr = {projectName: "${projectName}"};
                    paramArr.url = window.location.href;
                    $E('$ESD.exportRemoteServer()', paramArr)
                },

                screen: function () {
                    var paramArr = {projectName: "${projectName}"};
                    paramArr.url = window.location.href;
                    $E('$ESD.screen()', paramArr)
                },

                buildCustomModule: function (url, params) {
                    var paramArr = {projectName: "${projectName}"};
                    paramArr.handleId = window.handleId;
                    $E('$ESD.rebuildCustomModule()', paramArr)
                },

                download: function (url, params) {
                    var paramArr = params || {projectName: "${projectName}"};
                    if (url) {
                        paramArr.url = url;
                        paramArr.handleId = window.handleId;
                    }
                    $E('$ESD.download()', paramArr);
                    // 替换ood.busy()为我们的自定义加载动画
                    showLoadingAnimation();
                },

                // 全局方法：显示加载动画
                showLoadingAnimation: function (message) {
                    // 检查是否已存在loading元素
                    var existingLoading = document.getElementById('loading');
                    if (existingLoading) {
                        return; // 已存在，避免重复显示
                    }

                    // 创建临时加载动画
                    var loadingElement = document.createElement('div');
                    loadingElement.id = 'loading';
                    loadingElement.style.position = 'fixed';
                    loadingElement.style.top = '0';
                    loadingElement.style.left = '0';
                    loadingElement.style.width = '100%';
                    loadingElement.style.height = '100%';
                    loadingElement.style.background = 'rgba(245, 245, 245, 0.9)';
                    loadingElement.style.display = 'flex';
                    loadingElement.style.justifyContent = 'center';
                    loadingElement.style.alignItems = 'center';
                    loadingElement.style.zIndex = '9999';
                    loadingElement.style.transition = 'opacity 0.5s ease-in-out';

                    // 添加简化版动画
                    loadingElement.innerHTML = `
                <div style="text-align: center;">
                    <svg width="100" height="100" viewBox="0 0 100 100">
                        <circle cx="50" cy="50" r="40" fill="none" stroke="#2196F3" stroke-width="3" stroke-dasharray="251.2" stroke-dashoffset="251.2">
                            <animate attributeName="stroke-dashoffset" from="251.2" to="0" dur="2s" repeatCount="indefinite"/>
                            <animateTransform attributeName="transform" type="rotate" from="0 50 50" to="360 50 50" dur="2s" repeatCount="indefinite"/>
                        </circle>
                    </svg>
                    <div style="margin-top: 10px; font-family: 'Comic Sans MS', cursive, sans-serif; color: #333;">
                        <#--${message || '处理中...'}-->
                    </div>
                </div>
            `;

                    document.body.appendChild(loadingElement);

                    // 设置自动隐藏（防止卡住）
                    setTimeout(function () {
                        if (loadingElement && loadingElement.parentNode) {
                            loadingElement.style.opacity = '0';
                            setTimeout(function () {
                                loadingElement.remove();
                            }, 500);
                        }
                    }, 30000); // 30秒后自动隐藏
                },

                // 全局方法：隐藏加载动画
                hideLoadingAnimation: function () {
                    var loadingElement = document.getElementById('loading');
                    if (loadingElement && loadingElement.parentNode) {
                        loadingElement.style.opacity = '0';
                        setTimeout(function () {
                            loadingElement.remove();
                        }, 500);
                    }
                },


                startDebugServer: function (url, params) {
                    var paramArr = params || {projectName: "${projectName}"};
                    if (url) {
                        paramArr.url = url;
                        paramArr.handleId = window.handleId;
                    }

                    $E('$ESD.startDebugServer()', paramArr)
                },
                stopDebugServer: function (url, params) {
                    var paramArr = params || {projectName: "${projectName}"};
                    if (url) {
                        paramArr.url = url;
                        paramArr.handleId = window.handleId;
                    }

                    $E('$ESD.stopDebugServer()', paramArr)
                },

                exportLocalServer: function (url, params) {
                    var paramArr = params || {projectName: "${projectName}"};
                    if (url) {
                        paramArr.url = url;
                        paramArr.handleId = window.handleId;
                    }
                    $E('$ESD.exportLocalServer()', paramArr)
                },


                pull: function (url, params) {
                    var paramArr = params || {projectName: "${projectName}"};
                    if (url) {
                        paramArr.url = url;
                        paramArr.handleId = window.handleId;
                    }
                    $E('$ESD.pull()', paramArr)
                },

                clearAll: function (url, params) {
                    var paramArr = params || {projectName: "${projectName}"};
                    if (url) {
                        paramArr.url = url;
                        paramArr.handleId = window.handleId;
                    }
                    $E('$ESD.clearAll()', paramArr)
                },


                push: function (url, params) {
                    var paramArr = params || {projectName: "${projectName}"};
                    if (url) {
                        paramArr.url = url;
                        paramArr.handleId = window.handleId;
                    }
                    $E('$ESD.push()', paramArr)
                },


                clearAll: function (url, params) {
                    var paramArr = params || {projectName: "${projectName}"};
                    if (url) {
                        paramArr.url = url;
                        paramArr.handleId = window.handleId;
                    }
                    $E('$ESD.clearAll()', paramArr)
                },

                open: function (url, params) {
                    var paramArr = params | {};
                    if (url) {
                        paramArr.url = url;
                        paramArr.handleId = window.handleId;
                    }
                    $E('$ESD.open()', paramArr)
                },

                reload: function (packageName, params) {
                    var paramArr = params || {projectName: "${projectName}"};
                    paramArr.handleId = window.handleId;
                    if (packageName) {
                        paramArr.packageName = packageName;
                    }
                    $E('$ESD.reload()', paramArr)
                },

                customDebug: function (params) {
                    var paramArr = params || {projectName: "${projectName}"};
                    paramArr.handleId = window.handleId;
                    $E('$ESD.customDebug()', paramArr)
                },

                clear: function (spaceName, params) {
                    var paramArr = params || {projectName: "${projectName}"};
                    paramArr.handleId = window.handleId;
                    if (spaceName) {
                        paramArr.spaceName = spaceName;
                    }
                    $E('$ESD.clear()', paramArr)
                },

                createDBModule: function (url, params) {
                    var paramArr = params || {projectName: "${projectName}"};
                    if (url) {
                        paramArr.url = url;

                    }
                    paramArr.handleId = window.handleId;
                    $E('$ESD.createDBModule()', paramArr)
                },

                openProject: function (newProjectName, params) {
                    var paramArr = params || {};
                    if (newProjectName) {
                        paramArr.newProjectName = newProjectName;
                        paramArr.handleId = window.handleId;
                    }
                    $E('$ESD.openProject()', paramArr)
                },
                quit: function () {
                    $E('$ESD.quit()')
                },
                logout: function () {
                    $E('$ESD.logout()')
                },
                clear: function () {
                    $E('$ESD.clear()', {projectName: SPA.curProjectName})
                }
            }
</script>



</html>