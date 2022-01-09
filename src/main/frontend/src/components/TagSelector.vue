<template>
  <v-container fluid>
    <v-combobox
        v-model="model"
        :items="items"
        label="Combobox"
        multiple
        outlined
        dense
    >
      <template v-slot:selection="{ attrs, item, parent }">
        <v-chip
            v-bind="attrs"
            color="primary"
            label
            small
        >
          <span class="pr-2">
            {{ item.tag }}
          </span>
          <v-icon
              small
              @click="parent.selectItem(item.tag)"
          >
            $delete
          </v-icon>
        </v-chip>
      </template>
      <template v-slot:item="{ index, item }">
        <v-chip
            :key="item.tag"
            color="primary"
            dark
            label
            small
        >
          {{ item.tag }}
        </v-chip>
      </template>
    </v-combobox>
  </v-container>
</template>

<script>
import api from "@/utils/api";

export default {
  name: "TagSelector",
  data: () => ({
    activator: null,
    attach: null,
    items: [
      {header: 'Select a tag or create one'},
    ],
    nonce: 1,
    menu: false,
    model: [],
    x: 0,
    search: null,
    y: 0,
  }),

  watch: {
    /*model(val, prev) {
      if (val.length === prev.length) return

      this.model = val.map(v => {
        if (typeof v === 'string') {
          v = {
            tag: v
          }

          this.items.push(v)
          this.nonce++
        }
        return v
      })
    },*/
  },

  methods: {
    filter(item, queryText) {
      if (item.header) return false

      const hasValue = val => val != null ? val : ''

      const text = hasValue(item.tag)
      const query = hasValue(queryText)

      return text.toString()
          .toLowerCase()
          .indexOf(query.toString().toLowerCase()) > -1
    },
  },
  mounted() {
    api.tags.getAll().then(response => {
      console.log(response)
      this.items = response
    })
  }
}
</script>

<style scoped>

</style>