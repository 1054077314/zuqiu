/**
 * 页面跳转
 */
function jump(url) {
    if (!url || url == 'null' || url == null) {
        window.location.href = './index.html';
    }
    localStorage.setItem('iframeUrl', url.replace('../', './pages/'));
    window.location.href = url;
}

/**
 * 返回上一页
 */
function back(num = -1) {
    window.history.go(num)
}

/**
 * 生成订单号
 */
function genTradeNo() {
    var date = new Date();
    var tradeNo = date.getFullYear().toString() + (date.getMonth() + 1).toString() +
        date.getDate().toString() + date.getHours().toString() + date.getMinutes().toString() +
        date.getSeconds().toString() + date.getMilliseconds().toString();
    for (var i = 0; i < 5; i++) {
        tradeNo += Math.floor(Math.random() * 10);
    }
    return tradeNo;
}

/*
 * 生成分享路径输入框
 */
function createElement(text) {
    var isRTL = document.documentElement.getAttribute('dir') === 'rtl';
    var element = document.createElement('textarea');
    element.style.fontSize = '12pt';
    element.style.border = '0';
    element.style.padding = '0';
    element.style.margin = '0';
    element.style.position = 'absolute';
    element.style[isRTL ? 'right' : 'left'] = '-9999px';
    let yPosition = window.pageYOffset || document.documentElement.scrollTop;
    element.style.top = `${yPosition}px`;
    element.setAttribute('readonly', '');
    element.value = text;
    document.body.appendChild(element);
    return element;
}

/**
 * 获取当前时间 yyyy-MM-dd hh:mm:ss
 */
function getCurDateTime() {
    var currentTime = new Date(),
    year = currentTime.getFullYear(),
    month = currentTime.getMonth() + 1 < 10 ? '0' + (currentTime.getMonth() + 1) : currentTime.getMonth() + 1,
    day = currentTime.getDate() < 10 ? '0' + currentTime.getDate() : currentTime.getDate(),
    hour = currentTime.getHours(),
    minute = currentTime.getMinutes(),
    second = currentTime.getSeconds();
    return year + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second;
}

/**
 * 获取年份
 */
function getYearFormat(currentTime) {
    if (currentTime == null) {
        currentTime = new Date();
    }
    return currentTime.getFullYear();
}

/**
 * 获取月份 yyyy-MM
 */
function getMonthFormat(currentTime) {
    if (currentTime == null) {
        currentTime = new Date();
    }
    var year = currentTime.getFullYear();
    var month = currentTime.getMonth() + 1 < 10 ? '0' + (currentTime.getMonth() + 1) : currentTime.getMonth() + 1;
    return year + '-' + month;
}

/**
 * 获取日期 yyyy-MM-dd
 */
function getDateFormat(currentTime) {
    if (currentTime == null) {
        currentTime = new Date();
    }
    var year = currentTime.getFullYear();
    var month = currentTime.getMonth() + 1 < 10 ? '0' + (currentTime.getMonth() + 1) : currentTime.getMonth() + 1;
    var day = currentTime.getDate() < 10 ? '0' + currentTime.getDate() : currentTime.getDate();
    return year + '-' + month + '-' + day;
}

/**
 * 获取时间 yyyy-MM-dd hh:mm:ss
 */
function getDatetimeFormat(currentTime) {
    if (currentTime == null) {
        currentTime = new Date();
    }
    var year = currentTime.getFullYear();
    var month = currentTime.getMonth() + 1 < 10 ? '0' + (currentTime.getMonth() + 1) : currentTime.getMonth() + 1;
    var day = currentTime.getDate() < 10 ? '0' + currentTime.getDate() : currentTime.getDate();
    var hour = currentTime.getHours();
    var minute = currentTime.getMinutes();
    var second = currentTime.getSeconds();
    return year + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second;
}
