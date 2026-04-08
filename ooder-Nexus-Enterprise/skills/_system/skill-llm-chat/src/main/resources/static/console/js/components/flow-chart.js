/**
 * FlowChart - 流程图可视化组件
 * 
 * 功能：
 * 1. 流程图渲染（节点、连线）
 * 2. 节点拖拽
 * 3. 缩放和平移
 * 4. 节点选择和编辑
 * 5. 状态可视化
 */

class FlowChart {
    constructor(container, options = {}) {
        this.container = typeof container === 'string' 
            ? document.querySelector(container) 
            : container;
        
        this.options = {
            editable: true,
            showMinimap: true,
            showToolbar: true,
            showLegend: true,
            nodeWidth: 160,
            nodeHeight: 60,
            gridSize: 20,
            snapToGrid: true,
            ...options
        };
        
        this.nodes = [];
        this.edges = [];
        this.selectedNode = null;
        this.selectedEdge = null;
        this.scale = 1;
        this.translateX = 0;
        this.translateY = 0;
        this.isDragging = false;
        this.dragStartX = 0;
        this.dragStartY = 0;
        this.nodeIdCounter = 0;
        
        this.init();
    }
    
    init() {
        this.container.classList.add('flow-chart-container');
        this.render();
        this.bindEvents();
    }
    
    render() {
        let html = '<div class="flow-chart-canvas" id="flowCanvas">';
        
        if (this.options.showToolbar) {
            html += `
                <div class="flow-chart-toolbar">
                    <button class="toolbar-btn" onclick="this.closest('.flow-chart-container')._flowChart.zoomIn()" title="放大">
                        <i class="ri-zoom-in-line"></i>
                    </button>
                    <button class="toolbar-btn" onclick="this.closest('.flow-chart-container')._flowChart.zoomOut()" title="缩小">
                        <i class="ri-zoom-out-line"></i>
                    </button>
                    <button class="toolbar-btn" onclick="this.closest('.flow-chart-container')._flowChart.fitView()" title="适应视图">
                        <i class="ri-aspect-ratio-line"></i>
                    </button>
                    <button class="toolbar-btn" onclick="this.closest('.flow-chart-container')._flowChart.resetView()" title="重置视图">
                        <i class="ri-restart-line"></i>
                    </button>
                </div>
            `;
        }
        
        html += '<svg class="flow-edges-svg" style="position:absolute;top:0;left:0;width:100%;height:100%;pointer-events:none;"></svg>';
        html += '<div class="flow-nodes-container"></div>';
        
        if (this.options.showMinimap) {
            html += '<div class="flow-chart-minimap"><div class="minimap-viewport"></div></div>';
        }
        
        html += '</div>';
        
        if (this.options.showLegend) {
            html += `
                <div class="flow-chart-legend">
                    <div class="legend-item">
                        <span class="legend-dot completed"></span>
                        <span>已完成</span>
                    </div>
                    <div class="legend-item">
                        <span class="legend-dot in-progress"></span>
                        <span>进行中</span>
                    </div>
                    <div class="legend-item">
                        <span class="legend-dot pending"></span>
                        <span>待执行</span>
                    </div>
                    <div class="legend-item">
                        <span class="legend-dot error"></span>
                        <span>异常</span>
                    </div>
                </div>
            `;
        }
        
        this.container.innerHTML = html;
        this.container._flowChart = this;
        
        this.canvas = this.container.querySelector('.flow-chart-canvas');
        this.nodesContainer = this.container.querySelector('.flow-nodes-container');
        this.edgesSvg = this.container.querySelector('.flow-edges-svg');
    }
    
    bindEvents() {
        this.canvas.addEventListener('mousedown', this.onCanvasMouseDown.bind(this));
        this.canvas.addEventListener('mousemove', this.onCanvasMouseMove.bind(this));
        this.canvas.addEventListener('mouseup', this.onCanvasMouseUp.bind(this));
        this.canvas.addEventListener('wheel', this.onCanvasWheel.bind(this));
        this.canvas.addEventListener('contextmenu', this.onContextMenu.bind(this));
    }
    
    onCanvasMouseDown(e) {
        if (e.target === this.canvas || e.target.classList.contains('flow-nodes-container')) {
            this.isDragging = true;
            this.dragStartX = e.clientX - this.translateX;
            this.dragStartY = e.clientY - this.translateY;
            this.canvas.style.cursor = 'grabbing';
            this.deselectAll();
        }
    }
    
