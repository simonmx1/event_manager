import axios from "axios";

export default {
    async login(username, password) {
        return await axios.post('/api/auth',
            {'username': username, 'password': password},
            {headers: {"Content-Type": "application/json"}}
        ).then(response => {            
            localStorage.setItem('jwt', JSON.stringify(response.data.jwt));
            return true;
        }).catch(() => false);
    },
    async logout() {
        localStorage.removeItem('jwt');
    },
    async getUserList() {

    }
}