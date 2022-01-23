<template>
  <v-dialog v-model="showDialog" max-width="700px">
    <template v-slot:activator="{ on, attrs }">
      <v-btn
          icon
          @click.stop.prevent="showDialog = true"
          v-bind="attrs"
          v-on="on"
      >
        <v-icon>mdi-help-circle</v-icon>
      </v-btn>
    </template>
    <v-toolbar color="green">
      <v-card-title>Info</v-card-title>
    </v-toolbar>
    <v-card style="overflow: hidden" class="pa-4">
      <v-row>
        <v-col cols="1">
          <v-icon color="#aaa" style="height: 100%; width: 50px; align: center">mdi-reorder-horizontal</v-icon>
        </v-col>
        <v-col cols="11">
          <v-card-text style="float: left;">The blocks can be placed in the desired order with <b>Drag and Drop</b>.</v-card-text>
        </v-col>
      </v-row>
      <v-row>
        <v-col cols="1">
            <v-chip style="margin-block-start: 10px; margin-left: 10px">1</v-chip>
        </v-col>
        <v-col cols="11">
          <v-card-text style="float: left;">The preference (1 - greatest preference)</v-card-text>
        </v-col>
      </v-row>
      <v-row>
        <v-col cols="1">
          <v-chip color="red" style="margin-block-start: 10px; margin-left: 10px">X</v-chip>
        </v-col>
        <v-col cols="11">
          <v-card-text style="float: left;">Disabled timeslot. This means you can not participate in this timeslot.</v-card-text>
        </v-col>
      </v-row>
      <v-row>
        <v-col cols="1">
          <v-icon color="red"  style="width: 50px; height: 100%">mdi-close</v-icon>
        </v-col>
        <v-col cols="11">
          <v-card-text style="float: left;">With this symbol the timeslot can be deactivated.</v-card-text>
        </v-col>
      </v-row>
      <v-row>
        <v-col cols="1">
          <v-icon color="green" style="width: 50px; height: 100%">mdi-check</v-icon>
        </v-col>
        <v-col cols="11">
          <v-card-text style="float: left;">With this symbol the timeslot can be activated.</v-card-text>
        </v-col>
      </v-row>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: "PollInfoDialog",
  data: () => ({
    showDialog: false
  })
}
</script>

<style scoped>

</style>