<script setup>
import { RouterLink, RouterView } from 'vue-router'
import Header from '@/components/common/Header.vue'
import PasswordChangeModal from '@/components/MyPage/PasswordChangeModal.vue'
import { ref } from "vue";
import axios from 'axios'
import { useRoute, useRouter } from 'vue-router'
import ConfirmUnregisterModal from '@/components/MyPage/ConfirmUnregisterModal.vue';

//companyCode, id, username, email, password, passwordCheck
const modalID = ref('passwordChangeModal');

const token = localStorage.getItem("token");
const username = ref(localStorage.getItem("username"));
const email = ref(localStorage.getItem("userEmail"));
const id = ref(localStorage.getItem("userId"));
const errorMessage = ref("");
const loading = ref(false);
const plan = ref(localStorage.getItem("plan"));
const userRole = ref(localStorage.getItem("userRole"));

const route = useRoute();
const router = useRouter();

const OpenChangeModal = () => {
    const modalElement = document.getElementById(modalID.value);
    const modalInstance = new bootstrap.Modal(modalElement);
    modalInstance.show();
};

const OpenUnregisterModal = () => {
    const modalElement = document.getElementById('confirmUnregisterModal');
    const modalInstance = new bootstrap.Modal(modalElement);
    modalInstance.show();
};

// 회원정보 수정 함수
const ChangeInfo = async() => {
    loading.value = true;
    try {
        await axios.put(`/v1/users/update`, {
        }, {
                headers: {
                    'Authorization': `Bearer ${token}`
                },
                params: {
                    email: email.value,
                    plan: plan.value
                }
            })
        .then((res) => {
            console.log(res.data.message);
            alert("회원 정보 수정에 성공하였습니다.");
            localStorage.setItem("userEmail", email.value);
            localStorage.setItem("plan", plan.value);
            router.push(`/archive`);
        })
    } catch(error) {
        // 비밀번호 변경 오류 메시지
        errorMessage.value = error.response.data.message;
        alert(errorMessage.value);
        console.log(error);
    } finally {
        loading.value = false;
    };
};

const unregister = async() => {
    try {
        console.log(token)
        const response = await axios.delete(`/v1/users/unregister`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        // 성공적으로 응답을 받은 경우 처리
        if (response.data) {
            const { code, message } = response.data;
            console.log("Unregister Success:", code, message);
            localStorage.clear();
            // 모달 닫기
            const modalElement = document.getElementById('confirmUnregisterModal');
            const modalInstance = bootstrap.Modal.getInstance(modalElement);
            modalInstance.hide();
            
            router.push(`/login`)
        }
    } catch (error) {
        console.error("Error unregister:", error);
    }
}

</script>

<template>
    <Header></Header>
    <div class="container text-center align-items-center justify-content-between" style="background-color: #F5F6FA;">        
        <div class="m-2 title">
            <p class="fw-bold fs-3">나의 정보</p>
        </div>

        <div class="box justify-content-center" style="background-color: #FFFFFF;">
            <form @submit.prevent="ChangeInfo">
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
                        <input type="text" class="form-control form-control-sm w-50" style="font-size: 0.9rem;" :placeholder="email" id="inputEmail" v-model="email">
                        </div>
                    </div>
                </div>
                <div class="mb-3 row justify-content-center">
                    <label for="inputPassword" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">비밀번호 변경</label>
                    <div class="col-sm-8 d-inline-flex justify-contetnt-center">
                        <button type="button" class="btn pwchange-btn w-20" style="font-size: 0.9rem; color:#7248BD; border-color:#7248BD; border-radius: 20px; background-color: #FFFFFF;" @click="OpenChangeModal()">비밀번호 변경</button>
                    </div>
                </div>
                <div class="mb-5 row justify-content-center" style="align-items: center;">
                    <label for="planSelect" class="col-sm-4 col-form-label col-form-label-sm d-flex justify-content-end" style="font-size: 0.9rem;">요금제</label>
                    <div class="col-sm-8 d-inline-flex" style="justify-content: flex-start;">
                        <select 
                            class="form-select" 
                            id="planSelect" 
                            v-model="plan" 
                            :disabled="userRole === 'USER'" 
                            style="font-size: 0.9rem; height: 38px; line-height: 1.5; width: 50%;">
                            <option disabled value="">요금제를 선택하세요</option>
                            <option value="basic">basic</option>
                            <option value="standard">standard</option>
                            <option value="premium">premium</option>
                        </select>
                    </div>
                </div>
                <div class="d-flex m-5 mt-1 justify-content-center">
                    <button type="submit" class="btn change-btn  w-20 mb-3" style="font-size: 0.9rem; color:#FFFFFF; border-color:#FFFFFF; background-color: #7248BD;">정보수정</button>
                    <button type="button" class="btn unregist-btn w-20 ms-3 mb-3" style="font-size: 0.9rem; color:#FFFFFF; border-color:#FFFFFF; background-color: #7248BD;" @click="OpenUnregisterModal()">회원탈퇴</button>
                </div>
            </form>    
        </div>
    </div>
    <PasswordChangeModal></PasswordChangeModal>
    <ConfirmUnregisterModal @confirm="unregister"></ConfirmUnregisterModal>
    <div v-if="loading" class="loading-overlay">
            <div class="spinner-border text-secondary" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
    </div>
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
    background-color:#7248BD !important;
    color: white !important;
    border-color: white !important;
}
.change-btn{
    border-radius: 20px;
    width: 100px;
}
.unregist-btn{
    border-radius: 20px;
    width: 100px;
}
.change-btn:hover {
    background-color: #8E6DCA !important;
    transform: translateY(-1px);
}
.unregist-btn:hover {
    background-color: #8E6DCA !important;
    border-radius: 20px;
    transform: translateY(-1px);
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
