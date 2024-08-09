<script setup>
// 비밀번호 변경 구현
import { useRoute, useRouter } from 'vue-router';
import { ref, onMounted } from "vue";
import axios from 'axios';
import { useForm, useField } from 'vee-validate';
import * as yup from 'yup';

const token = localStorage.getItem("token");
const errorMessage = ref("");

// 유효성 검사 스키마 정의
const schema = yup.object({
    originalPassword: yup.string().required('기존 비밀번호를 입력해주세요.'),
    newPassword: yup.string().required('새로운 비밀번호를 입력해주세요.'),
    newPasswordCheck: yup.string().oneOf([yup.ref('newPassword')], '비밀번호가 일치하지 않습니다.').required('비밀번호 확인을 입력해주세요.')
});

// vee-validate 폼 설정
const { handleSubmit, resetForm } = useForm({
    validationSchema: schema
})

// 필드 설정
const { value: originalPassword, errorMessage: originalPasswordError, resetField: resetOriginalPassword } = useField('originalPassword');
const { value: newPassword, errorMessage: newPasswordError, resetField: resetNewPassword } = useField('newPassword');
const { value: newPasswordCheck, errorMessage: newPasswordCheckError, resetField: resetNewPasswordCheck } = useField('newPasswordCheck');

// 비밀번호 변경 함수
const ChangePassword = handleSubmit(async() => {
    await axios.put(`/v1/users/update`, {
        originalPassword: originalPassword.value,
        newPassword: newPassword.value,
        newPasswordCheck: newPasswordCheck.value,
    }, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
    .then((res) => {
        console.log(res.data.message);
        alert("비밀번호 변경에 성공하였습니다.");
        // 비밀번호 변경 성공 후 모달 창 닫기
        const modal = document.getElementById('passwordChangeModal');
        const bootstrapModal = bootstrap.Modal.getInstance(modal); // Bootstrap 모달 인스턴스 가져오기
        bootstrapModal.hide(); // 모달 창 닫기
    })
    .catch(function (error) {
        // 비밀번호 변경 오류 메시지
        errorMessage.value = error.response.data.message;
        console.log(error);
    });
});

// 폼 초기화 함수
const CancelModify = () => {
    // 값 초기화
    originalPassword.value = "";
    newPassword.value = "";
    newPasswordCheck.value = "";
    errorMessage.value = "";

    // VeeValidate에서 관리되는 상태 및 필드 초기화
    resetOriginalPassword();
    resetNewPassword();
    resetNewPasswordCheck();

    // 전체 폼 초기화
    resetForm();
};

// 모달이 완전히 닫힐 때 초기화하는 이벤트 리스너 등록
onMounted(() => {
    const modalElement = document.getElementById('passwordChangeModal');
    modalElement.addEventListener('hidden.bs.modal', CancelModify);
});

</script>

<template>
    <div class="modal fade" id="passwordChangeModal" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
        <div class="modal-dialog fixed-size">
            <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5 text-center container d-flex flex-column align-items-center" id="modalLabel">비밀번호 변경</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="d-flex flex-column align-items-center" style="margin-top: 20px; margin-bottom: 3px;">
                <div class="mb-3">
                    <label for="inputOriginalPassword" class="form-label">기존 비밀번호 <span class="text-danger">*</span></label>
                    <input type="password" v-model="originalPassword" class="form-control" placeholder="기존 비밀번호를 입력하세요" id="inputOriginalPassword">
                    <div style="height: 15px;">
                        <span v-if="originalPasswordError" class="text-danger" style="font-size: 0.7rem;">{{ originalPasswordError }}</span>
                    </div>
                </div>
                <div class="mb-3">
                    <label for="inputNewPassword" class="form-label">신규 비밀번호 <span class="text-danger">*</span></label>
                    <input type="password" v-model="newPassword" class="form-control" placeholder="새로운 비밀번호를 입력하세요" id="inputNewPassword">
                    <div style="height: 15px;">
                        <span v-if="newPasswordError" class="text-danger" style="font-size: 0.7rem;">{{ newPasswordError }}</span>
                    </div>
                </div>
                <div class="mb-3">
                    <label for="inputNewPasswordCheck" class="form-label">신규 비밀번호 확인<span class="text-danger">*</span></label>
                    <input type="password" v-model="newPasswordCheck" class="form-control" placeholder="새로운 비밀번호를 재입력하세요" id="inputNewPasswordCheck">
                    <div style="height: 15px;">
                        <span v-if="newPasswordCheckError" class="text-danger" style="font-size: 0.7rem;">{{ newPasswordCheckError }}</span>
                    </div>
                </div>
            </div>
            <div class="modal-footer  flex-column align-items-center" style="margin:3px; padding-bottom:5px;">
                <button type="button" class="btn btn-secondary" @click="ChangePassword()" style="background-color:#7248BD; border:none; margin: 3px;">비밀번호 변경</button>
                <div style="height:30px;">
                    <p v-if="errorMessage" class="text-danger text-center mt-2" style="font-size: 0.8rem; margin-bottom: 10px;">{{ errorMessage }}</p>
                </div>
            </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.form-control {
    padding: 10px;
    font-size: 0.9rem;
    border-radius: 0;
    border: 1px solid #ced4da;
    height: 40px;
    width: 320px;
}
.modal-body .d-flex {
    width: 100%; /* 부모 컨테이너 전체 너비 */
}

.fixed-size {
    width: 500px; /* 모달의 너비 고정 */
    max-width: 365px;
    min-height: 550px; /* 모달의 최소 높이 설정 */
    max-height: 550px; /* 모달의 최대 높이 설정 */
    overflow-y: hidden; /* 모달 내부 스크롤 비활성화 */
}

.modal-body {
    width: 100%; /* 모달 바디 전체 너비 */
    display: flex;
    flex-direction: column;
    align-items: center;
}
</style>
