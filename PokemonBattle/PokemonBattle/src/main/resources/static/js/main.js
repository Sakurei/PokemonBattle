// ===== API BASE URL =====
const API_BASE = '/api/game';

// ===== UTILITY FUNCTIONS =====
function displayResponse(data) {
    // Kita gunakan console.log agar tidak mengganggu UI baru
    console.log("API Response:", data);
}

async function makeRequest(endpoint, options = {}) {
    try {
        const response = await fetch(`${API_BASE}${endpoint}`, {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const data = await response.json();
        displayResponse(data);
        return data;
    } catch (error) {
        console.error('Error:', error);
        return null;
    }
}

// ===== UI STATE =====
// Data dummy ini nantinya bisa kamu ganti dengan hasil fetch dari backend
const POKEMON = [
  {name:'Charizard',sprite:'🔥',type:'fire',hp:78,maxhp:78,atk:84,def:78,spd:100,level:75},
  {name:'Blastoise',sprite:'💧',type:'water',hp:79,maxhp:79,atk:83,def:100,spd:78,level:72},
  {name:'Venusaur',sprite:'🌿',type:'grass',hp:80,maxhp:80,atk:82,def:83,spd:80,level:70},
  {name:'Emboar',sprite:'🐗',type:'fire',hp:110,maxhp:110,atk:123,def:65,spd:65,level:68},
  {name:'Zebstrika',sprite:'⚡',type:'electric',hp:75,maxhp:75,atk:100,def:63,spd:116,level:65},
];

// ===== NAVIGATION =====
function goTo(pageName) {
  window.location.href = '/' + pageName;
}

// ===== INITIALIZATION =====
document.addEventListener('DOMContentLoaded', async () => {
  const pageId = document.body.getAttribute('data-page');
  
  if (pageId === 'collection') {
    renderCollection();
    // Contoh untuk integrasi backend nantinya:
    // const collectionData = await makeRequest('/pokemon/collection');
    // renderCollectionWithRealData(collectionData);
  } else if (pageId === 'battle-home') {
    renderBattlePick();
  } else if (pageId === 'home') {
      // Init player saat masuk home
      // await makeRequest(`/init?playerName=Admin12345`, { method: 'POST' });
  } else if (pageId === 'battle-ingame') {
      // Start battle saat masuk arena
      // await makeRequest('/battle/start', { method: 'POST' });
  }
});

// ===== RENDER LOGIC =====
function renderCollection(){
  const list = document.getElementById('pokemon-list');
  if (!list) return;
  list.innerHTML = POKEMON.map((p,i)=>`
    <div class="pokemon-card" onclick="showDetail(${i})" id="pkcard-${i}">
      <div class="pokemon-sprite">${p.sprite}</div>
      <div style="flex:1">
        <div style="display:flex;align-items:center;gap:6px;margin-bottom:2px">
          <span class="pokemon-name">${p.name}</span>
          <span class="type-badge type-${p.type}">${p.type}</span>
        </div>
        <div class="pokemon-hp-text">${p.hp}/${p.maxhp} HP</div>
        <div class="stat-bar"><div class="stat-fill fill-green" style="width:${Math.round(p.hp/p.maxhp*100)}%"></div></div>
      </div>
    </div>
  `).join('');
}

function showDetail(i){
  document.querySelectorAll('.pokemon-card').forEach(c=>c.classList.remove('selected'));
  const selectedCard = document.getElementById('pkcard-'+i);
  if (selectedCard) selectedCard.classList.add('selected');
  const p = POKEMON[i];
  const panel = document.getElementById('detail-panel');
  if (!panel) return;
  const hpPct = Math.round(p.hp/p.maxhp*100);
  const atkPct = Math.round(p.atk/150*100);
  const defPct = Math.round(p.def/150*100);
  const spdPct = Math.round(p.spd/150*100);
  panel.innerHTML = `
    <div class="detail-sprite">${p.sprite}</div>
    <div style="display:flex;align-items:center;justify-content:space-between;margin-bottom:6px">
      <div class="detail-name">${p.name}</div>
      <span class="type-badge type-${p.type}">${p.type}</span>
    </div>
    <div style="font-size:11px;color:var(--text3);margin-bottom:2px">Level ${p.level}</div>
    <div class="detail-stats">
      <div class="detail-stat-row">
        <span class="detail-stat-lbl">HP</span>
        <div class="stat-bar" style="flex:1"><div class="stat-fill fill-green" style="width:${hpPct}%"></div></div>
        <span class="detail-stat-val">${p.hp}</span>
      </div>
      <div class="detail-stat-row">
        <span class="detail-stat-lbl">Atk</span>
        <div class="stat-bar" style="flex:1"><div class="stat-fill" style="width:${atkPct}%;background:var(--red)"></div></div>
        <span class="detail-stat-val">${p.atk}</span>
      </div>
      <div class="detail-stat-row">
        <span class="detail-stat-lbl">Def</span>
        <div class="stat-bar" style="flex:1"><div class="stat-fill" style="width:${defPct}%;background:var(--blue)"></div></div>
        <span class="detail-stat-val">${p.def}</span>
      </div>
      <div class="detail-stat-row">
        <span class="detail-stat-lbl">Spd</span>
        <div class="stat-bar" style="flex:1"><div class="stat-fill" style="width:${spdPct}%;background:var(--yellow)"></div></div>
        <span class="detail-stat-val">${p.spd}</span>
      </div>
    </div>
    <button class="btn btn-red btn-sm" style="width:100%;justify-content:center;margin-top:16px" onclick="goTo('battle-home')">⚔️ Gunakan di Battle</button>
  `;
}

function renderBattlePick(){
  const grid = document.getElementById('battle-pick-grid');
  if (!grid) return;
  grid.innerHTML = POKEMON.slice(0,3).map((p,i)=>`
    <div class="battle-pick-card ${i===0?'selected':''}" onclick="selectPick(this)" id="pick-${i}">
      <div class="pick-sprite">${p.sprite}</div>
      <div class="pick-name">${p.name}</div>
      <div class="pick-level">Lv. ${p.level}</div>
      <div style="margin-top:8px">
        <div style="font-size:9px;color:var(--text3);margin-bottom:3px">HP ${p.hp}/${p.maxhp}</div>
        <div class="stat-bar"><div class="stat-fill fill-green" style="width:${Math.round(p.hp/p.maxhp*100)}%"></div></div>
      </div>
      <span class="type-badge type-${p.type}" style="margin-top:8px;display:inline-block">${p.type}</span>
    </div>
  `).join('');
}

function selectPick(el){
  document.querySelectorAll('.battle-pick-card').forEach(c=>c.classList.remove('selected'));
  el.classList.add('selected');
}

// ===== BATTLE LOGIC =====
let logCount = 0;
async function addLog(){
  const log = document.querySelector('.battle-log');
  if (!log) return;
  
  // Nanti temanmu bisa mengaktifkan baris ini untuk memanggil endpoint attack:
  // const attackResult = await makeRequest(`/battle/attack?skillIndex=0`, { method: 'POST' });
  
  logCount++;
  const entries = ['Charizard menggunakan Flamethrower!','Agumon terkena 12 damage!','Agumon menggunakan Thunder!','Charizard terkena 8 damage!'];
  const div = document.createElement('div');
  div.className = 'log-entry';
  div.textContent = entries[logCount % entries.length];
  log.appendChild(div);
  log.scrollTop = log.scrollHeight;
  if(logCount >= 3) {
      // End battle
      // await makeRequest('/battle/end', { method: 'POST' });
      setTimeout(()=>goTo('battle-result'),400);
  }
}

// ===== RESULT LOGIC =====
let isWin = true;
function toggleResult(){
  isWin = !isWin;
  document.getElementById('result-icon').textContent = isWin ? '🏆' : '💀';
  const title = document.getElementById('result-title');
  title.textContent = isWin ? 'VICTORY!' : 'DEFEATED...';
  title.className = 'result-title ' + (isWin?'win':'lose');
  const badge = document.getElementById('result-badge');
  badge.textContent = isWin ? '⭐ Kamu berhasil mengalahkan lawan!' : '💔 Kamu berhasil dikalahkan lawan...';
  badge.className = 'result-badge ' + (isWin?'win':'lose');
  document.getElementById('res-menang').textContent = isWin ? '1' : '0';
  document.getElementById('res-winrate').textContent = isWin ? '100%' : '0%';
  document.getElementById('result-tip').textContent = isWin
    ? '✨ Pokémon kamu semakin kuat! Terus berlatih!'
    : '😔 Gunakan item di inventory untuk memulihkan Pokémon dan coba lagi!';
}

// ===== API WRAPPERS (Untuk digunakan temanmu nanti) =====
async function doAttack(skillIndex) {
    const result = await makeRequest(`/battle/attack?skillIndex=${skillIndex}`, { method: 'POST' });
    console.log("Attack result:", result);
    return result;
}

async function doUseItem(itemIndex) {
    const result = await makeRequest(`/battle/use-item?itemIndex=${itemIndex}`, { method: 'POST' });
    console.log("Item used:", result);
    return result;
}