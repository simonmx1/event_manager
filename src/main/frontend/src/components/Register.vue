<template>
  <div class="text-center">
    <v-card class="elevation-12">
      <v-toolbar dark color="primary">
        <v-toolbar-title> {{ admin ? 'Create User' : 'Register' }}</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <user-form ref="form" :admin="admin" @validated="register"/>
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
        <v-btn color="blue darken-1" text @click="closeDialog()">{{ admin ? 'Cancel' : 'Close' }}</v-btn>
        <v-btn color="primary" text @click="tryRegister()"> {{ admin ? 'Create User' : 'Register' }}</v-btn>
      </v-card-actions>
    </v-card>
  </div>
</template>

<script>
import UserForm from "@/components/UserForm";

export default {
  name: 'Register',
  components: {UserForm},
  props: {
    admin: {type: Boolean, default: false}
  },
  data: () => ({
    response: '',
    wrongUsername: false,
    success: undefined,
  }),
  methods: {
    tryRegister() {
      this.$refs.form.validate()
    },
    register(event) {
      this.$api.user.register(event)
          .then(response => {
            this.success = response.status === 201;
            this.response = response.data
          })
    },
    closeDialog() {
      this.$refs.form.clear()
      this.$emit("close")
      this.response = ''
      this.success = undefined
      this.wrongUsername = false
    }
  }
}
</script>