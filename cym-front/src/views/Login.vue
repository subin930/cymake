<script setup>
import { useRoute, useRouter } from 'vue-router'
import InitialHeader from '@/components/common/InitialHeader.vue'
import { ref } from "vue";
const route = useRoute();
const router = useRouter();

const companycode = ref("");
const userId = ref("");
const password = ref("");

const Login = () => {
    localStorage.setItem("token", "Test"); //임시로 토큰 생성 (그냥 버튼 누르면 작동되게...)
    localStorage.setItem("userId","Test2");
    localStorage.setItem("username", "임시용");
    router.push("/archive");
}
const MoveToSignup = () => {
    router.push("/signup");
};

const updateCompanycode = (event) => {
    companycode.value = event.target.value
}

const updateUserId = (event) => {
    userId.value = event.target.value
}

const updatePassword = (event) => {
    password.value = event.target.value
}

</script>

<template>
    <InitialHeader></InitialHeader>
    <div class="container-sm text-center">        
        <div class="m-3 mt-4">
            <p class="fw-bold fs-3">로그인</p>
        </div>

        <div class="m-5">
            <div class="mb-3">
                <div class="mb-3 row">
                    <label for="inputCompanycode" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">회사코드</label>
                    <div class="col-sm-8">
                    <input type="text" class="form-control form-control-sm w-50" style="font-size: 0.9rem;" placeholder="회사코드를 입력하세요." id="inputCompanycode" @input="updateCompanycode">
                    </div>
                </div>
            </div>
            <div class="mb-3">
                <div class="mb-3 row">
                    <label for="inputUserId" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">아이디</label>
                    <div class="col-sm-8">
                    <input type="text" class="form-control form-control-sm w-50" style="font-size: 0.9rem;" placeholder="아이디를 입력하세요." id="inputUserId" @input="updateUserId">
                    </div>
                </div>
            </div>
            <div class="mb-3 row">
                <label for="inputPassword" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">비밀번호</label>
                <div class="col-sm-8">
                <input type="password" class="form-control form-control-sm w-50" style="font-size: 0.9rem;" placeholder="비밀번호를 입력하세요." id="inputPassword" @input="updatePassword">
                </div>
            </div>
            <div class="d-flex flex-column align-items-center m-5 mt-1">
                <button type="button" class="btn text-white btn-outline-secondary w-50" style="font-size: 0.9rem; background-color: #6b42db;" @click="Login()">로그인</button>
                <p class="m-1" style="font-size: 0.8rem; cursor: pointer;" @click="MoveToSignup()">회원가입</p>
            </div>
        </div>
        
        
    </div>
    
</template>