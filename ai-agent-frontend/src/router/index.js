import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue'),
    meta: {
      title: '首页 - YUANYU智能体平台',
      description: 'YUANYU智能体平台提供AI健康生活顾问和AI超级智能体服务，满足您的各种AI对话需求'
    }
  },
  {
    path: '/master',
    name: 'Master',
    component: () => import('../views/Master.vue'),
    meta: {
      title: 'AI健康生活顾问 - YUANYU智能体平台',
      description: 'AI健康生活顾问是YUANYU智能体平台的专业健康顾问，帮你解答健康、生活、养生等问题，提供科学建议'
    }
  },
  {
    path: '/super-agent',
    name: 'SuperAgent',
    component: () => import('../views/SuperAgent.vue'),
    meta: {
      title: 'AI超级智能体 - YUANYU智能体平台',
      description: 'AI超级智能体是YUANYU智能体平台的全能助手，能解答各类专业问题，提供精准建议和解决方案'
    }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 全局导航守卫，设置文档标题
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta.title) {
    document.title = to.meta.title
  }
  next()
})

export default router 