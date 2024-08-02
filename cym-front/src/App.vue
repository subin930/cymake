<script setup>
import { RouterLink, RouterView } from 'vue-router'

</script>

<template>
  <Suspense>
    <div class="app-container">
      <RouterView />
    </div>
  </Suspense>
  
</template>

<style>
.app-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh; /* 페이지 전체 높이 차지 */
  background-color: #F5F6FA; /* 페이지 전체 배경 색상 */
}
</style>