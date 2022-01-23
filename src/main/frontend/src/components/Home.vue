<template>
  <v-main>
    <event-overview/>
  </v-main>
</template>

<script>
import EventOverview from "./EventOverview";

export default {
  name: "Home",
  components: {EventOverview},
}
</script>