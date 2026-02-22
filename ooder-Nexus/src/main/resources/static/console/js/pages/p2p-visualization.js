(function(global) {
    'use strict';

    var networkData = {
        nodes: [
            { id: 'node-001', name: '主节点', type: 'self', status: 'active', x: 400, y: 100 },
            { id: 'node-002', name: '节点A', type: 'active', status: 'active', x: 200, y: 200 },
            { id: 'node-003', name: '节点B', type: 'active', status: 'active', x: 600, y: 200 },
            { id: 'node-004', name: '节点C', type: 'active', status: 'active', x: 100, y: 300 },
            { id: 'node-005', name: '节点D', type: 'inactive', status: 'inactive', x: 300, y: 300 },
            { id: 'node-006', name: '节点E', type: 'active', status: 'active', x: 500, y: 300 },
            { id: 'node-007', name: '节点F', type: 'active', status: 'active', x: 700, y: 300 }
        ],
        links: [
            { source: 'node-001', target: 'node-002' },
            { source: 'node-001', target: 'node-003' },
            { source: 'node-002', target: 'node-004' },
            { source: 'node-002', target: 'node-005' },
            { source: 'node-003', target: 'node-006' },
            { source: 'node-003', target: 'node-007' }
        ]
    };

    var P2PVisualization = {
        init: function() {
            window.onPageInit = function() {
                console.log('P2P网络可视化页面初始化完成');
                P2PVisualization.renderTopology();
                P2PVisualization.updateStats();
            };
        },

        renderTopology: function() {
            var svg = document.getElementById('topology-svg');
            svg.innerHTML = '';

            networkData.links.forEach(function(link) {
                var source = networkData.nodes.find(function(n) { return n.id === link.source; });
                var target = networkData.nodes.find(function(n) { return n.id === link.target; });
                if (source && target) {
                    var line = document.createElementNS('http://www.w3.org/2000/svg', 'line');
                    line.setAttribute('x1', source.x);
                    line.setAttribute('y1', source.y);
                    line.setAttribute('x2', target.x);
                    line.setAttribute('y2', target.y);
                    line.setAttribute('stroke', 'var(--nx-border)');
                    line.setAttribute('stroke-width', '2');
                    svg.appendChild(line);
                }
            });

            networkData.nodes.forEach(function(node) {
                var g = document.createElementNS('http://www.w3.org/2000/svg', 'g');
                var color = node.type === 'self' ? '#3b82f6' : node.status === 'active' ? '#22c55e' : '#ef4444';

                var circle = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
                circle.setAttribute('cx', node.x);
                circle.setAttribute('cy', node.y);
                circle.setAttribute('r', '20');
                circle.setAttribute('fill', color);

                var text = document.createElementNS('http://www.w3.org/2000/svg', 'text');
                text.setAttribute('x', node.x);
                text.setAttribute('y', node.y + 35);
                text.setAttribute('text-anchor', 'middle');
                text.setAttribute('fill', 'var(--nx-text-primary)');
                text.setAttribute('font-size', '12');
                text.textContent = node.name;

                g.appendChild(circle);
                g.appendChild(text);
                svg.appendChild(g);
            });
        },

        updateStats: function() {
            document.getElementById('stat-nodes').textContent = networkData.nodes.length;
            var onlineCount = 0;
            networkData.nodes.forEach(function(n) {
                if (n.status === 'active') onlineCount++;
            });
            document.getElementById('stat-online').textContent = onlineCount;
            document.getElementById('stat-packets').textContent = Math.floor(Math.random() * 1000);
            document.getElementById('stat-skills').textContent = Math.floor(Math.random() * 50);
        },

        refreshTopology: function() {
            P2PVisualization.renderTopology();
            P2PVisualization.updateStats();
        },

        resetZoom: function() {
            P2PVisualization.renderTopology();
        },

        discoverNodes: function() {
            alert('正在发现新节点...');
        },

        loadSkillMarket: function() {
            alert('加载技能市场...');
        }
    };

    P2PVisualization.init();

    global.refreshTopology = P2PVisualization.refreshTopology;
    global.resetZoom = P2PVisualization.resetZoom;
    global.discoverNodes = P2PVisualization.discoverNodes;
    global.loadSkillMarket = P2PVisualization.loadSkillMarket;

})(typeof window !== 'undefined' ? window : this);
