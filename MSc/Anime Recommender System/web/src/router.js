import Vue from 'vue'
import VueRouter from 'vue-router'

import Login from './components/pages/Login'
import Rating from './components/pages/Rating'
import Category from './components/pages/Category'
import Recommendation from './components/pages/Recommendation'
import Evaluation from './components/pages/Evaluation'
import Thank from './components/pages/Thank'

import login from './auth'
import store from './store'


Vue.use(VueRouter)

const routes = [
    { path: '/', redirect: '/login' },
    { path: '/evaluation', name: 'evaluation', component: Evaluation },
    { path: '/thank', name: 'thank', component: Thank },
    { path: '/login', name: 'login', component: Login },
    { path: '/rating', name: 'rating', component: Rating, meta:{ middleware: login } },
    { path: '/category', name: 'category', component: Category, meta:{ middleware: login } },
    { path: '/recommendation', name: 'recommendation', component: Recommendation, meta:{ middleware: login } }
]

const router = new VueRouter({
    mode: 'history',
    routes
})

router.beforeEach((to, from, next) => {

    if(!to.meta.middleware) return next()

    const middleware = to.meta.middleware
    const context = {
        to,
        from,
        next,
        store
    }

    return middleware({...context})

  })

export default router