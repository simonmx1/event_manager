const weekdays = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']
const monthsShort = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]
const weekdayShort = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"]

export default {
    dateFromTimestamp(timestamp) {
        return new Date(timestamp).toISOString().slice(0, 10);
    },
    timeFromTimestamp(timestamp) {
        return new Date(timestamp).toTimeString().slice(0, 8)
    },
    formatTimeslot(timeslot) {
        return this.formatTimestamp(timeslot.start) + " - " + this.formatTimestamp(timeslot.end)
    },
    formatTimestamp(timestamp) {
        //2023-01-02T19:15:00.000+00:00
        const date = this.dateFromTimestamp(timestamp)
        const time = this.timeFromTimestamp(timestamp)
        return time + " " + date
    },
    timeDifferenceToday(timestamp) {
        return new Date(timestamp).getTime() - new Date().getTime()
    },
    calculatePercent(timestamp) {
        let dif = this.timeDifferenceToday(timestamp)
        let p = 100 - dif / 1000 / 3600 / 24 * 100
        return dif < 86400000 ? p : dif < 0 ? 100 : 0
    },
    calculateColor(percent) {
        if (percent >= 95.83) {//1 hour
            return 'red'
        } else if (percent >= 91.66) {//2 hours
            return 'orange'
        } else if (percent >= 87.5) {//3 hours
            return '#e6c000'
        } else {
            return 'green'
        }
    },
    formatTimeWithoutMillis(time) {
        return time.substring(0, 5)
    },
    sortByWeekday(list) {
        return list.sort((a, b) => (a.weekday !== b.weekday) ? a.weekday - b.weekday : (a.start > b.start ? 1 : -1))
    },
    formatWeekday(weekday) {
        return weekdays[weekday]
    },
    getWeek() {
        return weekdays
    },
    formatTimestampPoll(timestamp) {
        const time = new Date(timestamp).toTimeString().slice(0, 8)
        return {"date": this.formatDatePoll(new Date(timestamp)), "time": time}
    },
    formatDatePoll(date) {
        const weekday = weekdayShort[date.getDay()]
        const day = date.getDate();
        const month = monthsShort[date.getMonth()]
        const year = date.getFullYear();
        return weekday + ", " + day + "." + month + "." + year;
    }
}