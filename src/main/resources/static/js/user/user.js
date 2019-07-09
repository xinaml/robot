var User = (function () {

    function User() {

    }

    User.prototype.initTable = function (path) {
        $('#user_table').bootstrapTable({
            url: "/user/maps",
            method: 'get',
            toolbar: '#toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: false,                     //是否启用排序
            sortOrder: "asc",                   //排序方式
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber: 1,                       //初始化加载第一页，默认第一页
            pageSize: 10,                       //每页的记录行数（*）
            pageList: [10, 20, 50],        //可供选择的每页的行数（*）
            clickToSelect: true,
            singleSelect: false,
            selectItemName: "custonSelectItem",
            striped: true,
            maintainSelected: false,
            height: 530,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            cardView: false,                    //是否显示详细视图
            detailView: false,
            queryParamsType: "undefined",
            //设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder
            //设置为limit可以获取limit, offset, search, sort, order

            //是否显示父子表
            onClickRow: function (row, dom) {
                var va = $(dom).hasClass('class');
                va == true ? $(dom).addClass("success") : $(dom).removeClass("success");


            }, onCheck: function (row, dom) {
                var va = dom.parent().parent()
                va.addClass("success")
            }, onUncheck: function (row, dom) {
                var va = dom.parent().parent()
                va.removeClass("success")
            },
            columns: [{
                checkbox: true,
            }, {
                field: 'id',
                title: 'id',
                visible: false
            }, {
                type: 'text',
                field: 'username',
                title: '用户名',
                sortable: true,
                align: 'center'
            }, {
                field: 'email',
                title: '邮箱'
            }, {
                field: 'phone',
                title: '手机'
            }, {
                field: 'birthday',
                title: '生日'
            }]
            ,
            onLoadSuccess: function (rs) {  //加载成功时执行
            },
            onLoadError: function (data) {  //加载失败时执行
                alert("服务器错误");
            },
            ajaxOptions: {
                // headers: {"token":localStorage.token}
            },
            queryParams: function queryParams(params) {   //设置查询参数
                var param = {
                    page: params.pageNumber,
                    limit: params.pageSize,
                    sorts: "createDate",
                };
                return param;
            }
        });

        $(".form_datetime").datetimepicker({
            format: "yyyy-mm-dd",
            autoclose: true,
            todayBtn: true,
            todayHighlight: true,
            showMeridian: true,
            pickerPosition: "bottom-left",
            language: 'zh-CN',//中文，需要引用zh-CN.js包
            startView: 2,//月视图
            minView: 2//日期时间选择器所能够提供的最精确的时间选择视图
        });
    };

    User.prototype.delUser = function (arg) {
        var rows = $('#user_table').bootstrapTable("getSelections");
        if (arg) {
            if (rows.length > 0) {
                $('#delModal').modal('show');
            }
        } else {
            var ids = new Array();
            $.each(rows, function (index, obj) {
                ids.push(obj.id);
            })
            if (rows.length > 0) {
                $.ajax({
                    type: 'POST',
                    url: '/user/del',
                    dataType: 'json',
                    traditional: true,
                    data: {ids: ids},
                    success: function (rs) {
                        if (rs.code == 0) {
                            $(".success.selected").remove();
                            $(".selected").remove()
                            $('#delModal').modal('hide');
                            $('#user_table').bootstrapTable('refresh')
                            toastr.success(rs.msg);
                        } else {
                            toastr.error(rs.msg);
                        }
                    },
                    error: function () {
                        //请求失败时
                    }
                })
            }
        }


    };


    User.prototype.addUser = function (arg) {
        if (arg) { //添加按钮跟提交按钮均进入此方法，提交按钮不传递参数
            $("#modalUserLabel").html("添加");
            $('#modalUser').modal('show');
            $("#opt").attr("onclick", "$user.addUser()")

        } else {
            var data = $('#userFrom').serialize();
            var submitData = decodeURIComponent(data, true);
            $.ajax({
                type: 'POST',
                url: '/user/add',
                data: submitData,
                dataType: 'json',
                cache: false,//false是不缓存，true为缓存
                async: true,//true为异步，false为同步
                beforeSend: function () {
                    //请求前
                },
                success: function (rs) {
                    if (rs.code == 0) {
                        $('#modalUser').modal('hide');
                        $('#user_table').bootstrapTable('refresh')
                        toastr.success(rs.msg);
                    } else {
                        toastr.error(rs.msg);
                    }
                },
                error: function () {
                    //请求失败时
                }
            })
        }

    };
    User.prototype.editUser = function (arg) {
        var rows = $('#user_table').bootstrapTable("getSelections");
        if (rows.length == 1) {
            var obj = rows[0];
            if (arg) { //添加按钮跟提交按钮均进入此方法，提交按钮不传递参数
                $("#modalUserLabel").html("编辑");
                $('#modalUser').modal('show');
                $("#username").val(obj.username);
                $("#email").val(obj.email);
                $("#phone").val(obj.phone);
                $("#id").val(obj.id);
                $('#birthday').val(obj.birthday)
                $("#opt").attr("onclick", "$user.editUser()")

            } else {
                var data = $('#userFrom').serialize();
                var submitData = decodeURIComponent(data, true);
                $.ajax({
                    type: 'PUT',
                    url: '/user/edit',
                    data: submitData,
                    dataType: 'json',
                    success: function (rs) {
                        if (rs.code == 0) {
                            $('#modalUser').modal('hide');
                            $('#user_table').bootstrapTable('refresh')
                            toastr.success(rs.msg);
                        } else {
                            toastr.error(rs.msg);
                        }
                    },
                    error: function () {
                        //请求失败时
                    }
                })
            }

        } else {
            toastr.warning('只能选择一行进行编辑');
            return;
        }

    };
    return User;
})();