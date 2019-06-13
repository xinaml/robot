var Storage = (function () {
    var $currentPath;// 当前路径
    var $baseUrl = "/storage";
    var $fileNums;// 文件数
    var $button = $('.btn-group button');
    var $disabled = $('.isdisabled');
    var $danxuan = $('.danxuan input');
    var listArr = [];// 存储数据数组
    function Storage() {

    }

    $('#right').css({
        height: $(window).height() - 100
    });
    $('.prompt_box').css({
        height: $(window).height() - 100
    });
    $('#searchText').css({
        width: $('#right').width() / 7.5
    });
    // 缩略图后缀
    var exts = [['txt', 'txt'], ['zip', 'zip'], ['doc', 'word'],
        ['docx', 'word'], ['xls', 'excel'], ['xlsx', 'excel'],
        ['html', 'html'], ['rar', 'zip'],
        ['pdf', 'pdf'], ['mp3', 'mp3'], ['avi', 'video'],
        ['rmvb', 'video'], ['mp4', 'video'], ['swf', 'video'],
        ['ppt', 'ppt'], ['tar', 'zip'], ['tgz', 'zip']];

    // 文件列表
    Storage.prototype.list = function (path) {
        $currentPath = path;
        setCookie("currentPath", path, 1);
        $.ajax({
            url: $baseUrl + "/list",
            type: 'GET', //GET
            async: true,    //或false,是否异步
            dataType: "json",
            data: {
                'path': $storage.encrypt(path)
            },
            beforeSend: function (xmlHttp) {
                xmlHttp.setRequestHeader("If-Modified-Since", "0");
                xmlHttp.setRequestHeader("Cache-Control", "no-cache");
            },
            success: function (rs) {
                if (rs.code == 0) {
                    if (null != rs.data && rs.data.length > 0) {
                        var gridHtml = ""; // 网格内容
                        $.each(rs.data, function (index, obj) { // 网格数据
                            var gridImg = "";
                            var size = "-";
                            var fileName = getByNameLen(obj.name);
                            if (!obj.dir) {
                                var fileType = obj.fileType;
                                size = obj.size;
                                $.each(exts, function (index, ext) {
                                    if (ext[0] == fileType) {
                                        gridImg = "s_" + ext[1] + ".png";
                                    }
                                });
                                if (gridImg == "") { // 未知文件类型
                                    gridImg = "s_unknow.png";
                                }
                            } else {
                                gridImg = "s_floder.png";
                            }
                            var gridImgPath = "<img src='" + '../images/storage/' + gridImg + "'>";
                            if (fileType == "jpg" || fileType == "png" || fileType == "jpeg" || fileType == "bmp") {
                                gridImgPath = "<img src='" + $baseUrl + "/thumbnails?path=" + $storage.encrypt(obj.path) + "&width=55" + "&height=44" + "'>";
                            }
                            if (fileType == "gif") {
                                gridImgPath = "<img height='44' width='55' src='" + $baseUrl + "/gif?path=" + encodeURI(obj.path) + "'>";
                            }

                            gridHtml += '<li   ondblclick="$storage.dblclick(this)">' + '<div onclick="$storage.click(this)" class="important">' + '<input  path="' + obj.path + '" fileType="' + obj.fileType + '" class="ischcked" type="checkbox" index="' + index + '">' +
                                gridImgPath + '<p class="space" style="cursor: pointer;" title="文&nbsp;件&nbsp;名&nbsp;&nbsp;：' + obj.name + '\n文件大小：' + size + '\n修改日期：' + obj.modifyTime + '">' +
                                fileName + '</p>' + '<p>' + size + '</p>' + ' <p>' + obj.modifyTime + '</p>' + '</div>' + ' </li>';
                        });
                        $("#countCol-content").html('').html(gridHtml);
                        $("#fileNums").html("共" + rs.data.length + "个文件");
                        $fileNums = rs.data.length;
                        ishover();
                    } else {
                        var notHasFile = '<div class="undefinedCount"><img style="padding-top:30px" src="' +
                            '../images/storage/wnr.png" alt=""><p>该文件夹是空的~</p> </div>';
                        $("#fileNums").html("共" + 0 + "个文件");
                        $("#countCol-content").html(notHasFile);
                        $fileNums = rs.data.length;
                    }
                    initcurrentPath();
                } else if (rs.code == 403) {
                    window.location.href = "/login"
                }

            },
            error: function (result) {
                var data = JSON.parse(result);
                alert(data.msg);
            },
        })
    };


    // 初始化当前路径
    function initcurrentPath() {
        var arrays = $currentPath.split("\/");
        if (arrays[arrays.length - 1] == "") {
            arrays.pop();
        }
        var html = "";
        $("#back_level").show();
        $("#back_level").next().show();
        var path = "/";
        $.each(arrays, function (index, obj) { // 网格数据
            if (index != 0) {
                if (index == arrays.length - 1) {
                    html += "<span>" + arrays[index] + "</span>";
                } else {
                    path += (arrays[index] + "/");
                    html += "<a href='#' onclick=$storage.list('" + path + "')>" + arrays[index] + "</a>&nbsp;<font color='#969696'>></font> ";
                }
            }
        });
        $("#currentPath").html(html);
    }

    Storage.prototype.goback = function () {
        var arrays = $currentPath.split("\/");
        if (arrays.length >= 1) {
            arrays.pop();
            var path = arrays.join("/");
            if (path == "") {
                path="/";
            }
            $storage.list(path);
        }
    };

    /**
     * 创建文件夹
     */
    function mkdir(dir) {
        promptBox("正在创建文件夹...");
        $.post($baseUrl + "/mkdir", {
            'path': $storage.encrypt($currentPath),
            'dir': $storage.encrypt(dir)
        }, function (rs) {
            if (rs.code == 0) {
                promptBox("创建文件夹成功!", 1000);
            } else {
                promptBox(rs.msg);
            }
            $fileNums = $fileNums + 1;
            $(".undefinedCount").hide();
            $("#fileNums").html("共" + $fileNums + "个文件");
        });
    }

    /**
     * 删除文件或文件夹
     */
    function del(paths) {
        $('#myModal').modal('hide');
        promptBox("正在删除文件...");
        $.post($baseUrl + "/delfile", {
            'paths': $storage.encrypt(paths)
        }, function (rs) {
            if (rs.code == 0) {
                $storage.list($currentPath);
                $("#myModal").hide();
                $(".modal-backdrop").hide();
                promptBox("删除文件成功!", 1000);
            } else {
                promptBox(rs.msg);
            }
        });
    }

    /**
     * 重命名
     */

    Storage.prototype.renameFile = function () {
        var rename = '<input class="rename" type="text">&nbsp;&nbsp;<button class="istrue"></button>&nbsp;&nbsp;<button class="isfalse"></button>';
        var textInput = $("input[index=" + listArr[0] + "]").siblings('.space').text();
        $("input[index=" + listArr[0] + "]").siblings('.space').text('').append(rename);
        $('.rename').val(textInput).focus();
        $("input[type='checkbox']").attr('disabled', "false");
        console.info($danxuan)
        $danxuan.attr('disabled', "false");
        // 取消
        $('.isfalse').click(function () {
            $danxuan.removeAttr("disabled");
            $button.eq(4).removeAttr("disabled");
            $("input[type='checkbox']").removeAttr("disabled");
            $("input[index=" + listArr[0] + "]").siblings('.space').text(textInput);
        });
        // 确定
        $('.istrue').click(function () {
            $danxuan.removeAttr("disabled");
            $button.eq(4).removeAttr("disabled");
            $("input[type='checkbox']").removeAttr("disabled");
            var newValue = $('.rename').val();
            $("input[index=" + listArr[0] + "]").siblings('.space').text(newValue);
            promptBox("正在重命名文件...");
            $.post($baseUrl + "/rename", {
                'path': $storage.encrypt(path),
                "oldName": $storage.encrypt(textInput),
                'newName': $storage.encrypt(newValue)
            }, function (rs) {
                if (rs.code==0) {
                    promptBox("重命名文件成功!", 1000);
                } else {
                    promptBox(rs.msg);
                }
            });
        });

    }

    /**
     * 移动文件
     */
    Storage.prototype.moveFile = function () {
        modal();
        loadTree();
        var myModalLabelText = $(this).text();
        $('#myModalLabel').text(myModalLabelText);
        $('.btn-primary').click(function () {
            promptBox("正在移动文件...");
            $('#myModal').modal('hide');
            var str = $(".colstage");
            var arrays = [];
            $.each(str, function (index, dom) {
                arrays.push($(dom).find('input').attr('path'));
            });
            $.unique(arrays);
            var zTreeOjb = $.fn.zTree.getZTreeObj("tree"); // ztree的Id zTreeId
            var selectedNodes = zTreeOjb.getSelectedNodes();
            if (selectedNodes.length > 0) {
                $.post($baseUrl + "/move", {
                    'fromPath': $storage.encrypt(arrays.join("##")),
                    'toPath': $storage.encrypt(selectedNodes[0].id)
                }, function (rs) {
                    if (rs.code == 0) {
                        promptBox("移动文件成功!", 1000);
                        $storage.list($currentPath);
                    } else {
                        promptBox(rs.msg);
                    }
                });
            }
        });

    }

    /**
     * 复制文件
     */
    Storage.prototype.copyFile = function () {
        modal();
        loadTree();
        var myModalLabelText = $(this).text();
        $('#myModalLabel').text(myModalLabelText);
        $('.btn-primary').click(function () {
            $("#myModal").on("hiden.bs.modal", function () {
                $(this).removeData("bs.modal");
            });
            promptBox("正在复制文件...");
            $('#myModal').modal('hide');
            var str = $(".colstage");
            var arrays = [];
            $.each(str, function (index, dom) {
                arrays.push($(dom).find('input').attr('path'));
            });
            $.unique(arrays);
            var zTreeOjb = $.fn.zTree.getZTreeObj("tree"); // ztree的Id zTreeId
            var selectedNodes = zTreeOjb.getSelectedNodes();
            if (selectedNodes.length > 0) {
                $.post($baseUrl + "/copy", {
                    'fromPath': $storage.encrypt(arrays.join("##")),
                    'toPath': $storage.encrypt(selectedNodes[0].id)
                }, function (rs) {
                    if (rs.code ==0) {
                        $storage.list($currentPath);
                        promptBox("复制文件成功!", 1000);
                    } else {
                        promptBox(rs.msg);
                    }
                });
            }
        });

    }

    // 上传
    Storage.prototype.upload = function (uploader, relevanceId) {
        uploader.options.formData.path = $storage.encrypt($currentPath);// 参数添加
        uploader.options.formData.relevanceId = relevanceId; // 关联id
        uploader.upload();// 上传
    };

    Storage.prototype.reLoad = function () {
        $storage.list($currentPath);
    };

    // md5验证
    Storage.prototype.validateMd5 = function (uploader, file) {
        $.ajax({// 向服务端发送请求
            cache: false,
            type: "get",
            dataType: "json",
            url: $baseUrl + "/md5/exist",
            data: {
                fileMd5: $storage.encrypt(file.wholeMd5),
                fileName: $storage.encrypt(file.name),
                toPath: $storage.encrypt($currentPath),
            },
            success: function (rs) {
                $('#upload_msg').html("");
                $('#upload_msg').fadeOut();
                if (rs.code==0 ) {
                    if(rs.data==false){
                        uploader.options.formData.md5value = file.wholeMd5;// 每个文件都附带一个md5，便于实现秒传
                        $storage.upload(uploader);
                        console.log("秒传失败！");
                    }else {
                        console.log("开始秒传！");
                        uploader.removeFile(file, true);
                        var html = ' <div style="width:220px;display:inline-block;height:30px; padding-top:10px;" class="progress progress-striped active"><div class="progress-bar progress-bar-success" role="progressbar" style="width: 100%;">100%</div></div>';
                        $('#' + file.id).append(html);
                        $("#status").html("上传完成");
                        $storage.list($currentPath);
                        $('#upload_msg').html(
                            " <div class='title-left' style='text-align:center'><span >"
                            + $('.item').length + "个文件上传成功</span>").fadeIn('slow');
                    }
                } else {
					console.log(rs.data);
                }
            }
        });

    };


    Storage.prototype.closeImgPreview = function () {
        $("#imgPreview").fadeOut();
    };


    Storage.prototype.downFile = function () {
        var path = $("input[index=" + listArr[0] + "]").attr("path");
        var filetype = $("input[index=" + listArr[0] + "]").attr("filetype");
        var isFolder = (filetype == "FOLDER");
        var a = document.createElement('a')
        a.href = $baseUrl + "/download?path=" + $storage.encrypt(path) + "&isFolder=" + isFolder;
        a.target = '_blank'
        document.body.appendChild(a)
        a.click()
        buttonShowandHide();
    }
    Storage.prototype.delFile = function () {
        modal();
        $('#myModalLabel').text('确认删除');
        $('.modal-body').text('确认要把所选文件删除吗？').css({
            height: '90px'
        });
        $('.btn-primary').click(function () {
            var str = $(".colstage");
            var arrays = [];
            var arrindex = [];
            $.each(str, function (index, dom) {
                arrays.push($(dom).find('input').attr('path'));
                arrindex.push($(dom).find('input').attr('index'));
            });
            $.unique(arrays);
            del(arrays.join("##"));
            listArr = [];
            buttonShowandHide();
            $('.countTitle').find('input').prop("checked", false);
        });
    }

    ishover();

    function initmodal() {
        // 初始化模态框
        function modal() {
            $("#myModal").modal({
                backdrop: false, // 相当于data-backdrop
                keyboard: false, // 相当于data-keyboard
                show: true, // 相当于data-show
                remote: "" // 相当于a标签作为触发器的href
            });
        }

        $("#myModal").on("hidden.bs.modal", function () {
            var mymodal = '<div class="modal fade" id="myModal" tabindex="-1" role="dialog"><div class="modal-dialog" role="document"><div class="modal-content"><div class="modal-header"><button type="button" class="close" data-dismiss="modal"aria-label="Close"><span aria-hidden="true">&times;</span></button><h4 class="modal-title" id="myModalLabel">复制到</h4></div><div class="modal-body"><!--ztree插件--><ul id="tree" class="ztree"></ul></div><div class="modal-footer"><button type="button" class="btn btn-primary">确定</button><button type="button" class="btn btn-default" data-dismiss="modal">取消</button></div></div></div></div>';
            $("#myModal").remove();
            $('body').prepend(mymodal);
        });
    }

    // 初始化模态框
    function modal() {
        $("#myModal").modal({
            backdrop: false, // 相当于data-backdrop
            keyboard: false, // 相当于data-keyboard
            show: true, // 相当于data-show
            remote: "" // 相当于a标签作为触发器的href
        });
    }

    $("#myModal").on("hidden.bs.modal", function () {
        var mymodal = '<div class="modal fade" id="myModal" tabindex="-1" role="dialog"><div class="modal-dialog" role="document"><div class="modal-content"><div class="modal-header"><button type="button" class="close" data-dismiss="modal"aria-label="Close"><span aria-hidden="true">&times;</span></button><h4 class="modal-title" id="myModalLabel">复制到</h4></div><div class="modal-body"><!--ztree插件--><ul id="tree" class="ztree"></ul></div><div class="modal-footer"><button type="button" class="btn btn-default" data-dismiss="modal">取消</button><button type="button" class="btn btn-primary">确定</button></div></div></div></div>';
        $("#myModal").remove();
        $('body').prepend(mymodal);
    });


    // 新建文件夹
    Storage.prototype.mkdir = function () {
        // 网格
        var rowHtml = '<li><div class="important"><img src="'
            + '../images/storage/wjj_d.png" alt=""><p class="space"><input class="spaceInput" type="text" placeholder="新建文件夹">&nbsp;&nbsp;<button class="istrue"></button>&nbsp;&nbsp;<button class="isfalse"></button></p></div></li>';
        // 网格
        $('.colInfo ul').prepend(rowHtml);
        var $spaceInout = $('.spaceInput');
        $spaceInout.val('新建文件夹');// 默认创建文件夹名称
        $spaceInout.focus();// 默认创建被选中输入
        // 取消
        $('.isfalse').click(isfalse);
        // 确定
        $('.istrue').click(istrue);
        ishover();
    }

    // 新建文件确定
    function istrue() {
        var $spaceInout = $('.spaceInput');
        $('.titleTwo').removeAttr("disabled");
        newValue = $spaceInout.val();
        var rowText = '<li  ondblclick="$storage.dblclick(this)"><div onclick="$storage.click()" class="important"><input path="' + $currentPath + '/' + newValue + '" filetype="FOLDER" class="ischcked" type="checkbox" index="'
            + ($fileNums + 1) + '"><img src="' + '../images/storage/wjj_d.png" alt=""><p class="space">' + newValue + '</p><p>-</p><p>' + getDate() + '</p></div></li>';

        $.ajax({
            cache: true,
            type: "get",
            dataType:'json',
            url: $baseUrl + "/exists",
            data:{'path': $currentPath + "/" + newValue},
            async: false,
            success: function (rs) {
                if(rs.data==true){
                    promptBox('该目录已存在！');
                }else {
                    $('.istrue').parents('li').remove();
                    $('.colInfo ul').prepend(rowText);
                    ishover();
                    mkdir(newValue);
                    $button.hide();
                }
            },
            error: function (data) {
                alert("error");
            }
        });
    }

    // 下载,复制,移动按钮显示和隐藏
    function buttonShowandHide() {
        // 重命名和查看禁止点击判断
        if (listArr.length == 1 && listArr[0] != 'undefined') {
            $disabled.removeAttr("disabled");
            $button.show();
        } else if (listArr.length > 1) {
            $disabled.attr('disabled', "true");
        } else {
            $button.hide();
        }
    }

    //单击选中
    Storage.prototype.click = function (dom) {
        if($(dom).parent().hasClass('colstage')){
            $(dom).parent().attr('class', '');
            $(dom).find('input').hide();
            $(dom).find('input').prop("checked", false);
        }else {
            $(dom).parent().attr('class', 'colstage');
            $(dom).find('input').show();
            $(dom).find('input').prop("checked", true);
        }
        var list =$("#countCol-content").find('li');
        var exists = false;
        $.each(list,function (index ,li) {
            if($(li).hasClass('colstage')){
                $('#btn-group').show();
                exists =true;
            }
        })
        if(!exists){
            $('#btn-group').hide();
        }

    }


    Storage.prototype.dblclick = function (dom) {
        var fileType = $(dom).find('input').attr('fileType');
        var path = $(dom).find('input').attr('path');
        if ("FOLDER" == fileType) { // 进入文件夹
            $button.hide();
            $storage.list(path);
        } else if (fileType == "jpg" || fileType == "png" || fileType == "jpeg" || fileType == "bmp" || fileType == "gif") {
            $button.show();
            $("#imgPreview").fadeIn();
            $("#imgPreview").find('img').attr('src', $baseUrl + "/download?path=" + $storage.encrypt(path));
            $("#imgDown").html("<a href='" + $baseUrl + "/download?path=" + $storage.encrypt(path) + "'><font color='white' style='font-weight:bold;cursor:pointer;'>下载图片</font></a>");
        } else {
            var a = document.createElement('a')
            a.href = $baseUrl + "/download?path=" + $storage.encrypt(path)
            a.target = '_blank'
            document.body.appendChild(a)
            a.click()
        }
    }


    Storage.prototype.initUpload = function (uploader) {

        // 当有文件被添加进队列的时候
        uploader.on('fileQueued', function (file) {
            var ext = file.ext;
            var thumb = "";
            $.each(exts, function (index, ext) {
                if (ext[0] == ext) {
                    thumb = "s_" + ext[1] + ".png";
                }
            });
            if (ext == "jpg" || ext == "png" || ext == "jpeg"
                || ext == "bmp") {
                thumb = "s_png.png";
            }
            if (thumb == "") {
                thumb = "s_unknow.png";
            }
            var src = '../images/storage/' + thumb;
            var fileName = getByNameLen(file.name);
            $('#fileList').append('<div id="' + file.id + '" class="item">'
                + '<img style="margin-left: 20px;margin-right: 20px;vertical-align: top;" src="' + src + '" alt=""><h4 class="info" style="vertical-align: top;width: 230px;display:inline-block">'
                + '<span style="cursor: pointer;width:80px;" title=' + file.name + '>'
                + fileName + '</span><b>文件大小:&nbsp;</b>' + WebUploader.formatSize(file.size, 0, ['B', 'KB', 'MB', 'GB'])
                + '<input type="hidden" fileId="' + file.id + '" ></input></h4></div>');
            $('.upload').show('slow');
            $('#upload_msg').fadeIn('slow');
            $("#status").html("加载中");
            $('#upload_msg').html(" <div class='titleleft'><span>正在加载验证文件，请稍等...</span></div>");
            uploader.options.formData.guid = WebUploader.guid();// 每个文件都附带一个guid，以在服务端确定哪些文件块本来是一个
            uploader.md5File(file, 0, 50 * 1024 * 1024).then(function (fileMd5) { // 完成
                file.wholeMd5 = fileMd5;// 获取到了md5
                uploader.options.formData.md5value = file.wholeMd5;// 每个文件都附带一个md5，便于实现秒传
                $storage.validateMd5(uploader, file);
            });
        });

        // 文件上传过程中创建进度条实时显示。
        uploader.on('uploadProgress', function (file, percentage) {
            var $li = $('#' + file.id), $percent = $li.find('.progress .progress-bar');
            // 避免重复创建
            if (!$percent.length) {
                $percent = $('<div style="width:220px;display:inline-block;height:20px;margin-top:7px; " class="progress progress-striped active">'
                    + '<div class="progress-bar" role="progressbar" style="width: 0%">0%</div></div>').appendTo($li).find('.progress-bar');
            }
            $("#status").html("上传中");
            var val = parseInt(percentage * 100);
            $percent.css('width', val + '%');
            $percent.text(val + '%');
        });

        uploader.on('uploadSuccess', function (file) {
            $('#' + file.id).find(".progress").find(".progress-bar").attr("class", "progress-bar progress-bar-success");

        });

        uploader.on('uploadError', function (file, reason) {
            $('#status').text('上传出错');
            $('#upload_msg').html(" <div class='title-left' style='text-align:center'><span >" + fileNums + "个文件上传失败</span>");

            // 上传出错后进度条爆红
            $('#' + file.id).find(".progress").find(".progress-bar").attr("class", "progress-bar progress-bar-danger");
            // 添加重试按钮
            if ($('#' + file.id).find(".btn-retry").length < 1) {
                var btn = $('<button type="button" fileid="' + file.id + '" class="btn btn-success btn-retry" style="padding=0px 5px!important;float:rihgt;margin:0px 0px 10px 30px;"><span class="glyphicon glyphicon-refresh"></span></button>');
                $('#' + file.id).append(btn);// .find(".btn-danger")
            }

            $(".btn-retry").click(function () {
                uploader.retry(uploader.getFile($(this).attr("fileId")));
            });
        });

        uploader.on('uploadComplete', function (file) {// 上传完成后回调
            // $('#' + file.id).find('.progress').fadeOut();//上传完删除进度条
            // $('#' + file.id + 'btn').fadeOut('slow')//上传完后删除"删除"按钮
        });
        uploader.on('filesQueued', function (files) {// 上传前条用
            if (files.length > 10) {
                promptBox("单次上传文件不能超过10个!");
                return;
            }
        });

        uploader.on('uploadFinished', function () {
//			 alert("所有文件上传完毕");
            initmodal();
            $storage.reLoad();
            uploader.reset();
            $("#status").html("上传完成");
            var fileNums = $('.item').length;
            $('#upload_msg').html(" <div class='title-left' style='text-align:center'><span >" + fileNums + "个文件上传成功</span>");
            $('#upload_msg').fadeIn('slow');

        });

        $("#UploadBtn").click(function () {
            uploader.upload();// 上传
            $("#StopBtn").fadeIn('slow');
        });

        $("#StopBtn").click(function () {
            var status = $('#StopBtn').attr("status");
            if (status == "suspend") {
                $("#StopBtn").html("继续上传");
                $("#StopBtn").attr("status", "continuous");
                uploader.stop(true);

            } else {
                $("#StopBtn").html("暂停上传");
                $("#StopBtn").attr("status", "suspend");
                uploader.upload(uploader.getFiles("interrupt"));
            }
            ;
        });

        uploader.on('uploadAccept', function (file, response) {
            if (response._raw === '{"error":true}') {
                return false;
            }
        });
    };

    var zNodes;
    var zTree;

    function loadTree() {
        $.ajax({
            cache: true,
            type: "get",
            dataType:'json',
            url: $baseUrl + "/tree?path=" + $storage.encrypt("/"),
            async: false,
            success: function (rs) {
                zNodes = rs.data;
            },
            error: function (data) {
                alert("error");
            }
        });
        zTree = $.fn.zTree.init($("#tree"), setting, zNodes);
        zTree.expandAll(false);
    }

    var setting = {
        view: {
            nameIsHTML: true
        },
        data: {
            simpleData: {
                enable: true
            },
            keep: {
                parent: true
            },
            key: {
                isParent: "parent"
            }
        },
        open: false,
        callback: {
            onClick: nodeClick,
            onExpand: function (event, treeId, treeNode) {
                addSubNode(treeNode);
            }
        }
    };

    function nodeClick(event, treeId, treeNode, clickFlag) {
        addSubNode(treeNode);
    }

    function addSubNode(treeNode) {
        if (!treeNode.isParent)
            return;
        var s = treeNode.children;
        if (s != undefined)
            return;
        $.ajax({
            cache: true,
            type: "get",
            dataType:'json',
            url: $baseUrl + "/tree",
            data: {
                'path': $storage.encrypt(treeNode.id)
            },
            async: true,
            success: function (rs) {
                zTree.addNodes(treeNode, rs.data);
            },
            error: function (data) {
                alert("error");
            }
        });

    }

    Storage.prototype.encrypt = function (word) {
        var key = CryptoJS.enc.Utf8.parse("abcdefgabcdefg12");
        var srcs = CryptoJS.enc.Utf8.parse(word);
        var encrypted = CryptoJS.AES.encrypt(srcs, key, {mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Pkcs7});
        return encrypted.toString();
    }

    Storage.prototype.search = function () {
        var text = $("#searchText").val();
        text = text.trim();
        if (text == "") {
            $(".space").each(function (index, dom) {
                $(dom).parent().parent().css("display", "block");
            });
            $(".countRow .one").each(function (index, dom) {
                $(dom).parent().parent().css("display", "block");
            });
            $("#fileNums").html("共" + ($fileNums) + "个文件");
        } else {
            var removeCount = 0;
            $(".space").each(function (index, dom) {
                if ($(dom).text().indexOf(text) < 0) {
                    $(dom).parent().parent().css("display", "none");
                    removeCount++;
                } else {
                    $(dom).parent().parent().css("display", "block");
                }
            });

            $(".countRow .one").each(function (index, dom) {
                if ($(dom).find('span').text().indexOf(text) < 0) {
                    $(dom).parent().parent().css("display", "none");
                } else {
                    $(dom).parent().parent().css("display", "block");
                }
            });
            $("#fileNums").html("共" + ($fileNums - removeCount) + "个文件");
        }

    };

    Storage.prototype.getCookie = function (objName) {// 获取指定名称的cookie的值
        var arrStr = document.cookie.split("; ");
        for (var i = 0; i < arrStr.length; i++) {
            var temp = arrStr[i].split("=");
            if (temp[0] == objName)
                return unescape(temp[1]);
        }
        return "";
    };

    // 设置cookie的值
    function setCookie(cname, cvalue, exdays) {
        var d = new Date();
        d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
        var expires = "expires=" + d.toGMTString();
        document.cookie = cname + "=" + escape(cvalue) + "; " + expires;
    }

    // 新建文件取消
    function isfalse() {
        var $change = $('.change');
        $change.removeAttr("disabled");
        $(this).parents('li').remove();
        $('.titleTwo').removeAttr("disabled");
    }

    // 鼠标hover效果
    function ishover() {
        $('.danxuan li').on({
            mouseenter: function () {
                $(this).addClass('rowstage1').find('input').show();
            }, mouseleave: function () {
                $(this).removeClass('rowstage1');
                if (!$(this).find('input').prop("checked") && $(this).parents('div').hasClass('countCol')) {
                    $(this).find('input').hide();
                }
            }
        });
    }

    function promptBox(strName, speed) {
        if (speed == null) {
            $('.prompt_box').fadeIn();
            $('.prompt_boxbody span').text('');
            $('.prompt_boxbody span').text(strName);
            setTimeout(function () {
                $('.prompt_box').fadeOut();
            }, 2000);
        } else {
            $('.prompt_box').fadeIn();
            $('.prompt_boxbody span').text('');
            $('.prompt_boxbody span').text(strName);
            var timer = setTimeout(function () {
                $('.prompt_boxbody span').text(strName);
                setTimeout(function () {
                    $('.prompt_box').fadeOut();
                }, speed);

            }, speed);
        }
    }

    // 获取时间
    function getDate() {
        var date = new Date();// 当前时间
        var month = zeroFill(date.getMonth() + 1);// 月
        var day = zeroFill(date.getDate());// 日
        var hour = zeroFill(date.getHours());// 时
        var minute = zeroFill(date.getMinutes());// 分
        var second = zeroFill(date.getSeconds());// 秒
        // 当前时间
        var curTime = date.getFullYear() + "-" + month + "-" + day + " " + hour
            + ":" + minute + ":" + second;
        return curTime;
    }

    // 补零
    function zeroFill(i) {
        if (i >= 0 && i <= 9) {
            return "0" + i;
        } else {
            return i;
        }
    }

    function getByNameLen(val) {
        var len = val.length;
        var existsChinese = false;
        for (var i = 0; i < val.length; i++) {
            var length = val.charCodeAt(i);
            if (length > 128) {
                len += 1;
                existsChinese = true;
            }
            ;
        }
        if (len >= 10) {
            var last = 5;
            if (!existsChinese) {
                last = 9;
            }
            return val.substring(0, last) + "...";
        } else {
            return val;
        }
    }

    return Storage;
})();