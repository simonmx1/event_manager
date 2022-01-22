import axios from "axios";

export default {
    user: {
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
                    'username': user.username,
                    'password': user.password,
                    'firstName': user.firstName,
                    'lastName': user.lastName,
                    'email': user.email,
                    'enabled': user.enabled,
                    'role': user.role
                }
            ).then(response => {
                return response
            }).catch(() => false);
        },
        async changePassword(username, password) {
            return await axios.post('/api/users/password', {
                    'username': username,
                    'password': password,
                },
                {
                    headers: {
                        "Authorization": "Bearer " + JSON.parse(localStorage.getItem('jwt')),
                        "Content-Type": "application/json"
                    }
                }
            ).then(response => {
                return response
            }).catch(() => false);
        },
        async getAll() {
            return await axios.get('/api/users/getAll',
                {headers: {"Authorization": "Bearer " + JSON.parse(localStorage.getItem('jwt'))}}
            ).then(response => {
                return response.data;
            }).catch(() => false);
        },
        async get(username) {
            return await axios.get('/api/users/get?username=' + username,
                {headers: {"Authorization": "Bearer " + JSON.parse(localStorage.getItem('jwt'))}}
            ).then(response => {
                return response.data;
            }).catch(() => false);
        },
        async edit(user) {
            return await axios.post('/api/users/edit', {
                    'username': user.username,
                    'password': user.password,
                    'firstName': user.firstName,
                    'lastName': user.lastName,
                    'email': user.email,
                    'enabled': user.enabled,
                    'role': user.role
                },
                {
                    headers: {
                        "Authorization": "Bearer " + JSON.parse(localStorage.getItem('jwt')),
                        "Content-Type": "application/json"
                    }
                }
            ).then(response => {
                return response
            }).catch(() => false);
        },
        async delete(username) {
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
                return false
            } else {
                return await axios.post('/api/auth/loggedIn',
                    {'jwt': JSON.parse(localStorage.getItem('jwt'))},
                    {headers: {"Content-Type": "application/json"}}
                ).then(response => response.data).catch(error => {
                    if (error.response.status === 417) {
                        localStorage.removeItem('jwt')
                        location.reload();
                    }
                    return false;
                })
            }
        },
    },
    location: {
        async getAll() {
            return await axios.get('/api/location/getAll',
                {headers: {"Authorization": "Bearer " + JSON.parse(localStorage.getItem('jwt'))}}
            ).then(response => {
                return response.data;
            }).catch(() => false);
        },
        async get() {
            return await axios.get('/api/location/get',
                {headers: {"Authorization": "Bearer " + JSON.parse(localStorage.getItem('jwt'))}}
            ).then(response => {
                return response.data;
            }).catch(() => false);
        },
        async delete(locationId) {
            return await axios.post('/api/location/delete', {'locationId': locationId},
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
        async create(location) {
            return await axios.post('/api/location/create', {
                    'name': location.name,
                    'menu': location.menu,
                    'geolocation': location.geolocation,
                    'tags': location.tags,
                    'description': location.description,
                    'enabled': location.enabled
                },
                {
                    headers: {
                        "Authorization": "Bearer " + JSON.parse(localStorage.getItem('jwt')),
                        "Content-Type": "application/json"
                    }
                }
            ).then(response => {
                return response
            }).catch(() => false);
        },
        async edit(location) {
            return await axios.post('/api/location/edit', {
                    'locationId': location.locationId,
                    'name': location.name,
                    'menu': location.menu,
                    'geolocation': location.geolocation,
                    'tags': location.tags,
                    'description': location.description,
                    'enabled': location.enabled
                },
                {
                    headers: {
                        "Authorization": "Bearer " + JSON.parse(localStorage.getItem('jwt')),
                        "Content-Type": "application/json"
                    }
                }
            ).then(response => {
                return response
            }).catch(() => false);
        },
    },
    tag: {
        async getAll() {
            return await axios.get('/api/tag/getAll',
                {headers: {"Authorization": "Bearer " + JSON.parse(localStorage.getItem('jwt'))}}
            ).then(response => {
                return response.data;
            }).catch(() => false);
        },
        async get(tag) {
            return await axios.get('/api/tag/get?tag=' + tag,
                {headers: {"Authorization": "Bearer " + JSON.parse(localStorage.getItem('jwt'))}}
            ).then(response => {
                return response.data;
            }).catch(() => false);
        },
        async delete(text) {
            return await axios.post('/api/tag/delete', {'text': text},
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
        async create(text) {
            return await axios.post('/api/tag/create', {
                    'text': text,
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
        }
    },
    event: {
        async getAll() {
            return await axios.get('/api/event/getAll',
                {headers: {"Authorization": "Bearer " + JSON.parse(localStorage.getItem('jwt'))}}
            ).then(response => {
                return response.data;
            }).catch(() => false);
        },
        async getAllFromUser(username) {
            return await axios.get('/api/event/getAllFromUser?username=' + username,
                {headers: {"Authorization": "Bearer " + JSON.parse(localStorage.getItem('jwt'))}}
            ).then(response => {
                return response.data;
            }).catch(() => false);
        },
        async create(event) {
            console.log(event);
            return await axios.post('/api/event/create', {
                    'name': event.name,
                    'creatorUsername': event.creatorUsername,
                    'participants': JSON.stringify(event.participants),
                    'locations': JSON.stringify(event.locations),
                    'timeslots': JSON.stringify(event.timeslots),
                    'creatorIsPreferred': event.creatorIsPreferred,
                    'pollEndDate': event.pollEndDate
                },
                {headers: {"Authorization": "Bearer " + JSON.parse(localStorage.getItem('jwt'))}}
            ).then(response => {
                return response
            }).catch(() => false);
        },
        async delete(eventId) {
            return await axios.post('/api/event/delete', eventId,
                {
                    headers: {
                        "Authorization": "Bearer " + JSON.parse(localStorage.getItem('jwt')),
                        "Content-Type": "application/json"
                    }
                }
            ).then(response => {
                return response;
            }).catch(() => false);
        }
    },
    poll: {
        async get(eventId, username) {
            return await axios.get('/api/poll/get?eventId=' + eventId + '&username=' + username,
                {headers: {"Authorization": "Bearer " + JSON.parse(localStorage.getItem('jwt'))}}
            ).then(response => {
                console.log(response)
                return response.data;
            }).catch(() => false);
        },
        async edit(poll) {
            return await axios.post('/api/poll/edit', {
                    'pollId': poll.pollId,
                    'pollLocations': poll.pollLocations,
                    'pollTimeslots': poll.pollTimeslots,
                    'user': poll.user,
                    'event': poll.event,
                },
                {
                    headers: {
                        "Authorization": "Bearer " + JSON.parse(localStorage.getItem('jwt')),
                        "Content-Type": "application/json"
                    }
                }
            ).then(response => {
                return response
            }).catch(() => false);
        },
    },
    pollLocations: {
        async edit(pollLocation, poll) {
            return await axios.post('/api/pollLocations/edit', {
                    'pollId': poll.id,
                    'locationId': pollLocation.location.id,
                    'points': pollLocation.points,
                },

                {
                    headers: {
                        "Authorization": "Bearer " + JSON.parse(localStorage.getItem('jwt')),
                        "Content-Type": "application/json"
                    }
                }
            ).then(response => {
                return response
            }).catch(() => false);
        },
    },
    pollTimeslots: {
        async edit(pollTimeslot, poll) {
            return await axios.post('/api/pollTimeslots/edit', {
                    'pollId': poll.id,
                    'timeslotId': pollTimeslot.timeslot.id,
                    'points': pollTimeslot.points,
                },
                {
                    headers: {
                        "Authorization": "Bearer " + JSON.parse(localStorage.getItem('jwt')),
                        "Content-Type": "application/json"
                    }
                }
            ).then(response => {
                return response
            }).catch(() => false);
        }
    }

}