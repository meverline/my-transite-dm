<template>
  <div>
    <md-field>
      <label>Distance:</label>
      <md-input v-model="distance" type="text" maxlength="4" required />
      <md-tooltip md-direction="right">Radius of circle</md-tooltip>
    </md-field>
    <md-field>
      <label>Unit:</label>
      <md-select id="unit" required v-model="distanceUnits"  >
        <md-option value="Meter">Meter</md-option>
        <md-option value="Kilometer">Kilometer</md-option>
        <md-option value="Mile">Mile</md-option>
        <md-option value="Feet">Feet</md-option>
      </md-select>
      <md-tooltip md-direction="right">Distance units used</md-tooltip>
    </md-field>
    <md-field>
      <label>Center:</label>
      <md-input v-model="centerX" type="range" min="-90", max="90"  maxlength="10" required/>
      <md-tooltip md-direction="right">Center of circle North/South</md-tooltip>
      <md-input v-model="centerY" type="range" min="-180", max="180"  maxlength="10" required/>
      <md-tooltip md-direction="right">Center of circle East/West</md-tooltip>
    </md-field>
  </div>
</template>

<script>
export default {
  name: "Circle",
  event: 'change',
  data: () => ({
    centerX: null,
    centerY: null,
    distance: 10,
    distanceUnits: 'Meter',
  }),
  watch: {
    // eslint-disable-next-line no-unused-vars
    centerX(val, oldval) {
        this.$emit( 'change', {
            center: { X: val, Y: this.centerY },
            distance: this.toMeters(this.distance)
        });
    },
    // eslint-disable-next-line no-unused-vars
    centerY(val, oldval) {
      this.$emit( 'change', {
        center: { X: this.centerX, Y:  val},
        distance: this.toMeters(this.distance, this.distanceUnits)
      });
    },
    // eslint-disable-next-line no-unused-vars
    distance(val, oldval) {
      this.$emit( 'change', {
        center: { X: this.centerX, Y: this.centerY },
        distance: this.toMeters(val, this.distanceUnits)
      });
    },
    // eslint-disable-next-line no-unused-vars
    distanceUnits(val, oldval) {
      this.$emit( 'change', {
        center: { X: this.centerX, Y: this.centerY },
        distance: this.toMeters(this.distance, val)
      });
    }
  },
  methods: {
     toMeters(distance, units) {
         switch (units) {
           case "Meter":
              return distance;
           case "Kilometer":
              return distance * 1000.0;
           case "Mile":
              return distance * 1609.344;
           case "Feet":
               return distance / 3.2808;
         }
     }
  }
}
</script>

<style scoped>

</style>