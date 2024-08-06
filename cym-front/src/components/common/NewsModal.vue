<script setup>
//import defaultImage from '@/assets/card-image.svg';
import {onMounted} from 'vue';
const props=defineProps({
    title: String,
    imgUrl: String,
    newsLink: String,
    summary: Array,
    keywords: Array
});

//const handleImageError = (event) => {
//    event.target.src = defaultImage;
//};
onMounted(() => {
    console.log(props);
});
</script>

<template>
    <!-- Modal --> 
    <div class="modal fade" id="newsModal" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="modalLabel" style="font-weight: bold">{{title}}</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <img :src="imgUrl" alt="news image" class="news-image">
                <br>
                
                <p class="mt-2" style="color: #7248BD; font-weight: bold">뉴스 요약</p>
                
                <ul class="list-group m-2">
                    <li v-for="summaryItem in summary" :key="summaryItem" class="list-group-item px-2" style="font-size: .9rem;">{{ summaryItem }}</li>
                </ul>
                <p class="news-keyword ms-3" style="color: #7248BD; font-weight: bold; font-size: .9rem;"> {{ keywords.join(' / ') }} </p>
                <a :href="newsLink" class="news-link mt-2" style="font-size: .9rem; font-weight: bold" target="_blank" rel="noopener noreferrer">뉴스 원문 보기 <i class="bi bi-box-arrow-up-right"></i></a>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
            </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.news-image {
    width: 100%;
    height: auto;
    display: block;
    margin-bottom: 10px;
}
.news-link {
  color: #464646; /* 기본 텍스트 색상 사용 */
  text-decoration: none; /* 밑줄 제거 */
}
.news-link:hover {
  color: #7248BD;
}
.modal-footer {
    border-top: none; /* 모달 푸터의 상단 선 제거 */
}
</style>