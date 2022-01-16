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
      <v-card-title style="width: 100%">
        {{currentLocation.name}}
      </v-card-title>
      <v-card-text>
        <v-simple-table>
          <tbody>
          <tr>
            <th class="text-left" style="font-size: medium">
              Menu
            </th>
            <th class="text-right">
              <a :href="'//' + currentLocation.menu" target="_blank">
                {{currentLocation.menu}}
              </a>
            </th>
          </tr>
          <tr>
            <th class="text-left" style="font-size: medium">
              Location
            </th>
            <th class="text-right">
              <a :href="'//' + currentLocation.location" target="_blank" style="text-decoration: none">
                <v-icon>
                  mdi-map-search
                </v-icon>
              </a>
            </th>
          </tr>
          <tr>
            <th class="text-left" style="font-size: medium">
              Opening Times
            </th>
            <!--th>
              <v-menu
                  transition="slide-y-transition"
                  bottom
                  offset-y
              >
                <template v-if="currentLocation.op.length > 0" v-slot:activator="{ on, attrs }">
                  <v-btn
                      dark
                      v-bind="attrs"
                      v-on="on"
                  >
                    ...
                  </v-btn>
                </template>
                <v-list>
                  <v-list-item
                      v-for="(openingTime,id) in item.openingTimes"
                      :key="id"
                  >
                    <v-list-item-title>
                      {{ formatWeekday(openingTime.weekday) }} <br>
                      {{ formatTime(openingTime.start) }} - {{ formatTime(openingTime.end) }}
                    </v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-menu>
            </th-->
          </tr>
          </tbody>
        </v-simple-table>
      </v-card-text>
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
    formatWeekday(weekday) {
      switch (weekday) {
        case 0:
          return 'Monday'
        case 1:
          return 'Tuesday'
        case 2:
          return 'Wednesday'
        case 3:
          return 'Thursday'
        case 4:
          return 'Friday'
        case 5:
          return 'Saturday'
        case 6:
          return 'Sunday'
      }
    },
    formatTime(time) {
      return time.substring(0, 5)
    },
    sortByWeekday(list) {
      return list.sort((a, b) => (a.weekday !== b.weekday) ? a.weekday - b.weekday : (a.start > b.start ? 1 : -1))
    },
  },
  mounted() {
    console.log(this.currentLocation);
  },
};
</script>