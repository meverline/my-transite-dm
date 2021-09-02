<template>
  <div>
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
</template>

<script>
export default {
  name: "Rectangle",
  data: () => ({
    upperLeft: '',
    lowerRight: '',
  }),
  watch: {
    // eslint-disable-next-line no-unused-vars
    upperLeft(val, oldval) {
      this.$emit('change', {
        upperLeft: val,
        lowerRight: this.lowerRight,
      });
    },
    // eslint-disable-next-line no-unused-vars
    lowerRight(val, oldval) {
        this.$emit( 'change', {
          upperLeft: this.upperLeft,
          lowerRight: val
        });
    }
  }
}
</script>

<style scoped>

</style>