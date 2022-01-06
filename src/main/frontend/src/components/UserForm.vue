<template>
  <v-form ref="form" v-model="valid" lazy-validation>
    <v-text-field
        v-model="user.username"
        :rules="usernameRules"
        prepend-icon="mdi-account-box"
        name="username"
        label="Username"
        type="text"
    ></v-text-field>
    <v-text-field
        v-model="user.password"
        :rules="passwordRules"
        prepend-icon="mdi-lock"
        name="password"
        label="Password"
        type="password"
    ></v-text-field>
    <v-divider></v-divider>
    <v-text-field
        v-model="user.firstName"
        :rules="firstNameRules"
        prepend-icon="mdi-form-textbox"
        name="firstName"
        label="First Name"
        type="firstName"
        required
    ></v-text-field>
    <v-text-field
        v-model="user.lastName"
        :rules="lastNameRules"
        prepend-icon="mdi-form-textbox"
        name="lastName"
        label="Last Name"
        type="lastName"
    ></v-text-field>
    <v-text-field
        v-model="user.email"
        :rules="emailRules"
        prepend-icon="mdi-mail"
        name="email"
        label="Email"
        type="email"
    ></v-text-field>
    <v-select
        v-if="admin"
        v-model="user.role"
        :items="roles"
        label="Select Role"
    ></v-select>
    <v-checkbox
        v-if="admin"
        v-model="user.enabled"
        label="Enable Account"
    ></v-checkbox>
  </v-form>
</template>

<script>
export default {
  name: "UserForm",
  props: {
    admin: {type: Boolean, default: false}
  },
  data: () => ({
    valid: true,
    user: {
      username: '',
      password: '',
      firstName: '',
      lastName: '',
      email: '',
      role: 'USER',
      enabled: true,
    },
    roles: ['USER'],
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
    ],
    roleRules: [
      v => !!v || 'Role is required'
    ],
  }),
  methods: {
    validate() {
      if (this.$refs.form.validate()) {
        this.$emit('validated', this.user)
      }
    },
    clear() {
      this.$refs.form.resetValidation()
      this.user = {
        username: '',
        password: '',
        firstName: '',
        lastName: '',
        email: '',
        role: 'USER',
        enabled: true,
      }
    }
  },
  mounted() {
    if (this.admin) {
      this.roles = [
        'ADMIN',
        'LOCATION_MANAGER',
        'USER'
      ]
    }
  }
}
</script>

<style scoped>

</style>