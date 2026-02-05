ood.Class('RAD.AudioManager', 'ood.Module', {
    Instance: {
        Dependencies: [],
        Required: [],
        events: {},
        initialize: function () {
        },

        iniComponents: function () {
            // [[Code created by Audio Quality System
            var host = this, children = [], properties = {}, append = function (child) {
                children.push(child.get(0));
            };
            ood.merge(properties, this.properties);

            // API调用器 - 获取录音文件列表
            append(
                ood.create("ood.APICaller")
                    .setHost(host, "getAudioList")
                    .setName("getAudioList")
                    .setAutoRun(true)
                    .setQueryURL("/audio/getAudioList")
                    .setQueryMethod("POST")
                    .setRequestDataSource([
                        {
                            "name": "searchForm",
                            "path": "",
                            "type": "form"
                        }
                    ])
                    .setResponseDataTarget([
                        {
                            "name": "audioGrid",
                            "path": "data",
                            "type": "grid"
                        }
                    ])
                    .setResponseCallback([])
            );

            // API调用器 - 上传录音文件
            append(
                ood.create("ood.APICaller")
                    .setHost(host, "uploadAudio")
                    .setName("uploadAudio")
                    .setQueryURL("/audio/uploadAudio")
                    .setQueryMethod("POST")
                    .setRequestDataSource([
                        {
                            "name": "uploadDialog",
                            "path": "",
                            "type": "form"
                        }
                    ])
                    .setResponseDataTarget([])
                    .setResponseCallback([])
                    .onData("_upload_success")
            );

            // API调用器 - 删除录音文件
            append(
                ood.create("ood.APICaller")
                    .setHost(host, "deleteAudio")
                    .setName("deleteAudio")
                    .setQueryURL("/audio/deleteAudio")
                    .setQueryMethod("POST")
                    .setRequestDataSource([])
                    .setResponseDataTarget([])
                    .setResponseCallback([])
                    .onData("_delete_success")
            );

            // API调用器 - 获取录音详情
            append(
                ood.create("ood.APICaller")
                    .setHost(host, "getAudioInfo")
                    .setName("getAudioInfo")
                    .setQueryURL("/audio/getAudioInfo")
                    .setQueryMethod("POST")
                    .setRequestDataSource([])
                    .setResponseDataTarget([
                        {
                            "name": "detailDialog",
                            "path": "data",
                            "type": "form"
                        }
                    ])
                    .setResponseCallback([])
            );

            // 主容器
            append(
                ood.create("ood.UI.Block")
                    .setHost(host, "mainContainer")
                    .setDock("fill")
            );

            // 工具栏
            host.mainContainer.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "toolbar")
                    .setDock("top")
                    .setHeight("4em")
                    .setStyleDesc("background: #f8f9fa; border-bottom: 1px solid #ddd; padding: 0.5em;")
            );

            // 搜索表单
            host.toolbar.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "searchForm")
                    .setName("searchForm")
                    .setLeft("0em")
                    .setTop("0em")
                    .setWidth("50em")
                    .setHeight("3em")
                    .setStyleDesc("display: flex; align-items: center; gap: 1em;")
            );

            // 项目ID搜索
            host.searchForm.append(
                (new ood.UI.ComboInput())
                    .setHost(host, "projectId")
                    .setName("projectId")
                    .setWidth("12em")
                    .setLabelCaption("项目ID:")
                    .setLabelSize("4em")
                    .setType("textinput")
                    .setPlaceholder("输入项目ID")
            );

            // 状态筛选
            host.searchForm.append(
                (new ood.UI.ComboInput())
                    .setHost(host, "status")
                    .setName("status")
                    .setWidth("10em")
                    .setLabelCaption("状态:")
                    .setLabelSize("3em")
                    .setType("dropdownlist")
                    .setItems([
                        { "id": "", "caption": "全部" },
                        { "id": "PENDING", "caption": "待处理" },
                        { "id": "PROCESSING", "caption": "处理中" },
                        { "id": "COMPLETED", "caption": "已完成" },
                        { "id": "FAILED", "caption": "失败" }
                    ])
            );

            // 文件名搜索
            host.searchForm.append(
                (new ood.UI.ComboInput())
                    .setHost(host, "pattern")
                    .setName("pattern")
                    .setWidth("12em")
                    .setLabelCaption("文件名:")
                    .setLabelSize("4em")
                    .setType("textinput")
                    .setPlaceholder("输入文件名关键词")
            );

            // 搜索按钮
            host.searchForm.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "searchBtn")
                    .setCaption("搜索")
                    .setImageClass("ri-search-line")
                    .setWidth("6em")
                    .onClick("_search")
            );

            // 上传按钮
            host.toolbar.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "uploadBtn")
                    .setLeft("52em")
                    .setCaption("上传录音")
                    .setImageClass("ri-upload-line")
                    .setWidth("8em")
                    .onClick("_showUploadDialog")
            );

            // 刷新按钮
            host.toolbar.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "refreshBtn")
                    .setLeft("61em")
                    .setCaption("刷新")
                    .setImageClass("ri-refresh-line")
                    .setWidth("6em")
                    .onClick("_refresh")
            );

            // 录音文件列表网格
            host.mainContainer.append(
                ood.create("ood.UI.Grid")
                    .setHost(host, "audioGrid")
                    .setName("audioGrid")
                    .setDock("fill")
                    .setColumns([
                        {
                            "id": "fileName",
                            "caption": "文件名",
                            "width": "200",
                            "type": "ro"
                        },
                        {
                            "id": "format",
                            "caption": "格式",
                            "width": "80",
                            "type": "ro"
                        },
                        {
                            "id": "duration",
                            "caption": "时长(秒)",
                            "width": "100",
                            "type": "ro"
                        },
                        {
                            "id": "fileSize",
                            "caption": "文件大小",
                            "width": "120",
                            "type": "ro",
                            "render": "_formatFileSize"
                        },
                        {
                            "id": "status",
                            "caption": "状态",
                            "width": "100",
                            "type": "ro",
                            "render": "_formatStatus"
                        },
                        {
                            "id": "uploadTime",
                            "caption": "上传时间",
                            "width": "160",
                            "type": "ro",
                            "render": "_formatTime"
                        },
                        {
                            "id": "actions",
                            "caption": "操作",
                            "width": "200",
                            "type": "template",
                            "template": "<button onclick='{page}.viewDetail(\"{row.id}\")' class='btn btn-sm btn-info'>查看</button> " +
                                      "<button onclick='{page}.downloadAudio(\"{row.id}\")' class='btn btn-sm btn-success'>下载</button> " +
                                      "<button onclick='{page}.deleteAudio(\"{row.id}\")' class='btn btn-sm btn-danger'>删除</button>"
                        }
                    ])
                    .setPageSize(20)
                    .setShowPage(true)
            );

            // 上传对话框
            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host, "uploadDialog")
                    .setName("uploadDialog")
                    .setCaption("上传录音文件")
                    .setImageClass("ri-upload-line")
                    .setWidth("30em")
                    .setHeight("20em")
                    .setVisible(false)
            );

            // 上传表单内容
            host.uploadDialog.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "uploadForm")
                    .setDock("fill")
                    .setStyleDesc("padding: 1em;")
            );

            // 项目ID输入
            host.uploadForm.append(
                (new ood.UI.ComboInput())
                    .setHost(host, "uploadProjectId")
                    .setName("projectId")
                    .setDock("top")
                    .setHeight("3em")
                    .setLabelCaption("项目ID:")
                    .setLabelSize("6em")
                    .setType("textinput")
                    .setPlaceholder("输入项目ID（可选）")
            );

            // 文件选择
            host.uploadForm.append(
                (new ood.UI.ComboInput())
                    .setHost(host, "fileInput")
                    .setName("file")
                    .setDock("top")
                    .setHeight("3em")
                    .setLabelCaption("选择文件:")
                    .setLabelSize("6em")
                    .setType("file")
                    .setAccept("audio/*")
            );

            // 上传提示
            host.uploadForm.append(
                ood.create("ood.UI.Label")
                    .setHost(host, "uploadTip")
                    .setDock("top")
                    .setHeight("4em")
                    .setCaption("支持格式：WAV, MP3, M4A, AAC, FLAC, OGG<br/>最大文件大小：100MB")
                    .setStyleDesc("color: #666; font-size: 12px; margin-top: 1em;")
            );

            // 上传按钮区
            host.uploadForm.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "uploadBtnArea")
                    .setDock("bottom")
                    .setHeight("3em")
                    .setStyleDesc("text-align: center; margin-top: 1em;")
            );

            host.uploadBtnArea.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "confirmUploadBtn")
                    .setCaption("上传")
                    .setImageClass("ri-upload-line")
                    .setWidth("6em")
                    .onClick("_doUpload")
            );

            host.uploadBtnArea.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "cancelUploadBtn")
                    .setLeft("8em")
                    .setCaption("取消")
                    .setImageClass("ri-close-line")
                    .setWidth("6em")
                    .onClick("_cancelUpload")
            );

            // 详情对话框
            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host, "detailDialog")
                    .setName("detailDialog")
                    .setCaption("录音文件详情")
                    .setImageClass("ri-information-line")
                    .setWidth("40em")
                    .setHeight("30em")
                    .setVisible(false)
            );

            // 详情表单
            host.detailDialog.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "detailForm")
                    .setDock("fill")
                    .setStyleDesc("padding: 1em;")
            );

            // 详情字段
            var detailFields = [
                { name: "id", label: "文件ID:" },
                { name: "fileName", label: "文件名:" },
                { name: "format", label: "格式:" },
                { name: "duration", label: "时长(秒):" },
                { name: "fileSize", label: "文件大小:" },
                { name: "status", label: "状态:" },
                { name: "transcriptionText", label: "语音识别结果:" },
                { name: "averageVolume", label: "平均音量:" },
                { name: "speechRate", label: "语速:" },
                { name: "silenceDuration", label: "静音时长:" }
            ];

            detailFields.forEach(function(field, index) {
                host.detailForm.append(
                    (new ood.UI.ComboInput())
                        .setHost(host, "detail_" + field.name)
                        .setName(field.name)
                        .setDock("top")
                        .setHeight("3em")
                        .setLabelCaption(field.label)
                        .setLabelSize("8em")
                        .setType(field.name === "transcriptionText" ? "textarea" : "textinput")
                        .setReadOnly(true)
                        .setStyleDesc("margin-bottom: 0.5em;")
                );
            });

            // 详情关闭按钮
            host.detailDialog.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "detailBtnArea")
                    .setDock("bottom")
                    .setHeight("3em")
                    .setStyleDesc("text-align: center; padding: 1em;")
            );

            host.detailBtnArea.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "closeDetailBtn")
                    .setCaption("关闭")
                    .setImageClass("ri-close-line")
                    .setWidth("6em")
                    .onClick("_closeDetail")
            );

            this.setDomNode(ood.create("div").append(children));
            // ]]Code created by JDSEasy RAD Studio
        },

        // 搜索录音文件
        _search: function() {
            this.getAudioList.invoke();
        },

        // 刷新列表
        _refresh: function() {
            this.getAudioList.invoke();
        },

        // 显示上传对话框
        _showUploadDialog: function() {
            this.uploadDialog.show();
        },

        // 取消上传
        _cancelUpload: function() {
            this.uploadDialog.hide();
            this.fileInput.setValue('');
            this.uploadProjectId.setValue('');
        },

        // 执行上传
        _doUpload: function() {
            if (!this.fileInput.getValue()) {
                alert("请选择要上传的文件");
                return;
            }
            this.uploadAudio.invoke();
        },

        // 上传成功回调
        _upload_success: function(caller, data) {
            if (data.errcode) {
                alert("上传失败: " + data.errdes);
            } else {
                alert("上传成功");
                this._cancelUpload();
                this._refresh();
            }
        },

        // 查看详情
        viewDetail: function(audioId) {
            this.getAudioInfo.setQueryData({ audioId: audioId });
            this.getAudioInfo.invoke();
            this.detailDialog.show();
        },

        // 关闭详情
        _closeDetail: function() {
            this.detailDialog.hide();
        },

        // 下载录音文件
        downloadAudio: function(audioId) {
            window.open("/audio/downloadAudio?audioId=" + audioId, "_blank");
        },

        // 删除录音文件
        deleteAudio: function(audioId) {
            if (confirm("确定要删除这个录音文件吗？")) {
                this.deleteAudio.setQueryData({ audioId: audioId });
                this.deleteAudio.invoke();
            }
        },

        // 删除成功回调
        _delete_success: function(caller, data) {
            if (data.errcode) {
                alert("删除失败: " + data.errdes);
            } else {
                alert("删除成功");
                this._refresh();
            }
        },

        // 格式化文件大小
        _formatFileSize: function(value) {
            if (!value) return '';
            var size = parseInt(value);
            if (size < 1024) return size + ' B';
            if (size < 1024 * 1024) return (size / 1024).toFixed(1) + ' KB';
            return (size / (1024 * 1024)).toFixed(1) + ' MB';
        },

        // 格式化状态
        _formatStatus: function(value) {
            var statusMap = {
                'PENDING': '待处理',
                'PROCESSING': '处理中',
                'COMPLETED': '已完成',
                'FAILED': '失败'
            };
            return statusMap[value] || value;
        },

        // 格式化时间
        _formatTime: function(value) {
            if (!value) return '';
            return new Date(parseInt(value)).toLocaleString();
        }
    }
});