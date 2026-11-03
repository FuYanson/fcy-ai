<script setup>
import { computed, onMounted, onUnmounted, ref } from 'vue'

const sections = ['首页', '成长画廊', '时光手账', '百日之约']
const active = ref('首页')
const now = ref(new Date())
const lightbox = ref(null)
const playing = ref(false)
const muted = ref(false)
const volume = ref(0.32)
const note = ref('')
const invitationImage = '/invitation/invitation.jpg'
const wishes = ref([])
const album = [
  { icon: '☁', title: '初见的那一天', date: '珍藏每一次心动', cls: 'peach', photo: '/photos/first-day.jpg' },
  { icon: '✿', title: '甜甜的梦', date: '小小的你，大大的幸福', cls: 'lavender', photo: '/photos/sweet-dream.jpg' },
  { icon: '☀', title: '第一次微笑', date: '世界因你而温柔', cls: 'butter', photo: '/photos/first-smile.jpg' },
  { icon: '♡', title: '掌心里的爱', date: '每一天都值得纪念', cls: 'rose', photo: '/photos/little-hands.jpg' }
]
const timeline = [
  { day: 'DAY 01', title: '你好，世界', text: '我们的故事，从这一声啼哭开始。', icon: '✦' },
  { day: 'FIRST MONTH', title: '满月快乐', text: '小小的你，正在慢慢长大。', icon: '☾' },
  { day: 'DAY 100', title: '百日的约定', text: '愿你被爱包围，平安喜乐。', icon: '✿' },
  { day: 'TO BE CONTINUED', title: '未来的每一天', text: '还有好多好多美好，等我们一起记录。', icon: '♡' }
]
const dateLabel = computed(() => now.value.toLocaleDateString('zh-CN', { year:'numeric', month:'long', day:'numeric', timeZone:'Asia/Shanghai' }))
let clockTimer, audioContext, audioTimer, melodyIndex = 0
const notes = [392, 440, 523.25, 440, 349.23, 392, 440, 329.63, 349.23, 392, 523.25, 392, 293.66, 329.63, 349.23, 261.63]
function navigate(section) {
  active.value = section
  document.getElementById(section)?.scrollIntoView({ behavior:'smooth' })
}
function tone() {
  if (!audioContext || !playing.value || muted.value || volume.value === 0) return
  const oscillator = audioContext.createOscillator()
  const gain = audioContext.createGain()
  oscillator.type = 'sine'
  oscillator.frequency.value = notes[melodyIndex++ % notes.length]
  const start = audioContext.currentTime
  gain.gain.setValueAtTime(0.0001, start)
  gain.gain.exponentialRampToValueAtTime(Math.max(0.0001, volume.value * 0.085), start + 0.08)
  gain.gain.exponentialRampToValueAtTime(0.0001, start + 0.54)
  oscillator.connect(gain).connect(audioContext.destination)
  oscillator.start(start)
  oscillator.stop(start + 0.56)
}
async function toggleMusic() {
  if (playing.value) {
    playing.value = false
    clearInterval(audioTimer)
    return
  }
  try {
    audioContext ||= new (window.AudioContext || window.webkitAudioContext)()
    await audioContext.resume()
    playing.value = true
    tone()
    audioTimer = setInterval(tone, 650)
  } catch (e) {
    playing.value = false
  }
}
function addWish() {
  const value = note.value.trim()
  if (!value) return
  wishes.value.unshift({ text: value.slice(0, 120), id: Date.now() })
  wishes.value = wishes.value.slice(0, 8)
  note.value = ''
}
function photoError(event) { event.target.style.display = 'none' }
onMounted(() => { clockTimer = setInterval(() => now.value = new Date(), 60_000) })
onUnmounted(() => { clearInterval(clockTimer); clearInterval(audioTimer); audioContext?.close() })
</script>

