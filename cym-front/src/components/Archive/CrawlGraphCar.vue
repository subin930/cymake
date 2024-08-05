<template>
    <div>
      <LineChart :chartData="preparedChartData" :options="chartOptions" />
    </div>
</template>
  
<script setup>
  import { ref, computed, onMounted } from 'vue';
  import { LineChart } from 'vue-chart-3';
  import { Chart, registerables } from 'chart.js';
  
  // Chart.js 컴포넌트에 필요한 모든 차트 타입을 등록
  Chart.register(...registerables);
  
  const props = defineProps({
    // 예상되는 데이터 구조를 명시적으로 정의 (예: 각 월의 데이터를 포함하는 배열)
    crwlData: Array,
    title: String,
  });
  
  const chartOptions = ref({
    responsive: false,
    plugins: {
      legend: {
        display: false,
        position: 'top',
      },
      title: {
        display: true,
        text: props.title,
      }
    }
  });
  
  // props로 받은 데이터를 사용하여 chartData를 설정
  const preparedChartData = computed(() => ({
    
    labels: props.crwlData.map(item => item.date),
    datasets: [
      {
        label: ' ',
        data: props.crwlData.map(item => item.total),
        fill: false,
        borderColor: 'rgb(75, 192, 192)',
        tension: 0.1
      }
    ]
  }));
  
  const check = () => {
    console.log(props.crwlData.date);
    console.log(props.crwlData.total);
  }
onMounted(() => {
  console.log(props.crwlData);
});
onMounted(check);
</script>
  