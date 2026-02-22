(function(global) {
    'use strict';

    var topologyData = {
        nodes: [],
        edges: []
    };

    var NetworkTopology = {
        init: function() {
            window.onPageInit = function() {
                console.log('网络拓扑页面初始化完成');
                NetworkTopology.loadTopology();
            };
        },

        loadTopology: async function() {
            try {
                var response = await fetch('/api/network/path/topology');
                var result = await response.json();
                
                if (result.requestStatus === 200 && result.data) {
                    topologyData.nodes = result.data.nodes || [];
                    topologyData.edges = result.data.edges || [];
                    NetworkTopology.renderTopology();
                    NetworkTopology.updateStatusOverview(result.data);
                } else {
                    console.warn('使用模拟数据');
                    NetworkTopology.loadMockData();
                }
            } catch (error) {
                console.error('加载拓扑数据失败:', error);
                NetworkTopology.loadMockData();
            }
        },

        loadMockData: function() {
            topologyData.nodes = [
                { id: 'mcp-001', name: '主Nexus', type: 'mcp', x: 400, y: 80, status: 'active' },
                { id: 'route-001', name: '路由代理1', type: 'route', x: 250, y: 200, status: 'active' },
                { id: 'route-002', name: '路由代理2', type: 'route', x: 550, y: 200, status: 'active' },
                { id: 'end-001', name: '智能灯泡', type: 'end', x: 150, y: 320, status: 'active' },
                { id: 'end-002', name: '智能插座', type: 'end', x: 350, y: 320, status: 'active' },
                { id: 'end-003', name: '摄像头', type: 'end', x: 450, y: 320, status: 'inactive' },
                { id: 'end-004', name: '智能音箱', type: 'end', x: 650, y: 320, status: 'active' }
            ];
            topologyData.edges = [
                { id: 'e1', source: 'mcp-001', target: 'route-001', type: 'DIRECT' },
                { id: 'e2', source: 'mcp-001', target: 'route-002', type: 'DIRECT' },
                { id: 'e3', source: 'route-001', target: 'end-001', type: 'DIRECT' },
                { id: 'e4', source: 'route-001', target: 'end-002', type: 'DIRECT' },
                { id: 'e5', source: 'route-002', target: 'end-003', type: 'RELAY' },
                { id: 'e6', source: 'route-002', target: 'end-004', type: 'DIRECT' }
            ];
            NetworkTopology.renderTopology();
            NetworkTopology.updateStatusOverview({
                nodeCount: topologyData.nodes.length,
                edgeCount: topologyData.edges.length
            });
        },

        renderTopology: function() {
            var svg = document.getElementById('topologySvg');
            svg.innerHTML = '';

            var nodePositions = {};
            topologyData.nodes.forEach(function(node, index) {
                var row = Math.floor(index / 3);
                var col = index % 3;
                nodePositions[node.id] = {
                    x: 150 + col * 250,
                    y: 100 + row * 150,
                    name: node.name || node.id,
                    type: node.type || 'unknown',
                    status: node.status || 'active'
                };
            });

            topologyData.edges.forEach(function(edge) {
                var source = nodePositions[edge.source];
                var target = nodePositions[edge.target];
                if (source && target) {
                    var line = document.createElementNS('http://www.w3.org/2000/svg', 'line');
                    line.setAttribute('x1', source.x);
                    line.setAttribute('y1', source.y);
                    line.setAttribute('x2', target.x);
                    line.setAttribute('y2', target.y);
                    line.setAttribute('stroke', NetworkTopology.getEdgeColor(edge.type));
                    line.setAttribute('stroke-width', '2');
                    line.setAttribute('data-source', edge.source);
                    line.setAttribute('data-target', edge.target);
                    line.setAttribute('id', 'edge-' + edge.id);
                    svg.appendChild(line);
                }
            });

            Object.keys(nodePositions).forEach(function(nodeId) {
                var node = nodePositions[nodeId];
                var g = document.createElementNS('http://www.w3.org/2000/svg', 'g');
                g.setAttribute('style', 'cursor: pointer;');
                g.setAttribute('onclick', 'selectNode("' + nodeId + '")');

                var circle = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
                circle.setAttribute('cx', node.x);
                circle.setAttribute('cy', node.y);
                circle.setAttribute('r', '25');
                circle.setAttribute('fill', NetworkTopology.getNodeColor(node.type));
                circle.setAttribute('stroke', node.status === 'active' ? '#22c55e' : '#ef4444');
                circle.setAttribute('stroke-width', '3');

                var text = document.createElementNS('http://www.w3.org/2000/svg', 'text');
                text.setAttribute('x', node.x);
                text.setAttribute('y', node.y + 5);
                text.setAttribute('text-anchor', 'middle');
                text.setAttribute('fill', 'white');
                text.setAttribute('font-size', '12');
                text.textContent = node.name.substring(0, 4);

                var label = document.createElementNS('http://www.w3.org/2000/svg', 'text');
                label.setAttribute('x', node.x);
                label.setAttribute('y', node.y + 45);
                label.setAttribute('text-anchor', 'middle');
                label.setAttribute('fill', 'var(--nx-text-primary)');
                label.setAttribute('font-size', '11');
                label.textContent = node.name;

                g.appendChild(circle);
                g.appendChild(text);
                g.appendChild(label);
                svg.appendChild(g);
            });
        },

        getNodeColor: function(type) {
            var colors = { mcp: '#3b82f6', route: '#22c55e', end: '#f59e0b', gateway: '#8b5cf6' };
            return colors[type] || '#64748b';
        },

        getEdgeColor: function(type) {
            var colors = { DIRECT: '#22c55e', ROUTED: '#3b82f6', TUNNEL: '#8b5cf6', P2P: '#f59e0b', RELAY: '#ef4444' };
            return colors[type] || '#64748b';
        },

        updateStatusOverview: function(data) {
            document.getElementById('totalNodes').textContent = data.nodeCount || topologyData.nodes.length;
            document.getElementById('totalConnections').textContent = data.edgeCount || topologyData.edges.length;
            var activeCount = 0;
            topologyData.nodes.forEach(function(n) {
                if (n.status === 'active') activeCount++;
            });
            document.getElementById('activeNodes').textContent = activeCount;
        },

        findPath: async function() {
            var sourceId = document.getElementById('pathSource').value;
            var targetId = document.getElementById('pathTarget').value;
            
            if (!sourceId || !targetId) {
                alert('请输入源节点和目标节点');
                return;
            }
            
            try {
                var response = await fetch('/api/network/path/optimal', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ sourceId: sourceId, targetId: targetId })
                });
                
                var result = await response.json();
                if (result.requestStatus === 200 && result.data) {
                    NetworkTopology.highlightPath(result.data.path);
                    alert('找到路径，跳数: ' + result.data.hopCount);
                } else {
                    alert('未找到路径');
                }
            } catch (error) {
                console.error('查找路径失败:', error);
                alert('查找路径失败');
            }
        },

        highlightPath: function(path) {
            var svg = document.getElementById('topologySvg');
            var lines = svg.querySelectorAll('line');
            lines.forEach(function(line) {
                line.setAttribute('stroke-width', '2');
                line.style.filter = '';
            });
            
            if (!path || path.length === 0) return;
            
            path.forEach(function(link) {
                var edge = document.getElementById('edge-' + link.linkId);
                if (edge) {
                    edge.setAttribute('stroke-width', '4');
                    edge.style.filter = 'drop-shadow(0 0 6px #22c55e)';
                }
            });
        },

        selectNode: function(nodeId) {
            document.getElementById('pathSource').value = nodeId;
            alert('已选择节点: ' + nodeId);
        },

        refreshTopology: function() {
            NetworkTopology.loadTopology();
        },

        exportTopology: function() {
            var data = {
                nodes: topologyData.nodes,
                edges: topologyData.edges,
                exportTime: new Date().toISOString()
            };
            var json = JSON.stringify(data, null, 2);
            var blob = new Blob([json], { type: 'application/json' });
            var url = URL.createObjectURL(blob);
            var a = document.createElement('a');
            a.href = url;
            a.download = 'network-topology.json';
            a.click();
            URL.revokeObjectURL(url);
        }
    };

    NetworkTopology.init();

    global.refreshTopology = NetworkTopology.refreshTopology;
    global.exportTopology = NetworkTopology.exportTopology;
    global.findPath = NetworkTopology.findPath;
    global.selectNode = NetworkTopology.selectNode;
    global.NetworkTopology = NetworkTopology;

})(typeof window !== 'undefined' ? window : this);
