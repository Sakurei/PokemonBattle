// ===== API BASE URL =====
const API_BASE = '/api/game';

// ===== GLOBAL STATE =====
let POKEMON = [];
let currentPlayer = null;
let currentBattleState = null;
let INVENTORY = [];
let selectedPokemonIndex = 0;
let battleActive = false;
let turnCount = 0;

// ===== UTILITY FUNCTIONS =====
function displayResponse(data) {
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

// ===== LOAD PLAYER DATA FROM BACKEND =====
async function loadPlayerData() {
    const playerName = localStorage.getItem('playerName') || 'Admin12345';
    
    // Initialize game
    const initResponse = await makeRequest(`/init?playerName=${encodeURIComponent(playerName)}`, { method: 'POST' });
    
    if (initResponse && initResponse.player) {
        currentPlayer = initResponse.player;
        localStorage.setItem('playerName', currentPlayer.name);
        
        // Load Pokemon collection
        await loadPokemonCollection();
    }
}

async function loadPokemonCollection() {
    const response = await makeRequest('/pokemon/collection');
    
    if (response && response.collection) {
        // Convert backend Pokemon objects to UI format
        POKEMON = response.collection.map(p => ({
            name: p.name,
            sprite: getPokemonSprite(p.type),
            type: p.type.toLowerCase(),
            hp: p.hp,
            maxhp: p.maxHp,
            atk: p.attack,
            def: p.defense,
            spd: p.speed || 80, // Default speed if not available
            level: p.level || 50,
            skills: p.skills || []
        }));
        
        return POKEMON;
    }
    return [];
}

// ===== LOAD INVENTORY FROM BACKEND =====
async function loadInventory() {
    const response = await makeRequest('/pokemon/collection');
    
    if (response && response.collection) {
        // Get items from player's inventory (from backend if available)
        // For now, creating sample items based on ItemData.java
        INVENTORY = [
            { name: 'Potion', icon: '💊', amount: 2, description: 'Restores 30 HP', index: 0 },
            { name: 'Antidote', icon: '🧴', amount: 1, description: 'Cures poisoning', index: 1 },
            { name: 'Full Restore', icon: '💊', amount: 5, description: 'Fully restores HP', index: 2 },
            { name: 'Full Revive', icon: '❤️', amount: 3, description: 'Revives fainted Pokemon', index: 3 }
        ];
        
        return INVENTORY;
    }
    return [];
}

// ===== POKEMON SPRITE MAPPER =====
function getPokemonSprite(type) {
    const sprites = {
        'FIRE': '🔥',
        'WATER': '💧',
        'GRASS': '🌿',
        'ELECTRIC': '⚡',
        'ICE': '❄️',
        'FIGHTING': '👊',
        'POISON': '☠️',
        'GROUND': '🪨',
        'FLYING': '🦅',
        'PSYCHIC': '🧠',
        'BUG': '🐛',
        'ROCK': '⛰️',
        'GHOST': '👻',
        'DRAGON': '🐉',
        'DARK': '🌑',
        'STEEL': '⚙️',
        'FAIRY': '✨'
    };
    return sprites[type] || '🟡';
}

// ===== NAVIGATION =====
function goTo(pageName) {
  window.location.href = '/' + pageName;
}

// ===== INITIALIZATION =====
document.addEventListener('DOMContentLoaded', async () => {
  // Load player data from backend first
  await loadPlayerData();
  await loadInventory();
  
  const pageId = document.body.getAttribute('data-page');
  
  if (pageId === 'collection') {
    renderCollection();
  } else if (pageId === 'battle-home') {
    renderBattlePick();
  } else if (pageId === 'home') {
      renderHomeStats();
  } else if (pageId === 'inventory') {
      renderInventory();
  } else if (pageId === 'battle-ingame') {
      await initBattle();
  } else if (pageId === 'battle-result') {
      renderBattleResult();
  }
});

// ===== RENDER HOME STATS =====
function renderHomeStats() {
  if (!currentPlayer) return;
  
  // Update trainer name
  const trainerBadge = document.querySelector('.badge-trainer');
  if (trainerBadge) trainerBadge.textContent = `⭐ ${currentPlayer.name}`;
  
  const trainerName = document.querySelector('.trainer-name');
  if (trainerName) trainerName.textContent = currentPlayer.name;
  
  // Update collection stats
  const collectionStat = document.querySelector('.stat-card-val');
  if (collectionStat) collectionStat.textContent = POKEMON.length;
}

// ===== INITIALIZE BATTLE =====
async function initBattle() {
  const battleResponse = await makeRequest('/battle/start', { method: 'POST' });
  
  if (battleResponse && battleResponse.battleState) {
    currentBattleState = battleResponse.battleState;
    battleActive = true;
    turnCount = 0;
    
    renderBattleArena();
    renderBattleSkills();
    renderBattleLog('Battle started! GO!');
  }
}

// ===== RENDER BATTLE ARENA =====
function renderBattleArena() {
  if (!currentBattleState) return;
  
  const playerPoke = currentBattleState.playerPokemon;
  const enemyPoke = currentBattleState.enemyPokemon;
  
  // Update enemy area
  const enemySprite = document.querySelector('.enemy-area .battle-pokemon-sprite');
  const enemyHUD = document.querySelector('.enemy-area .battle-hud');
  
  if (enemySprite && enemyPoke) {
    enemySprite.textContent = getPokemonSprite(enemyPoke.type);
  }
  
  if (enemyHUD && enemyPoke) {
    const enemyHpPct = Math.round((enemyPoke.hp / enemyPoke.maxHp) * 100);
    enemyHUD.innerHTML = `
      <div style="font-weight:bold">${enemyPoke.name}</div>
      <div style="font-size:10px;color:var(--text3);margin-bottom:4px">Lv. ${enemyPoke.level || 50}</div>
      <div style="font-size:9px;margin-bottom:2px">${enemyPoke.hp}/${enemyPoke.maxHp} HP</div>
      <div class="stat-bar" style="height:6px">
        <div class="stat-fill" style="width:${enemyHpPct}%;background:${enemyHpPct > 50 ? 'var(--green)' : enemyHpPct > 25 ? 'var(--yellow)' : 'var(--red)'}"></div>
      </div>
    `;
  }
  
  // Update player area
  const playerSprite = document.querySelector('.player-area .battle-pokemon-sprite');
  const playerHUD = document.querySelector('.player-area .battle-hud');
  
  if (playerSprite && playerPoke) {
    playerSprite.textContent = getPokemonSprite(playerPoke.type);
  }
  
  if (playerHUD && playerPoke) {
    const playerHpPct = Math.round((playerPoke.hp / playerPoke.maxHp) * 100);
    playerHUD.innerHTML = `
      <div style="text-align:right">
        <div style="font-weight:bold">${playerPoke.name}</div>
        <div style="font-size:10px;color:var(--text3);margin-bottom:4px">Lv. ${playerPoke.level || 50}</div>
        <div style="font-size:9px;margin-bottom:2px">${playerPoke.hp}/${playerPoke.maxHp} HP</div>
        <div class="stat-bar" style="height:6px">
          <div class="stat-fill" style="width:${playerHpPct}%;background:${playerHpPct > 50 ? 'var(--green)' : playerHpPct > 25 ? 'var(--yellow)' : 'var(--red)'}"></div>
        </div>
      </div>
    `;
  }
}

// ===== RENDER BATTLE SKILLS =====
async function renderBattleSkills() {
  if (!currentBattleState || !currentBattleState.playerPokemon) return;
  
  const skillsResponse = await makeRequest('/skills');
  const allSkills = skillsResponse?.skills || [];
  
  const actionPanel = document.querySelector('.battle-actions');
  if (!actionPanel) return;
  
  const playerPoke = currentBattleState.playerPokemon;
  const pokemonSkills = playerPoke.skills || [];
  
  let actionsHTML = `<div class="action-title2">Giliran ${playerPoke.name} — Apa yang kamu lakukan?</div>
    <div style="display:grid;grid-template-columns:1fr 1fr;gap:8px;margin-bottom:12px">`;
  
  // Render skills as buttons (max 4 skills)
  pokemonSkills.slice(0, 4).forEach((skill, index) => {
    actionsHTML += `
      <button class="btn btn-sm" style="background:var(--blue);color:white;cursor:pointer" onclick="performAttack(${index})">
        ${skill.name}
      </button>
    `;
  });
  
  actionsHTML += `</div>
    <div style="display:grid;grid-template-columns:1fr 1fr;gap:8px">
      <button class="btn btn-sm" style="background:var(--yellow);color:black;cursor:pointer" onclick="openItemMenu()">
        🎒 Use Item
      </button>
      <button class="btn btn-sm" style="background:var(--purple);color:white;cursor:pointer" onclick="switchPokemonBattle()">
        🔄 Switch Pokemon
      </button>
    </div>`;
  
  actionPanel.innerHTML = actionsHTML;
}

// ===== PERFORM ATTACK =====
async function performAttack(skillIndex) {
  const attackResult = await makeRequest(`/battle/attack?skillIndex=${skillIndex}`, { method: 'POST' });
  
  if (attackResult && attackResult.result) {
    turnCount++;
    currentBattleState = attackResult.battleState;
    
    // Add log entry
    renderBattleLog(attackResult.result.action);
    
    // Update arena
    renderBattleArena();
    
    // Check if battle is over
    if (!attackResult.result.battleActive) {
      await endBattle(attackResult.result.message);
    } else {
      // Re-render skills for next turn
      await renderBattleSkills();
    }
  }
}

// ===== BATTLE LOG =====
function renderBattleLog(message) {
  const log = document.querySelector('.battle-log');
  if (!log) return;
  
  const entry = document.createElement('div');
  entry.className = 'log-entry';
  entry.textContent = message;
  log.appendChild(entry);
  log.scrollTop = log.scrollHeight;
}

// ===== SWITCH POKEMON =====
async function switchPokemonBattle() {
  // Show available Pokemon for switch
  const availablePokemon = POKEMON.filter((_, i) => i !== selectedPokemonIndex && POKEMON[i].hp > 0);
  
  if (availablePokemon.length === 0) {
    renderBattleLog('No other Pokemon available!');
    return;
  }
  
  // For simplicity, switch to first available
  const newIndex = POKEMON.findIndex((_, i) => i !== selectedPokemonIndex && POKEMON[i].hp > 0);
  if (newIndex === -1) return;
  
  const switchResponse = await makeRequest(`/pokemon/switch?index=${newIndex}`, { method: 'POST' });
  
  if (switchResponse) {
    selectedPokemonIndex = newIndex;
    currentBattleState = switchResponse.battleState;
    
    renderBattleLog(`Switched to ${POKEMON[newIndex].name}!`);
    renderBattleArena();
    await renderBattleSkills();
  }
}

// ===== USE ITEM IN BATTLE =====
async function openItemMenu() {
  const items = INVENTORY.filter(item => item.amount > 0);
  
  if (items.length === 0) {
    renderBattleLog('No items available!');
    return;
  }
  
  // Use first available item
  const itemResult = await makeRequest(`/battle/use-item?itemIndex=${items[0].index}`, { method: 'POST' });
  
  if (itemResult) {
    currentBattleState = itemResult.battleState;
    renderBattleLog(itemResult.message);
    renderBattleArena();
    await renderBattleSkills();
  }
}

// ===== CAPTURE POKEMON =====
async function capturePokemonBattle() {
  const captureResult = await makeRequest('/battle/capture', { method: 'POST' });
  
  if (captureResult && captureResult.result) {
    renderBattleLog(captureResult.result.message);
    
    if (captureResult.result.captured) {
      renderBattleLog(`Captured ${currentBattleState.enemyPokemon.name}!`);
      await endBattle('🎉 Pokemon captured!');
    } else {
      renderBattleLog('Capture failed! The Pokemon broke free!');
    }
  }
}

// ===== END BATTLE =====
async function endBattle(message) {
  battleActive = false;
  
  const endResponse = await makeRequest('/battle/end', { method: 'POST' });
  
  renderBattleLog(message);
  
  // Determine win/loss
  const isVictory = message.includes('Menang') || message.includes('Captured');
  localStorage.setItem('lastBattleWin', isVictory);
  localStorage.setItem('lastBattleMessage', message);
  localStorage.setItem('lastBattleTurns', turnCount);

  // Simpan data Pokemon (nama & type untuk sprite) sebelum currentBattleState hilang
  if (currentBattleState) {
    if (currentBattleState.playerPokemon) {
      localStorage.setItem('lastPlayerPokemon', JSON.stringify({
        name: currentBattleState.playerPokemon.name,
        type: currentBattleState.playerPokemon.type
      }));
    }
    if (currentBattleState.enemyPokemon) {
      localStorage.setItem('lastEnemyPokemon', JSON.stringify({
        name: currentBattleState.enemyPokemon.name,
        type: currentBattleState.enemyPokemon.type
      }));
    }
  }

  // Akumulasi statistik total battle & menang
  const totalBattles = parseInt(localStorage.getItem('totalBattles') || '0', 10) + 1;
  const totalWins = parseInt(localStorage.getItem('totalWins') || '0', 10) + (isVictory ? 1 : 0);
  localStorage.setItem('totalBattles', totalBattles);
  localStorage.setItem('totalWins', totalWins);
  
  setTimeout(() => goTo('battle-result'), 2000);
}

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

// ===== RENDER INVENTORY =====
function renderInventory() {
  const grid = document.querySelector('.inventory-grid');
  if (!grid || INVENTORY.length === 0) return;
  
  grid.innerHTML = INVENTORY.map(item => `
    <div class="item-card">
      <div class="item-icon" style="background:rgba(124,58,237,.15)">${item.icon}</div>
      <div>
        <div class="item-name">${item.name}</div>
        <div class="item-desc">${item.description}</div>
        <div class="item-count">${item.amount}x</div>
      </div>
    </div>
  `).join('');
}

// ===== RENDER BATTLE RESULT =====
// NOTE: renderBattleResult() didefinisikan di inline <script> battle-result.html
// (versi lengkap: ambil pokemon, turns, winrate dari localStorage).
// Sengaja TIDAK didefinisikan di sini lagi supaya tidak ada 2 deklarasi
// function dengan nama sama yang saling override secara implisit.

// ===== BATTLE LOGIC =====
async function doAttack(skillIndex) {
    return await makeRequest(`/battle/attack?skillIndex=${skillIndex}`, { method: 'POST' });
}

async function doUseItem(itemIndex) {
    return await makeRequest(`/battle/use-item?itemIndex=${itemIndex}`, { method: 'POST' });
}

async function doCapture() {
    return await makeRequest('/battle/capture', { method: 'POST' });
}

async function doSwitch(pokemonIndex) {
    return await makeRequest(`/pokemon/switch?index=${pokemonIndex}`, { method: 'POST' });
}

async function getBattleState() {
    return await makeRequest('/battle/state');
}