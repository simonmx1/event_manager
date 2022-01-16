import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '@/components/Home.vue'
import Login from '@/components/Login.vue'
import api from "@/utils/api";

Vue.use(VueRouter)

const routes = [
    {
        path: '/home',
        name: 'Home',
        component: Home
    },
    {
        path: '/login',
        name: 'Login',
        component: Login
    },
    {
        path: '/users',
        name: 'UserManagement',
        component: () => import('@/components/UserManagement.vue')
    },
    {
        path: '/location',
        name: 'LocationManagement',
        component: () => import('@/components/LocationManagement.vue')
    },
    {
        path: '/accountSettings',
        name: 'AccountSettings',
        component: () => import('@/components/AccountSettings.vue')
    },
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

router.beforeEach(async (to, from, next) => {
    let session = null;
    await api.user.loggedIn().then(response => {
        session = response;
    })
    if (to.name !== 'Login' && session === false) {
        next({name: 'Login'})
    }
    if (to.name === 'UserManagement') {
        api.user.loggedIn().then(response => {
            if (to.name === 'UserManagement' && response !== false && response[1] === 'ADMIN') next()
            else next(false)
        })
    } else next()
})

export default router
