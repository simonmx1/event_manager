<template>
  <v-dialog v-model="showDialog" max-width="500px">
    <template v-slot:activator="{ on, attrs }">
      <v-btn
        icon
        @click.stop.prevent="showDialog = true"
        v-bind="attrs"
        v-on="on"
      >
        <v-icon> mdi-information-variant </v-icon>
      </v-btn>
    </template>
    <v-card>
      <v-card-title style="width: 100%"
        >Are you sure you want to delete this Event?</v-card-title
      >
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn @click="showDialog=false">Ok</v-btn>
        <v-spacer></v-spacer>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: "LocationInfoDialog",
  props: {
    currentLocation: { type: Object, required: true },
  },
  data: () => ({
    showDialog: false
  }),
  methods: {

  },
  mounted() {
    console.log(this.currentLocation);
  },
};
</script>