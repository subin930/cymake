import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: Login
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/Login.vue')
    },
    {
      path: '/signup',
      name: 'signup',
      component: () => import('@/views/Signup.vue')
    },
    {
      path: '/archive',
      name: 'Archive',
      component: () => import('@/views/Archive.vue')
    },
    {
      path: '/drive/:searchBody?',
      name: 'drive',
      component: () => import('@/views/Drive.vue'),
      props: true
    },
    {
      path: '/archive/total',
      name: 'ArchiveTotal',
      component: () => import('@/views/ArchiveTotal.vue'),
      props: route => ({ // ! 쿼리 파라미터를 props로 전달하여 뒤로가기 시 상태를 유지
        carPage: parseInt(route.query.carPage) || 1,
        beautyPage: parseInt(route.query.beautyPage) || 1,
        searchBody: route.query.searchBody || ''
      })
    },
    {
      path: '/mypage',
      name: 'mypage',
      component: () => import('@/views/MyPage.vue')
    },
    {
      path: '/search/:searchBody',
      name: 'Search',
      component: () => import('@/views/Search.vue'),
      props: true,
    }
  ]
})

export default router
