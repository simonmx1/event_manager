<template>
  <v-container>
    <v-layout align-center justify-center>
      <v-flex xs12 sm8 md4>
        <v-card class="elevation-12">
          <v-toolbar dark color="primary">
            <v-toolbar-title>Login form</v-toolbar-title>
          </v-toolbar>
          <v-card-text>
            <v-form>
              <v-text-field @keyup.enter="login()"
                  v-model="username"
                  :rules="usernameRules"
                  prepend-icon="mdi-account-box"
                  name="login"
                  label="Login"
                  type="text"
              ></v-text-field>
              <v-text-field @keyup.enter="login()"
                  v-model="password"
                  :rules="passwordRules"
                  prepend-icon="mdi-lock"
                  name="password"
                  label="Password"
                  type="password"
              ></v-text-field>
            </v-form>
            <v-alert
                v-if="wrongCredentials"
                dense
                outlined
                type="error"
            >
              <strong>Incorrect username or password</strong>
            </v-alert>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="primary" @click="login()">Login</v-btn>
          </v-card-actions>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import api from "@/utils/api";

export default {
  name: 'Login',
  data: () => ({
    users: null,
    username: '',
    password: '',
    wrongCredentials: false,
    usernameRules: [
      v => !!v || 'Username is required'
    ],
    passwordRules: [
      v => !!v || 'Password is required',
    ],
  }),
  methods: {
    login() {
      api.login(this.username, this.password).then(response => {response ? this.$router.push("/home") : this.wrongCredentials = true})
    },
  }
}
</script>
