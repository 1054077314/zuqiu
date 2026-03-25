layui.define(['jquery', 'layer'], function(exports) {
    'use strict';

    var jquery = layui.jquery;
    var layer = layui.layer;

    function resolveProjectBase() {
        if (typeof window.resolveProjectBase === 'function') {
            return window.resolveProjectBase();
        }
        var path = window.location.pathname || '';
        var ctx = (path.split('/')[1] || '').trim();
        return window.location.protocol + '//' + window.location.host + (ctx ? '/' + ctx : '');
    }

    function adaptUrl(url) {
        if (!url) {
            return url;
        }

        var adapted = url;

        adapted = adapted.replace(/^config\/list$/, 'config/page');
        adapted = adapted.replace(/^dictionary\/list$/, 'dictionary/page');

        adapted = adapted.replace(/^(saishi|gonggao|hetong|shuju|xunlian|jiaolian|yonghu|dictionary|config)\/list$/, '$1/page');
        adapted = adapted.replace(/^(saishi|gonggao|hetong|shuju|xunlian|jiaolian|yonghu|dictionary)\/detail\/(\d+)$/, '$1/info/$2');
        adapted = adapted.replace(/^(saishi|gonggao|hetong|shuju|xunlian|jiaolian|yonghu|dictionary)\/add$/, '$1/save');

        return adapted;
    }

    function getRequestErrorMessage(xhr) {
        if (xhr && xhr.status === 0) {
            return '???????????????';
        }
        if (xhr && xhr.status >= 500) {
            return '????????????';
        }
        return '??????';
    }

    var baseurl = resolveProjectBase();

    function resolveImageUrl(url, customBase) {
        var base = customBase || baseurl;
        if (typeof window.resolveImage === 'function') {
            return window.resolveImage(url, base);
        }
        return url;
    }

    function fixRichTextHtml(html, customBase) {
        var base = customBase || baseurl;
        if (typeof window.fixRichTextImages === 'function') {
            return window.fixRichTextImages(html, base);
        }
        return html || '';
    }

    var http = {
        domain: baseurl,
        baseurl: baseurl,
        resolveImage: function(url, customBase) {
            return resolveImageUrl(url, customBase);
        },
        fixRichTextImages: function(html, customBase) {
            return fixRichTextHtml(html, customBase);
        },
        getParam: function(name) {
            var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)');
            var r = window.location.search.substr(1).match(reg);
            if (r != null) {
                return decodeURI(r[2]);
            }
            return null;
        },
        request: function(url, type, data, callback) {
            var index = layer.load(1, {
                shade: [0.1, '#fff']
            });
            url = baseurl + '/' + adaptUrl(url).replace(/^\/+/, '');
            data = data || {};
            data.t = jquery.now();
            jquery.ajax({
                url: url,
                beforeSend: function(request) {
                    request.setRequestHeader('Token', localStorage.getItem('Token'));
                },
                contentType: 'application/x-www-form-urlencoded',
                data: data,
                dataType: 'json',
                type: type,
                success: function(result) {
                    if (result.code == 0) {
                        callback(result);
                    } else if (result.code == 401) {
                        window.location.href = baseurl + '/front/pages/login/login.html';
                    } else {
                        layer.msg(result.msg, {
                            time: 2000,
                            icon: 5
                        });
                    }
                    layer.close(index);
                },
                error: function(xhr, status, error) {
                    console.log(xhr, status, error);
                    layer.msg(getRequestErrorMessage(xhr), {
                        time: 2000,
                        icon: 5
                    });
                    layer.close(index);
                }
            });
        },
        requestJson: function(url, type, data, callback) {
            var index = layer.load(1, {
                shade: [0.1, '#fff']
            });
            url = baseurl + '/' + adaptUrl(url).replace(/^\/+/, '');
            data = data || {};
            data.t = jquery.now();
            var params = JSON.stringify(data);
            jquery.ajax({
                url: url,
                beforeSend: function(request) {
                    request.setRequestHeader('Token', localStorage.getItem('Token'));
                },
                contentType: 'application/json',
                data: params,
                dataType: 'json',
                type: type,
                success: function(result) {
                    if (result.code == 0) {
                        callback(result);
                    } else if (result.code == 401) {
                        window.location.href = baseurl + '/front/pages/login/login.html';
                    } else {
                        layer.msg(result.msg, {
                            time: 2000,
                            icon: 5
                        });
                    }
                    layer.close(index);
                },
                error: function(xhr, status, error) {
                    console.log(xhr, status, error);
                    layer.msg(getRequestErrorMessage(xhr), {
                        time: 2000,
                        icon: 5
                    });
                    layer.close(index);
                }
            });
        },
        upload: function(file, fileName, callback) {
            var url = baseurl + '/file/upload';
            var formData = new FormData();
            formData.append('file', file);
            formData.append('fileName', fileName);
            jquery.ajax({
                url: url,
                type: 'post',
                data: formData,
                headers: {
                    Token: localStorage.getItem('Token')
                },
                contentType: false,
                processData: false,
                success: function(res) {
                    if (res.code == 0) {
                        callback(res);
                    } else if (res.code == 401) {
                        window.location.href = baseurl + '/front/pages/login/login.html';
                    } else {
                        layer.msg(res.msg, {
                            time: 2000,
                            icon: 5
                        });
                    }
                },
                error: function(xhr, status, error) {
                    console.log(xhr, status, error);
                    layer.msg(getRequestErrorMessage(xhr), {
                        time: 2000,
                        icon: 5
                    });
                }
            });
        }
    };

    exports('http', http);
});