<template>
  <div class="site">
    <header class="topbar">
      <a class="brand" href="#首页" @click.prevent="navigate('首页')"><span class="brand-mark">✿</span><span>Little Universe <small>宝宝成长纪念馆</small></span></a>
      <nav aria-label="主导航"><a v-for="item in sections" :key="item" :class="{ selected: active === item }" :href="'#' + item" @click.prevent="navigate(item)">{{ item }}</a></nav>
      <button class="music-button" type="button" @click="toggleMusic" :aria-pressed="playing">{{ playing ? '♫ 音乐播放中' : '♫ 开启音乐' }}</button>
    </header>

    <main>
      <section id="首页" class="hero">
        <div class="hero-sparkle s1">✧</div><div class="hero-sparkle s2">✦</div><div class="hero-sparkle s3">✧</div>
        <div class="hero-copy">
          <div class="eyebrow"><span class="line"></span> A LITTLE LOVE STORY <span class="line"></span></div>
          <div class="tiny-tag">✿ &nbsp; 写给世界上最可爱的你 &nbsp; ✿</div>
          <h1>你是落在<br/>人间的<span class="script">小星星<span class="star">✦</span></span></h1>
          <p class="hero-sub">从遇见你的那一刻起，<br/>平凡的日子都有了闪闪发光的意义。</p>
          <button class="primary" @click="navigate('成长画廊')">翻开我们的故事 <span>↗</span></button>
          <div class="hero-foot"><span>EST. WITH LOVE ♡</span><span>{{ dateLabel }}</span></div>
        </div>
        <div class="hero-art" aria-hidden="true">
          <div class="orbit orbit-one"></div><div class="orbit orbit-two"></div>
          <div class="cloud cloud-left">☁</div><div class="cloud cloud-right">☁</div>
          <div class="moon">☾</div><div class="floating-heart heart-one">♡</div><div class="floating-heart heart-two">✧</div>
          <div class="portrait"><div class="portrait-inner"><div class="baby-face">◡</div><span>HELLO, LITTLE ONE</span></div></div>
          <div class="art-caption">Every little moment is magic.</div>
        </div>
      </section>

      <section class="marquee"><span>♡ &nbsp; LITTLE MOMENTS, BIG LOVE &nbsp; ✦ &nbsp; 珍藏每一刻的温柔 &nbsp; ♡ &nbsp; LITTLE MOMENTS, BIG LOVE &nbsp; ✦ &nbsp; 珍藏每一刻的温柔 &nbsp; ♡</span></section>

      <section id="成长画廊" class="section gallery-section">
        <div class="section-head"><div><span class="kicker">01 / OUR GALLERY</span><h2>成长<span>画廊</span><span class="flower">✿</span></h2><p>把日子装进相框，把爱留在时光里。</p></div><span class="hand-note">Little memories<br/>last forever ↗</span></div>
        <div class="gallery-grid">
          <button v-for="(item,i) in album" :key="item.title" class="photo-card" :class="item.cls" @click="lightbox = item">
            <div class="photo-placeholder"><img :src="item.photo" :alt="item.title" @error="photoError" /><span class="photo-icon">{{ item.icon }}</span><span class="photo-placeholder-text">等待一张珍贵的照片</span></div>
            <div class="photo-caption"><div><h3>{{ item.title }}</h3><p>{{ item.date }}</p></div><span>↗</span></div>
            <span class="photo-number">0{{ i + 1 }}</span>
          </button>
        </div>
        <p class="gallery-tip">✧ 画廊已预留照片位置，后续可上传真实照片替换占位画面</p>
      </section>

      <section id="时光手账" class="section timeline-section">
        <div class="section-head centered"><span class="kicker">02 / OUR STORY</span><h2>写给你的<span>时光手账</span></h2><p>长大的每一步，都是值得庆祝的小奇迹。</p></div>
        <div class="timeline"><div v-for="(item,i) in timeline" :key="item.day" class="milestone"><div class="milestone-symbol">{{ item.icon }}</div><div class="milestone-body"><span class="milestone-day">{{ item.day }}</span><h3>{{ item.title }}</h3><p>{{ item.text }}</p></div><span class="milestone-index">0{{ i + 1 }}</span></div></div>
      </section>

      <section id="百日之约" class="section invitation-section">
        <div class="invite-wrap">
          <div class="invite-intro"><span class="kicker">03 / A SPECIAL DAY</span><h2>一百天的<span>小小奇迹</span></h2><p>从第一声啼哭，到第一个微笑。<br/>每一天，都是生命送来的礼物。</p><div class="invite-date">♡ &nbsp; 百日宴 · 邀请函珍藏</div><p class="muted">这里可以放入你精心设计的百日宴邀请函，作为永远珍藏的纪念。</p></div>
          <div class="invite-card"><div class="invite-border"><div class="invite-deco">✦ &nbsp; ✿ &nbsp; ✦</div><div class="invite-small">YOU ARE INVITED</div><div class="invite-script">One Hundred Days</div><div class="invite-main">百日之喜</div><div class="invite-rule"></div><div class="invite-message">时光温柔 · 见证成长<br/>愿所有美好与你不期而遇</div><div class="invite-bottom">WITH ALL OUR LOVE ♡</div><img  :src="invitationImage" alt="百日宴邀请函" @error="photoError" /></div></div>
        </div>
      </section>

      <section class="section wishes-section"><div class="section-head centered"><span class="kicker">04 / SWEET WISHES</span><h2>留下一句<span>悄悄话</span></h2><p>把想对宝宝说的话，写成一封小小的情书。</p></div><div class="wish-panel"><div class="wish-quote">“愿你慢慢长大，<br/>愿你永远拥有发现美好的眼睛。”</div><form @submit.prevent="addWish"><label for="wish">写下你的祝福 ♡</label><textarea id="wish" v-model="note" maxlength="120" placeholder="亲爱的宝贝，愿你……" rows="3"></textarea><button class="primary" type="submit">珍藏这份祝福 ↗</button></form><p class="local-note">留言仅在当前页面临时展示，刷新后会清空，不会上传到服务器。</p><div v-if="wishes.length" class="wish-list"><div v-for="w in wishes" :key="w.id">♡ {{ w.text }}</div></div></div></section>
    </main>

    <footer><div class="footer-flower">✿</div><h2>愿你的世界，永远有光。</h2><p>Made with infinite love · fcy-ai</p><span>♡ EVERY DAY IS A GIFT ♡</span></footer>

    <div v-if="lightbox" class="lightbox" role="dialog" aria-modal="true" aria-label="照片详情" @click.self="lightbox=null"><div class="lightbox-card"><button class="close" @click="lightbox=null" aria-label="关闭">×</button><div class="lightbox-image" :class="lightbox.cls"><img :src="lightbox.photo" :alt="lightbox.title" @error="photoError"/><span>{{ lightbox.icon }}</span></div><h3>{{ lightbox.title }}</h3><p>{{ lightbox.date }}</p></div></div>
    <div class="audio-panel" v-if="playing"><span>♫ 原创轻柔旋律</span><button @click="muted=!muted" :aria-label="muted?'取消静音':'静音'">{{ muted ? '🔇' : '♪' }}</button><input type="range" min="0" max="0.7" step="0.01" v-model.number="volume" aria-label="音乐音量" /></div>
  </div>
