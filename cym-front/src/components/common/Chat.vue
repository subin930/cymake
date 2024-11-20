<script setup>
import { ref, watch, onMounted, nextTick } from 'vue';
import axios from 'axios';

const token = localStorage.getItem("token");
const sessionId = ref(localStorage.getItem("sessionId"));
const uploadedFile= ref(null);

// Chat state (local에 저장할지 session에 저장할지) -> local로 구현한 상태
const messages = ref(JSON.parse(localStorage.getItem('chatMessages')) || []);
const newMessage = ref('');
const botMessage = ref('');
const chatMessages = ref(null);
const botLoading = ref(false);
const dragOver = ref(false); // 드래그 중인지 상태 체크

// message 추가 시 바로 스토리지에 저장될 수 있도록
watch(messages, (newMessages) => {
    localStorage.setItem('chatMessages', JSON.stringify(newMessages));
}, { deep: true });

const openChat = () => {
  const chatOffcanvas = new bootstrap.Offcanvas(document.getElementById('chatOffcanvas'));
  chatOffcanvas.show();
};


const closeChat = () => {
  const chatOffcanvas = bootstrap.Offcanvas.getInstance(document.getElementById('chatOffcanvas'));
  chatOffcanvas.hide();
};

//챗봇에게 메세지 전송
const sendMessage = async() => {
  if (uploadedFile.value) {
    if (uploadedFile.value.size > 30 * 1024 * 1024) { // 파일 크기 30MB 초과 확인
    alert('파일 크기는 30MB를 초과할 수 없습니다.');
    return; // 파일이 너무 크면 업로드 중단
  }
  }
  if (newMessage.value.trim()) {
    //메세지 목록에 유저 메세지 추가
    messages.value.push({ text: newMessage.value, sender: 'user' });
    console.log(sessionId);
    let formData = new FormData();
    formData.append('sessionId', sessionId.value);
    formData.append('question', newMessage.value);
    if (uploadedFile.value) {
      formData.append('file', uploadedFile.value);
      messages.value.push({ text: uploadedFile.value.name, sender: 'user-file' });
    }
    // Clear input field (메세지와 파일 초기화) + 자동 스크롤
    newMessage.value = '';
    uploadedFile.value = null;
    document.getElementById('file').value = null;
    scrollToBottom();
    try {
      botLoading.value = true;
      const response = await axios.post(`/v1/chat/question`,  formData, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'multipart/form-data'
        }
      });
      console.log(formData);
      console.log(response.data.message);
      if(sessionId.value=='0'){
        sessionId.value = response.data.content.sessionId;
        localStorage.setItem("sessionId", sessionId.value);
        console.log(sessionId.value);
      }
      botMessage.value = response.data.content.response;
    } catch (error){
      console.error('sendMessage error:', error);
      botMessage.value = '오류가 발생했습니다';
    }
    botLoading.value = false;
    // 챗봇 답변 추가
    messages.value.push({ text: botMessage.value, sender: 'bot' });
    scrollToBottom();
  }
};
const handleTextareaKeydown = (event) => {
  // Enter를 눌렀을 때 메시지 전송
  if (event.key === 'Enter' && !event.shiftKey) {
    event.preventDefault();  // 기본 Enter 동작(새 줄 추가)을 막습니다.
    sendMessage();  // 메시지 전송 함수 호출
  }
  // Shift + Enter가 눌리면 기본적으로 새 줄을 추가합니다.
};

const handleFileAdded = (event) => {
  uploadedFile.value = event.target.files[0];
  if (uploadedFile) {
    console.log(`파일이 선택되었습니다: ${uploadedFile.name}`);
    
  }
};

const cancelFile = () => {
  console.log(`파일이 선택 취소되었습니다: ${uploadedFile.name}`);
  uploadedFile.value = null;
  document.getElementById('file').value = null;
}
//탭 닫았다 키거나 다른 페이지로 넘어갈 시 스크롤 자동으로 아래로 돼있게 뜨게
const scrollToBottom = () => {
  nextTick(() => {
    chatMessages.value.scrollTop = chatMessages.value.scrollHeight;
  });
};
// 드래그 앤 드롭 관련 함수
const handleDragOver = (event) => {
  event.preventDefault();
  dragOver.value = true;
};

const handleDragLeave = () => {
  dragOver.value = false;
};

const handleDrop = (event) => {
  event.preventDefault();
  dragOver.value = false; // 드래그 상태 해제
  const droppedFiles = event.dataTransfer.files;
  if (droppedFiles.length) {
    uploadedFile.value = droppedFiles[0];
    console.log(`드롭된 파일: ${uploadedFile.value.name}`);
  }
};

// 텍스트박스에 붙여넣기(Ctrl + V)로 파일 업로드
const handlePaste = (event) => {
  const items = event.clipboardData.items;
  for (let i = 0; i < items.length; i++) {
    const item = items[i];
    if (item.kind === 'file') {
      const file = item.getAsFile();
      uploadedFile.value = file;
      console.log(`붙여넣기된 파일: ${file.name}`);
      break; // 파일 하나만 처리
    }
  }
};
onMounted(() => {
  scrollToBottom();
  
  const textarea = document.querySelector('textarea');
  textarea.addEventListener('paste', handlePaste); // 붙여넣기 이벤트 리스너 추가
});
</script>


