// 默认的代码是一个从 ood.Module 派生来的类
// 要确保键值对的值不能包含外部引用
ood.Class('App', 'ood.Module',{
    Instance:{
        // 依赖的类
        Dependencies:[],
        // 需要的模块
        Required:[],

        // 初始化属性
        properties : {},

        // 初始化函数
        initialize : function(){
        },

        // 初始化内部控件（通过界面编辑器生成的代码，大部分是界面控件）
        // *** 如果您不是非常熟悉OOD框架，请慎重手工改变本函数的代码 ***
        iniComponents : function(){
            // [[Code created by EUSUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};

            return children;
            // ]]Code created by EUSUI RAD Studio
        },

        // 可以自定义哪些界面控件将会被加到父容器中
        customAppend : function(parent, subId, left, top){
            // "return false" 表示默认情况下所有的第一层内部界面控件会被加入到父容器
            return false;
        }
        /*,
        // 属性影响本模块的部分
        propSetAction : function(prop){
        },
        // 本模块中所有ood dom节点的定制CSS style
        customStyle:{}
    },
    // 制定义模块的默认属性和事件声明
    Static:{
        $DataModel:{
        },
        $EventHandlers:{
        }
    */
    }
});
