// ===== API BASE URL =====
const API_BASE = '/api/game';

// ===== UTILITY FUNCTIONS =====
function displayResponse(data) {
    const responseElement = document.getElementById('apiResponse');
    responseElement.textContent = JSON.stringify(data, null, 2);
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
        displayResponse({ error: error.message });
        return null;
    }
}

// ===== INITIALIZE PAGE =====
document.addEventListener('DOMContentLoaded', function() {
    loadSkills();
    attachEventListeners();
});

// ===== LOAD SKILLS TABLE =====
async function loadSkills() {
    const data = await makeRequest('/skills');
    
    if (data && data.skills) {
        const skillsTableBody = document.getElementById('skillsTableBody');
        skillsTableBody.innerHTML = '';
        
        data.skills.forEach(skill => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${skill.name}</td>
                <td><span class="badge bg-info">${skill.type}</span></td>
                <td>${skill.power}</td>
            `;
            skillsTableBody.appendChild(row);
        });
    }
}

// ===== ATTACH EVENT LISTENERS =====
function attachEventListeners() {
    // Refresh Skills
    document.getElementById('refreshSkills').addEventListener('click', loadSkills);
    
    // Initialize Game
    document.getElementById('initForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const playerName = document.getElementById('playerName').value;
        await makeRequest(`/init?playerName=${encodeURIComponent(playerName)}`, { method: 'POST' });
    });
    
    // Start Battle
    document.getElementById('startBattle').addEventListener('click', async () => {
        await makeRequest('/battle/start', { method: 'POST' });
    });
    
    // Get Battle State
    document.getElementById('battleState').addEventListener('click', async () => {
        await makeRequest('/battle/state');
    });
    
    // End Battle
    document.getElementById('endBattle').addEventListener('click', async () => {
        await makeRequest('/battle/end', { method: 'POST' });
    });
    
    // Player Attack
    document.getElementById('attackForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const skillIndex = document.getElementById('skillIndex').value;
        await makeRequest(`/battle/attack?skillIndex=${skillIndex}`, { method: 'POST' });
    });
    
    // Use Item
    document.getElementById('useItemForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const itemIndex = document.getElementById('itemIndex').value;
        await makeRequest(`/battle/use-item?itemIndex=${itemIndex}`, { method: 'POST' });
    });
    
    // Switch Pokemon
    document.getElementById('switchForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const switchIndex = document.getElementById('switchIndex').value;
        await makeRequest(`/pokemon/switch?index=${switchIndex}`, { method: 'POST' });
    });
    
    // Capture Pokemon
    document.getElementById('capturePokemon').addEventListener('click', async () => {
        await makeRequest('/battle/capture', { method: 'POST' });
    });
    
    // Get Collection
    document.getElementById('getCollection').addEventListener('click', async () => {
        await makeRequest('/pokemon/collection');
    });
    
    // Health Check
    document.getElementById('healthCheck').addEventListener('click', async () => {
        await makeRequest('/health');
    });
}
