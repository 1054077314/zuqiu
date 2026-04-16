/**
 * 邮箱校验
 */
function isEmail(s) {
    if (s) {
        return /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(s)
    }
    return true;
}

/**
 * 手机号校验
 */
function isMobile(s) {
    if (s) {
        return /^1[0-9]{10}$/.test(s)
    }
    return true;
}

/**
 * 固定电话校验
 */
function isPhone(s) {
    if (s) {
        return /^([0-9]{3,4}-)?[0-9]{7,8}$/.test(s)
    }
    return true;
}

/**
 * URL 地址校验
 */
function isURL(s) {
    if (s) {
        return /^http[s]?:\/\/.*/.test(s)
    }
    return true;
}

/**
 * 数字校验，可为小数，可为空
 */
function isNumber(s) {
    if (s) {
        return /(^-?[+-]?([0-9]*\.?[0-9]+|[0-9]+\.?[0-9]*)([eE][+-]?[0-9]+)?$)|(^$)/.test(s);
    }
    return true;
}

/**
 * 整数校验，可为空
 */
function isIntNumer(s) {
    if (s) {
        return /(^-?\d+$)|(^$)/.test(s);
    }
    return true;
}

/**
 * 身份证号校验
 */
function isIdentity(idcard) {
    const regIdCard = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
    if (idcard) {
        return regIdCard.test(idcard);
    }
    return true;
}
