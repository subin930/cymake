<template>
    <Popper>
      <button type="button" class="btn btn-secondary mx-4" style="border-radius: 20px; background-color: #7248BD;">파일등록</button>
      <template #content>
        <div class="align-items-center justify-content-center">
            <div class="row w-100 text-center mb-3">
                <p style="font-size: 1.1rem; font-weight:bold">파일등록</p>
            </div>
            <form @submit.prevent="submitForm">
              <div class="form-group mb-3">
                <label for="title" class="px-2" style="font-size: 0.9rem">제목</label>
                <input type="text" class="w-75" style="font-size: 0.9rem" id="title" v-model="title" required />
              </div>
              <div class="form-group mb-3">
                <label for="file" class="px-2" style="font-size: 0.9rem">파일첨부</label>
                <input type="file"style="font-size: 0.9rem" id="file" @change="handleFileUpload" required />
                <p class="px-2" style="font-size: .8rem">현재 {{ fileSize }}MB / (첨부파일 : 30MB로 제한)</p>
              </div>
              <div class="row button-group d-flex justify-content-center mb-2">
                <button type="button" @click="close" class="btn btn-secondary cancel-button col-3 mx-3" style="border-radius: 20px; font-size:0.9rem">취소</button>
                <button type="submit" class="btn btn-secondary submit-button col-3" style="border-radius: 20px; font-size: 0.9rem; background-color: #7248BD;">등록</button>
              </div>
            </form>
        </div>
        
      </template>
    </Popper>
</template>
  
<script setup>
  import { defineComponent, ref } from "vue";
  import Popper from "vue3-popper";
  
  const title = ref('');
  const fileSize = ref(0);

  const handleFileUpload = (event) => {
    const file = event.target.files[0];
    if (file) {
      fileSize.value = (file.size / (1024 * 1024)).toFixed(2); // MB 단위로 변환
    }
  };

</script>

<style>
:root {
  --popper-theme-background-color: #ffffff;
  --popper-theme-background-color-hover: #ececec;
  --popper-theme-text-color: #000000;
  --popper-theme-border-radius: 18px;
  --popper-theme-border-width: 1px;
  --popper-theme-border-style: solid;
  --popper-theme-border-color: #c9c9c9;
  --popper-theme-padding: 32px;
  
}
</style>