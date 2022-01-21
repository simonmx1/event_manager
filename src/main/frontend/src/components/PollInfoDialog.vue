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
     <!-- <v-row>
        <v-col>
          <v-card color="#3d3d3d" class="pa-2, ma-2" style="height: 75px">
            <v-card-title>
              <div style="margin-right: 10px; margin-top: 5px">
                <template>
                  <v-icon>mdi-info</v-icon>
                </template>
              </div>
              <div style="margin-top: 5px">
                Test
              </div>
              <v-spacer></v-spacer>
              <v-icon style="margin-top: 5px" color="#aaa">mdi-reorder-horizontal</v-icon>
            </v-card-title>
          </v-card>
        </v-col>
        <v-col>
          <v-card color="#3d3d3d" class="pa-2, ma-2" style="height: 75px">
            <v-card-subtitle style="font-size: medium; color: white">
              <v-row>
                <v-col cols="2">
                  <div style="float: left">
                    From:
                  </div>
                  <br>
                  <div style="float: left">
                    To:
                  </div>
                </v-col>
                <v-col cols="7">
                  <div style="float: right">
                    Datum1
                  </div>
                  <br>
                  <div style="float: right">
                    Datum2
                  </div>
                </v-col>
                <v-col cols="1">
                  <v-btn icon style="height: 100%; width: 43px; padding: 5px ">
                    <v-icon color="red">mdi-close</v-icon>
                  </v-btn>
                </v-col>
                <v-col cols="1">
                  <v-icon color="#aaa" style="height: 100%; width: 50px">mdi-reorder-horizontal</v-icon>
                </v-col>
              </v-row>
            </v-card-subtitle>
          </v-card>
          <v-card color="#3d3d3d" class="pa-2, ma-2" style="height: 75px">
            <v-card-subtitle style="font-size: medium; color: white">
              <v-row>
                <v-col cols="2">
                  <div style="float: left">
                    From:
                  </div>
                  <br>
                  <div style="float: left">
                    To:
                  </div>
                </v-col>
                <v-col cols="7">
                  <div style="float: right">
                    Datum1
                  </div>
                  <br>
                  <div style="float: right">
                    Datum2
                  </div>
                </v-col>
                <v-col cols="1">
                  <v-btn icon style="height: 100%; width: 43px; padding: 5px ">
                    <v-icon color="red">mdi-close</v-icon>
                  </v-btn>
                </v-col>
                <v-col cols="1">
                  <v-icon color="#aaa" style="height: 100%; width: 50px">mdi-reorder-horizontal</v-icon>
                </v-col>
              </v-row>
            </v-card-subtitle>
          </v-card>
        </v-col>
      </v-row>-->
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
  name: "PollInfoDialog"
}
</script>

<style scoped>

</style>