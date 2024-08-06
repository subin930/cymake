<template>
  <button type="button"
          class="btn news-btn text-start"
          style="font-size: .8rem; font-weight: bold"
          @click="openModal(title, imgUrl, newsLink, summary, keywords)">
    <div class="news-image-wrapper">
      <img :src="imgUrl" alt="news image" class="news-image">
    </div>
    <div class="news-content">
      <p class="news-title">{{ title }}</p>
      <div class="news-info">
        <p class="news-keyword">{{ keywords.join(' / ') }}</p>
        <p class="news-date">{{ formattedDate }}</p>
      </div>
    </div>
  </button>
</template>
  
<script setup>
  import { computed } from 'vue';
  import { defineProps } from 'vue';
  
  // defineProps를 사용하여 부모 컴포넌트로부터 필요한 속성을 받아옵니다.
  const props = defineProps({
    title: String,
    imgUrl: String,
    keywords: Array,
    summary: Array,
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
  padding: 0;
  width: 100%;
  height: 20rem;
}

.news-image-wrapper {
  height: 50%;
  width: 100%;
  background-color: #F2F2F2;
  display: flex;
  justify-content: center;
  align-items: center;
}

.news-image {
  max-width: 100%;
  max-height: 100%;
  object-fit: cover;
}

.news-content {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  flex: 1;
  padding: 5px 10px;
}

.news-title {
  font-size: 0.8rem;
  font-weight: bold;
}

.news-info {
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  flex: 1;
}

.news-keyword {
  font-size: 0.7rem;
  color: #7248BD;
  font-weight: normal;
  margin-top: auto; /* 키워드 위치 고정 */
}

.news-date {
  font-size: 0.6rem;
  color: #6D6D6D;
}
  </style>
  