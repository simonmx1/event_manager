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
        url: "/locations",
        roles: ['ADMIN', 'LOCATION_MANAGER']
    },
    {
        title: "Event Management",
        icon: "mdi-calendar",
        url: "/events",
        roles: ['ADMIN']
    }
]



export default {
    async getNavBarItems() {
        return await api.user.loggedIn().then(response => {
            return navBarItems.filter(item => item.roles.includes(response[1]))
        })
    }
}