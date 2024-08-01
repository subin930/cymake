<template>
    <button type="button"
            class="btn news-btn text-start"
            style="font-size: .8rem; font-weight: bold"
            @click="openModal(title, imgUrl, newsLink)">
      <div class="news-image-wrapper">
        <img :src="imgUrl" alt="news image" class="news-image">
      </div>
      <br/>
      <p class="news-title ms-1">{{ title }}</p>
      <br/>
      <p class="news-date ms-1" style="font-size: .6rem; font-weight:normal">{{ formattedDate }}</p>
    </button>
  </template>
  
  <script setup>
  import { computed } from 'vue';
  import { defineProps } from 'vue';
  
  // defineProps를 사용하여 부모 컴포넌트로부터 필요한 속성을 받아옵니다.
  const props = defineProps({
    title: String,
    imgUrl: String,
    newsLink: String,
    uploadDate: String,
    openModal: Function,
  });
  
  const formattedDate = computed(() => {
    // 날짜 포맷팅 로직 추가 (예: '2024-07-30' 형식으로 포맷팅)
    const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' };
    const date = new Date(props.uploadDate);
    return date.toLocaleDateString(undefined, options);
  });
  </script>
  
  <style scoped>
.news-btn:hover {
    color: #7248BD;
    border-color:#7248BD;
}
.news-btn {
  border-radius: 0px;
  border-color:#E7E7E9;
  background-color: #FFFFFF;
  display: flex;
  flex-direction: column;
  padding: 0%;
  width: 100%; /* Set the width of the image */
  height: 20rem;
  text-overflow: ellipsis;
}
.news-image-wrapper {
  height: 50%;
  width: 100%;
  background-color: #F2F2F2;
  display: flex;
  justify-content: center;
  align-items: center;
}
.news-image-wrapper:hover {
  color: black; /* 마우스 오버 시에도 글자색을 검정색으로 유지 */
}
.news-image {
  max-width: 100%;
  max-height: 100%; /* Set the height of the image */
  object-fit: cover;
}
.news-title {
  flex: 0 0 auto;
  font-size: 0.8rem;
  font-weight: bold;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: normal;
  margin-top: 5px;
}
.news-date {
  flex: 0 0 auto;
  font-size: 0.6rem;
  color: #6D6D6D;
  margin-top: auto;
}
  </style>
  