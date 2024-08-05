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
    salesData: Array
  });
  
  const chartOptions = ref({
    responsive: true,
    plugins: {
      legend: {
        position: 'top',
      },
      title: {
        display: true,
        text: 'Monthly Sales Data'
      }
    }
  });
  
  // props로 받은 데이터를 사용하여 chartData를 설정
  const preparedChartData = computed(() => ({
    labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
    datasets: [
      {
        label: 'Monthly Sales',
        data: props.salesData,
        fill: false,
        borderColor: 'rgb(75, 192, 192)',
        tension: 0.1
      }
    ]
  }));
  
  const check = () => {
    console.log(props.salesData);
  }
onMounted(() => {
  console.log(props.salesData);
});
  onMounted(check);
</script>
  