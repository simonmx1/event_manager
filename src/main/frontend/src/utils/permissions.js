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
            return navBarItems.filter(item => item.roles.includes(response[1]))
        })
    }
}