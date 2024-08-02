<script setup>
import { ref } from "vue";
import { useRoute, useRouter } from "vue-router";
const route = useRoute();
const router = useRouter();

</script>



<template>
    <header class="init-header d-flex flex-wrap justify-content-center px-3 py-2 border-bottom" style="border-color: #00A5BD;">
        <div class="d-flex">
            <a href="/login">
                <img src="@/assets/CYMAKE.jpg" style="height: 3rem;"/>
            </a>
        </div>
    </header>
    <div class="content-wrapper">
    <slot></slot> <!-- 메인 콘텐츠가 들어갈 자리 -->
    </div>
</template>

<style scoped>
.init-header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 65px;
  background-color: #fff; /* 배경색 */
  box-shadow: 0 1px 5px rgba(0, 0, 0, 0.1);
  z-index: 1000; /* 다른 콘텐츠보다 위에 위치하게 설정 */
}
.content-wrapper {
  margin-top: 65px; /* 헤더의 높이만큼 추가 */
  
}
</style>