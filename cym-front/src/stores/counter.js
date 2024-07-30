import { ref, computed } from 'vue'
import { defineStore } from 'pinia'


export const searchStore = defineStore('search', () => {
  //통합 검색 시 검색 정보 저장
  const searchQuery = ref('')

  const setSearchQuery = (newSearchBody) => {
    searchQuery.value = newSearchBody;
  }

  return { searchQuery,  setSearchQuery }
});