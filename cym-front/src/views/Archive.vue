<script setup>
import { RouterLink, RouterView, useRoute, useRouter } from 'vue-router'
import Header from '@/components/common/Header.vue'
import NewsModal from '@/components/common/NewsModal.vue';
import { ref } from "vue";
const route = useRoute();
const router = useRouter();

const testfun = () => {
    console.log("testfun played");
    router.push("/signup");
};


const openModal = (titleVal, messageVal) => {
    title.value = titleVal;
    message.value = messageVal;
    const modalElement = document.getElementById(modalID.value);
    const modalInstance = new bootstrap.Modal(modalElement);
    modalInstance.show();
};

const modalID = ref('newsModal');
const title = ref('title news test');
const message = ref('news body test');
const eventFunction = ref(testfun);

</script>

<template>
    <Header></Header>
    
    <div class="container-fluid">        
        <div class="m-3 mt-4 text-center">
            <p class="fw-bold fs-3">지식아카이브</p>
        </div>
        <div class="container m-3">
            <div class="row row-cols-auto">
                    <p class="px-1 fw-bold">자동차 산업 정보</p>
                    <a class="nav-link active mt-1 px-2" aria-current="page" style="font-size: 0.8rem; font-weight: 550;" href="/archive/total"><span class="material-symbols-outlined" style="font-size:0.8rem">description</span>더 많은 정보 보기 〉</a>
            </div>
            <div class="container text-center justify-content-between">
                <div class="d-flex flex-row row-cols-auto">
                    <div class="col-3">
                        <button type="button" class="btn news-btn" @click="openModal('자동차 뉴스1 제목', '자동차 뉴스1 본문')">자동차 뉴스1</button>
                    </div>
                    <div class="col-3">
                        <button type="button" class="btn news-btn" @click="openModal('자동차 뉴스2 제목', '자동차 뉴스2 본문')">자동차 뉴스2</button>
                    </div>
                    <div class="col-3">
                        <button type="button" class="btn news-btn" @click="openModal('자동차 뉴스3 제목', '자동차 뉴스3 본문')">자동차 뉴스3</button>
                    </div>
                    <div class="col-3">
                        <button type="button" class="btn news-btn" @click="openModal('자동차 뉴스4 제목', '자동차 뉴스4 본문')">자동차 뉴스4</button>
                    </div>
                </div>
            </div> 
        </div>
        <div class="container m-3">
            <div class="row row-cols-auto">
                    <p class="px-1 fw-bold">화장품 산업 정보</p>
                    <a class="nav-link active mt-1 px-2" aria-current="page" style="font-size: 0.8rem; font-weight: 550;" href="/archive/total"><span class="material-symbols-outlined" style="font-size:0.8rem">description</span>더 많은 정보 보기 〉</a>
            </div>
            <div class="container text-center justify-content-between">
                <div class="d-flex flex-row row-cols-auto">
                    <div class="col-3">
                        <button type="button" class="btn news-btn" @click="openModal('화장품 뉴스1 제목', '화장품 뉴스1 본문')">화장품 뉴스1</button>
                    </div>
                    <div class="col-3">
                        <button type="button" class="btn news-btn" @click="openModal('화장품 뉴스2 제목', '화장품 뉴스2 본문')">화장품 뉴스2</button>
                    </div>
                    <div class="col-3">
                        <button type="button" class="btn news-btn" @click="openModal('화장품 뉴스3 제목', '뉴스3 본문')">화장품 뉴스3</button>
                    </div>
                    <div class="col-3">
                        <button type="button" class="btn news-btn" @click="openModal('화장품 뉴스4 제목', '뉴스4 본문')">화장품 뉴스4</button>
                    </div>
                </div>
            </div> 
        </div>
    </div>
    
    <NewsModal :modalID="modalID" :title="title" :message="message" :eventFunction="eventFunction"></NewsModal>
</template>

<style scoped>
.nav-link.active {
    color: #6D6D6D;
}
.nav-link.active:hover {
    color: #7248BD;
}
  
.nav-link.active:hover .material-symbols-outlined {
    color: #7248BD; 
}
.btn {
    border-color:#6D6D6D
}
.btn:hover {
    color: #7248BD;
    border-color:#7248BD;
}

</style>