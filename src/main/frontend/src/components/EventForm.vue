<template>
  <v-form
      ref="form"
      v-model="valid"
      lazy-validation>
    <v-text-field
        v-model="currentEvent.name"
        :rules="nameRules"
        prepend-icon="mdi-account-box"
        name="name"
        label="Name"
        type="text"
    ></v-text-field>
    <v-text-field
        v-model="currentEvent.participants"
        :disabled="editMode"
        :rules="passwordRules"
        prepend-icon="mdi-lock"
        name="password"
        label="Password"
        type="password"
    ></v-text-field>
  </v-form>
</template>

<script>
export default {
  name: "EventForm",
  props: {
    event: {
      type: Object, default: () => ({
        name: '',
        location: null,
        timestamp: null,
        participants: [],
        enabled: true,
      })
    },
  },
  data: () => ({
    valid: false,
    currentEvent: null
  }),
  mounted() {
    this.currentEvent = this.event;
  }
}
</script>

<style scoped>

</style>