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
    <link href="https://cdn.bootcdn.net/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <!-- 引入Remix Icon -->
    <link href=" https://fastly.jsdelivr.net/npm/remixicon@3.5.0/fonts/remixicon.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/ood/iconfont/iconfont.css"/>
    <!-- 默认加载dark主题 -->
    <link rel="stylesheet" type="text/css" href="/ood/appearance/dark/theme.css" id="theme-style"/>
    <link rel="stylesheet" type="text/css" href="/ood/js/mobile/mobile.css" id="theme-style"/>
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
        <!-- 抽取的SVG动画 -->
        <svg width="400" height="300" viewBox="0 0 400 300">
            <!-- 网格背景 -->
            <g class="grid-background">
                <pattern id="grid" width="20" height="20" patternUnits="userSpaceOnUse">
                    <path d="M 20 0 L 0 0 0 20" fill="none" stroke="#eee" stroke-width="0.5"/>
                </pattern>
                <rect width="400" height="300" fill="url(#grid)">
                    <animate attributeName="opacity" values="0;0.5" dur="2s" fill="freeze"/>
                </rect>
            </g>

            <!-- 显示编程代码的电脑 -->
            <g class="computer" style="animation: floating 5s ease-in-out infinite; animation-delay: 2s;">
                <!-- 显示器 -->
                <rect x="110" y="90" width="100" height="70" rx="3" ry="3" fill="#f0f0f0" stroke="#333"
                      stroke-width="2">
                    <animate attributeName="width" values="0;100" dur="1.5s" fill="freeze"/>
                    <animate attributeName="height" values="0;70" dur="1.5s" fill="freeze"/>
                    <animate attributeName="opacity" values="0;1" dur="1.5s" fill="freeze"/>
                </rect>
                <!-- 显示器底座 -->
                <rect x="140" y="160" width="40" height="5" rx="1" ry="1" fill="#333">
                    <animate attributeName="width" values="0;40" dur="1.7s" fill="freeze"/>
                    <animate attributeName="opacity" values="0;1" dur="1.7s" fill="freeze"/>
                </rect>
                <!-- 主机 -->
                <rect x="120" y="180" width="80" height="60" rx="3" ry="3" fill="#f0f0f0" stroke="#333"
                      stroke-width="2">
                    <animate attributeName="height" values="0;60" dur="2s" fill="freeze"/>
                    <animate attributeName="opacity" values="0;1" dur="2s" fill="freeze"/>
                </rect>
                <!-- 主机按钮 -->
                <circle cx="190" cy="190" r="3" fill="#333">
                    <animate attributeName="r" values="0;3" dur="2.2s" fill="freeze"/>
                    <animate attributeName="opacity" values="0;1" dur="2.2s" fill="freeze"/>
                </circle>
                <rect x="130" y="190" width="20" height="3" rx="1" ry="1" fill="#ccc">
                    <animate attributeName="width" values="0;20" dur="2.4s" fill="freeze"/>
                    <animate attributeName="opacity" values="0;1" dur="2.4s" fill="freeze"/>
                </rect>
                <!-- 键盘 -->
                <rect x="100" y="240" width="120" height="20" rx="3" ry="3" fill="#f0f0f0" stroke="#333"
                      stroke-width="2">
                    <animate attributeName="width" values="0;120" dur="2.5s" fill="freeze"/>
                    <animate attributeName="opacity" values="0;1" dur="2.5s" fill="freeze"/>
                </rect>
                <!-- 键盘按键（简化表示） -->
                <g class="keys">
                    <rect x="105" y="243" width="5" height="5" fill="#ddd" stroke="#ccc" stroke-width="1">
                        <animate attributeName="opacity" values="0;1" dur="2.7s" fill="freeze"/>
                    </rect>
                    <rect x="113" y="243" width="5" height="5" fill="#ddd" stroke="#ccc" stroke-width="1">
                        <animate attributeName="opacity" values="0;1" dur="2.75s" fill="freeze"/>
                    </rect>
                    <rect x="121" y="243" width="5" height="5" fill="#ddd" stroke="#ccc" stroke-width="1">
                        <animate attributeName="opacity" values="0;1" dur="2.8s" fill="freeze"/>
                    </rect>
                    <rect x="129" y="243" width="5" height="5" fill="#ddd" stroke="#ccc" stroke-width="1">
                        <animate attributeName="opacity" values="0;1" dur="2.85s" fill="freeze"/>
                    </rect>
                    <rect x="137" y="243" width="5" height="5" fill="#ddd" stroke="#ccc" stroke-width="1">
                        <animate attributeName="opacity" values="0;1" dur="2.9s" fill="freeze"/>
                    </rect>
                    <rect x="145" y="243" width="5" height="5" fill="#ddd" stroke="#ccc" stroke-width="1">
                        <animate attributeName="opacity" values="0;1" dur="2.95s" fill="freeze"/>
                    </rect>
                    <rect x="153" y="243" width="5" height="5" fill="#ddd" stroke="#ccc" stroke-width="1">
                        <animate attributeName="opacity" values="0;1" dur="3s" fill="freeze"/>
                    </rect>
                    <rect x="161" y="243" width="5" height="5" fill="#ddd" stroke="#ccc" stroke-width="1">
                        <animate attributeName="opacity" values="0;1" dur="3.05s" fill="freeze"/>
                    </rect>
                    <rect x="169" y="243" width="5" height="5" fill="#ddd" stroke="#ccc" stroke-width="1">
                        <animate attributeName="opacity" values="0;1" dur="3.1s" fill="freeze"/>
                    </rect>
                    <rect x="177" y="243" width="5" height="5" fill="#ddd" stroke="#ccc" stroke-width="1">
                        <animate attributeName="opacity" values="0;1" dur="3.15s" fill="freeze"/>
                    </rect>
                    <rect x="185" y="243" width="5" height="5" fill="#ddd" stroke="#ccc" stroke-width="1">
                        <animate attributeName="opacity" values="0;1" dur="3.2s" fill="freeze"/>
                    </rect>
                </g>
                <!-- 显示器屏幕 -->
                <rect x="115" y="95" width="90" height="60" fill="#2d3748" stroke="none">
                    <animate attributeName="opacity" values="0;1" dur="2s" fill="freeze"/>
                </rect>
                <!-- 模拟代码显示 -->
                <g class="code-display">
                    <text x="120" y="110" font-family="'Courier New', monospace" font-size="6" fill="#a0aec0"
                          opacity="0.8">
                        <animate attributeName="opacity" values="0;0.8" dur="2.5s" fill="freeze"/>
                        function helloWorld() {
                    </text>
                    <text x="135" y="120" font-family="'Courier New', monospace" font-size="6" fill="#4fd1c5"
                          opacity="0.8">
                        <animate attributeName="opacity" values="0;0.8" dur="2.7s" fill="freeze"/>
                        console.log("Hello OPQ!");
                    </text>
                    <text x="120" y="130" font-family="'Courier New', monospace" font-size="6" fill="#a0aec0"
                          opacity="0.8">
                        <animate attributeName="opacity" values="0;0.8" dur="2.9s" fill="freeze"/>
                        }
                    </text>
                    <text x="120" y="140" font-family="'Courier New', monospace" font-size="6" fill="#f56565"
                          opacity="0.8">
                        <animate attributeName="opacity" values="0;0.8" dur="3.1s" fill="freeze"/>
                        // 智能开发助手
                    </text>
                    <text x="120" y="150" font-family="'Courier New', monospace" font-size="6" fill="#a0aec0"
                          opacity="0.8">
                        <animate attributeName="opacity" values="0;0.8" dur="3.3s" fill="freeze"/>
                        helloWorld();
                    </text>
                    <!-- 光标效果 -->
                    <rect x="160" y="147" width="3" height="6" fill="#a0aec0">
                        <animate attributeName="opacity" values="0;1;0;1;0;1;0" dur="1.5s" repeatCount="indefinite"
                                 begin="4s"/>
                    </rect>
                </g>
            </g>

            <!-- 对话框气泡 -->
            <g class="speech-bubble" style="animation: glowing 3s ease-in-out infinite; animation-delay: 4s;">
                <path d="M200,80 Q250,60 250,80 Q250,100 200,80 Z" fill="#fff" stroke="#333" stroke-width="2">
                    <animate attributeName="d"
                             values="M200,80 Q200,80 200,80 Q200,80 200,80 Z;M200,80 Q250,60 250,80 Q250,100 200,80 Z"
                             dur="2.5s" fill="freeze"/>
                    <animate attributeName="opacity" values="0;1" dur="2.5s" fill="freeze"/>
                </path>
                <path d="M200,80 L180,100 L190,95 L200,80 Z" fill="#fff" stroke="#333" stroke-width="2">
                    <animate attributeName="opacity" values="0;1" dur="3s" fill="freeze"/>
                </path>
            </g>

            <!-- OPQ牌子 -->
            <g class="opq-sign" style="animation: floating 4.5s ease-in-out infinite; animation-delay: 3s;">
                <rect x="240" y="150" width="100" height="60" rx="5" ry="5" fill="#fff" stroke="#333" stroke-width="2">
                    <animate attributeName="width" values="0;100" dur="2s" fill="freeze"/>
                    <animate attributeName="height" values="0;60" dur="2s" fill="freeze"/>
                    <animate attributeName="opacity" values="0;1" dur="2s" fill="freeze"/>
                </rect>
                <text x="290" y="185" font-family="'Comic Sans MS', cursive, sans-serif" font-size="36"
                      font-weight="bold" text-anchor="middle" fill="#333">
                    <animate attributeName="opacity" values="0;1" dur="3.5s" fill="freeze"/>
                    <animate attributeName="font-size" values="0;36" dur="3.5s" fill="freeze"/>
                    OPQ
                </text>
                <!-- 旋转箭头 -->
                <path d="M240,180 L220,170 L225,185 L220,200 Z" fill="#333">
                    <animate attributeName="opacity" values="0;1" dur="3s" fill="freeze"/>
                    <animateTransform attributeName="transform" attributeType="XML" type="rotate" from="0 240 180"
                                      to="360 240 180" dur="6s" repeatCount="indefinite" begin="4s"/>
                </path>
            </g>

            <!-- 扳手工具 -->
            <g class="wrench" style="animation: floating 4s ease-in-out infinite; animation-delay: 3.5s;">
                <path d="M260,220 L280,220 L300,200 L290,215 L275,210 L275,230 Z" fill="#795548" stroke="#333"
                      stroke-width="1.5">
                    <animate attributeName="opacity" values="0;1" dur="3.5s" fill="freeze"/>
                    <animateTransform attributeName="transform" attributeType="XML" type="rotate" from="0 270 220"
                                      to="-30 270 220" dur="3s" repeatCount="indefinite" begin="4.5s"/>
                </path>
                <line x1="270" y1="220" x2="270" y2="240" stroke="#333" stroke-width="2" stroke-linecap="round">
                    <animate attributeName="y2" values="220;240" dur="3.5s" fill="freeze"/>
                    <animate attributeName="opacity" values="0;1" dur="3.5s" fill="freeze"/>
                </line>
            </g>

            <!-- 代码块图标 -->
            <g class="code-blocks">
                <path d="M80,80 L120,80 L120,100 L80,100 Z" fill="#e3f2fd" stroke="#333" stroke-width="1.5">
                    <animate attributeName="opacity" values="0;1" dur="2s" fill="freeze"/>
                    <animateTransform attributeName="transform" attributeType="XML" type="rotate" from="0 100 90"
                                      to="-15 100 90" dur="4s" repeatCount="indefinite" begin="3s"/>
                </path>
                <text x="100" y="94" font-family="Arial, sans-serif" font-size="10" text-anchor="middle" fill="#333">
                    <animate attributeName="opacity" values="0;1" dur="2.5s" fill="freeze"/>
                    &lt;/&gt;
                </text>

                <path d="M320,70 L360,70 L360,90 L320,90 Z" fill="#e3f2fd" stroke="#333" stroke-width="1.5">
                    <animate attributeName="opacity" values="0;1" dur="2.5s" fill="freeze"/>
                    <animateTransform attributeName="transform" attributeType="XML" type="rotate" from="0 340 80"
                                      to="15 340 80" dur="3.5s" repeatCount="indefinite" begin="3.5s"/>
                </path>
                <text x="340" y="84" font-family="Arial, sans-serif" font-size="10" text-anchor="middle" fill="#333">
                    <animate attributeName="opacity" values="0;1" dur="3s" fill="freeze"/>
                    &lt;/&gt;
                </text>

                <path d="M330,230 L370,230 L370,250 L330,250 Z" fill="#e3f2fd" stroke="#333" stroke-width="1.5">
                    <animate attributeName="opacity" values="0;1" dur="3s" fill="freeze"/>
                    <animateTransform attributeName="transform" attributeType="XML" type="rotate" from="0 350 240"
                                      to="-10 350 240" dur="4.5s" repeatCount="indefinite" begin="4s"/>
                </path>
                <text x="350" y="244" font-family="Arial, sans-serif" font-size="10" text-anchor="middle" fill="#333">
                    <animate attributeName="opacity" values="0;1" dur="3.5s" fill="freeze"/>
                    &lt;/&gt;
                </text>
            </g>

            <!-- 主题文字 -->
            <text x="200" y="50" font-family="'Comic Sans MS', cursive, sans-serif" font-size="24" font-weight="bold"
                  text-anchor="middle" fill="#2196F3">
                <animate attributeName="opacity" values="0;1" dur="4s" fill="freeze"/>
                <animate attributeName="font-size" values="0;24" dur="4s" fill="freeze"/>
                ooder 智能开发助手
            </text>
        </svg>

        <!-- 加载状态文本 -->
        <div class="loading-status">正在准备开发环境...</div>
    </div>
