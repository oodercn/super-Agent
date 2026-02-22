(function() {
    'use strict';

    let devices = [];
    let editingId = null;

    async function loadDevices() {
        try {
            const response = await fetch('/api/devices/list', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({})
            });
            const rs = await response.json();
            if (rs.requestStatus === 200 && rs.data) {
                devices = rs.data.devices || [];
                renderDevices();
                updateStats();
            }
        } catch (error) {
            console.error('加载设备列表失败:', error);
        }
    }

    function updateStats() {
        const total = devices.length;
        const online = devices.filter(function(d) { return d.status === 'online'; }).length;
        const offline = total - online;
        document.querySelector('.asset-stats').innerHTML = 
            '<div class="asset-stat-card">' +
                '<div class="asset-stat-value">' + total + '</div>' +
                '<div class="asset-stat-label">总设备数</div>' +
            '</div>' +
            '<div class="asset-stat-card">' +
                '<div class="asset-stat-value" style="color: var(--ns-success);">' + online + '</div>' +
                '<div class="asset-stat-label">在线设备</div>' +
            '</div>' +
            '<div class="asset-stat-card">' +
                '<div class="asset-stat-value" style="color: var(--ns-danger);">' + offline + '</div>' +
                '<div class="asset-stat-label">离线设备</div>' +
            '</div>' +
            '<div class="asset-stat-card">' +
                '<div class="asset-stat-value" style="color: var(--ns-warning);">0</div>' +
                '<div class="asset-stat-label">需要关注</div>' +
            '</div>';
    }

    function renderDevices() {
        var tbody = document.querySelector('table tbody');
        var html = '';
        for (var i = 0; i < devices.length; i++) {
            var d = devices[i];
            html += '<tr>' +
                '<td><div style="display: flex; align-items: center; gap: 8px;">' +
                    '<i class="' + getDeviceIcon(d.type) + '" style="color: var(--nx-primary);"></i>' +
                    (d.name || d.deviceName || '未命名') +
                '</div></td>' +
                '<td>' + (d.ip || d.ipAddress || '-') + '</td>' +
                '<td>' + (d.mac || d.macAddress || '-') + '</td>' +
                '<td>' + (d.type || d.deviceType || '未知') + '</td>' +
                '<td>' + (d.connection || '未知') + '</td>' +
                '<td><span class="badge ' + (d.status === 'online' ? 'badge-success' : 'badge-secondary') + '">' + 
                    (d.status === 'online' ? '在线' : '离线') + '</span></td>' +
                '<td>' + (d.lastSeen || '刚刚') + '</td>' +
                '<td>' +
                    '<button class="btn btn-sm btn-secondary" onclick="DeviceAssets.editDevice(\'' + d.id + '\')">编辑</button> ' +
                    '<button class="btn btn-sm btn-danger" onclick="DeviceAssets.deleteDevice(\'' + d.id + '\')">删除</button>' +
                '</td>' +
            '</tr>';
        }
        tbody.innerHTML = html;
    }

    function getDeviceIcon(type) {
        var icons = {
            'computer': 'ri-computer-line',
            'mobile': 'ri-smartphone-line',
            'iot': 'ri-tv-line',
            'server': 'ri-database-2-line',
            'printer': 'ri-printer-line',
            'game': 'ri-gamepad-line'
        };
        return icons[type] || 'ri-device-line';
    }

    function showAddDevice() {
        editingId = null;
        document.getElementById('deviceModalTitle').textContent = '添加设备';
        document.getElementById('deviceForm').reset();
        document.getElementById('deviceModal').style.display = 'flex';
    }

    function editDevice(id) {
        editingId = id;
        var device = null;
        for (var i = 0; i < devices.length; i++) {
            if (devices[i].id === id) {
                device = devices[i];
                break;
            }
        }
        if (device) {
            document.getElementById('deviceModalTitle').textContent = '编辑设备';
            document.getElementById('deviceName').value = device.name || device.deviceName || '';
            document.getElementById('deviceIp').value = device.ip || device.ipAddress || '';
            document.getElementById('deviceMac').value = device.mac || device.macAddress || '';
            document.getElementById('deviceType').value = device.type || device.deviceType || '';
            document.getElementById('deviceModal').style.display = 'flex';
        }
    }

    function closeDeviceModal() {
        document.getElementById('deviceModal').style.display = 'none';
    }

    async function saveDevice() {
        var data = {
            name: document.getElementById('deviceName').value,
            ip: document.getElementById('deviceIp').value,
            mac: document.getElementById('deviceMac').value,
            type: document.getElementById('deviceType').value
        };

        if (!data.name) {
            alert('请填写设备名称');
            return;
        }

        try {
            var url = editingId ? '/api/devices/update' : '/api/devices/add';
            if (editingId) data.id = editingId;
            var response = await fetch(url, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });
            var rs = await response.json();
            if (rs.requestStatus === 200) {
                closeDeviceModal();
                loadDevices();
                alert(editingId ? '设备更新成功' : '设备添加成功');
            } else {
                alert(rs.message || '操作失败');
            }
        } catch (error) {
            console.error('保存设备失败:', error);
            alert('保存设备失败');
        }
    }

    async function deleteDevice(id) {
        if (!confirm('确定要删除此设备吗？')) return;
        try {
            var response = await fetch('/api/devices/delete', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ id: id })
            });
            var rs = await response.json();
            if (rs.requestStatus === 200) {
                loadDevices();
                alert('设备删除成功');
            } else {
                alert(rs.message || '删除失败');
            }
        } catch (error) {
            console.error('删除设备失败:', error);
            alert('删除设备失败');
        }
    }

    function init() {
        loadDevices();
    }

    window.DeviceAssets = {
        init: init,
        showAddDevice: showAddDevice,
        editDevice: editDevice,
        deleteDevice: deleteDevice,
        closeDeviceModal: closeDeviceModal,
        saveDevice: saveDevice
    };

    window.onPageInit = init;
})();
