<script setup>
import { ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import Chat from '@/components/common/Chat.vue';
const route = useRoute();
const router = useRouter();

const token = ref(localStorage.getItem("token"));
const username = ref(localStorage.getItem("username"));
const searchBody = ref('');

const isActive = (path) => {
  return route.path === path;
};

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
const ChatSearch = async () => {
  console.log("챗봇 구현 전");
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
      <ul class="nav ms-1 d-none d-md-flex"> <!-- 화면 크기에 따라 메뉴 숨김 처리 -->
        <li><a  :class="['nav-link', { 'active': isActive('/archive')|| isActive('/archive/total') }]" aria-current="page" style="font-size: 1rem; font-weight: 550;" href="/archive"><span class="material-symbols-outlined" style="font-size:1rem">description</span>지식아카이브</a></li>
        <li><a  :class="['nav-link', { 'active': isActive('/drive') }]" aria-current="page" style="font-size: 1rem; font-weight: 550;" href="/drive"><span class="material-symbols-outlined" style="font-size:1rem">attach_file</span>통합 자료실</a></li>
      </ul>
    </div>

    <div class="d-flex align-items-center">
      <form  @submit.prevent="totalSearch" v-if="token !== null" class="input-group w-20 me-1" role="search">
        <input type="search" class="form-control" placeholder="total search" aria-label="Search"
        v-model="searchBody">
        <button type="button" class="btn btn-outline-secondary"
        style="border-color: #D6D8DB" @click="totalSearch"><i class="bi bi-search"></i></button>
      </form>
      <Chat></Chat>
      
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
max-width: 100vw; /* 헤더는 뷰포트 너비 전체까지만 확장 */
height: 65px;
background-color: #fff; /* 배경색 */
box-shadow: 0 1px 1px rgba(0, 0, 0, 0.1);
z-index: 1000; /* 다른 콘텐츠보다 위에 위치하게 설정 */
}
.fixed-header input[type="search"],
.fixed-header .dropdown-toggle {
  min-width: 120px; /* 최소 크기 설정으로 너무 작아지지 않게 */
  flex-shrink: 0; /* 필요 이상으로 축소되지 않도록 */
}
.form-control::placeholder {
opacity: .5;
}
.content-wrapper {
margin-top: 65px; /* 헤더의 높이만큼 추가 */

}
.nav-link {
color: #3b3b3b;
}
.nav-link:hover {
color: #7248BD;
}

.nav-link:hover .material-symbols-outlined {
color: #7248BD;
}
.nav-link.active {
color: #7248BD; /* 활성화된 링크의 글씨 색상 */
}
.dropdown-item:hover {
color: #7248BD;
background-color: #F1EDF8;
}
.dropdown-item:active {
color: #7248BD;
background-color: #F1EDF8;
}
input:focus {
outline: none;
border-color: #7248BD;
box-shadow: 0 0 0 0 rgba(114, 72, 189, 0.25);
}
input[type="search"], .btn {
  max-width: 150px; /* 검색 창과 버튼 최대 너비 설정 */
}
</style>
