var projectName = '足球俱乐部管理系统';

/**
 * 轮播图配置
 */
var swiper = {
    width: '100%',
    height: '400px',
    arrow: 'hover',
    anim: 'default',
    interval: 3000,
    indicator: 'inside'
};

/**
 * 个人中心菜单
 */
var centerMenu = [
    {
        name: '个人中心',
        url: '../' + (localStorage.getItem('userTable') || 'yonghu') + '/center.html'
    },
    {
        name: '合同信息',
        url: '../hetong/list.html'
    },
    {
        name: '球员数据',
        url: '../shuju/list.html'
    },
    {
        name: '训练计划',
        url: '../xunlian/list.html'
    }
];

/**
 * 前台导航
 */
var indexNav = [
    {
        name: '公告信息',
        url: './pages/gonggao/list.html'
    },
    {
        name: '赛事信息',
        url: './pages/saishi/list.html'
    },
    {
        name: '教练信息',
        url: './pages/jiaolian/list.html'
    },
    {
        name: '球员数据',
        url: './pages/shuju/list.html'
    },
    {
        name: '训练计划',
        url: './pages/xunlian/list.html'
    },
    {
        name: '合同信息',
        url: './pages/hetong/list.html'
    },
    {
        name: '用户信息',
        url: './pages/yonghu/list.html'
    }
];

function resolveProjectBase() {
    var protocol = window.location.protocol || '';
    if (protocol === 'file:') {
        return 'http://localhost:8080/zuqiujulebguanli/';
    }

    var origin = window.location.origin || (window.location.protocol + '//' + window.location.host);
    if (!origin || origin === 'null') {
        return 'http://localhost:8080/zuqiujulebguanli/';
    }

    var pathname = window.location.pathname || '';
    var context = '/zuqiujulebguanli/';
    var matched = pathname.match(/^(.*?\/zuqiujulebguanli\/)/);
    if (matched && matched[1]) {
        context = matched[1];
    }
    return origin + context.replace(/\/+$/, '/');
}

var adminurl = resolveProjectBase() + 'admin/dist/index.html#/login';

var menu = [
    {
        roleName: '管理员',
        tableName: 'users',
        frontMenu: [],
        backMenu: []
    },
    {
        roleName: '教练',
        tableName: 'jiaolian',
        frontMenu: [],
        backMenu: []
    },
    {
        roleName: '用户',
        tableName: 'yonghu',
        frontMenu: [],
        backMenu: []
    }
];

var frontPermissionMap = {
    yonghu: {
        hetong: ['新增', '查看', '修改', '删除'],
        shuju: ['新增', '查看', '修改', '删除'],
        xunlian: ['新增', '查看', '修改', '删除'],
        saishi: ['查看'],
        gonggao: ['查看'],
        jiaolian: ['查看'],
        yonghu: ['查看', '修改']
    },
    jiaolian: {
        saishi: ['查看', '新增', '修改', '删除'],
        gonggao: ['查看'],
        hetong: ['查看'],
        shuju: ['查看'],
        xunlian: ['查看'],
        jiaolian: ['查看', '修改'],
        yonghu: ['查看']
    },
    users: {
        saishi: ['查看', '新增', '修改', '删除'],
        gonggao: ['查看', '新增', '修改', '删除'],
        hetong: ['查看', '新增', '修改', '删除'],
        shuju: ['查看', '新增', '修改', '删除'],
        xunlian: ['查看', '新增', '修改', '删除'],
        jiaolian: ['查看', '新增', '修改', '删除'],
        yonghu: ['查看', '新增', '修改', '删除'],
        config: ['查看', '新增', '修改', '删除'],
        dictionary: ['查看', '新增', '修改', '删除']
    }
};

var isAuth = function(tableName, key) {
    var role = localStorage.getItem('userTable');
    var permissions = frontPermissionMap[role] || {};
    var buttons = permissions[tableName] || [];
    return buttons.indexOf(key) !== -1;
};

var isFrontAuth = function(tableName, key) {
    return isAuth(tableName, key);
};
