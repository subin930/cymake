<script setup>
import { RouterLink, RouterView } from 'vue-router'

</script>

<template>
  <div class="app-container">
    <RouterView />
  </div>
</template>

<style>
.app-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh; /* 페이지 전체 높이 차지 */
  background-color: #f5f5f5; /* 페이지 전체 배경 색상 */
}
</style>