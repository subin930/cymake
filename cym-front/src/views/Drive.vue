<script setup>
import { RouterLink, RouterView, useRoute, useRouter } from 'vue-router'
import Header from '@/components/common/Header.vue'
import FileModifyBtn from '@/components/Drive/FileModifyBtn.vue'
import FileDeleteBtn from '@/components/Drive/FileDeleteBtn.vue'
import FileUploadBtn from '@/components/Drive/FileUploadBtn.vue'

import { ref, onMounted, computed, watch } from "vue";
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
const searchBody = ref(localStorage.getItem("searchBody") || route.query.searchBody || "");
const searchResults = ref([]);
const totalSize = ref(0);
const usagePercentage = ref(0);
const loading = ref(false); // 로딩 상태 관리
const maxUsage = ref(parseFloat(localStorage.getItem("usage")) || 0);
const handleSearch = async () => {
    if (searchBody.value && searchBody.value.trim() !== '') { // 최소 1글자 이상일 때 검색
      loading.value = true;  
      console.log(searchBody.value);
      try {
            await router.push(`/drive/${searchBody.value}`);
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
          alert(error.response.message);
          console.error('Error fetching search results:', error);
        } finally {
        loading.value = false; // 검색 완료 후 로딩 상태 false
        }
    } else {
      console.log('no searchBody!!');
      // 검색어 없으면 목록 초기화
      await router.push('/drive');
      searchResults.value = []; // 검색어가 1글자 미만일 경우 결과 초기화
      fetchData();
    } 
};
const updateFile = async({fileId, newFileData }) => {
  console.log('updatefile');
  console.log(fileId.value);
  console.log(newFileData);
  const index = content.value.findIndex(item => item.fileId === fileId.value);
  console.log(fileId.value);
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

const removeFile = async(fileId) => {
  //content.value = content.value.filter(item => item.fileId !== fileId.value);
  const removedFile = content.value.find(item => item.fileId === fileId.value);
  //현재 사용량도 수정해야함
  if (removedFile) {
    console.log(`Removing file: ${removedFile.fileName}`);
    // 현재 사용량 수정
    totalSize.value = (parseFloat(totalSize.value) - parseFloat(removedFile.size)).toFixed(3);
    setUsagePercentage();

    // 바로 파일 목록에서 제거
    content.value = content.value.filter(item => item.fileId !== fileId.value);
  } else {
    console.error('File not found');
  }
};

const fetchData = async () => {
  loading.value = true;
  try {
    const response = await axios.get(`/v1/drive/list`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }});
    content.value = response.data.content;
    setTotalSize();
    setUsagePercentage();
  } catch (error) {
    alert(error.response.message);
    console.error('Error fetching data:', error);
  } finally {
    loading.value = false; // 데이터 가져오기 완료 후 로딩 상태 false
  }
};

const checkSearch = () => {
  console.log(localStorage.getItem("usage"));
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

watch(() => route.params.searchBody, (newSearchBody) => {
  searchBody.value = newSearchBody || ''; // 검색어 업데이트
  if (searchBody.value) {
    handleSearch();
  } else {
    fetchData();
  }
});
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
  const max = parseFloat(maxUsage.value);

  console.log("Parsed totalSize:", size);  // 여기서 size가 올바른 숫자인지 확인
  console.log("Parsed maxUsage:", max);    // 여기서 max가 올바른 숫자인지 확인

  if (!isNaN(size) && !isNaN(max) && max > 0) {
    usagePercentage.value = ((size / max) * 100).toFixed(2);
  } else {
    usagePercentage.value = 100;  // 계산 불가능할 경우 0%로 설정
  }

  console.log("Usage Percentage:", usagePercentage.value);
};

