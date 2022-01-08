<template>
  <v-form
      ref="form"
      v-model="valid"
      lazy-validation
      v-if="currentUser != null">
    <v-text-field
        v-model="currentUser.username"
        :disabled="editMode || accountSettings"
        :rules="usernameRules"
        prepend-icon="mdi-account-box"
        name="username"
        label="Username"
        type="text"
    ></v-text-field>
    <v-text-field
        v-model="currentUser.password"
        :disabled="editMode || accountSettings"
        :rules="passwordRules"
        prepend-icon="mdi-lock"
        name="password"
        label="Password"
        type="password"
    ></v-text-field>
    <v-divider></v-divider>
    <v-text-field
        v-model="currentUser.firstName"
        :disabled="accountSettings"
        :rules="firstNameRules"
        prepend-icon="mdi-form-textbox"
        name="firstName"
        label="First Name"
        type="firstName"
        required
    ></v-text-field>
    <v-text-field
        v-model="currentUser.lastName"
        :disabled="accountSettings"
        :rules="lastNameRules"
        prepend-icon="mdi-form-textbox"
        name="lastName"
        label="Last Name"
        type="lastName"
    ></v-text-field>
    <v-text-field
        v-model="currentUser.email"
        :disabled="accountSettings"
        :rules="emailRules"
        prepend-icon="mdi-mail"
        name="email"
        label="Email"
        type="email"
    ></v-text-field>
    <v-select
        v-if="admin"
        v-model="currentUser.role"
        :items="roles"
        label="Select Role"
    ></v-select>
    <v-checkbox
        v-if="admin"
        v-model="currentUser.enabled"
        label="Enable Account"
    ></v-checkbox>
  </v-form>
</template>

<script>
export default {
  name: "UserForm",
  props: {
    user: {
      type: Object, default: () => ({
        username: '',
        password: '',
        firstName: '',
        lastName: '',
        email: '',
        role: 'USER',
        enabled: true,
      })
    },
    edit: {type: Boolean, default: false},
    admin: {type: Boolean, default: false},
    accountSettings: {type: Boolean, default: false},
  },
  data: () => ({
    editMode: false,
    currentUser: null,
    valid: true,
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
        this.$emit('validated', this.currentUser)
      }
    },
    clear() {
      this.$refs.form.resetValidation()
      this.currentUser = JSON.parse(JSON.stringify(this.user));
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
    this.currentUser = JSON.parse(JSON.stringify(this.user));
    this.editMode = this.edit
  },
  watch: {
    user:function() {
      this.currentUser = this.user
    }
  }
}
</script>

<style scoped>

</style>