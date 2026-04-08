(function(global) {
'use strict';

var state = DiscoveryState;

var DiscoveryFilter = {
    renderBusinessCategoryFilter: function() {
        var container = document.getElementById('businessCategoryFilter');
        if (!container) return;
        
        fetch('/api/v1/discovery/categories/user-facing')
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result && result.status === 'success' && result.data) {
                    DiscoveryFilter.renderUserFacingCategories(result.data);
                } else {
                    DiscoveryFilter.renderDefaultCategories();
                }
            })
            .catch(function(error) {
                console.error('加载用户可见分类失败:', error);
                DiscoveryFilter.renderDefaultCategories();
            });
    },

    renderUserFacingCategories: function(categories) {
        var container = document.getElementById('businessCategoryFilter');
        if (!container) return;
        var html = '<span class="filter-group-label">业务领域:</span>';
        html += '<span class="filter-chip" data-bc="" onclick="selectBusinessCategory(\'\')" title="全部业务领域"><i class="ri-apps-line"></i> 全部</span>';
        categories.forEach(function(cat) {
            html += '<span class="filter-chip" data-bc="' + cat.id + '" onclick="selectBusinessCategory(\'' + cat.id + '\')" title="' + (cat.desc || cat.name) + '">' +
                '<i class="' + cat.icon + '"></i> ' + cat.name + '</span>';
        });
        container.innerHTML = html;
    },

    renderDefaultCategories: function() {
        var container = document.getElementById('businessCategoryFilter');
        if (!container) return;
        var userFacingCategories = CategoryService.getUserFacing();
        var html = '<span class="filter-group-label">业务领域:</span>';
        html += '<span class="filter-chip" data-bc="" onclick="selectBusinessCategory(\'\')" title="全部业务领域"><i class="ri-apps-line"></i> 全部</span>';
        userFacingCategories.forEach(function(cat) {
            html += '<span class="filter-chip" data-bc="' + cat.code + '" onclick="selectBusinessCategory(\'' + cat.code + '\')" title="' + (cat.desc || cat.name) + '">' +
                '<i class="' + cat.icon + '"></i> ' + cat.name + '</span>';
        });
        container.innerHTML = html;
    },

    initFilters: function() {
        var primaryFilterContainer = document.querySelector('.results-filter--primary');
        if (primaryFilterContainer) {
            primaryFilterContainer.addEventListener('click', function(e) {
                var chip = e.target.closest('.filter-chip');
                if (!chip || chip.dataset.bc !== undefined) return;
                primaryFilterContainer.querySelectorAll('.filter-chip').forEach(function(c) { c.classList.remove('active'); });
                chip.classList.add('active');
                var filter = chip.dataset.filter;
                DiscoveryFilter.applyFilter(filter);
            });
        }
        
        var businessFilterContainer = document.getElementById('businessCategoryFilter');
        if (businessFilterContainer) {
            businessFilterContainer.addEventListener('click', function(e) {
                var chip = e.target.closest('.filter-chip');
                if (!chip || chip.dataset.bc === undefined) return;
                businessFilterContainer.querySelectorAll('.filter-chip').forEach(function(c) { c.classList.remove('active'); });
                chip.classList.add('active');
                var bc = chip.dataset.bc;
                state.selectedBusinessCategory = bc;
                DiscoveryFilter.applyBusinessCategoryFilter(bc);
            });
        }
        
        var scoreFilterContainer = document.getElementById('scoreFilter');
        if (scoreFilterContainer) {
            scoreFilterContainer.addEventListener('click', function(e) {
                var chip = e.target.closest('.filter-chip');
                if (!chip || chip.dataset.score === undefined) return;
                scoreFilterContainer.querySelectorAll('.filter-chip').forEach(function(c) { c.classList.remove('active'); });
                chip.classList.add('active');
                DiscoveryFilter.applyScoreFilter(chip.dataset.score);
            });
        }
    },

    applyScoreFilter: function(scoreRange) {
        var container = document.getElementById('resultsBody');
        if (!container) return;
        var items = container.querySelectorAll('.result-item');
        var visibleCount = 0;
        
        items.forEach(function(item) {
            var score = parseInt(item.dataset.score) || 0;
            var show = false;
            
            if (scoreRange === 'all') {
                show = true;
            } else if (scoreRange === 'high' && score >= 8) {
                show = true;
            } else if (scoreRange === 'medium' && score >= 3 && score < 8) {
                show = true;
            } else if (scoreRange === 'low' && score < 3) {
                show = true;
            }
            
            if (show && item.style.display !== 'none') {
                visibleCount++;
            }
            
            if (show) {
                item.style.display = '';
            } else {
                item.style.display = 'none';
            }
        });
        
        document.getElementById('resultsCount').textContent = visibleCount;
    },

    applyBusinessCategoryFilter: function(bc) {
        var container = document.getElementById('resultsBody');
        if (!container) return;
        var items = container.querySelectorAll('.result-item');
        var visibleCount = 0;
        
        items.forEach(function(item) {
            var itemBc = item.dataset.businessCategory;
            var show = !bc || itemBc === bc;
            
            if (show) {
                item.style.display = '';
                visibleCount++;
            } else {
                item.style.display = 'none';
            }
        });
        
        document.getElementById('resultsCount').textContent = visibleCount;
    },

    selectBusinessCategory: function(bc) {
        state.selectedBusinessCategory = bc;
        var container = document.getElementById('businessCategoryFilter');
        if (!container) return;
        container.querySelectorAll('.filter-chip').forEach(function(chip) {
            chip.classList.remove('active');
            if (chip.dataset.bc === bc) {
                chip.classList.add('active');
            }
        });
        DiscoveryFilter.applyBusinessCategoryFilter(bc);
    },

    applyFilter: function(filter) {
        var visibleCount = 0;
        var container = document.getElementById('resultsBody');
        if (!container) return;
        var items = container.querySelectorAll('.result-item');
        items.forEach(function(item) {
            var skillForm = item.dataset.skillForm;
            var sceneType = item.dataset.sceneType;
            var businessCategory = item.dataset.businessCategory;
            var installed = item.dataset.installed === 'true';
            var show = false;
            if (filter === 'all') { show = true; }
            else if (filter === 'scene' && skillForm === 'SCENE') { show = true; }
            else if (filter === 'provider' && skillForm === 'PROVIDER') { show = true; }
            else if (filter === 'driver' && skillForm === 'DRIVER') { show = true; }
            else if (filter === 'new' && !installed) { show = true; }
            else if (filter === 'installed' && installed) { show = true; }
            else if (filter && filter.startsWith('bc_')) {
                var bc = filter.replace('bc_', '');
                if (businessCategory === bc) { show = true; }
            }
            item.style.display = show ? '' : 'none';
            if (show) { visibleCount++; }
        });
        document.getElementById('resultsCount').textContent = visibleCount;
    },

    updateScoreFilterCounts: function(scoreCounts) {
        var el;
        el = document.getElementById('scoreCountAll'); if (el) el.textContent = state.discoveredCapabilities.length;
        el = document.getElementById('scoreCountHigh'); if (el) el.textContent = scoreCounts.high;
        el = document.getElementById('scoreCountMedium'); if (el) el.textContent = scoreCounts.medium;
        el = document.getElementById('scoreCountLow'); if (el) el.textContent = scoreCounts.low;
    },

    updateFilterCounts: function(counts, businessCounts) {
        var el;
        el = document.getElementById('filterCountAll'); if (el) el.textContent = state.discoveredCapabilities.length;
        el = document.getElementById('filterCountScene'); if (el) el.textContent = counts.scene;
        el = document.getElementById('filterCountProvider'); if (el) el.textContent = counts.provider;
        el = document.getElementById('filterCountDriver'); if (el) el.textContent = counts.driver;
        el = document.getElementById('filterCountNew'); if (el) el.textContent = counts.new;
        el = document.getElementById('filterCountInstalled'); if (el) el.textContent = counts.installed;
    }
};

global.DiscoveryFilter = DiscoveryFilter;
global.selectBusinessCategory = function(bc) { DiscoveryFilter.selectBusinessCategory(bc); };

})(typeof window !== 'undefined' ? window : this);
