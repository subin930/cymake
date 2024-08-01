<script setup>
import { ref, watchEffect } from 'vue';
import axios from 'axios';

const props = defineProps({
  file: Object,
});
const emit = defineEmits(['fileDeleted']);
const token = localStorage.getItem("token");
const originalFileName = ref(props.file.fileName);

const deleteFile = async() => {
    try {
    const response = await axios.delete(`/v1/drive/delete`, {
      params: {
        filename: props.file.fileName,
      },
      headers: {
        'Authorization': `Bearer ${token}`
      }});
    console.log(response.data.message);
    console.log(originalFileName.value);
    emit('fileDeleted', originalFileName);
  } catch (error) {
    console.log(props.file.fileName);
    console.log('Token:', token);
    console.error('파일 삭제 오류:', error);
  }
}
watchEffect(() => {
  originalFileName.value = props.file.fileName;
});
</script>

<template>
    <button type="button" class="btn btn-outline-secondary btn-sm" @click="deleteFile()" >
        <i class="bi bi-trash"></i>
    </button>
</template>

<style scoped>
.btn {
  border-color: #c9c9c9;
}
</style>