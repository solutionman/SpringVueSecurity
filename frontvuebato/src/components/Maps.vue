<template>
  <v-card
      height="100%"
      width="100%">
    <div id="map" style="width: 100%; height: 100%"></div>
  </v-card>
</template>
<script>
export default {
  mounted() {
    ymaps.ready(init);
    function init(){
      let myMap = new ymaps.Map("map", {
        center: [54.925240841084886,83.07739967971673],
        zoom: 12
      });
    }
  }
}
</script>