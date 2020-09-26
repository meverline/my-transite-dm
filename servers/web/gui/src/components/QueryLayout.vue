<template>
  <form id="standardFilters" v-on:submit.prevent="onSubmit">
    <div>
      <md-radio v-model="queryType" value="circle">Circle</md-radio>
      <md-radio v-model="queryType" value="rectangle">Rectangle</md-radio>
      <md-radio v-model="queryType" value="agency">Agency</md-radio>
    </div>
    <div v-if="queryType === 'circle'">
      <md-field>
        <label>Distance:</label>
        <md-input v-model="distance" type="text" maxlength="4" required v-model="initial"/>
        <md-tooltip md-direction="right">Distance or radius of circle</md-tooltip>
      </md-field>
      <md-field>
        <label>Unit:</label>
        <md-select id="unit" required v-model="distanceUnits">
          <md-option value="Meter">Meter</md-option>
          <md-option value="Kilometer">Kilometer</md-option>
          <md-option value="Mile">Mile</md-option>
          <md-option value="Feet">Feet</md-option>
        </md-select>
        <md-tooltip md-direction="right">Distance units used</md-tooltip>
      </md-field>
      <md-field>
        <label>Center:</label>
        <md-input v-model="center" type="text" minlength="14" maxlength="14" required/>
        <md-tooltip md-direction="right">Center of circle</md-tooltip>
      </md-field>
    </div>
    <div v-else-if="queryType === 'rectangle'">
      <md-field>
        <label>Upper Left:</label>
        <input v-model="upperLeft" type="text" minlength="14" maxlength="14" required/>
        <md-tooltip md-direction="right">Upper Left corner of geographic box</md-tooltip>
      </md-field>
      <md-field>
        <label>Lower Right:</label>
        <input v-model="lowerRight" type="text" minlength="14" maxlength="14" required/>
        <md-tooltip md-direction="right">Lower Left corner of geographic box</md-tooltip>
      </md-field>
    </div>
    <div v-else-if="queryType === 'agency'">
      <md-content class="md-scrollbar">
        <select id="v-for-agencys" multiple size="5">
          <option v-for="value in agencys" v-bind:value="value">{{ value }}</option>
        </select>
      </md-content>
    </div>
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
export default {
  name: "QueryLayout",
  data: () => ({
    queryType: 'rectangle',
    metric: '',
    weekdays: [],
    startHour: '',
    endHour: '',
    agencys: ['Metro', 'ART', 'CUE', 'RideOn', 'FallsChurch', 'FairfaxCity'],
    upperLeft: '',
    lowerRight: '',
    center: '',
    distance: 10,
    distanceUnits: 'Meter',
    showQueryData: false,
  })
}
</script>

<style scoped>

</style>