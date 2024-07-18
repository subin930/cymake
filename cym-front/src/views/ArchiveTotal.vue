<script setup>
import { RouterLink, RouterView, useRoute, useRouter } from 'vue-router'
import Header from '@/components/common/Header.vue'
import NewsModal from '@/components/common/NewsModal.vue';
import { ref } from "vue";
const route = useRoute();
const router = useRouter();
const contentToken = ref('0');
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

const setCotentCar = () => {
    contentToken.value = '0';
};

const setContentCos = () => {
    contentToken.value = '1';
};

const modalID = ref('newsModal');
const title = ref('title news test');
const message = ref('news body test');
const eventFunction = ref(testfun);

</script>

<template>
    <Header></Header>
    <div class="container  justify-content-center">        
        <div class="m-3 mt-4 text-center">
            <p class="fw-bold fs-3">지식아카이브</p>
        </div>
        <div class="container m-3">
            <div class="row">
                <div class="btn-group" role="group" aria-label="Basic radio toggle button group">
                    <input type="radio" class="btn-check col-6" name="btnradio" id="btnradio1" autocomplete="off" checked>
                    <label class="btn fw-bold" for="btnradio1" style = "--bs-btn-active-color: #FFFFFF; --bs-btn-active-bg: #7248BD; --bs-btn-bg: #F5F6FA; font-size:1.1rem;" @click="setCotentCar()"><span class="material-symbols-outlined" style="font-size:1.1rem">description</span>자동차 산업 정보</label>

                    <input type="radio" class="btn-check col-6" name="btnradio" id="btnradio2" autocomplete="off">
                    <label class="btn fw-bold" for="btnradio2" style = "--bs-btn-active-color: #FFFFFF; --bs-btn-active-bg: #7248BD; --bs-btn-bg: #F5F6FA; font-size:1.1rem;" @click="setContentCos()"><span class="material-symbols-outlined" style="font-size:1.1rem">description</span>화장품 산업 정보</label>
                </div>
            </div>
            <div class="row d-flex align-items-center">
                <form class="input-group mt-3 mb-3" role="search">
                    <input type="search" class="form-control" placeholder="Please input" aria-label="Search">
                    <button type="button" class="btn btn-outline-secondary">검색</button>
                </form>
                
            </div>
            <div class="container text-center justify-content-between">
                <div v-if="contentToken == '0'" class="d-flex flex-row row-cols-auto">
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
                <div v-if="contentToken == '1'" class="d-flex flex-row row-cols-auto">
                    <div class="col-3">
                        <button type="button" class="btn news-btn" @click="openModal('화장품 뉴스1 제목', '화장품 뉴스1 본문')">화장품 뉴스1</button>
                    </div>
                    <div class="col-3">
                        <button type="button" class="btn news-btn" @click="openModal('화장품 뉴스2 제목', '화장품 뉴스2 본문')">화장품 뉴스2</button>
                    </div>
                    <div class="col-3">
                        <button type="button" class="btn news-btn" @click="openModal('화장품 뉴스3 제목', '화장품 뉴스3 본문')">화장품 뉴스3</button>
                    </div>
                    <div class="col-3">
                        <button type="button" class="btn news-btn" @click="openModal('화장품 뉴스4 제목', '화장품 뉴스4 본문')">화장품 뉴스4</button>
                    </div>
                </div>
            </div> 
        </div>
    </div>
    <NewsModal :modalID="modalID" :title="title" :message="message" :eventFunction="eventFunction"></NewsModal>
</template>

<style scoped>
.news-btn {
    border-color:#6D6D6D
}
.test-btn:hover {
  color: #7248BD; /* 마우스를 올렸을 때의 색상 */
}
</style>