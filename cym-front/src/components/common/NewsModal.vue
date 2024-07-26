<script setup>
//import defaultImage from '@/assets/card-image.svg';

const props=defineProps({
    title: String,
    imgUrl: String,
    newsLink: String,
});

//const handleImageError = (event) => {
//    event.target.src = defaultImage;
//};
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
                <a :href="newsLink" class="news-link" style="font-weight: bold" target="_blank" rel="noopener noreferrer">뉴스 원문 보기 <i class="bi bi-box-arrow-up-right"></i></a>
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
</style>