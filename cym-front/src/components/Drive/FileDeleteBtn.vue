<script setup>
import { ref, watchEffect } from 'vue';
import axios from 'axios';

const props = defineProps({
  file: Object,
});
const emit = defineEmits(['fileDeleted']);
const token = localStorage.getItem("token");
const fileId = ref(props.file.fileId);

const deleteFile = async() => {
    try {
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
  }
}
watchEffect(() => {
  fileId.value = props.file.fileId;
});
</script>

<template>
    <button type="button" class="btn btn-outline-secondary btn-sm" @click="deleteFile()"
    style="font-size: .7rem" >
      <i class="bi bi-x-lg"></i>
    </button>
</template>

<style scoped>
.btn {
  border-color: #c9c9c9;
}
</style>