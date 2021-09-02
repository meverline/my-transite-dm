<template>
    <md-content class="md-scrollbar">
      <select id="v-for-agencys" multiple size="5" v-model="selected">
        <option v-for="value in agencys" v-bind:value.agency="value" >{{ value.name }}</option>
      </select>
    </md-content>
</template>

<script>
export default {
  name: "Agency",
  data: {
    agencys: [ { agency: 'Metro', name: 'WMATA'} ,
               { agency: 'ART', name: 'ART'},
               { agency: 'CUE', name: 'Fairfax CUE'},
               { agency: 'RideOn', name:'RideOn' },
               { agency: 'FallsChurch', name: 'FallsChurch' },
               { agency: 'FairfaxCity', name: 'Fairfax City'} ],
    selected: null
  },
  event: 'change',
  watch : {
     selected: function(val, oldval) {
       this.$emit('change', val);
     }
  }
  methods : {
  }

}
</script>

<style scoped>

</style>