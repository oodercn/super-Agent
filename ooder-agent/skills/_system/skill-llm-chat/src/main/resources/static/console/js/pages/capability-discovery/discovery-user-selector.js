(function(global) {
'use strict';

var state = DiscoveryState;

var DiscoveryUserSelector = {
    show: function(type, title, callback) {
        var modal = document.getElementById('userSelectorModal');
        if (!modal) {
            modal = document.createElement('div');
            modal.id = 'userSelectorModal';
            modal.className = 'modal-overlay';
            modal.innerHTML = 
                '<div class="modal-content user-selector-modal">' +
                '<div class="modal-header">' +
                '<h3>' + title + '</h3>' +
                '<button class="modal-close" onclick="closeUserSelector()"><i class="ri-close-line"></i></button>' +
                '</div>' +
                '<div class="modal-body">' +
                '<div class="user-search">' +
                '<input type="text" id="userSearchInput" class="form-input" placeholder="搜索用户..." oninput="searchUsers(this.value)">' +
                '</div>' +
                '<div class="user-list" id="userSelectorList">' +
                '<div class="user-loading">加载中...</div>' +
                '</div>' +
                '</div>' +
                '</div>';
            document.body.appendChild(modal);
        }
        
        modal.style.display = 'flex';
        window._userSelectorCallback = callback;
        
        DiscoveryUserSelector.loadUsers();
    },
    
    loadUsers: function(keyword) {
        var list = document.getElementById('userSelectorList');
        if (!list) return;
        
        list.innerHTML = '<div class="user-loading">加载中...</div>';
        
        ApiClient.get('/api/v1/org/users', { keyword: keyword || '' })
            .then(function(result) {
                var users = result.data || result || [];
                if (users.length === 0) {
                    list.innerHTML = '<div class="user-empty">暂无用户</div>';
                    return;
                }
                
                var html = '';
                users.forEach(function(user) {
                    var name = user.name || user.username || user;
                    html += '<div class="user-item" onclick="selectUser(this)" data-user=\'' + JSON.stringify(user) + '\'>' +
                        '<div class="user-avatar"><i class="ri-user-line"></i></div>' +
                        '<div class="user-info">' +
                        '<div class="user-name">' + name + '</div>' +
                        (user.email ? '<div class="user-email">' + user.email + '</div>' : '') +
                        '</div>' +
                        '</div>';
                });
                list.innerHTML = html;
            })
            .catch(function(error) {
                console.error('[loadUsers] Error:', error);
                list.innerHTML = '<div class="user-empty">加载失败，请重试</div>';
            });
    }
};

global.DiscoveryUserSelector = DiscoveryUserSelector;
global.closeUserSelector = function() {
    var modal = document.getElementById('userSelectorModal');
    if (modal) {
        modal.style.display = 'none';
    }
};
global.selectUser = function(el) {
    var userJson = el.dataset.user;
    if (userJson) {
        var user = JSON.parse(userJson);
        if (window._userSelectorCallback) {
            window._userSelectorCallback(user);
        }
    }
    global.closeUserSelector();
}
global.searchUsers = function(keyword) {
    DiscoveryUserSelector.loadUsers(keyword);
}

})(typeof window !== 'undefined' ? window : this);
