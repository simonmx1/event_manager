<template>
  <div class="text-center">
    <v-card class="elevation-12">
      <v-toolbar dark color="primary">
        <v-toolbar-title>Edit User</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <user-form
            v-if="user != null"
            ref="form"
            :admin="admin"
            :user="user"
            :edit="true"
            @validated="edit"/>
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
        <v-btn color="primary" text @click="tryEdit()">Save User</v-btn>
      </v-card-actions>
    </v-card>
  </div>
</template>

<script>
import UserForm from "@/components/UserForm";

export default {
  name: 'EditUser',
  components: {UserForm},
  props: {
    user: {type: Object, required: true},
    admin: {type: Boolean, default: true},
  },
  data: () => ({
    response: '',
    wrongUsername: false,
    success: undefined,
  }),
  methods: {
    tryEdit() {
      this.$refs.form.validate()
    },
    edit(event) {
      this.$api.user.edit(event)
          .then(response => {
            this.success = response.status === 200;
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