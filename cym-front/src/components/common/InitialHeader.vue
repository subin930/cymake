<script setup>
import { ref } from "vue";
import { useRoute, useRouter } from "vue-router";
const route = useRoute();
const router = useRouter();

</script>



<template>
    <header class="d-flex flex-wrap justify-content-center px-3 py-2 border-bottom" style="border-color: #00A5BD;">
        <div class="d-flex">
            <a href="/login">
                <img src="@/assets/CYMAKE.jpg" style="height: 3rem;"/>
            </a>
        </div>
    </header>
</template>