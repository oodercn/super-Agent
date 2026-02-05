/**
 * 注解测试服务类
 * 用于演示ooder注解规范中服务参数设计的正确使用方式
 */
ood.Class("ood.Mobile.OA.AnnotationTestService", ["ood.Service"], {
    Instance: {
        initialize: function() {
            this.constructor.upper.prototype.initialize.apply(this, arguments);
        },
        
        // 简单参数使用示例 - 根据ID获取数据
        getDataById: function(id) {
            // 模拟API调用
            return new Promise(function(resolve) {
                setTimeout(function() {
                    resolve({
                        id: id,
                        name: "测试数据 " + id,
                        description: "这是ID为 " + id + " 的测试数据"
                    });
                }, 100);
            });
        },
        
        // 复杂视图对象参数使用示例 - 提交表单数据
        submitData: function(viewData) {
            // 模拟API调用
            return new Promise(function(resolve, reject) {
                setTimeout(function() {
                    if (viewData && viewData.name) {
                        resolve({
                            success: true,
                            message: "数据提交成功",
                            data: {
                                id: Math.floor(Math.random() * 1000),
                                ...viewData
                            }
                        });
                    } else {
                        reject({
                            success: false,
                            message: "数据提交失败，缺少必要字段"
                        });
                    }
                }, 200);
            });
        },
        
        // 多参数使用示例 - 复杂查询
        queryData: function(queryParams) {
            // 模拟API调用
            return new Promise(function(resolve) {
                setTimeout(function() {
                    var results = [];
                    for (var i = 1; i <= 5; i++) {
                        results.push({
                            id: i,
                            name: queryParams.keyword ? queryParams.keyword + " - 结果 " + i : "结果 " + i,
                            description: "查询结果 " + i,
                            category: queryParams.category || "默认分类"
                        });
                    }
                    resolve(results);
                }, 150);
            });
        }
    },
    
    Static: {}
});