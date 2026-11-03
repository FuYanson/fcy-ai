<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const time = ref('等待服务器时间...')
const error = ref('')
let timer

async function refresh() {
  try {
    const response = await fetch('/api/time', { cache: 'no-store' })
    if (!response.ok) throw new Error('HTTP ' + response.status)
    const serverTime = await response.text()
    time.value = new Date(serverTime).toLocaleTimeString('zh-CN', {
      timeZone: 'Asia/Shanghai', hour12: false
    })
    error.value = ''
  } catch (e) {
    error.value = '连接后端失败：' + e.message
  }
}

onMounted(() => {
  refresh()
  timer = setInterval(refresh, 1000)
})
onUnmounted(() => clearInterval(timer))
</script>

<template>
  <main>
    <h1>服务器实时时钟</h1>
    <div class="clock">{{ time }}</div>
    <p>北京时间（Asia/Shanghai）</p>
    <p v-if="error" class="error">{{ error }}</p>
  </main>
</template>

<style>
body { margin: 0; font-family: system-ui, sans-serif; background: #101827; color: white; }
main { min-height: 100vh; display: flex; flex-direction: column; justify-content: center; align-items: center; }
.clock { font-size: clamp(3rem, 10vw, 7rem); font-weight: 700; font-variant-numeric: tabular-nums; }
p { color: #aabbd0; }
.error { color: #ff9090; }
</style>
