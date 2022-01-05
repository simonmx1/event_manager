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
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

router.beforeEach((to, from, next) => {
    if (to.name !== 'Login' && !localStorage.getItem('jwt')) {
        next({name: 'Login'})
    }
    if (to.name === 'UserManagement') {
        api.loggedIn().then(response => {
            console.log(response)
            if (to.name === 'UserManagement' && response !== false && response[1] === 'ADMIN') next()
            else next(false)
        })
    }
    else next()
})

export default router
