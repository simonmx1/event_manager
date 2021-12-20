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
              <v-text-field
                  v-model="username"
                  prepend-icon="mdi-account-box"
                  name="login"
                  label="Login"
                  type="text"
              ></v-text-field>
              <v-text-field
                  v-model="password"
                  id="mdi-password"
                  prepend-icon="mdi-lock"
                  name="password"
                  label="Password"
                  type="password"
              ></v-text-field>
            </v-form>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="primary" @click="login">Login</v-btn>
          </v-card-actions>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import axios from "axios";

export default {
  name: 'Login',
  data: () => ({
    users: null,
    username: '',
    password: '',
  }),
  mounted() {
    fetch("/api/userlist/getUsers").then(response => response.text()).then(data => this.users = JSON.parse(data))
  },
  methods: {
    login() {
      let form_data;
      form_data = new FormData()
      form_data.append('username', this.username)
      form_data.append('password', this.password)
      axios.post('http://localhost:8080/login',
        form_data
      ,{
        headers: { "Content-Type": "application/json" },
      }).then(() => {
        // this.$router.push("/home")
      }).catch(err => {
        console.log(err.response);
      });
    }
  }
}
</script>