    onCanvasMouseMove(e) {
        if (this.isDragging && !this.selectedNode) {
            this.translateX = e.clientX - this.dragStartX;
            this.translateY = e.clientY - this.dragStartY;
            this.updateTransform();
        }
    }
    
    onCanvasMouseUp(e) {
        this.isDragging = false;
        this.canvas.style.cursor = 'default';
    }
    
    onCanvasWheel(e) {
        e.preventDefault();
        const delta = e.deltaY > 0 ? -0.1 : 0.1;
        const newScale = Math.max(0.25, Math.min(2, this.scale + delta));
        
        const rect = this.canvas.getBoundingClientRect();
        const mouseX = e.clientX - rect.left;
        const mouseY = e.clientY - rect.top;
        
        this.translateX = mouseX - (mouseX - this.translateX) * (newScale / this.scale);
        this.translateY = mouseY - (mouseY - this.translateY) * (newScale / this.scale);
        this.scale = newScale;
        
        this.updateTransform();
    }
    
    onContextMenu(e) {
        e.preventDefault();
    }
    
    updateTransform() {
        this.nodesContainer.style.transform = `translate(${this.translateX}px, ${this.translateY}px) scale(${this.scale})`;
        this.edgesSvg.style.transform = `translate(${this.translateX}px, ${this.translateY}px) scale(${this.scale})`;
        this.updateMinimap();
    }
    
    deselectAll() {
        this.selectedNode = null;
        this.selectedEdge = null;
        this.container.querySelectorAll('.flow-node.selected').forEach(n => n.classList.remove('selected'));
    }
    
    addNode(nodeData) {
        const node = {
            id: nodeData.id || `node_${++this.nodeIdCounter}`,
            type: nodeData.type || 'step',
            title: nodeData.title || '未命名',
            content: nodeData.content || '',
            status: nodeData.status || 'pending',
            role: nodeData.role || '',
            x: nodeData.x || 100,
            y: nodeData.y || 100,
            ...nodeData
        };
        
        this.nodes.push(node);
        this.renderNode(node);
        return node;
    }
    
    renderNode(node) {
        const nodeEl = document.createElement('div');
        nodeEl.className = `flow-node ${node.type}-node ${node.status}`;
        nodeEl.id = node.id;
        nodeEl.style.left = `${node.x}px`;
        nodeEl.style.top = `${node.y}px`;
        nodeEl.dataset.nodeId = node.id;
        
        let statusIcon = '';
        if (node.status === 'completed') {
            statusIcon = '<i class="ri-check-line"></i>';
        } else if (node.status === 'in-progress') {
            statusIcon = '<i class="ri-loader-4-line"></i>';
        } else if (node.status === 'error') {
            statusIcon = '<i class="ri-error-warning-line"></i>';
        }
        
        if (node.type === 'start' || node.type === 'end') {
            nodeEl.innerHTML = `
                <i class="ri-${node.type === 'start' ? 'play' : 'stop'}-line"></i>
                ${statusIcon ? `<span class="node-status ${node.status}">${statusIcon}</span>` : ''}
            `;
        } else if (node.type === 'gateway') {
            nodeEl.innerHTML = `
                <div class="node-content">
                    <i class="ri-git-branch-line"></i>
                </div>
                ${statusIcon ? `<span class="node-status ${node.status}">${statusIcon}</span>` : ''}
            `;
        } else {
            nodeEl.innerHTML = `
                <div class="node-header">
                    <i class="node-icon ri-${this.getNodeIcon(node.type)}"></i>
                    <span class="node-title">${node.title}</span>
                </div>
                ${node.content ? `<div class="node-content">${node.content}</div>` : ''}
                ${node.role ? `<div class="node-role"><i class="ri-user-line"></i> ${node.role}</div>` : ''}
                ${statusIcon ? `<span class="node-status ${node.status}">${statusIcon}</span>` : ''}
            `;
        }
        
        if (this.options.editable) {
            this.makeNodeDraggable(nodeEl, node);
        }
        
        nodeEl.addEventListener('click', (e) => {
            e.stopPropagation();
            this.selectNode(node, nodeEl);
        });
        
        this.nodesContainer.appendChild(nodeEl);
    }
    
    getNodeIcon(type) {
        const icons = {
            'step': 'checkbox-circle-line',
            'approval': 'user-follow-line',
            'parallel': 'git-branch-line',
            'exclusive': 'git-merge-line',
            'subprocess': 'stack-line'
        };
        return icons[type] || 'checkbox-circle-line';
    }
    
