<script setup>
import { RouterLink, RouterView, useRoute, useRouter } from 'vue-router'
import Header from '@/components/common/Header.vue'
import NewsModal from '@/components/common/NewsModal.vue';
import NewsItem from '@/components/common/NewsItem.vue';
import { ref, onMounted, watch } from "vue";
import axios from 'axios';

const route = useRoute();
const router = useRouter();
const contentToken = ref(localStorage.getItem("contentToken"));
const token = localStorage.getItem("token");
const contentCar = ref([]);
const contentBeauty = ref([]);
const subject = ref('');

const searchBody = ref(localStorage.getItem("searchBody"));
const searchResults = ref([]);

const handleSearch = async () => {
    console.log(searchBody.value);
    if (searchBody.value.length > 0) { // 최소 1글자 이상일 때 검색
        try {
            const response = await axios.get(`/v1/archive/total/${subject.value}/search`, {
                params: {
                    searchBody: searchBody.value
                },
                headers: {
                'Authorization': `Bearer ${token}`
                }
            });
            searchResults.value = response.data.content;
            console.log(response.data.content);
            if (subject.value === "car"){
              console.log("searched Car");
              contentCar.value = searchResults.value;
            } else {
              console.log("searched Beauty");
              contentBeauty.value = searchResults.value;
            }
        } catch (error) {
            console.error('Error fetching search results:', error);
        }
    } else {
        searchResults.value = []; // 검색어가 1글자 미만일 경우 결과 초기화
    }
};

const fetchCarNews = async () => {
  subject.value = "car";
  try {
    const response = await axios.get(`/v1/archive/total/${subject.value}`, {
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
    const response = await axios.get(`/v1/archive/total/${subject.value}`, {
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

const setSubject = () => {
  console.log(contentToken.value);
  if (contentToken.value =='0') {
    subject.value = 'car';
    console.log("set subject car");
  }
  else {
    subject.value = 'beauty';
    console.log("set subject beauty");
  }
  console.log(subject.value);

  if(searchBody.value !== null) {
    console.log('has SearchBody: '+searchBody.value);
    handleSearch();
  }
}

//통합 검색에서 더 많은 결과 보기로 넘어온 경우 검색 결과가 바로 뜨도록 하는 함수
const checkSearch = () => {
  if(searchBody.value !== null) {
    console.log('has SearchBody: '+searchBody.value);
    handleSearch();
    localStorage.removeItem("searchBody");
  }
  else {
    console.log('no SearchBody');
    fetchCarNews();
    fetchBeautyNews();
  }
}
const title = ref('no title');
const imgUrl = ref(null);
const newsLink = ref('no link');

onMounted(setSubject);
onMounted(checkSearch);
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
                    <input type="radio" v-model="contentToken" class="btn-check col-6" name="btnradio" id="btnradio1" value="0" @change="setSubject">
                    <label autocomplete="off" class="btn fw-bold { 'active': contentToken === '0' }" for="btnradio1" style = "--bs-btn-active-color: #FFFFFF; --bs-btn-active-bg: #7248BD; --bs-btn-bg: #F5F6FA; font-size:1.1rem;"><span class="material-symbols-outlined" style="font-size:1.1rem">description</span>자동차 산업 정보</label>

                    <input type="radio" v-model="contentToken" class="btn-check col-6" name="btnradio" id="btnradio2" value="1" @change="setSubject">
                    <label autocomplete="off" class="btn fw-bold { 'active': contentToken === '1' }" for="btnradio2" style = "--bs-btn-active-color: #FFFFFF; --bs-btn-active-bg: #7248BD; --bs-btn-bg: #F5F6FA; font-size:1.1rem;"><span class="material-symbols-outlined" style="font-size:1.1rem">description</span>화장품 산업 정보</label>
                </div>
            </div>
            <div class="row d-flex align-items-center">
                <form @submit.prevent="handleSearch" class="input-group mt-3 mb-3" role="search">
                    <input type="search" class="form-control" placeholder="Please input" aria-label="Search"
                    v-model="searchBody">
                    <button type="button" class="btn btn-outline-secondary" @click="handleSearch">검색</button>
                </form>

            </div>
            <div class="container text-start justify-content-between">
                <div v-if="contentToken =='0'" class="row g-3">
                    <div v-for="carNews in contentCar" :key="carNews.title" class="col-12 col-sm-6 col-md-4 col-lg-2">
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
                <div v-if="contentToken =='1'" class="row g-3">
                    <div v-for="beautyNews in contentBeauty" :key="beautyNews.title" class="col-12 col-sm-6 col-md-4 col-lg-2">
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
    <NewsModal :title="title" :imgUrl="imgUrl" :newsLink="newsLink"></NewsModal>
</template>