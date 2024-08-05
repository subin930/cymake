<script setup>
import { RouterLink, RouterView, useRoute, useRouter } from 'vue-router'
import Header from '@/components/common/Header.vue'
import NewsModal from '@/components/common/NewsModal.vue';
import NewsItem from '@/components/common/NewsItem.vue';
import { ref, onMounted } from "vue";
import axios from 'axios'
import CarGraph from '@/components/Archive/CrawlGraphCar.vue';

const route = useRoute();
const router = useRouter();
const token = localStorage.getItem("token");
const contentCar = ref([]);
const contentBeauty = ref([]);
const subject = ref('');

const title = ref('no title');
const imgUrl = ref(null);
const newsLink = ref('no link');


const fetchCarNews = async () => {
  subject.value = "car";
  try {
    const response = await axios.get(`/v1/archive/${subject.value}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }});
    contentCar.value = response.data.content;
  } catch (error) {
    console.error('Error fetching data:', error);
  }
};
const fetchBeautyNews = async () => {
  subject.value = "beauty";
  try {
    const response = await axios.get(`/v1/archive/${subject.value}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }});
    contentBeauty.value = response.data.content;
  } catch (error) {
    console.error('Error fetching data:', error);
  }
};
const openModal = (titleVal, imgUrlVal, linkVal) => {
    title.value = titleVal;
    imgUrl.value = imgUrlVal;
    newsLink.value = linkVal;
    const modalElement = document.getElementById("newsModal");
    const modalInstance = new bootstrap.Modal(modalElement);
    modalInstance.show();
};
const setTokenCar = () => {
    localStorage.setItem("contentToken", 0);
}
const setTokenBeauty = () => {
    localStorage.setItem("contentToken", 1);
}


onMounted(fetchCarNews);
onMounted(fetchBeautyNews);
</script>

<template>
    <Header></Header>
    
    <div class="container-fluid justify-content-center">        
        <div class="m-3 text-center title">
            <p class="fw-bolder fs-3">지식아카이브</p>
        </div>
        <div class="container m-3 justify-content-between">
            <div class="row row-cols-auto">
                    <p class="px-1 fw-bold">자동차 산업 정보</p>
                    <a class="nav-link active mt-1 px-2" @click="setTokenCar" aria-current="page" style="font-size: 0.8rem; font-weight: 550;" href="/archive/total"><span class="material-symbols-outlined" style="font-size:0.8rem">description</span>더 많은 정보 보기 〉</a>
            </div>
            <div class="container text-start justify-content-between">
                <div class="row g-3">
                    <div  v-for="carNews in contentCar" :key="carNews.title" class="col-12 col-sm-6 col-md-4 col-lg-2">
                        <div class="col">
                          <NewsItem
                            :title="carNews.title"
                            :imgUrl="carNews.imgUrl"
                            :newsLink="carNews.newsLink"
                            :uploadDate="carNews.uploadDate"
                            :openModal="openModal"
                          />
                    </div>
                    </div>
                </div>
            </div> 
        </div>
        <div class="container m-3">
            <div class="row row-cols-auto">
                    <p class="px-1 fw-bold">화장품 산업 정보</p>
                    <a class="nav-link active mt-1 px-2" @click="setTokenBeauty" aria-current="page" style="font-size: 0.8rem; font-weight: 550;" href="/archive/total"><span class="material-symbols-outlined" style="font-size:0.8rem">description</span>더 많은 정보 보기 〉</a>
            </div>
            <div class="container text-start justify-content-between">
                <div class="row">
                    <div  v-for="beautyNews in contentBeauty" :key="beautyNews.title" class="col-12 col-sm-6 col-md-4 col-lg-2">
                        <div class="col">
                          <NewsItem
                            :title="beautyNews.title"
                            :imgUrl="beautyNews.imgUrl"
                            :newsLink="beautyNews.newsLink"
                            :uploadDate="beautyNews.uploadDate"
                            :openModal="openModal"
                          />
                    </div>
                    </div>
                </div>
            </div> 
        </div>
    </div>
    <CarGraph :salesData="[65, 59, 80, 81, 56, 55, 40]" />
    <NewsModal :title="title" :imgUrl="imgUrl" :newsLink="newsLink"></NewsModal>
</template>

<style scoped>
.title {
  padding: 20px;
}
.nav-link.active {
    color: #6D6D6D;
}
.nav-link.active:hover {
    color: #7248BD;
}
  
.nav-link.active:hover .material-symbols-outlined {
    color: #7248BD; 
}
</style>