<script setup>
import { defineProps, defineEmits } from 'vue';
import axios from 'axios';

const props = defineProps({
  file: Object,
});
const emit = defineEmits(['fileDeleted']);
const token = localStorage.getItem("token");
const modifyFile = async() => {
    console.log("modifyFile called");
    try {
    const response = await axios.delete(`/v1/drive/delete`, {
      headers: {
        'Authorization': `Bearer ${token}`
      },
      data: props.file.fileName // Correct way to send data with delete request
    });
    console.log(response.data.message);
    emit('fileDeleted');
  } catch (error) {
    console.error('파일 삭제 오류:', error);
  }
}

</script>

<template>
    <button type="button" class="btn btn-outline-secondary btn-sm" @click="modifyFile()" >
        <i class="bi bi-trash"></i>
    </button>
</template>