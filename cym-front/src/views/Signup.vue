<script setup>
import { useRouter } from 'vue-router';
import InitialHeader from '@/components/common/InitialHeader.vue';
import { ref } from 'vue';
import axios from 'axios';
import { useForm, useField } from 'vee-validate';
import * as yup from 'yup';

const errorMessage = ref("");
const loading = ref(false);

// 유효성 검사 스키마 정의
const schema = yup.object({
    companyCode: yup.string().required('회사코드를 입력해주세요.'),
    id: yup.string().required('아이디를 입력해주세요.'),
    username: yup.string().required('이름을 입력해주세요.'),
    email: yup.string().email('유효한 이메일을 입력해주세요.').required('이메일을 입력해주세요.'),
    password: yup.string().required('비밀번호를 입력해주세요.'),
    passwordCheck: yup.string().oneOf([yup.ref('password')], '비밀번호가 일치하지 않습니다.').required('비밀번호 확인을 입력해주세요.')
});

// vee-validate 폼 설정
const { handleSubmit, errors } = useForm({
    validationSchema: schema
});

// 필드 설정
const { value: companyCode, errorMessage: companyCodeError } = useField('companyCode');
const { value: id, errorMessage: idError } = useField('id');
const { value: username, errorMessage: usernameError } = useField('username');
const { value: email, errorMessage: emailError } = useField('email');
const { value: password, errorMessage: passwordError } = useField('password');
const { value: passwordCheck, errorMessage: passwordCheckError } = useField('passwordCheck');

const router = useRouter();

// 회원가입 함수
const Signup = handleSubmit(async (values) => {
    const body = {
        id: values.id,
        username: values.username,
        companyCode: values.companyCode,
        email: values.email,
        password: values.password,
        passwordCheck: values.passwordCheck
    };
    loading.value = true;
    try {
        const response = await axios.post('/v1/users/register', body);
        if (response.data.message === 'success') {
            router.push('/login');
        } else {
            console.log('회원가입 실패');
        }
    } catch (error) {
        errorMessage.value = error.response.data.message;
        console.error('An error occurred while registering the user:', error);
    } finally {
        loading.value = false;
    }
});
</script>

<template>
    <InitialHeader></InitialHeader>
    <div class="container d-flex flex-column align-items-center" style="min-height: 100vh; margin-top: 20px !important;">
        <div class="m-3 mt-4 title text-center" style="margin: 0px !important; padding: 0px !important;">
            <p class="fw-bold fs-3" style="margin: 10px;">회원가입</p>
        </div>

        <div class="m-5 w-100" style="max-width: 400px; margin: 0px !important;">
            <form @submit.prevent="Signup">
                <div class="mb-3">
                    <label for="inputcompanyCode" class="form-label">회사코드 <span class="text-danger">*</span></label>
                    <input type="text" v-model="companyCode" class="form-control" placeholder="회사코드를 입력하세요" id="inputcompanyCode">
                    <span v-if="companyCodeError" class="text-danger" style="font-size: 0.7rem;">{{ companyCodeError }}</span>
                </div>

                <div class="mb-3">
                    <label for="inputid" class="form-label">아이디 <span class="text-danger">*</span></label>
                    <input type="text" v-model="id" class="form-control" placeholder="아이디를 입력하세요" id="inputid">
                    <span v-if="idError" class="text-danger" style="font-size: 0.7rem;">{{ idError }}</span>
                </div>

                <div class="mb-3">
                    <label for="inputUsername" class="form-label">이름 <span class="text-danger">*</span></label>
                    <input type="text" v-model="username" class="form-control" placeholder="이름을 입력하세요" id="inputUsername">
                    <span v-if="usernameError" class="text-danger" style="font-size: 0.7rem;">{{ usernameError }}</span>
                </div>

                <div class="mb-3">
                    <label for="inputEmail" class="form-label">이메일 <span class="text-danger">*</span></label>
                    <input type="email" v-model="email" class="form-control" placeholder="이메일을 입력하세요" id="inputEmail">
                    <span v-if="emailError" class="text-danger" style="font-size: 0.7rem;">{{ emailError }}</span>
                </div>

                <div class="mb-3">
                    <label for="inputPassword" class="form-label">비밀번호 <span class="text-danger">*</span></label>
                    <input type="password" v-model="password" class="form-control" placeholder="비밀번호를 입력하세요" id="inputPassword">
                    <span v-if="passwordError" class="text-danger" style="font-size: 0.7rem;">{{ passwordError }}</span>
                </div>

                <div class="mb-3">
                    <label for="CheckPassword" class="form-label">비밀번호 확인 <span class="text-danger">*</span></label>
                    <input type="password" v-model="passwordCheck" class="form-control" placeholder="비밀번호를 한 번 더 입력하세요" id="CheckPassword">
                    <span v-if="passwordCheckError" class="text-danger" style="font-size: 0.7rem;">{{ passwordCheckError }}</span>
                </div>

                <div class="d-flex flex-column align-items-center mt-4">
                    <button type="submit" class="btn text-white btn-outline-secondary w-100" style="font-size: 0.9rem; background-color: #6b42db;">회원가입</button>
                </div>
                <p v-if="errorMessage" class="text-danger text-center mt-2" style="font-size: 0.875rem; margin-bottom: 10px;">{{ errorMessage }}</p>
            </form>
        </div>
        <!-- 로딩 창 -->
        <div v-if="loading" class="loading-overlay">
            <div class="loading-spinner"></div>
        </div>
        <!-- 로딩 창 -->
        <div v-if="loading" class="loading-overlay">
            <div class="spinner-border text-secondary" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
        </div>
    </div>
</template>

<style scoped>
.form-label {
    font-size: 0.9rem;
    font-weight: 500;
    margin-bottom: 0.5rem;
    display: block;
}

.form-control {
    padding: 10px;
    font-size: 0.9rem;
    border-radius: 0;
    border: 1px solid #ced4da;
    height: 40px;
}

.form-control:focus {
    
    box-shadow: none;
}

.btn-outline-secondary {
    border-radius: 20px;
}

.text-danger {
    font-size: 0.8rem;
}

.title {
    padding: 20px;
}
/* 로딩 창 스타일 */
.loading-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 9999;
}
</style>