    makeNodeDraggable(nodeEl, node) {
        let startX, startY, initialX, initialY;
        
        nodeEl.addEventListener('mousedown', (e) => {
            if (e.button !== 0) return;
            e.stopPropagation();
            
            startX = e.clientX;
            startY = e.clientY;
            initialX = node.x;
            initialY = node.y;
            
            const onMouseMove = (e) => {
                const dx = (e.clientX - startX) / this.scale;
                const dy = (e.clientY - startY) / this.scale;
                
                let newX = initialX + dx;
                let newY = initialY + dy;
                
                if (this.options.snapToGrid) {
                    newX = Math.round(newX / this.options.gridSize) * this.options.gridSize;
                    newY = Math.round(newY / this.options.gridSize) * this.options.gridSize;
                }
                
                node.x = newX;
                node.y = newY;
                nodeEl.style.left = `${newX}px`;
                nodeEl.style.top = `${newY}px`;
                
                this.renderEdges();
                this.updateMinimap();
            };
            
            const onMouseUp = () => {
                document.removeEventListener('mousemove', onMouseMove);
                document.removeEventListener('mouseup', onMouseUp);
                
                if (this.options.onNodeMoved) {
                    this.options.onNodeMoved(node);
                }
            };
            
            document.addEventListener('mousemove', onMouseMove);
            document.addEventListener('mouseup', onMouseUp);
        });
    }
    
    selectNode(node, nodeEl) {
        this.deselectAll();
        this.selectedNode = node;
        nodeEl.classList.add('selected');
        
        if (this.options.onNodeSelected) {
            this.options.onNodeSelected(node);
        }
    }
    
    addEdge(edgeData) {
        const edge = {
            id: edgeData.id || `edge_${Date.now()}`,
            source: edgeData.source,
            target: edgeData.target,
            label: edgeData.label || '',
            status: edgeData.status || 'pending',
            ...edgeData
        };
        
        this.edges.push(edge);
        this.renderEdges();
        return edge;
    }
    
    renderEdges() {
        let svgContent = '';
        
        this.edges.forEach(edge => {
            const sourceNode = this.nodes.find(n => n.id === edge.source);
            const targetNode = this.nodes.find(n => n.id === edge.target);
            
            if (!sourceNode || !targetNode) return;
            
            const sourceX = sourceNode.x + (this.options.nodeWidth / 2);
            const sourceY = sourceNode.y + this.options.nodeHeight;
            const targetX = targetNode.x + (this.options.nodeWidth / 2);
            const targetY = targetNode.y;
            
            const path = this.createEdgePath(sourceX, sourceY, targetX, targetY);
            
            svgContent += `
                <g class="flow-edge ${edge.status}" data-edge-id="${edge.id}">
                    <path d="${path}" marker-end="url(#arrowhead-${edge.status})"></path>
                    ${edge.label ? `<text x="${(sourceX + targetX) / 2}" y="${(sourceY + targetY) / 2 - 10}" 
                        class="flow-edge-label" text-anchor="middle">${edge.label}</text>` : ''}
                </g>
            `;
        });
        
        const defs = `
            <defs>
                <marker id="arrowhead-pending" markerWidth="10" markerHeight="7" refX="9" refY="3.5" orient="auto">
                    <polygon points="0 0, 10 3.5, 0 7" class="flow-edge-arrow" />
                </marker>
                <marker id="arrowhead-completed" markerWidth="10" markerHeight="7" refX="9" refY="3.5" orient="auto">
                    <polygon points="0 0, 10 3.5, 0 7" class="flow-edge-arrow" fill="#22c55e" />
                </marker>
                <marker id="arrowhead-active" markerWidth="10" markerHeight="7" refX="9" refY="3.5" orient="auto">
                    <polygon points="0 0, 10 3.5, 0 7" class="flow-edge-arrow" fill="#3b82f6" />
                </marker>
            </defs>
        `;
        
        this.edgesSvg.innerHTML = defs + svgContent;
    }
    
    createEdgePath(x1, y1, x2, y2) {
        const midY = (y1 + y2) / 2;
        return `M ${x1} ${y1} C ${x1} ${midY}, ${x2} ${midY}, ${x2} ${y2}`;
    }
    
    loadFlowData(data) {
        this.clear();
        
        if (data.nodes) {
            data.nodes.forEach(node => this.addNode(node));
        }
        
        if (data.edges) {
            data.edges.forEach(edge => this.addEdge(edge));
        }
        
        this.fitView();
    }
    
