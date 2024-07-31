<script setup>
//검색 결과창 띄우는 화면 (헤더에 있는 total search 기능 사용 시)
import { RouterLink, RouterView, useRoute, useRouter } from 'vue-router'
import Header from '@/components/common/Header.vue'
import NewsModal from '@/components/common/NewsModal.vue';
import NewsItem from '@/components/common/NewsItem.vue';
import { ref, onMounted, watch, computed } from "vue";
import axios from 'axios'

const token = localStorage.getItem("token");
const route = useRoute();
const searchBody = ref(route.params.searchBody);
const numTotal = ref(0);
const numCar = ref(0);
const numBeauty = ref(0);
const numDrive = ref(0);
//Archive
const title = ref('no title');
const imgUrl = ref(null);
const newsLink = ref('no link');
const contentCar = ref([]);
const contentBeauty = ref([]);
//Drive
const content = ref([]);

const handleSearch = async () => {
    console.log(searchBody.value);
    if (searchBody.value.length > 0) { // 최소 1글자 이상일 때 검색
        try {
            const response = await axios.get(`/v1/total/search`, {
                params: {
                    searchBody: searchBody.value
                },
                headers: {
                'Authorization': `Bearer ${token}`
                }
            });
            contentCar.value = response.data.content.archiveCarSearchResult;
            contentBeauty.value = response.data.content.archiveBeautySearchResult;
            content.value = response.data.content.driveSearchResult;
            numCar.value = response.data.content.numCar;
            numBeauty.value = response.data.content.numBeauty;
            numDrive.value = response.data.content.numDrive;
            numTotal.value = numCar.value + numBeauty.value + numDrive.value;
            console.log(response.data.content);
        } catch (error) {
            console.error('Error fetching search results:', error);
        }
    } //else {
        //searchResults.value = []; // 검색어가 1글자 미만일 경우 결과 초기화
    //}
};

//Archive
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
    localStorage.setItem("searchBody", searchBody.value);
}
const setTokenBeauty = () => {
    localStorage.setItem("contentToken", 1);
    localStorage.setItem("searchBody", searchBody.value);
}

//Drive
const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' };
    return new Date(dateString).toLocaleDateString(undefined, options);
};
const setDriveSearch = () => {
    localStorage.setItem("searchBody", searchBody.value);
}
onMounted(handleSearch);
watch(() => route.params.searchBody, (newSearchBody) => {
  searchBody.value = newSearchBody;
  handleSearch(newSearchBody);
});
</script>

<template>
    <Header></Header>
    <div class="container-fluid m-3">
        <div class="m-3 mt-4 text-center">
            <p class="fw-bold fs-3"><span style = "color: #7248BD;">"{{ searchBody }}"</span>에 대한 검색 결과</p>
            <p><span style = "color: #7248BD;">{{ numTotal }}</span> 개의 검색 결과가 있습니다.</p>
        </div>
        <div>
            <div class="row justify-content-between">
                <p class="col-6 px-1 fw-bold">자동차 산업 정보    <span style = "color: #7248BD;">{{ numCar }}</span></p>
                <a class="col-2 nav-link active mt-1 px-2" @click="setTokenCar" aria-current="page" style="font-size: 0.8rem; font-weight: 550;" href="/archive/total"><span class="material-symbols-outlined" style="font-size:0.8rem">description</span>더 많은 결과 보기 〉</a>
            </div>
            <div class="container text-center justify-content-between mb-3">
                <div class="row g-3">
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
            </div> 
            <div class="row justify-content-between">
                <p class="col-6 px-1 fw-bold">화장품 산업 정보  <span style = "color: #7248BD;">{{ numBeauty }}</span></p>
                <a class="col-2 nav-link active mt-1 px-2" @click="setTokenBeauty" aria-current="page" style="font-size: 0.8rem; font-weight: 550;" href="/archive/total"><span class="material-symbols-outlined" style="font-size:0.8rem">description</span>더 많은 결과 보기 〉</a>
            </div>
            <div class="container text-start justify-content-between mb-3">
                <div class="row g-3">
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
            <div class="row justify-content-between">
                <p class="col-6 px-1 fw-bold">통합 자료실  <span style = "color: #7248BD;">{{ numDrive }}</span></p> 
                <a class="col-2 nav-link active mt-1 px-2" @click="setDriveSearch" aria-current="page" style="font-size: 0.8rem; font-weight: 550;" href="/drive"><span class="material-symbols-outlined" style="font-size:0.8rem">description</span>더 많은 결과 보기 〉</a>
            </div>
            <div class="container drive-container m-3 d-flex">
                <table class="table table-hover table-bordered">
                    <thead class="table-head">
                        <tr>
                        <th scope="col"> </th>
                        <th scope="col">제목</th>
                        <th scope="col">파일명</th>
                        <th scope="col">파일 크기</th>
                        <th scope="col">업로더 아이디</th>
                        <th scope="col">등록자</th>
                        <th scope="col">등록일시</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(item, index) in content" :key="item.id">
                            <td>{{ index+1 }}</td>
                            <td>{{ item.postTitle }}</td>
                            <td><a :href="item.fileUrl" class="download-link"><i class="bi bi-download px-1"></i>{{ item.fileName }}</a></td>
                            <td>{{ item.size }}MB</td>
                            <td>{{  item.id  }}</td>
                            <td>{{ item.username }}</td>
                            <td>{{ formatDate(item.uploadDate) }}</td>
                        </tr>
                    </tbody>
                </table>
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

.table {
    font-size: .8rem;
}
.drive-container {
    overflow-y: scroll;
    max-height: 60vh;
}
.download-link {
  color: inherit; /* 기본 텍스트 색상 사용 */
  text-decoration: none; /* 밑줄 제거 */
}
.download-link:hover {
  color: #7248BD;
}
</style>