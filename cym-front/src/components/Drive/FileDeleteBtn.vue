<script setup>
import { ref, watchEffect } from 'vue';
import axios from 'axios';

const props = defineProps({
  file: Object,
});
const emit = defineEmits(['fileDeleted']);
const token = localStorage.getItem("token");
const loading = ref(false);
const fileId = ref(props.file.fileId);

const deleteFile = async() => {
  try {
    loading.value = true;
    const response = await axios.delete(`/v1/drive/delete`, {
      params: {
        fileId: props.file.fileId,
      },
      headers: {
        'Authorization': `Bearer ${token}`
      }});
    console.log(response.data.message);
    console.log(fileId.value);
    emit('fileDeleted', fileId);
  } catch (error) {
    console.log(props.file.fileId);
    console.log('Token:', token);
    console.error('파일 삭제 오류:', error);
  } finally {
    loading.value = false;
  }
}
watchEffect(() => {
  fileId.value = props.file.fileId;
});
</script>

<template>
  <div v-if="loading" class="text-center">
    <div class="spinner-border text-secondary" role="status">
      <span class="visually-hidden">Loading...</span>
    </div>
  </div>
  <button v-else type="button" class="btn btn-outline-secondary btn-sm" @click="deleteFile()"
  style="font-size: .7rem" >
      <i class="bi bi-x-lg"></i>
  </button>
</template>

<style scoped>
.btn {
  border-color: #c9c9c9;
}
.spinner-border {
  width: 1rem; /* 스피너의 너비를 1rem으로 설정 */
  height: 1rem; /* 스피너의 높이를 1rem으로 설정 */
  border-width: 0.1em; /* 테두리의 두께 조절 */
}

/* 스피너 내부의 숨김 텍스트 영역도 조절 */
.visually-hidden {
  font-size: 0.7rem; /* 필요한 경우 글꼴 크기 조절 */
}
</style>