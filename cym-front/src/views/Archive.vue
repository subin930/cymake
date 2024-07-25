<script setup>
import { RouterLink, RouterView, useRoute, useRouter } from 'vue-router'
import Header from '@/components/common/Header.vue'
import NewsModal from '@/components/common/NewsModal.vue';
import { ref, onMounted } from "vue";
import axios from 'axios'

const route = useRoute();
const router = useRouter();
const token = localStorage.getItem("token");
const contentCar = ref([]);
const contentBeauty = ref([]);
const subject = ref('');
/*const testfun = () => {
    console.log("testfun played");
    router.push("/signup");
};*/
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

const title = ref('no title');
const imgUrl = ref(null);
const newsLink = ref('no link');

onMounted(fetchCarNews);
onMounted(fetchBeautyNews);
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
                <div class="row g-3">
                    <div  v-for="carNews in contentCar" :key="carNews.title" class="col-12 col-sm-6 col-md-4 col-lg-2">
                        <div class="col">
                        <button type="button" 
                        class="btn news-btn" 
                        style="font-size: .8rem; font-weight: bold"
                        @click="openModal(carNews.title, carNews.imgUrl, carNews.newsLink)">
                        <img :src="carNews.imgUrl" alt="news image" class="news-image">
                        <br/>
                            {{ carNews.title }}
                        <br/>
                        <p style="font-size: .6rem; font-weight:normal">{{ carNews.uploadDate }}</p>
                        </button>
                    </div>
                    </div>
                </div>
            </div> 
        </div>
        <div class="container m-3">
            <div class="row row-cols-auto">
                    <p class="px-1 fw-bold">화장품 산업 정보</p>
                    <a class="nav-link active mt-1 px-2" aria-current="page" style="font-size: 0.8rem; font-weight: 550;" href="/archive/total"><span class="material-symbols-outlined" style="font-size:0.8rem">description</span>더 많은 정보 보기 〉</a>
            </div>
            <div class="container text-start justify-content-between">
                <div class="row g-3">
                    <div  v-for="beautyNews in contentBeauty" :key="beautyNews.title" class="col-12 col-sm-6 col-md-4 col-lg-2">
                        <div class="col">
                        <button type="button" 
                        class="btn news-btn" 
                        style="font-size: .8rem; font-weight: bold"
                        @click="openModal(beautyNews.title, beautyNews.imgUrl, beautyNews.newsLink)">
                        <img :src="beautyNews.imgUrl" alt="news image" class="news-image">
                        <br/>
                            {{ beautyNews.title }}
                        <br/>
                        <p style="font-size: .6rem; font-weight:normal">{{ beautyNews.uploadDate }}</p>
                        </button>
                    </div>
                    </div>
                </div>
            </div> 
        </div>
    </div>
    
    <NewsModal :title="title" :imgUrl="imgUrl" :newsLink="newsLink"></NewsModal>
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
.news-btn {
  display: flex;
  flex-direction: column;
  margin-bottom: 10px;
  width: 12rem; /* Set the width of the image */
  height: 20rem;
  text-overflow: ellipsis;
}
.news-image {
  width: 10rem; /* Set the width of the image */
  height: 12rem; /* Set the height of the image */
}
</style>