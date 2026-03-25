const menu = {
  list() {
    return [
      {
        backMenu: [
          {
            menu: '管理员管理',
            child: [
              {
                menu: '管理员管理',
                menuJump: '列表',
                tableName: 'users',
                buttons: ['查看', '新增', '修改', '删除']
              }
            ]
          },
          {
            menu: '教练管理',
            child: [
              {
                menu: '教练管理',
                menuJump: '列表',
                tableName: 'jiaolian',
                buttons: ['查看', '新增', '修改', '删除']
              }
            ]
          },
          {
            menu: '用户管理',
            child: [
              {
                menu: '用户管理',
                menuJump: '列表',
                tableName: 'yonghu',
                buttons: ['查看', '新增', '修改', '删除']
              }
            ]
          },
          {
            menu: '合同管理',
            child: [
              {
                menu: '合同管理',
                menuJump: '列表',
                tableName: 'hetong',
                buttons: ['查看', '新增', '修改', '删除']
              }
            ]
          },
          {
            menu: '赛事管理',
            child: [
              {
                menu: '赛事管理',
                menuJump: '列表',
                tableName: 'saishi',
                buttons: ['查看', '新增', '修改', '删除', '报表']
              }
            ]
          },
          {
            menu: '球员数据管理',
            child: [
              {
                menu: '球员数据管理',
                menuJump: '列表',
                tableName: 'shuju',
                buttons: ['查看', '新增', '修改', '删除']
              }
            ]
          },
          {
            menu: '训练计划管理',
            child: [
              {
                menu: '训练计划管理',
                menuJump: '列表',
                tableName: 'xunlian',
                buttons: ['查看', '新增', '修改', '删除']
              }
            ]
          },
          {
            menu: '公告信息管理',
            child: [
              {
                menu: '公告信息管理',
                menuJump: '列表',
                tableName: 'gonggao',
                buttons: ['查看', '新增', '修改', '删除']
              }
            ]
          },
          {
            menu: '基础数据管理',
            child: [
              {
                menu: '公告类型管理',
                menuJump: '列表',
                tableName: 'dictionaryGonggao',
                buttons: ['查看', '新增', '删除', '修改']
              },
              {
                menu: '赛事类型管理',
                menuJump: '列表',
                tableName: 'dictionarySaishi',
                buttons: ['查看', '新增', '删除', '修改']
              },
              {
                menu: '球员数据类型管理',
                menuJump: '列表',
                tableName: 'dictionaryShuju',
                buttons: ['查看', '新增', '删除', '修改']
              },
              {
                menu: '训练计划类型管理',
                menuJump: '列表',
                tableName: 'dictionaryXunlian',
                buttons: ['查看', '新增', '删除', '修改']
              },
              {
                menu: '性别类型管理',
                menuJump: '列表',
                tableName: 'dictionarySex',
                buttons: ['查看', '新增', '删除', '修改']
              }
            ]
          },
          {
            menu: '轮播图信息',
            child: [
              {
                menu: '轮播图管理',
                menuJump: '列表',
                tableName: 'config',
                buttons: ['查看', '新增', '修改', '删除']
              }
            ]
          }
        ],
        frontMenu: [],
        hasBackLogin: '是',
        hasBackRegister: '否',
        hasFrontLogin: '否',
        hasFrontRegister: '否',
        roleName: '管理员',
        tableName: 'users'
      },
      {
        backMenu: [
          {
            menu: '用户管理',
            child: [
              {
                menu: '用户管理',
                menuJump: '列表',
                tableName: 'yonghu',
                buttons: ['查看']
              }
            ]
          },
          {
            menu: '赛事管理',
            child: [
              {
                menu: '赛事管理',
                menuJump: '列表',
                tableName: 'saishi',
                buttons: ['查看']
              }
            ]
          },
          {
            menu: '球员数据管理',
            child: [
              {
                menu: '球员数据管理',
                menuJump: '列表',
                tableName: 'shuju',
                buttons: ['查看', '新增', '修改', '删除']
              }
            ]
          },
          {
            menu: '训练计划管理',
            child: [
              {
                menu: '训练计划管理',
                menuJump: '列表',
                tableName: 'xunlian',
                buttons: ['查看', '新增', '修改', '删除']
              }
            ]
          },
          {
            menu: '公告信息管理',
            child: [
              {
                menu: '公告信息管理',
                menuJump: '列表',
                tableName: 'gonggao',
                buttons: ['查看']
              }
            ]
          },
          {
            menu: '轮播图信息',
            child: [
              {
                menu: '轮播图管理',
                menuJump: '列表',
                tableName: 'config',
                buttons: ['查看']
              }
            ]
          }
        ],
        frontMenu: [],
        hasBackLogin: '是',
        hasBackRegister: '否',
        hasFrontLogin: '否',
        hasFrontRegister: '否',
        roleName: '教练',
        tableName: 'jiaolian'
      }
    ]
  }
}

export default menu
