(function() {
    'use strict';

    var reportData = null;
    var currentTab = 'business';
    var expandedCards = new Set();

    function init() {
        loadReport();
        initTabs();
    }

    async function loadReport() {
        var loadingState = document.getElementById('loading-state');
        var content = document.getElementById('statistics-content');
        
        try {
            var response = await fetch('/api/v1/discovery/report');
            var result = await response.json();
            
            if (result.status === 'success' && result.data) {
                reportData = result.data;
                renderReport();
                loadingState.style.display = 'none';
                content.style.display = 'block';
            } else {
                loadingState.innerHTML = '<div class="empty-state"><i class="ri-error-warning-line"></i><p>加载失败: ' + (result.message || '未知错误') + '</p></div>';
            }
        } catch (e) {
            console.error('Failed to load report:', e);
            loadingState.innerHTML = '<div class="empty-state"><i class="ri-error-warning-line"></i><p>加载失败: ' + e.message + '</p></div>';
        }
    }

    function renderReport() {
        renderOverviewCards();
        renderDirectoryTable();
        renderCategoryContent();
        renderCategoryCards();
        renderTreeView();
        renderComparisonTable();
        renderTestSuggestion();
    }

    function renderOverviewCards() {
        document.getElementById('totalSkills').textContent = reportData.total || 0;
        
        var sceneCount = 0;
        var providerCount = 0;
        var driverCount = 0;
        
        if (reportData.bySkillForm) {
            reportData.bySkillForm.forEach(function(item) {
                if (item.code === 'SCENE') sceneCount = item.count;
                else if (item.code === 'PROVIDER') providerCount = item.count;
                else if (item.code === 'DRIVER') driverCount = item.count;
            });
        }
        
        document.getElementById('sceneCount').textContent = sceneCount;
        document.getElementById('providerCount').textContent = providerCount;
        document.getElementById('driverCount').textContent = driverCount;
    }

    function renderDirectoryTable() {
        var tbody = document.getElementById('directoryTable');
        var html = '';
        
        if (reportData.directoryStats) {
            reportData.directoryStats.forEach(function(stat) {
                html += '<tr>' +
                    '<td><code>' + escapeHtml(stat.directory) + '</code></td>' +
                    '<td>' + escapeHtml(stat.description) + '</td>' +
                    '<td><strong>' + stat.count + '</strong></td>' +
                    '<td><div class="progress-bar"><div class="progress-fill" style="width: ' + stat.percentage + '%"></div></div><span class="progress-text">' + stat.percentage + '%</span></td>' +
                '</tr>';
            });
        }
        
        tbody.innerHTML = html || '<tr><td colspan="4" class="empty-cell">暂无数据</td></tr>';
    }

    function renderCategoryContent() {
        var container = document.getElementById('categoryContent');
        var data = null;
        
        if (currentTab === 'business') {
            data = reportData.byBusinessCategory;
        } else if (currentTab === 'form') {
            data = reportData.bySkillForm;
        } else if (currentTab === 'visibility') {
            data = reportData.byVisibility;
        }
        
        if (!data || data.length === 0) {
            container.innerHTML = '<div class="empty-state"><i class="ri-inbox-line"></i><p>暂无数据</p></div>';
            return;
        }
        
        var html = '<div class="category-grid">';
        data.forEach(function(item) {
            var color = item.color || '#8c8c8c';
            var icon = item.icon || 'ri-price-tag-3-line';
            html += '<div class="category-item" onclick="showCategoryDetail(\'' + item.code + '\')" style="border-left-color: ' + color + '">' +
                '<div class="category-icon" style="background: ' + color + '20; color: ' + color + '"><i class="' + icon + '"></i></div>' +
                '<div class="category-info">' +
                    '<div class="category-name">' + escapeHtml(item.name) + '</div>' +
                    '<div class="category-count">' + item.count + ' 个技能</div>' +
                '</div>' +
                '<div class="category-percent" style="color: ' + color + '">' + item.percentage + '%</div>' +
            '</div>';
        });
        html += '</div>';
        
        container.innerHTML = html;
    }

    function renderCategoryCards() {
        var container = document.getElementById('categoryCards');
        var html = '';
        
        if (reportData.byBusinessCategory) {
            reportData.byBusinessCategory.forEach(function(cat) {
                var color = cat.color || '#8c8c8c';
                var isExpanded = expandedCards.has(cat.code);
                
                html += '<div class="category-card" data-category="' + cat.code + '">' +
                    '<div class="card-header" onclick="toggleCategoryCard(\'' + cat.code + '\')">' +
                        '<span class="category-badge" style="background: ' + color + '20; color: ' + color + '">' +
                            '<i class="' + (cat.icon || 'ri-price-tag-3-line') + '"></i> ' + escapeHtml(cat.name) +
                        '</span>' +
                        '<span class="category-count">' + cat.count + '个</span>' +
                        '<button class="expand-btn"><i class="ri-arrow-' + (isExpanded ? 'up' : 'down') + '-s-line"></i></button>' +
                    '</div>' +
                    '<div class="card-body" style="display: ' + (isExpanded ? 'block' : 'none') + '">' +
                        '<table class="path-table">' +
                            '<thead><tr><th>Skill ID</th><th>绝对路径</th><th>操作</th></tr></thead>' +
                            '<tbody>';
                
                if (cat.skillIds && cat.absolutePaths) {
                    for (var i = 0; i < cat.skillIds.length; i++) {
                        var skillId = cat.skillIds[i];
                        var path = cat.absolutePaths[i] || '-';
                        html += '<tr>' +
                            '<td><code>' + escapeHtml(skillId) + '</code></td>' +
                            '<td class="path">' + escapeHtml(path) + '</td>' +
                            '<td><button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="copyPath(\'' + escapeHtml(path) + '\')"><i class="ri-file-copy-line"></i></button></td>' +
                        '</tr>';
                    }
                }
                
                html += '</tbody></table></div></div>';
            });
        }
        
        container.innerHTML = html || '<div class="empty-state"><i class="ri-inbox-line"></i><p>暂无数据</p></div>';
    }

    function renderTreeView() {
        var container = document.getElementById('treeView');
        var html = '';
        
        if (reportData.skillPaths) {
            var tree = buildTree(reportData.skillPaths);
            html = renderTreeNode(tree);
        }
        
        container.innerHTML = html || '<div class="empty-state"><i class="ri-inbox-line"></i><p>暂无数据</p></div>';
    }

    function buildTree(skills) {
        var tree = {};
        skills.forEach(function(skill) {
            var dir = skill.directory || 'unknown';
            if (!tree[dir]) {
                tree[dir] = [];
            }
            tree[dir].push(skill);
        });
        return tree;
    }

    function renderTreeNode(tree) {
        var dirInfo = {
            '_system': { name: '系统级服务', icon: 'ri-server-line', color: '#64748b' },
            '_drivers': { name: '驱动适配器', icon: 'ri-steering-line', color: '#52c41a' },
            '_business': { name: '业务服务', icon: 'ri-briefcase-line', color: '#f59e0b' },
            'capabilities': { name: '能力组件', icon: 'ri-cpu-line', color: '#1890ff' },
            'tools': { name: '工具服务', icon: 'ri-tools-line', color: '#8b5cf6' },
            'scenes': { name: '场景应用', icon: 'ri-layout-grid-line', color: '#722ed1' },
            'unknown': { name: '未知', icon: 'ri-question-line', color: '#8c8c8c' }
        };
        
        var html = '';
        for (var dir in tree) {
            var info = dirInfo[dir] || dirInfo.unknown;
            var skills = tree[dir];
            html += '<div class="tree-node">' +
                '<div class="tree-node-header" onclick="toggleTreeNode(this)">' +
                    '<i class="ri-arrow-right-s-line tree-arrow"></i>' +
                    '<i class="' + info.icon + '" style="color: ' + info.color + '"></i>' +
                    '<span class="tree-name">' + dir + '</span>' +
                    '<span class="tree-desc">' + info.name + '</span>' +
                    '<span class="tree-count">' + skills.length + '</span>' +
                '</div>' +
                '<div class="tree-node-children" style="display: none;">';
            
            skills.forEach(function(skill) {
                html += '<div class="tree-leaf">' +
                    '<i class="ri-puzzle-line"></i>' +
                    '<span class="leaf-name">' + escapeHtml(skill.skillId) + '</span>' +
                    '<span class="leaf-path">' + escapeHtml(skill.absolutePath || '') + '</span>' +
                    '<button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="copyPath(\'' + escapeHtml(skill.absolutePath || '') + '\')"><i class="ri-file-copy-line"></i></button>' +
                '</div>';
            });
            
            html += '</div></div>';
        }
        
        return html;
    }

    function renderComparisonTable() {
        var tbody = document.getElementById('comparisonTable');
        var html = '';
        
        if (reportData.dimensionComparison) {
            reportData.dimensionComparison.slice(0, 50).forEach(function(item) {
                html += '<tr>' +
                    '<td><code>' + escapeHtml(item.skillId || '') + '</code></td>' +
                    '<td>' + escapeHtml(item.name || '') + '</td>' +
                    '<td><span class="badge">' + escapeHtml(item.businessCategory || '-') + '</span></td>' +
                    '<td><span class="badge badge-form">' + escapeHtml(item.skillForm || '-') + '</span></td>' +
                    '<td>' + escapeHtml(item.directory || '-') + '</td>' +
                    '<td><span class="badge badge-' + (item.visibility === 'public' ? 'success' : 'secondary') + '">' + escapeHtml(item.visibility || '-') + '</span></td>' +
                '</tr>';
            });
        }
        
        tbody.innerHTML = html || '<tr><td colspan="6" class="empty-cell">暂无数据</td></tr>';
    }

    function renderTestSuggestion() {
        if (reportData.testSuggestion) {
            var test = reportData.testSuggestion;
            document.getElementById('estimatedTestCases').textContent = test.estimatedTestCases || 0;
            
            renderList('highPriorityList', test.highPriority);
            renderList('mediumPriorityList', test.mediumPriority);
            renderList('lowPriorityList', test.lowPriority);
        }
    }

    function renderList(elementId, items) {
        var ul = document.getElementById(elementId);
        if (!ul) return;
        
        if (!items || items.length === 0) {
            ul.innerHTML = '<li class="empty-item">暂无建议</li>';
            return;
        }
        
        var html = '';
        items.forEach(function(item) {
            html += '<li>' + escapeHtml(item) + '</li>';
        });
        ul.innerHTML = html;
    }

    function initTabs() {
        document.querySelectorAll('.category-tabs .tab').forEach(function(tab) {
            tab.addEventListener('click', function() {
                document.querySelectorAll('.category-tabs .tab').forEach(function(t) {
                    t.classList.remove('active');
                });
                this.classList.add('active');
                currentTab = this.dataset.tab;
                renderCategoryContent();
            });
        });
    }

    function escapeHtml(str) {
        if (str === null || str === undefined) return '';
        return String(str)
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#039;');
    }

    window.toggleCategoryCard = function(code) {
        if (expandedCards.has(code)) {
            expandedCards.delete(code);
        } else {
            expandedCards.add(code);
        }
        renderCategoryCards();
    };

    window.toggleTreeNode = function(header) {
        var children = header.nextElementSibling;
        var arrow = header.querySelector('.tree-arrow');
        if (children.style.display === 'none') {
            children.style.display = 'block';
            arrow.classList.add('rotated');
        } else {
            children.style.display = 'none';
            arrow.classList.remove('rotated');
        }
    };

    window.toggleAllTrees = function() {
        var allChildren = document.querySelectorAll('.tree-node-children');
        var allArrows = document.querySelectorAll('.tree-arrow');
        var anyOpen = Array.from(allChildren).some(function(c) { return c.style.display !== 'none'; });
        
        allChildren.forEach(function(c) {
            c.style.display = anyOpen ? 'none' : 'block';
        });
        allArrows.forEach(function(a) {
            if (anyOpen) {
                a.classList.remove('rotated');
            } else {
                a.classList.add('rotated');
            }
        });
    };

    window.copyPath = function(path) {
        navigator.clipboard.writeText(path).then(function() {
            alert('路径已复制: ' + path);
        }).catch(function(err) {
            console.error('Failed to copy:', err);
        });
    };

    window.showCategoryDetail = function(code) {
        expandedCards.add(code);
        renderCategoryCards();
        var card = document.querySelector('.category-card[data-category="' + code + '"]');
        if (card) {
            card.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
    };

    window.filterSkills = function() {
        var keyword = document.getElementById('searchPath').value.toLowerCase();
        var cards = document.querySelectorAll('.category-card');
        
        cards.forEach(function(card) {
            var skillIds = card.querySelectorAll('.path-table tbody tr');
            var hasMatch = false;
            
            skillIds.forEach(function(row) {
                var text = row.textContent.toLowerCase();
                if (text.includes(keyword)) {
                    row.style.display = '';
                    hasMatch = true;
                } else {
                    row.style.display = keyword ? 'none' : '';
                }
            });
            
            card.style.display = hasMatch || !keyword ? '' : 'none';
        });
    };

    window.exportReport = async function(format) {
        try {
            var response = await fetch('/api/v1/discovery/report/export?format=' + format);
            var result = await response.json();
            
            if (result.status === 'success' && result.data) {
                var content = result.data;
                var filename = 'skill-report.' + format;
                var mimeType = 'text/plain';
                
                if (format === 'json') {
                    mimeType = 'application/json';
                } else if (format === 'csv') {
                    mimeType = 'text/csv';
                } else {
                    mimeType = 'text/markdown';
                }
                
                var blob = new Blob([content], { type: mimeType });
                var url = URL.createObjectURL(blob);
                var a = document.createElement('a');
                a.href = url;
                a.download = filename;
                document.body.appendChild(a);
                a.click();
                document.body.removeChild(a);
                URL.revokeObjectURL(url);
            } else {
                alert('导出失败: ' + (result.message || '未知错误'));
            }
        } catch (e) {
            console.error('Export failed:', e);
            alert('导出失败: ' + e.message);
        }
    };

    window.refreshReport = function() {
        document.getElementById('loading-state').style.display = 'flex';
        document.getElementById('statistics-content').style.display = 'none';
        loadReport();
    };

    document.addEventListener('DOMContentLoaded', init);
})();