</div>
</body>


<script type="text/javascript" src="/ood/js/ThirdParty/hammer.js"></script>
<script type="text/javascript" src="/ood/js/ood.js"></script>

<script type="text/javascript" src="/ood/js/APICaller.js"></script>
<script type="text/javascript" src="/ood/js/MQTT.js"></script>
<script type="text/javascript" src="/ood/js/DataBinder.js"></script>
<script type="text/javascript" src="/ood/js/Event.js"></script>
<script type="text/javascript" src="/ood/js/CSS.js"></script>
<script type="text/javascript" src="/ood/js/Dom.js"></script>
<script type="text/javascript" src="/ood/js/DragDrop.js"></script>
<script type="text/javascript" src="/ood/js/Template.js"></script>
<script type="text/javascript" src="/ood/js/Cookies.js"></script>
<script type="text/javascript" src="/ood/js/History.js"></script>
<script type="text/javascript" src="/ood/js/Tips.js"></script>
<script type="text/javascript" src="/ood/js/Module.js"></script>
<script type="text/javascript" src="/ood/js/XML.js"></script>
<script type="text/javascript" src="/ood/js/XMLRPC.js"></script>

<script type="text/javascript" src="/ood/js/SOAP.js"></script>
<script type="text/javascript" src="/ood/js/ModuleFactory.js"></script>
<script type="text/javascript" src="/ood/js/Debugger.js"></script>
<script type="text/javascript" src="/ood/js/Date.js"></script>
<script type="text/javascript" src="/ood/js/UI.js"></script>
<script type="text/javascript" src="/ood/js/IconMapping.js"></script>
<script type="text/javascript" src="/ood/js/UI/Image.js"></script>
<script type="text/javascript" src="/ood/js/UI/Flash.js"></script>
<script type="text/javascript" src="/ood/js/UI/Audio.js"></script>
<script type="text/javascript" src="/ood/js/UI/FileUpload.js"></script>
<script type="text/javascript" src="/ood/js/UI/Video.js"></script>
<script type="text/javascript" src="/ood/js/UI/Tensor.js"></script>
<script type="text/javascript" src="/ood/js/UI/Camera.js"></script>
<script type="text/javascript" src="/ood/js/UI/Resizer.js"></script>
<script type="text/javascript" src="/ood/js/UI/Block.js"></script>
<script type="text/javascript" src="/ood/js/UI/Label.js"></script>
<script type="text/javascript" src="/ood/js/UI/ProgressBar.js"></script>
<script type="text/javascript" src="/ood/js/UI/Slider.js"></script>
<script type="text/javascript" src="/ood/js/UI/Input.js"></script>
<script type="text/javascript" src="/ood/js/UI/CheckBox.js"></script>
<script type="text/javascript" src="/ood/js/UI/HiddenInput.js"></script>
<script type="text/javascript" src="/ood/js/UI/RichEditor.js"></script>
<script type="text/javascript" src="/ood/js/UI/ComboInput.js"></script>
<script type="text/javascript" src="/ood/js/UI/ColorPicker.js"></script>
<script type="text/javascript" src="/ood/js/UI/DatePicker.js"></script>
<script type="text/javascript" src="/ood/js/UI/TimePicker.js"></script>
<script type="text/javascript" src="/ood/js/UI/List.js"></script>
<script type="text/javascript" src="/ood/js/UI/Gallery.js"></script>
<script type="text/javascript" src="/ood/js/UI/ButtonLayout.js"></script>
<script type="text/javascript" src="/ood/js/UI/TitleBlock.js"></script>
<script type="text/javascript" src="/ood/js/UI/ContentBlock.js"></script>
<script type="text/javascript" src="/ood/js/UI/Panel.js"></script>
<script type="text/javascript" src="/ood/js/UI/Group.js"></script>
<script type="text/javascript" src="/ood/js/UI/PageBar.js"></script>
<script type="text/javascript" src="/ood/js/UI/Tabs.js"></script>
<script type="text/javascript" src="/ood/js/UI/Stacks.js"></script>
<script type="text/javascript" src="/ood/js/UI/ButtonViews.js"></script>
<script type="text/javascript" src="/ood/js/UI/MTabs.js"></script>
<script type="text/javascript" src="/ood/js/UI/RadioBox.js"></script>
<script type="text/javascript" src="/ood/js/UI/StatusButtons.js"></script>
<script type="text/javascript" src="/ood/js/UI/TreeBar.js"></script>
<script type="text/javascript" src="/ood/js/UI/TreeView.js"></script>
<script type="text/javascript" src="/ood/js/UI/MTreeView.js"></script>
<script type="text/javascript" src="/ood/js/UI/PopMenu.js"></script>
<script type="text/javascript" src="/ood/js/UI/MenuBar.js"></script>
<script type="text/javascript" src="/ood/js/UI/ToolBar.js"></script>
<script type="text/javascript" src="/ood/js/UI/Layout.js"></script>
<script type="text/javascript" src="/ood/js/UI/TreeGrid.js"></script>
<script type="text/javascript" src="/ood/js/UI/MTreeGrid.js"></script>
<script type="text/javascript" src="/ood/js/UI/Dialog.js"></script>
<#--<script type="text/javascript" src="/ood/js/UI/MDialog.js"></script>-->
<script type="text/javascript" src="/ood/js/UI/FormLayout.js"></script>
<script type="text/javascript" src="/ood/js/UI/MFormLayout.js"></script>
<script type="text/javascript" src="/ood/js/UI/FoldingTabs.js"></script>
<script type="text/javascript" src="/ood/js/UI/FoldingList.js"></script>
<script type="text/javascript" src="/ood/js/UI/Opinion.js"></script>
<script type="text/javascript" src="/ood/js/ThirdParty/raphael.js"></script>
<script type="text/javascript" src="/ood/js/svg.js"></script>
<script type="text/javascript" src="/ood/js/UI/SVGPaper.js"></script>
<script type="text/javascript" src="/ood/js/UI/FusionChartsXT.js"></script>
<script type="text/javascript" src="/ood/js/UI/ECharts.js"></script>
<script type="text/javascript" src="/ood/js/Coder.js"></script>
<script type="text/javascript" src="/ood/js/Module/JSONEditor.js"></script>
<script type="text/javascript" src="/RAD/custom/AdvResizer.js"></script>

