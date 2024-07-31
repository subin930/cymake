<script setup>
import { ref } from "vue";
import { useRoute, useRouter } from "vue-router";
const route = useRoute();
const router = useRouter();

const token = ref(localStorage.getItem("token"));
const username = ref(localStorage.getItem("username"));
const searchBody = ref('');

const totalSearch = async () => {
  console.log(searchBody.value);
  if(searchBody.value && searchBody.value.trim() !== ''){
    try {
      router.push(`/search/${searchBody.value}`);
    }
    catch (error) {
      console.error('Failed to navigate:', error);
    }
  }
  else {
    console.log('searchBody has no value')
  }
}
const Logout = () => {
  //다시 로그인창으로 돌아가는
  localStorage.clear();
  router.push("/login");
};

const Login = () => {
  router.push("/login");
};

const SignUp = () => {
  router.push("/signup");
};

</script>
  


<template>
    <header class="d-flex flex-wrap align-items-center justify-content-between px-3 py-2 border-bottom fixed-header" style="background-color: #FFFFFF;">
      <div class="d-flex align-items-center">
        <a href="/archive">
            <img src="@/assets/CYMAKE.jpg" style="height: 3rem;"/>
        </a>
        <ul class="nav ms-1">
          <li><a class="nav-link active archive-link" aria-current="page" style="font-size: 1rem; font-weight: 550;" href="/archive"><span class="material-symbols-outlined" style="font-size:1rem">description</span>지식아카이브</a></li>
          <li><a class="nav-link active drive-link" aria-current="page" style="font-size: 1rem; font-weight: 550;" href="/drive"><span class="material-symbols-outlined" style="font-size:1rem">attach_file</span>통합 자료실</a></li>
        </ul>
      </div>
  
      <div class="d-flex align-items-center">
        <form  @submit.prevent="totalSearch" v-if="token !== null" class="w-20 me-1" role="search">
          <input type="search" class="form-control" placeholder="total search" aria-label="Search"
          v-model="searchBody">
        </form>
        <button v-if="token !== null" type="button" class="btn btn-outline-light me-1" style="--bs-btn-padding-y: .5rem; --bs-btn-padding-x: .5rem; --bs-btn-font-size: .9rem;  color: #7248BD;" @click="Search()"><span class="material-symbols-outlined">saved_search</span></button>
        
        <button v-if="token === null" type="button" class="btn btn-outline-light me-2 btn-sm" style="--bs-btn-padding-y: .5rem; --bs-btn-padding-x: .5rem; --bs-btn-font-size: .9rem; border-color: #7248BD; color: #7248BD;" @click="SignUp()">Sign Up</button>
        <button v-if="token === null" type="button" class="btn btn-outline-light text-white btn-sm" style="--bs-btn-padding-y: .5rem; --bs-btn-padding-x: .5rem; --bs-btn-font-size: .9rem; border-color: #7248BD; background-color: #7248BD;" @click="Login()">Login</button>
        <div class="dropdown">
          <button v-if="token !== null" class="btn btn-outline-light dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false" style="--bs-btn-color: #4b4b4b; color: black;">
          {{ username }}
          </button>
          <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="/mypage">나의 정보</a></li>
            <li><a class="dropdown-item" @click="Logout()">로그아웃</a></li>
          </ul>
        </div>
      </div>
    </header>
    <div class="content-wrapper">
    <slot></slot> <!-- 메인 콘텐츠가 들어갈 자리 -->
    </div>
</template>

<style scoped>
.fixed-header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 65px;
  background-color: #fff; /* 배경색 */
  box-shadow: 0 1px 5px rgba(0, 0, 0, 0.1);
  z-index: 1000; /* 다른 콘텐츠보다 위에 위치하게 설정 */
}
.content-wrapper {
  margin-top: 85px; /* 헤더의 높이만큼 추가 */
}
.nav-link.active {
  color: #3b3b3b;
}
.nav-link.active:hover {
  color: #7248BD;
}
  
.nav-link.active:hover .material-symbols-outlined {
  color: #7248BD;
}
.dropdown-item:hover {
  color: #7248BD;
}
</style>