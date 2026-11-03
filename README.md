# 最小前后端时钟

Java 17 + Spring Boot 3 后端提供服务器当前时间，Vue 3 + Vite 前端每秒获取并显示北京时间。

## 启动

需要 JDK 17+、Maven、Node.js 18+。

终端一：
```bash
cd backend
mvn spring-boot:run
```

终端二：
```bash
cd frontend
npm install
npm run dev
```

浏览器打开 http://127.0.0.1:5173

## 验证

```bash
curl http://localhost:8080/api/time
cd frontend && npm run build
```

接口返回 ISO 8601 UTC 时间字符串，如 `2026-11-03T02:15:00Z`。前端通过 Vite 代理访问后端，每秒请求一次，按 Asia/Shanghai 时区显示。服务器系统时钟是唯一时间源。

## Cloudflare Pages 免费部署（线上使用 Pages Functions）

线上仍由 Vue 前端每秒请求 `GET /api/time`，但这个接口由 `frontend/functions/api/time.js` 在 Cloudflare 边缘运行时提供。原 Spring Boot 后端保留，供本地运行和学习。

1. 登录 https://dash.cloudflare.com/ ，进入 **Workers & Pages**。
2. 选择 **Create application → Pages → Connect to Git**，授权 Cloudflare 访问 `FuYanson/fcy-ai`。
3. 选择 `main` 分支，使用以下构建设置：
   - **Framework preset**: Vue（或 None）
   - **Root directory**: `frontend`
   - **Build command**: `npm run build`
   - **Build output directory**: `dist`（若页面要求相对于仓库根目录，则填写 `frontend/dist`）
   - **Environment variable**: 可选 `NODE_VERSION=20`
4. 点击 **Save and Deploy**。部署成功后访问 Cloudflare 分配的 `https://<project>.pages.dev`。
5. 验证 `https://<project>.pages.dev/api/time` 返回 ISO 8601 UTC 时间文本，首页每秒显示北京时间。

注意：必须创建 **Pages** 项目并连接 Git 仓库，不能只上传构建后的 `dist`，否则 `functions/` 接口不会自动生效。免费额度和账号资格以 Cloudflare 控制台实际展示为准。
