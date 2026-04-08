var DictCache = (function() {
    var cache = {};
    var loadingPromises = {};
    var initialized = false;

    var DICT_CODES = {
        CAPABILITY_TYPE: 'capability_type',
        PARTICIPANT_TYPE: 'participant_type',
        PARTICIPANT_ROLE: 'participant_role',
        PARTICIPANT_STATUS: 'participant_status',
        SCENE_GROUP_STATUS: 'scene_group_status',
        SCENE_TYPE: 'scene_type',
        CONNECTOR_TYPE: 'connector_type',
        CAPABILITY_PROVIDER_TYPE: 'capability_provider_type',
        CAPABILITY_BINDING_STATUS: 'capability_binding_status',
        TEMPLATE_STATUS: 'template_status',
        TEMPLATE_CATEGORY: 'template_category',
        KEY_TYPE: 'key_type',
        KEY_STATUS: 'key_status',
        AUDIT_EVENT_TYPE: 'audit_event_type',
        AUDIT_RESULT_TYPE: 'audit_result_type'
    };

    function getDict(code) {
        return new Promise(function(resolve, reject) {
            if (cache[code]) {
                resolve(cache[code]);
                return;
            }

            if (loadingPromises[code]) {
                loadingPromises[code].then(resolve).catch(reject);
                return;
            }

            loadingPromises[code] = fetch('/api/v1/dicts/' + code)
                .then(function(response) {
                    return response.json();
                })
                .then(function(result) {
                    if (result.code === 200 && result.data) {
                        cache[code] = result.data;
                        resolve(result.data);
                    } else {
                        reject(new Error('Failed to load dict: ' + code));
                    }
                })
                .catch(function(error) {
                    console.error('Error loading dict:', code, error);
                    reject(error);
                })
                .finally(function() {
                    delete loadingPromises[code];
                });

            loadingPromises[code].then(resolve).catch(reject);
        });
    }

    function getDictItems(code) {
        return getDict(code).then(function(dict) {
            return dict.items || [];
        });
    }

    function getDictItemName(code, itemCode) {
        return getDictItems(code).then(function(items) {
            for (var i = 0; i < items.length; i++) {
                if (items[i].code === itemCode) {
                    return items[i].name;
                }
            }
            return itemCode;
        });
    }

    function getDictItem(code, itemCode) {
        return getDictItems(code).then(function(items) {
            for (var i = 0; i < items.length; i++) {
                if (items[i].code === itemCode) {
                    return items[i];
                }
            }
            return null;
        });
    }

    function preloadAll() {
        var promises = [];
        for (var key in DICT_CODES) {
            if (DICT_CODES.hasOwnProperty(key)) {
                promises.push(getDict(DICT_CODES[key]));
            }
        }
        return Promise.all(promises);
    }

    function init() {
        if (initialized) {
            return Promise.resolve();
        }
        initialized = true;
        return preloadAll();
    }

    function clearCache() {
        cache = {};
        initialized = false;
    }

    function renderSelectOptions(selectElement, code, options) {
        options = options || {};
        var defaultOption = options.defaultOption || { value: '', text: '请选择' };
        var valueField = options.valueField || 'code';
        var textField = options.textField || 'name';
        var selectedValue = options.selectedValue || selectElement.value;

        getDictItems(code).then(function(items) {
            var html = '<option value="' + defaultOption.value + '">' + defaultOption.text + '</option>';
            items.forEach(function(item) {
                var selected = item[valueField] === selectedValue ? ' selected' : '';
                html += '<option value="' + item[valueField] + '"' + selected + '>' + item[textField] + '</option>';
            });
            selectElement.innerHTML = html;
        });
    }

    function createSelectElement(code, options) {
        options = options || {};
        var select = document.createElement('select');
        select.className = options.className || 'nx-select';
        select.id = options.id || '';
        select.name = options.name || '';
        
        if (options.onchange) {
            select.onchange = options.onchange;
        }

        renderSelectOptions(select, code, options);
        return select;
    }

    return {
        DICT_CODES: DICT_CODES,
        init: init,
        getDict: getDict,
        getDictItems: getDictItems,
        getDictItemName: getDictItemName,
        getDictItem: getDictItem,
        preloadAll: preloadAll,
        clearCache: clearCache,
        renderSelectOptions: renderSelectOptions,
        createSelectElement: createSelectElement
    };
})();

if (typeof module !== 'undefined' && module.exports) {
    module.exports = DictCache;
}
