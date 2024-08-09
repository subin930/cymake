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
        return;
    }
    if (!id.value) {
        errorMessage.value = "아이디를 입력하세요.";
        return;
    }
    if (!password.value) {
        errorMessage.value = "비밀번호를 입력하세요.";
        return;
    }
    // 에러 메시지가 존재하면 서버 요청을 보내지 않음
    if (errorMessage.value.length > 0) {
        return;
    }
    try {
        const res = await axios.post(`/v1/auth/login`, {
            companyCode: companyCode.value,
            id: id.value,
            password: password.value,
        });
        console.log(res.data.content.role)
        localStorage.setItem("token", res.data.content.accessToken);
        localStorage.setItem("refreshToken", res.data.content.refreshToken);
        localStorage.setItem("userId", res.data.content.id);
        localStorage.setItem("userRole", res.data.content.role);
        localStorage.setItem("userEmail", res.data.content.email);
        localStorage.setItem("username", res.data.content.username);
        localStorage.setItem("sessionId", '0');
        console.log(res.data.content.email);
        console.log(res.data.content.id);
        console.log(res.data.content.role);
        console.log(res.data.content.accessToken);
        router.push(`/archive`);
    } catch (error) {
        errorMessage.value = error.response.data.message;
        console.log(error);
    }
};

const MoveToSignup = () => {
    router.push("/signup");
};

const updatecompanyCode = (event) => {
    companyCode.value = event.target.value;
};

const updateid = (event) => {
    id.value = event.target.value;
};

const updatePassword = (event) => {
    password.value = event.target.value;
};

</script>

<template>
    <InitialHeader></InitialHeader>
    <div class="container-sm d-flex flex-column align-items-center justify-content-start" style="min-height: 100vh; padding-top: 50px;">
        <div class="text-center mb-3">
            <h3 class="fw-bold">로그인</h3>
        </div>
        <form @submit.prevent="Login" class="w-100" style="max-width: 320px;">
            <div class="mb-3" style = "margin-bottom: 10px !important; ">
                <input type="text" class="form-control" style="height: 40px;" id="inputcompanyCode" placeholder="회사코드를 입력하세요." @input="updatecompanyCode" required>
            </div>
            <div class="mb-3" style = "margin-bottom: 10px !important;">
                <input type="text" class="form-control" style="height: 40px;" id="inputid" placeholder="아이디를 입력하세요." @input="updateid" required>
            </div>
            <div class="mb-3" style = "margin-bottom: 15px !important;">
                <input type="password" class="form-control" style="height: 40px;" id="inputPassword" placeholder="비밀번호를 입력하세요." @input="updatePassword" required>
            </div>
            <div class="d-grid">
                <button type="submit" class="btn text-white login-button">로그인</button> 
            </div>
            <p v-if="errorMessage" class="text-danger text-center mt-2" style="font-size: 0.875rem; margin-bottom: 10px;">{{ errorMessage }}</p>
            <div class="text-center mt-3" style="margin-top: 5px !important;">
                <p class="m-0 text-muted option-links">
                    <span @click="MoveToSignup">회원가입</span>
                </p>
            </div>
        </form>
    </div>
</template>

<style scoped>
.login-card {
    max-width: 320px;
    width: 100%;
    border-radius: 0; /* 둥근 모서리 제거 */
    background-color: #f8f9fa;
}

.login-button {
    background-color: #6b42db;
    padding: 0.65rem;
    font-size: 0.95rem;
    border-radius: 30px; /* 둥근 모서리 제거 */
    height: 38px; /* // TODO: 로그인 버튼 height 줄임 */
    float:right;
    display:flex;
    text-align: center;
    align-items:center;
    justify-content:center;
}

.form-control {
    padding: 0.65rem;
    font-size: 0.85rem;
    border-radius: 0; /* 둥근 모서리 제거 */
    border: 1px solid #DDDFE5;
}

.option-links span {
    cursor: pointer;
    font-size: 0.85rem;
    color: #6b42db;
}
</style>
