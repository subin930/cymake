<script setup>
import { useRoute, useRouter } from 'vue-router'
import InitialHeader from '@/components/common/InitialHeader.vue'
import { ref } from "vue"
import axios from 'axios'

const route = useRoute();
const router = useRouter();

const companyCode = ref("");
const id = ref("");
const password = ref("");
const errorMessage = ref("");

const Login = async () => {
    errorMessage.value = "";
    // Null 또는 빈 값 확인
    if (!companyCode.value) {
    errorMessage.value = "회사코드를 입력하세요.";
    }
    if (!id.value) {
        errorMessage.value = "아이디를 입력하세요.";
    }
    if (!password.value) {
        errorMessage.value = "비밀번호를 입력하세요.";
    }

    // 에러 메시지가 존재하면 서버 요청을 보내지 않음
    if (errorMessage.value.length > 0) {
        return;
    }
    await axios.post(`/v1/auth/login`, {
        companyCode: companyCode.value,
        id: id.value,
        password: password.value,
    })
    .then((res) => {
        console.log(res.data.content.role)
        localStorage.setItem("token", res.data.content.accessToken);
        localStorage.setItem("refreshToken", res.data.content.refreshToken);
        localStorage.setItem("userId", res.data.content.id);
        localStorage.setItem("userRole", res.data.content.role);
        localStorage.setItem("userEmail", res.data.content.email);
        localStorage.setItem("username", res.data.content.username);
        console.log(res.data.content.email);
        console.log(res.data.content.id);
        console.log(res.data.content.role);
        console.log(res.data.content.accessToken);
        router.push(`/archive`);
    })
    .catch(function (error) {
        // TODO: 로그인 실패 창 보여주기
        errorMessage.value = error.response.data.message;
        console.log(error);
});
};
const MoveToSignup = () => {
    router.push("/signup");
};

const updatecompanyCode = (event) => {
    companyCode.value = event.target.value
}

const updateid = (event) => {
    id.value = event.target.value
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
                    <label for="inputcompanyCode" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">회사코드</label>
                    <div class="col-sm-8">
                    <input type="text" class="form-control form-control-sm w-50" style="font-size: 0.9rem;" placeholder="회사코드를 입력하세요." id="inputcompanyCode" @input="updatecompanyCode" required>
                    </div>
                </div>
            </div>
            <div class="mb-3">
                <div class="mb-3 row">
                    <label for="inputid" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">아이디</label>
                    <div class="col-sm-8">
                    <input type="text" class="form-control form-control-sm w-50" style="font-size: 0.9rem;" placeholder="아이디를 입력하세요." id="inputid" @input="updateid" required>
                    </div>
                </div>
            </div>
            <div class="mb-3 row">
                <label for="inputPassword" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">비밀번호</label>
                <div class="col-sm-8">
                <input type="password" class="form-control form-control-sm w-50" style="font-size: 0.9rem;" placeholder="비밀번호를 입력하세요." id="inputPassword" @input="updatePassword" required>
                </div>
            </div>
            <div class="d-flex flex-column align-items-center m-5 mt-1">
                        <button type="button" class="btn text-white btn-outline-secondary w-50" style="font-size: 0.9rem; background-color: #6b42db;" @click="Login()" @submit.prevent = "Login()">로그인</button>
                        <p v-if="errorMessage" class="text-danger" style="font-size: .7rem; margin-bottom: 0.5rem; margin-top: 0.1rem;">{{ errorMessage }}</p>
                        <p class="m-1" style="font-size: 0.8rem; cursor: pointer;" @click="MoveToSignup()">회원가입</p>
                    </div>
                </div>
    </div>
    
</template>