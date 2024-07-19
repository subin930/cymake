<script setup>
import { useRoute, useRouter } from 'vue-router'
import InitialHeader from '@/components/common/InitialHeader.vue'
import { ref } from "vue";
import axios from 'axios';

const route = useRoute();
const router = useRouter();

const company_code = ref('');
const id = ref('');
const password = ref('');

const Login = async () => {
    await axios.post(`/v1/auth/login`, {
        company_code: company_code.value,
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
        console.log(res.data.content.email);
        console.log(res.data.content.id);
        console.log(res.data.content.role);
        router.push(`/archive`);
    })
    .catch(function (error) {
        // TODO: 로그인 실패 창 보여주기
        console.log(error);
});
};
const MoveToSignup = () => {
    router.push("/signup");
};

const updatecompany_code = (event) => {
    company_code.value = event.target.value
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
                    <label for="inputcompany_code" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">회사코드</label>
                    <div class="col-sm-8">
                    <input type="text" class="form-control form-control-sm w-50" style="font-size: 0.9rem;" placeholder="회사코드를 입력하세요." id="inputcompany_code" @input="updatecompany_code">
                    </div>
                </div>
            </div>
            <div class="mb-3">
                <div class="mb-3 row">
                    <label for="inputid" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">아이디</label>
                    <div class="col-sm-8">
                    <input type="text" class="form-control form-control-sm w-50" style="font-size: 0.9rem;" placeholder="아이디를 입력하세요." id="inputid" @input="updateid">
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