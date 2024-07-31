<script setup>
import { RouterLink, RouterView, useRoute, useRouter } from 'vue-router'
import Header from '@/components/common/Header.vue'
import FileModifyBtn from '@/components/Drive/FileModifyBtn.vue'
import FileDeleteBtn from '@/components/Drive/FileDeleteBtn.vue'
import FileUploadBtn from '@/components/Drive/FileUploadBtn.vue'

import { ref, onMounted, computed } from "vue";
import axios from 'axios'

const token = localStorage.getItem("token");
const route = useRoute();
const router = useRouter();
/*const content = ref([
  {
    "fileName": "example.txt",
    "postTitle": "Sample Title",
    "id": "1",
    "username": "john_doe",
    "uploadDate": "2024-07-23T04:35:18.513Z"
  }
);*/
const content = ref([]);
const searchBody = ref(localStorage.getItem("searchBody"));
const searchResults = ref([]);
const totalSize = ref(0);
const usagePercentage = ref(0);

const handleSearch = async () => {
    console.log(searchBody.value);
    if (searchBody.value && searchBody.value.trim() !== '') { // 최소 1글자 이상일 때 검색
        try {
            const response = await axios.get('/v1/drive/search', {
                params: {
                    searchBody: searchBody.value
                },
                headers: {
                'Authorization': `Bearer ${token}`
                }
            });
            searchResults.value = response.data.content;
            content.value = searchResults.value;
        } catch (error) {
            console.error('Error fetching search results:', error);
        }
    } else {
        searchResults.value = []; // 검색어가 1글자 미만일 경우 결과 초기화
        fetchData();
    }
};
const updateFile = async({ originalFileName, newFileData }) => {
  const index = content.value.findIndex(item => item.fileName === originalFileName.value);
  console.log(originalFileName.value);
  if (index !== -1) {
    // Create a new object to trigger reactivity
    content.value[index] = {
      ...content.value[index],
      postTitle: newFileData.postTitle,
      fileName: newFileData.fileName,
      size: newFileData.size
    };
    console.log(content.value[index]);
  } else {
    console.error('File not found');
  }
};

const removeFile = async(originalFileName) => {
  content.value = content.value.filter(item => item.fileName !== originalFileName.value);
};

const fetchData = async () => {
  try {
    const response = await axios.get(`/v1/drive/list`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }});
    content.value = response.data.content;
    setTotalSize();
    setUsagePercentage();
  } catch (error) {
    console.error('Error fetching data:', error);
  }
};

const checkSearch = () => {
  if(searchBody.value && searchBody.value.trim() !== '') {
    console.log('has SearchBody: '+searchBody.value);
    handleSearch();
    localStorage.removeItem("searchBody");
  }
  else {
    console.log('no SearchBody');
    fetchData();
  }
}
onMounted(checkSearch);

const formatDate = (dateString) => {
  const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' };
  return new Date(dateString).toLocaleDateString(undefined, options);
};

const setTotalSize = () => {
  totalSize.value = content.value.reduce((total, item) => total + item.size, 0).toFixed(3);
  console.log(totalSize.value);
};

const setUsagePercentage = () => {
  const size = parseFloat(totalSize.value);
  usagePercentage.value = ((size / 10) * 100).toFixed(2); //데이터가 적은 상태이기에 UI 확인 위해 임의로 10MB로 한계 설정
  console.log(usagePercentage.value);
};
</script>

<template>
    <Header></Header>
    <div class="container-fluid">        
        <div class="m-3 mt-4">
            <p class="fw-bold fs-3">통합 자료실</p>
        </div>
        <div class="row d-flex align-items-center justify-content-between m-3">
            <!-- Form element -->
            <form @submit.prevent="handleSearch" class="input-group col-md-6 col-lg-4 mt-3 mb-3" role="search" style="width: 300px;">
              <input type="search" class="form-control" placeholder="Search..." aria-label="Search"
              v-model="searchBody">
              <button type="button" class="btn btn-outline-secondary" @click="handleSearch">검색</button>
            </form>

            <!-- FileUploadBtn element -->
            <div class="col-md-6 col-lg-4 d-flex mt-3 mb-3 mx-6 justify-content-end">
                <FileUploadBtn @fileUploaded="fetchData"/>
                
            </div>
        </div>
        <div class="justify-content-start m-3">
          <div class="row-col-4 d-flex align-items-center m-3">
          <p class="col-1" style="font-size: .8rem; margin-bottom: 0;">자료실 사용량</p>
          <p class="col-3 text-end" style="font-size: .8rem; margin-bottom: 0;">{{ totalSize }} MB / 10 MB</p>
          </div>
          <div class="progress col-4 m-3" role="progressbar" aria-label="통합 자료실 사용량" aria-valuenow="usagePercentage" aria-valuemin="0" aria-valuemax="100">
            <div class="progress-bar" :style="{ width: usagePercentage + '%',  backgroundColor: '#7248BD'}"></div>
            <p class="px-1" style="font-size: .8rem;">{{ usagePercentage }}%</p>
          </div>
        </div>
        <div class="container m-3 d-flex">
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
                    <th scope="col">수정</th>
                    <th scope="col">삭제</th>
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
                        <td><FileModifyBtn :file="item" @fileModified="updateFile"/></td>
                        <td><FileDeleteBtn :file="item" @fileDeleted="removeFile"/></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
    
</template>

<style scoped>
.table {
    font-size: .8rem;
}
.container {
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