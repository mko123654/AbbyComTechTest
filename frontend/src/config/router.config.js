// eslint-disable-next-line
import { UserLayout, BasicLayout, RouteView, BlankLayout, PageView } from '../layouts'
import { examList, examAdmin, questionAdmin } from '../core/icons'

export const asyncRouterMap = [
  {
    path: '/',
    name: 'index',
    component: BasicLayout,
    meta: { title: 'Abby.com 線上考試系統' },
    redirect: '/dashboard/home',
    children: [
      // dashboard
      {
        path: '/dashboard',
        name: 'dashboard',
        redirect: '/dashboard/home',
        component: RouteView,
        hideChildrenInMenu: true,
        meta: { title: '首頁', keepAlive: true, icon: 'home', permission: ['dashboard'] },
        children: [
          {
            path: '/dashboard/home',
            name: 'Workplace',
            component: () => import('../views/Home'),
            meta: { title: '簡介', keepAlive: true, permission: ['dashboard'] }
          }
        ]
      },

      {
        path: '/exam-card',
        name: 'exam-card',
        redirect: '/list/exam-card',
        component: PageView,
        hideChildrenInMenu: true,
        meta: { title: '待考科目', keepAlive: true, icon: examList, permission: ['exam-card'] },
        children: [
          {
            path: '/list/exam-card',
            name: 'ExamCardList',
            component: () => import('../views/list/ExamCardList'),
            meta: { title: '待考科目列表', keepAlive: true, permission: ['exam-card'] }
          }
        ]
      },
      {
        path: '/question-admin',
        name: 'question-admin',
        redirect: '/list/question-table-list',
        component: PageView,
        hideChildrenInMenu: true,
        meta: { title: '考題管理', keepAlive: true, icon: questionAdmin, permission: ['question-admin'] },
        children: [
          {
            path: '/list/question-table-list',
            name: 'QuestionTableListWrapper',
            hideChildrenInMenu: true, // 強制顯示 MenuItem 而不是 SubMenu
            component: () => import('../views/list/QuestionTableList'),
            meta: { title: '考題列表', keepAlive: true, permission: ['question-admin'] }
          }
        ]
      },
      // list
      {
        path: '/list/exam-table-list',
        name: 'exam-table-list',
        component: PageView,
        redirect: '/list/exam-table-list',
        hideChildrenInMenu: true,
        meta: { title: '考試管理', icon: examAdmin, permission: ['exam-table-list'] },
        children: [
          {
            path: '/list/exam-table-list',
            name: 'ExamTableListWrapper',
            hideChildrenInMenu: true,
            component: () => import('../views/list/ExamTableList'),
            meta: { title: '考試列表', keepAlive: true, permission: ['exam-table-list'] }
          }
        ]
      },
      {
        path: '/exam-record-list',
        name: 'exam-record-list',
        redirect: '/list/exam-record-list',
        component: PageView,
        hideChildrenInMenu: true,
        meta: { title: '我的考試紀錄', keepAlive: true, icon: 'user', permission: ['exam-record-list'] },
        children: [
          {
            path: '/list/exam-record-list',
            name: 'ExamRecordList',
            component: () => import('../views/list/ExamRecordList'),
            meta: { title: '參加過的考試列表', keepAlive: true, permission: ['exam-record-list'] }
          }
        ]
      },
      // account
      {
        path: '/account',
        component: RouteView,
        redirect: '/account/center',
        name: 'account',
        // 隱藏這個路徑，但因為是需要的，所以不能删掉
        hidden: true,
        meta: { title: '個人頁面', icon: 'user', keepAlive: true, permission: ['user'] },
        children: [
          {
            path: '/account/settings',
            name: 'settings',
            component: () => import('../views/account/settings/Index'),
            meta: { title: '個人設定', hideHeader: true, permission: ['user'] },
            redirect: '/account/settings/base',
            hideChildrenInMenu: true,
            children: [
              {
                path: '/account/settings/base',
                name: 'BaseSettings',
                component: () => import('../views/account/settings/BaseSetting'),
                meta: { title: '基本設定', hidden: true, permission: ['user'] }
              },
              {
                path: '/account/settings/custom',
                name: 'CustomSettings',
                component: () => import('../views/account/settings/Custom'),
                meta: { title: '個性化設定', hidden: true, keepAlive: true, permission: ['user'] }
              }
            ]
          }
        ]
      }
    ]
  },
  {
    // 讀不到的路徑都進404
    path: '*', redirect: '/404', hidden: true
  }
]

export const constantRouterMap = [
  {
    path: '/user',
    component: UserLayout,
    redirect: '/user/login',
    hidden: true,
    children: [
      {
        path: 'login',
        name: 'login',
        component: () => import(/* webpackChunkName: "user" */ '../views/user/Login')
      },
      {
        path: 'register',
        name: 'register',
        component: () => import(/* webpackChunkName: "user" */ '../views/user/Register')
      },
      {
        path: 'register-result',
        name: 'registerResult',
        component: () => import(/* webpackChunkName: "user" */ '../views/user/RegisterResult')
      }
    ]
  },

  {
    path: '/test',
    component: BlankLayout,
    redirect: '/test/home',
    children: [
      {
        path: 'home',
        name: 'TestHome',
        component: () => import('../views/Home')
      },
      {
        path: 'note',
        name: 'NoteTest',
        component: () => import('@views/test/SummerNoteDemo')
      },
      {
        path: 'table',
        name: 'TableTest',
        component: () => import('@views/test/BootStrapTableDemo')
      }
    ]
  },

  {
    path: '/404',
    component: () => import(/* webpackChunkName: "fail" */ '../views/exception/404')
  },
  {
    path: '/exam/:id',
    component: () => import(/* webpackChunkName: "fail" */ '../views/list/ExamDetail')
  },
  {
    path: '/exam/record/:exam_id/:record_id',
    component: () => import(/* webpackChunkName: "fail" */ '../views/list/ExamRecordDetail')
  }
]
