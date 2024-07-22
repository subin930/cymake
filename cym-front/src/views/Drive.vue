<script setup>
import { RouterLink, RouterView, useRoute, useRouter } from 'vue-router'
import Header from '@/components/common/Header.vue'
import FileModifyBtn from '@/components/Drive/FileModifyBtn.vue'
import FileDeleteBtn from '@/components/Drive/FileDeleteBtn.vue'
import FileUploadBtn from '@/components/Drive/FileUploadBtn.vue'

import { ref } from "vue";
const route = useRoute();
const router = useRouter();

</script>

<template>
    <Header></Header>
    <div class="container-fluid">        
        <div class="m-3 mt-4">
            <p class="fw-bold fs-3">통합 자료실</p>
        </div>
        <div class="row d-flex align-items-center justify-content-between m-3">
            <!-- Form element -->
            <form class="input-group col-md-6 col-lg-4 mt-3 mb-3" role="search" style="width: 300px;">
                <input type="search" class="form-control" placeholder="search..." aria-label="Search">
                <button type="button" class="btn btn-outline-secondary">검색</button>
            </form>

            <!-- FileUploadBtn element -->
            <div class="col-md-6 col-lg-4 d-flex mt-3 mb-3 mx-6 justify-content-end">
                <FileUploadBtn />
                
            </div>
        </div>
        <div class="container m-3 d-flex">
            <table class="table table-hover table-bordered">
                <thead class="table-head">
                    <tr>
                    <th scope="col"> </th>
                    <th scope="col">제목</th>
                    <th scope="col">파일명</th>
                    <th scope="col">파일 크기</th>
                    <th scope="col">업로더 아이디</th>
                    <th scope="col">등록자</th>
                    <th scope="col">등록일시</th>
                    <th scope="col">수정</th>
                    <th scope="col">삭제</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                    <th scope="row">1</th>
                    <td>Mark</td>
                    <td>Otto</td>
                    <td>@mdo</td>
                    <td>@mdo</td>
                    <td>@mdo</td>
                    <td>@mdo</td>
                    <td><FileModifyBtn/></td>
                    <td><FileDeleteBtn/></td>
                    </tr>
                    <tr>
                    <th scope="row">2</th>
                    <td>Jacob</td>
                    <td>Thornton</td>
                    <td>@fat</td>
                    <td>@mdo</td>
                    <td>@mdo</td>
                    <td>@mdo</td>
                    <td><FileModifyBtn/></td>
                    <td><FileDeleteBtn/></td>
                    </tr>
                    <tr>
                    <th scope="row">3</th>
                    <td>Larry the Bird</td>
                    <td>ised</td>
                    <td>@twitter</td>
                    <td>@mdo</td>
                    <td>@mdo</td>
                    <td>@mdo</td>
                    <td><FileModifyBtn/></td>
                    <td><FileDeleteBtn/></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
    
</template>

<style scoped>
.table {
    font-size: .8rem;
}
.container {
    overflow-y: scroll;
    max-height: 60vh;
}
</style>