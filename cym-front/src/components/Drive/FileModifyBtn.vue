<script setup>
import { onMounted, ref, watchEffect, nextTick } from "vue";
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
const dragOver = ref(false); // 드래그 중인지 상태 체크

const emit = defineEmits(['fileModified']);

const token = localStorage.getItem("token");
const loading = ref(false);
const formElement = ref(null); 

const handleNewFileUpload = async(event) => {
  const newFile = event.target.files[0];
  console.log('handleUPload');
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
  if (!currentfile.value) {
    console.log("no file and no current file loaded");
    console.log(currentfile.value);
  }
  else {
    console.log("yes file");
    loading.value = true;
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
      alert(error.response.data.message);
      console.error('파일 수정 오류:', error);
    } finally {
      loading.value = false;
    }
  }
};

const submitForm = (close) => {
  fileModify(close);
};

const cancelFile = async() => {
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

const resetForm =async() => {
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
// 드래그 상태 처리
const handleDragOver = (event) => {
  event.preventDefault();
  dragOver.value = true;
};

// 드래그 나갔을 때 상태 초기화
const handleDragLeave = () => {
  dragOver.value = false;
};

// 드래그 & 드롭으로 파일 업로드 처리
const handleDrop = (event) => {
  event.preventDefault();
  dragOver.value = false; // 드래그 상태 해제
  const droppedFiles = event.dataTransfer.files;
  if (droppedFiles.length) {
    Newfile.value = droppedFiles[0];
    currentfile.value = droppedFiles[0];
    fileSize.value = (droppedFiles[0].size / (1024 * 1024)).toFixed(3); // MB 단위로 변환
    fileName.value = droppedFiles[0].name;
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
                        <label for="title" class="px-2 text-center col-3 me-2" style="white-space: nowrap;font-size: 0.9rem">제목</label>
                        <input type="text" class="w-75" style="font-size: 0.9rem" id="title" v-model="title" required />
                    </div>
                    <div class="form-group row d-flex mb-1"  style="max-height:80px; min-height:80px;">
                      <label for="file" class="px-2 col-3 text-center me-2" style="white-space: nowrap; font-size: 0.9rem">파일 첨부</label>
                        <div v-if="currentfile===null" class="col">
                          <label for="input-modify-file" class="file-button col-auto text-start" style="font-size: 0.8rem;"><i class="bi bi-paperclip"></i>파일첨부</label>
                          <input type="file" class="file-form" style="font-size: 0.9rem" id="input-modify-file" @change="handleNewFileUpload" />
                        </div>
                      <div class="col d-flex align-items-center" v-if="currentfile!==null">
                        <p class="mb-1 file-name" style="font-size:.8rem;">
                          {{ fileName }}
                        </p>
                        <button class="cancel-button mb-1" type="button" style="font-size: .8rem;" @click="cancelFile"><i class="bi bi-x-square"></i></button>
                      </div>
                      <div class="row d-flex mb-3">
                        <div class="col-3"></div>
                        <div 
                            v-if="currentfile === null" 
                            class="drag-and-drop-area col-auto"
                            @dragover="handleDragOver"
                            @dragleave="handleDragLeave"
                            @drop="handleDrop"
                            :class="{ 'drag-over': dragOver }"
                        >
                          <p class="text-muted" style="font-size: .8rem;">첨부할 파일을 마우스로 끌어서 추가할 수 있습니다.</p>
                        </div>
                      </div>
                      <div class="row d-flex file-size-details">
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
  min-height: 300px;
  max-height: 300px;
  padding: 0px;
  background-color: white;
}
.file-form {
  opacity: 0;
}
.form-group {
  display: flex;
  align-items: center; /* 요소들을 수직 중심으로 정렬 */
  justify-content: flex-start; /* 요소들을 왼쪽에서 시작하도록 정렬 */
}
.file-name {
  display: inline-block;
  overflow: hidden;        /* 내용이 넘칠 경우 숨깁니다 */
  white-space: nowrap;
  text-overflow: ellipsis; /* 넘친 텍스트를 ...로 표시합니다 */
  max-width: 25rem;
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
.button-group {
  margin-top: 80px;
}
.btn {
  border-color: #c9c9c9;
}
.drag-and-drop-area {
  background-color: #F5F6FA;
  border: 1.5px dashed #cccccc;
  max-width: 350px;
  padding: 10px;
  margin-top: 10px;
  margin-left: 20px;
  text-align: center;
}
/* 드래그 중일 때 스타일 변경 */
.drag-over {
  background-color: #ecebfe;
  border-color: #9f8dc0;
}
</style>