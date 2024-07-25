<script setup>
    const props=defineProps({
        title: String,
        imgUrl: String,
        newsLink: String,
    });

</script>

<template>
    <!-- Modal --> 
    <div class="modal fade" id="newsModal" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="modalLabel" style="font-weight: bold">{{title}}</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <img :src="imgUrl" alt="news image" class="news-image">
                <br>
                <p>뉴스 링크</p>
                {{ newsLink }}
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
            </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.news-image {
    width: 100%;
    height: auto;
    display: block;
    margin-bottom: 10px;
}
</style>