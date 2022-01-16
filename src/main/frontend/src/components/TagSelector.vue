<template>
  <v-container fluid>
    <v-combobox
        v-model="model"
        :items="items"
        label="Tags"
        :search-input.sync="search"
        multiple
        outlined
        dense
    >
      <template v-slot:selection="{ attrs, item, parent }">
        <v-chip
            v-bind="attrs"
            color="#437505"
            small
        >
          <span class="pr-2">
            {{ item.text }}
          </span>
          <v-icon
              small
              @click="parent.selectItem(item)"
          >
            $delete
          </v-icon>
        </v-chip>
      </template>
      <template v-slot:item="{ index, item }">
        <v-chip
            :key="item.text"
            color="#437505"
            dark
            small
        >
          {{ item.text }}
        </v-chip>
        <v-spacer></v-spacer>
        <v-btn
            icon
            @click.stop.prevent="deleteItem(item)"
        >
          <v-icon
              small>
            mdi-delete
          </v-icon>
        </v-btn>
      </template>
    </v-combobox>
  </v-container>
</template>

<script>
import api from "@/utils/api";

export default {
  name: "TagSelector",
  props: {
    confirm: {type: Boolean, required: true},
    tags: {type: Array, default: () => []}
  },
  data: () => ({
    activator: null,
    attach: null,
    items: [
      {header: 'Select a tag'},
    ],
    nonce: 1,
    menu: false,
    model: [],
    x: 0,
    search: null,
    y: 0,
  }),

  watch: {
    model(val, prev) {
      if (val.length === prev.length) return

      this.model = val.map(v => {
        if (typeof v === 'string') {
          v = {
            text: v
          }

          this.createItem(v)
          this.items.push(v)
          this.nonce++
        }
        return v
      })
    },
    confirm(val) {
      if (val) {
        this.$emit("confirmed", this.model)
      }
    }
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
    createItem(item) {
      api.tag.create(item.text).then(() => this.getTags())
    },
    deleteItem(item) {
      this.model.splice(this.model.indexOf(item), 1)
      api.tag.delete(item.text).then(() => this.getTags())
    },
    getTags() {
      api.tag.getAll().then(response => {
        this.items = response
      })
    },
    clear() {
      this.model = []
    }
  },
  mounted() {
    this.getTags()
    this.model = this.tags
  }
}
</script>

<style scoped>

</style>