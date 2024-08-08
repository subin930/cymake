
<script setup>
import { ref, nextTick, onMounted } from "vue";
import Popper from "vue3-popper";
import axios from 'axios';
const emit = defineEmits(['fileUploaded']);
const token = localStorage.getItem("token");
const loading = ref(false);

const formElement = ref(null);
const title = ref('');
const fileSize = ref(0);
const file = ref(null);
const filename = ref('');

const handleFileUpload = (event) => {
  file.value = event.target.files[0];
  console.log(file.value);
  if (file.value) {
    fileSize.value = (file.value.size / (1024 * 1024)).toFixed(2); // MB 단위로 변환
    filename.value = file.value.name;
  }
  
};

const fileUpload = async (close) => {
  const formData = new FormData();
  formData.append('file', file.value);
  formData.append('title', title.value);
  if(file.value){
    console.log(file.value);
    try {
      loading.value = true;
      const response = await axios.post(`/v1/drive/upload`, formData, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'multipart/form-data'
        }
      });
      console.log(response.data.message);
      await nextTick();
      resetForm(); //초기화
      emit('fileUploaded');
      close(); // 파일 업로드 성공 시 Popper 닫기
    } catch (error) {
      console.error('파일 업로드 오류:', error);
    } finally {
      loading.value = false;
    }
  } else {
    console.log('nofile');
  }
};
const cancelFile = () => {
  console.log(`파일이 선택 취소되었습니다: ${file.value.name}`);
  file.value = null;
  fileSize.value = 0;
  document.getElementById('file').value = null;
  formElement.value.reset();
}

const submitForm = (close) => {
  fileUpload(close);
};
const cancel = (close) => {
  resetForm();
  close();
}
const resetForm = () => {
  title.value = '';
  file.value = null;
  formElement.value.reset(); // 폼의 모든 입력 필드를 초기화
  console.log('resetForm');
};

onMounted(() => {

document.getElementById('uploadForm').addEventListener('submit', function(event) {
  var fileInput = document.getElementById('input-file');
  if (fileInput.files.length === 0) {
    if(!file.value){
      alert('파일을 선택해 주세요.');
      event.preventDefault(); // 폼 제출 방지
    }
  }
})
});
</script>

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
      <div class="popper-content align-items-center justify-content-center">
        <div class="row text-center justify-content-center me-2">
          <p style="font-size: 1.1rem; font-weight:bold;">파일등록</p>
        </div>
        <form @submit.prevent="submitForm(close)" ref="formElement" id="uploadForm">
          <div class="form-group d-flex mb-3">
            <label for="title" class="px-2 col-3 text-center me-2" style="white-space: nowrap; font-size: 0.9rem">제목</label>
            <input type="text" class="w-75" style="font-size: 0.9rem" id="title" v-model="title" required />
          </div> 
          <div class="form-group row d-flex mb-3">
            <label for="file" class="px-2 col-3 text-center me-2" style="white-space: nowrap; font-size: 0.9rem">파일 첨부</label>
            <label v-if="file===null" for="input-file" class="file-button col-auto ms-2 text-start" style="font-size: 0.8rem"><i class="bi bi-paperclip"></i>파일첨부</label>
            <input type="file" class="file-form" style="font-size: 0.9rem" id="input-file" @change="handleFileUpload" />
            <div class="col align-items-center" v-if="file!==null">
              <p class="col mb-1" style="font-size:.8rem;">
                {{ filename }}
                <button class="cancel-button" type="button" style="font-size: .8rem;" @click="cancelFile"><i class="bi bi-x-square"></i></button>
              </p>
            </div>
            <div class="row d-flex">
              <p class="col-3"></p>
              <p class="col file-size d-flex text-start" style="font-size: .8rem">현재 {{ fileSize }}MB / (첨부파일 : 30MB로 제한)</p>
          
            </div>
          </div>
          <div v-if="loading" class="text-center my-4">
              <div class="spinner-border text-secondary" role="status">
                <span class="visually-hidden">Loading...</span>
              </div>
          </div>
          <div v-else class="row button-group d-flex justify-content-center mb-2">
            <button
              type="button"
              @click="cancel(close)"
              class="btn btn-secondary cancel-button col-3 mx-4"
              style="border-radius: 20px; width: 90px; font-size:0.9rem"
            >
              취소
            </button>
            <button
              type="submit"
              class="btn btn-secondary submit-button col-3"
              style="border-radius: 20px; width: 90px; font-size: 0.9rem; background-color: #7248BD;"
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
  --popper-theme-background-color-hover:  #ffffff;
  --popper-theme-text-color: #000000;
  --popper-theme-border-radius: 2px;
  --popper-theme-border-width: 1px;
  --popper-theme-border-style: solid;
  --popper-theme-border-color: #e3e3e3;
  --popper-theme-padding: 30px;
}
.popper-content {
  min-width: 600px; /* Fixed width */
  max-width: 600px;
  padding: 0px;
  background-color: white;
}
.file-form {
  display: none;
}
.file-button {
  display: inline-block;
  padding: 4px;
  padding-right: 5px;
  background-color: #7248BD;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: .8rem;
  text-align: center;
  text-decoration: none;
}
.cancel-button {
  padding: 0px 2px 0px 2px; /* 조절 가능한 패딩 */
  font-size: 0.8rem;
  color:#7248BD; /* 아이콘 색상 */
  background-color: #F5F6FA; /* 배경색 */
  border: none; /* 테두리 제거 */
  cursor: pointer; /* 마우스 오버 시 커서 변경 */
}
.cancel-button:hover {
  color:white;
  background-color: #7248BD; /* 호버 시 배경색 변경 */
}
.file-size {
  margin-left:15px;
}
</style>
