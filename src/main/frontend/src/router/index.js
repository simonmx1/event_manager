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
        path: '/locations',
        name: 'LocationManagement',
        component: () => import('@/components/LocationManagement.vue')
    },
    {
        path: '/events',
        name: 'EventManagement',
        component: () => import('@/components/EventManagement.vue')
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
    } else next()

    if (to.name === 'UserManagement' && (session === false || session[1] !== 'ADMIN'))
        next(false)

    if (to.name === 'LocationManagement' && (session === false || (session[1] !== 'LOCATION_MANAGER' || session[1] !== 'ADMIN')))
        next(false)

    next()
})

export default router
