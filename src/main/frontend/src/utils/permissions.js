import api from "@/utils/api";

let navBarItems = [
    {
        title: "Users",
        icon: "mdi-account",
        url: "/users",
        roles: ['ADMIN']
    },
    {
        title: "Locations",
        icon: "mdi-earth",
        url: "/locations",
        roles: ['ADMIN', 'LOCATION_MANAGER']
    },
    {
        title: "Events",
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