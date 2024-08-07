<template>
    <div class="modal fade" id="confirmUnregisterModal" tabindex="-1" aria-labelledby="confirmUnregisterModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="confirmUnregisterModalLabel">회원 탈퇴 확인</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            정말로 탈퇴하시겠습니까?
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
            <button type="button" class="btn btn" style = "color:#FFFFFF; border-color:#FFFFFF; background-color: #6b42db;" @click="$emit('confirm')">탈퇴</button>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  // 아무것도 필요하지 않습니다.
  </script>
  
  <style scoped>
  .modal-content {
    border-radius: 8px;
  }
  .modal-header {
    background-color: white;
    color: black;
  }
  </style>
  