    clear() {
        this.nodes = [];
        this.edges = [];
        this.nodesContainer.innerHTML = '';
        this.edgesSvg.innerHTML = '';
    }
    
    zoomIn() {
        this.scale = Math.min(2, this.scale + 0.1);
        this.updateTransform();
    }
    
    zoomOut() {
        this.scale = Math.max(0.25, this.scale - 0.1);
        this.updateTransform();
    }
    
    fitView() {
        if (this.nodes.length === 0) return;
        
        let minX = Infinity, minY = Infinity, maxX = -Infinity, maxY = -Infinity;
        
        this.nodes.forEach(node => {
            minX = Math.min(minX, node.x);
            minY = Math.min(minY, node.y);
            maxX = Math.max(maxX, node.x + this.options.nodeWidth);
            maxY = Math.max(maxY, node.y + this.options.nodeHeight);
        });
        
        const padding = 50;
        const contentWidth = maxX - minX + padding * 2;
        const contentHeight = maxY - minY + padding * 2;
        
        const containerRect = this.canvas.getBoundingClientRect();
        const scaleX = containerRect.width / contentWidth;
        const scaleY = containerRect.height / contentHeight;
        
        this.scale = Math.min(scaleX, scaleY, 1);
        this.translateX = (containerRect.width - contentWidth * this.scale) / 2 - minX * this.scale + padding * this.scale;
        this.translateY = (containerRect.height - contentHeight * this.scale) / 2 - minY * this.scale + padding * this.scale;
        
        this.updateTransform();
    }
    
    resetView() {
        this.scale = 1;
        this.translateX = 0;
        this.translateY = 0;
        this.updateTransform();
    }
    
    updateMinimap() {
        if (!this.options.showMinimap) return;
        
        const minimap = this.container.querySelector('.flow-chart-minimap');
        const viewport = minimap.querySelector('.minimap-viewport');
        
        const scale = 0.1;
        
        minimap.querySelectorAll('.minimap-node').forEach(n => n.remove());
        
        this.nodes.forEach(node => {
            const nodeEl = document.createElement('div');
            nodeEl.className = `minimap-node ${node.status}`;
            nodeEl.style.left = `${node.x * scale}px`;
            nodeEl.style.top = `${node.y * scale}px`;
            nodeEl.style.width = `${this.options.nodeWidth * scale}px`;
            nodeEl.style.height = `${this.options.nodeHeight * scale}px`;
            minimap.appendChild(nodeEl);
        });
        
        const containerRect = this.canvas.getBoundingClientRect();
        viewport.style.width = `${containerRect.width * scale / this.scale}px`;
        viewport.style.height = `${containerRect.height * scale / this.scale}px`;
        viewport.style.left = `${-this.translateX * scale / this.scale}px`;
        viewport.style.top = `${-this.translateY * scale / this.scale}px`;
    }
    
    updateNodeStatus(nodeId, status) {
        const node = this.nodes.find(n => n.id === nodeId);
        if (!node) return;
        
        node.status = status;
        const nodeEl = this.container.querySelector(`[data-node-id="${nodeId}"]`);
        if (nodeEl) {
            nodeEl.className = `flow-node ${node.type}-node ${status}`;
            
            let statusIcon = '';
            if (status === 'completed') {
                statusIcon = '<i class="ri-check-line"></i>';
            } else if (status === 'in-progress') {
                statusIcon = '<i class="ri-loader-4-line"></i>';
            } else if (status === 'error') {
                statusIcon = '<i class="ri-error-warning-line"></i>';
            }
            
            let statusEl = nodeEl.querySelector('.node-status');
            if (statusIcon) {
                if (statusEl) {
                    statusEl.className = `node-status ${status}`;
                    statusEl.innerHTML = statusIcon;
                } else {
                    nodeEl.insertAdjacentHTML('beforeend', 
                        `<span class="node-status ${status}">${statusIcon}</span>`);
                }
            } else if (statusEl) {
                statusEl.remove();
            }
        }
        
        this.updateMinimap();
    }
    
    getFlowData() {
        return {
            nodes: this.nodes.map(n => ({...n})),
            edges: this.edges.map(e => ({...e}))
        };
    }
    
    exportAsImage() {
        return new Promise((resolve) => {
            const data = this.getFlowData();
            resolve(JSON.stringify(data, null, 2));
        });
    }
}

if (typeof module !== 'undefined' && module.exports) {
    module.exports = FlowChart;
}

window.FlowChart = FlowChart;
