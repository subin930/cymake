<template>
  <Popper arrow>
    <button
      type="button"
      class="btn btn-secondary me-3"
      style="border-radius: 20px; background-color: #7248BD; font-size:.8rem;"
      ref="triggerButton"
    >
      <i class="bi bi-plus-lg"></i> 파일등록 
    </button>
    <template #content="{ close }">
      <div class="align-items-center justify-content-center">
        <div class="row w-100 text-center me-2">
          <p style="font-size: 1.1rem; font-weight:bold">파일등록</p>
        </div>
        <form @submit.prevent="submitForm(close)">
          <div class="form-group mb-3">
            <label for="title" class="px-2 col-2 text-end me-3" style="font-size: 0.9rem">제목</label>
            <input type="text" class="w-75" style="font-size: 0.9rem" id="title" v-model="title" required />
          </div>
          <div class="form-group mb-3">
            <label for="file" class="px-2  text-start" style="font-size: 0.9rem">파일첨부</label>
            <input type="file" style="font-size: 0.9rem" id="file" @change="handleFileUpload" required />
            <p class="px-2" style="font-size: .8rem">현재 {{ fileSize }}MB / (첨부파일 : 30MB로 제한)</p>
          </div>
          <div class="row button-group d-flex justify-content-center mb-2">
            <button
              type="button"
              @click="cancel(close)"
              class="btn btn-secondary cancel-button col-3 mx-3"
              style="border-radius: 20px; font-size:0.9rem"
            >
              취소
            </button>
            <button
              type="submit"
              class="btn btn-secondary submit-button col-3"
              style="border-radius: 20px; font-size: 0.9rem; background-color: #7248BD;"
            >
              등록
            </button>
          </div>
        </form>
      </div>
    </template>
  </Popper>
</template>


<style>
:root {
  --popper-theme-background-color: #ffffff;
  --popper-theme-background-color-hover: #ececec;
  --popper-theme-text-color: #000000;
  --popper-theme-border-radius: 18px;
  --popper-theme-border-width: 1px;
  --popper-theme-border-style: solid;
  --popper-theme-border-color: #c9c9c9;
  --popper-theme-padding: 30px;
}
</style>

<script setup>
import { ref } from "vue";
import Popper from "vue3-popper";
import axios from 'axios';
const emit = defineEmits(['fileUploaded']);
const token = localStorage.getItem("token");
const title = ref('');
const fileSize = ref(0);
const file = ref(null);

const handleFileUpload = (event) => {
  file.value = event.target.files[0];
  if (file.value) {
    fileSize.value = (file.value.size / (1024 * 1024)).toFixed(2); // MB 단위로 변환
  }
};

const fileUpload = async (close) => {
  const formData = new FormData();
  formData.append('file', file.value);
  formData.append('title', title.value);
  try {
    const response = await axios.post(`/v1/drive/upload`, formData, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    console.log(response.data.message);
    resetForm(); //초기화
    emit('fileUploaded');
    close(); // 파일 업로드 성공 시 Popper 닫기
  } catch (error) {
    console.error('파일 업로드 오류:', error);
  }
};

const submitForm = (close) => {
  fileUpload(close);
};
const cancel = (close) => {
  resetForm();
  close();
}
const resetForm = () => {
  title.value = '';
  fileSize.value = 0;
  file.value = null;
  // 파일 입력 필드 초기화
  const fileInput = document.getElementById('file');
  if (fileInput) {
    fileInput.value = null;
  }
};
</script>
