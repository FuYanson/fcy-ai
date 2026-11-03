// Cloudflare Pages Function: deployed at GET /api/time.
// The browser receives the server-side current time, not its own clock.
export function onRequestGet() {
  return new Response(new Date().toISOString(), {
    headers: {
      'Content-Type': 'text/plain; charset=utf-8',
      'Cache-Control': 'no-store'
    }
  })
}