<script type="text/javascript" src="/ood/js/mobile/Basic/Button.js"></script>
<script type="text/javascript" src="/ood/js/mobile/Basic/Input.js"></script>
<script type="text/javascript" src="/ood/js/mobile/Basic/List.js"></script>
<script type="text/javascript" src="/ood/js/mobile/Display/Avatar.js"></script>
<script type="text/javascript" src="/ood/js/mobile/Display/Badge.js"></script>
<script type="text/javascript" src="/ood/js/mobile/Display/Card.js"></script>
<script type="text/javascript" src="/ood/js/mobile/Feedback/ActionSheet.js"></script>

<script type="text/javascript" src="/ood/js/mobile/Feedback/Modal.js"></script>
<script type="text/javascript" src="/ood/js/mobile/Feedback/Toast.js"></script>

<script type="text/javascript" src="/ood/js/mobile/Form/Form.js"></script>
<script type="text/javascript" src="/ood/js/mobile/Form/Picker.js"></script>
<script type="text/javascript" src="/ood/js/mobile/Form/Switch.js"></script>

<script type="text/javascript" src="/ood/js/mobile/Layout/Grid.js"></script>
<script type="text/javascript" src="/ood/js/mobile/Layout/Layout.js"></script>
<script type="text/javascript" src="/ood/js/mobile/Layout/Panel.js"></script>

