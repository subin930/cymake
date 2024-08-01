<script setup>
import { RouterLink, RouterView } from 'vue-router'
import Header from '@/components/common/Header.vue'
import PasswordChangeModal from '@/components/MyPage/PasswordChangeModal.vue'
import { ref } from "vue";
//companyCode, id, username, email, password, passwordCheck
const modalID = ref('passwordChangeModal');

const token = ref(localStorage.getItem("token"));
const username = ref(localStorage.getItem("username"));
const email = ref(localStorage.getItem("userEmail"));
const id = ref(localStorage.getItem("userId"));

const OpenChangeModal = () => {
    console.log(token);
    const modalElement = document.getElementById(modalID.value);
    const modalInstance = new bootstrap.Modal(modalElement);
    modalInstance.show();
};

</script>

<template>
    <Header></Header>
    <div class="container text-center align-items-center justify-content-between" style="background-color: #F5F6FA;">        
        <div class="m-2 title">
            <p class="fw-bold fs-3">나의 정보</p>
        </div>

        <div class="box justify-content-center" style="background-color: #FFFFFF;">
            <div class="mb-3">
                <div class="mb-3 row  justify-content-center">
                    <label for="inputid" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">아이디</label>
                    <div class="col-sm-8">
                    <input type="text" class="form-control form-control-sm w-50 disabled" style="font-size: 0.9rem;" :placeholder="id" id="inputid" disabled>
                    </div>
                </div>
            </div>
            <div class="mb-3">
                <div class="mb-3 row justify-content-center">
                    <label for="inputUsername" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">이름</label>
                    <div class="col-sm-8">
                    <input type="text" class="form-control form-control-sm w-50" style="font-size: 0.9rem;" :placeholder="username" id="inputUsername" disabled>
                    </div>
                </div>
            </div>
            <div class="mb-3">
                <div class="mb-3 row justify-content-center">
                    <label for="inputEmail" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">이메일</label>
                    <div class="col-sm-8">
                    <input type="text" class="form-control form-control-sm w-50" style="font-size: 0.9rem;" :placeholder="email" id="inputEmail" disabled>
                    </div>
                </div>
            </div>
            <div class="mb-5 row justify-content-center">
                <label for="inputPassword" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">비밀번호 변경</label>
                <div class="col-sm-8 d-inline-flex justify-contetnt-center">
                    <button type="button" class="btn pwchange-btn w-20" style="font-size: 0.9rem; color:#6b42db; border-color:#6b42db; background-color: #FFFFFF;" @click="OpenChangeModal()">비밀번호 변경</button>
                </div>
                
            </div>
            <div class="d-flex m-5 mt-1 justify-content-center">
                <button type="button" class="btn change-btn  w-20 mb-3" style="font-size: 0.9rem; color:#FFFFFF; border-color:#FFFFFF; background-color: #6b42db;" @click="ChangeInfo()">정보수정</button>
                <button type="button" class="btn unregist-btn w-20 ms-3 mb-3" style="font-size: 0.9rem; color:#FFFFFF; border-color:#FFFFFF; background-color: #6b42db;" @click="UserQuit()">회원탈퇴</button>
            </div>
        </div>
    </div>
    <PasswordChangeModal></PasswordChangeModal>
</template>

<style scoped>
.title {
    padding-top: 20px;
}
.box {
    background-color: #FFFFFF; /* 박스의 배경색 */
    border-radius: 8px; /* 모서리 둥글게 */
    padding-top:40px;
    margin-left: 25%;
    margin-right: 25%;
}

.pwchange-btn:hover {
    background-color: #8E6DCA !important;
    color: white !important;
    border-color: white !important;
}
.change-btn:hover {
    background-color: #8E6DCA !important;
}
.unregist-btn:hover {
    background-color: #8E6DCA !important;
}
</style>
