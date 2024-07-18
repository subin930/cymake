<script setup>
import { RouterLink, RouterView } from 'vue-router'
import InitialHeader from '@/components/common/InitialHeader.vue'
//companycode, userId, username, email, password, passwordCheck
</script>

<template>
    <InitialHeader></InitialHeader>
    <div class="container-sm text-center">        
        <div class="m-3 mt-4">
            <p class="fw-bold fs-3">회원가입</p>
        </div>

        <div class="m-5">
            <div class="mb-3">
                <div class="mb-3 row">
                    <label for="inputCompanycode" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">회사코드</label>
                    <div class="col-sm-8">
                    <input type="text" class="form-control form-control-sm w-50"style="font-size: 0.9rem;" placeholder="회사코드를 입력하세요" id="inputCompanycode" :value="companycode" @input="companycode = $event.target.value">
                    </div>
                </div>
            </div>
            <div class="mb-3">
                <div class="mb-3 row">
                    <label for="inputUserId" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">아이디</label>
                    <div class="col-sm-8">
                    <input type="text" class="form-control form-control-sm w-50" style="font-size: 0.9rem;" placeholder="아이디를 입력하세요" id="inputUserId" :value="userId" @input="userId = $event.target.value">
                    </div>
                </div>
            </div>
            <div class="mb-3">
                <div class="mb-3 row">
                    <label for="inputUsername" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">이름</label>
                    <div class="col-sm-8">
                    <input type="text" class="form-control form-control-sm w-50" style="font-size: 0.9rem;" placeholder="이름을 입력하세요" id="inputUsername" :value="username" @input="username = $event.target.value">
                    </div>
                </div>
            </div>
            <div class="mb-3">
                <div class="mb-3 row">
                    <label for="inputEmail" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">이메일</label>
                    <div class="col-sm-8">
                    <input type="text" class="form-control form-control-sm w-50" style="font-size: 0.9rem;" placeholder="이메일을 입력하세요" id="inputEmail" :value="email" @input="email = $event.target.value">
                    </div>
                </div>
            </div>
            <div class="mb-3 row">
                <label for="inputPassword" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">비밀번호</label>
                <div class="col-sm-8">
                <input type="password" class="form-control form-control-sm w-50" style="font-size: 0.9rem;" placeholder="비밀번호를 입력하세요" id="inputPassword" :value="password" @input="password = $event.target.value">
                </div>
            </div>
            <div class="mb-3 row">
                <label for="CheckPassword" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">비밀번호 확인</label>
                <div class="col-sm-8">
                <input type="password" class="form-control form-control-sm w-50" style="font-size: 0.9rem;" placeholder="비밀번호를 한 번 더 입력하세요" id="CheckPassword" :value="passwordCheck" @input="passwordCheck = $event.target.value">
                </div>
            </div>
            <div class="d-flex flex-column align-items-center m-5 mt-1">
                <button type="button" class="btn text-white btn-outline-secondary w-50" style="font-size: 0.9rem; background-color: #6b42db;" @click="Signup()">회원가입</button>
            </div>
        </div>
    </div>
</template>
