<script setup>
//비밀번호 변경 구현
import { ref } from "vue";

const originalPassword = ref("");
const newPassword = ref("");
const newPasswordCheck = ref("");

const ChangePassword = () => {
    console.log("Change Password Activated");
    console.log(originalPassword.value);
    console.log(newPassword.value);
    console.log(newPasswordCheck.value);
} 
</script>

<template>
    <div class="modal fade" id="passwordChangeModal" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="modalLabel">비밀번호 변경</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="mb-2 row">
                    <label for="inputOriginalPassword" class="justify-content-start" style="font-size: .8rem;">기존 비밀번호</label>
                    <input type="text" class="form-control" style="font-size: .8rem;" placeholder="기존 비밀번호를 입력하세요" id="inputOriginalPassword" :value="originalPassword" @input="originalPassword = $event.target.value">
                </div>
                <div class="mb-2 row">
                    <label for="inputNewPassword" class=" justify-content-start" style="font-size: .8rem;">신규 비밀번호</label>
                    <input type="text" class="form-control" style="font-size: .8rem;" placeholder="새로운 비밀번호를 입력하세요" id="inputNewPassword" :value="newPassword" @input="newPassword = $event.target.value">
                </div>
                <div class="mb-2 row">
                    <label for="inputNewPasswordCheck" class="justify-content-start" style="font-size: .8rem;">신규 비밀번호 확인</label>
                    <input type="text" class="form-control" style="font-size: .8rem;" placeholder="새로운 비밀번호를 재입력하세요" id="inputNewPasswordCheck" :value="newPasswordCheck" @input="newPasswordCheck = $event.target.value">
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" @click="ChangePassword()">비밀번호 변경</button>
            </div>
            </div>
        </div>
    </div>
</template>