<template>
    <div>
        <!-- Chat Button -->
        <button v-if="token !== null" type="button" class="btn btn-outline-light me-1" style="--bs-btn-font-size: .8rem;  color: #7248BD;" @click="openChat()"><span class="material-symbols-outlined">saved_search</span></button>
        <!-- Offcanvas Chat -->
        <div class="offcanvas offcanvas-end" tabindex="-1" id="chatOffcanvas" aria-labelledby="chatOffcanvasLabel">
            <div class="offcanvas-header">
                <button type="button" class="btn-close text-reset" @click="closeChat"></button>
            </div>
            <br>
            <div class="offcanvas-body p-0">
            <!-- Chat messages -->
                <div class="chat-container" ref="chatMessages" >
                    <div v-for="(message, index) in messages" :key="index" style="white-space: pre-wrap;"
                    class="message" :class="{'user': message.sender === 'user', 'bot': message.sender === 'bot', 'user-file': message.sender === 'user-file'}">
                    {{ message.text }}
                    </div>
                    <div v-if="botLoading===true" class="message bot" style="font-size: .9rem; color: #212121;">응답 생성 중입니다...</div>
                </div>
                <!-- Input for new message -->
                <div class="container chat-input justify-content-between mb-2"
                  @dragover.prevent="handleDragOver"
                  @dragleave="handleDragLeave"
                  @drop="handleDrop"
                  :class="{ 'drag-over': dragOver }">
                    <div class="d-flex chat-file mb-2">
                        <label v-if="uploadedFile===null" class="input-file-button mx-1" for="file"><i class="bi bi-paperclip"></i>파일첨부</label>
                          <input type="file" class="form-control input-file" id="file"
                          accept=".txt,.md,.html,.doc,.docx,.csv,.xls,.xlsx,.pdf" @change="handleFileAdded"/>
                        <div class="row align-items-center" v-if="uploadedFile!==null">
                          <p class="col ms-1 mb-1" style="font-size:.8rem;">
                            {{ uploadedFile.name}}
                            <button class="cancel-button" style="white-space: pre-wrap; font-size: .8rem;" @click="cancelFile"><i class="bi bi-x-square"></i></button>
                          </p>
                        </div>
                    </div>
                    <div class="d-inline-flex align-items-center">
                        <textarea v-model="newMessage" @keydown="handleTextareaKeydown" 
                        type="text" class="d-inline form-control"
                        placeholder="질문을 입력하세요..." style="font-size: .9rem; height:80px;">
                        </textarea>
                        <button class="btn btn-primary" @click="sendMessage" style="font-weight: 400">전송</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>


<style scoped>
.offcanvas {
  width: 30rem;
}
.offcanvas-header {
  border-bottom: 1px solid #ccc; /* Add bottom border to the header */
  height:30px;
}
.chat-container {
    display: flex;
    flex-direction: column;
    max-height: calc(100vh - 185px); /* Adjust height based on header and input area */
    overflow-y: auto;
    padding-left: 10px;
    padding-right: 10px;
    margin-bottom: 60px; /* Space for the input box */
}

.message {
    padding: 15px;
    border-radius: 10px;
    margin-bottom: 10px;
    max-width: 80%;
}
.message.user {
    background-color: #F5F6FA;
    font-size: .9rem; 
    align-self: flex-end;
    color: #212121;
}

.message.user-file {
    background-color: #F5F6FA; /* 동일 배경 */
    font-size: 0.7rem; /* 글씨 크기를 줄임 */
    align-self: flex-end; /* 정렬 유지 */
    padding: 3px; /* 패딩 줄임 */
    margin-bottom: 2px; /* 간격 줄임 */
    color: #666666; /* 텍스트 색상 변경 */
}
.message.bot {
    background-color: #D6D6D6;
    align-self: flex-start;
    font-size: .9rem; 
    color: #212121;
}
.chat-file {
    display: flex;
    align-items: center;
    width: 100%; /* 입력 요소와 버튼을 컨테이너 너비에 맞춤 */
}
.input-file {
  display: none;
}
.input-file-button {
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
  transition: background-color 0.3s ease;
}
.cancel-button {
  padding: 0px 2px 0px 2px; /* 조절 가능한 패딩 */
  font-size: 0.8rem;
  color:#7248BD; /* 아이콘 색상 */
  background-color: #F5F6FA; /* 배경색 */
  border: none; /* 테두리 제거 */
  cursor: pointer; /* 마우스 오버 시 커서 변경 */
  transition: background-color 0.3s; /* 호버 효과를 위한 전환 */
}

.cancel-button:hover {
  color:white;
  background-color: #7248BD; /* 호버 시 배경색 변경 */
}
.chat-input {
    position: fixed;
    bottom: 5px;
    width: 30rem; /* Adjust for padding of offcanvas */
    display: box;
    padding: 5px;
    height: 120px;
    background-color: #F5F6FA;
}

.chat-input .form-control {
    width: 25rem;
}
.chat-input .btn {
    width: 4rem;
    margin-left: .5rem;
    background-color: #7248BD;
    border-color: #D9CEFF;
    color: white;
}
.chat-input.drag-over {
  border: 0.5px dashed #b3a4cf;
  background-color: #f0f0f0;
}
</style>
