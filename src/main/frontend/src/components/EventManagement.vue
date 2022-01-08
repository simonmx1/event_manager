<template>
  <v-main>
    <v-btn @click="createDialog = true">Create Event</v-btn>
    <v-dialog v-model="createDialog">
      <event-form>

      </event-form>
    </v-dialog>
  </v-main>
</template>

<script>
import EventForm from "@/components/EventForm";
export default {
  name: "EventManagement",
  components: {EventForm},
  data: () => ({
    createDialog: false
  })
}
</script>

<style scoped>

</style>