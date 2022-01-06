import axios from "axios";

export default {
    async login(username, password) {
        return await axios.post('/api/auth/login',
            {'username': username, 'password': password},
            {headers: {"Content-Type": "application/json"}}
        ).then(response => {
            localStorage.setItem('jwt', JSON.stringify(response.data.msg));
            return true;
        }).catch(() => false);
    },
    async logout() {
        localStorage.removeItem('jwt');
    },
    async register(user) {
        return await axios.post('/api/auth/register', {
            'username': user.username, 'password': user.password, 'firstName': user.firstName, 'lastName': user.lastName,
            'email': user.email, 'enabled': user.enabled, 'role': user.role
            }
        ).then(response => {
            return response
        }).catch(() => false);
    },
    async getUsers() {
        return await axios.get('/api/users/get',
            {headers: {"Authorization": "Bearer " + JSON.parse(localStorage.getItem('jwt'))}}
        ).then(response => {
            return response.data;
        }).catch(() => false);
    },
    async editUser(user) {
        return await axios.post('/api/users/edit', {
                'username': user.username, 'password': user.password, 'firstName': user.firstName, 'lastName': user.lastName,
                'email': user.email, 'enabled': user.enabled, 'role': user.role
            },
            {
                headers: {
                    "Authorization": "Bearer " + JSON.parse(localStorage.getItem('jwt')),
                    "Content-Type": "application/json"
                }
            }
        ).then(response => {
            return response;
        }).catch(() => false);
    },
    async deleteUser(username) {
        return await axios.post('/api/users/delete', {'username': username},
            {
                headers: {
                    "Authorization": "Bearer " + JSON.parse(localStorage.getItem('jwt')),
                    "Content-Type": "application/json"
                }
            }
        ).then(response => {
            return response;
        }).catch(() => false);
    },
    async loggedIn() {
        if (JSON.parse(localStorage.getItem('jwt')) == null) {
            return ""
        } else {
            return await axios.post('/api/auth/loggedIn',
                {'jwt': JSON.parse(localStorage.getItem('jwt'))},
                {headers: {"Content-Type": "application/json"}}
            ).then(response => response.data).catch(() => false)
        }

    }
}