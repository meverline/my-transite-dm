<template>
  <form id="standardFilters" v-on:submit.prevent="onSubmit">
    <md-tabs>
      <md-tab id="circle" md-label="Circle" >
        <Circle></Circle>
      </md-tab>
      <md-tab id="rectangle" md-label="Rectangle" >
        <Rectangle></Rectangle>
      </md-tab>
      <md-tab id="agency" md-label="Agency" >
        <Agency></Agency>
      </md-tab>
    </md-tabs>
    <md-field>
      <label>Generate:</label>
      <md-select v-model="metric">
        <md-option value="KDE">Heatmap</md-option>
        <md-option value="STING">Clusters</md-option>
      </md-select>
      <md-tooltip md-direction="right">Either KDE generated heatmap or Statiscal information grid clusters</md-tooltip>
    </md-field>
    <md-field>
      <label>Metric:</label>
      <md-select v-model="metric">
        <md-option value="ServiceFrequnceAtStop">Service Frequence</md-option>
        <md-option value="TransitStopSpatialSample">Stops</md-option>
        <md-option value="ServiceDateSample">Service Date</md-option>
      </md-select>
      <md-tooltip md-direction="right">The metric used to assign value to location in grid</md-tooltip>
    </md-field>
    <md-field>
      <label>Days:</label>
      <md-select v-model="weekdays" multiple>
        <md-option value="Sunday">Sunday</md-option>
        <md-option value="Monday">Monday</md-option>
        <md-option value="Tuesday">Tuesday</md-option>
        <md-option value="Wednesday">Wednesday</md-option>
        <md-option value="Thursday">Thursday</md-option>
        <md-option value="Friday">Friday</md-option>
        <md-option value="Saturday">Saturday</md-option>
      </md-select>
      <md-tooltip md-direction="right">Days of the week of service default is all</md-tooltip>
    </md-field>
    <md-field>
      <label>Start Hour:</label>
      <md-input v-model="startHour"></md-input>
      <md-tooltip md-direction="right">Start of service time window</md-tooltip>
    </md-field>
    <md-field>
      <label>End Hour:</label>
      <md-input v-model="endHour"></md-input>
      <md-tooltip md-direction="right">End of service time window</md-tooltip>
    </md-field>
    <md-field>
      <md-checkbox v-model="showQueryData">Show Data</md-checkbox>
      <md-tooltip md-direction="right">Should query data used in grid be returned</md-tooltip>
    </md-field>
    <md-button type="submit" class="md-primary">Submit</md-button>
  </form>
</template>

<script>

import Circle from "@/components/queryforms/Circle";
import Rectangle from "@/components/queryforms/Rectangle";
import Agency from "@/components/queryforms/Agency";

export default {
  name: "QueryLayout",
  components: {Agency, Rectangle, Circle},
  data: () => ({
    metric: '',
    weekdays: [],
    startHour: '',
    endHour: '',
    query: {}
  })
}
</script>

<style scoped>

</style>