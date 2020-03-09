<template>
  <div>
    <h3>我的收藏页面</h3>
    <p>id：{{ id }}</p>
  </div>
</template>
<script>
export default {
  props: {
    id: {
      type: String,
      default: null
    }
  }
}
</script>
