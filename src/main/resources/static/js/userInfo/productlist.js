layui.use(['form', 'element', 'util', 'carousel', 'laypage', 'layer', 'table'], function () {
    var element = layui.element;
    var util = layui.util;
    var form = layui.form;
    var laypage = layui.laypage
        , layer = layui.layer;
    form.on('select(types)', function (data) {
        var indexGID = data.elem.selectedIndex;
        lookallproduct(data.elem[indexGID].title);
    });
});
function lookallproduct(stuatus) {
    layui.use(['form', 'element', 'util', 'carousel', 'laypage', 'layer','table'], function () {
        var form = layui.form,
        table = layui.table;
        table.render({
            elem: '#product'
            , url: basePath+'/user/commodity/'+stuatus
            , page: {
                layout: ['limit', 'count', 'prev', 'page', 'next', 'skip']
                , groups: 3
                , limits: [10,20, 50, 100]
                , limit: 10
            }, cols: [[
                {field: 'qid', title: 'ID',width:80, align:'center'}
                , {field: 'commname', title: '名称', width: 300, align:'center'}
                , {field: 'categoryName', title: '类别', width: 100, align:'center'}
                , {field: 'commdesc', title: '描述', width: 700, align:'center'}
                , {field: 'commstatus', title: '状态  ', width: 160,sort: true, align:'center',templet: function(d){
                     if(d.commstatus == 1){
                         return '<span class="layui-badge-rim" style="margin-right: 10px">正常</span>';
                     }else if(d.commstatus == 3){
                         return '<span class="layui-badge-rim" style="margin-right: 10px">待审核</span>';
                     }else if(d.commstatus == 4){
                           return '<span class="layui-badge-rim" style="margin-right: 10px">已售出</span>';
                       }else if(d.commstatus == 5){
                           return '<span class="layui-badge-rim" style="margin-right: 10px">已捐赠</span>';
                       }
                 }},
                , {field: 'createtime', title: '创建时间', width: 160,sort: true, align:'center'}
                , {field: 'updatetime', title: '更新时间', width: 160,sort: true, align:'center'}
                , {fixed: 'right', title: '操作', toolbar: '#barDemo', width:250, align:'center'}
            ]], done: function (res, curr, count) {
                var i=1;
                $("[data-field='qid']").children().each(function () {
                    if($(this).text() == 'ID') {
                        $(this).text("ID")
                    }else{
                        $(this).text(i++)
                    }
                });
            }
        });
        //监听行工具事件
        table.on('tool(test)', function (obj) {
            var data = obj.data;
            if (obj.event === 'xiangqing') {
                window.open(basePath+"/product-detail/"+data.commid)
            }else if (obj.event === 'bianji') {
                layer.open({
                    type: 2,
                    title: '修改商品',
                    shadeClose: true,
                    shade: 0.8,
                    maxmin: true,
                    area: ['80%', '80%'],
                    content: basePath+'/user/editgoods/'+data.commid,
                    end: function () {
                        location.reload();
                    }
                });
            }else if(obj.event === 'shanchu'){
                layer.confirm('确认删除商品吗？', {
                    btn: ['确定','算了'], //按钮
                    title:"删除商品",
                    offset:"50px"
                }, function(){
                    layer.closeAll();
                    $.ajax({
                        url: basePath+'/user/changecommstatus/'+data.commid+"/2",
                        data: "",
                        contentType: "application/json;charset=UTF-8", //发送数据的格式
                        type: "get",
                        dataType: "json", //回调
                        beforeSend: function () {
                            layer.load(1, { //icon支持传入0-2
                                content: '请稍等...',
                                success: function (layero) {
                                    layero.find('.layui-layer-content').css({
                                        'padding-top': '39px',
                                        'width': '60px'
                                    });
                                }
                            });
                        },
                        complete: function () {
                            layer.closeAll('loading');
                        },
                        success: function (data) {
                            console.log(data)
                            if(data.status===200){
                                layer.msg(data.message, {
                                    time: 1000,
                                    icon: 1,
                                    offset: '50px'
                                }, function () {
                                    location.reload();
                                });
                            }else {
                                layer.msg(data.message, {
                                    time: 1000,
                                    icon: 2,
                                    offset: '50px'
                                });
                            }
                        }
                    });
                }, function(){
                });
            }else if (obj.event === 'yishou') {
                layer.confirm('确认设置该商品为已售吗？', {
                    btn: ['确定','算了'], //按钮
                    title:"售出商品",
                    offset:"50px"
                }, function(){
                    layer.closeAll();
                    $.ajax({
                        url: basePath+'/user/changecommstatus/'+data.commid+"/4",
                        data: "",
                        contentType: "application/json;charset=UTF-8", //发送数据的格式
                        type: "get",
                        dataType: "json", //回调
                        beforeSend: function () {
                            layer.load(1, { //icon支持传入0-2
                                content: '请稍等...',
                                success: function (layero) {
                                    layero.find('.layui-layer-content').css({
                                        'padding-top': '39px',
                                        'width': '60px'
                                    });
                                }
                            });
                        },
                        complete: function () {
                            layer.closeAll('loading');
                        },
                        success: function (data) {
                            console.log(data)
                            if(data.status===200){
                                layer.msg(data.message, {
                                    time: 1000,
                                    icon: 1,
                                    offset: '50px'
                                }, function () {
                                    location.reload();
                                });
                            }else {
                                layer.msg(data.message, {
                                    time: 1000,
                                    icon: 2,
                                    offset: '50px'
                                });
                            }
                        }
                    });
                }, function(){
                });
            }else if (obj.event === 'donate') {
                    var index = layer.open({
                        type: 1,
                        title: "填写回收信息",
                        shadeClose: true,
                        area: ['500px', '400px'],
                        shadeClose: true,
                        content: $("#add-main"),
                        success: function () {
                            // 数据回显
                            form.val("layui-edit-form", {
                                "commodityId":data.commid,
                                "commodityName": data.commname,
                                "commodityDesc": data.commdesc,
                                "userId": data.userid,
                                "type":2,
                            })
                        }
                    })
            }
        });


    // 提交按钮点击事件监听
    form.on('submit(donate)', function (data) {
        // 在这里处理表单提交逻辑
        // data.field 包含表单中的所有字段值
        console.log(data.field);
        // 将表单数据转换为 JSON 字符串
        var jsonData = JSON.stringify(data.field);
         $.ajax({
             url: basePath + "/donate/add",
             type: 'POST',
            contentType: "application/json;charset=UTF-8", //发送数据的格式
             data: jsonData,
             success: function (data) {
                if (data.status == 200) {
                    layer.msg(data.message, {
                        time: 1000,
                        icon: 1,
                        offset: '100px'
                    },function(index){
                         CloseWin();
                     })
                }else {
                    layer.msg(data.message, {
                        time: 1000,
                        icon: 5,
                        offset: '100px'
                    });
                }
             },
             error: function (err) {
                 // 处理失败的回调
                 layer.msg("系统错误", {
                     time: 1000,
                     icon: 2,
                     offset: '100px'
                 });
             }
         });

        // 阻止表单跳转
        return false;

    });


    });

   }

          //关闭页面
          function CloseWin(){
                location.reload(); // 页面刷新
                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                parent.layer.close(index); //再执行关闭
          }
lookallproduct(100);