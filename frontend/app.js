/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
document.querySelectorAll('.tab').forEach(button => button.onclick = () => {
  document.querySelectorAll('.tab,.view').forEach(el => el.classList.remove('active'));
  button.classList.add('active');
  document.getElementById(button.dataset.view).classList.add('active');
});

const fields = {
  plan: document.getElementById('plan'), script: document.getElementById('script'),
  avatar: document.getElementById('avatar'), voice: document.getElementById('voice'),
  ratio: document.getElementById('ratio'), avatarAuth: document.getElementById('avatarAuth'),
  voiceAuth: document.getElementById('voiceAuth'), disclosure: document.getElementById('disclosure'),
  segments: document.getElementById('segments'), summary: document.getElementById('summary')
};

fields.plan.onclick = async () => {
  fields.plan.disabled = true;
  fields.plan.textContent = '正在编排口播…';
  const payload = {script: fields.script.value, avatarId: fields.avatar.value, voiceId: fields.voice.value,
    aspectRatio: fields.ratio.value, avatarAuthorized: fields.avatarAuth.checked,
    voiceAuthorized: fields.voiceAuth.checked, disclosureEnabled: fields.disclosure.checked};
  let result;
  try {
    const response = await fetch('http://localhost:8080/api/digitalhuman/plan', {method: 'POST', headers: {'Content-Type': 'application/json'}, body: JSON.stringify(payload)});
    if (!response.ok) throw new Error('API unavailable');
    result = await response.json();
  } catch (error) {
    const rows = payload.script.trim().split(/(?<=[。！？!?；;])/).filter(Boolean);
    result = {status: payload.avatarAuthorized && payload.voiceAuthorized ? 'READY' : 'BLOCKED', segments: rows.map((text, index) => ({sequence: index + 1, text, seconds: Math.max(2, Math.round(text.length / 3.8 * 10) / 10), visual: index % 2 ? '中景 + 要点字幕' : '半身正面 + 品牌背景'}))};
    result.estimatedSeconds = result.segments.reduce((sum, item) => sum + item.seconds, 0);
  }
  render(result);
  setTimeout(() => { fields.plan.disabled = false; fields.plan.innerHTML = '分析并生成播报段落 <span>→</span>'; }, 400);
};

function render(result) {
  fields.summary.textContent = `${result.segments.length} 段 · 预计 ${result.estimatedSeconds.toFixed(1)} 秒 · ${result.status}`;
  fields.segments.innerHTML = result.segments.map((item, index) => `<div><i${result.status === 'BLOCKED' ? ' class="bad"' : ''}>${String(item.sequence).padStart(2, '0')}</i><span><b>${item.text}</b><small>${item.visual} · ${item.seconds.toFixed(1)} 秒</small></span><em>${index === 0 ? '开场' : index === result.segments.length - 1 ? '收束' : '主体'}</em></div>`).join('');
}
