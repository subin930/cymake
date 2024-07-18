<script setup>
import { RouterLink, RouterView } from 'vue-router'
import Header from '@/components/common/Header.vue'
import { ref } from "vue";
//companycode, userId, username, email, password, passwordCheck
const userId = ref('testId');
const username = ref('Test2');
const email = ref('testmail@test.com');
</script>

<template>
    <Header></Header>
    <div class="container text-center align-items-center justify-content-between">        
        <div class="m-3 mt-4">
            <p class="fw-bold fs-3">나의 정보</p>
        </div>

        <div class="container">
            <div class="mb-3">
                <div class="mb-3 row">
                    <label for="inputUserId" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">아이디</label>
                    <div class="col-sm-8">
                    <input type="text" class="form-control form-control-sm w-50 disabled" style="font-size: 0.9rem;" :placeholder="userId" id="inputUserId" disabled>
                    </div>
                </div>
            </div>
            <div class="mb-3">
                <div class="mb-3 row">
                    <label for="inputUsername" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">이름</label>
                    <div class="col-sm-8">
                    <input type="text" class="form-control form-control-sm w-50" style="font-size: 0.9rem;" :placeholder="username" id="inputUsername" disabled>
                    </div>
                </div>
            </div>
            <div class="mb-3">
                <div class="mb-3 row">
                    <label for="inputEmail" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">이메일</label>
                    <div class="col-sm-8">
                    <input type="text" class="form-control form-control-sm w-50" style="font-size: 0.9rem;" :placeholder="email" id="inputEmail" disabled>
                    </div>
                </div>
            </div>
            <div class="mb-5 row ">
                <label for="inputPassword" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">비밀번호 변경</label>
                <div class="col-sm-8 d-inline-flex justify-contetnt-center">
                    <button type="button" class="btn btn-outline-secondary w-20" style="font-size: 0.9rem;" @click="ChangePassword()">비밀번호 변경</button>
                </div>
                
            </div>
            <div class="d-flex m-5 mt-1 justify-content-center">
                <button type="button" class="btn text-white btn-outline-secondary w-20" style="font-size: 0.9rem; background-color: #6b42db;" @click="ChangeInfo()">정보수정</button>
                <button type="button" class="btn btn-outline-secondary w-20 ms-3" style="font-size: 0.9rem; border-color: #6b42db;" @click="UserQuit()">회원탈퇴</button>
            </div>
        </div>
    </div>
</template>
