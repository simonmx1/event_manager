<template>
  <v-container>
    <div v-for="(day, dayIndex) in weekdays" :key="day.day">
      <v-checkbox :label="day.day" v-model="day.enabled"></v-checkbox>

      <div v-if="loadOpeningTimes && day.enabled">
        <v-row v-for="(time, timeIndex) in day.openingTimes" :key="timeIndex" ref="timeslots">
          <v-col class=".col-auto">
            <v-dialog
                width="800"
                persistent
                v-model="timepicker[dayIndex][timeIndex]"
            >
              <template v-slot:activator="{ on, attrs }">
                <v-text-field
                    prepend-icon="mdi-clock"
                    v-model="time.text"
                    v-bind="attrs"
                    v-on="on">
                </v-text-field>
              </template>
              <v-card width="100%" style="horiz-align: center">
                <v-toolbar color="primary">
                  <v-card-title>Opening Time on {{ day.day }}</v-card-title>
                </v-toolbar>
                <v-row class="ma-8">
                  <v-col cols="5"
                         style="margin-left: 25px">
                    <h2>Opening:</h2>
                    <v-time-picker
                        v-if="timepicker[dayIndex][timeIndex]"
                        format="24hr"
                        v-model="time.start"
                        :max="time.end"
                        :allowed-minutes="allowedStep"
                    ></v-time-picker>
                  </v-col>
                  <v-col cols="5"
                         style="margin-left: 65px">
                    <h2>Closing:</h2>
                    <v-time-picker
                        v-if="timepicker[dayIndex][timeIndex]"
                        format="24hr"
                        v-model="time.end"
                        :min="time.start"
                        :allowed-minutes="allowedStep"
                    ></v-time-picker>
                  </v-col>
                </v-row>
                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn color="primary" text @click="saveTime(dayIndex, timeIndex)">Save</v-btn>
                </v-card-actions>
              </v-card>
            </v-dialog>
          </v-col>
          <v-col v-if="timeIndex + 1 === day.openingTimes.length" cols="auto" style="margin-top: 10px">
            <v-btn icon @click="addOpeningTimesInput(dayIndex)">
              <v-icon>mdi-plus</v-icon>
            </v-btn>
          </v-col>
          <v-col v-if="timeIndex + 1 !== day.openingTimes.length" cols="auto" style="margin-top: 10px">
            <v-btn icon @click="removeOpeningTimesInput(dayIndex, timeIndex)">
              <v-icon>mdi-minus</v-icon>
            </v-btn>
          </v-col>
        </v-row>
      </div>
    </div>
  </v-container>
</template>

<script>
export default {
  name: "OpeningTimesSelector",
  props: {
    openingTimes: {type: Array, default: () => []}
  },
  data: () => ({
    timepicker: [],
    weekdays: [],
    loadOpeningTimes: true
  }),
  mounted() {
    this.resetTimes()
  },
  methods: {
    addOpeningTimesInput(dayIndex) {
      this.timepicker[dayIndex].push(false)
      this.weekdays[dayIndex].openingTimes.push({start: null, end: null, text: ''})
    },
    removeOpeningTimesInput(dayIndex, timeIndex) {
      this.timepicker[dayIndex].splice(timeIndex, 1)
      this.weekdays[dayIndex].openingTimes.splice(timeIndex, 1)
      this.forceRerender()
    },
    forceRerender() {
      this.loadOpeningTimes = false
      this.$nextTick(() => this.loadOpeningTimes = true)
    },
    saveTime(dayIndex, timeIndex) {
      let time = this.weekdays[dayIndex].openingTimes[timeIndex]
      if (time.start != null && time.end != null) {
        this.weekdays[dayIndex].openingTimes[timeIndex].text = time.start + ' - ' + time.end
      } else {
        this.weekdays[dayIndex].openingTimes[timeIndex].start = null;
        this.weekdays[dayIndex].openingTimes[timeIndex].end = null;
        this.weekdays[dayIndex].openingTimes[timeIndex].text = '';
      }
      this.timepicker[dayIndex][timeIndex] = false
      this.$forceUpdate()
    },
    allowedStep: m => m % 30 === 0,
    sendData() {
      let openingTimes = [];
      this.weekdays.forEach(day => {if (day.enabled) openingTimes.push(day)})
      this.$emit('confirm', JSON.parse(JSON.stringify(openingTimes)))
    },
    resetTimes() {
      this.weekdays = []
      this.timepicker = []
      this.$date.getWeek().forEach(day => {
        this.weekdays.push({day: day, enabled: false, openingTimes: [{start: null, end: null, text: ''}]})
        this.timepicker.push([false])
      })
      this.openingTimes.forEach(time => {
        time.start = this.$date.formatTimeWithoutMillis(time.start)
        time.end = this.$date.formatTimeWithoutMillis(time.end)
        if (this.weekdays[time.weekday].enabled) {
          this.weekdays[time.weekday].openingTimes.push({
            start: time.start,
            end: time.end,
            text: time.start + ' - ' + time.end
          })
        } else {
          this.weekdays[time.weekday].openingTimes[0].start = time.start
          this.weekdays[time.weekday].openingTimes[0].end = time.end
          this.weekdays[time.weekday].openingTimes[0].text = time.start + ' - ' + time.end

          this.weekdays[time.weekday].enabled = true
        }
      })
    },
    clear() {
      this.resetTimes()
    },
  }
}
</script>