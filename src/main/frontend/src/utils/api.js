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
    async register(username, password, firstName, lastName, email, enabled, role) {
        return await axios.post('/api/auth/register',
            {'username': username, 'password': password, 'firstName': firstName, 'lastName': lastName,
             'email': email, 'enabled': enabled, 'role': role
            }
        ).then(response => {
            return response
        }).catch(() => {
            return false
        });
    },
    async getUserList() {

    }
}