<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta http-equiv="Content-Style-Type" content="text/css"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta http-equiv="imagetoolbar" content="no"/>
    <meta name="viewport"
          content="user-scalable=no, initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0,width=device-width, height=device-height"/>
    <meta http-equiv="no-cache">
    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="content-type" content="no-cache, must-revalidate"/>
    <meta http-equiv="expires" content="-1"/>
    <link rel="stylesheet" type="text/css" href="/RAD/css/default.css"/>
    <link rel="stylesheet" type="text/css" href="/ood/iconfont/iconfont.css"/>
    <link rel="stylesheet" type="text/css" href="/plugins/formlayout/handsontable.full.min.css"/>
    <link rel="stylesheet" type="text/css" href="/ood/bpm/bpmfont.css"/>
    <link type="text/css" href="/ood/fontawesome/font-awesome.min.css" rel="stylesheet">
    <link href="https://fastly.jsdelivr.net/npm/remixicon@3.5.0/fonts/remixicon.css" rel="stylesheet">
    <link type="text/css" href="/ood/appearance/webflat/theme.css" rel="stylesheet">
    <#list $CssFills as item>
        <link rel="stylesheet" type="text/css" href="/root/${item.path}"/>
    </#list>
    <script type="text/javascript" src="/ood/ThirdParty/hammer.js"></script>
    <script type="text/javascript" src="/RAD/ood.js"></script>
    <script type="text/javascript" src="/RAD/index.js"></script>
    <title>${projectName}调试界面custom</title>
</head>


<body>
<div id="loading" style="position:fixed;width:100%;text-align:center;">
    <img id="loadingimg" alt="Loading..." title="Loading..." src="/RAD/img/loading.gif"/>
</div>
</body>


<script type="text/javascript">
    if (/#.*touch\=(1|true)/.test(location.href)) {
        window.ood_ini = {fakeTouch: 1};
        document.body.className += " ood-cursor-touch";
    }
</script>

<!-- 修改前 -->
<script type="text/javascript">
    var args = ood.getUrlParams(),
            onEnd = function () {
                this.setData(args);
                ood('loading').remove();
                this.initData();

            };
    ood.launch('${className}', onEnd, 'cn', args && args.theme || 'default');


    $E = ood.execExpression;
</script>

<!-- 修改后 -->
<script type="text/javascript">
    var args = ood.getUrlParams(),
            onEnd = function () {
                this.setData(args);
                ood('loading').remove();
                this.initData();

            };
    ood.launch('${className}', onEnd, 'cn', args && args.theme || 'default');


    $E = ood.execExpression;
</script>
    $BPD = {
        open: function () {
            $E('$BPD.open()')
        },
        close: function () {
            $E('$BPD.close()')
        }
    }
    $ESD = {
        export: function (url, params) {
            var paramArr = params | {};
            if (url) {
                paramArr.url = url;
            }
            $E('$ESD.export()', paramArr)
        },
        reload: function (packageName, params) {
            var paramArr = params || {projectName: "${projectName}"};
            if (packageName) {
                paramArr.packageName = packageName;
                paramArr.handleId = window.handleId;
            }
            $E('$ESD.reload()', paramArr)
        },
        open: function (url, params) {
            var paramArr = params | {};
            if (url) {
                paramArr.url = url;
            }
            $E('$ESD.open()', paramArr)
        },
        quit: function () {
            $E('$BPD.quit()')
        },
        logout: function () {
            $E('$BPD.logout()')
        }
    }
</script>
</html>