</template>

<style>
@import url('https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Noto+Serif+SC:wght@400;500;600;700&display=swap');
:root{font-family:'DM Sans',system-ui,sans-serif;color:#554a46;background:#fffaf6;font-synthesis:none}*{box-sizing:border-box}html{scroll-behavior:smooth}body{margin:0}button,a{font:inherit}button{cursor:pointer}a{text-decoration:none;color:inherit}.site{overflow:hidden}.topbar{height:88px;display:flex;align-items:center;justify-content:space-between;padding:0 max(5vw,24px);background:#fffdfb;position:relative;z-index:3;border-bottom:1px solid #f4eae3}.brand{display:flex;align-items:center;gap:12px;font-size:19px;font-weight:700;color:#b47d75;letter-spacing:.2px}.brand-mark{font-size:34px;color:#dca39e}.brand small{display:block;font-size:10px;font-weight:500;letter-spacing:2px;margin-top:3px;color:#ac9a92}.topbar nav{display:flex;gap:36px}.topbar nav a{font-size:13px;color:#8b817d;transition:.2s}.topbar nav a:hover,.topbar nav a.selected{color:#c98d85}.music-button{border:1px solid #e9cfc8;border-radius:50px;background:#fff9f7;color:#b88078;padding:11px 18px;font-size:12px}.hero{min-height:680px;display:grid;grid-template-columns:1fr 1fr;position:relative;background:radial-gradient(circle at 74% 38%,#fff3e9 0,transparent 42%),linear-gradient(115deg,#fffaf7,#fff0e9);padding:88px max(8vw,30px) 65px;gap:30px}.hero-copy{z-index:1;max-width:580px;align-self:center}.eyebrow{font-size:11px;letter-spacing:3px;color:#b9958c;display:flex;align-items:center;gap:12px}.line{width:28px;height:1px;background:#d5aaa0}.tiny-tag{margin-top:36px;font-size:13px;color:#c28d88;letter-spacing:3px}.hero h1{font-family:'Noto Serif SC',serif;font-size:clamp(43px,5vw,76px);line-height:1.45;letter-spacing:3px;color:#66514d;font-weight:600;margin:18px 0}.script{color:#d79a92;position:relative}.star{position:absolute;right:-30px;top:-12px;font-family:serif;font-size:30px;color:#e4bc86}.hero-sub{line-height:2.2;color:#9a8983;font-size:15px;margin-bottom:32px}.primary{background:#c99189;border:0;border-radius:50px;color:#fff;padding:16px 29px;box-shadow:0 9px 20px #d9aaa35c;font-size:13px;letter-spacing:1px;transition:transform .2s}.primary:hover{transform:translateY(-3px)}.primary span{margin-left:15px}.hero-foot{margin-top:72px;display:flex;gap:34px;color:#b9a49a;font-size:10px;letter-spacing:2px}.hero-art{position:relative;min-height:500px;display:flex;align-items:center;justify-content:center}.orbit{position:absolute;border:1px solid #ead7cc;border-radius:50%;transform:rotate(-17deg)}.orbit-one{width:470px;height:390px}.orbit-two{width:380px;height:470px;transform:rotate(35deg);border-style:dashed}.portrait{width:330px;height:410px;border-radius:49% 49% 45% 45%;padding:15px;background:#f6d7cc;box-shadow:25px 28px 0 #f6e5d8,0 20px 50px #e6c6b97d;transform:rotate(5deg);z-index:1}.portrait-inner{width:100%;height:100%;border-radius:48%;background:radial-gradient(circle at 45% 42%,#fff9ed,#f8e4d9);border:2px solid #fff8f3;display:flex;align-items:center;justify-content:center;flex-direction:column;gap:40px}.baby-face{width:140px;height:140px;border-radius:50%;background:#f9d3bb;display:flex;align-items:center;justify-content:center;font-size:85px;color:#a47b6a;box-shadow:inset -10px -10px 0 #f0c3ae}.portrait-inner span{font-size:11px;letter-spacing:3px;color:#b89487}.cloud{position:absolute;font-size:100px;color:#fff;z-index:2;filter:drop-shadow(0 8px 8px #e9cfc25c)}.cloud-left{left:-15px;top:50px}.cloud-right{right:-5px;bottom:65px}.moon{position:absolute;right:12%;top:5%;font-size:94px;color:#eacfae}.floating-heart{position:absolute;color:#d7a59a;font-size:48px}.heart-one{left:5%;bottom:15%}.heart-two{right:8%;top:35%}.art-caption{position:absolute;bottom:-5px;font-size:13px;font-style:italic;color:#b69b91}.hero-sparkle{position:absolute;color:#e5c7b2;font-size:24px}.s1{left:48%;top:15%}.s2{left:6%;bottom:10%}.s3{right:4%;top:10%}.marquee{background:#d5a59e;color:#fff9f4;white-space:nowrap;overflow:hidden;padding:18px 0;font-size:12px;letter-spacing:4px;text-align:center}.section{padding:110px max(8vw,28px)}.section-head{display:flex;justify-content:space-between;align-items:end;margin-bottom:48px}.section-head.centered{display:block;text-align:center}.kicker{font-size:11px;letter-spacing:3px;color:#c78d86}.section h2{font-family:'Noto Serif SC',serif;font-size:clamp(31px,3.4vw,48px);color:#5f504a;font-weight:600;margin:18px 0}.section h2 span{color:#d29b93}.section-head p{color:#a3958f;font-size:14px}.flower{font-size:28px;margin-left:12px}.hand-note{font-family:serif;font-size:20px;font-style:italic;line-height:1.5;color:#d4aaa2;transform:rotate(-9deg)}.gallery-section{background:#fffdfb}.gallery-grid{display:grid;grid-template-columns:repeat(4,1fr);gap:22px}.photo-card{border:0;background:#fff;padding:10px 10px 18px;text-align:left;box-shadow:0 10px 35px #ab8c7a1c;position:relative;transition:transform .25s}.photo-card:hover{transform:translateY(-9px) rotate(-1deg)}.photo-placeholder{height:270px;display:flex;align-items:center;justify-content:center;flex-direction:column;gap:10px;position:relative;overflow:hidden}.photo-placeholder img,.invite-border>img,.lightbox-image img{position:absolute;inset:0;width:100%;height:100%;object-fit:cover;z-index:2}.peach .photo-placeholder,.lightbox-image.peach{background:linear-gradient(140deg,#f5ddd0,#faeae1)}.lavender .photo-placeholder,.lightbox-image.lavender{background:linear-gradient(140deg,#e9e1ed,#f7eef3)}.butter .photo-placeholder,.lightbox-image.butter{background:linear-gradient(140deg,#f4ebcf,#fff6e5)}.rose .photo-placeholder,.lightbox-image.rose{background:linear-gradient(140deg,#efd6d9,#f9e7e7)}.photo-icon{font-size:85px;color:#fff8f4;text-shadow:0 3px 15px #b89c9059}.photo-placeholder-text{font-size:11px;color:#b9a6a1;letter-spacing:1px}.photo-caption{display:flex;justify-content:space-between;align-items:center;padding:17px 8px 0}.photo-caption h3{margin:0 0 7px;font-family:'Noto Serif SC',serif;font-size:16px}.photo-caption p{margin:0;font-size:11px;color:#aa9b95}.photo-caption>span{font-size:22px;color:#d5a29a}.photo-number{position:absolute;top:20px;left:20px;font-size:11px;color:#b58e86;z-index:2}.gallery-tip{text-align:center;color:#b8aaa3;font-size:12px;margin:36px 0 0}.timeline-section{background:#fcf5f0}.timeline{max-width:900px;margin:65px auto 0;display:grid;grid-template-columns:1fr 1fr;gap:25px}.milestone{background:#fffdfa;border:1px solid #f1e5dd;border-radius:8px;padding:32px;display:flex;gap:24px;position:relative;box-shadow:0 12px 24px #bda59710}.milestone-symbol{background:#faeee9;width:62px;height:62px;flex-shrink:0;border-radius:50%;display:grid;place-items:center;color:#ce9a8d;font-size:29px}.milestone-day{font-size:10px;letter-spacing:2px;color:#c5a096}.milestone h3{font-family:'Noto Serif SC',serif;font-size:20px;margin:9px 0}.milestone p{font-size:12px;color:#a49891;line-height:1.9}.milestone-index{position:absolute;right:24px;top:20px;color:#e7d9d1;font-size:12px}.invitation-section{background:#fffdfa}.invite-wrap{display:grid;grid-template-columns:1fr 1fr;align-items:center;gap:12%;max-width:1150px;margin:auto}.invite-intro p{line-height:2.2;color:#9d8d86;font-size:15px}.invite-date{display:inline-block;margin:26px 0;background:#f9ece6;border-radius:50px;padding:12px 20px;color:#b9827a;font-size:13px}.invite-intro .muted{font-size:12px;color:#b7aaa3}.invite-card{background:#e9d1c5;padding:20px;max-width:440px;width:100%;margin:auto;box-shadow:18px 20px 0 #f6e8df;transform:rotate(2deg)}.invite-border{border:1px solid #fff7ee;min-height:540px;position:relative;text-align:center;background:#fff9f2;display:flex;flex-direction:column;align-items:center;justify-content:center;padding:30px}.invite-deco{color:#d4a995;font-size:23px}.invite-small{font-size:10px;letter-spacing:4px;color:#bca096;margin-top:32px}.invite-script{font-family:Georgia,serif;font-style:italic;font-size:31px;color:#c5948b;margin-top:18px}.invite-main{font-family:'Noto Serif SC',serif;font-size:48px;color:#b98b7e;margin-top:25px}.invite-rule{width:70px;height:1px;background:#dcb8a8;margin:28px auto}.invite-message{font-size:13px;line-height:2.3;color:#a48b80}.invite-bottom{position:absolute;bottom:30px;font-size:10px;letter-spacing:3px;color:#c6a397}.wishes-section{background:#fdf5f1}.wish-panel{max-width:680px;margin:auto;text-align:center;background:#fffdfa;padding:40px 50px;box-shadow:0 15px 40px #d8bcb020}.wish-quote{font-family:'Noto Serif SC',serif;font-size:21px;line-height:2;color:#b58a83;margin-bottom:25px}.wish-panel form{text-align:left}.wish-panel label{display:block;color:#a58c83;font-size:13px;margin-bottom:10px}.wish-panel textarea{width:100%;resize:vertical;border:1px solid #eadbd3;border-radius:5px;padding:14px;font:inherit;color:#66514d;background:#fff;outline-color:#d3a19b}.wish-panel .primary{margin-top:14px}.local-note{font-size:11px;color:#b2a39d}.wish-list{margin-top:22px;text-align:left}.wish-list div{border-top:1px solid #f3e6e0;padding:12px 0;color:#a18078;font-size:13px}footer{background:#c9968e;color:#fff;text-align:center;padding:75px 20px}footer .footer-flower{font-size:35px}footer h2{font-family:'Noto Serif SC',serif;font-size:29px;font-weight:500}footer p{font-size:13px;color:#fff0e9}footer span{display:block;margin-top:36px;font-size:10px;letter-spacing:4px}.lightbox{position:fixed;inset:0;background:#332d2bd9;display:grid;place-items:center;z-index:10;padding:24px}.lightbox-card{position:relative;background:#fffdfb;padding:20px 20px 30px;text-align:center;max-width:460px;width:100%}.close{position:absolute;right:12px;top:4px;font-size:30px;border:0;background:none;z-index:4;color:#765f58}.lightbox-image{height:350px;position:relative;display:grid;place-items:center}.lightbox-image span{font-size:120px;color:#fff}.lightbox-card h3{font-family:'Noto Serif SC',serif}.lightbox-card p{font-size:13px;color:#a99791}.audio-panel{position:fixed;right:24px;bottom:24px;z-index:5;background:#fffdfbf2;box-shadow:0 5px 25px #94766d45;border:1px solid #f0e1da;border-radius:50px;display:flex;align-items:center;gap:10px;padding:10px 17px;color:#b9847c;font-size:12px}.audio-panel button{background:none;border:0;color:#b9847c}.audio-panel input{width:72px;accent-color:#c99189}
@media(max-width:900px){.topbar{height:auto;min-height:75px;flex-wrap:wrap;gap:12px;padding:16px 20px}.topbar nav{order:3;width:100%;justify-content:space-between;gap:8px}.topbar nav a{font-size:12px}.hero{grid-template-columns:1fr;padding:65px 30px}.hero-art{min-height:450px}.hero-foot{margin-top:40px}.gallery-grid{grid-template-columns:repeat(2,1fr)}.invite-wrap{grid-template-columns:1fr;gap:60px}.section{padding:75px 28px}}@media(max-width:540px){.brand{font-size:16px}.brand-mark{font-size:27px}.music-button{font-size:11px;padding:9px 12px}.hero{padding:54px 22px}.hero h1{font-size:45px}.hero-art{min-height:340px}.portrait{width:225px;height:290px}.baby-face{width:100px;height:100px;font-size:60px}.orbit-one{width:310px;height:260px}.orbit-two{width:270px;height:340px}.cloud{font-size:70px}.moon{font-size:65px}.gallery-grid{gap:14px}.photo-placeholder{height:175px}.photo-caption h3{font-size:13px}.photo-caption p{font-size:10px}.hand-note{display:none}.timeline{grid-template-columns:1fr}.invite-border{min-height:450px}.invite-main{font-size:39px}.wish-panel{padding:30px 20px}.audio-panel{right:10px;bottom:10px}.section h2{font-size:32px}}
</style>