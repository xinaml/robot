var Order = (function () {

    function Order() {

    }

    Order.prototype.initTable = function (path) {
        $('#order_table').bootstrapTable({
            url: "/order/maps",
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
                field: 'status',
                title: '订单状态', formatter: function (value, row, index) {
                    return "待卖出";
                },
                align: 'center'

            }, {
                field: 'type',
                title: '订单类型', formatter: function (value, row, index) {
                    if (value == "1") {
                        return "买入"
                    }
                    if (value == "2") {
                        return "卖出"
                    }
                    return "未知";
                },
                align: 'center'

            }, {
                field: 'price',
                title: '买入价格',
                align: 'center'

            }, {
                field: 'price',
                title: '卖出价格',
                align: 'center'

            }, {
                field: 'profit',
                title: '盈利',
                align: 'center'

            }, {
                field: 'sellId',
                title: '卖出id',
                align: 'center'

            }, {
                type: 'text',
                field: 'orderId',
                title: '订单id',
                align: 'center'
            }, {
                field: 'createDate',
                title: '买入时间',
                align: 'center'

            }, {
                field: 'sellDate',
                title: '卖出时间',
                align: 'center'

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
    Order.prototype.cancel = function (arg) {
        var rows = $('#order_table').bootstrapTable("getSelections");
        if (arg) {
            if (rows.length > 0) {
                $('#delModal').modal('show');
            }
        } else {
            var obj = rows[0];
            var id = obj.orderId;
            var type = obj.type;
            if (rows.length == 1) {
                $.ajax({
                    type: 'POST',
                    url: '/order/del',
                    dataType: 'json',
                    traditional: true,
                    data: {id: id, type: type},
                    success: function (rs) {
                        if (rs.code == 0) {
                            $(".success.selected").remove();
                            $(".selected").remove()
                            $('#delModal').modal('hide');
                            toastr.success(rs.msg);
                        } else {
                            toastr.error(rs.msg);
                        }
                    },
                    error: function () {
                        //请求失败时
                    }
                })
            }else {
                toastr.error("只能选择一行进行撤销！");
            }
        }


    };

    return Order;
})();