<template lang="html">
  <app-model-view
    :initiativeId="$route.params.initiativeId"
    :viewId="$route.params.viewId"
    :key="$route.params.viewId">
  </app-model-view>
</template>

<script>
import ModelView from '@/components/model/ModelView.vue'

export default {
  components: {
    'app-model-view': ModelView
  }
}
</script>

<style lang="css">
</style>
