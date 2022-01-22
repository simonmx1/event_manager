<template>
  <v-card>
    <v-toolbar dark color="primary">
      <v-toolbar-title>Change Password</v-toolbar-title>
    </v-toolbar>
    <v-card-text>
      <v-form>
        <v-text-field
            label="Enter a new Password"
            :rules="passwordRules"
            type="password"
            v-model="password"
        ></v-text-field>
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
    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn color="blue darken-1" text @click="closeDialog()">Close</v-btn>
      <v-btn color="primary" text @click="changePassword()">Save Password</v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import api from "@/utils/api";

export default {
  name: "ChangePassword",
  props: {
    username: {type: String, required: true}
  },
  data: () => ({
    password: null,
    passwordRules: [
      v => !!v || 'Password is required'
    ],
    success: undefined,
    response: null
  }),
  methods: {
    closeDialog() {
      this.$emit('close')
    },
    changePassword() {
      if (this.password != null && this.password.length > 0) {
        api.user.changePassword(this.username, this.password).then(response => {
          this.success = response.status === 200
          this.response = response.data
        })
      }
    }
  }
}
</script>

<style scoped>

</style>