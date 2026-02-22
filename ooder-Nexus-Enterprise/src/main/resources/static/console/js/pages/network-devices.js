(function(global) {
    'use strict';

    var NetworkDevices = {
        allDevices: [],
        currentPage: 1,
        pageSize: 10,
        filteredDevices: [],

        init: function() {
            window.onPageInit = function() {
                console.log('网络设备管理页面初始化完成');
                NetworkDevices.loadDevicesFromApi();
            };

            document.getElementById('searchInput').addEventListener('keyup', function(e) {
                if (e.key === 'Enter') {
                    NetworkDevices.searchDevices();
                }
            });

            document.getElementById('statusFilter').addEventListener('change', function() {
                NetworkDevices.searchDevices();
            });

            document.getElementById('typeFilter').addEventListener('change', function() {
                NetworkDevices.searchDevices();
            });
        },

        loadDevicesFromApi: function() {
            var tableBody = document.getElementById('deviceTableBody');
            tableBody.innerHTML = '<tr><td colspan="6" style="text-align: center; padding: 40px;"><i class="ri-loader-4-line ri-spin"></i> 加载中...</td></tr>';

            fetch('/api/devices/list', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({})
            })
                .then(function(response) {
                    return response.json();
                })
                .then(function(rs) {
                    if ((rs.requestStatus === 200 || rs.code === 200) && rs.data) {
                        NetworkDevices.allDevices = rs.data.devices.map(function(device) {
                            return {
                                id: device.id,
                                name: device.name,
                                type: device.type,
                                ip: device.ip || 'N/A',
                                mac: device.mac || 'N/A',
                                status: device.status,
                                description: device.description || ''
                            };
                        });
                        NetworkDevices.filteredDevices = NetworkDevices.allDevices.slice();
                        NetworkDevices.renderDevices();
                    } else {
                        NetworkDevices.showError('加载设备数据失败: ' + (rs.message || '未知错误'));
                    }
                })
                .catch(function(error) {
                    console.error('加载设备数据失败:', error);
                    NetworkDevices.showError('加载设备数据失败，请检查网络连接');
                });
        },

        showError: function(message) {
            var tableBody = document.getElementById('deviceTableBody');
            tableBody.innerHTML = '<tr><td colspan="6" style="text-align: center; padding: 40px; color: var(--nx-danger);"><i class="ri-error-warning-line"></i> ' + message + '</td></tr>';
        },

        renderDevices: function() {
            var tableBody = document.getElementById('deviceTableBody');
            tableBody.innerHTML = '';

            if (NetworkDevices.filteredDevices.length === 0) {
                tableBody.innerHTML = '<tr><td colspan="6" style="text-align: center; padding: 40px;">暂无设备数据</td></tr>';
                return;
            }

            var startIndex = (NetworkDevices.currentPage - 1) * NetworkDevices.pageSize;
            var endIndex = startIndex + NetworkDevices.pageSize;
            var pageDevices = NetworkDevices.filteredDevices.slice(startIndex, endIndex);

            pageDevices.forEach(function(device) {
                var row = document.createElement('tr');
                var statusClass = device.status === 'online' ? 'success' : device.status === 'warning' ? 'warning' : 'danger';
                row.innerHTML = 
                    '<td><i class="ri-' + NetworkDevices.getDeviceIcon(device.type) + '"></i> ' + device.name + '</td>' +
                    '<td>' + NetworkDevices.getDeviceTypeName(device.type) + '</td>' +
                    '<td>' + device.ip + '</td>' +
                    '<td>' + device.mac + '</td>' +
                    '<td><span class="nx-text-' + statusClass + '">' + NetworkDevices.getStatusText(device.status) + '</span></td>' +
                    '<td>' +
                        '<button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="viewDevice(\'' + device.id + '\')"><i class="ri-eye-line"></i></button> ' +
                        '<button class="nx-btn nx-btn--sm nx-btn--primary" onclick="editDevice(\'' + device.id + '\')"><i class="ri-edit-line"></i></button> ' +
                        '<button class="nx-btn nx-btn--sm nx-btn--danger" onclick="deleteDevice(\'' + device.id + '\')"><i class="ri-delete-line"></i></button>' +
                    '</td>';
                tableBody.appendChild(row);
            });
        },

        getDeviceIcon: function(type) {
            var iconMap = {
                router: 'router-wireless-line',
                switch: 'exchange-line',
                pc: 'computer-line',
                printer: 'printer-line',
                nas: 'hard-drive-line',
                other: 'device-line'
            };
            return iconMap[type] || 'device-line';
        },

        getDeviceTypeName: function(type) {
            var typeMap = {
                router: '路由器',
                switch: '交换机',
                pc: '电脑',
                printer: '打印机',
                nas: 'NAS',
                other: '其他'
            };
            return typeMap[type] || '其他';
        },

        getStatusText: function(status) {
            var statusMap = {
                online: '在线',
                offline: '离线',
                warning: '警告'
            };
            return statusMap[status] || '未知';
        },

        searchDevices: function() {
            var searchTerm = document.getElementById('searchInput').value.toLowerCase();
            var statusFilter = document.getElementById('statusFilter').value;
            var typeFilter = document.getElementById('typeFilter').value;

            NetworkDevices.filteredDevices = NetworkDevices.allDevices.filter(function(device) {
                var matchesSearch = !searchTerm || 
                    device.name.toLowerCase().indexOf(searchTerm) !== -1 || 
                    device.ip.toLowerCase().indexOf(searchTerm) !== -1 ||
                    device.mac.toLowerCase().indexOf(searchTerm) !== -1;
                var matchesStatus = statusFilter === 'all' || device.status === statusFilter;
                var matchesType = typeFilter === 'all' || device.type === typeFilter;
                return matchesSearch && matchesStatus && matchesType;
            });
            NetworkDevices.currentPage = 1;
            NetworkDevices.renderDevices();
        },

        addDevice: function() {
            alert('添加设备功能开发中...');
        },

        viewDevice: function(id) {
            window.location.href = 'device-details.html?id=' + id;
        },

        editDevice: function(id) {
            fetch('/api/devices/detail', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ deviceId: id })
            })
                .then(function(response) {
                    return response.json();
                })
                .then(function(rs) {
                    if (rs.requestStatus === 200 && rs.data) {
                        document.getElementById('edit-device-id').value = rs.data.id;
                        document.getElementById('edit-device-name').value = rs.data.name || '';
                        document.getElementById('edit-device-type').value = rs.data.type || '';
                        document.getElementById('edit-device-location').value = rs.data.location || '';
                        document.getElementById('edit-device-modal').style.display = 'flex';
                    } else {
                        alert(rs.message || '获取设备信息失败');
                    }
                })
                .catch(function(error) {
                    console.error('获取设备信息错误:', error);
                    alert('获取设备信息失败');
                });
        },

        closeEditDeviceModal: function() {
            document.getElementById('edit-device-modal').style.display = 'none';
        },

        submitEditDevice: function() {
            var id = document.getElementById('edit-device-id').value;
            var name = document.getElementById('edit-device-name').value;
            var type = document.getElementById('edit-device-type').value;
            var location = document.getElementById('edit-device-location').value;

            if (!name) {
                alert('请填写设备名称');
                return;
            }

            fetch('/api/devices/update', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ id: id, name: name, type: type, location: location })
            })
                .then(function(response) {
                    return response.json();
                })
                .then(function(rs) {
                    if (rs.requestStatus === 200) {
                        NetworkDevices.closeEditDeviceModal();
                        alert('设备更新成功');
                        NetworkDevices.loadDevicesFromApi();
                    } else {
                        alert(rs.message || '设备更新失败');
                    }
                })
                .catch(function(error) {
                    console.error('更新设备错误:', error);
                    alert('设备更新失败');
                });
        },

        deleteDevice: function(id) {
            if (!confirm('确定要删除此设备吗？')) return;

            fetch('/api/devices/delete', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ id: id })
            })
                .then(function(response) {
                    return response.json();
                })
                .then(function(rs) {
                    if (rs.requestStatus === 200) {
                        alert('设备删除成功');
                        NetworkDevices.loadDevicesFromApi();
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

    NetworkDevices.init();

    global.addDevice = NetworkDevices.addDevice;
    global.viewDevice = NetworkDevices.viewDevice;
    global.editDevice = NetworkDevices.editDevice;
    global.closeEditDeviceModal = NetworkDevices.closeEditDeviceModal;
    global.submitEditDevice = NetworkDevices.submitEditDevice;
    global.deleteDevice = NetworkDevices.deleteDevice;

})(typeof window !== 'undefined' ? window : this);
