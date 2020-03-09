<template>
  <div>
    <h3>个人信息</h3>
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
