(function() {
    'use strict';

    var currentDeptId = null;
    var orgTreeData = [];

    function init() {
        console.log('组织机构管理页面初始化完成');
        loadOrgStats();
        loadOrgTree();
    }

    async function loadOrgStats() {
        try {
            var response = await fetch('/api/org/stats', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({})
            });
            var rs = await response.json();

            if (rs.requestStatus === 200 && rs.data) {
                document.getElementById('total-depts').textContent = rs.data.totalDepartments;
                document.getElementById('total-users').textContent = rs.data.totalUsers;
                document.getElementById('total-groups').textContent = rs.data.totalGroups;
            }
        } catch (error) {
            console.error('加载统计信息错误:', error);
        }
    }

    async function loadOrgTree() {
        try {
            var response = await fetch('/api/org/tree', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({})
            });
            var rs = await response.json();

            if (rs.requestStatus === 200 && rs.data) {
                orgTreeData = rs.data;
                renderOrgTree(rs.data);
            } else {
                document.getElementById('org-tree').innerHTML = '<li class="nx-text-center nx-p-4 nx-text-secondary">加载失败</li>';
            }
        } catch (error) {
            console.error('加载组织树错误:', error);
            document.getElementById('org-tree').innerHTML = '<li class="nx-text-center nx-p-4 nx-text-secondary">加载失败</li>';
        }
    }

    function renderOrgTree(nodes, container) {
        if (!container) {
            container = document.getElementById('org-tree');
            container.innerHTML = '';
        }

        for (var i = 0; i < nodes.length; i++) {
            var node = nodes[i];
            var li = document.createElement('li');
            li.className = 'org-tree-node';
            li.style.cssText = 'margin: 2px 0;';

            var nodeDiv = document.createElement('div');
            nodeDiv.className = 'org-tree-node__content';
            nodeDiv.style.cssText = 'display: flex; align-items: center; padding: 8px 12px; cursor: pointer; border-radius: var(--nx-radius); transition: background 0.2s;';
            nodeDiv.onmouseenter = function() { this.style.background = 'var(--nx-bg-elevated)'; };
            nodeDiv.onmouseleave = function() { this.style.background = 'transparent'; };

            var icon = document.createElement('i');
            icon.className = node.type === 'user' ? 'ri-user-line' : 'ri-folder-line';
            icon.style.cssText = 'margin-right: 8px; color: ' + (node.type === 'user' ? 'var(--nx-success)' : 'var(--nx-primary)') + ';';

            var name = document.createElement('span');
            name.textContent = node.name;
            name.style.cssText = 'flex: 1;';

            nodeDiv.appendChild(icon);
            nodeDiv.appendChild(name);

            var nodeData = node;
            if (node.hasChildren || (node.children && node.children.length > 0)) {
                var expandIcon = document.createElement('i');
                expandIcon.className = 'ri-arrow-right-s-line';
                expandIcon.style.cssText = 'margin-left: 8px; transition: transform 0.2s;';
                nodeDiv.insertBefore(expandIcon, name);

                var childUl = document.createElement('ul');
                childUl.style.cssText = 'list-style: none; padding-left: 20px; margin: 0; display: none;';

                if (node.children && node.children.length > 0) {
                    renderOrgTree(node.children, childUl);
                }

                (function(nodeData, childUl, expandIcon) {
                    nodeDiv.onclick = function(e) {
                        e.stopPropagation();
                        if (childUl.style.display === 'none') {
                            childUl.style.display = 'block';
                            expandIcon.style.transform = 'rotate(90deg)';
                        } else {
                            childUl.style.display = 'none';
                            expandIcon.style.transform = 'rotate(0deg)';
                        }
                        selectNode(nodeData);
                    };
                })(nodeData, childUl, expandIcon);

                li.appendChild(nodeDiv);
                li.appendChild(childUl);
            } else {
                (function(nodeData) {
                    nodeDiv.onclick = function() { selectNode(nodeData); };
                })(nodeData);
                li.appendChild(nodeDiv);
            }

            container.appendChild(li);
        }
    }

    async function selectNode(node) {
        currentDeptId = node.id;

        var detailPanel = document.getElementById('detail-panel');

        if (node.type === 'user') {
            var position = node.data && node.data.position ? node.data.position : '员工';
            var email = node.data && node.data.email ? node.data.email : '-';
            var mobile = node.data && node.data.mobile ? node.data.mobile : '-';
            detailPanel.innerHTML = 
                '<div class="nx-flex nx-items-center nx-gap-4 nx-mb-6">' +
                    '<div class="nx-avatar nx-avatar--lg" style="background: var(--nx-success-light); color: var(--nx-success);"><i class="ri-user-line"></i></div>' +
                    '<div><h3 class="nx-text-lg nx-font-semibold">' + node.name + '</h3><p class="nx-text-secondary">' + position + '</p></div>' +
                '</div>' +
                '<div class="nx-mb-6">' +
                    '<h4 class="nx-text-sm nx-font-semibold nx-text-secondary nx-mb-3"><i class="ri-information-line"></i> 基本信息</h4>' +
                    '<div class="nx-grid nx-grid-cols-2" style="gap: var(--nx-space-3);">' +
                        '<div class="nx-flex nx-justify-between nx-p-2" style="background: var(--nx-bg-elevated); border-radius: var(--nx-radius);"><span class="nx-text-secondary">邮箱</span><span>' + email + '</span></div>' +
                        '<div class="nx-flex nx-justify-between nx-p-2" style="background: var(--nx-bg-elevated); border-radius: var(--nx-radius);"><span class="nx-text-secondary">手机</span><span>' + mobile + '</span></div>' +
                        '<div class="nx-flex nx-justify-between nx-p-2" style="background: var(--nx-bg-elevated); border-radius: var(--nx-radius);"><span class="nx-text-secondary">状态</span><span class="nx-badge nx-badge--success">在职</span></div>' +
                    '</div>' +
                '</div>';
        } else {
            var childCount = node.children ? node.children.length : 0;
            detailPanel.innerHTML = 
                '<div class="nx-flex nx-items-center nx-gap-4 nx-mb-6">' +
                    '<div class="nx-avatar nx-avatar--lg" style="background: var(--nx-primary-light); color: var(--nx-primary);"><i class="ri-building-line"></i></div>' +
                    '<div><h3 class="nx-text-lg nx-font-semibold">' + node.name + '</h3><p class="nx-text-secondary">部门</p></div>' +
                '</div>' +
                '<div class="nx-mb-6">' +
                    '<h4 class="nx-text-sm nx-font-semibold nx-text-secondary nx-mb-3"><i class="ri-information-line"></i> 基本信息</h4>' +
                    '<div class="nx-grid nx-grid-cols-2" style="gap: var(--nx-space-3);">' +
                        '<div class="nx-flex nx-justify-between nx-p-2" style="background: var(--nx-bg-elevated); border-radius: var(--nx-radius);"><span class="nx-text-secondary">部门编号</span><span>' + node.id + '</span></div>' +
                        '<div class="nx-flex nx-justify-between nx-p-2" style="background: var(--nx-bg-elevated); border-radius: var(--nx-radius);"><span class="nx-text-secondary">子部门数</span><span>' + childCount + '</span></div>' +
                    '</div>' +
                '</div>' +
                '<div>' +
                    '<h4 class="nx-text-sm nx-font-semibold nx-text-secondary nx-mb-3"><i class="ri-team-line"></i> 部门成员</h4>' +
                    '<div id="member-list"><div class="nx-text-center nx-py-4 nx-text-secondary">加载中...</div></div>' +
                '</div>';
            loadDepartmentUsers(node.id);
        }
    }

    async function loadDepartmentUsers(deptId) {
        try {
            var response = await fetch('/api/org/user/list', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ departmentId: deptId })
            });
            var rs = await response.json();

            var memberList = document.getElementById('member-list');

            if (rs.requestStatus === 200 && rs.data && rs.data.length > 0) {
                var html = '';
                for (var i = 0; i < rs.data.length; i++) {
                    var user = rs.data[i];
                    html += '<div class="nx-flex nx-items-center nx-gap-3 nx-p-3 nx-mb-2" style="background: var(--nx-bg-elevated); border-radius: var(--nx-radius);">' +
                        '<div class="nx-avatar" style="background: var(--nx-primary-light); color: var(--nx-primary);"><i class="ri-user-line"></i></div>' +
                        '<div class="nx-flex-1">' +
                            '<div class="nx-font-medium">' + user.name + '</div>' +
                            '<div class="nx-text-sm nx-text-secondary">' + (user.position || '员工') + ' · ' + (user.email || '') + '</div>' +
                        '</div>' +
                        '<span class="nx-badge nx-badge--success">' + (user.status === 1 ? '在职' : '离职') + '</span>' +
                    '</div>';
                }
                memberList.innerHTML = html;
            } else {
                memberList.innerHTML = '<div class="nx-text-center nx-py-4 nx-text-secondary">暂无成员</div>';
            }
        } catch (error) {
            console.error('加载部门用户错误:', error);
            document.getElementById('member-list').innerHTML = '<div class="nx-text-center nx-py-4 nx-text-secondary">加载失败</div>';
        }
    }

    async function handleSearch(keyword) {
        if (!keyword || keyword.length < 2) {
            renderOrgTree(orgTreeData);
            return;
        }

        try {
            var response = await fetch('/api/org/user/search', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ keyword: keyword })
            });
            var rs = await response.json();

            if (rs.requestStatus === 200 && rs.data) {
                var container = document.getElementById('org-tree');
                container.innerHTML = '';

                if (rs.data.length === 0) {
                    container.innerHTML = '<li class="nx-text-center nx-p-4 nx-text-secondary">未找到匹配结果</li>';
                    return;
                }

                for (var i = 0; i < rs.data.length; i++) {
                    var user = rs.data[i];
                    var li = document.createElement('li');
                    li.className = 'org-tree-node';
                    li.style.cssText = 'margin: 2px 0;';

                    var nodeDiv = document.createElement('div');
                    nodeDiv.className = 'org-tree-node__content';
                    nodeDiv.style.cssText = 'display: flex; align-items: center; padding: 8px 12px; cursor: pointer; border-radius: var(--nx-radius);';
                    nodeDiv.onmouseenter = function() { this.style.background = 'var(--nx-bg-elevated)'; };
                    nodeDiv.onmouseleave = function() { this.style.background = 'transparent'; };

                    var icon = document.createElement('i');
                    icon.className = 'ri-user-line';
                    icon.style.cssText = 'margin-right: 8px; color: var(--nx-success);';

                    var name = document.createElement('span');
                    name.textContent = user.name;

                    var info = document.createElement('span');
                    info.className = 'nx-text-secondary nx-text-sm';
                    info.style.cssText = 'margin-left: 8px;';
                    info.textContent = user.position || '';

                    nodeDiv.appendChild(icon);
                    nodeDiv.appendChild(name);
                    nodeDiv.appendChild(info);
                    li.appendChild(nodeDiv);
                    container.appendChild(li);
                }
            }
        } catch (error) {
            console.error('搜索用户错误:', error);
        }
    }

    async function syncOrganization() {
        try {
            var btn = event.target.closest('button');
            btn.disabled = true;
            btn.innerHTML = '<i class="ri-loader-4-line" style="animation: spin 1s linear infinite;"></i> 同步中...';

            var response = await fetch('/api/org/sync', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({})
            });
            var rs = await response.json();

            if (rs.requestStatus === 200) {
                alert('组织同步成功');
                loadOrgStats();
                loadOrgTree();
            } else {
                alert(rs.message || '同步失败');
            }
        } catch (error) {
            console.error('同步组织错误:', error);
            alert('同步失败');
        } finally {
            var btn = event.target.closest('button');
            btn.disabled = false;
            btn.innerHTML = '<i class="ri-refresh-line"></i> 同步组织';
        }
    }

    window.OrgManagement = {
        init: init,
        handleSearch: handleSearch,
        syncOrganization: syncOrganization
    };

    window.onPageInit = init;
})();
