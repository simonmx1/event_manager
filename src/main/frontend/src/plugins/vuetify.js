import Vue from 'vue';
import Vuetify from 'vuetify/lib/framework';
import colors from "vuetify/es5/util/colors";

Vue.use(Vuetify);

export default new Vuetify({
    theme: {
        dark: true,
        themes: {
            dark: {
                primary: colors.blue.darken1,
                background: colors.indigo.base,
                info: colors.teal.lighten1,
                secondary: '#b0bec5',
                accent: '#8c9eff',
                error: '#b71c1c',
            },
        },
    },
});
