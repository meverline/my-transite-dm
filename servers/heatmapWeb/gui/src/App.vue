<link rel="stylesheet" href="//fonts.googleapis.com/css?family=Roboto:400,500,700,400italic|Material+Icons">
<script src="https://cdn.jsdelivr.net/npm/vue@2.6.12"></script>

<div id="app">
  <md-toolbar class="md-dense">
    <md-menu-item>Settings</md-menu-item>
    <md-menu-item>Notifications</md-menu-item>
    <md-menu-item>About</md-menu-item>
  </md-toolbar>
  <vue-splitter-v initial-size="20%">
    <template slot="left">
      <p>Query</p>
    </template>
    <template slot="'right">
      <vue-splitter-h>
        <template slot="top" ><p>Map</p></template>
        <template slot="bottom">
          <p>Table</p>
        </template>
      </vue-splitter-h>
    </template>
  </vue-splitter-v>
</div>

<script>

import 'vue-material/dist/vue-material.min.css'
import 'vue-material/dist/theme/default.css'
import QueryLayout from '@/components/QueryLayout.vue'
import ReusltsTable from "@/components/ReusltsTable";
import VueSplitter, {VueSplitterH, VueSplitterV} from 'vue-splitter-pane'

export default {
  name: 'App',
  components: {
    QueryLayout,
    ReusltsTable,
    VueSplitterV,
    VueSplitterH
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
