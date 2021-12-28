import axios from "axios";

export default {
    async login(username, password) {
        return await axios.post('/api/auth',
            {'username': username, 'password': password},
            {headers: {"Content-Type": "application/json"}}
        ).then(() => true).catch(() => false);
    },
    async getUserList() {

    }
}