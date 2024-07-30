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
      path: '/drive',
      name: 'drive',
      component: () => import('@/views/Drive.vue')
    },
    {
      path: '/archive/total/:searchBody?',
      name: 'ArchiveTotal',
      component: () => import('@/views/ArchiveTotal.vue')
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