const downloadFile = async (fileId, fileName) => {
    try {
        const response = await axios({
            url: `/v1/drive/download?fileId=${encodeURIComponent(fileId)}`, //todo: URL을 /v1/drive/download로 수정
            method: 'GET',
            responseType: 'blob',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        const blob = new Blob([response.data]);
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', fileName);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    } catch (error) {
      if(error.response.data.message !== null) {
        alert(error.response.data.message)
      }
      alert("글 수정에 실패하였습니다.");
      console.error('Error downloading file:', error);
    }
};

</script>

<template>
    <Header></Header>
    <div class="container-fluid">        
        <div class="m-1 title">
            <p class="fw-bold fs-3">통합 자료실</p>
        </div>
        <div class="row d-flex align-items-center justify-content-between m-3"  style="border-top-width: 1px; border-top-style: solid; border-top-color: #DFE0E0;border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: #DFE0E0;">
            <!-- Form element -->
            <form @submit.prevent="handleSearch" class="input-group col-md-6 col-lg-4 mt-3 mb-3" role="search" style="width: 300px;">
              <input type="search" class="form-control" placeholder="Search..." aria-label="Search" style="font-size: .8rem;"
              v-model="searchBody">
              <button type="button" class="btn btn-outline-secondary search-btn" 
              style="border-color:#D6D8DB; font-size: .8rem;" @click="handleSearch">검색</button>
            </form>

            <!-- FileUploadBtn element -->
            <div class="col-md-6 col-lg-4 d-flex mt-3 mb-3 mx-6 justify-content-end">
                <FileUploadBtn @fileUploaded="fetchData"/>
                
            </div>
        </div>
        <div class="container d-flex justify-content-start align-items-center ms-4 me-4" style="background-color: #EAECF0; padding: 15px 0; border-radius: 10px;">
          <p class="px-3" style="font-size: .8rem; margin-bottom: 0;">자료실 사용량</p>
          <div class="progress col-3" style="border-radius: 20px; vertical-align: middle; background-color:#DDDDDD;" role="progressbar" aria-label="통합 자료실 사용량" aria-valuenow="usagePercentage" aria-valuemin="0" aria-valuemax="100">
            <div class="progress-bar" :style=" { width: usagePercentage + '%',  backgroundColor: '#7248BD'}"></div>
            <p class="px-1" style="font-size: .7rem; font-weight: bold; color:#FFFFFF">{{ usagePercentage }}%</p>
          </div>
          <p class="px-3" style="font-size: .8rem; margin-bottom: 0;">{{ totalSize }} MB / {{ maxUsage }} MB</p>
        </div>
        <div v-if="loading" class="text-center my-4">
            <div class="spinner-border text-secondary" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
        </div>
        <div v-else class="container table-wrapper m-3 mt-4 d-flex">
            <table class="table table-hover table-bordered">
                <thead class="table-head">
                    <tr class="table-light">
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
                  <tr v-for="(item, index) in content" :key="item.uploader">
                    <td>{{ index+1 }}</td>
                    <td>{{ item.postTitle }}</td>
                    <td><a @click.prevent="downloadFile(item.fileId, item.fileName)" class="download-link"><i class="bi bi-download px-1"></i>{{ item.fileName }}</a></td>
                    <td>{{ item.size }}MB</td>
                    <td>{{  item.uploader  }}</td>
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
.table tbody tr:hover td, .table tbody tr:hover th {
  background-color: rgb(254, 254, 254);
}
.title {
  padding-top: 20px;
  padding-left: 20px;
}
.table {
  font-size: .7rem;
  vertical-align: middle;
  background-color:white;
}
.table-head {
  background-color:#FAFAFA;
}
.table-wrapper {
    overflow-y: scroll;
    max-height: 60vh;
}
.download-link {
  color: inherit; /* 기본 텍스트 색상 사용 */
  text-decoration: none; /* 밑줄 제거 */
  cursor: pointer;
}
.download-link:hover {
  color: #7248BD;
}
.form-control::placeholder {
  opacity: .5;
}
.search-btn:hover {
  background-color: darkgray;
}
input:focus {
  outline: none;
  border-color: #7248BD;
  box-shadow: 0 0 0 0 rgba(114, 72, 189, 0.25);
}
</style>