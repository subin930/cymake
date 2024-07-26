<script setup>
import { ref } from "vue";
import Popper from "vue3-popper";
import axios from 'axios';

const props = defineProps({
  file: Object,
});
const title = ref(props.file.postTitle);
const fileSize = ref(props.file.size);
const originalFileName = ref(props.file.fileName);
const fileName = ref(props.file.fileName);
const Newfile = ref(null);
const emit = defineEmits(['fileModified']);
const token = localStorage.getItem("token");

const handleNewFileUpload = (event) => {
  Newfile.value = event.target.files[0];
  if (Newfile.value) {
    fileSize.value = (Newfile.value.size / (1024 * 1024)).toFixed(2); // MB 단위로 변환
    fileName.value = Newfile.value.name;
  }
};

const fileModify = async (close) => {
  const formData = new FormData();
  formData.append('postTitle', title.value);
  formData.append('originalFilename', originalFileName.value);
  formData.append('file', Newfile.value);
  try {
    const response = await axios.put(`/v1/drive/edit`, formData, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    console.log(response.data.message);
    resetForm();
    emit('fileModified');
    close(); // 파일 수정 성공 시 Popper 닫기
  } catch (error) {
    console.log(title.value);
    console.error('파일 수정 오류:', error);
  }
};

const submitForm = (close) => {
  fileModify(close);
};

const resetForm = () => {
  
  fileSize.value = 0;
  file.value = null;
  // 파일 입력 필드 초기화
  const fileInput = document.getElementById('file');
  if (fileInput) {
    fileInput.value = null;
  }
};
</script>

<template>
    <Popper arrow>
        <button type="button" class="btn btn-outline-secondary btn-sm">
            <i class="bi bi-pencil-square"></i>
        </button>
        <template #content="{ close }">
            <div class="align-items-center justify-content-center">
                <div class="row w-100 text-center mb-3">
                    <p style="font-size: 1.1rem; font-weight:bold">파일수정</p>
                </div>
                <form @submit.prevent="submitForm(close)">
                    <div class="form-group mb-3">
                        <label for="title" class="px-2" style="font-size: 0.9rem">제목</label>
                        <input type="text" class="w-75" style="font-size: 0.9rem" id="title" v-model="title" required />
                    </div>
                    <div class="form-group mb-3">
                        <label for="file" class="px-2" style="font-size: 0.9rem">파일첨부</label>
                        <input type="file" style="font-size: 0.9rem" id="file" @change="handleNewFileUpload" />
                        <p class="px-2" style="font-size: .8rem">현재 파일: {{ fileName }}</p>
                        <p class="px-2" style="font-size: .8rem">현재 {{ fileSize }}MB / (첨부파일 : 30MB로 제한)</p>
                    </div>
                    <div class="row button-group d-flex justify-content-center mb-2">
                        <button
                            type="button"
                            @click="close"
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

<style scoped>
:root {
  --popper-theme-background-color: #ffffff;
  --popper-theme-background-color-hover: #ececec;
  --popper-theme-text-color: #000000;
  --popper-theme-border-radius: 18px;
  --popper-theme-border-width: 1px;
  --popper-theme-border-style: solid;
  --popper-theme-border-color: #c9c9c9;
  --popper-theme-padding: 32px;
}
.btn {
  border-color: #c9c9c9;
}
</style>