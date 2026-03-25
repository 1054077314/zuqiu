import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const VueRouterPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(to) {
  return VueRouterPush.call(this, to).catch(err => err)
}

import Index from '@/views/index'
import Home from '@/views/home'
import Login from '@/views/login'
import NotFound from '@/views/404'
import UpdatePassword from '@/views/update-password'
import register from '@/views/register'
import center from '@/views/center'

import users from '@/views/modules/users/list'
import gonggao from '@/views/modules/gonggao/list'
import hetong from '@/views/modules/hetong/list'
import jiaolian from '@/views/modules/jiaolian/list'
import saishi from '@/views/modules/saishi/list'
import shuju from '@/views/modules/shuju/list'
import xunlian from '@/views/modules/xunlian/list'
import yonghu from '@/views/modules/yonghu/list'
import config from '@/views/modules/config/list'
import dictionaryGonggao from '@/views/modules/dictionaryGonggao/list'
import dictionarySaishi from '@/views/modules/dictionarySaishi/list'
import dictionarySex from '@/views/modules/dictionarySex/list'
import dictionaryShuju from '@/views/modules/dictionaryShuju/list'
import dictionaryXunlian from '@/views/modules/dictionaryXunlian/list'

const routes = [
  {
    path: '/index',
    name: '首页',
    component: Index,
    children: [
      {
        path: '/',
        name: '首页',
        component: Home,
        meta: { icon: '', title: 'home' }
      },
      {
        path: '/updatePassword',
        name: '修改密码',
        component: UpdatePassword,
        meta: { icon: '', title: 'updatePassword' }
      },
      {
        path: '/center',
        name: '个人信息',
        component: center,
        meta: { icon: '', title: 'center' }
      },
      {
        path: '/users',
        name: '管理员管理',
        component: users
      },
      {
        path: '/dictionaryGonggao',
        name: '公告类型管理',
        component: dictionaryGonggao
      },
      {
        path: '/dictionarySaishi',
        name: '赛事类型管理',
        component: dictionarySaishi
      },
      {
        path: '/dictionarySex',
        name: '性别类型管理',
        component: dictionarySex
      },
      {
        path: '/dictionaryShuju',
        name: '球员数据类型管理',
        component: dictionaryShuju
      },
      {
        path: '/dictionaryXunlian',
        name: '训练计划类型管理',
        component: dictionaryXunlian
      },
      {
        path: '/config',
        name: '轮播图信息',
        component: config
      },
      {
        path: '/gonggao',
        name: '公告信息管理',
        component: gonggao
      },
      {
        path: '/hetong',
        name: '合同管理',
        component: hetong
      },
      {
        path: '/jiaolian',
        name: '教练管理',
        component: jiaolian
      },
      {
        path: '/saishi',
        name: '赛事管理',
        component: saishi
      },
      {
        path: '/shuju',
        name: '球员数据管理',
        component: shuju
      },
      {
        path: '/xunlian',
        name: '训练计划管理',
        component: xunlian
      },
      {
        path: '/yonghu',
        name: '用户管理',
        component: yonghu
      }
    ]
  },
  {
    path: '/login',
    name: 'login',
    component: Login,
    meta: { icon: '', title: 'login' }
  },
  {
    path: '/register',
    name: 'register',
    component: register,
    meta: { icon: '', title: 'register' }
  },
  {
    path: '/',
    name: '首页重定向',
    redirect: '/index'
  },
  {
    path: '*',
    component: NotFound
  }
]

const router = new VueRouter({
  mode: 'hash',
  routes
})

export default router
