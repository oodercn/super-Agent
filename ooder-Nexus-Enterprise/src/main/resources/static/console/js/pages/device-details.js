(function(global) {
    'use strict';

    var DeviceDetails = {
        currentDeviceId: null,

        init: function() {
            window.onPageInit = function() {
                console.log('设备详情页面初始化完成');
                DeviceDetails.loadPage();
            };
        },

        getDeviceId: function() {
            var urlParams = new URLSearchParams(window.location.search);
            return urlParams.get('id');
        },

        loadPage: function() {
            DeviceDetails.currentDeviceId = DeviceDetails.getDeviceId();
            console.log('加载设备 ID:', DeviceDetails.currentDeviceId);
            if (DeviceDetails.currentDeviceId) {
                DeviceDetails.loadDeviceDetail();
            } else {
                alert('设备ID不存在');
                DeviceDetails.backToList();
            }
        },

        loadDeviceDetail: function() {
            fetch('/api/devices/detail', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ deviceId: DeviceDetails.currentDeviceId })
            })
                .then(function(response) {
                    return response.json();
                })
                .then(function(rs) {
                    if (rs.requestStatus === 200 && rs.data) {
                        DeviceDetails.renderDevice(rs.data);
                    } else {
                        alert(rs.message || '加载设备详情失败');
                        DeviceDetails.backToList();
                    }
                })
                .catch(function(error) {
                    console.error('加载设备详情错误:', error);
                    alert('加载设备详情失败');
                    DeviceDetails.backToList();
                });
        },

        renderDevice: function(device) {
            document.getElementById('deviceName').textContent = device.name || '-';
            document.getElementById('deviceType').textContent = DeviceDetails.getDeviceTypeName(device.type);
            document.getElementById('deviceIp').textContent = device.ip || '-';
            document.getElementById('deviceMac').textContent = device.mac || '-';

            document.getElementById('detailName').textContent = device.name || '-';
            document.getElementById('detailType').textContent = DeviceDetails.getDeviceTypeName(device.type);
            document.getElementById('detailIp').textContent = device.ip || '-';
            document.getElementById('detailMac').textContent = device.mac || '-';
            document.getElementById('detailStatus').textContent = device.status || '-';
            document.getElementById('detailLocation').textContent = device.location || '-';

            var iconClass = DeviceDetails.getDeviceIcon(device.type);
            document.getElementById('deviceIcon').innerHTML = '<i class="ri-' + iconClass + '"></i>';
        },

        getDeviceIcon: function(type) {
            var iconMap = {
                router: 'router-wireless-line',
                switch: 'exchange-line',
                pc: 'computer-line',
                printer: 'printer-line',
                nas: 'hard-drive-line'
            };
            return iconMap[type] || 'device-line';
        },

        getDeviceTypeName: function(type) {
            var typeMap = {
                router: '路由器',
                switch: '交换机',
                pc: '电脑',
                printer: '打印机',
                nas: 'NAS'
            };
            return typeMap[type] || '其他';
        },

        backToList: function() {
            window.location.href = 'network-devices.html';
        },

        showEditModal: function() {
            fetch('/api/devices/detail', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ deviceId: DeviceDetails.currentDeviceId })
            })
                .then(function(response) {
                    return response.json();
                })
                .then(function(rs) {
                    if (rs.requestStatus === 200 && rs.data) {
                        document.getElementById('edit-id').value = rs.data.id;
                        document.getElementById('edit-name').value = rs.data.name || '';
                        document.getElementById('edit-type').value = rs.data.type || 'router';
                        document.getElementById('edit-ip').value = rs.data.ip || '';
                        document.getElementById('edit-mac').value = rs.data.mac || '';
                        document.getElementById('edit-location').value = rs.data.location || '';
                        document.getElementById('edit-modal').style.display = 'flex';
                    } else {
                        alert(rs.message || '获取设备信息失败');
                    }
                })
                .catch(function(error) {
                    console.error('获取设备信息错误:', error);
                    alert('获取设备信息失败');
                });
        },

        closeEditModal: function() {
            document.getElementById('edit-modal').style.display = 'none';
        },

        submitEdit: function() {
            var data = {
                id: document.getElementById('edit-id').value,
                name: document.getElementById('edit-name').value,
                type: document.getElementById('edit-type').value,
                ip: document.getElementById('edit-ip').value,
                mac: document.getElementById('edit-mac').value,
                location: document.getElementById('edit-location').value
            };

            if (!data.name) {
                alert('请填写设备名称');
                return;
            }

            fetch('/api/devices/update', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            })
                .then(function(response) {
                    return response.json();
                })
                .then(function(rs) {
                    if (rs.requestStatus === 200) {
                        DeviceDetails.closeEditModal();
                        alert('设备更新成功');
                        DeviceDetails.loadDeviceDetail();
                    } else {
                        alert(rs.message || '设备更新失败');
                    }
                })
                .catch(function(error) {
                    console.error('更新设备错误:', error);
                    alert('设备更新失败');
                });
        },

        deleteDevice: function() {
            if (!confirm('确定要删除此设备吗？')) return;

            fetch('/api/devices/delete', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ id: DeviceDetails.currentDeviceId })
            })
                .then(function(response) {
                    return response.json();
                })
                .then(function(rs) {
                    if (rs.requestStatus === 200) {
                        alert('设备删除成功');
                        DeviceDetails.backToList();
                    } else {
                        alert(rs.message || '设备删除失败');
                    }
                })
                .catch(function(error) {
                    console.error('删除设备错误:', error);
                    alert('设备删除失败');
                });
        }
    };

    DeviceDetails.init();

    global.backToList = DeviceDetails.backToList;
    global.showEditModal = DeviceDetails.showEditModal;
    global.closeEditModal = DeviceDetails.closeEditModal;
    global.submitEdit = DeviceDetails.submitEdit;
    global.deleteDevice = DeviceDetails.deleteDevice;

})(typeof window !== 'undefined' ? window : this);
