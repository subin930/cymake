<script setup>
import { RouterLink, RouterView, useRoute, useRouter } from 'vue-router'
import Header from '@/components/common/Header.vue'
import NewsModal from '@/components/common/NewsModal.vue';
import NewsItem from '@/components/common/NewsItem.vue';
import { ref, onMounted, watch } from "vue";
import axios from 'axios';
// props 정의
defineProps({
  carPage: {
    type: Number,
    default: 1
  },
  beautyPage: {
    type: Number,
    default: 1
  },
  searchBody: {
    type: String,
    default: ''
  }
});
const route = useRoute();
const router = useRouter();
const token = localStorage.getItem("token");
const loading = ref(false); // 로딩 상태

const contentToken = ref(localStorage.getItem("contentToken"));
const contentCar = ref([]);
const contentBeauty = ref([]);
const subject = ref('');

const currentCarNews = ref([]);
const currentBeautyNews = ref([]);
const itemsPerPage = 30; // 한 페이지에 표시할 아이템 수
const currentCarPage = ref(parseInt(route.query.carPage) || 1);
const totalCarPages = ref(1);
const currentBeautyPage = ref(parseInt(route.query.beautyPage) || 1);
const totalBeautyPages = ref(1);

const searchBody = ref(localStorage.getItem("searchBody") || route.query.searchBody || "");
const searchResults = ref([]);

const handleSearch = async () => {
    if (searchBody.value && searchBody.value.trim() !== '') {
        loading.value = true;
        localStorage.setItem("searchBody", searchBody.value);
        
        const query = {
            searchBody: searchBody.value,
        };

        if (subject.value === "car") {
            query.carPage = currentCarPage.value; // 자동차 뉴스일 때만 carPage 추가
        } else if (subject.value === "beauty") {
            query.beautyPage = currentBeautyPage.value; // 화장품 뉴스일 때만 beautyPage 추가
        }

        await router.push({ query });
        //await router.push({ 
        //    query: { 
        //        searchBody: searchBody.value, 
        //        carPage: currentCarPage.value,
        //        beautyPage: currentBeautyPage.value 
        //    } 
        //});
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
            if (subject.value === "car") {
                contentCar.value = searchResults.value;
                totalCarPages.value = Math.ceil(contentCar.value.length / itemsPerPage);
                currentCarPage.value = 1;
            } else {
                contentBeauty.value = searchResults.value;
                totalBeautyPages.value = Math.ceil(contentBeauty.value.length / itemsPerPage);
                currentBeautyPage.value = 1;
            }
            paginateNews();
        } catch (error) {
            console.error('Error fetching search results:', error);
        } finally {
            loading.value = false;
        }
    } else {
        fetchCarNews();
        fetchBeautyNews();
        setSubject();
    }
};

