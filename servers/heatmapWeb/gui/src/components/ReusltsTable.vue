<template>
  <div>
    <md-tab id="stopTable" md-label="Stop Table">
      <md-table id="v-for-stops">
        <md-table-row>
          <md-table-thead>Agency</md-table-thead>
          <md-table-thead>Name</md-table-thead>
          <md-table-thead>Type</md-table-thead>
          <md-table-thead>Code</md-table-thead>
          <md-table-thead>Wheelchair Boarding</md-table-thead>
          <md-table-thead>Description</md-table-thead>
        </md-table-row>
        <md-table-row v-for="item in stops">
          <md-table-cell>{{ item.agency_name.agency_name }}</md-table-cell>
          <md-table-cell>{{ item.name }}</md-table-cell>
          <md-table-cell>{{ item.location_type }}</md-table-cell>
          <md-table-cell>{{ item.code }}</md-table-cell>
          <md-table-cell>{{ item.wheelchair_boarding }}</md-table-cell>
          <md-table-cell>{{ item.desc }}</md-table-cell>
        </md-table-row>
      </md-table>
    </md-tab>
    <md-tab id="routeTable" md-label="Route Table">
      <md-table id="v-for-routes">
        <md-table-row>
          <md-table-thead>Agency</md-table-thead>
          <md-table-thead>Type</md-table-thead>
          <md-table-thead>Short Name</md-table-thead>
          <md-table-thead>Long Name</md-table-thead>
          <md-table-thead>Description</md-table-thead>
        </md-table-row>
        <md-table-row v-for="item in routes">
          <md-table-cell>{{ item.agency_name.agency_name }}</md-table-cell>
          <md-table-cell>{{ item.route_type }}</md-table-cell>
          <md-table-cell>{{ item.route_short_name }}</md-table-cell>
          <md-table-cell>{{ item.route_long_name }}</md-table-cell>
          <md-table-cell>{{ item.route_desc }}</md-table-cell>
        </md-table-row>
      </md-table>
    </md-tab>
  </div>
</template>

<script>
export default {
  name: "ResultsTable",
  data: () => ({
    stops: [],
    routes: []
  })
}
</script>

<style scoped>

</style>