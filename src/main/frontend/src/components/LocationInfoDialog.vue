<template>
  <v-dialog v-model="showDialog" max-width="800px">
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
    <v-card style="overflow: hidden"  class="pa-4">
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
        <v-col cols="12"
               sm="6"
               md="8">
          <v-list style="margin-left: 30px">
            <v-list-item-title>
              <b>Menu</b>
            </v-list-item-title>
            <v-list-item>
              <a :href="'//' + currentLocation.menu" target="_blank">
                {{ currentLocation.menu }}
              </a>
            </v-list-item>
            <v-list-item-title style="margin-top: 10px">
              <b>Geo Location</b>
            </v-list-item-title>
            <v-list-item>
              <a :href="'//' + currentLocation.location" target="_blank" style="text-decoration: none">
                <v-icon>
                  mdi-map-search
                </v-icon>
              </a>
            </v-list-item>
            <v-item-group>
              <v-list-item-title style="margin-top: 10px">
                <b>Tags</b>
              </v-list-item-title>
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
              <v-list-item-title style="margin-top: 10px" v-if="currentLocation.description">
                <b>Description</b>
              </v-list-item-title>
              <v-list-item v-if="currentLocation.description">
                {{currentLocation.description}}
              </v-list-item>
            </v-item-group>
          </v-list>
        </v-col>
        <v-divider
            vertical
        ></v-divider>
        <v-col cols="6"
               md="4">
          <th class="text-center" style="font-size: medium">
            Opening Times
          </th>
          <v-list>
            <v-list-item
                v-for="(openingTime,id) in currentLocation.openingTimes"
                :key="id"
            >
              <v-list-item-title>
                {{ $date.formatWeekday(openingTime.weekday) }} <br>
                {{ $date.formatTimeWithoutMillis(openingTime.start) }} - {{ $date.formatTimeWithoutMillis(openingTime.end) }}
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
};
</script>