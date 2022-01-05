<template>
  <div class="text-center">
    <v-card class="elevation-12">
      <v-toolbar dark color="primary">
        <v-toolbar-title>Register</v-toolbar-title>
      </v-toolbar>

      <v-card-text>
        <v-form ref="form" v-model="valid" lazy-validation>
          <v-text-field
              v-model="username"
              :rules="usernameRules"
              prepend-icon="mdi-account-box"
              name="username"
              label="Username"
              type="text"
          ></v-text-field>
          <v-text-field
              v-model="password"
              :rules="passwordRules"
              prepend-icon="mdi-lock"
              name="password"
              label="Password"
              type="password"
          ></v-text-field>
          <v-divider></v-divider>
          <v-text-field
              v-model="firstName"
              :rules="firstNameRules"
              prepend-icon="mdi-form-textbox"
              name="firstName"
              label="First Name"
              type="firstName"
              required
          ></v-text-field>
          <v-text-field
              v-model="lastName"
              :rules="lastNameRules"
              prepend-icon="mdi-form-textbox"
              name="lastName"
              label="Last Name"
              type="lastName"
          ></v-text-field>
          <v-text-field
              v-model="email"
              :rules="emailRules"
              prepend-icon="mdi-mail"
              name="email"
              label="Email"
              type="email"
          ></v-text-field>
          <v-select
              v-model="role"
              :items="roles"
              label="Select Role"
          ></v-select>
          <v-checkbox
              v-model="enabled"
              label="enable Account"
          ></v-checkbox>
        </v-form>
        <v-alert
            v-if="typeof success !== 'undefined'"
            dense
            outlined
            :type="success ? 'success' : 'error'"
        >
          <strong>{{ response }}</strong>
        </v-alert>
      </v-card-text>

      <v-divider></v-divider>

      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue darken-1" text @click="closeDialog()">Close</v-btn>
        <v-btn color="primary" text @click="register()">Register</v-btn>
      </v-card-actions>
    </v-card>
  </div>
</template>

<script>
import api from "@/utils/api";

export default {
  name: 'Register',
  data: () => ({
    response: '',
    valid: true,
    wrongUsername: false,
    success: undefined,
    username: '',
    password: '',
    firstName: '',
    lastName: '',
    email: '',
    role: 'USER',
    roles: [
      'ADMIN',
      'LOCATION_MANAGER',
      'USER'
    ],
    enabled: true,
    usernameRules: [
      v => !!v || 'Username is required'
    ],
    passwordRules: [
      v => !!v || 'Password is required',
    ],
    firstNameRules: [
      v => !!v || 'First Name is required',
    ],
    lastNameRules: [
      v => !!v || 'Last Name is required',
    ],
    emailRules: [
      v => !!v || 'E-mail is required',
      v => /.+@.+\..+/.test(v) || 'E-mail must be valid',
    ]
  }),
  methods: {
    register() {
      if (this.$refs.form.validate()) {
        api.register(this.username, this.password, this.firstName, this.lastName, this.email, this.enabled, this.role)
            .then(response => {this.success = response.status === 201; this.response = response.data})
      }
    },
    closeDialog() {
      this.dialog = false
      this.username = ''
      this.password = ''
      this.firstName = ''
      this.lastName = ''
      this.email = ''
      this.role = 'USER'
      this.enabled = true
      this.success = false
      this.wrongUsername = false
      this.$refs.form.resetValidation()
      this.$emit("close")
    }
  }
}
</script>