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

const carLoading = ref(false);
const beautyLoading = ref(false);

const crwlInfoCar = ref([]);
const crwlInfoBeauty = ref([]);
const crwlData = ref([]);
const titleCar = ref('자동차 일일 크롤링 수');
const titleBeauty = ref('화장품 일일 크롤링 수');

const title = ref('no title');
const imgUrl = ref(null);
const newsLink = ref('no link');
const summary = ref([]);
const keywords = ref([]);

const fetchCarNews = async () => {
  subject.value = "car";
  carLoading.value = true;
  try {
    const response = await axios.get(`/v1/archive/${subject.value}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }});
    contentCar.value = response.data.content;
  } catch (error) {
    console.error('Error fetching data:', error);
  } finally {
    carLoading.value = false;
  }
};

const fetchBeautyNews = async () => {
  subject.value = "beauty";
  beautyLoading.value = true;
  try {
    const response = await axios.get(`/v1/archive/${subject.value}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }});
    contentBeauty.value = response.data.content;
  } catch (error) {
    console.error('Error fetching data:', error);
  } finally {
    beautyLoading.value = false;
  }
};

const openModal = (titleVal, imgUrlVal, linkVal, summaryVal, keywordsVal) => {
    console.log(summaryVal, keywordsVal); 
    title.value = titleVal;
    imgUrl.value = imgUrlVal;
    newsLink.value = linkVal;
    summary.value = summaryVal;
    keywords.value = keywordsVal;
    const modalElement = document.getElementById("newsModal");
    const modalInstance = new bootstrap.Modal(modalElement);
    modalInstance.show();
};

const setTokenCar = () => {
    localStorage.setItem("contentToken", 0); //car 토큰 설정
}

const setTokenBeauty = () => {
    localStorage.setItem("contentToken", 1); //beauty 토큰 설정
}


const fetchCrawlInfo = async() => {
  try {
    const response = await axios.get(`/v1/archive/crwlTotal`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }});
    crwlInfoCar.value = response.data.content.carCrwlData;
    crwlInfoBeauty.value = response.data.content.beautyCrwlData;
    console.log(crwlInfoCar.value);
    console.log(crwlInfoBeauty.value);

  } catch (error) {
    console.error('Error fetching crawl data:', error);
  }
}

onMounted(fetchCarNews);
onMounted(fetchBeautyNews);
onMounted(fetchCrawlInfo);
</script>

<template>
    <Header></Header>
    
    <div class="container-fluid justify-content-center">        
        <div class="m-3 text-center title">
            <p class="fw-bolder fs-3">지식아카이브</p>
        </div>
        <div class="container-fluid m-3 justify-content-center">
            <div class="row row-cols-auto">
                    <p class="text-start px-1 fw-bold">자동차 산업 정보</p>
                    <a class="nav-link active text-start mt-1 px-2" @click="setTokenCar" aria-current="page" style="font-size: 0.8rem; font-weight: 550;" href="/archive/total?carPage=1"><span class="material-symbols-outlined" style="font-size:0.8rem">description</span>더 많은 정보 보기 〉</a>
            </div>
            <div v-if="carLoading" class="text-center my-4">
                      <div class="spinner-border text-secondary" role="status">
                        <span class="visually-hidden">Loading...</span>
                      </div>
            </div>
            <div v-else class="container">
                <div class="row g-3">
                    <div  v-for="carNews in contentCar" :key="carNews.title" class="col-12 col-sm-6 col-md-4 col-lg-2">
                        <div class="col">
                          <NewsItem
                            :title="carNews.title"
                            :imgUrl="carNews.imgUrl"
                            :keywords="carNews.keywords"
                            :summary="carNews.summary"
                            :newsLink="carNews.newsLink"
                            :uploadDate="carNews.uploadDate"
                            :openModal="openModal"
                          />
                      </div>
                    </div>
                </div>
            </div> 
        </div>
        <div class="container-fluid m-3">
            <div class="row row-cols-auto">
                    <p class="text-start px-1 fw-bold">화장품 산업 정보</p>
                    <a class="nav-link active text-start mt-1 px-2" @click="setTokenBeauty" aria-current="page" style="font-size: 0.8rem; font-weight: 550;" href="/archive/total?beautyPage=1"><span class="material-symbols-outlined" style="font-size:0.8rem">description</span>더 많은 정보 보기 〉</a>
            </div>
            <div v-if="beautyLoading" class="text-center my-4">
                      <div class="spinner-border text-secondary" role="status">
                        <span class="visually-hidden">Loading...</span>
                      </div>
            </div>
            <div v-else class="container">
                <div class="row">
                    <div  v-for="beautyNews in contentBeauty" :key="beautyNews.title" class="col-12 col-sm-6 col-md-4 col-lg-2">
                        <div class="col">
                          <NewsItem
                            :title="beautyNews.title"
                            :imgUrl="beautyNews.imgUrl"
                            :keywords="beautyNews.keywords"
                            :summary="beautyNews.summary"
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
    <div class="container-fluid m-3 row align-items-center justify-content-center">
        <div v-if="crwlInfoCar.length > 0" class="col">
            <CarGraph :crwlData="crwlInfoCar" :title="titleCar"></CarGraph>
        </div>
        <div v-if="crwlInfoBeauty.length > 0" class="col">
            <CarGraph :crwlData="crwlInfoBeauty" :title="titleBeauty"></CarGraph>
        </div>
    </div>

    <NewsModal :title="title" :imgUrl="imgUrl" :newsLink="newsLink" :summary="summary" :keywords="keywords"></NewsModal>
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