const fetchCarNews = async () => {
    subject.value = "car";
    loading.value = true;
    try {
        const response = await axios.get(`/v1/archive/total/${subject.value}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        contentCar.value = response.data.content;
        totalCarPages.value = Math.ceil(contentCar.value.length / itemsPerPage);
        paginateNews();
    } catch (error) {
        console.error('Error fetching data:', error);
    } finally {
        loading.value = false;
    }
};

const fetchBeautyNews = async () => {
    subject.value = "beauty";
    loading.value = true;
    try {
        const response = await axios.get(`/v1/archive/total/${subject.value}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        contentBeauty.value = response.data.content;
        totalBeautyPages.value = Math.ceil(contentBeauty.value.length / itemsPerPage);
        paginateNews();
    } catch (error) {
        console.error('Error fetching data:', error);
    } finally {
        loading.value = false;
    }
};

const paginateNews = () => {
    if(subject.value === "car") {
        const start = (currentCarPage.value - 1) * itemsPerPage;
        const end = start + itemsPerPage;
        currentCarNews.value = contentCar.value.slice(start, end);
    } else {
        const start = (currentBeautyPage.value - 1) * itemsPerPage;
        const end = start + itemsPerPage;
        currentBeautyNews.value = contentBeauty.value.slice(start, end);
    }
};

const changePage = (step) => {
    scrollToTop(); 
    if(subject.value === "car") {
        currentCarPage.value += step;
        router.push({ 
        query: { 
            ...route.query, 
            carPage: currentCarPage.value, 
            beautyPage: undefined
        } 
    });
    } else {
        currentBeautyPage.value += step;
        router.push({ 
        query: { 
            ...route.query, 
            carPage: undefined,
            beautyPage: currentBeautyPage.value 
        } 
    });
    }
    //router.push({ 
    //    query: { 
    //        ...route.query, 
    //        carPage: currentCarPage.value, 
    //        beautyPage: currentBeautyPage.value 
    //    } 
    //});
    paginateNews();
};

watch(route, (newRoute, oldRoute) => {
    const newSearchBody = newRoute.query?.searchBody || ''; // 안전 접근
    const newCarPage = parseInt(newRoute.query?.carPage) || 1;
    const newBeautyPage = parseInt(newRoute.query?.beautyPage) || 1;

    // 검색어 및 페이지 번호 업데이트
    searchBody.value = newSearchBody;
    currentCarPage.value = newCarPage;
    currentBeautyPage.value = newBeautyPage;

     // 페이지 변경 처리
     if (newCarPage !== currentCarPage.value || newBeautyPage !== currentBeautyPage.value) {
        paginateNews(); // 페이지네이션 동기화
    }

    console.log("watching U");
    // 주제 변경 시 처리 (query가 없을 때도 안전 처리)
    //if (oldRoute?.query?.carPage && !newRoute.query?.carPage) {
    //    console.log("Changed to Beauty");
    //    contentToken.value = '1'; // carPage에서 beautyPage로 이동
    //    setSubject(); // beauty 데이터 로드
    //} else if (oldRoute?.query?.beautyPage && !newRoute.query?.beautyPage) {
    //    console.log("CHanged to Car");
    //    contentToken.value = '0'; // beautyPage에서 carPage로 이동
    //    setSubject(); // car 데이터 로드
    //}
    // 주제가 변경될 때만 contentToken 업데이트 및 setSubject 호출
    if (newRoute.query?.carPage && contentToken.value !== '0') {
        console.log("CHanged to Car");
        contentToken.value = '0'; // 자동차 뉴스
        setSubject(); 
    } else if (newRoute.query?.beautyPage && contentToken.value !== '1') {
        console.log("Changed to Beauty");
        contentToken.value = '1'; // 화장품 뉴스
        setSubject();
    }

    // 검색어가 있으면 검색 수행
    if (searchBody.value.trim() !== '') {
        handleSearch();
    } else {
        paginateNews();
    }
}, { immediate: true });

const openModal = (titleVal, imgUrlVal, linkVal, summaryVal, keywordsVal) => {
    title.value = titleVal;
    imgUrl.value = imgUrlVal;
    newsLink.value = linkVal;
    summary.value = summaryVal;
    keywords.value = keywordsVal;
    const modalElement = document.getElementById("newsModal");
    const modalInstance = new bootstrap.Modal(modalElement);
    modalInstance.show();
};

const setSubject = () => {
    if (contentToken.value == '0') {
        subject.value = 'car';
    } else {
        subject.value = 'beauty';
    }

    scrollToTop(); 
    // URL에서 현재 주제에 해당하는 페이지 쿼리 파라미터 가져오기
    const currentPage = parseInt(route.query[`${subject.value}Page`]) || 1;
    if (searchBody.value && searchBody.value.trim() !== '') {
      router.push({
            query: {
                searchBody: searchBody.value,
                [`${subject.value}Page`]: currentPage // 현재 주제에 맞는 페이지 설정
            }
      });
      handleSearch();
    }
    else {
      router.push({
            query: {
                [`${subject.value}Page`]: currentPage // 현재 주제에 맞는 페이지 설정
            }
        });
    }
    paginateNews();
}

const scrollToTop = () => {
    window.scrollTo(0, 0);
};

const title = ref('no title');
const imgUrl = ref(null);
const newsLink = ref('no link');
const summary = ref([]);
const keywords = ref([]);

onMounted(() => {
    if (searchBody.value.trim() !== '') {
        handleSearch();
    } else {
        fetchCarNews();
        fetchBeautyNews();
        setSubject();
    }
    paginateNews();
});
</script>

<template>
    <Header></Header>
    <div class="container total-container justify-content-center">
        <div class="m-3 mt-4 text-center title">
            <p class="fw-bold fs-3">지식아카이브</p>
        </div>
        <div class="container m-3">
            <div class="row">
                <div class="btn-group" role="group" aria-label="Basic radio toggle button group">
                    <input type="radio" v-model="contentToken" class="btn-check col-6" name="btnradio" id="btnradio1" value="0" @change="setSubject">
                    <label autocomplete="off" class="btn fw-bold { 'active': contentToken === '0' }" for="btnradio1" 
                    style = "border: 1px solid #E3E3E3; --bs-btn-active-color: #f2f2f2; --bs-btn-active-bg: #7248BD; --bs-btn-bg:#FAFAFA; font-size:1.1rem; border-top-left-radius: 20px; border-top-right-radius: 20px; border-bottom-left-radius: 0px;"><span class="material-symbols-outlined" style="font-size:1.1rem">description</span>자동차 산업 정보</label>

                    <input type="radio" v-model="contentToken" class="btn-check col-6" name="btnradio" id="btnradio2" value="1" @change="setSubject">
                    <label autocomplete="off" class="btn fw-bold { 'active': contentToken === '1' }" for="btnradio2" 
                    style = "border: 1px solid #E3E3E3; --bs-btn-active-color: #f2f2f2; --bs-btn-active-bg: #7248BD; --bs-btn-bg:#FAFAFA; font-size:1.1rem; border-top-left-radius: 20px; border-top-right-radius: 20px; border-bottom-right-radius: 0px;"><span class="material-symbols-outlined" style="font-size:1.1rem">description</span>화장품 산업 정보</label>
                </div>
            </div>
            <div class="row d-flex align-items-center">
                <form @submit.prevent="handleSearch" class="input-group mt-3 mb-3" role="search">
                    <input type="search" class="form-control" placeholder="Please input" aria-label="Search"
                    v-model="searchBody">
                    <button type="button" class="btn btn-outline-secondary"
                    style="border-color:#D6D8DB" @click="handleSearch"><i class="bi bi-search"></i></button>
                </form>

            </div>
            <div v-if="loading" class="text-center my-4">
              <div class="spinner-border text-secondary" role="status">
                <span class="visually-hidden">Loading...</span>
              </div>
            </div>
            <div v-else class="container news-container text-start justify-content-between">
                <div v-if="contentToken =='0'" class="row g-3">
                    <div v-for="carNews in currentCarNews" :key="carNews.title" class="col-12 col-sm-6 col-md-4 col-lg-2">
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
                    <div class="mt-3 container d-inline-flex pagination-controls justify-content-center">
                      <button class="btn btn-outline-secondary me-3" style="font-size: .7rem;"
                      @click="changePage(-1)" :disabled="currentCarPage <= 1">이전</button>
                      <span>{{ currentCarPage }} / {{ totalCarPages }}</span>
                      <button class="btn btn-outline-secondary ms-3" style="font-size: .7rem;"@click="changePage(1)" :disabled="currentCarPage >= totalCarPages">다음</button>
                    </div>
                </div>
                <div v-if="contentToken =='1'" class="row g-3">
                    <div v-for="beautyNews in currentBeautyNews" :key="beautyNews.title" class="col-12 col-sm-6 col-md-4 col-lg-2">
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
                    <div class="mt-3 container d-inline-flex pagination-controls justify-content-center">
                      <button class="btn btn-outline-secondary me-3" style="font-size: .7rem;"@click="changePage(-1)" :disabled="currentBeautyPage <= 1">이전</button>
                      <span>{{ currentBeautyPage }} / {{ totalBeautyPages }}</span>
                      <button class="btn btn-outline-secondary ms-3" style="font-size: .7rem;"@click="changePage(1)" :disabled="currentBeautyPage >= totalBeautyPages">다음</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <NewsModal :title="title" :imgUrl="imgUrl" :newsLink="newsLink" :summary="summary" :keywords="keywords"></NewsModal>
</template>

<style scoped>
.title {
  padding: 20px;
}
input:focus {
  outline: none;
  border-color: #7248BD;
  box-shadow: 0 0 0 0 rgba(114, 72, 189, 0.25);
}
.btn-group {
  border-color: #D6D8DB;
}
.btn-check {
  border-top-left-radius: 20px !important;
  border-top-right-radius: 20px !important;
  border-bottom-left-radius: 0px !important; 
  border-bottom-right-radius: 0px !important;
}
.form-control::placeholder {
  opacity: .5;
}
</style>