<template>
  <v-dialog v-model="showDialog" max-width="700px">
    <template v-slot:activator="{ on, attrs }">
      <v-btn
          icon
          @click.stop.prevent="showDialog = true"
          v-bind="attrs"
          v-on="on"
      >
        <v-icon> mdi-information-variant</v-icon>
      </v-btn>
    </template>
    <v-card>
      <v-card-title style="width: 100%">
        {{ currentLocation.name }}
        <v-spacer></v-spacer>
        <v-btn @click="showDialog=false" icon>
          <v-icon dark>
            mdi-close
          </v-icon>
        </v-btn>
      </v-card-title>
      <v-row>
        <v-col cols="6">
          <v-list style="margin-left: 30px">
            <v-list-item-title>
              Menu
            </v-list-item-title>
            <v-list-item>
              <a :href="'//' + currentLocation.menu" target="_blank">
                {{ currentLocation.menu }}
              </a>
            </v-list-item>
            <v-list-item-title>
              Geo Location
            </v-list-item-title>
            <v-list-item>
              <a :href="'//' + currentLocation.location" target="_blank" style="text-decoration: none">
                <v-icon>
                  mdi-map-search
                </v-icon>
              </a>
            </v-list-item>
            <v-item-group>
              <v-item
                  v-for="(tag,id) in currentLocation.tags"
                  :key="id"
              >
                <v-chip
                    style="margin: 5px"
                    :color="'#437505'">
                  {{ tag.text }}
                </v-chip>
              </v-item>
            </v-item-group>
          </v-list>
        </v-col>
        <v-divider
            vertical
        ></v-divider>
        <v-col cols="6">
          <th class="text-center" style="font-size: medium">
            Opening Times
          </th>
          <v-list>
            <v-list-item
                v-for="(openingTime,id) in currentLocation.openingTimes"
                :key="id"
            >
              <v-list-item-title>
                {{ formatWeekday(openingTime.weekday) }} <br>
                {{ formatTime(openingTime.start) }} - {{ formatTime(openingTime.end) }}
              </v-list-item-title>
            </v-list-item>
          </v-list>
        </v-col>
      </v-row>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: "LocationInfoDialog",
  props: {
    currentLocation: {type: Object, required: true},
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