import api from "@/utils/api";

let navBarItems = [
    {
        title: "User Management",
        icon: "mdi-account",
        url: "/users",
        roles: ['ADMIN']
    },
    {
        title: "Location Management",
        icon: "mdi-earth",
        url: "/location",
        roles: ['ADMIN', 'LOCATION_MANAGER']
    }
]



export default {
    async getNavBarItems() {
        return await api.loggedIn().then(response => {
            let role = response[1]
            console.log(navBarItems.filter(item => item.roles.includes(role)))
            return navBarItems.filter(item => item.roles.includes(role))
        })
    }
}