<script type="text/javascript" src="/ood/js/mobile/Navigation/Drawer.js"></script>
<script type="text/javascript" src="/ood/js/mobile/Navigation/NavBar.js"></script>
<script type="text/javascript" src="/ood/js/mobile/Navigation/TabBar.js"></script>
<script type="text/javascript" src="/ood/js/mobile/OA/NoticeDesignerAdvanced.js"></script>
<script type="text/javascript" src="/ood/js/mobile/OA/NoticeAPICallerExample.js"></script>
<script type="text/javascript" src="/ood/js/mobile/OA/NoticeActionConfig.js"></script>
<script type="text/javascript" src="/ood/js/mobile/OA/NoticeUsageExample.js"></script>
<script type="text/javascript" src="/ood/js/mobile/OA/NoticeDesignerStandard.js"></script>
<script type="text/javascript" src="/ood/js/mobile/OA/NoticeDesignerFinal.js"></script>
<script type="text/javascript" src="/ood/js/mobile/OA/NoticeDesignerFinalUsage.js"></script>
<script type="text/javascript" src="/ood/js/mobile/OA/NoticeDesignerAPICaller.js"></script>
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
                        <!-- 抽取的SVG动画 -->
                        <svg width="400" height="300" viewBox="0 0 400 300">
                        <!-- 网格背景 -->
                        <g class="grid-background">
                        <pattern id="grid" width="20" height="20" patternUnits="userSpaceOnUse">
                        <path d="M 20 0 L 0 0 0 20" fill="none" stroke="#eee" stroke-width="0.5"/>
                        </pattern>
                        </g>
                        <!-- 电脑设备 -->
                        <g class="computer" style="animation: floating 3s ease-in-out infinite;">
                        <!-- 显示器 -->
                        <rect x="80" y="60" width="240" height="160" rx="5" fill="#2c3e50" stroke="#34495e" stroke-width="5" opacity="0.9">
                        <animate attributeName="opacity" values="0.8;1;0.8" dur="3s" repeatCount="indefinite"/>
                        <animate attributeName="width" values="238;240;238" dur="3s" repeatCount="indefinite"/>
                        <animate attributeName="height" values="158;160;158" dur="3s" repeatCount="indefinite"/>
                        </rect>
                        <!-- 底座 -->
                        <rect x="130" y="220" width="140" height="20" rx="5" fill="#34495e" stroke="#2c3e50" stroke-width="3"/>
                        <rect x="150" y="240" width="100" height="10" rx="3" fill="#2c3e50" stroke="#34495e" stroke-width="2"/>
                        <!-- 主机 -->
                        <rect x="340" y="120" width="40" height="100" rx="3" fill="#2c3e50" stroke="#34495e" stroke-width="3">
                        <animate attributeName="height" values="98;100;98" dur="4s" repeatCount="indefinite"/>
                        <animate attributeName="opacity" values="0.8;1;0.8" dur="4s" repeatCount="indefinite"/>
                        </rect>
                        <!-- 主机按钮 -->
                        <circle cx="360" cy="140" r="3" fill="#3498db"/>
                        <circle cx="360" cy="150" r="3" fill="#e74c3c"/>
                        <!-- 键盘 -->
                        <rect x="100" y="250" width="200" height="30" rx="3" fill="#34495e" stroke="#2c3e50" stroke-width="2">
                        <animateTransform attributeName="transform" type="translate" values="0 0; 0 -2; 0 0" dur="2s" repeatCount="indefinite"/>
                        </rect>
                        <!-- 键盘按键 -->
                        <rect x="110" y="255" width="16" height="16" rx="2" fill="#2c3e50">
                        <animateTransform attributeName="transform" type="translate" values="0 0; 0 -1; 0 0" dur="1s" repeatCount="indefinite" begin="0.1s"/>
                        </rect>
                        <rect x="130" y="255" width="16" height="16" rx="2" fill="#2c3e50">
                        <animateTransform attributeName="transform" type="translate" values="0 0; 0 -1; 0 0" dur="1s" repeatCount="indefinite" begin="0.3s"/>
                        </rect>
                        <rect x="150" y="255" width="16" height="16" rx="2" fill="#2c3e50">
                        <animateTransform attributeName="transform" type="translate" values="0 0; 0 -1; 0 0" dur="1s" repeatCount="indefinite" begin="0.5s"/>
                        </rect>
                        <!-- 屏幕内容 - 代码 -->
                        <g class="screen-content">
                        <!-- 代码背景 -->
                        <rect x="90" y="70" width="220" height="140" rx="2" fill="#1e1e1e"/>
                        <!-- JavaScript代码 -->
                        <text x="100" y="95" font-family="'Courier New', monospace" font-size="10" fill="#d4d4d4">function helloWorld() {</text>
                        <text x="120" y="115" font-family="'Courier New', monospace" font-size="10" fill="#4ec9b0">  console.log("Hello OPQ!");
                        </text>
                        <text x="100" y="135" font-family="'Courier New', monospace" font-size="10" fill="#d4d4d4">}</text>
                        <!-- 闪烁光标 -->
                        <rect x="108" y="152" width="6" height="12" fill="#3498db">
                        <animate attributeName="opacity" values="1;0;1" dur="1s" repeatCount="indefinite"/>
                        </rect>
                        </g>
                        </g>
                        <!-- 对话框气泡 -->
                        <g class="speech-bubble" style="animation: glowing 2s ease-in-out infinite; animation-delay: 0.5s;">
                        <path d="M300,80 Q340,80 340,110 L340,130 L320,130 L320,140 L290,120 L320,100 L320,110 Q320,90 300,80 Z" fill="#ffffff"/>
                        </g>
                        <!-- OPQ牌子 -->
                        <g class="opq-sign" style="animation: floating 4s ease-in-out infinite; animation-delay: 1s;">
                        <rect x="10" y="20" width="60" height="30" rx="2" fill="#3498db"/>
                        <text x="40" y="40" font-family="Arial, sans-serif" font-size="14" font-weight="bold" fill="#ffffff" text-anchor="middle">OPQ</text>
                        <path d="M40,55 L40,70 L45,65 M40,65 L45,70" stroke="#3498db" stroke-width="2" fill="none"/>
                        </g>
                        <!-- 扳手工具 -->
                        <g class="wrench" style="animation: floating 3.5s ease-in-out infinite; animation-delay: 1.5s;">
                        <path d="M350,200 L360,180 L370,170 L360,160 L340,165 L330,175 L335,185 Z" fill="#e74c3c" stroke="#c0392b" stroke-width="1"/>
                        <circle cx="335" cy="180" r="5" fill="#c0392b"/>
                        <path d="M355,190 L355,205 L340,220" stroke="#e74c3c" stroke-width="2" stroke-linecap="round"/>
                        </g>
                        <!-- 代码块图标 -->
                        <g class="code-block" style="animation: floating 3s ease-in-out infinite; animation-delay: 2s;">
                        <rect x="320" y="40" width="50" height="30" rx="2" fill="#27ae60" stroke="#229954" stroke-width="2"/>
                        <rect x="325" y="45" width="40" height="5" rx="1" fill="#229954"/>
                        <rect x="325" y="55" width="30" height="5" rx="1" fill="#229954"/>
                        <rect x="325" y="65" width="35" height="5" rx="1" fill="#229954"/>
                        </g>
                        <!-- 主题文字 -->
                        <text x="200" y="50" font-family="Arial, sans-serif" font-size="18" font-weight="bold" fill="#3498db" text-anchor="middle" style="animation: floating 3s ease-in-out infinite;">
                        ooder 智能开发助手
                        <animate attributeName="font-size" values="17;18;17" dur="3s" repeatCount="indefinite"/>
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
                    ood.Module.load('RAD', function () {
                        var that = this;
                        console.log('加载模块完成');
                        SPA = that;
                        // 在应用初始化时设置全局主题
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


<script type="text/javascript" src="/plugins/codemirror5/lib/codemirror.js"></script>
<script type="text/javascript" src="/plugins/codemirror5/mode/meta.js"></script>
<script type="text/javascript" src="/plugins/codemirror5/keymap/sublime.js"></script>
<script type="text/javascript" src="/plugins/codemirror5/addon/mode/loadmode.js"></script>
<script type="text/javascript" src="/plugins/codemirror5/addon/lint/jshint.js"></script>
<script type="text/javascript" src="/plugins/codemirror5/addon/lint/jsonlint.js"></script>
<script type="text/javascript" src="/plugins/codemirror5/addon/lint/csslint.js"></script>
<script type="text/javascript" src="/plugins/codemirror5/addon/lint/htmlhint.js"></script>
<script type="text/javascript" src="/plugins/codemirror5/addon/lint/lint.js"></script>
<script type="text/javascript" src="/plugins/codemirror5/addon/lint/javascript-lint.js"></script>
<script type="text/javascript" src="/plugins/codemirror5/addon/lint/json-lint.js"></script>
<script type="text/javascript" src="/plugins/codemirror5/addon/lint/css-lint.js"></script>
<script type="text/javascript" src="/plugins/codemirror5/addon/lint/html-lint.js"></script>
<script type="text/javascript" src="/plugins/codemirror5/addon/fold/foldcode.js"></script>
<script type="text/javascript" src="/plugins/codemirror5/addon/fold/foldgutter.js"></script>
<script type="text/javascript" src="/plugins/codemirror5/addon/fold/brace-fold.js"></script>
<script type="text/javascript" src="/plugins/codemirror5/addon/fold/xml-fold.js"></script>
<script type="text/javascript" src="/plugins/codemirror5/addon/fold/comment-fold.js"></script>
<script type="text/javascript" src="/plugins/codemirror5/addon/search/searchcursor.js"></script>
<script type="text/javascript" src="/plugins/codemirror5/addon/edit/matchbrackets.js"></script>
<script type="text/javascript" src="/plugins/codemirror5/addon/edit/closebrackets.js"></script>
<script type="text/javascript" src="/plugins/codemirror5/addon/comment/comment.js"></script>
<script type="text/javascript" src="/plugins/codemirror5/addon/comment/continuecomment.js"></script>
<script type="text/javascript" src="/plugins/codemirror5/mode/javascript/javascript.js"></script>
<script type="text/javascript" src="/plugins/codemirror5/mode/xml/xml.js"></script>
<script type="text/javascript" src="/plugins/codemirror5/mode/css/css.js"></script>
<script type="text/javascript" src="/plugins/codemirror5/mode/htmlmixed/htmlmixed.js"></script>

<script type="text/javascript" src="/RAD/conf.js"></script>
<script type="text/javascript" src="/RAD/conf_widgets.js"></script>
<script type="text/javascript" src="/RAD/SPAConf.js"></script>
<script type="text/javascript" src="/RAD/fcconf.js"></script>
<script type="text/javascript" src="/RAD/CodeEditor.js"></script>
<script type="text/javascript" src="/RAD/ClassTool.js"></script>
<script type="text/javascript" src="/RAD/JSEditor.js"></script>
<script type="text/javascript" src="/RAD/expression/JavaEditor.js"></script>
<script type="text/javascript" src="/RAD/PageEditor.js"></script>
<script type="text/javascript" src="/RAD/FunEditor.js"></script>
<script type="text/javascript" src="/RAD/JSONEditor.js"></script>
<script type="text/javascript" src="/RAD/ObjectEditor2.js"></script>
<script type="text/javascript" src="/RAD/FileSelector.js"></script>
<script type="text/javascript" src="/RAD/MobileInstruction.js"></script>
<script type="text/javascript" src="/RAD/HTMLEditor.js"></script>
<script type="text/javascript" src="/RAD/Designer.js"></script>
<script type="text/javascript" src="/RAD/ClassTree.js"></script>
<script type="text/javascript" src="/RAD/ServiceTree.js"></script>
<script type="text/javascript" src="/RAD/ProjectTree.js"></script>

<script type="text/javascript" src="/RAD/JumpTo.js"></script>
<script type="text/javascript" src="/RAD/FAndR.js"></script>
<script type="text/javascript" src="/RAD/EditorTool.js"></script>
<script type="text/javascript" src="/RAD/EditorTheme.js"></script>
<script type="text/javascript" src="/RAD/ServiceTester.js"></script>
<script type="text/javascript" src="/RAD/SelRenderMode.js"></script>
<script type="text/javascript" src="/RAD/About.js"></script>
<script type="text/javascript" src="/RAD/GridBorder.js"></script>


<script type="text/javascript" src="/RAD/CustomDecoration.js"></script>
<script type="text/javascript" src="/RAD/CustomDecoration2.js"></script>
<script type="text/javascript" src="/RAD/CustomBorder.js"></script>
<script type="text/javascript" src="/RAD/CustomBoxShadow.js"></script>
<script type="text/javascript" src="/RAD/CustomTextShadow.js"></script>
<script type="text/javascript" src="/RAD/CustomTransform.js"></script>
<script type="text/javascript" src="/RAD/CustomGradients.js"></script>
<script type="text/javascript" src="/RAD/CustomEffects.js"></script>
<script type="text/javascript" src="/RAD/ActionsEditor.js"></script>
<script type="text/javascript" src="/RAD/InteractionMap.js"></script>
<script type="text/javascript" src="/RAD/AnimationConf.js"></script>
<script type="text/javascript" src="/RAD/EventPicker.js"></script>

<script type="text/javascript" src="/RAD/SVGCustomDecoration.js"></script>
<script type="text/javascript" src="/RAD/SVGCustomGradients.js"></script>
<script type="text/javascript" src="/RAD/ChartCustomSetting.js"></script>
<script type="text/javascript" src="/RAD/CustomArrows.js"></script>
<script type="text/javascript" src="/RAD/PropEditor.js"></script>
<script type="text/javascript" src="/RAD/MixPropEditor.js"></script>

<script type="text/javascript" src="/RAD/CustomPackage.js"></script>
<script type="text/javascript" src="/RAD/APIConfig.js"></script>

<script type="text/javascript" src="/RAD/SwitchWorkspace.js"></script>

<script type="text/javascript" src="/RAD/UIDesigner.js"></script>
<script type="text/javascript" src="/RAD/SelFontAwesome.js"></script>

<script type="text/javascript" src="/RAD/OODBuilder.js"></script>
<script type="text/javascript" src="/RAD/AddFile.js"></script>
<script type="text/javascript" src="/RAD/ProjectPro.js"></script>
<script type="text/javascript" src="/RAD/TemplatePreview.js"></script>
<script type="text/javascript" src="/RAD/TemplateSelector.js"></script>
<script type="text/javascript" src="/RAD/ImageSelector.js"></script>
<script type="text/javascript" src="/RAD/index.js"></script>
<script type="text/javascript" src="/RAD/expression/JavaEditor.js"></script>
<script type="text/javascript" src="/RAD/expression/CodeEditor.js"></script>
<script type="text/javascript" src="/plugins/formlayout/handsontable.full.min.js"></script>
<script type="text/javascript" src="/RAD/expression/ExpressionTemp.js"></script>
<script type="text/javascript" src="/RAD/expression/ExpressionEditor.js"></script>
<script type="text/javascript" src="/RAD/expression/PageEditor.js"></script>

<script type="text/javascript" src="/Locale/cn.js"></script>
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
            <#--CONF.widgets =${widgets},-->

            <#--CONF.widgets_itemsProp.items_conf=${itemsProp},-->

            <#--CONF.widgets_itemsProp.items=${items},-->

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
                        className: SPA.getCurrentClassName()
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
