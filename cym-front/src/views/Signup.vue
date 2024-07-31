<script setup>
import { useRouter } from 'vue-router';
import InitialHeader from '@/components/common/InitialHeader.vue';
import { ref } from 'vue';
import axios from 'axios';
import { useForm, useField } from 'vee-validate';
import * as yup from 'yup';

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

    try {
        const response = await axios.post('/v1/users/register', body);
        if (response.data.message === 'success') {
            router.push('/login');
        } else {
            console.log('회원가입 실패');
        }
    } catch (error) {
        console.error('An error occurred while registering the user:', error);
    }
});
</script>

<template>
    <InitialHeader></InitialHeader>
    <div class="container-sm" style="background-color: #F5F6FA;">
        <div class="m-3 mt-4 title">
            <p class="fw-bold fs-3">회원가입</p>
        </div>

        <div class="m-5">
            <form @submit.prevent="Signup">
                <div class="mb-3">
                    <div class="mb-3 row">
                        <label for="inputcompanyCode" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">회사코드</label>
                        <div class="col-sm-8">
                            <input type="text" v-model="companyCode" class="form-control form-control-sm w-50" style="font-size: 0.9rem;" placeholder="회사코드를 입력하세요" id="inputcompanyCode">
                            <span v-if="companyCodeError" class="text-danger" style="font-size: 0.7rem;">{{ companyCodeError }}</span>
                        </div>
                    </div>
                </div>
                <div class="mb-3">
                    <div class="mb-3 row">
                        <label for="inputid" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">아이디</label>
                        <div class="col-sm-8">
                            <input type="text" v-model="id" class="form-control form-control-sm w-50" style="font-size: 0.9rem;" placeholder="아이디를 입력하세요" id="inputid">
                            <span v-if="idError" class="text-danger" style="font-size: 0.7rem;">{{ idError }}</span>
                        </div>
                    </div>
                </div>
                <div class="mb-3">
                    <div class="mb-3 row">
                        <label for="inputUsername" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">이름</label>
                        <div class="col-sm-8">
                            <input type="text" v-model="username" class="form-control form-control-sm w-50" style="font-size: 0.9rem;" placeholder="이름을 입력하세요" id="inputUsername">
                            <span v-if="usernameError" class="text-danger" style="font-size: 0.7rem;">{{ usernameError }}</span>
                        </div>
                    </div>
                </div>
                <div class="mb-3">
                    <div class="mb-3 row">
                        <label for="inputEmail" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">이메일</label>
                        <div class="col-sm-8">
                            <input type="email" v-model="email" class="form-control form-control-sm w-50" style="font-size: 0.9rem;" placeholder="이메일을 입력하세요" id="inputEmail">
                            <span v-if="emailError" class="text-danger" style="font-size: 0.7rem;">{{ emailError }}</span>
                        </div>
                    </div>
                </div>
                <div class="mb-3 row">
                    <label for="inputPassword" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">비밀번호</label>
                    <div class="col-sm-8">
                        <input type="password" v-model="password" class="form-control form-control-sm w-50" style="font-size: 0.9rem;" placeholder="비밀번호를 입력하세요" id="inputPassword">
                        <span v-if="passwordError" class="text-danger" style="font-size: 0.7rem;">{{ passwordError }}</span>
                    </div>
                </div>
                <div class="mb-3 row">
                    <label for="CheckPassword" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">비밀번호 확인</label>
                    <div class="col-sm-8">
                        <input type="password" v-model="passwordCheck" class="form-control form-control-sm w-50" style="font-size: 0.9rem;" placeholder="비밀번호를 한 번 더 입력하세요" id="CheckPassword">
                        <span v-if="passwordCheckError" class="text-danger" style="font-size: 0.7rem;">{{ passwordCheckError }}</span>
                    </div>
                </div>
                <div class="d-flex flex-column align-items-center m-5 mt-1">
                    <button type="submit" class="btn text-white btn-outline-secondary w-50" style="font-size: 0.9rem; background-color: #6b42db;">회원가입</button>
                </div>
            </form>
        </div>
    </div>
</template>

<style scoped>
.title {
    padding: 20px;
}
</style>