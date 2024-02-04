var vcid="";
var vcuserid="";
layui.use(['form', 'util','layer','carousel'], function () {
    var util = layui.util;
    var form = layui.form;
    var layer = layui.layer;
    var carousel = layui.carousel;
    carousel.render({
        elem: '#test1'
        ,arrow: 'always'
        ,height: '500px'
    });
    util.fixbar({
        bar1: false,
        bar2: false,
        bgcolor: "#08BF91",
        css: {
            right:30,
            bottom: 30
        },
        click: function (type) {
            if (type === 'bar1') {
            }
        }
    });
    layer.photos({
        photos: '#layer-photos-demo'
        ,anim: 5
    });
    form.on('submit(demo1)', function(data){
        $("#subid").addClass("layui-btn-disabled");
        $("#subid").attr("disabled", true);
        var object = new Object();
        object["commid"] = goodsid;
        object["spuserid"] = gooduser;
        object["content"] = data.field.content;
        var jsonData = JSON.stringify(object);
        $.ajax({
            url: basePath + "/comment/insert",
            data: jsonData,
            contentType: "application/json;charset=UTF-8", //发送数据的格式
            type: "post",
            dataType: "json", //回调
            beforeSend: function () {
                layer.load(1, { //icon支持传入0-2
                    content: '提交中...',
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
                if (data.status == 200) {
                    lookallcomment();
                    $("#content").val("")
                    layer.msg(data.message, {
                        time: 1000,
                        icon: 1,
                        offset: '100px'
                    });
                }else {
                    layer.msg(data.message, {
                        time: 1000,
                        icon: 5,
                        offset: '100px'
                    });
                }
                $("#subid").removeClass("layui-btn-disabled");
                $("#subid").attr("disabled", false);
            },error:function () {
                layer.msg("系统错误", {
                    time: 1000,
                    icon: 2,
                    offset: '100px'
                });
            }
        });
        return false;
    });
    form.on('submit(demo2)', function(data){
        var object = new Object();
        object["commid"] = goodsid;
        object["spuserid"] = gooduser;
        object["cid"] = vcid;
        object["cuserid"] = vcuserid;
        object["recontent"] = data.field.content;
        var jsonData = JSON.stringify(object);
        $.ajax({
            url: basePath + "/reply/insert",
            data: jsonData,
            contentType: "application/json;charset=UTF-8", //发送数据的格式
            type: "post",
            dataType: "json", //回调
            beforeSend: function () {
                layer.load(1, { //icon支持传入0-2
                    content: '提交中...',
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
                if (data.status == 200) {
                    lookallcomment();
                    $("#content").val("");
                    $('#content').attr('placeholder', '请输入评论内容');
                    $("#subid").show();
                    $("#reply").hide();
                    layer.msg(data.message, {
                        time: 1000,
                        icon: 1,
                        offset: '100px'
                    });
                }else {
                    layer.msg(data.message, {
                        time: 1000,
                        icon: 5,
                        offset: '100px'
                    });
                }
            },error:function () {
                layer.msg("系统错误", {
                    time: 1000,
                    icon: 2,
                    offset: '100px'
                });
            }
        });
        return false;
    });

    // 提交按钮点击事件监听
    form.on('submit(buy)', function (data) {
        // 在这里处理表单提交逻辑
        // data.field 包含表单中的所有字段值
        console.log(data.field);
        // 将表单数据转换为 JSON 字符串
        var jsonData = JSON.stringify(data.field);
         $.ajax({
             url: basePath + "/order/create",
             type: 'POST',
            contentType: "application/json;charset=UTF-8", //发送数据的格式
             data: jsonData,
             success: function (data) {
                if (data.status == 200) {
                    layer.msg(data.message, {
                        time: 1000,
                        icon: 1,
                        offset: '100px'
                    });
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

function buy(){
    layui.use('layer', function () {
            var layer = layui.layer;

            // 打开弹窗
            layer.open({
                type: 1,
                title: '收件信息',
                area: ['400px', '300px'],
                content: $('#add-main'),
                success: function (layero, index) {
                    // 弹窗弹出后的回调
                    layui.form.render(); // 重新渲染表单，确保表单元素正常显示
                }
            });
        });
}

function addCart() {
    var object = new Object();
    object["commid"] = goodsid;
    object["cmuserid"] = gooduser;
    object["commname"] = $("#commname").text();
    object["commdesc"] = $("#commdesc").text();
    object["username"] = $("#username").text();
    object["school"] = $("#school").text();
    var jsonData = JSON.stringify(object);
    $.ajax({
        url: basePath + "/collect/operate",
        data: jsonData,
        contentType: "application/json;charset=UTF-8", //发送数据的格式
        type: "post",
        dataType: "json", //回调
        beforeSend: function () {
            layer.load(1, { //icon支持传入0-2
                content: '提交中...',
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
            if (data.status == 200) {
                layer.msg(data.message, {
                    time: 1000,
                    icon: 1,
                    offset: '100px'
                });
            }else {
                layer.msg(data.message, {
                    time: 1000,
                    icon: 5,
                    offset: '100px'
                });
            }
        },error:function () {
            layer.msg("系统错误", {
                time: 1000,
                icon: 2,
                offset: '100px'
            });
        }
    });
}
var app = new Vue({
    el: '#comments',
    data() {
        return {
            listcomments: [],
        }
    },
    mounted: function () {
        window.lookallcomment = this.lookallcomment;
        window.replyuser = this.replyuser;
        window.vcid = this.vcid;
        window.vcuserid = this.vcuserid;
    },
    methods: {
        lookallcomment: function () {
            var that = this;
            $.ajax({
                url: basePath + "/comment/query/"+goodsid,
                data: "",
                contentType: "application/json;charset=UTF-8", //发送数据的格式
                type: "get",
                dataType: "json", //回调
                success: function (data) {
                    that.listcomments=data.data;
                    if(that.listcomments.length>0){
                        $("#havecomment").show();
                    }else{
                        $("#nocomment").show();
                    }
                },error:function () {
                    layer.msg("系统错误", {
                        time: 1000,
                        icon: 5,
                        offset: '100px'
                    });
                }
            });
        },replyuser:function (username,cid,cuserid) {
            $("#content").val("");
            $("#subid").hide();
            $("#reply").show();
            $("#content").focus();
            $('#content').attr('placeholder', '@'+username);
            vcid=cid;
            vcuserid=cuserid;
        }
    },filters: {
        //timeago.js插件
        //计算时间，类似于几分钟前，几小时前，几天前等
        changeTime(val){
            var time = new Date(val); //先将接收到的json格式的日期数据转换成可用的js对象日期
            return new timeago().format(time, 'zh_CN'); //转换成类似于几天前的格式
        }
    }
});
lookallcomment();