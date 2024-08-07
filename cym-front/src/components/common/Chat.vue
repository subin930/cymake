<script setup>
import { ref, watch, onMounted, nextTick } from 'vue';
import axios from 'axios';

const token = localStorage.getItem("token");
const sessionId = ref(localStorage.getItem("sessionId"));
const uploadedFile= ref(null);

// Chat state (local에 저장할지 session에 저장할지)
const messages = ref(JSON.parse(localStorage.getItem('chatMessages')) || []);
const newMessage = ref('');
const botMessage = ref('');
const chatMessages = ref(null);
const botLoading = ref(false);

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
  if (newMessage.value.trim()) {
    //메세지 목록에 유저 메세지 추가
    messages.value.push({ text: newMessage.value + uploadedFile.value, sender: 'user' });
    console.log(sessionId);
    let formData = new FormData();
    formData.append('sessionId', sessionId.value);
    formData.append('question', newMessage.value);
    if (uploadedFile.value) {
      formData.append('file', uploadedFile.value);
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

const handleFileAdded = (event) => {
  uploadedFile.value = event.target.files[0];
  if (uploadedFile) {
    console.log(`파일이 선택되었습니다: ${uploadedFile.name}`);
    
  }
};

//탭 닫았다 키거나 다른 페이지로 넘어갈 시 스크롤 자동으로 아래로 돼있게 뜨게
const scrollToBottom = () => {
  nextTick(() => {
    chatMessages.value.scrollTop = chatMessages.value.scrollHeight;
  });
};

onMounted(scrollToBottom);
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
                    <div v-for="(message, index) in messages" :key="index" style="font-size: .9rem; color: #212121;"
                    class="message" :class="{'user': message.sender === 'user', 'bot': message.sender === 'bot'}">
                    {{ message.text }}
                    </div>
                    <div v-if="botLoading===true" class="message bot" style="font-size: .9rem; color: #212121;">응답 생성 중입니다...</div>
                </div>
                <!-- Input for new message -->
                <div class="container chat-input mb-2">
                    <div class="d-block chat-file py-2">
                        <input type="file" style="font-size: 0.9rem" id="file" 
                        accept=".txt,.md,.html,.doc,.docx,.csv,.xls,.xlsx,.pdf" @change="handleFileAdded"/>
                    </div>
                    <div class="d-inline-flex align-items-center">
                        <textarea v-model="newMessage" @keyup.enter="sendMessage" type="text" class="d-inline form-control"
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
    padding: 10px;
    border-radius: 10px;
    margin-bottom: 10px;
    max-width: 70%;
}

.message.user {
    background-color: #F5F6FA;
    align-self: flex-end;
}

.message.bot {
    background-color: #D6D6D6;
    align-self: flex-start;
}

.chat-input {
    position: fixed;
    bottom: 5px;
    width: 25rem; /* Adjust for padding of offcanvas */
    display: box;
    padding: 5px;
    height: 120px;
    background-color: #F5F6FA;
}

.chat-input .form-control {
    width: 20rem;
}
.chat-input .btn {
    width: 4rem;
    margin-left: .5rem;
    background-color: #7248BD;
    border-color: #D9CEFF;
    color: white;
}
</style>
