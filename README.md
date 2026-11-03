# fcy-ai · 宝宝成长纪念馆

一个基于 Vue 3 + Vite 的宝宝成长档案门户。包含成长首页、照片画廊、时光手账、百日宴邀请函展示、祝福留言和原创旋律播放器。

## 本地开发
```bash
cd frontend
npm install
npm run dev
```
打开 http://127.0.0.1:5173 。

## 替换照片和邀请函
将照片放在 `frontend/public/photos/`，文件名依次为 `first-day.jpg`、`sweet-dream.jpg`、`first-smile.jpg`、`little-hands.jpg`。
邀请函图片放在 `frontend/public/invitation/invitation.jpg`。没有文件时会显示设计好的占位画面。不要将宝宝真实隐私信息提交到公开仓库。

## 音乐
点击顶部“开启音乐”后使用浏览器 Web Audio API 播放项目内置的原创简短旋律；支持暂停、静音、调节音量，无需第三方音乐版权或音频文件。由于浏览器限制，必须由访客主动点击开始播放。

## 后端
`backend/` 保留 Spring Boot Java 17 项目，并提供 `GET /api/time` 以及 `GET /api/baby/profile`、`GET /api/baby/milestones` 演示接口。它们返回不含个人隐私的展示数据。可用 `cd backend && mvn spring-boot:run` 启动。

## Cloudflare Pages
将 GitHub 仓库 `FuYanson/fcy-ai` 连接 Cloudflare Pages。Root directory: `frontend`，Build command: `npm run build`，Build output directory: `dist`，分支 `main`。
线上 `/api/time` 由 `frontend/functions/api/time.js` 提供，Spring Boot 不会在 Pages 上运行。前端当前使用静态展示数据，后端 API 为后续扩展预留。

## 隐私
公开演示版不包含真实姓名、出生日期、宝宝照片等个人信息。祝福仅保留在当前页面内存，刷新即消失，不会上传服务器。
