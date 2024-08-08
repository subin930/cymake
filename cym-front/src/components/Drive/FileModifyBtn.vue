<script setup>
import { onMounted, ref, watchEffect } from "vue";
import Popper from "vue3-popper";
import axios from 'axios';

const props = defineProps({
  file: Object,
});
const title = ref(props.file.postTitle);
const fileSize = ref(props.file.size);
const fileId = ref(props.file.fileId);
const fileName = ref(props.file.fileName);
const currentfile = ref(props.file);
const Newfile = ref(null);
const emit = defineEmits(['fileModified']);
const token = localStorage.getItem("token");

const formElement = ref(null); 

const handleNewFileUpload = (event) => {
  const newFile = event.target.files[0];
  if (newFile) {
    Newfile.value = newFile;
    currentfile.value = newFile; // 이 부분에서 객체를 직접 할당
    fileSize.value = (newFile.size / (1024 * 1024)).toFixed(2);
    fileName.value = newFile.name;
  }
  console.log(currentfile.value);
};
const formatSize = (size) => {
  console.log(title.value+fileName.value);
  return (size  / (1024 * 1024)).toFixed(3); // 3자리 소수점으로 표현
};

const fileModify = async (close) => {
  const formData = new FormData();
  formData.append('postTitle', title.value);
  formData.append('fileId', fileId.value);
  formData.append('file', Newfile.value);

  const newFileData = {
    postTitle: title.value,
    fileName:Newfile.value ? Newfile.value.name : fileName.value,
    size: Newfile.value ? formatSize(Newfile.value.size) : fileSize.value,
  }
  var fileInput = document.getElementById('input-modify-file');
  if (fileInput.files.length === 0 && !currentfile.value) {
    console.log("no file and no current file loaded");
    console.log(currentfile.value);
  }
  else {
    console.log("yes file");
    try {
      const response = await axios.put(`/v1/drive/edit`, formData, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'multipart/form-data'
        }
      });
      console.log(response.data.message);
      console.log(fileId.value);
      console.log(newFileData);
      emit('fileModified', { fileId, newFileData });
      resetForm();
      close(); // 파일 수정 성공 시 Popper 닫기
    } catch (error) {
      for (let [key, value] of formData.entries()) {
        console.log(key, value);
      }  
      console.error('파일 수정 오류:', error);
    }
  }
};

const submitForm = (close) => {
  fileModify(close);
};

const cancelFile = () => {
  console.log(`파일이 선택 취소되었습니다: ${currentfile.value}`);
  currentfile.value = null;
  fileSize.value = 0;
  Newfile.value = null;
  if (formElement.value) {  // formElement가 존재하는지 확인
    document.getElementById('input-file').value = null;
  }
}

const modifyCancel =  async (close) => {
  console.log('파일 수정 취소');
  currentfile.value = props.file;
  title.value = props.file.postTitle;
  fileSize.value = props.file.size;
  fileName.value = props.file.fileName;
  console.log(currentfile.value);
  close();
}

const resetForm = () => {
  fileSize.value = 0;
  file.value = null;
  title.value = '';
  fileId.value = 0;
  fileName.value = '';
  // 파일 입력 필드 초기화
  const fileInput = document.getElementById('file');
  if (fileInput) {
    fileInput.value = null;
  }
};

onMounted(() => {

  document.getElementById('modifyForm').addEventListener('submit', function(event) {
    var fileInput = document.getElementById('input-modify-file');
    if (fileInput.files.length === 0) {
      if(!currentfile.value){
        alert('파일을 선택해 주세요.');
        event.preventDefault(); // 폼 제출 방지
      }
    }
  })
});

watchEffect(() => {
  title.value = props.file.postTitle;
  fileSize.value = props.file.size;
  fileId.value = props.file.fileId;
  fileName.value = props.file.fileName;
});
</script>

<template>
    <Popper arrow>
        <button type="button" class="btn btn-outline-secondary btn-sm" style="font-size:.7rem;">
            <i class="bi bi-pencil-square"></i>
        </button>
        <template #content="{ close }">
            <div class="popper-content align-items-center justify-content-center">
                <div class="row w-100 text-center mb-3">
                    <p style="font-size: 1.1rem; font-weight:bold">파일수정</p>
                </div>
                <form @submit.prevent="submitForm(close)" id="modifyForm">
                    <div class="form-group d-flex mb-3">
                        <label for="title" class="px-2 text-center col-3 me-2" style="font-size: 0.9rem">제목</label>
                        <input type="text" class="col" style="font-size: 0.9rem" id="title" v-model="title" required />
                    </div>
                    <div class="form-group d-flex mb-3">
                      <label for="file" class="px-2 col-3 text-center me-2" style="white-space: nowrap; font-size: 0.9rem">파일 첨부</label>
                      <label v-if="currentfile===null" for="input-modify-file" class="file-button col-auto ms-2 text-start" style="font-size: 0.9rem"><i class="bi bi-paperclip"></i>파일첨부</label>
                        <input type="file" class="file-form" style="font-size: 0.9rem" id="input-modify-file" @change="handleNewFileUpload" />
                      <div class="col align-items-center" v-if="currentfile!==null">
                        <p class="col mb-1" style="font-size:.8rem;">
                          {{ fileName }}
                          <button class="cancel-button" type="button" style="font-size: .8rem;" @click="cancelFile"><i class="bi bi-x-square"></i></button>
                        </p>
                      </div>
                    </div>
                    <div class="row button-group d-flex justify-content-center mb-2">
                        <button
                            type="button"
                            @click="modifyCancel(close)"
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
.popper-content {
  min-width: 600px; /* Fixed width */
  max-width: 600px;
  padding: 0px;
  background-color: white;
}
.file-form {
  opacity: 0;
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
.btn {
  border-color: #c9c9c9;
}
</style>